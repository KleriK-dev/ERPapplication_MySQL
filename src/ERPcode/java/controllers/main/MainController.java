package controllers.main;

import commons.CommonMethods;
import controllers.customerInvoices.CustomerInvoiceMainController;
import controllers.customerInvoices.NewCustomerInvoiceController;
import controllers.customers.CustomersMainController;
import controllers.supplierInvoices.SupplierInvoiceMainController;
import controllers.suppliers.SupplierMainController;
import database.DatabaseMaintenance;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import objects.User;
import specialAlerts.SpecialAlert;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class MainController {

    @FXML
    private Button SupplierButton1;

    @FXML
    private Button SupplierInvoiceButton1;

    @FXML
    private Button SupplierInvoiceButton2;

    @FXML
    private Button SuppliersButton2;

    @FXML
    private Button customerButton1;

    @FXML
    private Button customerButton2;

    @FXML
    private Button customerInvoiceButton2;

    @FXML
    private MenuItem exitMenuButton;

    @FXML
    private Button navCustInvButton;

    @FXML
    private Button NewCustInvoiceButton;

    @FXML
    private MenuItem smallCustInvButton;

    @FXML
    private MenuItem smallCustomerButton;

    @FXML
    private MenuItem smallSupplierButton;

    @FXML
    private MenuItem smallSupplierInvoiceButton;

    @FXML
    private MenuItem transactionButton2;

    @FXML
    private MenuItem helpButton;

    @FXML
    private Button itemsButton1;

    @FXML
    private Button itemsButton2;

    @FXML
    private Button transactionButton1;

    @FXML
    private Button businessDataButton;

    @FXML
    private Button backupButton;

    @FXML
    private Button restoreButton;

    @FXML
    private MenuItem itemsMenuButton;

    @FXML
    private MenuItem ChangeUserMenu;

    @FXML
    private Label time;

    @FXML
    private Label programmeUser;

    @FXML
    private MenuItem usersMenu;

    CommonMethods commons = new CommonMethods();
    SpecialAlert alert = new SpecialAlert();
    private double xOffset = 0;
    private double yOffset = 0;
    private User user;

    @FXML
    public void initialize() {
        initClock();
    }

    private void initClock(){ //method that displays current date and time

        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy   HH:mm:ss");
            time.setText(LocalDateTime.now().format(formatter));
        }), new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();

    }

    @FXML
    private void OpenHelpWindow(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/instructions/HelpWindow.fxml"));
        Parent root = loader.load();

        Stage helpStage = new Stage();
        Scene helpScene = new Scene(root);
        Image image = new Image(getClass().getResourceAsStream("/images/help.png"));
        helpStage.getIcons().add(image);
        helpStage.centerOnScreen();
        helpStage.setScene(helpScene);
        helpStage.setAlwaysOnTop(true);
        helpStage.setTitle("Help");
        helpStage.show();

    }

    @FXML
    private void openCustomersWindow(ActionEvent event) throws IOException { //buttons that when they are pressed open new Stage

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/customers/CustomerMainWindow.fxml"));
        Parent root = loader.load();

        CustomersMainController customersMainController = loader.getController(); //get access to the methods of CustomersMainController
        customersMainController.showProgramUser(programmeUser.getText()); //pass the username that has logged in

        Stage customerStage = new Stage();
        Scene customerScene = new Scene(root);
        Image image = new Image(getClass().getResourceAsStream("/images/logo.png"));
        customerStage.getIcons().add(image);
        customerStage.setX(203);
        customerStage.setY(106);
        customerStage.setScene(customerScene);
        customerStage.setAlwaysOnTop(true);
        customerStage.setTitle("Customers");
        customerStage.setResizable(false);
        customerStage.show();

    }

    @FXML
    private void OpenCustomerInvoiceWindow(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/customerInvoices/CustomerInvoiceMainWindow.fxml"));
        Parent root = loader.load();

        CustomerInvoiceMainController customerInvoiceMainController = loader.getController(); //get access to the methods of CustomerInvoiceMainController
        customerInvoiceMainController.showProgramUser(programmeUser.getText()); //pass the username that has logged in

        Stage customerInvoiceStage = new Stage();
        Scene customerInvoiceScene = new Scene(root);
        Image image = new Image(getClass().getResourceAsStream("/images/logo.png"));
        customerInvoiceStage.getIcons().add(image);
        customerInvoiceStage.setX(203);
        customerInvoiceStage.setY(106);
        customerInvoiceStage.setScene(customerInvoiceScene);
        customerInvoiceStage.setTitle("Customer Invoices");
        customerInvoiceStage.setAlwaysOnTop(true);
        customerInvoiceStage.setResizable(false);
        customerInvoiceStage.show();

    }

    @FXML
    private void OpenNewCustomerInvoiceWindow(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/customerInvoices/NewCustomerInvoiceWindow.fxml"));
        Parent root = loader.load();

        //pass user to newCustomerInvoice Controller
        NewCustomerInvoiceController newCustomerInvoiceController = loader.getController();
        newCustomerInvoiceController.showProgramUser(programmeUser.getText());
        newCustomerInvoiceController.displayInvoiceID(commons.getNextID("customer_invoices", "id"));

        Stage newCustomerInvoiceStage = new Stage();
        Scene newCustomerInvoiceScene = new Scene(root);
        Image image = new Image(getClass().getResourceAsStream("/images/logo.png"));
        newCustomerInvoiceStage.getIcons().add(image);
        newCustomerInvoiceStage.setX(203);
        newCustomerInvoiceStage.setY(106);
        newCustomerInvoiceStage.setScene(newCustomerInvoiceScene);
        newCustomerInvoiceStage.setTitle("Customer invoice (New invoice)");
        newCustomerInvoiceStage.setAlwaysOnTop(true);
        newCustomerInvoiceStage.setResizable(false);
        newCustomerInvoiceStage.show();

        // handle when NewCustomerInvoice window is closed by X button
        newCustomerInvoiceStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {

                we.consume(); // consume the event so that the window does not close when cancel button is pressed to the confirmation window

                Optional<ButtonType> result = alert.showConfirmation("Cancel confirmation", "This invoice will be lost with all the items, are you sure you want to continue?");

                if(result.get() == ButtonType.OK) { // if its pressed OK

                    newCustomerInvoiceStage.close();

                }
            }
        });

    }

    @FXML
    private void OpenSupplierWindow(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/suppliers/SupplierMainWindow.fxml"));
        Parent root = loader.load();

        SupplierMainController supplierMainController = loader.getController(); //get access to the methods of SupplierMainController
        supplierMainController.showProgramUser(programmeUser.getText()); //pass the username that has logged in

        Stage supplierStage = new Stage();
        Scene supplierScene = new Scene(root);
        Image image = new Image(getClass().getResourceAsStream("/images/logo.png"));
        supplierStage.getIcons().add(image);
        supplierStage.setX(203);
        supplierStage.setY(106);
        supplierStage.setScene(supplierScene);
        supplierStage.setTitle("Suppliers");
        supplierStage.setAlwaysOnTop(true);
        supplierStage.setResizable(false);
        supplierStage.show();

    }

    @FXML
    void OpenItemsWindow(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/items/ItemMainWindow.fxml"));
        Stage itemStage = new Stage();
        Scene itemScene = new Scene(root);
        Image image = new Image(getClass().getResourceAsStream("/images/logo.png"));
        itemStage.getIcons().add(image);
        itemStage.setX(203);
        itemStage.setY(106);
        itemStage.setScene(itemScene);
        itemStage.setTitle("Items");
        itemStage.setAlwaysOnTop(true);
        itemStage.setResizable(false);
        itemStage.show();

    }

    @FXML
    private void OpenTransactionWindow(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/itemTransactions/ItemTransactionMainWindow.fxml"));
        Stage itemTransactionStage = new Stage();
        Scene itemTransactionScene = new Scene(root);
        Image image = new Image(getClass().getResourceAsStream("/images/logo.png"));
        itemTransactionStage.getIcons().add(image);
        itemTransactionStage.setX(208);
        itemTransactionStage.setY(106);
        itemTransactionStage.setScene(itemTransactionScene);
        itemTransactionStage.setTitle("Product transactions");
        itemTransactionStage.setAlwaysOnTop(true);
        itemTransactionStage.setResizable(false);
        itemTransactionStage.show();

    }

    @FXML
    private void OpenSupplierInvoiceWindow(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/supplierInvoices/SupplierInvoiceMainWindow.fxml"));
        Parent root = loader.load();

        SupplierInvoiceMainController supplierInvoiceMainController = loader.getController(); //get access to the methods of SupplierInvoiceMainController
        supplierInvoiceMainController.showProgramUser(programmeUser.getText()); //pass the username that has logged in

        Stage supplierInvoiceStage = new Stage();
        Scene supplierInvoiceScene = new Scene(root);
        Image image = new Image(getClass().getResourceAsStream("/images/logo.png"));
        supplierInvoiceStage.getIcons().add(image);
        supplierInvoiceStage.setX(203);
        supplierInvoiceStage.setY(106);
        supplierInvoiceStage.setScene(supplierInvoiceScene);
        supplierInvoiceStage.setTitle("Supplier Invoices");
        supplierInvoiceStage.setAlwaysOnTop(true);
        supplierInvoiceStage.setResizable(false);
        supplierInvoiceStage.show();

    }

    @FXML
    private void OpenBusinessData(ActionEvent event) throws IOException{

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/toolbox/BusinessDataWindow.fxml"));
        Stage businessDataStage = new Stage();
        Scene businessDataScene = new Scene(root);
        Image image = new Image(getClass().getResourceAsStream("/images/logo.png"));
        businessDataStage.getIcons().add(image);
        businessDataStage.setX(203);
        businessDataStage.setY(106);
        businessDataStage.setScene(businessDataScene);
        businessDataStage.setTitle("Business Information");
        businessDataStage.setAlwaysOnTop(true);
        businessDataStage.setResizable(false);
        businessDataStage.initModality(Modality.APPLICATION_MODAL);
        businessDataStage.show();

    }

    @FXML
    private void OpenBackupWindow(ActionEvent event) throws IOException{

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/toolbox/BackupWindow.fxml"));
        Stage backupStage = new Stage();
        Scene backupScene = new Scene(root);
        Image image = new Image(getClass().getResourceAsStream("/images/logo.png"));
        backupStage.getIcons().add(image);
        backupStage.centerOnScreen();
        backupStage.setScene(backupScene);
        backupStage.setTitle("Backup Tool");
        backupStage.setResizable(false);
        backupStage.show();

    }

    @FXML
    private void OpenRestoreWindow(ActionEvent event) throws IOException{

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/toolbox/RestoreWindow.fxml"));
        Stage backupStage = new Stage();
        Scene backupScene = new Scene(root);
        Image image = new Image(getClass().getResourceAsStream("/images/logo.png"));
        backupStage.getIcons().add(image);
        backupStage.centerOnScreen();
        backupStage.setScene(backupScene);
        backupStage.setTitle("Restore Tool");
        backupStage.setResizable(false);
        backupStage.show();

    }

    @FXML
    private void OpenCleanDatabaseWindow(ActionEvent event) throws IOException{

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/toolbox/CleanDatabaseWindow.fxml"));
        Stage cleanDatabaseStage = new Stage();
        Scene cleanDatabaseScene = new Scene(root);
        Image image = new Image(getClass().getResourceAsStream("/images/logo.png"));
        cleanDatabaseStage.getIcons().add(image);
        cleanDatabaseStage.centerOnScreen();
        cleanDatabaseStage.setScene(cleanDatabaseScene);
        cleanDatabaseStage.setTitle("Clean Database Tool");
        cleanDatabaseStage.setResizable(false);
        cleanDatabaseStage.show();

    }

    @FXML
    void OpenUsersWindow(ActionEvent event) throws IOException {

        if(programmeUser.getText().equals("admin")){ // get the text from username label to check if the logged in person is the administrator
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/users/UsersMainWindow.fxml"));
            Stage userStage = new Stage();
            Scene userScene = new Scene(root);
            Image image = new Image(getClass().getResourceAsStream("/images/logo.png"));
            userStage.getIcons().add(image);
            userStage.setX(203);
            userStage.setY(106);
            userStage.setScene(userScene);
            userStage.setTitle("List of Users");
            userStage.setAlwaysOnTop(true);
            userStage.setResizable(false);
            userStage.show();
        } else {
            alert.show("Warning","You do not have privileges for that action as you are not the administrator!", Alert.AlertType.WARNING);
        }

    }

    @FXML
    private void OpenLoginWindow(ActionEvent event) throws IOException {

        Optional<ButtonType> result = alert.showConfirmation("Exit confirmation", "Do you want to exit and change user?");

        if(result.get() == ButtonType.OK){
            Stage customerStage = (Stage) ChangeUserMenu.getParentPopup().getOwnerWindow();
            Scene customerScene = customerStage.getScene();
            customerStage.close();

            Parent root = FXMLLoader.load(getClass().getResource("/fxml/main/LoginWindow.fxml"));
            Stage loginStage = new Stage();
            loginStage.initStyle(StageStyle.UNDECORATED);

            root.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    xOffset = event.getSceneX();
                    yOffset = event.getSceneY();
                }
            });

            root.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    loginStage.setX(event.getScreenX() - xOffset);
                    loginStage.setY(event.getScreenY() - yOffset);
                }
            });

            Image image = new Image(getClass().getResourceAsStream("/images/logo.png"));
            loginStage.getIcons().add(image);

            Scene scene = new Scene(root);
            loginStage.setScene(scene);
            loginStage.show();

        }

    }

    @FXML
    private void exit(ActionEvent event) {

        Optional<ButtonType> result = alert.showConfirmation("Exit confirmation", "Do you want to exit? \nIt's recommended to take a backup before exiting!");

        if(result.get() == ButtonType.OK){
            Platform.exit();
            System.exit(0);
        }

    }

    public void showProgramUser(User userName){ //display who is the user

        user = userName;

        programmeUser.setText(user.getUsername());

    }


}
