package gui.controller.mainViewControllers;

import gui.model.*;
import io.github.palexdev.materialfx.controls.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.*;
import org.apache.logging.log4j.*;

import java.io.*;
import java.net.*;
import java.util.*;

public class LoginController extends BaseController implements Initializable{

    @FXML private MFXTextField userName;
    @FXML private MFXPasswordField passTF;
    @FXML private Label messageLabel;

    private AuthenticationModel authenticationModel;
    private static final Logger logger = LogManager.getLogger("debugLogger");


    public void login(ActionEvent actionEvent) {
        logger.info("login called in " + this.getClass().getName());
        String loginError = "Username or password incorrect.";
        logger.trace("Getting text from username and password text fields.");
        String userID = userName.getText();
        String pass = passTF.getText();

        try {
            authenticationModel.authenticateCredentials(userID, pass);
        }catch (NoSuchElementException e){
            messageLabel.setText(loginError);
            logger.warn("User did not input proper login information. Assure there are not warnings from UserLogic for lists.");
            return;
        }

        try {
            logger.info("Loading NavigationView.fxml");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/mainViews/NavigationView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setTitle("WUAV Documentation management system.");
            stage.setScene(scene);
            stage.show();
            logger.info("User login completed.");
        } catch (IOException e){
            logger.error("There has been a problem loading NavigationView.fxml. ",e );
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("There has been a problem loading NavigationView, please contact System administrator.");
            alert.show();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        logger.info("Initializing LoginController.");
        authenticationModel = AuthenticationModel.getInstance();
    }
}
