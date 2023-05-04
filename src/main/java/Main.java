


import javafx.application.Application;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.stage.Stage;


import java.io.*;


public class Main extends Application{

    @Override
    public void start(Stage stage) throws IOException {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("main/java/gui/view/Login.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setTitle("");
            stage.setScene(scene);
            stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
