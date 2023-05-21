package gui.controller.mainViewControllers;

import be.*;
import gui.controller.newAndUpdateControllers.*;
import gui.model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.*;
import org.apache.logging.log4j.*;


import java.io.*;
import java.net.*;
import java.util.*;

public class ManageCustomerController extends BaseController implements Initializable {


    @FXML private AnchorPane customerAnchor;
    // Tableview
    @FXML
    private TableView<CustomerWrapper> customersTV;
    @FXML
    private TableColumn<CustomerWrapper, String> customerEmail;
    @FXML
    private TableColumn<CustomerWrapper, String> customerName;
    @FXML
    private TableColumn<CustomerWrapper, String> customerAddress;
    @FXML
    private TableColumn<CustomerWrapper, String> projectName;
    @FXML
    private TableColumn<CustomerWrapper, String> postalCode;
    @FXML
    private TableColumn<CustomerWrapper, String> city;

    // TextField
    @FXML
    private TextField searchCustomer;

    // Labels
    @FXML
    private Label usernameLabel;
    @FXML
    private Label windowTitle;

    // Buttons
    @FXML
    private Button newCustomerButton;
    @FXML
    private Button editCustomerButton;

    // Models
    private CustomerModel customerModel = CustomerModel.getInstance();
    private AuthenticationModel authenticationModel = AuthenticationModel.getInstance();
    private ProjectModel projectModel = ProjectModel.getInstance();

    private static final Logger logger = LogManager.getLogger("debugLogger");

    /**
     * We use this to set our username label and window title label.
     */
    private void setUsernameLabel() {// set our username label to the users name
        logger.trace("Setting username label in " + this.getClass().getName());
        usernameLabel.setText(authenticationModel.getLoggedInUser().getFirstName() + " " + authenticationModel.getLoggedInUser().getLastName());
    }

    /**
     * We use this to set up the tableview customerTV with relative columns.
     */
    private void setCustomerTableView() {
        logger.info("setCustomerTableView() called in " + this.getClass().getName());
        customersTV.setItems(customerModel.getCustomersWithWrapper());
        customerModel.loadCustomersWithWrapper();

        logger.trace("Setting columns of customersTV");
        customerAddress.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustomer().getCustAddress()));
        postalCode.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPostalCode().getPostalCode()));
        city.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPostalCode().getCity()));
        customerName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustomer().getCustName()));
        customerEmail.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustomer().getCustEmail()));
        projectName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProject().getProjName()));

        logger.trace("adding listener to customersTV");
        customersTV.getSelectionModel().selectedItemProperty().addListener(((((observable, oldValue, selection) -> {
            if (selection != null) {
                projectModel.setSelectedProject(selection.getProject());
                customerModel.setSelectedCustomer(selection.getCustomer());
            }
        }))));
        logger.info("setCustomerTableView() complete.");
    }

    @FXML
    private void openDocumentButton(ActionEvent actionEvent) {
        if(customersTV.getSelectionModel().getSelectedItem() != null){
        openDocumentation();}
    }

    private void openDocumentation(){
        logger.info("openDocumentation() called in " + this.getClass().getName());
        try {
            projectModel.setSelectedProject(customersTV.getSelectionModel().getSelectedItem().getProject());
            logger.info("Loading DocumentationView.fxml");
            Node n = FXMLLoader.load(getClass().getResource("/gui/view/mainViews/DocumentationView.fxml"));
            super.getViewPane().getChildren().setAll(n);
        } catch (IOException e) {
            logger.error("There has been a problem loading DocumentationView.fxml.", e);
            String str = "There has been an error loading DocumentationView.fxml. Please contact system Admin.";
            super.createWarning(str);
        }
        logger.info("openDocumentation() complete.");
    }

    @FXML
    private void createCustomer(ActionEvent actionEvent) {
        openCustomerWindow(false);
    }
    @FXML
    private void editCustomer(ActionEvent actionEvent) {
        if(customersTV.getSelectionModel().getSelectedItem() != null){openCustomerWindow(true);}
    }

    private void openCustomerWindow(boolean isEdit){
        logger.info("openCustomerWindow called in " + this.getClass().getName());

            try {
                logger.info("Loading NECustomer.fxml");
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/newAndUpdateViews/NECustomer.fxml"));
                Parent root = loader.load();
                NECustomerController controller = loader.getController();
                controller.setNEController(isEdit);
                Stage stage = new Stage();
                Scene scene = new Scene(root);

                if(isEdit){
                    stage.setTitle("Edit Customer");
                } else {
                    stage.setTitle("New Customer");
                }

                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                logger.error("There has been a problem loading NECustomer.fxml.",e);
                String str = "There has been an error loading NECustomer.fxml. Please contact system Admin.";
                super.createWarning(str);
            }

    }


    /**
     * This will log the user out and change the view to the login.
     * @param actionEvent triggered by the logout button.
     */
    @FXML
    private void logOut(ActionEvent actionEvent) {
        super.logout(actionEvent);
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        logger.trace("Initializing ManageCustomerController");
        if(authenticationModel.getLoggedInUser().getAccess().toUpperCase().equals("SALES")){
           editCustomerButton.setVisible(false);
           newCustomerButton.setVisible(false);
        }

        setCustomerTableView();
        setUsernameLabel();
        logger.info("Adding listener for search customer in ManageCustomersController.");
        searchCustomer.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                customerModel.search(newValue);
            }
        });
    }

    @FXML private void onCustomerTVClick(MouseEvent mouseEvent) {
        logger.trace("User clicked customerTV in " + this.getClass().getName());
        if(customersTV.getSelectionModel().getSelectedItem() != null){
            customerModel.setSelectedCustomer(customersTV.getSelectionModel().getSelectedItem().getCustomer());
            projectModel.setSelectedProject(customersTV.getSelectionModel().getSelectedItem().getProject());

            if(mouseEvent.getClickCount() == 2){
                openDocumentation();
            }
        }

        if(customersTV.getSelectionModel().getSelectedItem() == null){
            if(mouseEvent.getClickCount() == 2){
                openCustomerWindow(false);
            }
        }

    }

    @FXML private void anchorOnClick(MouseEvent mouseEvent) {
        logger.trace("User clicked the anchor pane in " + this.getClass().getName());
        if(customersTV.getSelectionModel().getSelectedItem() != null){
            customersTV.getSelectionModel().clearSelection();
        }
        customersTV.refresh();
    }


}
