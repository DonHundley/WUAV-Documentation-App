package shapeFactory;

import javafx.scene.canvas.*;
import javafx.scene.paint.*;

public class Square extends Shapes{


    public Square(String size, Color color, boolean fill, int left, int top) {
        super(size, color, fill, left, top);
    }

    @Override
    public void chooseSize(String size) {
        switch (size) {
            case "small" -> {
                width = 50;
                height = 50;
            }
            case "medium" -> {
                width = 100;
                height = 100;
            }
            case "large" -> {
                width = 150;
                height = 150;
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
            gc.fillRect(left, top, width, height);
        }
        gc.setStroke(Color.BLACK);
        gc.strokeRect(left, top, width, height);
    }


}
