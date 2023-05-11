package com.WUAV.gui.controller;

import com.WUAV.be.*;
import com.WUAV.gui.model.*;
import io.github.palexdev.materialfx.controls.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.*;

import java.io.*;
import java.net.*;
import java.util.*;

public class LoginController implements Initializable{


    private HashMap<Integer, Integer> userInfo;
    private List<User> users;
    public MFXTextField userName;
    public MFXPasswordField passTF;
    @FXML private Label messageLabel;

    private Persistent persistentModel = Persistent.getInstance();
    private Observables observablesModel = Observables.getInstance();
    private Functions functionsModel = new Functions();




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
                        persistentModel.setLoggedInUser(user);

                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/WUAV/gui/view/NavigationView.fxml"));
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

                        }
                        System.out.println(persistentModel.getLoggedInUser().toString());

                    }
                }
            } else messageLabel.setText(loginError);
        } else messageLabel.setText(loginError);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        users = functionsModel.users();
        userInfo = functionsModel.userInfo();
    }

}
