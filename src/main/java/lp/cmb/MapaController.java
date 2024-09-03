/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package lp.cmb;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MapaController implements Initializable {
    @FXML
    private AnchorPane aPane;
    @FXML
    private WebView wvMapa;  
    private HomeController homeController;
    
    public MapaController() {
        // Construtor padrão vazio
    }
    
    public MapaController(HomeController homeController) {
        this.homeController = homeController;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
    
    public void mostraMapa() {
        
         try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("mapa.fxml"));
        Parent root = loader.load();
        MapaController mapaController = loader.getController();

        Stage mapaStage = new Stage();
        mapaStage.setTitle("Mapa");
        mapaStage.setScene(new Scene(root));
        mapaStage.initModality(Modality.APPLICATION_MODAL);
        mapaStage.show();

        String link = obterLinkParaNumeroLinha();
        WebEngine webEngine = mapaController.wvMapa.getEngine();
        webEngine.load(link);
    } catch (IOException e) {
        e.printStackTrace();
    }
    }
    
    private String obterLinkParaNumeroLinha() {
        String numeroLinha = homeController.getNumeroLinhaSelecionada();
        
        switch (numeroLinha) {
            case "225":
                return "https://www.google.com/maps/d/u/0/embed?mid=1jN3VsIkkdOCo2A_9Y7e0EFgrGFnZFQY&ehbc=2E312F&noprof=1";
            case "226":
                return "https://www.google.com/maps/d/u/0/edit?mid=18lFeXVMiGPj3RIN2N_IUUckeoU_F08M&usp=sharing";
            case "210":
                return "https://www.google.com/maps/d/u/0/embed?mid=1hHBBgn5LlK6K3tR-CrAbtivSDANoRcw&ehbc=2E312F&noprof=1";
            case "213":
                return "https://www.google.com/maps/d/u/0/embed?mid=1wwp8U4Gn5EJBFE3Pd0vm14Bjk27tfHE&ehbc=2E312F&noprof=1";
            case "227":
                return "https://www.google.com/maps/d/u/0/embed?mid=1QGn5NH-C1pjvfjFZoP6XbqyrGnFi1aM&ehbc=2E312F&noprof=1";
            case "BRT20":
                return "https://www.google.com/maps/d/u/0/embed?mid=1GtAbffAmToH-lNHWwwRaK4cv9Rsgoio&ehbc=2E312F&noprof=1";
            default:
                return "https://link-padrão-se-numero-linha-nao-estiver-mapeado";
        }
    }
    
}

