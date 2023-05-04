

import be.*;
import gui.controller.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import logic.businessLogic.*;

import java.io.*;
import java.util.*;

public class Main extends Application{

    private UserLogic userLogic = new UserLogic();


    @Override
    public void start(Stage stage) {
        List<User> users = userLogic.getUsers();
        HashMap<String, String> loginInfo = userLogic.loginInformation(users);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(getClass().getResource("Login.fxml"));
            Parent root = loader.load();
            LoginController controller = loader.getController();
            controller
            Scene scene = new Scene(root);
            stage.setTitle("Login");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e){

        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
