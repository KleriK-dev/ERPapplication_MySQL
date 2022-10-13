package controllers.items;

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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class NewItemController {

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
    private Button saveButton;

    @FXML
    private Button searchByCodeButton;

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
    private CheckBox retailContainsVat;

    @FXML
    private CheckBox wholesaleContainsVat;

    SpecialAlert alert = new SpecialAlert();
    Item item = new Item();

    /* From line 93 to line 99
     * create an instance of this controller with a constructor
     * and create a getter so it can return this instance
     * WITH THAT IT MAKES THIS CONTROLLER USABLE BY OTHER CONTROLLERS
     * THA MEANS EVERY PUBLIC METHOD IN THIS CONTROLLER CAN BE USED ON OTHER CONTROLLER */

    private static NewItemController instance;

    public NewItemController(){
        instance = this;
    }

    public static NewItemController getInstance(){
        return instance;
    }

    @FXML
    void CloseNewItemWindow(ActionEvent event) {

        Stage customerStage = (Stage) cancelButton.getScene().getWindow();
        customerStage.close();

    }

    @FXML
    void GenerateCodeItem(ActionEvent event) {

        Connection conn = MySQLConnection.connectToDB();
        String query = "SELECT * FROM items WHERE code REGEXP '^[0-9]+$' ORDER BY code DESC LIMIT 1 "; // select last row that has code only with numbers
        Statement st;
        ResultSet rs;

        String lastItemCode = null;

        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                lastItemCode = rs.getString("code");
            }

            code.setText(item.generateNextItemCode(lastItemCode)); //use generateNextItemCode method from Item class

        } catch (SQLException ex) {
            alert.show("Error", "Unknown error occured!", Alert.AlertType.ERROR);
        }

    }

    @FXML
    void OpenInstructions(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/instructions/ItemInstructions.fxml"));
        Parent root = loader.load();

        Stage newItemInstructionStage = new Stage();
        Scene newItemInstructionScene = new Scene(root);
        Image image = new Image(getClass().getResourceAsStream("/images/logo.png"));
        newItemInstructionStage.getIcons().add(image);
        newItemInstructionStage.setX(303);
        newItemInstructionStage.setY(106);
        newItemInstructionStage.setScene(newItemInstructionScene);
        newItemInstructionStage.setTitle("Instructions");
        newItemInstructionStage.setAlwaysOnTop(true);
        newItemInstructionStage.show();

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
    void OpenMeasurementSelection(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/selectionWindows/MeasurementSelectionWindow.fxml"));
        Parent root = loader.load();

        //pass from where this selection window is opened
        MeasurementSelectionController measurementSelectionController = loader.getController();
        measurementSelectionController.openedFrom("from new item");

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
        supplierSelectionController.openedFrom("from new item");
        supplierSelectionController.loadData(supplier.getText().trim()); //load data of the inputed brandname

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
        vatCategorySelectionController.openedFrom("from new item");

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
    void SaveNewItem(ActionEvent event) {

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

        if (itemCode.equals("")) { // check if this field is empty
            alert.show("Warning", "Empty Item Code! Please enter a code for the item!", Alert.AlertType.WARNING);
        } else {

            Item item = new Item(itemID, itemCode, itemDescription, itemBarcode, itemCostprice, itemWholesaleprice, itemRetailPrice, itemDiscount, 0.0, itemSupplier, null, itemVatcategorie, itemMeasurement, 1, 0);

            if(item.checkIfItemCodeExists(itemCode)){ // then check if this code exists
                alert.show("Warning", "Item with this code exists!", Alert.AlertType.WARNING);
            } else {

                item.addItem();

            }

            Stage stage = (Stage) saveButton.getScene().getWindow(); // close this stage
            stage.close();

            ItemMainController.getInstance().RefreshData();

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

    public void displayItemID(String ID){

        id.setText(ID);

    }

}
