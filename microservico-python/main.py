from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from fastapi.responses import HTMLResponse
import pandas as pd
import plotly.express as px
import requests  # A biblioteca que conecta o Python ao Java

app = FastAPI()

# Mantém as portas abertas para o navegador (HTMX) conseguir ler o gráfico
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"], 
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

@app.get("/gerar-grafico-plotly", response_class=HTMLResponse)
def gerar_grafico(regiao: str = "TODOS"):
    
    try:
        # 1. A MÁGICA DA API: O Python pede os dados seguros ao Java
        url_java = f"http://localhost:8080/api/dados?regiao={regiao}"
        resposta = requests.get(url_java)
        dados_json = resposta.json()  # Converte a resposta do Java para um formato utilizável
        
        # 2. O Pandas transforma o JSON numa tabela virtual de alta performance
        df_banco = pd.DataFrame(dados_json)
        
    except Exception as e:
        return f"<p style='color: red;'>Erro de conexão com o Java: {e}</p>"
    
    # Se o banco retornar vazio, avisa o usuário
    if df_banco.empty:
        return "<p style='color: gray; font-style: italic;'>Sem dados para a região selecionada.</p>"
        
    # 3. MATEMÁTICA: Agrupa os valores por mês
    df_agrupado = df_banco.groupby("mes", as_index=False)["valor"].sum()
    
    # 4. DESENHO: Cria o gráfico profissional com Plotly
    fig = px.bar(
        df_agrupado, 
        x="mes", 
        y="valor", 
        title=f"Análise de Faturamento (Motor Híbrido) - Região: {regiao}",
        template="plotly_white",
        labels={"mes": "Mês", "valor": "Faturamento (R$)"}
    )
    fig.update_traces(marker_color='#3b82f6')

    # Devolve APENAS o HTML do gráfico para o HTMX injetar na tela
    html_grafico = fig.to_html(full_html=False, include_plotlyjs='cdn')
    return html_grafico