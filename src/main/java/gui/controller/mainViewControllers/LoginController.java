package gui.controller.mainViewControllers;

import be.*;
import gui.model.*;
import io.github.palexdev.materialfx.controls.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.*;

import java.io.*;
import java.net.*;
import java.util.*;

public class LoginController extends BaseController implements Initializable{


    private HashMap<Integer, Integer> userInfo;
    private List<User> users;
    public MFXTextField userName;
    public MFXPasswordField passTF;
    @FXML private Label messageLabel;

    private AuthenticationModel authenticationModel = AuthenticationModel.getInstance();
    private UserModel userModel = UserModel.getInstance();



    public void login(ActionEvent actionEvent) {
        String loginError = "Username or password incorrect.";
        String userID = userName.getText();
        String pass = passTF.getText();

        if(userInfo.containsKey(userID.hashCode())){
            if(userInfo.get(userID.hashCode()).equals(pass.hashCode())){
                for (User user:users
                     ) {
                    if(Objects.equals(userID, user.getUserName())){
                        System.out.println("Logged in");
                        authenticationModel.setLoggedInUser(user);

                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/mainViews/NavigationView.fxml"));
                            Parent root = loader.load();
                            NavigationController controller = loader.getController();
                            controller.setNavigationController();
                            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                            Scene scene = new Scene(root);
                            stage.setTitle("WUAV Documentation management system.");
                            stage.setScene(scene);
                            stage.show();
                            break;
                        } catch (IOException e){
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Error");
                            alert.setHeaderText("There has been a problem loading NavigationView, please contact System administrator.");

                            alert.show();
                        }
                    }
                }
            } else messageLabel.setText(loginError);
        } else messageLabel.setText(loginError);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        users = userModel.users();
        userInfo = authenticationModel.userInfo();
    }

}
