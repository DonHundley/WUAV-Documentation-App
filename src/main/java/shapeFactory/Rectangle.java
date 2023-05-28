package shapeFactory;

import javafx.scene.canvas.*;
import javafx.scene.paint.*;

public class Rectangle extends Shapes{


    public Rectangle(String size, Color color, boolean fill, int left, int top, GraphicsContext gc) {
        super(size, color, fill, left, top, gc);
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
        chooseSize(size);
        if(fill) {
            gc.setFill(color);
            gc.fillRect(left, top, width, height);
        }
        gc.setStroke(Color.BLACK);
        gc.strokeRect(left, top, width, height);
    }


}
