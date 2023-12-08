module lp.cmb {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.base;
    requires javafx.web;
    //O comando opens é usado para abrir um pacote para ser acessado por outro módulo
    opens lp.cmb to javafx.fxml;
    exports lp.cmb;
}
