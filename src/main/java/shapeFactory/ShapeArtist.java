package shapeFactory;

import javafx.scene.canvas.*;
import javafx.scene.paint.*;

public class ShapeArtist { // This is a shape factory
    
    public Shapes drawShape(String shapeType, String size, boolean fill, Color color, int drawAtX, int drawAtY, boolean dragging, GraphicsContext gc){
        return switch (shapeType) {
            case "circle" -> new Circle(size, color, fill, drawAtX, drawAtY, gc);
            case "square" -> new Square(size, color, fill, drawAtX, drawAtY,gc);
            case "triangle" -> new Triangle(size, color, fill, drawAtX, drawAtY, dragging, gc);
            case "oval" -> new Oval(size, color, fill, drawAtX, drawAtY, gc);
            case "rectangle" -> new Rectangle(size, color, fill, drawAtX, drawAtY, gc);
            default -> throw new AssertionError();
        };
    }
}
