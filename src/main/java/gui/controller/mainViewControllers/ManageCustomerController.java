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

    /**
     * We use this to set our username label and window title label.
     */
    private void setUsernameLabel() {// set our username label to the users name
        usernameLabel.setText(authenticationModel.getLoggedInUser().getFirstName() + " " + authenticationModel.getLoggedInUser().getLastName());
    }

    /**
     * We use this to set up the tableview customerTV with relative columns.
     */
    private void setCustomerTableView() {
        customersTV.setItems(customerModel.getCustomersWithWrapper());
        customerModel.loadCustomersWithWrapper();

        customerAddress.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustomer().getCustAddress()));
        postalCode.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPostalCode().getPostalCode()));
        city.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPostalCode().getCity()));
        customerName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustomer().getCustName()));
        customerEmail.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustomer().getCustEmail()));
        projectName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProject().getProjName()));

        customersTV.getSelectionModel().selectedItemProperty().addListener(((((observable, oldValue, selection) -> {
            if (selection != null) {
                projectModel.setSelectedProject(selection.getProject());
                customerModel.setSelectedCustomer(selection.getCustomer());
            }
        }))));
    }

    @FXML
    private void openDocumentWindow(MouseEvent mouseEvent) throws IOException {
        try {
            if (mouseEvent.getClickCount() == 2) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/mainViews/DocumentationView.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setTitle("Documentation Manager");
                stage.setScene(scene);
                stage.show();
            }
        } catch (IOException e) {
            String str = "There has been an error loading DocumentationView.fxml. Please contact system Admin.";
            super.createWarning(str);
        }
    }

    @FXML
    private void openDocumentButton(ActionEvent actionEvent) {
        openDocumentation();
    }

    private void openDocumentation(){
        try {
            projectModel.setSelectedProject(customersTV.getSelectionModel().getSelectedItem().getProject());
            Node n = FXMLLoader.load(getClass().getResource("/gui/view/mainViews/DocumentationView.fxml"));
            super.getViewPane().getChildren().setAll(n);
        } catch (IOException e) {
            String str = "There has been an error loading DocumentationView.fxml. Please contact system Admin.";
            super.createWarning(str);
        }
    }
    @FXML
    private void editCustomer(ActionEvent actionEvent) {
        if(customersTV.getSelectionModel().getSelectedItem() != null){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/newAndUpdateViews/NECustomer.fxml"));
            Parent root = loader.load();
            NECustomerController controller = loader.getController();
            controller.setNEController(true);
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setTitle("Edit Customer");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            String str = "There has been an error loading NECustomer.fxml. Please contact system Admin.";
            super.createWarning(str);
        }
        }
    }

    @FXML
    private void createCustomer(ActionEvent actionEvent) {
        newCustomer();
    }

    private void newCustomer(){
        if(!authenticationModel.getLoggedInUser().getAccess().toUpperCase().equals("SALES")){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/newAndUpdateViews/NECustomer.fxml"));
            Parent root = loader.load();
            NECustomerController controller = loader.getController();
            controller.setNEController(false);
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setTitle("New customer");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            String str = "There has been an error loading NECustomer.fxml. Please contact system Admin.";
            super.createWarning(str);
        }
        }
    }

    /**
     * This will log the user out and change the view to the login.
     * We catch the IOException and show the user a crafted alert.
     *
     * @param actionEvent triggered by the logout button.
     */
    @FXML
    private void logOut(ActionEvent actionEvent) {
        super.logout(actionEvent);
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(authenticationModel.getLoggedInUser().getAccess().toUpperCase().equals("SALES")){
           editCustomerButton.setVisible(false);
           newCustomerButton.setVisible(false);
        }

        setCustomerTableView();
        setUsernameLabel();
        searchCustomer.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                customerModel.search(newValue);
            }
        });
    }

    @FXML private void onCustomerTVClick(MouseEvent mouseEvent) {
        if(customersTV.getSelectionModel().getSelectedItem() != null){
            customerModel.setSelectedCustomer(customersTV.getSelectionModel().getSelectedItem().getCustomer());
            projectModel.setSelectedProject(customersTV.getSelectionModel().getSelectedItem().getProject());

            if(mouseEvent.getClickCount() == 2){
                openDocumentation();
            }
        }

        if(customersTV.getSelectionModel().getSelectedItem() == null){
            if(mouseEvent.getClickCount() == 2){
                newCustomer();
            }
        }

    }

    @FXML private void anchorOnClick(MouseEvent mouseEvent) {
        if(customersTV.getSelectionModel().getSelectedItem() != null){
            customersTV.getSelectionModel().clearSelection();
        }
        customersTV.refresh();
    }
}
