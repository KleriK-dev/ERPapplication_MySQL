package controllers.selectionWindows;

import controllers.customers.EditCustomerController;
import controllers.customers.NewCustomerController;
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
import objects.PriceList;
import specialAlerts.SpecialAlert;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PriceListSelectionController {

    @FXML
    private Button cancelButton;

    @FXML
    private TableView<PriceList> table;

    @FXML
    private TableColumn<Integer, PriceList> col_id;

    @FXML
    private TableColumn<String, PriceList> col_pricelist;

    @FXML
    private Button selectButton;

    @FXML
    private TextField selectedID;

    @FXML
    private TextField selectedPricelist;

    @FXML
    private TextField openedFromTextfield;

    ObservableList<PriceList> oblist = FXCollections.observableArrayList();
    SpecialAlert alert = new SpecialAlert();
    int index = -1;

    @FXML
    public void initialize(){

        loadData();

    }

    @FXML
    void AddPricelist(ActionEvent event) {

        addPricelist();

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
            selectedPricelist.setText(String.valueOf(col_pricelist.getCellData(index)));
        }

        //on double click to a row, add pricelist
        table.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (e.isPrimaryButtonDown() && e.getClickCount() == 2) {

                    addPricelist();

                }
            }
        });

    }

    private void loadData(){

        Connection conn = MySQLConnection.connectToDB();
        String query = "SELECT * FROM pricelist";
        Statement st;
        ResultSet rs;

        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                oblist.add(new PriceList(rs.getInt("id"), rs.getString("description")));
            }

            col_id.setCellValueFactory(new PropertyValueFactory<>("pricelist_id"));
            col_pricelist.setCellValueFactory(new PropertyValueFactory<>("pricelist_description"));

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

    public void addPricelist(){

        String pricelistID = selectedID.getText();
        String pricelistDescription = selectedPricelist.getText();

        if(openedFromTextfield.getText().equals("from new customer")) { // check from where this selection window is opened

            NewCustomerController.getInstance().getPricelistData(pricelistID, pricelistDescription); // pass the data to that controller

        } else if (openedFromTextfield.getText().equals("from edit customer")){

            EditCustomerController.getInstance().getPricelistData(pricelistID, pricelistDescription); // pass the data to that controller

        } else if(openedFromTextfield.getText().equals("from new supplier")){

            NewSupplierController.getInstance().getPricelistData(pricelistID, pricelistDescription);

        } else if(openedFromTextfield.getText().equals("from edit supplier")){

            EditSupplierController.getInstance().getPricelistData(pricelistID, pricelistDescription);

        }

        Stage customerStage = (Stage) selectButton.getScene().getWindow();
        customerStage.close();

    }

}
