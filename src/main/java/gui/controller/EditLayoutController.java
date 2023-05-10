package gui.controller;

import be.Project;
import be.Task;
import gui.model.Functions;
import gui.model.Observables;
import gui.model.Persistent;
import gui.shapes.*;
import javafx.embed.swing.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;


import javafx.scene.control.Button;

import javax.imageio.*;
import java.io.*;
import java.util.*;
import java.util.logging.*;


/**
 * The drawing application used in this controller at its current state takes heavy inspiration from https://math.hws.edu/javanotes/
 * which is Introduction to Programming Using Java, a free, on-line textbook.
 */
public class EditLayoutController {


    public StackPane diagramStack;
    @FXML
    private BorderPane bPane;
    @FXML
    private Button editLayoutButton, cancelButton;
    private  Button freeDrawButton;

    private Task selectedTask;
    private Project selectedProject;

    // Model
    private Functions functionsModel = new Functions();

    private Persistent persistenceModel = Persistent.getInstance();

    private Observables observablesModel = Observables.getInstance();



    private Shape[] shapes = new Shape[500];  // Contains gui.shapes the user has drawn.
    private Color currentColor = Color.BLACK;
    private int shapeCount = 0; // Number of gui.shapes that the user has drawn.
    private double prevX, prevY;   // The previous location of the mouse, when
    // the user is drawing by dragging the mouse.
    private boolean dragging;   // This is set to true while the user is drawing.


    private Canvas canvas1;  // The canvas on which everything is drawn. Think of this as layer 1.
    private Canvas canvas2; // The canvas on which shapes will be placed and moved. This of this as layer 2.

    private GraphicsContext g1;  // For drawing on the canvas.
    private GraphicsContext g2; // For placing shapes on the canvas

    public void setUpCanvas(){


        canvas1 = new Canvas(600, 400);
        canvas2 = new Canvas(600, 400);

        canvas1.setOnMousePressed(this::mousePressed);
        canvas1.setOnMouseDragged(this::mouseDragged);
        canvas1.setOnMouseReleased(this::mouseReleased);

        canvas2.setOnMousePressed(this::mousePressedShape);
        canvas2.setOnMouseDragged(this::mouseDraggedShape);
        canvas2.setOnMouseReleased(this::mouseReleasedShape);

        diagramStack.getChildren().add(canvas1);
        diagramStack.getChildren().add(canvas2);

        /* Create the canvas and draw its content for the first time. */
        g1 = canvas1.getGraphicsContext2D();
        g2 = canvas2.getGraphicsContext2D();

        paintCanvas(g1);
        paintCanvas(g2);

        /* Respond to mouse events on the canvas, by calling methods in this class. */



        bPane.setBottom(makeToolPanel());
        freeDraw();
    }

    private HBox makeToolPanel() {
        // Make a pane containing the buttons that are used to add gui.shapes
        // and the pop-up menu for selecting the current color.
        freeDrawButton = new Button("Free draw");
        freeDrawButton.setOnAction( (e) -> freeDraw());
        Button ovalButton = new Button("Add an Oval");
        ovalButton.setOnAction( (e) -> addShape( new OvalShape() ) );
        Button rectButton = new Button("Add a Rect");
        rectButton.setOnAction( (e) -> addShape( new RectShape() ) );
        Button roundRectButton = new Button("Add a RoundRect");
        roundRectButton.setOnAction( (e) -> addShape( new RoundRectShape() ) );

        ComboBox<String> combobox = new ComboBox<>();
        combobox.setEditable(false);
        Color[] colors = { Color.RED, Color.GREEN, Color.BLUE, Color.CYAN,
                Color.MAGENTA, Color.YELLOW, Color.BLACK, Color.WHITE };
        String[] colorNames = { "Red", "Green", "Blue", "Cyan",
                "Magenta", "Yellow", "Black", "White" };
        combobox.getItems().addAll(colorNames);
        combobox.setValue("Black");
        combobox.setOnAction(
                e -> currentColor = colors[combobox.getSelectionModel().getSelectedIndex()] );


        HBox tools = new HBox(10);
        tools.getChildren().add(freeDrawButton);
        tools.getChildren().add(ovalButton);
        tools.getChildren().add(rectButton);
        tools.getChildren().add(roundRectButton);
        tools.getChildren().add(combobox);
        tools.setStyle("-fx-border-width: 5px; -fx-border-color: transparent; -fx-background-color: lightgray");
        return tools;
    }



    private void freeDraw() {
            freeDrawButton.setDisable(true);
            canvas1.toFront();
    }

    private void addShape(Shape shape) {
        // Add the shape to the canvas, and set its size/position and color.
        // The shape is added at the top-left corner, with size 80-by-50.
        // Then redraw the canvas to show the newly added shape.  This method
        // is used in the event listeners for the buttons in makeToolsPanel().
        canvas2.toFront();
        shape.setColor(currentColor);
        shape.reshape(10,10,150,100);
        shapes[shapeCount] = shape;
        shapeCount++;
        paintCanvas(g2);
        freeDrawButton.setDisable(false);

    }
    private void paintCanvas(GraphicsContext gc) {
        int width = (int)gc.getCanvas().getWidth();    // Width of the canvas.
        int height = (int)gc.getCanvas().getHeight();  // Height of the canvas.

        // Redraw the gui.shapes.  The entire list of gui.shapes
        // is redrawn whenever the user adds a new shape
        // or moves an existing shape.

        g2.clearRect(0,0,width, height);

        for (int i = 0; i < shapeCount; i++) {
            Shape s = shapes[i];
            s.draw(g2);
        }
    }


    // ------------ This part of the class defines methods to implement dragging -----------
    // -------------- These methods are added to the canvas as event listeners -------------

    private Shape shapeBeingDragged = null;  // This is null unless a shape is being dragged.
    // A non-null value is used as a signal that dragging
    // is in progress, as well as indicating which shape
    // is being dragged.

    private int prevDragX;  // During dragging, these record the x and y coordinates of the
    private int prevDragY;  //    previous position of the mouse.

    /**
     * This is called when the user presses the mouse anywhere in the canvas.
     * There are three possible responses, depending on where the user clicked:
     * Change the current color, clear the drawing, or start drawing a curve.
     * (Or do nothing if user clicks on the border.)
     */
    public void mousePressed(MouseEvent evt) {
        int x = (int) evt.getX();   // x-coordinate where the user clicked.
        int y = (int) evt.getY();   // y-coordinate where the user clicked.

        int width = (int) canvas1.getWidth();    // Width of the canvas.
        int height = (int) canvas1.getHeight();  // Height of the canvas.


            if (dragging == true)  // Ignore mouse presses that occur
                return;            //    when user is already drawing a curve.
            //    (This can happen if the user presses
            //    two mouse buttons at the same time.)



            if (x > 3 && x < width - 56 && y > 3 && y < height - 3) {
                // The user has clicked on the white drawing area.
                // Start drawing a curve from the point (x,y).
                prevX = x;
                prevY = y;
                dragging = true;
                g1.setLineWidth(2);  // Use a 2-pixel-wide line for drawing.
                g1.setStroke(currentColor);

            }
        } // end mousePressed()


        /**
         * Called whenever the user releases the mouse button. Just sets
         * dragging to false.
         */
        public void mouseReleased (MouseEvent evt){
            dragging = false;
            System.out.println(canvas1.getWidth());
            System.out.println(prevX);
            System.out.println(prevY);
            System.out.println(canvas1.getLayoutBounds());
        }


        /**
         * Called whenever the user moves the mouse while a mouse button is held down.
         * If the user is drawing, draw a line segment from the previous mouse location
         * to the current mouse location, and set up prevX and prevY for the next call.
         * Note that in case the user drags outside of the drawing area, the values of
         * x and y are "clamped" to lie within this area.  This avoids drawing on the color
         * palette or clear button.
         */
        public void mouseDragged (MouseEvent evt){
            if (dragging == false)
                return;  // Nothing to do because the user isn't drawing.

            double x = evt.getX();   // x-coordinate of mouse.
            double y = evt.getY();   // y-coordinate of mouse.

            if (x < 3)                          // Adjust the value of x,
                x = 3;                           //   to make sure it's in
            if (x > canvas1.getWidth())       //   the drawing area.
                x = (int) canvas1.getWidth();

            if (y < 3)                          // Adjust the value of y,
                y = 3;                           //   to make sure it's in
            if (y > canvas1.getHeight())       //   the drawing area.
                y = canvas1.getHeight();

            g1.strokeLine(prevX, prevY, x, y);  // Draw the line.

            prevX = x;  // Get ready for the next line segment in the curve.
            prevY = y;

        } // end mouseDragged()

        private void mouseDraggedShape(MouseEvent me){
            // User has moved the mouse.  Move the dragged shape by the same amount.
            int x = (int)me.getX();
            int y = (int)me.getY();
            if (shapeBeingDragged != null) {
                shapeBeingDragged.moveBy(x - prevDragX, y - prevDragY);
                prevDragX = x;
                prevDragY = y;
                paintCanvas(g2);      // redraw canvas to show shape in new position
            }
        }

        private void mouseReleasedShape(MouseEvent me){
            shapeBeingDragged = null;
        }

        private void mousePressedShape(MouseEvent me){
            int x = (int)me.getX(); // x-coord of where mouse was clicked
            int y = (int)me.getY(); // y-coord of where mouse was clicked

            for (int i = shapeCount - 1; i >= 0; i--) {  // check gui.shapes from front to back
                Shape s = shapes[i];
                if (s.containsPoint(x, y)) {
                    shapeBeingDragged = s;
                    prevDragX = x;
                    prevDragY = y;
                    if (me.isShiftDown()) { // s should be moved on top of all the other gui.shapes
                        for (int j = i; j < shapeCount - 1; j++) {
                            // move the gui.shapes following s down in the list
                            shapes[j] = shapes[j + 1];
                        }
                        shapes[shapeCount - 1] = s;  // put s at the end of the list
                        paintCanvas(g2);  // repaint canvas to show s in front of other gui.shapes
                    }
                    return;
                }
            }
        }

        /**
         * Method to update the layout for a task.
         */
        @FXML
        private void saveLayout (ActionEvent actionEvent){

            for (int i = 0; i < shapeCount; i++) {
                Shape s = shapes[i];
                s.draw(g1);
            }

            try{
                SnapshotParameters parameters = new SnapshotParameters();
                WritableImage writableImage = new WritableImage(600,400);
                WritableImage snapshot = canvas1.snapshot(parameters,writableImage);

                File output = new File( System.getProperty("user.home") + "/Desktop/snapshot" + new Date().getTime() + ".png");
                ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", output);
                selectedTask.setTaskLayoutAbsolute(output.getAbsolutePath());
            }catch (IOException io){
                Logger.getLogger(EditLayoutController.class.getName()).log(Level.SEVERE, null, io);
            }

            functionsModel.updateLayout(selectedTask);
            observablesModel.loadTasksByProject(selectedProject);

            Stage stage = (Stage) editLayoutButton.getScene().getWindow();
            stage.close();

        }


        /**
         * Closes the window with an action event.
         * @param actionEvent triggers when the user activates the cancel button.
         */
        @FXML
        private void cancel (ActionEvent actionEvent){
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
        }

        /**
         * We use this to display an error to the user if there is a problem.
         * @param str This is the source of the problem so that the user is informed.
         */
        private void taskError (String str){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText(str);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                alert.close();
            } else {
                alert.close();
            }
        }


        /**
         * Method to set selected task and setting the layout preview if available*/
        public void setLayoutOnEdit () {
            selectedTask = persistenceModel.getSelectedTask();
        }

        /**
         * This controller needs the selected project in order to get a list of tasks for it.
         * We fetch the selected project from persistent in order to do this, if there is a problem with this, we show an alert.
         */
        public void setSelectedProjectForLayout () {
            if (persistenceModel.getSelectedProject() != null) {
                selectedProject = persistenceModel.getSelectedProject();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Project does not exist.");
                alert.setHeaderText("An error has occurred, please contact system admin. Project was not selected or does not exist.");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    alert.close();
                }
            }
        }

    }




