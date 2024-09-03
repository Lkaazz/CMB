/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lp.cmb;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import lp.cmb.model.Linhas;


public class itemController implements Initializable{
    @FXML private Label lbLinha;
    @FXML private Label lbDestino;
    @FXML private HBox hBox;
    private Linhas linhas;
    @FXML private Pane painelLinhasSelect;
    private ItemListener listener;
    private HomeController homeController;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
    }
    public interface ItemListener {
    void selecionaLinha(String numeroLinha);
    }
    @FXML
    public void mostrarPainelEsp() {
        if (listener  != null) {
        listener.selecionaLinha(getNumeroLinha());
    }
    }
    public String getNumeroLinha() {
        return lbLinha.getText();
    }
    public void setLinhas(Linhas linhas) {
        this.linhas = linhas;
        lbLinha.setText(linhas.getNumeroLinhas() + "");
        lbDestino.setText(linhas.getDestino());
        
        //Efeitos quando passar o mouse
        
        hBox.setStyle(
        "-fx-background-color: #ffffff;"  // Cor de fundo padrão
                  // Raio da borda (bordas arredondadas)
                
    );
        
        hBox.setOnMouseEntered(event -> {
            hBox.setStyle("-fx-background-color : #797070");
        });
        hBox.setOnMouseExited(event -> {
            hBox.setStyle("-fx-background-color : #ffffff");
        });
    }
    public void setItemListener(ItemListener listener) {
        this.listener = listener;
    }
    
    public void setHomeController(HomeController controller) {
        this.homeController = controller;
    }
}
    
