package bll.eHandler;

import javafx.scene.control.*;

import java.util.*;

public class ErrorFacade {

    public void createIOexceptionAlert(String className, String errorType){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("An error has occurred!");
        alert.setHeaderText("An error has occurred, please contact system admin");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK){
            alert.close();
        }
    }
}
