package shapeFactory;

import javafx.scene.paint.*;

public class ShapeArtist { // This is a shape factory
    
    public Shapes drawShape(String shapeType, String size, boolean fill, Color color, int drawAtX, int drawAtY, boolean dragging){
        switch (shapeType){
            case "circle":
                return new Circle(size, color, fill, drawAtX, drawAtY);
            case "square":
                return new Square(size, color, fill, drawAtX, drawAtY);
            case "triangle":
                return new Triangle(size, color, fill, drawAtX, drawAtY, dragging);
            case "oval":
                return new Oval(size, color, fill,drawAtX,drawAtY);
            case "rectangle":
                return new Rectangle(size, color, fill, drawAtX, drawAtY);
            default:
                throw new AssertionError();
        }
    }
}
