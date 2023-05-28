package gui;

import javafx.scene.layout.*;

public class ViewPane {
    // This class is used to represent the pane that can have the view changed by any given controller.

    private final AnchorPane viewPane;
    public ViewPane() {
        viewPane = new AnchorPane();
        viewPane.setPrefWidth(950);
        viewPane.setPrefHeight(650);
        viewPane.setLayoutX(300);
        viewPane.setLayoutY(0);
    }
    private static ViewPane instance;
    public static ViewPane getViewPaneInstance(){
        if (instance == null){
            instance = new ViewPane();

        }
        return instance;
    }

    public AnchorPane getViewPane() {
        return viewPane;
    }
}
