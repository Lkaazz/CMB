package lp.cmb.dao;

import lp.cmb.BDConexaoMySQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author everton
 */
public class UpdatePerfilDao {

    public boolean atualizarPerfil(String email, String nome, String nascimento, String senha) {
        // Criar uma conexão com o banco de dados
        Connection conn = new BDConexaoMySQL().getConnection();

        // Preparar a consulta para realizar o update
        String updatePerfilSQL = "UPDATE passageiros SET nome = ?, nascimento = ?, senha = ? WHERE email = ?";
        try {
            PreparedStatement psUpdatePerfil = conn.prepareStatement(updatePerfilSQL);
            psUpdatePerfil.setString(1, nome);
            psUpdatePerfil.setString(2, nascimento);
            psUpdatePerfil.setString(3, senha);
            psUpdatePerfil.setString(4, email);

            // Executar o update e verificar se foi bem-sucedido
            int linhasAfetadas = psUpdatePerfil.executeUpdate();

            // Fechar recursos
            psUpdatePerfil.close();
            conn.close();

            // Se o update afetou alguma linha, retornar true, indicando sucesso
            return linhasAfetadas > 0;
        } catch (SQLException ex) {
            System.out.println("Erro ao atualizar perfil: " + ex);
            return false;
        }
    }

    // Dentro da classe UpdatePerfilDao

    public boolean verificarSenhaAntiga(String email, String senhaAntiga) {
        Connection conn = new BDConexaoMySQL().getConnection();

        // Preparar a consulta para verificar a senha antiga
        String verificaSenhaAntigaSQL = "SELECT COUNT(*) FROM passageiros WHERE email = ? AND senha = ?";
        try {
            PreparedStatement psVerificaSenhaAntiga = conn.prepareStatement(verificaSenhaAntigaSQL);
            psVerificaSenhaAntiga.setString(1, email);
            psVerificaSenhaAntiga.setString(2, senhaAntiga);

            ResultSet rsVerificaSenhaAntiga = psVerificaSenhaAntiga.executeQuery();
            rsVerificaSenhaAntiga.next();
            int count = rsVerificaSenhaAntiga.getInt(1);

            rsVerificaSenhaAntiga.close();
            psVerificaSenhaAntiga.close();

            // Se a senha antiga estiver correta, retornar true
            return count > 0;
        } catch (SQLException ex) {
            System.out.println("Erro ao verificar senha antiga: " + ex);
            return false;
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println("Erro ao fechar a conexão: " + e);
            }
        }
    }

}
