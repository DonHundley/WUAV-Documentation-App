package com.WUAV.gui.shapes;


import javafx.scene.canvas.*;
import javafx.scene.paint.*;

public class RectShape extends Shape {
    // This class represents rectangle gui.shapes.
    @Override
    public void draw(GraphicsContext g) {
        g.setFill(color);
        g.fillRect(left, top, width, height);
        g.setStroke(Color.BLACK);
        g.strokeRect(left, top, width, height);
    }
}
