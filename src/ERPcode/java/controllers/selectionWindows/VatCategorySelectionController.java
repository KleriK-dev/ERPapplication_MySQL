package controllers.selectionWindows;

import controllers.items.EditItemController;
import controllers.items.NewItemController;
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
import objects.VatCategorie;
import specialAlerts.SpecialAlert;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class VatCategorySelectionController {

    @FXML
    private Button cancelButton;

    @FXML
    private TableView<VatCategorie> table;

    @FXML
    private TableColumn<VatCategorie, Integer> col_id;

    @FXML
    private TableColumn<VatCategorie, Double> col_percentage;

    @FXML
    private TableColumn<VatCategorie, String> col_vatcategorie;

    @FXML
    private TextField openedFromTextfield;

    @FXML
    private Button selectButton;

    @FXML
    private TextField selectedID;

    @FXML
    private TextField selectedVatCategorie;

    ObservableList<VatCategorie> oblist = FXCollections.observableArrayList();
    SpecialAlert alert = new SpecialAlert();
    int index = -1;

    @FXML
    public void initialize() {

        loadData();

    }

    @FXML
    void AddVatCategory(ActionEvent event) {

        addVatCategory();

    }

    @FXML
    void CloseSelectionWindow(ActionEvent event) {

        Stage vatcategorieSelectionStage = (Stage) cancelButton.getScene().getWindow();
        vatcategorieSelectionStage.close();

    }

    @FXML
    void HandleSelectedRow(MouseEvent event) {

        index = table.getSelectionModel().getSelectedIndex(); // we make index equal to the index of the selected row of the table

        if (index <= -1) { // check if index is -1 or smaller then do not act
            return;
        } else {
            selectedID.setText(String.valueOf(col_id.getCellData(index))); // else set on the selectedid textfield the id that exists on this spesific index of tableview
            selectedVatCategorie.setText(String.valueOf(col_vatcategorie.getCellData(index)));
        }

        //on double click to a row, add vat category
        table.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (e.isPrimaryButtonDown() && e.getClickCount() == 2) {

                    addVatCategory();

                }
            }
        });

    }

    private void loadData(){

        Connection conn = MySQLConnection.connectToDB();
        String query = "SELECT * FROM vat_categorie";
        Statement st;
        ResultSet rs;

        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                oblist.add(new VatCategorie(rs.getInt("id"), rs.getString("description"), rs.getDouble("vat_percentage")));
            }

            col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
            col_vatcategorie.setCellValueFactory(new PropertyValueFactory<>("description"));
            col_percentage.setCellValueFactory(new PropertyValueFactory<>("vatPercentage"));

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

    public void addVatCategory(){

        String vatcaregorieID = selectedID.getText();
        String vatcategorieDescription = selectedVatCategorie.getText();

        if(openedFromTextfield.getText().equals("from new item")){

            // pass the data to that controller
            NewItemController.getInstance().getVatCategorieData(vatcaregorieID, vatcategorieDescription);

        } else if(openedFromTextfield.getText().equals("from edit item")){

            EditItemController.getInstance().getVatCategorieData(vatcaregorieID, vatcategorieDescription);

        }

        Stage vatcategorieSelectionStage = (Stage) cancelButton.getScene().getWindow();
        vatcategorieSelectionStage.close();

    }

}
