package gui.controller;

import be.Project;
import be.Task;
import gui.model.Functions;
import gui.model.Observables;
import gui.model.Persistent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.layout.BorderPane;

import javafx.scene.layout.HBox;

import javafx.scene.control.Button;

import java.util.Optional;

/**
 * The drawing application used in this controller at its current state takes heavy inspiration from and code snippets from both of these files:
 * https://math.hws.edu/javanotes/source/chapter5/ShapeDraw.java
 * https://math.hws.edu/javanotes/source/chapter6/SimplePaint.java
 * Both of these are resources attained from https://math.hws.edu/javanotes/ which is Introduction to Programming Using Java, a free, on-line textbook for introductory programming that uses Java as the language of instruction.
 */
public class EditLayoutController {


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

    /*
     * Array of colors corresponding to available colors in the palette.
     * (The last color is a slightly darker version of yellow for
     * better visibility on a white background.)
     */
    private final Color[] palette = {
            Color.BLACK, Color.RED, Color.GREEN, Color.BLUE,
            Color.CYAN, Color.MAGENTA, Color.color(0.95,0.9,0)
    };

    private Shape[] shapes = new Shape[500];  // Contains shapes the user has drawn.
    private int currentColorNum = 0;  // The currently selected drawing color,
    //   coded as an index into the above array

    private int shapeCount = 0; // Number of shapes that the user has drawn.

    private double prevX, prevY;   // The previous location of the mouse, when
    // the user is drawing by dragging the mouse.

    private boolean dragging;   // This is set to true while the user is drawing.

    private boolean freeHandDrawing; // This is set to true when the user wishes to draw by hand.

    @FXML
    private Canvas canvas;  // The canvas on which everything is drawn.

    private GraphicsContext g;  // For drawing on the canvas.

    public void setUpCanvas(){


        /* Create the canvas and draw its content for the first time. */
        g = canvas.getGraphicsContext2D();
        //clearAndDrawPalette();
        paintCanvas();
        /* Respond to mouse events on the canvas, by calling methods in this class. */

        canvas.setOnMousePressed(this::mousePressed);
        canvas.setOnMouseDragged(this::mouseDragged);
        canvas.setOnMouseReleased(this::mouseReleased);

        bPane.setBottom(makeToolPanel(canvas));
    }

    private HBox makeToolPanel(Canvas canvas) {
        // Make a pane containing the buttons that are used to add shapes
        // and the pop-up menu for selecting the current color.
        freeDrawButton = new Button("Free draw");
        freeDrawButton.setOnAction( (e) -> freeDraw());
        Button ovalButton = new Button("Add an Oval");
        ovalButton.setOnAction( (e) -> addShape( new OvalShape() ) );
        Button rectButton = new Button("Add a Rect");
        rectButton.setOnAction( (e) -> addShape( new RectShape() ) );
        Button roundRectButton = new Button("Add a RoundRect");
        roundRectButton.setOnAction( (e) -> addShape( new RoundRectShape() ) );


        HBox tools = new HBox(10);
        tools.getChildren().add(freeDrawButton);
        tools.getChildren().add(ovalButton);
        tools.getChildren().add(rectButton);
        tools.getChildren().add(roundRectButton);

        tools.setStyle("-fx-border-width: 5px; -fx-border-color: transparent; -fx-background-color: lightgray");
        return tools;
    }

    private void freeDraw() {
        freeHandDrawing = !freeHandDrawing;
        if(freeHandDrawing){
            freeDrawButton.setDisable(true);
        }
    }

    private void addShape(Shape shape) {
        // Add the shape to the canvas, and set its size/position and color.
        // The shape is added at the top-left corner, with size 80-by-50.
        // Then redraw the canvas to show the newly added shape.  This method
        // is used in the event listeners for the buttons in makeToolsPanel().
        shape.setColor(palette[currentColorNum]);
        shape.reshape(10,10,150,100);
        shapes[shapeCount] = shape;
        shapeCount++;
        paintCanvas();
        freeHandDrawing = false;
        freeDrawButton.setDisable(false);
    }
    private void paintCanvas() {
        int width = (int)canvas.getWidth();    // Width of the canvas.
        int height = (int)canvas.getHeight();  // Height of the canvas.

        // Redraw the shapes.  The entire list of shapes
        // is redrawn whenever the user adds a new shape
        // or moves an existing shape.
        GraphicsContext g = canvas.getGraphicsContext2D();
        g.setFill(Color.WHITE); // Fill with white background.
        g.fillRect(0,0,width,height);
        clearAndDrawPalette();

        for (int i = 0; i < shapeCount; i++) {
            Shape s = shapes[i];
            s.draw(g);
        }
    }

    /**
     * Fills the canvas with white and draws the color palette and (simulated)
     * "Clear" button on the right edge of the canvas.  This method is called when
     * the canvas is created and when the user clicks "Clear."
     */
    private void clearAndDrawPalette() {

        int width = (int)canvas.getWidth();    // Width of the canvas.
        int height = (int)canvas.getHeight();  // Height of the canvas.

        g.setFill(Color.WHITE);
        g.fillRect(0,0,width,height);

        int colorSpacing = (height - 56) / 7;
        // Distance between the top of one colored rectangle in the palette
        // and the top of the rectangle below it.  The height of the
        // rectangle will be colorSpacing - 3.  There are 7 colored rectangles,
        // so the available space is divided by 7.  The available space allows
        // for the gray border and the 50-by-50 CLEAR button.

        /* Draw a 3-pixel border around the canvas in gray.  This has to be
             done by drawing three rectangles of different sizes. */

        g.setStroke(Color.GRAY);
        g.setLineWidth(3);
        g.strokeRect(1.5, 1.5, width-3, height-3);

        /* Draw a 56-pixel wide gray rectangle along the right edge of the canvas.
             The color palette and Clear button will be drawn on top of this.
             (This covers some of the same area as the border I just drew. */

        g.setFill(Color.GRAY);
        g.fillRect(width - 56, 0, 56, height);

        /* Draw the "Clear button" as a 50-by-50 white rectangle in the lower right
             corner of the canvas, allowing for a 3-pixel border. */

        g.setFill(Color.WHITE);
        g.fillRect(width-53,  height-53, 50, 50);
        g.setFill(Color.BLACK);
        g.fillText("CLEAR", width-48, height-23);

        /* Draw the seven color rectangles. */

        for (int N = 0; N < 7; N++) {
            g.setFill( palette[N] );
            g.fillRect(width-53, 3 + N*colorSpacing, 50, colorSpacing-3);
        }

        /* Draw a 2-pixel white border around the color rectangle
             of the current drawing color. */

        g.setStroke(Color.WHITE);
        g.setLineWidth(2);
        g.strokeRect(width-54, 2 + currentColorNum*colorSpacing, 52, colorSpacing-1);



    } // end clearAndDrawPalette()


    /**
     * Change the drawing color after the user has clicked the
     * mouse on the color palette at a point with y-coordinate y.
     */
    private void changeColor(int y) {

        int width = (int)canvas.getWidth();
        int height = (int)canvas.getHeight();
        int colorSpacing = (height - 56) / 7;  // Space for one color rectangle.
        int newColor = y / colorSpacing;       // Which color number was clicked?

        if (newColor < 0 || newColor > 6)      // Make sure the color number is valid.
            return;

        /* Remove the highlight from the current color, by drawing over it in gray.
             Then change the current drawing color and draw a highlight around the
             new drawing color.  */

        g.setLineWidth(2);
        g.setStroke(Color.GRAY);
        g.strokeRect(width-54, 2 + currentColorNum*colorSpacing, 52, colorSpacing-1);
        currentColorNum = newColor;
        g.setStroke(Color.WHITE);
        g.strokeRect(width-54, 2 + currentColorNum*colorSpacing, 52, colorSpacing-1);

    } // end changeColor()



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

        if(freeHandDrawing) {
            if (dragging == true)  // Ignore mouse presses that occur
                return;            //    when user is already drawing a curve.
            //    (This can happen if the user presses
            //    two mouse buttons at the same time.)



            int width = (int) canvas.getWidth();    // Width of the canvas.
            int height = (int) canvas.getHeight();  // Height of the canvas.

            if (x > width - 53) {
                // User clicked to the right of the drawing area.
                // This click is either on the clear button or
                // on the color palette.
                if (y > height - 53)
                    clearAndDrawPalette();  //  Clicked on "CLEAR button".
                else
                    changeColor(y);  // Clicked on the color palette.
            } else if (x > 3 && x < width - 56 && y > 3 && y < height - 3) {
                // The user has clicked on the white drawing area.
                // Start drawing a curve from the point (x,y).
                prevX = x;
                prevY = y;
                dragging = true;
                g.setLineWidth(2);  // Use a 2-pixel-wide line for drawing.
                g.setStroke(palette[currentColorNum]);
            }
        }else {
        for (int i = shapeCount - 1; i >= 0; i--) {  // check shapes from front to back
            Shape s = shapes[i];
            if (s.containsPoint(x, y)) {
                shapeBeingDragged = s;
                prevDragX = x;
                prevDragY = y;
                if (evt.isShiftDown()) { // s should be moved on top of all the other shapes
                    for (int j = i; j < shapeCount - 1; j++) {
                        // move the shapes following s down in the list
                        shapes[j] = shapes[j + 1];
                    }
                    shapes[shapeCount - 1] = s;  // put s at the end of the list
                    paintCanvas();  // repaint canvas to show s in front of other shapes
                }
                return;
            }
        }
        }
        } // end mousePressed()


        /**
         * Called whenever the user releases the mouse button. Just sets
         * dragging to false.
         */
        public void mouseReleased (MouseEvent evt){
            if(freeHandDrawing){
            dragging = false;
            } else{
            shapeBeingDragged = null;
            }
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
        if(freeHandDrawing) {
            if (dragging == false)
                return;  // Nothing to do because the user isn't drawing.

            double x = evt.getX();   // x-coordinate of mouse.
            double y = evt.getY();   // y-coordinate of mouse.

            if (x < 3)                          // Adjust the value of x,
                x = 3;                           //   to make sure it's in
            if (x > canvas.getWidth() - 57)       //   the drawing area.
                x = (int) canvas.getWidth() - 57;

            if (y < 3)                          // Adjust the value of y,
                y = 3;                           //   to make sure it's in
            if (y > canvas.getHeight() - 4)       //   the drawing area.
                y = canvas.getHeight() - 4;

            g.strokeLine(prevX, prevY, x, y);  // Draw the line.

            prevX = x;  // Get ready for the next line segment in the curve.
            prevY = y;
        } else {
            // User has moved the mouse.  Move the dragged shape by the same amount.
            int x = (int)evt.getX();
            int y = (int)evt.getY();
            if (shapeBeingDragged != null) {
                shapeBeingDragged.moveBy(x - prevDragX, y - prevDragY);
                prevDragX = x;
                prevDragY = y;
                paintCanvas();      // redraw canvas to show shape in new position
            }
        }
        } // end mouseDragged()


        /**
         * Method to update the layout for a task.
         */
        @FXML
        private void editLayout (ActionEvent actionEvent){
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



    // ------- Nested class definitions for the abstract Shape class and three -----
    // -------------------- concrete subclasses of Shape. --------------------------

    abstract class Shape {

        // A class representing shapes that can be displayed on a ShapeCanvas.
        // The subclasses of this class represent particular types of shapes.
        // When a shape is first constructed, it has height and width zero
        // and a default color of white.

        int left, top;      // Position of top left corner of rectangle that bounds this shape.
        int width, height;  // Size of the bounding rectangle.
        Color color = Color.WHITE;  // Color of this shape.

        void reshape(int left, int top, int width, int height) {
            // Set the position and size of this shape.
            this.left = left;
            this.top = top;
            this.width = width;
            this.height = height;
        }

        void moveBy(int dx, int dy) {
            // Move the shape by dx pixels horizontally and dy pixels vertically
            // (by changing the position of the top-left corner of the shape).
            left += dx;
            top += dy;
        }

        void setColor(Color color) {
            // Set the color of this shape
            this.color = color;
        }

        boolean containsPoint(int x, int y) {
            // Check whether the shape contains the point (x,y).
            // By default, this just checks whether (x,y) is inside the
            // rectangle that bounds the shape.  This method should be
            // overridden by a subclass if the default behavior is not
            // appropriate for the subclass.
            if (x >= left && x < left+width && y >= top && y < top+height)
                return true;
            else
                return false;
        }

        abstract void draw(GraphicsContext g);
        // Draw the shape in the graphics context g.
        // This must be overriden in any concrete subclass.

    }  // end of class Shape



    class RectShape extends Shape {
        // This class represents rectangle shapes.
        void draw(GraphicsContext g) {
            g.setFill(color);
            g.fillRect(left,top,width,height);
            g.setStroke(Color.BLACK);
            g.strokeRect(left,top,width,height);
        }
    }


    class OvalShape extends Shape {
        // This class represents oval shapes.
        void draw(GraphicsContext g) {
            g.setFill(color);
            g.fillOval(left,top,width,height);
            g.setStroke(Color.BLACK);
            g.strokeOval(left,top,width,height);
        }
        boolean containsPoint(int x, int y) {
            // Check whether (x,y) is inside this oval, using the
            // mathematical equation of an ellipse.  This replaces the
            // definition of containsPoint that was inherited from the
            // Shape class.
            double rx = width/2.0;   // horizontal radius of ellipse
            double ry = height/2.0;  // vertical radius of ellipse
            double cx = left + rx;   // x-coord of center of ellipse
            double cy = top + ry;    // y-coord of center of ellipse
            if ( (ry*(x-cx))*(ry*(x-cx)) + (rx*(y-cy))*(rx*(y-cy)) <= rx*rx*ry*ry )
                return true;
            else
                return false;
        }
    }


    class RoundRectShape extends Shape {
        // This class represents rectangle shapes with rounded corners.
        // (Note that it uses the inherited version of the
        // containsPoint(x,y) method, even though that is not perfectly
        // accurate when (x,y) is near one of the corners.)
        void draw(GraphicsContext g) {
            g.setFill(color);
            g.fillRoundRect(left,top,width,height,width/3,height/3);
            g.setStroke(Color.BLACK);
            g.strokeRoundRect(left,top,width,height,width/3,height/3);
        }
    }



