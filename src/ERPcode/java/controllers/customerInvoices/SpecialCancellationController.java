package controllers.customerInvoices;

import commons.CommonMethods;
import database.MySQLConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import objects.CustomerInvoice;
import objects.ItemTransaction;
import specialAlerts.SpecialAlert;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class SpecialCancellationController {

    @FXML
    private TextField VAT;

    @FXML
    private TextField VATregime;

    @FXML
    private Button cancelButton;

    @FXML
    private TableView<ItemTransaction> table;

    @FXML
    private TableColumn<ItemTransaction, Double> col_VAT;

    @FXML
    private TableColumn<ItemTransaction, String> col_code;

    @FXML
    private TableColumn<ItemTransaction, String> col_description;

    @FXML
    private TableColumn<ItemTransaction, Double> col_discount;

    @FXML
    private TableColumn<ItemTransaction, String> col_etiology;

    @FXML
    private TableColumn<ItemTransaction, Double> col_price;

    @FXML
    private TableColumn<ItemTransaction, Double> col_quantity;

    @FXML
    private TableColumn<ItemTransaction, Double> col_total;

    @FXML
    private TextField customer_brandname;

    @FXML
    private TextField customer_id;

    @FXML
    private TextField dateTextfield;

    @FXML
    private TextField discountInvoice;

    @FXML
    private TextField discountvalue;

    @FXML
    private TextField from;

    @FXML
    private TextField id;

    @FXML
    private TextField previousID;

    @FXML
    private TextField initialvalue;

    @FXML
    private TextField invoicenumber;

    @FXML
    private TextField invoicetype_description;

    @FXML
    private TextField invoicetype_id;

    @FXML
    private TextField licenseplate;

    @FXML
    private TextField payingway_description;

    @FXML
    private TextField payingway_id;

    @FXML
    private Button printButton;

    @FXML
    private TextField purposeOfTracking;

    @FXML
    private TextField quantity;

    @FXML
    private TextField registrationUser;

    @FXML
    private TextField remarks;

    @FXML
    private TextField timeTextfield;

    @FXML
    private TextField to;

    @FXML
    private TextField total;

    @FXML
    private TextField valueBeforeVAT;

    private CustomerInvoice invoice;
    SpecialAlert alert = new SpecialAlert();
    CommonMethods commons = new CommonMethods();
    CustomerInvoice customerInvoice = new CustomerInvoice();
    ObservableList<ItemTransaction> oblist = FXCollections.observableArrayList(); // create observablelist to get the data from query and display them to table

    @FXML
    private void initialize(){

        // set current date and time to date and time textfields when this window appears
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        dateTextfield.setText(LocalDateTime.now().format(dateFormatter));
        timeTextfield.setText(LocalDateTime.now().format(timeFormatter));

    }

    @FXML
    void CloseCustomerInvoiceWindow(ActionEvent event) {

        Stage customerStage = (Stage) cancelButton.getScene().getWindow();
        customerStage.close();

    }

    @FXML
    void SaveAndPrint(ActionEvent event) {

        Integer invoiceID = Integer.valueOf(id.getText());
        String invoiceDate = dateTextfield.getText();
        String invoiceTime = timeTextfield.getText();
        Integer invoiceCustomerID = Integer.valueOf(customer_id.getText());
        Integer invoiceType = Integer.valueOf(invoicetype_id.getText()); // else add it to the invoiceType variable
        String invoiceNumber = invoicenumber.getText();
        Integer invoicePayingWayID = Integer.valueOf(payingway_id.getText());
        String invoiceTrackingPurpose = purposeOfTracking.getText();
        String fromPlace = from.getText();
        String toPlace = to.getText();
        String invoiceLicensePLate = licenseplate.getText();
        String invoiceVATregime = VATregime.getText();
        Integer previousInvoiceID = Integer.valueOf(previousID.getText());
        String programUser = registrationUser.getText();
        double initialValue = Double.parseDouble(initialvalue.getText().replace(",", "."));
        double discountPercentage = Double.parseDouble(discountInvoice.getText());
        double discountValue = Double.parseDouble(discountvalue.getText().replace(",", "."));
        double valueNoVAT = Double.parseDouble(valueBeforeVAT.getText().replace(",", "."));
        double vatvalue = Double.parseDouble(VAT.getText().replace(",", "."));
        double totalQuantity = Double.parseDouble(quantity.getText());
        double invoiceTotal = Double.parseDouble(total.getText().replace(",", "."));
        Integer handwrited = 0; // set 0 because checkbox handwrited invoice is unchecked
        String remark = remarks.getText();

        // create customer invoice object with the inserted data
        CustomerInvoice invoice = new CustomerInvoice(invoiceID, invoiceDate, invoiceTime, invoiceCustomerID, null, invoiceType, null, invoiceNumber, invoicePayingWayID, invoiceTrackingPurpose, fromPlace, toPlace, invoiceLicensePLate, invoiceVATregime, programUser, initialValue, discountPercentage, discountValue, valueNoVAT, vatvalue, totalQuantity, invoiceTotal, handwrited, remark);

        Optional<ButtonType> result = alert.showConfirmation("Special Cancellation Confirmation", "This invoice will be canceled, do you want to continue?");

        if(result.get() == ButtonType.OK) {
            //with a for loop add all the rows of the tableview to the database
            for (ItemTransaction items : oblist) {
                items.setInvoiceID(invoiceID);
                items.makeTransaction();
                //this time we add the items back to inventory as they were canceled
                items.updateItemRemaining("+"); // update each item remaining by adding to their current remaining value
            }

            invoice.cancelCustomerInvoice(); // cancel that invoice
            invoice.updtadeCounting(invoicetype_id.getText()); //update selected invoice type counter number

            invoice.setCustomerInvoiceID(previousInvoiceID); // set previous id
            invoice.updateRemarkOnInvoice(); //then update invoice with that id

            Stage customerStage = (Stage) printButton.getScene().getWindow();
            customerStage.close();

            CustomerInvoiceMainController.getInstance().RefreshData();

        }

    }

    public void getData(CustomerInvoice selectedInvoice){

        invoice = selectedInvoice;

        previousID.setText(String.valueOf(invoice.getCustomerInvoiceID()));
        customer_id.setText(String.valueOf(invoice.getCustomerID()));
        customer_brandname.setText(getCustomerBrandname(customer_id.getText()));
        invoicetype_id.setText("5");
        invoicetype_description.setText("SPECIAL CANCELLATION");

        Connection conn = MySQLConnection.connectToDB();
        String query = "SELECT number FROM invoicenumbering_" + invoicetype_id.getText() + " ORDER BY number DESC LIMIT 1 ";
        Statement st;
        ResultSet rs;

        String lastInvoiceNumber = null;

        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                lastInvoiceNumber = rs.getString("number");
            }

        } catch (SQLException ex) {
            alert.show("Error", "Unknown error occured!", Alert.AlertType.ERROR);
        }

        invoicenumber.setText(customerInvoice.generateInvoiceNumber(lastInvoiceNumber));
        payingway_id.setText(String.valueOf(invoice.getPayingWayID()));
        payingway_description.setText(commons.getSelectionDescription("paying_way", payingway_id.getText()));
        purposeOfTracking.setText(invoice.getTrackingPurpose());
        from.setText(invoice.getFromPlace());
        to.setText(invoice.getToPlace());
        licenseplate.setText(invoice.getLicensePlate());
        VATregime.setText(commons.getSelectionDescription("vat_regime", invoice.getVatRegimeDescription()));
        registrationUser.setText(commons.insertedByUser(invoice.getProgramUser()));
        initialvalue.setText(String.valueOf(invoice.getInitialValue()));
        discountInvoice.setText(String.valueOf(invoice.getDiscountPercentage()));
        discountvalue.setText(String.valueOf(invoice.getDiscountValue()));
        valueBeforeVAT.setText(String.valueOf(invoice.getValueNoVAT()));
        VAT.setText(String.valueOf(invoice.getVAT()));
        quantity.setText(String.valueOf(invoice.getQuantity()));
        total.setText(String.valueOf(invoice.getTotal()));
        remarks.setText("CANCEL: " +  invoice.getInvoiceTypeDescription() + " " + invoice.getInvoiceNumber() + " " + invoice.getInsertionDate());

    }

    public void loadTableWithData(String invoiceID){

        Connection conn = MySQLConnection.connectToDB();
        String query = "SELECT * FROM item_transactions1 " +
                "WHERE customer_invoiceID = " + invoiceID + " AND transaction_code = 300";

        PreparedStatement prst;
        Statement st;
        ResultSet rs;

        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                oblist.add(new ItemTransaction(rs.getInt("id"), rs.getString("date"), rs.getString("time"), rs.getString("item_code"), rs.getString("item_description"), rs.getDouble("quantity"), rs.getDouble("unit_price"), rs.getDouble("vat"), rs.getDouble("discount"), rs.getString("etiology"), rs.getDouble("total"), 301, rs.getInt("customer_invoiceID")));
            }

            col_code.setCellValueFactory(new PropertyValueFactory<>("item_code"));
            col_description.setCellValueFactory(new PropertyValueFactory<>("item_description"));
            col_quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
            col_price.setCellValueFactory(new PropertyValueFactory<>("unit_price"));
            col_discount.setCellValueFactory(new PropertyValueFactory<>("discount"));
            col_VAT.setCellValueFactory(new PropertyValueFactory<>("vat"));
            col_total.setCellValueFactory(new PropertyValueFactory<>("total"));
            col_etiology.setCellValueFactory(new PropertyValueFactory<>("etiology"));

            table.setItems(oblist);

        } catch (SQLException e) {
            alert.show("Error", "Unknown error occurred!", Alert.AlertType.ERROR);
        }


    }

    public String getCustomerBrandname(String customerID){

        Connection conn = MySQLConnection.connectToDB();
        String query = "SELECT brandname FROM customer WHERE customer_id = " + customerID;
        Statement st;
        ResultSet rs;

        String result = null;

        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                result = rs.getString("brandname");
            }


        } catch (SQLException ex) {
            alert.show("Error", "Unknown error occured!", Alert.AlertType.ERROR);
        }

        return result;

    }

    public void displayInvoiceID(String ID){

        id.setText(ID);

    }

}
