

import bll.LogicManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;

public class Main extends Application{



    @Override
    public void start(Stage stage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(""));
            Scene scene = new Scene(root);
            stage.setTitle("");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e){

        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
