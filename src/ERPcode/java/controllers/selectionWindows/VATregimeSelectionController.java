package controllers.selectionWindows;

import controllers.customerInvoices.NewCustomerInvoiceController;
import controllers.customers.EditCustomerController;
import controllers.customers.NewCustomerController;
import controllers.handwritedInvoices.NewHandwritedInvoiceController;
import controllers.supplierInvoices.NewSupplierInvoiceController;
import controllers.suppliers.EditSupplierController;
import controllers.suppliers.NewSupplierController;
import database.MySQLConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import objects.VATregime;
import specialAlerts.SpecialAlert;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class VATregimeSelectionController {

    @FXML
    private Button cancelButton;

    @FXML
    private TableView<VATregime> table;

    @FXML
    private TableColumn<Integer, VATregime> col_id;

    @FXML
    private TableColumn<String, VATregime> col_vatregime;

    @FXML
    private TextField selectedID;

    @FXML
    private TextField selectedVatRegime;

    @FXML
    private TextField openedFromTextfield;

    @FXML
    private Button selectButton;

    ObservableList<VATregime> oblist = FXCollections.observableArrayList();
    SpecialAlert alert = new SpecialAlert();
    int index = -1;

    @FXML
    public void initialize(){

        loadData();

    }

    @FXML
    void AddVatregime(ActionEvent event) throws IOException {

        addVatRegime();

    }

    @FXML
    void CloseSelectionWindow(ActionEvent event) {

        Stage customerStage = (Stage) cancelButton.getScene().getWindow();
        customerStage.close();

    }

    @FXML
    void HandleSelectedRow(MouseEvent event) {

        index = table.getSelectionModel().getSelectedIndex(); // we make index equal to the index of the selected row of the table

        if (index <= -1) { // check if index is -1 or smaller then do not act
            return;
        } else {
            selectedID.setText(String.valueOf(col_id.getCellData(index))); // else set on the selectedid textfield the id that exists on this spesific index of tableview
            selectedVatRegime.setText(String.valueOf(col_vatregime.getCellData(index)));
        }

        //on double click to a row, add vat regime
        table.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (e.isPrimaryButtonDown() && e.getClickCount() == 2) {

                    addVatRegime();

                }
            }
        });

    }

    private void loadData(){

        Connection conn = MySQLConnection.connectToDB();
        String query = "SELECT * FROM vat_regime";
        Statement st;
        ResultSet rs;

        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                oblist.add(new VATregime(rs.getInt("id"), rs.getString("description")));
            }

            col_id.setCellValueFactory(new PropertyValueFactory<>("vatregime_id"));
            col_vatregime.setCellValueFactory(new PropertyValueFactory<>("vatregime"));

            table.setItems(oblist);

        } catch (SQLException ex) {
            alert.show("Error", "Unknown error occured!", Alert.AlertType.ERROR);
        }

    }

    // this paying selection controller will be used by other controller
    // so we need to keep track from where is used
    // by adding the stage title that used it
    public void openedFrom(String location){

        openedFromTextfield.setText(location);

    }

    public void addVatRegime(){

        String vatregimeID = selectedID.getText();
        String vatRegimeDescription = selectedVatRegime.getText();

        if(openedFromTextfield.getText().equals("from new customer")){ // check from where this selection window is opened

            NewCustomerController.getInstance().getVatRegimeData(vatregimeID, vatRegimeDescription); // pass the data to that controller

        } else if (openedFromTextfield.getText().equals("from edit customer")){

            EditCustomerController.getInstance().getVatRegimeData(vatregimeID, vatRegimeDescription); // pass the data to that controller

        } else if(openedFromTextfield.getText().equals("from new supplier")){

            NewSupplierController.getInstance().getVatRegimeData(vatregimeID, vatRegimeDescription);

        } else if(openedFromTextfield.getText().equals("from edit supplier")){

            EditSupplierController.getInstance().getVatRegimeData(vatregimeID, vatRegimeDescription);

        } else if(openedFromTextfield.getText().equals("from new customer invoice")){

            NewCustomerInvoiceController.getInstance().getVatRegimeData(vatRegimeDescription);

        } else if(openedFromTextfield.getText().equals("from new handwrited invoice")){

            NewHandwritedInvoiceController.getInstance().getVatRegimeData(vatRegimeDescription);

        } else if(openedFromTextfield.getText().equals("from new supplier invoice")){

            NewSupplierInvoiceController.getInstance().getVatRegimeData(vatRegimeDescription);

        }

        Stage customerStage = (Stage) selectButton.getScene().getWindow();
        customerStage.close();

    }

}

