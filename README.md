Dashboard Híbrido: Java Spring Boot + Python FastAPI & HTMX
Este é um projeto de dashboard analítico interativo que utiliza uma arquitetura híbrida e distribuída. O sistema combina a robustez e segurança do Java (Spring Boot) para a gestão de dados com a eficiência do Python (FastAPI & Plotly) para renderização de gráficos complexos, utilizando o HTMX no front-end para atualizações dinâmicas e em tempo real sem a necessidade de recarregamento de página (refresh).

 Arquitetura do Sistema
O projeto funciona como um ecossistema de microsserviços integrados diretamente na interface do usuário:

Front-end (HTML5 + HTMX): A interface faz requisições assíncronas paralelas ao alterar os filtros da tela. Graças ao HTMX, os retalhos de HTML retornados pelos servidores substituem os componentes da tela instantaneamente.

Microsserviço Java (Porta 8080):

Conecta-se ao banco de dados H2 em memória.

Atua como um "escudo" seguro para o banco de dados.

Renderiza a tabela de dados via Thymeleaf (HTML) para o navegador.

Disponibiliza uma API REST (/api/dados) que fornece dados puros em formato JSON para o microsserviço Python.

Microsserviço Python (Porta 8000):

Consome os dados puros em JSON direto da API Java (comunicação servidor-para-servidor).

Processa as informações e gera gráficos estatísticos interativos utilizando Plotly e Pandas.

Retorna o componente do gráfico diretamente em formato HTML para ser injetado na tela.

 Tecnologias Utilizadas
Back-end & Banco de Dados
Java 17

Spring Boot 3.2.5 (Web, Thymeleaf)

Maven (Gerenciador de dependências Java)

H2 Database (Banco de dados SQL em memória)

Análise de Dados & Gráficos
Python 3.x

FastAPI + Uvicorn

Plotly & Pandas

Requests (Para consumo da API Java)

Front-end
HTMX (Superpoderes para o HTML via requisições AJAX nativas)

 Como Rodar o Projeto
Para executar o dashboard completo na sua máquina, siga os passos abaixo para iniciar ambos os servidores.

1. Pré-requisitos
Java JDK 17 instalado.

Python 3 instalado.

Uma IDE (recomendado IntelliJ IDEA para o Java e VS Code para o Python).

2. Configurando e Rodando o Servidor Java (Spring Boot)
Abra a pasta raiz do projeto no IntelliJ IDEA.

Certifique-se de que o Project SDK / Language Level está configurado para o Java 17.

Atualize as dependências do Maven (Clique no ícone do Maven no canto superior direito e selecione Reload All Maven Projects).

Abra o arquivo src/main/java/org/example/Main.java.

Clique no botão verde de Run (▶️).

O servidor Java estará rodando em: http://localhost:8080

3. Configurando e Rodando o Servidor Python (FastAPI)
Abra o terminal na pasta do microsserviço Python (ex: microservico-python).

Ative o seu ambiente virtual (venv):

Windows (PowerShell): .\venv\Scripts\Activate.ps1

Linux/Mac: source venv/bin/activate

Certifique-se de ter as dependências instaladas:

Bash
pip install -r requirements.txt
Inicie o servidor Uvicorn executando o comando:

Bash
python -m uvicorn main:app --reload --port 8000
O microsserviço Python estará rodando e pronto na porta 8000.

 Como Testar
Com os dois servidores rodando simultaneamente:

Abra o seu navegador e acesse http://localhost:8080.

Altere os filtros de Região, Produto ou Status na tela.

Observe que tanto a tabela (gerada pelo Java) quanto o gráfico (gerado pelo Python) se atualizam de forma independente e instantânea via HTMX, sem recarregar a página.

 Segurança & Eficiência
Blindagem de Dados: O navegador do usuário nunca acessa o banco SQL diretamente e nunca recebe JSONs expostos. Toda a comunicação de dados puros é feita de forma interna entre o Python e o Java. O cliente final recebe apenas HTML limpo e renderizado.

Divisão de Trabalho: O Java cuida da persistência e segurança dos dados, enquanto o Python foca exclusivamente no processamento lógico e visual dos gráficos.