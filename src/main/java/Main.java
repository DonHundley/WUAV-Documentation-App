


import gui.controller.ManageCustomerController;
import gui.controller.ManageProjectController;
import gui.model.Functions;
import gui.model.Observables;
import gui.model.Persistent;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.*;


public class Main extends Application {


    @Override
    public void start(Stage stage) throws FileNotFoundException {
        Persistent persistent = new Persistent();
        Functions functions = new Functions();
        Observables observables = new Observables();

          try {
          FXMLLoader loader = new FXMLLoader(getClass().getResource("gui/view/ManageCustomersView.fxml"));
            Parent root = loader.load();
            ManageCustomerController controller = loader.getController();
            controller.userController(persistent, observables, functions);
            Scene scene = new Scene(root);
            stage.setTitle("");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {

        }
        //Task t = new Task(1, "task name", new Image(("C:\\Users\\alice\\github\\WUAV-Documentation-App\\src\\main\\java\\images\\layout.png")), "description","Not Started");
        //functions.createTask(t);
        //System.out.println(t.getTaskLayout().getUrl());
    }

    public static void main(String[] args) {
        launch(args);


    }
}
