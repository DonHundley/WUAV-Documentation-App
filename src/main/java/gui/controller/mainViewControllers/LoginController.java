package gui.controller.mainViewControllers;

import be.*;
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

    private AuthenticationModel authenticationModel = AuthenticationModel.getInstance();
    private UserModel userModel = UserModel.getInstance();
    private HashMap<Integer, Integer> userInfo;
    private List<User> users;
    private static final Logger logger = LogManager.getLogger("debugLogger");


    public void login(ActionEvent actionEvent) {
        logger.info("login called in " + this.getClass().getName());
        String loginError = "Username or password incorrect.";
        logger.trace("Getting text from username and password text fields.");
        String userID = userName.getText();
        String pass = passTF.getText();

        logger.trace("Checking user name.");
        if(userInfo.containsKey(userID.hashCode())){
            logger.trace("Checking password");
            if(userInfo.get(userID.hashCode()).equals(pass.hashCode())){
                logger.info("Correct user information input. Locating user profile.");
                for (User user:users
                     ) {
                    if(Objects.equals(userID, user.getUserName())){
                        logger.info("Setting logged in user.");
                        authenticationModel.setLoggedInUser(user);

                        try {
                            logger.info("Loading NavigationView.fxml");
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/mainViews/NavigationView.fxml"));
                            Parent root = loader.load();
                            NavigationController controller = loader.getController();
                            controller.setNavigationController();
                            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                            Scene scene = new Scene(root);
                            stage.setTitle("WUAV Documentation management system.");
                            stage.setScene(scene);
                            stage.show();
                            logger.info("User login completed.");
                            break;
                        } catch (IOException e){
                            logger.error("There has been a problem loading NavigationView.fxml. ",e );
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Error");
                            alert.setHeaderText("There has been a problem loading NavigationView, please contact System administrator.");

                            alert.show();
                        }
                    }
                }
            } else {
                messageLabel.setText(loginError);
                logger.trace("User input incorrect.");
            }
        } else {
            messageLabel.setText(loginError);
            logger.trace("User input incorrect.");
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        logger.info("Initializing LoginController.");
        users = userModel.users();
        userInfo = authenticationModel.userInfo();
    }

}
