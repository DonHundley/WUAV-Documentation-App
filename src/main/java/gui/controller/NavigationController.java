package gui.controller;

import be.*;
import gui.model.*;
import io.github.palexdev.materialfx.controls.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.stage.*;

import java.io.*;
import java.util.*;

public class NavigationController {
    @FXML private AnchorPane navAnchor;
    @FXML private AnchorPane viewAnchor;
    @FXML private ImageView imageWUAV;
    @FXML private AnchorPane baseAnchor;
    @FXML private MFXButton manageProjectsButton;
    @FXML private MFXButton manageCustomerButton;
    @FXML private MFXButton manageUserButton;


    private Persistent persistentModel = Persistent.getInstance();
    private Observables observablesModel = Observables.getInstance();
    private Functions functionsModel = new Functions();

    private User loggedInUser = persistentModel.getLoggedInUser();
    private String home;

    public void setNavigationController() throws IOException {
        persistentModel.setViewAnchor(viewAnchor);

        String access = loggedInUser.getAccess().toUpperCase();
        switch (access){
            case "ADMIN":
                Node a = FXMLLoader.load(getClass().getResource("/gui/view/ManageTaskView.fxml"));
                viewAnchor.getChildren().setAll(a);
                home = "/gui/view/ManageTaskView.fxml";
                break;
            case "MANAGER":
                Node m = FXMLLoader.load(getClass().getResource("/gui/view/ManageTaskView.fxml"));
                viewAnchor.getChildren().setAll(m);
                home = "/gui/view/ManageTaskView.fxml";
                break;
            case "TECHNICIAN":
                System.out.println("Loading list");
                observablesModel.loadProjectsByUser(loggedInUser);
                System.out.println("done loading list");
                Node t = FXMLLoader.load(getClass().getResource("/gui/view/ManageTaskView.fxml"));
                viewAnchor.getChildren().setAll(t);
                home = "/gui/view/ManageTaskView.fxml";
                manageCustomerButton.setVisible(false);
                manageProjectsButton.setVisible(false);
                manageUserButton.setVisible(false);
                break;
            case "SALES":
                Node s = FXMLLoader.load(getClass().getResource("/gui/view/ManageCustomersView.fxml"));
                viewAnchor.getChildren().setAll(s);
                home = "/gui/view/ManageCustomersView.fxml";
                manageCustomerButton.setVisible(false);
                manageProjectsButton.setVisible(false);
                manageUserButton.setVisible(false);
                break;
            default:
                String str = "User account either has no role or the role is invalid. Please contact an Administrator.";
                navigationError(str);

        }
    }

    public void home(ActionEvent actionEvent) throws IOException {
        Node n = FXMLLoader.load(getClass().getResource(home));
        viewAnchor.getChildren().setAll(n);
    }

    public void manageProjects(ActionEvent actionEvent) throws IOException {
        Node n = FXMLLoader.load(getClass().getResource("/gui/view/ManageProjectView.fxml"));
        viewAnchor.getChildren().setAll(n);
    }

    public void manageCustomers(ActionEvent actionEvent) throws IOException {
        Node n = FXMLLoader.load(getClass().getResource("/gui/view/ManageCustomersView.fxml"));
        viewAnchor.getChildren().setAll(n);
    }

    public void manageUsers(ActionEvent actionEvent) throws IOException {
        Node n = FXMLLoader.load(getClass().getResource("/gui/view/ManageUsersView.fxml"));
        viewAnchor.getChildren().setAll(n);
    }

    /**
     * We use this to display an error to the user if there is a problem.
     *
     * @param str This is the source of the problem so that the user is informed.
     */
    private void navigationError(String str) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Error");
        alert.setHeaderText(str);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            alert.close();
        } else {
            alert.close();
        }
    }

}
