package gui.controller.newAndUpdateControllers;


import be.Task;
import gui.model.*;
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
import shapeFactory.*;

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
    private  Button freeDrawButton;
    private Button fillButton;

    //------------------------- Model ----------------------------------------------------------------------------------
    private ProjectModel projectModel = ProjectModel.getInstance();
    private Task selectedTask = projectModel.getSelectedTask();

    //--------------------- Shape variables-----------------------------------------------------------------------------
    private int shapeCount = 0; // Count of shapes.
    private String[] sizes = {"small", "medium", "large"}; // sizes to choose from
    private String selectedSize = "medium"; // default size
    private ShapeArtist artist; // Our "Artist" is the shape factory.
    private Shapes[] shapesList;  // Our list of shapes
    private boolean fillShape; // If true the shapes will be filled.
    private Shapes shapeBeingDragged = null;  // This is null unless a shape is being dragged.
    private boolean drawing; // true if freehand drawing.
    private String[] strokeChoices = {"fine", "normal", "large", "very large"};
    private String currentStroke = "normal";

    // ---------------------Color variables. --------------------------------------------------------------------------
    // colors and colorNames must be in the correct order to assure the name is the selected color.
    private Color currentColor = Color.BLACK; // Default color selection.
    private Color[] colors = { Color.RED, Color.GREEN, Color.BLUE, Color.CYAN, // Colors to choose from
            Color.MAGENTA, Color.YELLOW, Color.BLACK, Color.WHITE, Color.ORANGE, Color.VIOLET};
    private String[] colorNames = { "Red", "Green", "Blue", "Cyan", // The names of the colors to be selected.
            "Magenta", "Yellow", "Black", "White", "Orange", "Violet" };

    // -------------------- Mouse variables ---------------------------------------------------------------------------
    private int prevX, prevY;   // Last location of the mouse.
    private boolean dragging;   // True while drawing

    // ------------------- The canvases --------------------------------------------------------------------------------
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
        bPane.setBottom(createBottomToolBox());
        bPane.setRight(createRightToolBox());

        // Instantiate our artist.
        artist = new ShapeArtist();

        // Create our shape list and set the limit of shapes.
        shapesList = new Shapes[500];

        // Finally we set the default to free hand drawing.
        freeDraw();



        logger.trace("setUpCanvas() complete.");
    }

    /**
     * This is to create the tool pane for the user.
     * @return We are returning the tool box to be added to our border pane.
     */
    private HBox createBottomToolBox() {
        logger.trace("createToolBox() called in EditLayoutController.");

        Button circleButton = new Button("Add a Circle"); // Button to create a circle.
        circleButton.setOnAction( (e) -> drawShape("circle", selectedSize, fillShape));
        Button rectButton = new Button("Add a Rectangle"); // Button to create a rectangle
        rectButton.setOnAction( (e) -> drawShape("rectangle",selectedSize, fillShape));
        Button ovalButton = new Button("Add a Oval"); // Button to create an oval.
        ovalButton.setOnAction( (e) -> drawShape("oval", selectedSize, fillShape));
        Button squareButton = new Button("Add a Square"); // Button to create a square.
        squareButton.setOnAction( (e) -> drawShape("square", selectedSize, fillShape));
        Button triangleButton = new Button("Add a Triangle"); // Button to create a triangle.
        triangleButton.setOnAction( (e) -> drawShape("triangle", selectedSize, fillShape));

        HBox tools = new HBox(10);

        tools.getChildren().add(circleButton);
        tools.getChildren().add(rectButton);
        tools.getChildren().add(ovalButton);
        tools.getChildren().add(squareButton);
        tools.getChildren().add(triangleButton);

        tools.setStyle("-fx-border-width: 3px; -fx-border-color: transparent; -fx-background-color: silver");

        logger.trace("createToolBox Complete.");
        return tools;
    }


    private VBox createRightToolBox(){
        ComboBox<String> combobox = new ComboBox<>(); // This is our combobox of colors.
        combobox.setEditable(false);
        combobox.getItems().addAll(colorNames); // Adding the names of our colors.
        combobox.setValue("Black"); // Setting the value of the comboBox to meet our default color selection.
        combobox.setOnAction(
                e -> currentColor = colors[combobox.getSelectionModel().getSelectedIndex()] );

        freeDrawButton = new Button("Free draw"); // Button to activate free drawing.
        freeDrawButton.setOnAction( (e) -> freeDraw());
        freeDrawButton.setPrefWidth(95);

        ComboBox<String> comboBoxStrokeWidth = new ComboBox<>(); // This is our stroke width comboBox
        comboBoxStrokeWidth.setEditable(false);
        comboBoxStrokeWidth.getItems().addAll(strokeChoices);
        comboBoxStrokeWidth.setValue("normal");
        comboBoxStrokeWidth.setOnAction(e -> currentStroke = comboBoxStrokeWidth.getSelectionModel().getSelectedItem());
        comboBoxStrokeWidth.setPrefWidth(95);

        fillButton = new Button("Fill Shapes"); // Button to choose if shapes should be filled.
        fillButton.setOnAction( (e) -> fillShape());
        fillButton.setPrefWidth(95);
        fillShape = false;

        ComboBox<String> comboboxSize = new ComboBox<>(); // This is our combobox of sizes.
        comboboxSize.setEditable(false);
        comboboxSize.getItems().addAll(sizes); // Adding the names of our sizes.
        comboboxSize.setValue("medium"); // Setting the value of the comboBox to meet our default size selection.
        comboboxSize.setOnAction(
                e -> selectedSize = comboboxSize.getSelectionModel().getSelectedItem());
        comboboxSize.setPrefWidth(95);

        Button clearButton = new Button("Clear Canvas"); // Button to clear the canvases
        clearButton.setOnAction((e) -> clearCanvases());
        clearButton.setPrefWidth(95);

        VBox tools = new VBox(10);
        tools.getChildren().add(combobox);
        tools.getChildren().add(freeDrawButton);
        tools.getChildren().add(comboBoxStrokeWidth);
        tools.getChildren().add(fillButton);
        tools.getChildren().add(comboboxSize);
        tools.getChildren().add(clearButton);

        tools.setStyle("-fx-border-width: 3px; -fx-border-color: transparent; -fx-background-color: silver");

        return tools;
    }

    /**
     * When called this will fully clear the canvases and the array of shapes.
     */
    private void clearCanvases() {
        g1.clearRect(0,0, canvasWidth, canvasHeight);
        g2.clearRect(0,0, canvasWidth, canvasHeight);

        shapesList = new Shapes[shapesList.length];
        shapeCount = 0;
    }

    /**
     * This cycles the fill boolean and borders the button if true.
     * When true the shapes will be filled.
     */
    private void fillShape() {
        fillShape = !fillShape;
        if(fillShape){
            fillButton.setStyle("-fx-border-color: blue;");
        }else fillButton.setStyle("-fx-border-color: transparent;");
    }

    /**
     * Called when the free draw is selected in our tool box.
     */
    private void freeDraw() {
        logger.trace("freeDraw() called.");
        drawing = !drawing;
        if(drawing) {
            freeDrawButton.setStyle("-fx-border-color: blue;");
            canvas1.toFront(); // This is brought to the front because it is our free draw canvas.
        }else {
            canvas2.toFront(); // We cycle back to our shape canvas now.
            freeDrawButton.setStyle("-fx-border-color: transparent;");
        }
    }


    private void drawShape(String shapeType, String size, boolean fill){
        canvas2.toFront(); // We bring the canvas to the front because this is our shape canvas.
        drawing = false;
        freeDrawButton.setStyle("-fx-border-color: transparent;");
        Shapes shape = artist.drawShape(shapeType, size, fill, currentColor, 10, 10, dragging);
        shapesList[shapeCount] = shape;
        shapeCount++;
        paintCanvas(g2);
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
                Shapes s = shapesList[i];
                System.out.println(i);
                s.createShape(gc); // Draw the shape on the canvas again.
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

        g1.setLineWidth(strokeWidth());  // Use a 2-pixel-wide line for drawing.
        g1.setStroke(currentColor); // Line color is our current color.
    }

    /**
     * Sets the pixel size of our stroke based on the selection in the combobox.
     * @return returns an int that is representative of the pixel size of our stroke.
     */
    private int strokeWidth(){
        return switch (currentStroke) {
            case "fine" -> 1;
            case "normal" -> 3;
            case "large" -> 5;
            case "very large" -> 10;
            default -> throw new AssertionError();
        };
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
            shapeBeingDragged.moveShape(x - prevX, y - prevY);
            prevX = x;
            prevY = y;
            paintCanvas(g2);      // redraw canvas to show shape in new position
        }
    }

    private void mouseReleasedShape(MouseEvent mouseEvent){

        shapeBeingDragged = null;
        dragging = false;
    }

    /**
     * Checks to see if there is a shape where the user clicked.
     * @param mouseEvent fetches our mouse coordinates to be checked against the coordinates of shapes.
     */
    private void mousePressedShape(MouseEvent mouseEvent){
        int x = (int)mouseEvent.getX(); // x-coord of where mouse was clicked
        int y = (int)mouseEvent.getY(); // y-coord of where mouse was clicked

        for (int i = shapeCount - 1; i >= 0; i--) {  // check shapes
            Shapes s = shapesList[i];
            if (s.containedCoordinates(x,y)) { // true if a shape is at the mouse location
                dragging = true;
                shapeBeingDragged = s; // we set our shape being dragged
                prevX = x;
                prevY = y;


                paintCanvas(g2);  // repaint canvas
            }
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
            Shapes s = shapesList[i];
            s.createShape(g1);
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




