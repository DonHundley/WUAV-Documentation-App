package gui.controller.mainViewControllers;

import gui.*;
import gui.model.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import org.apache.logging.log4j.*;


import java.io.*;

public abstract class BaseController {

    private AuthenticationModel authenticationModel = AuthenticationModel.getInstance();
    private ViewPane viewPaneClass = ViewPane.getViewPaneInstance();;
    private static final Logger logger = LogManager.getLogger("debugLogger");

    /**
     * This is called in order to log the user out of the application
     * @param actionEvent source is the logout button.
     */
    public void logout(ActionEvent actionEvent){
        logger.info("logout() called in BaseController");
        try {
            logger.trace("setting logged in user to null");
            authenticationModel.setLoggedInUser(null);
            logger.info("Loading Login.fxml");
            Parent root = FXMLLoader.load(getClass().getResource("/gui/view/mainViews/Login.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setTitle("WUAV");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            logger.error("There was an issue loading Login.fxml. CLASS: BaseController CAUSE: ",e);
           String str = "There has been a problem loading Login.fxml, please contact system Admin.";
           createWarning(str);
        }
        logger.info("logout() completed.");
    }

    /**
     * We use this to display an error to the user if there is a problem.
     *
     * @param str This is the source of the problem so that the user is informed.
     */
    public void createWarning(String str) {
        logger.warn("A warning was shown to the user.");
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Error");
        alert.setHeaderText(str);
        alert.show();
    }


    public AnchorPane getViewPane() {
        return viewPaneClass.getViewPane();
    }

    public void newTask() {
        logger.info("newTask() called in " + this.getClass().getName());
        try {
            logger.info("Loading NewTask.fxml");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/newAndUpdateViews/NewTask.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setTitle("Create Task");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            logger.error("There has been a problem loading NewTask.fxml.", e);
            String str = "There has been an issue loading NewTask.fxml, please contact system Admin.";
            createWarning(str);
        }
        logger.info("newTask() complete.");
    }
}
