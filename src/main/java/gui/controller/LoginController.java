package gui.controller;

import be.*;
import gui.model.*;
import io.github.palexdev.materialfx.controls.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.control.*;

import java.net.*;
import java.util.*;

public class LoginController implements Initializable{


    private HashMap<String, String> userInfo;
    private List<User> users;
    public MFXTextField userName;
    public MFXPasswordField passTF;
    @FXML private Label messageLabel;

    private Persistent persistentModel = new Persistent();
    private Observables observablesModel = new Observables();
    private Functions functionsModel = new Functions();


    public void login(ActionEvent actionEvent) {
        String loginError = "Username or password incorrect.";
        String userID = userName.getText();
        String pass = passTF.getText();

        if(userInfo.containsKey(userID)){
            if(userInfo.get(userID).equals(pass)){




            } else messageLabel.setText(loginError);
        } else messageLabel.setText(loginError);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        users = observablesModel.getUsers();
        userInfo = functionsModel.userInfo(users);
    }
}
