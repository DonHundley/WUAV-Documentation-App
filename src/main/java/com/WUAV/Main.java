package com.WUAV;

import com.WUAV.gui.model.*;
import javafx.application.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.stage.*;

import java.io.*;


public class Main extends Application{

    @Override
    public void start(Stage stage) throws IOException {
        Persistent persistent = Persistent.getInstance();
        Functions functions = new Functions();
        Observables observables = Observables.getInstance();


        //User user = new User(10, "ManagerAcc", "password", "Manager", "Man", "Ager");
        //persistent.setLoggedInUser(user);


            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/WUAV/gui/view/Login.fxml"));
            Parent root = loader.load();
            //NavigationController controller = loader.getController();
            //controller.setNavigationController(persistent, observables, functions);
            Scene scene = new Scene(root);
            stage.setTitle("");
            stage.setScene(scene);
            stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
