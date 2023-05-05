


import be.*;
import gui.controller.*;
import gui.model.*;
import javafx.application.Application;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.stage.Stage;


import java.io.*;


public class Main extends Application{

    @Override
    public void start(Stage stage) throws IOException {
        Persistent persistent = Persistent.getInstance();
        Functions functions = new Functions();
        Observables observables = Observables.getInstance();


        //User user = new User(10, "ManagerAcc", "password", "Manager", "Man", "Ager");
        //persistent.setLoggedInUser(user);


            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/Login.fxml"));
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
