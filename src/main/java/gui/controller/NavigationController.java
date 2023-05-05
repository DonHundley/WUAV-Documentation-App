package gui.controller;

import gui.model.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.stage.*;

import java.io.*;

public class NavigationController {
    public AnchorPane navAnchor;
    public AnchorPane viewAnchor;
    public ImageView imageWUAV;
    public AnchorPane baseAnchor;
    public ScrollPane scrollPane;
    public Pane pane;

    private Persistent persistentModel;
    private Observables observablesModel;
    private Functions functionsModel;

    public void setNavigationController(Persistent persistentModel, Observables observablesModel, Functions functionsModel) throws IOException {
        this.persistentModel = persistentModel;
        this.observablesModel = observablesModel;
        this.functionsModel = functionsModel;


        Node n = FXMLLoader.load(getClass().getResource("/gui/view/ManageTaskView.fxml"));



        viewAnchor.getChildren().setAll(n);

        //scrollPane.setContent(n);

    }

    public void home(ActionEvent actionEvent) {
    }

    public void manageProjects(ActionEvent actionEvent) throws IOException {
        /**

        Node n = FXMLLoader.load(getClass().getResource("/gui/view/ManageProjectView.fxml"));

        viewAnchor.getChildren().setAll(n);
        viewAnchor.getChildren().
        */

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/gui/view/ManageProjectView.fxml"));
        try{
            Node n = loader.setLoadListener();
        }
    }

    public void manageCustomers(ActionEvent actionEvent) {
    }

    public void manageUsers(ActionEvent actionEvent) {
    }

}
