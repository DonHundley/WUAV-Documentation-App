package gui.controller;

import be.*;
import gui.model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.*;


import java.io.*;
import java.net.*;
import java.util.*;

public class ManageCustomerController implements Initializable{

    // Tableview
    @FXML private TableView<CustomerWrapper> customersTV;
    @FXML private TableColumn<CustomerWrapper, String> customerEmail;
    @FXML private TableColumn<CustomerWrapper, String> customerName;
    @FXML private TableColumn<CustomerWrapper, String> customerAddress;
    @FXML private TableColumn<CustomerWrapper, String> projectName;

    // TextField
    @FXML private TextField searchCustomer;

    // Labels
    @FXML private Label usernameLabel;
    @FXML private Label windowTitle;

    // Models
    private Persistent persistenceModel = Persistent.getInstance();
    private Observables observablesModel = Observables.getInstance();
    private Functions functionsModel = new Functions();



    /**
     * We use this to set our username label and window title label.
     */
    private void setUsernameLabel() {// set our username label to the users name
        windowTitle.setText("Customers");
        usernameLabel.setText(persistenceModel.getLoggedInUser().getFirstName() + " " + persistenceModel.getLoggedInUser().getLastName());
    }

    /**
     * We use this to set up the tableview customerTV with relative columns.
     */
    private void setCustomerTableView() {
        customersTV.setItems(observablesModel.getCustomers());
        observablesModel.loadCustomers();

        customerAddress.setCellValueFactory(cellData-> new SimpleStringProperty(cellData.getValue().getCustomer().getCustAddress()));
        customerName.setCellValueFactory(cellData-> new SimpleStringProperty(cellData.getValue().getCustomer().getCustName()));
        customerEmail.setCellValueFactory(cellData-> new SimpleStringProperty(cellData.getValue().getCustomer().getCustEmail()));
        projectName.setCellValueFactory(cellData-> new SimpleStringProperty(cellData.getValue().getProject().getProjName()));

        customersTV.getSelectionModel().selectedItemProperty().addListener(((((observable, oldValue, selectedProject) ->
                persistenceModel.setSelectedProject(selectedProject.getProject())
        ))));
    }

    @FXML private void openDocumentWindow(MouseEvent mouseEvent) throws IOException {
        try {
            if (mouseEvent.getClickCount() == 2) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/DocumentationView.fxml"));
                Parent root = loader.load();
                //DocumentationController controller = loader.getController();
                //controller.userController(persistenceModel, observablesModel, functionsModel);
                Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setTitle("Documentation Manager");
                stage.setScene(scene);
                stage.show();
            }
        }catch (IOException e){
            String str = "There has been an error loading DocumentationView.fxml. Please contact system Admin.";
            customerError(str);
        }
    }

    @FXML private void openDocumentButton(ActionEvent actionEvent) {
        try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/DocumentationView.fxml"));
                Parent root = loader.load();
                //DocumentationController controller = loader.getController();
                //controller.userController(persistenceModel, observablesModel, functionsModel);
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setTitle("Documentation Manager");
                stage.setScene(scene);
                stage.show();

        }catch (IOException e){
            String str = "There has been an error loading DocumentationView.fxml. Please contact system Admin.";
            customerError(str);
        }

    }

    @FXML private void editCustomer(ActionEvent actionEvent){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/NECustomer.fxml"));
            Parent root = loader.load();
            NECustomerController controller = loader.getController();
            controller.setNEController(true, persistenceModel, functionsModel);
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setTitle("Edit Customer");
            stage.setScene(scene);
            stage.show();
        }catch (IOException e){
            String str = "There has been an error loading NECustomer.fxml. Please contact system Admin.";
            customerError(str);
        }
    }

    @FXML private void createCustomer(ActionEvent actionEvent){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/NECustomer.fxml"));
            Parent root = loader.load();
            NECustomerController controller = loader.getController();
            controller.setNEController(false,persistenceModel,functionsModel);
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setTitle("New customer");
            stage.setScene(scene);
            stage.show();
        }catch (IOException e){
            String str = "There has been an error loading NECustomer.fxml. Please contact system Admin.";
            customerError(str);
        }
    }

    /**
     * This will log the user out and change the view to the login.
     * We catch the IOException and show the user a crafted alert.
     * @param actionEvent triggered by the logout button.
     */
    @FXML private void logOut(ActionEvent actionEvent){
        try {
            persistenceModel.setLoggedInUser(null);
            Parent root = FXMLLoader.load(getClass().getResource("/gui/view/Login.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setTitle("WUAV");
            stage.setScene(scene);
            stage.show();
        }catch (IOException e){
            String str = "There has been an error loading Login.fxml. Please contact system Admin.";
            customerError(str);
        }
    }

    /**
     * We use this to display an error to the user if there is a problem.
     * @param str This is the source of the problem so that the user is informed.
     */
    private void customerError(String str) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Error");
        alert.setHeaderText(str);

        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            alert.close();
        } else {
            alert.close();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setCustomerTableView();

        searchCustomer.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                observablesModel.search(newValue);
            }
        });
    }
}
