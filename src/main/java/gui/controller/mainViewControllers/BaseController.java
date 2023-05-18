package gui.controller.mainViewControllers;

import gui.model.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.*;


import java.io.*;
import java.util.*;

public abstract class BaseController {

    private AuthenticationModel authenticationModel = AuthenticationModel.getInstance();
    NavigationController nav;
    public void setNavController(NavigationController nav){
        this.nav = nav;
    }

    public void logout(ActionEvent actionEvent){
        try {
            authenticationModel.setLoggedInUser(null);
            Parent root = FXMLLoader.load(getClass().getResource("/gui/view/mainViews/Login.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setTitle("WUAV");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
           String str = "There has been a problem loading Login.fxml, please contact system Admin.";
           createWarning(str);
        }
    }

    /**
     * We use this to display an error to the user if there is a problem.
     *
     * @param str This is the source of the problem so that the user is informed.
     */
    public void createWarning(String str) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Error");
        alert.setHeaderText(str);

        alert.show();
    }

}
