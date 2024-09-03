package lp.cmb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import lp.cmb.dao.FeedbacksDao;
import lp.cmb.dao.LinhasDao;
import lp.cmb.dao.UpdatePerfilDao;
import lp.cmb.itemController.ItemListener;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import lp.cmb.dao.LoginDao;
import lp.cmb.dao.CadastroDao;
import lp.cmb.model.Linhas;
import java.time.LocalDate;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import lp.cmb.model.Reports;
import javafx.scene.control.DatePicker;


public class HomeController implements Initializable, ItemListener {

    @FXML private Pane painelFeedbacks;
    @FXML private Pane painelLinhas;
    @FXML private Pane painelHome;
    @FXML private Pane painelLinhasSelect;
    @FXML private TextField tfPesquisar;
    @FXML private TextField tfDescricao;
    @FXML private TextField tfSelecionaLinha;
    @FXML private TextField tfEmail;
    @FXML private TextField tfSenha;
    @FXML private PieChart grafFeedback;
    @FXML private Pane paneLogin;
    @FXML private Pane paneCadastro;
    private FeedbacksDao FeedbacksDao;
    @FXML private VBox vboxItems;
    @FXML private VBox vbLinhasEsp;
    @FXML private Text txLinhaMais;
    @FXML private Text txLot;
    @FXML private Text txSeg;
    @FXML private Text txAtr;
    @FXML private ComboBox<String> cbLinhas;
    @FXML private Text txAci;
    @FXML private Text txTotal;
    @FXML private TextField tfNomeCadastro;
    @FXML private TextField tfEmailCadastro;
    @FXML private DatePicker dateNascimentoCadastro;
    @FXML private PasswordField pfSenhaCadastro;
    @FXML private PasswordField pfConfirmarSenhaCadastro;
    @FXML private CheckBox checkboxTermosCadastro;
    @FXML
    private Pane painelAtualizarPerfil;
    @FXML
    private TextField tfAlterarEmail;
    @FXML
    private TextField tfAlterarNome;
    @FXML
    private DatePicker dateAlterarNascimento;
    @FXML
    private PasswordField pfAlterarSenha;
    @FXML private PasswordField pfSenhaAntigaAlterar;
    @FXML
    private PasswordField pfConfirmarSenhaAlterar;
    private String emailPassageiroLogado;


    private int tipoProblema;
    private List<Linhas> listLinhas;
    private List<Reports> listReports;

    @Override
    public void selecionaLinha(String numeroLinha) {
        if (painelLinhasSelect != null) {
            painelLinhasSelect.setVisible(true);
            tfSelecionaLinha.setText(numeroLinha); // Define o valor do tfSelecionaLinha com o número da linha
        }
        detalhesLinha();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadLinhas();
        addDados();
        cbLinhas.getItems().addAll("225", "226", "210", "213", "227", "BRT20");


        tfPesquisar.textProperty()
                .addListener((observable, oldValue, newValue) -> {
                    filtrarItens(newValue);
                });
    }
    @FXML
    public void logar() throws IOException {
        String email = tfEmail.getText();
        String senha = tfSenha.getText();

        // Verifica se os campos de email e senha
        if (email == null || email.isEmpty() || senha == null || senha.isEmpty()) {
            exibirAlertaErro("Erro de Login", "Por favor, insira email e senha.");
            return;
        }

        // Acessa o banco para verificar usuário e senha
        LoginDao ldao = new LoginDao();
        boolean res = ldao.login(email, senha);

        if (res) {
            this.emailPassageiroLogado = email;
            paneLogin.setVisible(false);
            System.out.println("E-mail do passageiro logado: " + this.emailPassageiroLogado);
        } else {
            exibirAlertaErro("Erro de Login", "Email e/ou senha incorretos.");
        }
    }

    public String getEmailPassageiroLogado() {
        return this.emailPassageiroLogado;
    }
    public String getNumeroLinhaSelecionada() {
        return tfSelecionaLinha.getText();
    }
    public void cadastrar() throws IOException {
        String emailCadastro = tfEmailCadastro.getText();
        String senhaCadastro = pfSenhaCadastro.getText();
        String confirmarSenha = pfConfirmarSenhaCadastro.getText();
        String nomeCadastro = tfNomeCadastro.getText();
        LocalDate nascimentoCadastro = dateNascimentoCadastro.getValue();


        if (emailCadastro == null || emailCadastro.isEmpty() ||
                senhaCadastro == null || senhaCadastro.isEmpty() ||
                confirmarSenha == null || confirmarSenha.isEmpty() ||
                nomeCadastro == null || nomeCadastro.isEmpty() ||
                nascimentoCadastro == null) {
            exibirAlertaErro("Erro de Cadastro", "Por favor, preencha todos os campos.");
            return;
        }
        // Verificar se a checkbox
        if (!checkboxTermosCadastro.isSelected()) {
            exibirAlertaErro("Erro de Cadastro", "Por favor, concorde com os termos de uso para cadastrar.");
            return;
        }

        // Verificar se a senha e a confirmação da senha são iguais
        if (!senhaCadastro.equals(confirmarSenha)) {
            exibirAlertaErro("Erro de Cadastro", "As senhas não coincidem. Por favor, insira senhas iguais.");
            return;
        }

        // Acessa o banco para cadastrar o novo passageiro
        CadastroDao cdao = new CadastroDao();
        boolean res = cdao.cadastro(emailCadastro, senhaCadastro, nomeCadastro, nascimentoCadastro);

        if (res) {
            exibirAlertaSucesso("Cadastro Realizado", "Cadastro realizado com sucesso!");

            tfEmailCadastro.clear();
            pfSenhaCadastro.clear();
            pfConfirmarSenhaCadastro.clear();
            tfNomeCadastro.clear();
            dateNascimentoCadastro.setValue(null); // Limpar a seleção da data
            fecharPainelCadastro();
        } else {
            exibirAlertaErro("Erro de Cadastro", "Falha ao cadastrar.");
        }
    }


    public void atualizarPerfil() throws IOException {
        String email = tfAlterarEmail.getText();
        String senhaAntiga = pfSenhaAntigaAlterar.getText();
        String novoNome = tfAlterarNome.getText();
        LocalDate novoNascimento = dateAlterarNascimento.getValue();
        String novaSenha = pfAlterarSenha.getText();
        String confirmarSenha = pfConfirmarSenhaAlterar.getText();

        // Verificar a senha antiga
        if (!verificarSenhaAntiga(email, senhaAntiga)) {
            exibirAlertaErro("Erro de Atualização", "Senha antiga incorreta.");
            return;
        }

        // Verificar se a senha nova e a confirmação da senha são iguais
        if (!novaSenha.equals(confirmarSenha)) {
            exibirAlertaErro("Erro de Atualização", "As senhas não coincidem. Por favor, insira senhas iguais.");
            return;
        }

        UpdatePerfilDao updatePerfilDao = new UpdatePerfilDao();
        boolean sucessoAtualizacao;

        if (novoNome != null && novoNascimento != null) {
            sucessoAtualizacao = updatePerfilDao.atualizarPerfil(email, novoNome, novoNascimento.toString(), novaSenha);
        } else if (novoNome != null) {
            sucessoAtualizacao = updatePerfilDao.atualizarPerfil(email, novoNome, null, novaSenha);
        } else if (novoNascimento != null) {
            sucessoAtualizacao = updatePerfilDao.atualizarPerfil(email, null, novoNascimento.toString(), novaSenha);
        } else {
            sucessoAtualizacao = updatePerfilDao.atualizarPerfil(email, null, null, novaSenha);
        }

        if (sucessoAtualizacao) {
            exibirAlertaSucesso("Atualização Realizada", "Perfil atualizado com sucesso!");
            limparCamposAtualizarPerfil();
        } else {
            exibirAlertaErro("Erro de Atualização", "Falha ao atualizar perfil.");
        }
    }


    private void limparCamposAtualizarPerfil() {
        tfAlterarEmail.clear();
        pfSenhaAntigaAlterar.clear();
        tfAlterarNome.clear();
        dateAlterarNascimento.setValue(null);
        pfAlterarSenha.clear();
        pfConfirmarSenhaAlterar.clear();
    }


    public boolean verificarSenhaAntiga(String email, String senhaAntiga) {
        Connection conn = new BDConexaoMySQL().getConnection();
        String verificaSenhaAntigaSQL = "SELECT COUNT(*) FROM passageiros WHERE email = ? AND senha = ?";
        try {
            PreparedStatement psVerificaSenhaAntiga = ((Connection) conn).prepareStatement(verificaSenhaAntigaSQL);
            psVerificaSenhaAntiga.setString(1, email);
            psVerificaSenhaAntiga.setString(2, senhaAntiga);

            ResultSet rsVerificaSenhaAntiga = psVerificaSenhaAntiga.executeQuery();
            rsVerificaSenhaAntiga.next();
            int count = rsVerificaSenhaAntiga.getInt(1);

            ((ResultSet) rsVerificaSenhaAntiga).close();
            psVerificaSenhaAntiga.close();

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


    public void fecharPainelCadastro(){
        if (paneCadastro.isVisible()) {
            paneCadastro.setVisible(false);
        }

    }

    public void abrirPainelCadastro() {
        if (!paneCadastro.isVisible()) {
            paneCadastro.setVisible(true);
        }
    }


    private void exibirAlertaSucesso(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void exibirAlertaErro(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }



    private void handleAbrirCadastroPane() {
        paneCadastro.setVisible(true);
    }

    public HomeController() {
        this.FeedbacksDao = new FeedbacksDao();
    }

    public void addDados() {
        int a = FeedbacksDao.obterQuantidadePorTipoProblema(1);
        int b = FeedbacksDao.obterQuantidadePorTipoProblema(2);
        int c = FeedbacksDao.obterQuantidadePorTipoProblema(3);
        int d = FeedbacksDao.obterQuantidadePorTipoProblema(4);


        grafFeedback.getData().clear();
        grafFeedback.getData().add(new PieChart.Data("Atraso", a));
        grafFeedback.getData().add(new PieChart.Data("Segurança", c));
        grafFeedback.getData().add(new PieChart.Data("Lotação", b));
        grafFeedback.getData().add(new PieChart.Data("Acidente", d));
        mudaCorGraf(grafFeedback.getData().get(0), "#F3D3BD");
        mudaCorGraf(grafFeedback.getData().get(1), "#FF5E5E");
        mudaCorGraf(grafFeedback.getData().get(2), "#BCFF87");
        mudaCorGraf(grafFeedback.getData().get(3), "#5F4CA8");


        txLinhaMais.setText("Linha mais problemática: " + FeedbacksDao.obterLinhaMaisFeedback());
        txLot.setText("Lotação: " + FeedbacksDao.obterQuantidadePorTipoProblema(2));
        txSeg.setText("Segurança: " + FeedbacksDao.obterQuantidadePorTipoProblema(3));
        txAtr.setText("Atraso: " + FeedbacksDao.obterQuantidadePorTipoProblema(1));
        txAci.setText("Acidente: " + FeedbacksDao.obterQuantidadePorTipoProblema(4));
        txTotal.setText("Total: " + FeedbacksDao.obterQuantidadeFeedbacks());
}

    
    private void filtrarItens(String searchText) {
        for (Node node : vboxItems.getChildren()) {
            if (node instanceof HBox) {
                HBox hBox = (HBox) node;
                boolean found = false;
                for (Node childNode : hBox.getChildren()) {
                    if (childNode instanceof Label) {
                        Label label = (Label) childNode;
                        // Verifica se o texto do Label contém o texto de pesquisa (ignorando maiúsculas e minúsculas)
                        if (label.getText().toLowerCase().contains(searchText.toLowerCase())) {
                            found = true;
                            break;
                        }
                    }
                }
                hBox.setVisible(found);
                hBox.setManaged(found);
            }
        }
    }
    private void mudaCorGraf(PieChart.Data slice, String color) {
        slice.getNode().setStyle("-fx-pie-color: " + color + ";");
    }
    public void enviarFeedback(ActionEvent event) {
        String descricao = tfDescricao.getText();
        String linha = cbLinhas.getValue();
        String email = this.emailPassageiroLogado;

        // Verifica se algum dos campos é nulo ou vazio
        if (descricao == null || descricao.isEmpty() || linha == null || linha.isEmpty() || email == null || email.isEmpty()) {
            exibirAlertaErro("Erro ao enviar feedback", "Por favor, preencha todos os campos.");
            return;
        }

        FeedbacksDao feedbackDao = new FeedbacksDao();
        // Tenta enviar o feedback
        feedbackDao.enviaFeedback(tipoProblema, descricao, linha, email);
        exibirAlertaSucesso("Feedback Enviado", "O feedback foi enviado com sucesso.");

        limparCamposFeedback();
    }

    private void limparCamposFeedback() {
        tfDescricao.clear();
        cbLinhas.getSelectionModel().clearSelection();
    }



    @FXML public void selecionaAtraso(ActionEvent event) {
        tipoProblema = 1;
    }
    @FXML public void selecionaLotacao(ActionEvent event) {
        tipoProblema = 2;
    }
    @FXML public void selecionaSeguranca(ActionEvent event) {
        tipoProblema = 3;
    }
    @FXML public void selecionaAcidente(ActionEvent event) {
        tipoProblema = 4;
    }
    
    
    private void loadLinhas() {
    if (listLinhas == null) {
        LinhasDao edao = new LinhasDao();
        listLinhas = edao.getListaLinhas();
    }

    for (Linhas emp : listLinhas) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("item.fxml"));
            Node itemNode = loader.load();
            vboxItems.getChildren().add(itemNode);
            itemController itemController = loader.getController();
            itemController.setItemListener(this);
            itemController.setLinhas(emp);
            itemController.setHomeController(this);
            vboxItems.getStyleClass().add("lp.cmb.styles.item.css");
        } catch (IOException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
    
    @FXML

public void mostrarDetalhesLinha(ActionEvent event) {
    String numeroLinha = tfSelecionaLinha.getText();
    
    if (!numeroLinha.isEmpty()) {
        List<Reports> feedbacksLinha = FeedbacksDao.obterFeedbacksLinha(numeroLinha);
        atualizarVBoxDetalhesLinha(feedbacksLinha);
    } else {
        System.out.println("Número da linha vazio.");
    }
}

private void atualizarVBoxDetalhesLinha(List<Reports> listReports) {
    vbLinhasEsp.getChildren().clear();
    if (listReports != null && !listReports.isEmpty()) {
        for (Reports reports : listReports) {
            criarFeedback(reports);
        }
    } else {
        System.out.println("Lista de feedbacks vazia ou nula.");
    }
}


private void criarFeedback(Reports reports) {
    System.out.println("Criando feedback para: " + reports.getNomePassageiro());
    try {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("itemLF.fxml"));
        Node itemNode = loader.load();
        vbLinhasEsp.getChildren().add(itemNode);
        ItemLFController itemController1 = loader.getController();
        itemController1.setNome(reports.getNomePassageiro());
        itemController1.setDescricao(reports.getDescricao());
        itemController1.setData(reports.getDataHora());
        
    } catch (IOException ex) {
        Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
    }
}


@FXML
public void detalhesLinha() {
    String numeroLinhaSelecionada = tfSelecionaLinha.getText();
    if (!numeroLinhaSelecionada.isEmpty()) {
        String linhaSelecionada = numeroLinhaSelecionada;
        List<Reports> feedbacksLinha = FeedbacksDao.obterFeedbacksLinha(linhaSelecionada);
        atualizarVBoxDetalhesLinha(feedbacksLinha);
    }
}
public void abrirMapa(){
        MapaController mapaController = new MapaController(this);
        mapaController.mostraMapa();
}
    public void painelFeedback() {
    if (!painelFeedbacks.isVisible()) {
        painelFeedbacks.setVisible(true);
    }
}
    public void fechaPainelFeedback() {
    if (painelFeedbacks.isVisible()) {
        painelFeedbacks.setVisible(false);
    }
}
    public void fechaPainelLinhasEsp() {
    if (painelLinhasSelect.isVisible()) {
        painelLinhasSelect.setVisible(false);
    }
}
    
public void painelLinhas() {
    if (!painelLinhas.isVisible()) {
        ocultarTodosPaineis();
        painelLinhas.setVisible(true);
    }
}

public void abrirPainelUsuario() {
    if (!painelAtualizarPerfil.isVisible()) {
        ocultarTodosPaineis();
        painelAtualizarPerfil.setVisible(true);
    }
}

public void painelHome() {
    if (!painelHome.isVisible()) {
        ocultarTodosPaineis();
        painelHome.setVisible(true);
    }
}

public void abrirCadastro(){
    paneCadastro.setVisible(true);
}

private void ocultarTodosPaineis() {
    painelFeedbacks.setVisible(false);
    painelLinhas.setVisible(false);
    painelHome.setVisible(false);
    painelAtualizarPerfil.setVisible(false);
    painelFeedbacks.setVisible(false);
    painelLinhas.setVisible(false);
}

    public void fecharAplicacao() {
        System.exit(0);
    }

}
