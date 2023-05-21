package gui.controller.newAndUpdateControllers;

import be.Project;
import be.Task;
import gui.model.*;
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
import org.apache.logging.log4j.*;

import javax.imageio.*;
import java.io.*;
import java.util.*;



/**
 * The drawing application used in this controller at its current state takes heavy inspiration from https://math.hws.edu/javanotes/
 * which is Introduction to Programming Using Java, a free, on-line textbook.
 */
public class EditLayoutController {

    private static final Logger logger = LogManager.getLogger("debugLogger");
    @FXML
    private  StackPane diagramStack;
    @FXML
    private BorderPane bPane;
    @FXML
    private Button editLayoutButton, cancelButton;
    @FXML
    private   Button freeDrawButton;

    // Model
    private ProjectModel projectModel = ProjectModel.getInstance();
    private Task selectedTask = projectModel.getSelectedTask();

    // Shape variables
    private Shape[] shapes = new Shape[100];  // We have a limit of 100 shapes.
    private int shapeCount = 0; // Count of shapes.

    // Color variables.colors and colorNames must be in the correct order to assure the name is the selected color.
    private Color currentColor = Color.BLACK; // Default color selection.
    private Color[] colors = { Color.RED, Color.GREEN, Color.BLUE, Color.CYAN, // Colors to choose from
            Color.MAGENTA, Color.YELLOW, Color.BLACK, Color.WHITE, Color.ORANGE, Color.VIOLET};

    private String[] colorNames = { "Red", "Green", "Blue", "Cyan", // The names of the colors to be selected.
            "Magenta", "Yellow", "Black", "White", "Orange", "Violet" };

    // Mouse variables
    private int prevX, prevY;   // Last location of the mouse.

    private boolean dragging;   // True while drawing

    private Shape shapeBeingDragged = null;  // This is null unless a shape is being dragged.

    // The canvases
    private int canvasHeight = 400; // The set height of our canvases
    private int canvasWidth = 600; // The set width of our canvases
    private Canvas canvas1;  // The canvas on which everything is drawn. Think of this as layer 1.
    private Canvas canvas2; // The canvas on which shapes will be placed and moved. This of this as layer 2.
    private GraphicsContext g1;  // For drawing on the canvas.
    private GraphicsContext g2; // For placing shapes on the canvas

    /**
     * This method is called to create our canvases to be drawn on.
     * Canvas 1 is for freehand drawing, canvas 2 is for shapes.
     */
    public void setUpCanvas(){
        logger.trace("setUpCanvas() called in EditLayoutController");
        canvas1 = new Canvas(canvasWidth, canvasHeight);
        canvas2 = new Canvas(canvasWidth, canvasHeight);

        //Both of the canvases have different methods to be called when drawn on.
        canvas1.setOnMousePressed(this::mousePressed);
        canvas1.setOnMouseDragged(this::mouseDragged);
        canvas1.setOnMouseReleased(this::mouseReleased);

        canvas2.setOnMousePressed(this::mousePressedShape);
        canvas2.setOnMouseDragged(this::mouseDraggedShape);
        canvas2.setOnMouseReleased(this::mouseReleasedShape);

        // We are adding our canvases to our stack pane to be cycled between.
        diagramStack.getChildren().add(canvas1);
        diagramStack.getChildren().add(canvas2);

        // This is to paint our canvases after creation.
        g1 = canvas1.getGraphicsContext2D();
        g2 = canvas2.getGraphicsContext2D();

        paintCanvas(g1);
        paintCanvas(g2);

        // We are now creating the tool box for the user.
        bPane.setBottom(createToolBox());

        // Finally we set the default to free hand drawing.
        freeDraw();

        logger.trace("setUpCanvas() complete.");
    }

    /**
     * This is to create the tool pane for the user.
     * @return We are returning the tool box to be added to our border pane.
     */
    private HBox createToolBox() {
        logger.trace("createToolBox() called in EditLayoutController.");

        freeDrawButton = new Button("Free draw"); // Button to activate free drawing.
        freeDrawButton.setOnAction( (e) -> freeDraw());
        

        Button ovalButton = new Button("Add an Oval"); // Button to create an oval shape.
        ovalButton.setOnAction( (e) -> addShape( new OvalShape() ) );
        Button rectButton = new Button("Add a Rectangle"); // Button to create a rectangle
        rectButton.setOnAction( (e) -> addShape( new RectShape() ) );
        Button roundRectButton = new Button("Add a RoundRect"); // Button to create a rounded rectangle.
        roundRectButton.setOnAction( (e) -> addShape( new RoundRectShape() ) );

        ComboBox<String> combobox = new ComboBox<>(); // This is our combobox of colors.
        combobox.setEditable(false);
        combobox.getItems().addAll(colorNames); // Adding the names of our colors.
        combobox.setValue("Black"); // Setting the value of the comboBox to meet our default color selection.
        combobox.setOnAction(
                e -> currentColor = colors[combobox.getSelectionModel().getSelectedIndex()] );


        HBox tools = new HBox(10);
        tools.getChildren().add(freeDrawButton); // Adding the buttons we created to the HBox
        tools.getChildren().add(ovalButton);
        tools.getChildren().add(rectButton);
        tools.getChildren().add(roundRectButton);
        tools.getChildren().add(combobox);
        tools.setStyle("-fx-border-width: 3px; -fx-border-color: transparent; -fx-background-color: silver");

        logger.trace("createToolBox Complete.");
        return tools;
    }

    /**
     * Called when the free draw is selected in our tool box.
     */
    private void freeDraw() {
        logger.trace("freeDraw() called.");

        freeDrawButton.setDisable(true); // We disable the button so the user has a visual queue of what is selected.
        canvas1.toFront(); // This is brought to the front because it is our free draw canvas.
    }

    /**
     * This is called whenever we want to add a shape to our canvas.
     * @param shape This is in reference to one of our shape classes that extend Shape
     */
    private void addShape(Shape shape) {
        logger.trace("addShape() called in EditLayoutController.");

        canvas2.toFront(); // We bring the canvas to the front because this is our shape canvas.
        freeDrawButton.setDisable(false); // Enable the button again to show that we are no longer drawing.
        shape.setColor(currentColor); // The color of the shape is the current color selected.
        shape.reshape(10,10,150,100); // This is the predetermined size of the shape.
        shapes[shapeCount] = shape;  // We are using the shapeCount to refer to the stored shape.
        shapeCount++;
        paintCanvas(g2); // We recreate the canvas with our old shapes + our new one.

        logger.trace("addShape() complete.");
    }

    /**
     * This is what we are using to reset our canvas when we move shapes.
     * @param gc This is the graphics context of the given canvas.
     */
    private void paintCanvas(GraphicsContext gc) {
        logger.trace("paintCanvas() called in EditLayoutController.");
        int width = (int)gc.getCanvas().getWidth();    // Width of the canvas.
        int height = (int)gc.getCanvas().getHeight();  // Height of the canvas.

        gc.clearRect(0,0,width, height); // We clear our canvas

        if (gc.equals(g2)) { // If the given graphics context is the gc for canvas2, we add the shapes again.
            for (int i = 0; i < shapeCount; i++) { // Iterate over our shape list.
                Shape s = shapes[i];
                s.draw(gc); // Draw the shape on the canvas again.
            }
        }
        logger.trace("paintCanvas() complete.");
    }

    /**
     * This is called when the user presses the mouse anywhere in canvas1.
     */
    public void mousePressed(MouseEvent mouseEvent) {
        int x = (int) mouseEvent.getX();   // x-coordinate where the user clicked.
        int y = (int) mouseEvent.getY();   // y-coordinate where the user clicked.

        if (dragging){return;}  // We use a boolean so that we are ignoring mouse clicks while drawing.

        prevX = x; // Setting our previous location X
        prevY = y; // Setting our previous location Y

        dragging = true;

        g1.setLineWidth(2);  // Use a 2-pixel-wide line for drawing.
        g1.setStroke(currentColor); // Line color is our current color.
    }


    /**
     * Called whenever the user releases the mouse button.
     */
    public void mouseReleased (MouseEvent mouseEvent){
        dragging = false;
    }


    /**
     * Called whenever the user moves the mouse while a mouse button is held down.
     * If the user is drawing, draw a line segment from the previous mouse location
     * to the current mouse location, and set up prevX and prevY for the next call.
     * The user is locked to drawing within the canvas.
     */
    public void mouseDragged (MouseEvent mouseEvent){
        if (!dragging)
            return;  // Nothing to do because the user isn't drawing.

        int x = (int)mouseEvent.getX();   // x-coordinate of mouse.
        int y = (int)mouseEvent.getY();   // y-coordinate of mouse.

        if (x < 3)                        // Assure x is in the drawing area
            x = 3;
        if (x > canvasWidth)
            x = canvasWidth;

        if (y < 3)                        // Assure y is in the drawing area.
            y = 3;
        if (y > canvasHeight)
            y = canvasHeight;

        g1.strokeLine(prevX, prevY, x, y);  // Draw the line.

        prevX = x;  // Setting our previous mouse location
        prevY = y;

    }

    /**
     * If there is a shape being dragged, we move it to the coordinates of the mouse.
     * @param mouseEvent fetches our mouse coordinates.
     */
    private void mouseDraggedShape(MouseEvent mouseEvent){
        int x = (int)mouseEvent.getX();
        int y = (int)mouseEvent.getY();
        if (shapeBeingDragged != null) {
            shapeBeingDragged.moveBy(x - prevX, y - prevY);
            prevX = x;
            prevY = y;
            paintCanvas(g2);      // redraw canvas to show shape in new position
        }
    }

    private void mouseReleasedShape(MouseEvent mouseEvent){
        shapeBeingDragged = null;
    }

    /**
     * Checks to see if there is a shape where the user clicked.
     * @param mouseEvent fetches our mouse coordinates to be checked against the coordinates of shapes.
     */
    private void mousePressedShape(MouseEvent mouseEvent){
        int x = (int)mouseEvent.getX(); // x-coord of where mouse was clicked
        int y = (int)mouseEvent.getY(); // y-coord of where mouse was clicked

        for (int i = shapeCount - 1; i >= 0; i--) {  // check shapes
            Shape s = shapes[i];
            if (s.containsPoint(x, y)) { // true if a shape is at the mouse location
                shapeBeingDragged = s; // we set our shape being dragged
                prevX = x;
                prevY = y;

                shapes[shapeCount - 1] = s;  // put s at the end of the list
                paintCanvas(g2);  // repaint canvas
            }
            return;
        }
    }


    /**
     * Method to update the layout for a task.
     */
    @FXML
    private void saveLayout (ActionEvent actionEvent){
        logger.info("Saving drawn layout.");

        logger.info("Iterating over shapes.");
        for (int i = 0; i < shapeCount; i++) {
            Shape s = shapes[i];
            s.draw(g1);
        }

        logger.info("Creating image from canvases.");
        try{
            SnapshotParameters parameters = new SnapshotParameters();
            WritableImage writableImage = new WritableImage(600,400);
            WritableImage snapshot = canvas1.snapshot(parameters,writableImage);

            File output = new File( System.getProperty("user.home") + "/Desktop/snapshot" + new Date().getTime() + ".png");
            ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", output);
            selectedTask.setTaskLayoutAbsolute(output.getAbsolutePath());
        }catch (IOException io){
            logger.error("There has been a problem saving the drawn layout.",io);
        }

        projectModel.updateLayout();
        projectModel.loadAllProjLists();

        logger.info("Layout creation process finished.");

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

}




