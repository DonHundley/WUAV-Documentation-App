package shapeFactory;

import javafx.scene.canvas.*;
import javafx.scene.paint.*;

public class Oval extends Shapes{


    public Oval(String size, Color color, boolean fill, int left, int top) {
        super(size, color, fill, left, top);
    }

    @Override
    public void chooseSize(String size) {
        switch (size) {
            case "small" -> {
                width = 50;
                height = 33;
            }
            case "medium" -> {
                width = 100;
                height = 66;
            }
            case "large" -> {
                width = 150;
                height = 100;
            }
            default -> throw new AssertionError();
        }
    }

    @Override
    public void createShape(GraphicsContext gc) {
        System.out.println("called" + size + left + top + width + height + color.toString());
        chooseSize(size);
        if(fill) {
            gc.setFill(color);
            gc.fillOval(left, top, width, height);
        }
        gc.setStroke(Color.BLACK);
        gc.strokeOval(left, top, width, height);
    }


    /**
     * Overrides containedCoordinates to be used with an oval by utilizing the equation of an ellipse
     * @param x the x coordinate to be checked
     * @param y the y coordinate to be checked
     * @return true if (x,y) is inside the oval
     */
    @Override
    public boolean containedCoordinates(int x, int y){
        double hx = height / 2.0; // Vertical radius
        double wy = width / 2.0; // Horizontal radius
        double lx = left + wy; // x of the center of the oval
        double ty = top + hx; // y of the center of the oval

        double a = (hx * (x - lx)) * (hx * (x - lx));
        double b = (wy * (y - ty)) * (wy * (y - ty));

        return a + b <= wy * wy * hx * hx; // true if the x and y are inside the oval
    }


}
