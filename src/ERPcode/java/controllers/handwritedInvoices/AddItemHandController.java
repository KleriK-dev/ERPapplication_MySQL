package controllers.handwritedInvoices;

import controllers.selectionWindows.ItemSelectionController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import objects.ItemTransaction;

import java.io.IOException;

public class AddItemHandController {

    @FXML
    private TextField VAT;

    @FXML
    private Button cancelButton;

    @FXML
    private Button searchItemBarcode;

    @FXML
    private TextField description;

    @FXML
    private TextField discount;

    @FXML
    private TextField etiology;

    @FXML
    private Button insertButton;

    @FXML
    private TextField itemcode;

    @FXML
    private TextField price;

    @FXML
    private TextField quantity;

    @FXML
    private Button searchItemCodeButton;

    @FXML
    private Button searchItemDescriptionButton;

    @FXML
    private TextField total;

    @FXML
    private TextField customer_id; //hidden

    @FXML
    private TextField barcode;

    @FXML
    private TextField invoice_id; //hidden

    @FXML
    private TextField date; //hidden

    @FXML
    private TextField time; //hidden

    @FXML
    private TextField transaction_id; //hidden

    /* create an instance of this controller with a constructor
     * and create a getter so it can return this instance
     * WITH THAT IT MAKES THIS CONTROLLER USABLE BY OTHER CONTROLLERS
     * THA MEANS EVERY PUBLIC METHOD IN THIS CONTROLLER CAN BE USED ON OTHER CONTROLLER */

    private static AddItemHandController instance;

    public AddItemHandController(){
        instance = this;
    }

    public static AddItemHandController getInstance(){
        return instance;
    }

    @FXML
    private void CloseWindow(ActionEvent event) {

        Stage customerStage = (Stage) cancelButton.getScene().getWindow();
        customerStage.close();

    }

    @FXML
    private void SelectAllText(MouseEvent event){
        quantity.selectAll();
        price.selectAll();
        discount.selectAll();
    }

    @FXML
    private void QalqulateTotal(KeyEvent event) {

        Double Quantity;
        Double UnitPrice;
        Double Discount;

        // when quantity textfield is left blank, add 1.0
        // as we don't want to get 0.0 at quantity
        // because the total will be 0 as unit price multiply by quantity
        if(quantity.getText().equals("")){
            Quantity = 1.0;
            quantity.setText(String.valueOf(Quantity));
        } else { //else add what user typed
            Quantity = Double.valueOf(quantity.getText());
        }

        //if price is blank, add 0.0 to avoid null exceptions
        if(price.getText().equals("")){
            UnitPrice = 0.0;
            price.setText(String.valueOf(UnitPrice));
        } else { //else add what user typed
            UnitPrice = Double.valueOf(price.getText());
        }

        //if discount is blanc, add 0.0 to avoid null exceptions
        if(discount.getText().equals("")){
            Discount = 0.0;
            discount.setText(String.valueOf(Discount));
        } else { //else add what user typed
            Discount = Double.valueOf(discount.getText());
        }

        Double priceBasedQuantity = UnitPrice * Quantity; //total price will be unit price multiplied by quantity

        Double s = 100 - Discount; // create a variable that substract from 100 the inputed number of user to find the difference

        //then the total price is the price multiplied by quantity multiplied by the difference of discount divided to 100
        Double Total = (s * priceBasedQuantity) / 100;

        total.setText(String.valueOf(Total));

    }

    @FXML
    private void InsertItemToTable(ActionEvent event) throws IOException {

        String Date = date.getText();
        String Time = time.getText();
        String Code = itemcode.getText();
        String Description = description.getText();
        Double Quantity = Double.valueOf(quantity.getText());
        Double Price = Double.valueOf(price.getText());
        Double Discount = Double.valueOf(discount.getText());
        Double VatValue = Double.valueOf(VAT.getText());
        String Etiology = etiology.getText();
        Double Total = Double.valueOf(total.getText());
        Integer InvoiceID = Integer.valueOf(invoice_id.getText());

        ItemTransaction transaction = new ItemTransaction(null, Date, Time, Code, Description, Quantity, Price, VatValue, Discount, Etiology, Total, 100, InvoiceID);

        // use add item to list from new handwrited invoice controller that adds to the list a ItemTransaction object
        NewHandwritedInvoiceController.getInstance().addItemToList(transaction);

        //close add item window
        Stage addItemStage = (Stage) cancelButton.getScene().getWindow();
        addItemStage.close();

        //Re open AddItemWindow to add another transaction
        NewHandwritedInvoiceController.getInstance().OpenAddItemWindow(event);

    }

    @FXML
    private void SearchItemByCode(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/selectionWindows/ItemSelectionWindow.fxml"));
        Parent root = loader.load();

        //pass from where this selection window is opened
        ItemSelectionController itemSelectionController = loader.getController();
        itemSelectionController.openedFrom("from new handwrited invoice add item");
        itemSelectionController.loadDataByCode(itemcode.getText().trim()); //load data of the inputed item code
        itemSelectionController.getCustomerOrSupplierID(customer_id.getText()); //pass customer id to item selection

        Stage measurementStage = new Stage();
        Scene measurementScene = new Scene(root);
        measurementStage.setX(270);
        measurementStage.setY(110);
        measurementStage.setScene(measurementScene);
        measurementStage.initStyle(StageStyle.UNDECORATED);
        measurementStage.initModality(Modality.APPLICATION_MODAL);
        measurementStage.setAlwaysOnTop(true);
        measurementStage.setResizable(false);
        measurementStage.show();

    }

    @FXML
    void SearchItemByBarcode(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/selectionWindows/ItemSelectionWindow.fxml"));
        Parent root = loader.load();

        //pass from where this selection window is opened
        ItemSelectionController itemSelectionController = loader.getController();
        itemSelectionController.openedFrom("from new customer invoice add item");
        itemSelectionController.loadDataByBarcode(barcode.getText().trim()); //load data of the inputed barcode
        itemSelectionController.getCustomerOrSupplierID(customer_id.getText()); //pass customer id to item selection

        Stage measurementStage = new Stage();
        Scene measurementScene = new Scene(root);
        measurementStage.setX(270);
        measurementStage.setY(110);
        measurementStage.setScene(measurementScene);
        measurementStage.initStyle(StageStyle.UNDECORATED);
        measurementStage.initModality(Modality.APPLICATION_MODAL);
        measurementStage.setAlwaysOnTop(true);
        measurementStage.setResizable(false);
        measurementStage.show();

    }

    @FXML
    private void SearchItemByDescription(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/selectionWindows/ItemSelectionWindow.fxml"));
        Parent root = loader.load();

        //pass from where this selection window is opened
        ItemSelectionController itemSelectionController = loader.getController();
        itemSelectionController.openedFrom("from new customer invoice add item");
        itemSelectionController.loadDataByDescription(description.getText().trim()); //load data of the inputed description
        itemSelectionController.getCustomerOrSupplierID(customer_id.getText()); //pass customer id to item selection

        Stage measurementStage = new Stage();
        Scene measurementScene = new Scene(root);
        measurementStage.setX(270);
        measurementStage.setY(110);
        measurementStage.setScene(measurementScene);
        measurementStage.initStyle(StageStyle.UNDECORATED);
        measurementStage.initModality(Modality.APPLICATION_MODAL);
        measurementStage.setAlwaysOnTop(true);
        measurementStage.setResizable(false);
        measurementStage.show();

    }

    public void getItemData(String code, String itemdescription, Double itemquantity, Double itemprice, String itembarcode, Double itemdiscount, Double vat, String itemetiology, Double itemtotal){

        itemcode.setText(code);
        description.setText(itemdescription);
        quantity.setText(String.valueOf(itemquantity));
        price.setText(String.valueOf(itemprice));
        barcode.setText(itembarcode);
        discount.setText(String.valueOf(itemdiscount));
        VAT.setText(String.valueOf(vat));
        etiology.setText(itemetiology);
        total.setText(String.valueOf(itemtotal));

    }

    public void loadDataForTransaction(String customerID, String invoiceID, String Date, String Time){

        customer_id.setText(customerID);
        invoice_id.setText(invoiceID);
        date.setText(Date);
        time.setText(Time);

    }

}
