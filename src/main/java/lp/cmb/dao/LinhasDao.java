
package lp.cmb.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import lp.cmb.BDConexaoMySQL;
import lp.cmb.model.Linhas;


public class LinhasDao {
    public List<Linhas> getListaLinhas() {

        //Cria conexão com o banco de dados
        Connection conn = new BDConexaoMySQL().getConnection();

        //Instanciar uma lista de empregados
        List<Linhas> listLinhas = new ArrayList<>();

        //Prepara a consulta
        String sql = "SELECT * FROM linhas_onibus";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);

            //Executa e obtém o resultado
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                
                String numeroLinhas = rs.getString("numero");
                String destino = rs.getString("descricao");
            
                listLinhas.add(new Linhas(numeroLinhas, destino));
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(LinhasDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listLinhas;
    }
}
