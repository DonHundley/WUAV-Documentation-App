
import bll.eHandler.*;
import bll.eHandler.errors.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;

public class Main extends Application{
    private ErrorFacade facade;

    @Override
    public void start(Stage stage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(""));
            Scene scene = new Scene(root);
            stage.setTitle("");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e){
            ErrorFacade facade = new ErrorFacade();

            AlertUser exception = new EIOexception();

            exception.showAlert(facade, "Main", "IOException");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
