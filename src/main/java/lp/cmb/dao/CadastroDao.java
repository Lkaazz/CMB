package lp.cmb.dao;

import lp.cmb.BDConexaoMySQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 *
 * @author everton
 */
public class CadastroDao {

    public boolean cadastro(String email, String senha, String nome, LocalDate nascimento) {

        // Criar uma conexão com o banco de dados
        Connection conn = new BDConexaoMySQL().getConnection();

        // Preparar a consulta
        String sql = "INSERT INTO passageiros (email, senha, nome, nascimento) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, senha);
            ps.setString(3, nome);
            ps.setString(4, String.valueOf(nascimento));

            // Executar a consulta
            int rowsAffected = ps.executeUpdate();

            // Verificar se o registro foi inserido com sucesso
            if (rowsAffected > 0) {
                System.out.println("Cadastro realizado com sucesso!");
                return true;
            } else {
                System.out.println("Falha ao cadastrar. Nenhum registro inserido.");
            }

            ps.close();
            conn.close();

        } catch (SQLException ex) {
            System.out.println("Erro ao executar a consulta: " + ex.getMessage());
        }
        return false;
    }
}
