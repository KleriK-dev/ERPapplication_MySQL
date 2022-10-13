package controllers.main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class HelpController {

    @FXML
    private Button ItemButton;

    @FXML
    private Button customerButton;

    @FXML
    private Button customerDocumentButton;

    @FXML
    private Button itemTransactionButton;

    @FXML
    private Button newCustomerDocumentButton;

    @FXML
    private Button newSupplierDocumentButton;

    @FXML
    private Button supplierButton;

    @FXML
    private Button supplierDocumentButton;

    @FXML
    void OpenCustomerHelp(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/instructions/CustomerInstructions.fxml"));
        Parent root = loader.load();

        Stage newCustomerInstructionStage = new Stage();
        Scene newCustomerInstructionScene = new Scene(root);
        Image image = new Image(getClass().getResourceAsStream("/images/logo.png"));
        newCustomerInstructionStage.getIcons().add(image);
        newCustomerInstructionStage.setX(303);
        newCustomerInstructionStage.setY(106);
        newCustomerInstructionStage.setScene(newCustomerInstructionScene);
        newCustomerInstructionStage.setTitle("Instructions");
        newCustomerInstructionStage.setAlwaysOnTop(true);
        newCustomerInstructionStage.show();

    }

    @FXML
    void OpenCustomerDocumentHelp(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/instructions/CustomerInvoiceInstructions.fxml"));
        Parent root = loader.load();

        Stage customerInvoiceInstructionStage = new Stage();
        Scene customerInvoiceInstructionScene = new Scene(root);
        Image image = new Image(getClass().getResourceAsStream("/images/logo.png"));
        customerInvoiceInstructionStage.getIcons().add(image);
        customerInvoiceInstructionStage.setX(303);
        customerInvoiceInstructionStage.setY(106);
        customerInvoiceInstructionStage.setScene(customerInvoiceInstructionScene);
        customerInvoiceInstructionStage.setTitle("Instructions");
        customerInvoiceInstructionStage.setAlwaysOnTop(true);
        customerInvoiceInstructionStage.show();

    }

    @FXML
    void OpenItemHelp(ActionEvent event) throws IOException {

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

    @FXML
    void OpenItemTransactionHelp(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/instructions/ItemTransactionInstructions.fxml"));
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
    void OpenNewCustomerDocumentHelp(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/instructions/NewCustomerInvoiceInstructions.fxml"));
        Parent root = loader.load();

        Stage newCustomerInvoiceInstructionStage = new Stage();
        Scene newCustomerInvoiceInstructionScene = new Scene(root);
        Image image = new Image(getClass().getResourceAsStream("/images/logo.png"));
        newCustomerInvoiceInstructionStage.getIcons().add(image);
        newCustomerInvoiceInstructionStage.setX(303);
        newCustomerInvoiceInstructionStage.setY(106);
        newCustomerInvoiceInstructionStage.setScene(newCustomerInvoiceInstructionScene);
        newCustomerInvoiceInstructionStage.setTitle("Instructions");
        newCustomerInvoiceInstructionStage.setAlwaysOnTop(true);
        newCustomerInvoiceInstructionStage.show();

    }

    @FXML
    void OpenNewSupplierDocumentHelp(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/instructions/NewSupplierInvoiceInstructions.fxml"));
        Parent root = loader.load();

        Stage newSupplierInvoiceInstructionStage = new Stage();
        Scene newSupplierInvoiceInstructionScene = new Scene(root);
        Image image = new Image(getClass().getResourceAsStream("/images/logo.png"));
        newSupplierInvoiceInstructionStage.getIcons().add(image);
        newSupplierInvoiceInstructionStage.setX(303);
        newSupplierInvoiceInstructionStage.setY(106);
        newSupplierInvoiceInstructionStage.setScene(newSupplierInvoiceInstructionScene);
        newSupplierInvoiceInstructionStage.setTitle("Instructions");
        newSupplierInvoiceInstructionStage.setAlwaysOnTop(true);
        newSupplierInvoiceInstructionStage.show();

    }

    @FXML
    void OpenSupplierDocumentHelp(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/instructions/SupplierInvoiceInstructions.fxml"));
        Parent root = loader.load();

        Stage supplierInvoiceInstructionStage = new Stage();
        Scene supplierInvoiceInstructionScene = new Scene(root);
        Image image = new Image(getClass().getResourceAsStream("/images/logo.png"));
        supplierInvoiceInstructionStage.getIcons().add(image);
        supplierInvoiceInstructionStage.setX(303);
        supplierInvoiceInstructionStage.setY(106);
        supplierInvoiceInstructionStage.setScene(supplierInvoiceInstructionScene);
        supplierInvoiceInstructionStage.setTitle("Instructions");
        supplierInvoiceInstructionStage.setAlwaysOnTop(true);
        supplierInvoiceInstructionStage.show();

    }

    @FXML
    void OpenSupplierHelp(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/instructions/SupplierInstructions.fxml"));
        Parent root = loader.load();

        Stage newSupplierInstructionStage = new Stage();
        Scene newSupplierInstructionScene = new Scene(root);
        Image image = new Image(getClass().getResourceAsStream("/images/logo.png"));
        newSupplierInstructionStage.getIcons().add(image);
        newSupplierInstructionStage.setX(303);
        newSupplierInstructionStage.setY(106);
        newSupplierInstructionStage.setScene(newSupplierInstructionScene);
        newSupplierInstructionStage.setTitle("Instructions");
        newSupplierInstructionStage.setAlwaysOnTop(true);
        newSupplierInstructionStage.show();

    }

}
