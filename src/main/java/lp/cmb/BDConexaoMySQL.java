package lp.cmb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BDConexaoMySQL extends BDConexao {

    public BDConexaoMySQL() {

        this.driver = "com.mysql.cj.jdbc.Driver";
        this.porta = 3306;
        this.servidor = "localhost";
        this.bd = "cmb";
        this.usuario = "root";
        this.senha = "root";
    }

    @Override
    public Connection getConnection() {

        try {

            Class.forName(driver);
            con = DriverManager.getConnection(getURL(), usuario, senha);

        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(BDConexaoMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }

        return con;

    }

    @Override
    public String getURL() {
        return "jdbc:mysql://" + this.servidor + ":" + this.porta + "/" + this.bd
                + "?useTimezone=true&serverTimezone=UTC";
    }

}
