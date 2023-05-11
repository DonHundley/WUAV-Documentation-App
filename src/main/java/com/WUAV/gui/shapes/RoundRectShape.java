package com.WUAV.gui.shapes;

import javafx.scene.canvas.*;
import javafx.scene.paint.*;

public class RoundRectShape extends Shape {
    // This class represents rectangle gui.shapes with rounded corners.
    // (Note that it uses the inherited version of the
    // containsPoint(x,y) method, even though that is not perfectly
    // accurate when (x,y) is near one of the corners.)
    @Override
    public void draw(GraphicsContext g) {
        g.setFill(color);
        g.fillRoundRect(left, top, width, height, width / 3, height / 3);
        g.setStroke(Color.BLACK);
        g.strokeRoundRect(left, top, width, height, width / 3, height / 3);
    }
}
