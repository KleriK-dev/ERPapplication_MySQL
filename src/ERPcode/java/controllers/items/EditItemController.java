package controllers.items;

import commons.CommonMethods;
import controllers.selectionWindows.MeasurementSelectionController;
import controllers.selectionWindows.SupplierSelectionController;
import controllers.selectionWindows.VatCategorySelectionController;
import database.MySQLConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import objects.Item;
import specialAlerts.SpecialAlert;

import java.io.IOException;
import java.sql.*;

public class EditItemController {

    @FXML
    private Button VATcategorieSearchButton;

    @FXML
    private TextField barcode;

    @FXML
    private Button cancelButton;

    @FXML
    private TextField code;

    @FXML
    private TextField costPrice;

    @FXML
    private TextField description;

    @FXML
    private TextField discount;

    @FXML
    private TextField id;

    @FXML
    private Button instructionsButton;

    @FXML
    private Button measurementSearchButton;

    @FXML
    private TextField measurement_id_textfield;

    @FXML
    private TextField measurement_textfield;

    @FXML
    private TextField retailPrice;

    @FXML
    private Button updateButton;

    @FXML
    private Button searchSupplierButton;

    @FXML
    private TextField supplier;

    @FXML
    private TextField supplierID;

    @FXML
    private TextField vatcategorie_id_textfield;

    @FXML
    private TextField vatcategorie_textfield;

    @FXML
    private TextField wholesalePrice;

    @FXML
    private TextField remaining;

    @FXML
    private CheckBox retailContainsVat;

    @FXML
    private CheckBox wholesaleContainsVat;

    Item item = new Item();
    SpecialAlert alert = new SpecialAlert();
    CommonMethods commons = new CommonMethods();

    /* From line 90 to line 96
     * create an instance of this controller with a constructor
     * and create a getter so it can return this instance
     * WITH THAT IT MAKES THIS CONTROLLER USABLE BY OTHER CONTROLLERS
     * THA MEANS EVERY PUBLIC METHOD IN THIS CONTROLLER CAN BE USED ON OTHER CONTROLLER */

    private static EditItemController instance;

    public EditItemController() {
        instance = this;
    }

    public static EditItemController getInstance() {
        return instance;
    }

    @FXML
    void CloseNewItemWindow(ActionEvent event) {

        Stage customerStage = (Stage) cancelButton.getScene().getWindow();
        customerStage.close();

    }

    //when user click on field select all the text so he does not have to press backspace to delete but can immediately start typing the value
    @FXML
    private void SelectAllText(MouseEvent event){
        costPrice.selectAll();
        wholesalePrice.selectAll();
        retailPrice.selectAll();
        discount.selectAll();
    }

    @FXML
    void SetZeroWhenItsEmpry(KeyEvent event) {

        if(costPrice.getText().equals("")){
            costPrice.setText("0.0");
        }

        if(wholesalePrice.getText().equals("")){
            wholesalePrice.setText("0.0");
        }

        if(retailPrice.getText().equals("")){
            retailPrice.setText("0.0");
        }

        if(discount.getText().equals("")){
            discount.setText("0.0");
        }

    }

    @FXML
    void OpenInstructions(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/instructions/ItemInstructions.fxml"));
        Parent root = loader.load();

        Stage editItemInstructionStage = new Stage();
        Scene editItemInstructionScene = new Scene(root);
        Image image = new Image(getClass().getResourceAsStream("/images/logo.png"));
        editItemInstructionStage.getIcons().add(image);
        editItemInstructionStage.setX(303);
        editItemInstructionStage.setY(106);
        editItemInstructionStage.setScene(editItemInstructionScene);
        editItemInstructionStage.setTitle("Instructions");
        editItemInstructionStage.setAlwaysOnTop(true);
        editItemInstructionStage.show();

    }

    @FXML
    void OpenMeasurementSelection(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/selectionWindows/MeasurementSelectionWindow.fxml"));
        Parent root = loader.load();

        //pass from where this selection window is opened
        MeasurementSelectionController measurementSelectionController = loader.getController();
        measurementSelectionController.openedFrom("from edit item");

        Stage measurementStage = new Stage();
        Scene measurementScene = new Scene(root);
        measurementStage.setX(220);
        measurementStage.setY(240);
        measurementStage.setScene(measurementScene);
        measurementStage.initStyle(StageStyle.UNDECORATED);
        measurementStage.initModality(Modality.APPLICATION_MODAL);
        measurementStage.setAlwaysOnTop(true);
        measurementStage.setResizable(false);
        measurementStage.show();

    }

    @FXML
    void OpenSupplierSelection(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/selectionWindows/SupplierSelectionWindow.fxml"));
        Parent root = loader.load();

        //pass from where this selection window is opened
        SupplierSelectionController supplierSelectionController = loader.getController();
        supplierSelectionController.openedFrom("from edit item");
        supplierSelectionController.loadData(supplier.getText().trim());

        Stage measurementStage = new Stage();
        Scene measurementScene = new Scene(root);
        measurementStage.setX(220);
        measurementStage.setY(130);
        measurementStage.setScene(measurementScene);
        measurementStage.initStyle(StageStyle.UNDECORATED);
        measurementStage.initModality(Modality.APPLICATION_MODAL);
        measurementStage.setAlwaysOnTop(true);
        measurementStage.setResizable(false);
        measurementStage.show();

    }

    @FXML
    void OpenVATcategorieSelection(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/selectionWindows/VatCategorySelectionWindow.fxml"));
        Parent root = loader.load();

        //pass from where this selection window is opened
        VatCategorySelectionController vatCategorySelectionController = loader.getController();
        vatCategorySelectionController.openedFrom("from edit item");

        Stage measurementStage = new Stage();
        Scene measurementScene = new Scene(root);
        measurementStage.setX(220);
        measurementStage.setY(240);
        measurementStage.setScene(measurementScene);
        measurementStage.initStyle(StageStyle.UNDECORATED);
        measurementStage.initModality(Modality.APPLICATION_MODAL);
        measurementStage.setAlwaysOnTop(true);
        measurementStage.setResizable(false);
        measurementStage.show();

    }

    @FXML
    void UpdateItem(ActionEvent event) {

        Integer itemID = Integer.valueOf(id.getText());
        String itemCode = code.getText().trim();
        String itemDescription = description.getText().trim();
        Integer itemSupplier;
        if(supplierID.getText() == ""){
            itemSupplier = 0;
        } else {
            itemSupplier = Integer.valueOf(supplierID.getText());
        }
        String itemBarcode = barcode.getText().trim();
        Double itemCostprice = Double.valueOf(costPrice.getText().trim());
        Double itemWholesaleprice = Double.valueOf(wholesalePrice.getText().trim());
        Double itemRetailPrice = Double.valueOf(retailPrice.getText().trim());
        Double itemDiscount = Double.valueOf(discount.getText().trim());
        Integer itemVatcategorie = Integer.valueOf(vatcategorie_id_textfield.getText());
        Integer itemMeasurement = Integer.valueOf(measurement_id_textfield.getText());

        if (itemDescription.equals("")) { // check if this field is empty
            alert.show("Warning", "Empty Item Description! Please enter a description for the item!", Alert.AlertType.WARNING);
        } else {

            Item item = new Item(itemID, itemCode, itemDescription, itemBarcode, itemCostprice, itemWholesaleprice, itemRetailPrice, itemDiscount, 0.0, itemSupplier, null, itemVatcategorie, itemMeasurement, 1, 0);

            item.editItem();

            Stage stage = (Stage) updateButton.getScene().getWindow(); // close this stage
            stage.close();

            ItemMainController.getInstance().RefreshData();

        }

    }


    public void getData(Item selectedItem) {

        item = selectedItem;

        id.setText(String.valueOf(item.getItemID()));
        code.setText(item.getCode());
        description.setText(item.getDescription());
        supplierID.setText(String.valueOf(item.getSupplierID()));
        supplier.setText(item.getSupplierBrandname());
        barcode.setText(item.getBarcode());
        costPrice.setText(String.valueOf(item.getPurchase_price()));
        wholesalePrice.setText(String.valueOf(item.getWholesale_price()));
        retailPrice.setText(String.valueOf(item.getRetail_price()));
        discount.setText(String.valueOf(item.getDiscount()));
        vatcategorie_id_textfield.setText(String.valueOf(item.getVatCategorieID()));
        vatcategorie_textfield.setText(commons.getSelectionDescription("vat_categorie", String.valueOf(item.getVatCategorieID())));
        measurement_id_textfield.setText(String.valueOf(item.getMeasurementUnitID()));
        measurement_textfield.setText(commons.getSelectionDescription("measurement_unit", String.valueOf(item.getMeasurementUnitID())));
        remaining.setText(String.valueOf(item.getRemaining()));

        String item_id = String.valueOf(item.getItemID());

        switch(checkIfRetailVatIsSelected(item_id)){
            case 1:
                retailContainsVat.setSelected(true);
                break;
            case 0:
                retailContainsVat.setSelected(false);
                break;
        }

        switch(checkIfWholesaleVatIsSelected(item_id)){
            case 1:
                wholesaleContainsVat.setSelected(true);
                break;
            case 0:
                wholesaleContainsVat.setSelected(false);
                break;
        }

    }

    public void getMeasurementData(String measurement_id, String measurement){

        measurement_id_textfield.setText(measurement_id);
        measurement_textfield.setText(measurement);

    }

    public void getVatCategorieData(String vatcategorie_id, String vatcategorie){

        vatcategorie_id_textfield.setText(vatcategorie_id);
        vatcategorie_textfield.setText(vatcategorie);

    }

    public void getSupplierData(String supplier_id, String supplier_brandname){

        supplierID.setText(supplier_id);
        supplier.setText(supplier_brandname);

    }

    public Integer checkIfRetailVatIsSelected(String itemID){

        Connection conn = MySQLConnection.connectToDB();
        String query = "SELECT retail_contains_vat FROM items " +
                "WHERE id =" + itemID;

        Integer retailVat = null;

        PreparedStatement prst;
        Statement st;
        ResultSet rs;

        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                retailVat = rs.getInt("retail_contains_vat");
            }
        } catch (SQLException e) {
            alert.show("Error", "Unknown error occured!", Alert.AlertType.ERROR);
        }
        return retailVat;

    }

    public Integer checkIfWholesaleVatIsSelected(String itemID){

        Connection conn = MySQLConnection.connectToDB();
        String query = "SELECT wholesale_contains_vat FROM items " +
                "WHERE id =" + itemID;

        Integer wholesaleVat = null;

        PreparedStatement prst;
        Statement st;
        ResultSet rs;

        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                wholesaleVat = rs.getInt("wholesale_contains_vat");
            }
        } catch (SQLException e) {
            alert.show("Error", "Unknown error occured!", Alert.AlertType.ERROR);
        }
        return wholesaleVat;
    }

}
