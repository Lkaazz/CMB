package lp.cmb;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import lp.cmb.model.Linhas;
import lp.cmb.model.Reports;


public class ItemLFController implements Initializable {
    @FXML private Text txDescricao;
    @FXML private Text txData;
    @FXML private Text txUsuario;
    @FXML private HBox hBox1;
    private Reports reports;
    
    public void setLinhas(Reports reports) {
        this.reports = reports;
        txUsuario.setText(reports.getNomePassageiro() + "");
        txDescricao.setText(reports.getDescricao());
        txData.setText(reports.getDataHora());
     
        //Efeitos quando passar o mouse
        hBox1.setOnMouseEntered(event -> {
            hBox1.setStyle("-fx-background-color : #797070");
        });
        hBox1.setOnMouseExited(event -> {
            hBox1.setStyle("-fx-background-color : #ffffff");
        });
        
    }
    public void setNome(String nome) {
        txUsuario.setText(nome);
    }

    public void setDescricao(String descricao) {
        txDescricao.setText(descricao);
    }

    public void setData(String data) {
        txData.setText(data);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
        
    }    
    
}
