package lp.cmb.dao;

import java.sql.Connection;
import lp.cmb.BDConexaoMySQL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import lp.cmb.model.Reports;

public class FeedbacksDao {
    Connection conn = new BDConexaoMySQL().getConnection();
        
    String query1 = "SELECT COUNT(*) FROM problemas_reportados"; // Retorna quantidade total de feedbacks
    String query2 = "SELECT lo.numero AS linhas_com_mais_problemas\n" +
                    "FROM problemas_reportados pr\n" +
                    "JOIN linhas_onibus lo ON pr.linha_id = lo.linha_id\n" +
                    "GROUP BY pr.linha_id\n" +
                    "ORDER BY COUNT(pr.linha_id) DESC\n" +
                    "LIMIT 1"; // Retorna número da linha que mais possui feedback
    String query3 = "SELECT COUNT(*) AS quantidade_tipo_problema\n" +
                    "FROM problemas_reportados\n" +
                    "WHERE tipo_problema_id = ?"; // Retorna quantidade de feedbacks de um determinado tipo
    String query4 = "SELECT COUNT(*) AS quantidade_linha_id\n" +
                    "FROM problemas_reportados\n" +
                    "WHERE linha_id = ?"; // Retorna quantidade de feedbacks de uma determinada linha
    String query5 = "SELECT p.nome, pr.descricao, pr.data_hora " +
                    "FROM problemas_reportados pr " +
                    "JOIN passageiros p ON pr.passageiro_id = p.passageiro_id " +
                    "JOIN linhas_onibus lo ON pr.linha_id = lo.linha_id " +
                    "WHERE lo.numero = ? " +
                    "ORDER BY pr.data_hora DESC";


    
     public int obterQuantidadeFeedbacks() {
        int quantidadeFeedbacks = 0;
        try (PreparedStatement stmt = conn.prepareStatement(query1);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                quantidadeFeedbacks = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(FeedbacksDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return quantidadeFeedbacks; // Retorna quantidade total de feedbacks
    }
    public String obterLinhaMaisFeedback() {
        String linhaMaisFeedback = "";
        try (PreparedStatement stmt = conn.prepareStatement(query2);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                linhaMaisFeedback = rs.getString(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(FeedbacksDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return linhaMaisFeedback;
    }
    
        public int obterQuantidadePorTipoProblema(int tipoProblemaId) {
            int quantidadeTipoProblema = 0;
            try (PreparedStatement stmt = conn.prepareStatement(query3)) {
                stmt.setInt(1, tipoProblemaId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        quantidadeTipoProblema = rs.getInt("quantidade_tipo_problema");
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace(); // Lide com a exceção apropriadamente
            }
        return quantidadeTipoProblema;
    }
    public int obterQuantidadePorLinha(int linhaId) {
        int quantidadeLinhaId = 0;
        try (PreparedStatement stmt = conn.prepareStatement(query4)) {
            stmt.setInt(1, linhaId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    quantidadeLinhaId = rs.getInt("quantidade_linha_id");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(FeedbacksDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return quantidadeLinhaId;
    }
   
     // Exemplo de um novo método em FeedbacksDao
public List<Reports> obterFeedbacksLinha(String numeroLinha) {
    List<Reports> feedbacks = new ArrayList<>();

    try (PreparedStatement stmt = conn.prepareStatement(query5)) {
        stmt.setString(1, numeroLinha);
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String nome = rs.getString("nome");
                String descricao = rs.getString("descricao");
                String dataHora = rs.getString("data_hora");

                Reports reports = new Reports(nome, descricao, dataHora);
                feedbacks.add(reports);
            }
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }

    return feedbacks;
}


    
 
    
    public void enviaFeedback(int tipoProblema, String descricao, String numeroLinha, String emailPassageiroLogado) {
    // Obter o passageiro_id com base no e-mail do passageiro logado
    String sqlObterPassageiroId = "SELECT passageiro_id FROM passageiros WHERE email = ?";
    int passageiroId;

    try (PreparedStatement stmtObterPassageiroId = conn.prepareStatement(sqlObterPassageiroId)) {
        stmtObterPassageiroId.setString(1, emailPassageiroLogado);
        ResultSet rsObterPassageiroId = stmtObterPassageiroId.executeQuery();

        if (rsObterPassageiroId.next()) {
            passageiroId = rsObterPassageiroId.getInt("passageiro_id");
        } else {
            // Trate o caso em que o passageiro não é encontrado pelo e-mail
            System.out.println("Passageiro não encontrado pelo e-mail.");
            return;
        }
    } catch (SQLException e) {
        e.printStackTrace(); // Trate a exceção de acordo com a necessidade do seu projeto
        return;
    }

    // Encontrar o ID da linha com base no número da linha
    String sqlObterLinhaId = "SELECT linha_id FROM linhas_onibus WHERE numero = ?";
    int linhaId;

    try (PreparedStatement stmtObterLinhaId = conn.prepareStatement(sqlObterLinhaId)) {
        stmtObterLinhaId.setString(1, numeroLinha);
        ResultSet rsObterLinhaId = stmtObterLinhaId.executeQuery();

        if (rsObterLinhaId.next()) {
            linhaId = rsObterLinhaId.getInt("linha_id");
        } else {
            // Trate o caso em que a linha não é encontrada pelo número
            System.out.println("Linha não encontrada pelo número.");
            return;
        }
    } catch (SQLException e) {
        e.printStackTrace(); // Trate a exceção de acordo com a necessidade do seu projeto
        return;
    }

    // Incluir o feedback na tabela
    String sql = "INSERT INTO problemas_reportados (descricao, tipo_problema_id, linha_id, passageiro_id, data_hora) VALUES (?, ?, ?, ?, CURDATE())";

    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, descricao);
        stmt.setInt(2, tipoProblema);
        stmt.setInt(3, linhaId);
        stmt.setInt(4, passageiroId);
        stmt.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace(); // Trate a exceção de acordo com a necessidade do seu projeto
    }
}


           
}
