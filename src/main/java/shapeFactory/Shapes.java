package shapeFactory;

import javafx.scene.canvas.*;
import javafx.scene.paint.*;

public abstract class Shapes {
    protected int left, top; // The top left corner of the bounds the shape will be placed in.
    protected int width, height; // Size of the shape
    protected Color color; // color of the shape
    protected String size; // The selected size of the shape.
    protected boolean fill; // If true the shape will be filled.
    protected GraphicsContext gc;
    public Shapes(String size, Color color, boolean fill, int left, int top, GraphicsContext gc) {
        this.fill = fill;
        this.size = size;
        this.color = color;
        this.left = left;
        this.top = top;
        this.gc = gc;
    }

    /**
     * This is used to check if the given coordinates are inside our shape.
     * @param x the x coordinate to be checked
     * @param y the y coordinate to be checked
     * @return true if the coordinate is inside the shape.
     */
    public boolean containedCoordinates(int x, int y){
        return x >= left && x < left + width && y >= top && y < top + height;
    }

    /**
     * We change the location of the left and top of the shape by taking the current left and top and adding x and y respectively.
     * @param x The amount of pixels the x coordinate will change (left)
     * @param y The amount of pixels the y coordinate will change (top)
     */
    public void moveShape(int x, int y){
        left += x;
        top += y;
    }

    /**
     * Set the color of the shape.
     * @param color The color chosen
     */
    public void setColor(Color color){
        this.color = color;
    }

    /**
     * Choose the size of the shape
     * @param size the size of the shape being small, medium or large.
     */
    public abstract void chooseSize(String size);

    /**
     * This is what is used to actually draw the shape.
     * @param gc the graphics context the shape will be drawn on.
     */
    public abstract void createShape(GraphicsContext gc);
}
