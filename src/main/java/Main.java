

import gui.controller.DocumentationController;
import gui.controller.ManageTaskController;
import gui.model.Functions;
import gui.model.Observables;
import gui.model.Persistent;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;

public class Main extends Application{



    @Override
    public void start(Stage stage) {
        Persistent persistent = new Persistent();
        Functions functions = new Functions();
        Observables observables = new Observables();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("main/java/gui/view/Login.fxml"));
            Parent root = loader.load();
            //DocumentationController controller = loader.getController();
           // controller.userController(persistent, observables, functions);
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
