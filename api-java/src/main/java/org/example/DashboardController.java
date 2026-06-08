package org.example;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Controller
public class DashboardController {

    // 1. CARREGA A PÁGINA INICIAL
    @GetMapping("/")
    public String carregarPaginaInicial(Model model) {
        List<String> regioes = new ArrayList<>();
        List<String> produtos = new ArrayList<>();
        List<String> statusList = new ArrayList<>();

        try {
            String url = "jdbc:h2:mem:bancohibrido;DB_CLOSE_DELAY=-1";
            Connection conexao = DriverManager.getConnection(url, "sa", "");
            Statement stmt = conexao.createStatement();
            stmt.execute("RUNSCRIPT FROM 'dados_dashboard.sql'");

            ResultSet rs1 = stmt.executeQuery("SELECT DISTINCT regiao FROM transacoes ORDER BY regiao");
            while (rs1.next()) regioes.add(rs1.getString("regiao"));

            ResultSet rs2 = stmt.executeQuery("SELECT DISTINCT produto FROM transacoes ORDER BY produto");
            while (rs2.next()) produtos.add(rs2.getString("produto"));

            ResultSet rs3 = stmt.executeQuery("SELECT DISTINCT status FROM transacoes ORDER BY status");
            while (rs3.next()) statusList.add(rs3.getString("status"));

            conexao.close();
        } catch (Exception e) { e.printStackTrace(); }

        model.addAttribute("transacoes", new ArrayList<>());
        model.addAttribute("totalRegiao", 0.0);
        model.addAttribute("listaDeRegioes", regioes);
        model.addAttribute("listaDeProdutos", produtos);
        model.addAttribute("listaDeStatus", statusList);

        return "index";
    }

    // 2. ROTA DA TABELA (HTMX)
    @GetMapping("/filtrar")
    public String filtrarTabela(
            @RequestParam(defaultValue = "TODOS") String regiao,
            @RequestParam(defaultValue = "TODOS") String produto,
            @RequestParam(defaultValue = "TODOS") String status,
            Model model) {

        List<Transacao> lista = new ArrayList<>();
        double total = 0.0;
        try {
            Connection conexao = DriverManager.getConnection("jdbc:h2:mem:bancohibrido;DB_CLOSE_DELAY=-1", "sa", "");
            Statement stmt = conexao.createStatement();

            String sql = "SELECT * FROM transacoes WHERE 1=1";
            if (!regiao.equals("TODOS")) sql += " AND regiao = '" + regiao + "'";
            if (!produto.equals("TODOS")) sql += " AND produto = '" + produto + "'";
            if (!status.equals("TODOS")) sql += " AND status = '" + status + "'";

            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Transacao t = new Transacao(rs.getString("mes"), rs.getString("regiao"), rs.getString("produto"), rs.getString("status"), rs.getDouble("valor"));
                lista.add(t);
                total += t.getValor();
            }
            conexao.close();
        } catch (Exception e) { e.printStackTrace(); }

        model.addAttribute("transacoes", lista);
        model.addAttribute("totalRegiao", total);
        return "index :: fragmento-tabela";
    }

    // 3. ROTA DA API (JSON PARA O PYTHON)
    @GetMapping("/api/dados")
    @ResponseBody
    public List<Transacao> obterDadosPuros(@RequestParam(defaultValue = "TODOS") String regiao) {
        List<Transacao> lista = new ArrayList<>();
        try {
            Connection conexao = DriverManager.getConnection("jdbc:h2:mem:bancohibrido;DB_CLOSE_DELAY=-1", "sa", "");
            Statement stmt = conexao.createStatement();
            String sql = "SELECT * FROM transacoes WHERE 1=1";
            if (!regiao.equals("TODOS")) sql += " AND regiao = '" + regiao + "'";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                lista.add(new Transacao(rs.getString("mes"), rs.getString("regiao"), rs.getString("produto"), rs.getString("status"), rs.getDouble("valor")));
            }
            conexao.close();
        } catch (Exception e) { e.printStackTrace(); }
        return lista;
    }

    public static class Transacao {
        public String mes, regiao, produto, status; public double valor;
        public Transacao(String m, String r, String p, String s, double v) { mes=m; regiao=r; produto=p; status=s; valor=v; }
        public String getMes() { return mes; }
        public String getRegiao() { return regiao; }
        public String getProduto() { return produto; }
        public String getStatus() { return status; }
        public double getValor() { return valor; }
    }
}