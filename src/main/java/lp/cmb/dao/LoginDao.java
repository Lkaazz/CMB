package lp.cmb.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import lp.cmb.BDConexaoMySQL;
/**
 *
 * @author everton
 */
public class LoginDao {

    public boolean login(String email, String senha) {
        // Criar uma conexão com o banco de dados
        Connection conn = new BDConexaoMySQL().getConnection();

        // Preparar a consulta para verificar a existência da conta
        String verificaContaSQL = "SELECT COUNT(*) FROM passageiros WHERE email = ?";
        try {
            PreparedStatement psVerificaConta = conn.prepareStatement(verificaContaSQL);
            psVerificaConta.setString(1, email);

            ResultSet rsVerificaConta = psVerificaConta.executeQuery();
            rsVerificaConta.next();
            int count = rsVerificaConta.getInt(1);

            rsVerificaConta.close();
            psVerificaConta.close();

            // Se a conta não existir, retornar falso
            if (count == 0) {
                System.out.println("Conta não encontrada.");
                return false;
            }
        } catch (SQLException ex) {
            System.out.println(ex);
            return false;
        }

        // Preparar a consulta para verificar a senha
        String verificaSenhaSQL = "SELECT * FROM passageiros WHERE email = ? AND senha = ?";
        try {
            PreparedStatement psVerificaSenha = conn.prepareStatement(verificaSenhaSQL);
            psVerificaSenha.setString(1, email);
            psVerificaSenha.setString(2, senha);
            
            // Executar a consulta e obter o resultado
            ResultSet rsVerificaSenha = psVerificaSenha.executeQuery();

            // Se a consulta retornar algum resultado, significa que o login é bem-sucedido
            if (rsVerificaSenha.next()) {
                String u = rsVerificaSenha.getString("email");
                String s = rsVerificaSenha.getString("senha");
                System.out.println("email: " + u);
                System.out.println("senha: " + s);

                rsVerificaSenha.close();
                psVerificaSenha.close();
                conn.close();

                return true;
            } else {
                System.out.println("Senha incorreta.");
                return false;
            }
        } catch (SQLException ex) {
            System.out.println(ex);
            return false;
        }
        
        
        
    }


}
