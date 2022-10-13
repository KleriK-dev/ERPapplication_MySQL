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

public class ViewCustomerInvoiceController {

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
    private TextField discountvalue;

    @FXML
    private TextField from;

    @FXML
    private TextField id;

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
    private TextField purposeOfTracking;

    @FXML
    private TextField quantity;

    @FXML
    private TextField registrationUser;

    @FXML
    private Button printButton;

    @FXML
    private TextField to;

    @FXML
    private TextField total;

    @FXML
    private TextField valueBeforeVAT;

    @FXML
    private TextField dateTextfield;

    @FXML
    private TextField timeTextfield;

    @FXML
    private TextField discountInvoice;

    @FXML
    private TextField remarks;

    @FXML
    private CheckBox handwritedCheckbox;

    private CustomerInvoice invoice;
    SpecialAlert alert = new SpecialAlert();
    CommonMethods commons = new CommonMethods();
    ObservableList<ItemTransaction> oblist = FXCollections.observableArrayList(); // create observablelist to get the data from query and display them to table


    @FXML
    private void PrintInvoice(ActionEvent event) {

        //TO DO

    }

    @FXML
    private void CloseCustomerInvoiceWindow(ActionEvent event) {

        Stage customerStage = (Stage) cancelButton.getScene().getWindow();
        customerStage.close();

    }

    public void getData(CustomerInvoice selectedInvoice){

        invoice = selectedInvoice;

        id.setText(String.valueOf(invoice.getCustomerInvoiceID()));
        dateTextfield.setText(invoice.getInsertionDate());
        timeTextfield.setText(invoice.getInsertionTime());
        customer_id.setText(String.valueOf(invoice.getCustomerID()));
        customer_brandname.setText(invoice.getCustomerBrandname());
        invoicetype_id.setText(String.valueOf(invoice.getInvoiceTypeID()));
        invoicetype_description.setText(invoice.getInvoiceTypeDescription());
        invoicenumber.setText(invoice.getInvoiceNumber());
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
        handwritedCheckbox.setSelected(invoice.getHandwrited() == 1);
        remarks.setText(invoice.getRemarks());

    }

    public void getData(String invoiceID){

        Connection conn = MySQLConnection.connectToDB();
        String query = "SELECT * FROM customer_invoices " +
                "WHERE id = " + invoiceID;

        Statement st;
        ResultSet rs;

        Integer InvoiceID = null;
        String invoiceDate = null;
        String invoiceTime = null;
        Integer customerID = null;
        Integer invoiceType = null;
        String invoiceNumber = null;
        Integer invoicePayingWayID = null;
        String invoiceTrackingPurpose = null;
        String fromPlace = null;
        String toPlace = null;
        String invoiceLicensePLate = null;
        Integer invoiceVATregimeID = null;
        Integer programUserID = null;
        Double initialValue = null;
        Double discountPercentage = null;
        Double discountValue = null;
        Double valueNoVAT = null;
        Double vatvalue = null;
        Double totalQuantity = null;
        Double invoiceTotal = null;
        String remark = null;
        Integer handwritten = null;

        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {

                InvoiceID = rs.getInt("id");
                invoiceDate = rs.getString("date");
                invoiceTime = rs.getString("time");
                customerID = rs.getInt("customer_id");
                invoiceType = rs.getInt("invoicetype_id");
                invoiceNumber = rs.getString("invoice_number");
                invoicePayingWayID = rs.getInt("payingway_id");
                invoiceTrackingPurpose = rs.getString("purpose_of_tracking");
                fromPlace = rs.getString("from_place");
                toPlace = rs.getString("to_place");
                invoiceLicensePLate = rs.getString("license_plate");
                invoiceVATregimeID = rs.getInt("vatregime_id");
                programUserID = rs.getInt("user_id");
                initialValue = rs.getDouble("initial_value");
                discountPercentage = rs.getDouble("discount_percent");
                discountValue = rs.getDouble("discount_value");
                valueNoVAT = rs.getDouble("value_beforevat");
                vatvalue = rs.getDouble("vat_value");
                totalQuantity = rs.getDouble("quantity");
                invoiceTotal = rs.getDouble("total");
                remark = rs.getString("remarks");
                handwritten = rs.getInt("handwrited_invoice");

            }

            id.setText(String.valueOf(InvoiceID));
            dateTextfield.setText(invoiceDate);
            timeTextfield.setText(invoiceTime);
            customer_id.setText(String.valueOf(customerID));
            customer_brandname.setText(getCustomerBrandname(customer_id.getText()));
            invoicetype_id.setText(String.valueOf(invoiceType));
            invoicetype_description.setText(getInvoiceTypeDescription(invoicetype_id.getText()));
            invoicenumber.setText(invoiceNumber);
            payingway_id.setText(String.valueOf(invoicePayingWayID));
            payingway_description.setText(commons.getSelectionDescription("paying_way", String.valueOf(invoicePayingWayID)));
            purposeOfTracking.setText(invoiceTrackingPurpose);
            from.setText(fromPlace);
            to.setText(toPlace);
            licenseplate.setText(invoiceLicensePLate);
            VATregime.setText(commons.getSelectionDescription("vat_regime", String.valueOf(invoiceVATregimeID)));
            registrationUser.setText(commons.insertedByUser(String.valueOf(programUserID)));
            initialvalue.setText(String.valueOf(initialValue));
            discountInvoice.setText(String.valueOf(discountPercentage));
            discountvalue.setText(String.valueOf(discountValue));
            valueBeforeVAT.setText(String.valueOf(valueNoVAT));
            VAT.setText(String.valueOf(vatvalue));
            quantity.setText(String.valueOf(totalQuantity));
            total.setText(String.valueOf(invoiceTotal));
            remarks.setText(remark);
            handwritedCheckbox.setSelected(handwritten == 1);

        } catch (SQLException e) {
            alert.show("Error", "Error code: " + e.getErrorCode() + "\nError message: " + e.getMessage() + "\nSQL state is: " + e.getSQLState(), Alert.AlertType.ERROR);
        }

    }

    public void loadTableWithData(String invoiceID, Integer transactionCode){

        Connection conn = MySQLConnection.connectToDB();
        String query = "SELECT * FROM item_transactions1 " +
                "WHERE customer_invoiceID = " + invoiceID + " AND transaction_code = " + transactionCode;

        PreparedStatement prst;
        Statement st;
        ResultSet rs;

        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                oblist.add(new ItemTransaction(null, null, null, rs.getString("item_code"), rs.getString("item_description"), rs.getDouble("quantity"), rs.getDouble("unit_price"), rs.getDouble("vat"), rs.getDouble("discount"), rs.getString("etiology"), rs.getDouble("total"), null, null));
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
            alert.show("Error", "Unknown error occurred! STO LOAD TABLE", Alert.AlertType.ERROR);
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
            alert.show("Error", "Unknown error occured! STO GET BRANDNAME", Alert.AlertType.ERROR);
        }

        return result;

    }

    public String getInvoiceTypeDescription(String invoiceTypeID){

        Connection conn = MySQLConnection.connectToDB();
        String query = "SELECT description FROM type_of_customer_document WHERE id = " + invoiceTypeID;
        Statement st;
        ResultSet rs;

        String result = null;

        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                result = rs.getString("description");
            }


        } catch (SQLException ex) {
            alert.show("Error", "Unknown error occurred! STO INVOICE TYPE DESCRIPTION", Alert.AlertType.ERROR);
        }

        return result;

    }

}
