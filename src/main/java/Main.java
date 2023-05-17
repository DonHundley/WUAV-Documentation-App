

import javafx.application.Application;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.stage.Stage;
import org.apache.logging.log4j.*;


import java.io.*;


public class Main extends Application{

    private static final Logger logger = LogManager.getLogger("debugLogger");

    @Override
    public void start(Stage stage)  {
        logger.info("Starting program with Login.fxml");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/mainViews/Login.fxml"));

            logger.info("loading root in Main");

            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setTitle("");
            stage.setScene(scene);
            stage.show();
        }catch (IOException e){
            logger.error("There was a problem with loading the fxml file. Class: Main CAUSE: " + e);
        }
        logger.info("finished program start.");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
