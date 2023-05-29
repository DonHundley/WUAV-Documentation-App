package shapeFactory;

import javafx.scene.canvas.*;
import javafx.scene.paint.*;


public class Triangle extends Shapes{
    private int x1;
    private int x2;
    private int x3;

    private int y1;
    private int y2;
    private int y3;

    private double[] xPoints;
    private double[] yPoints;

    public Triangle(String size, Color color, boolean fill, int left, int top, boolean dragging, GraphicsContext gc) {
        super(size, color, fill, left, top, gc);
        if(!dragging){
        chooseSize(size);
        xPoints = new double[]{x1, x2, x3};
        yPoints = new double[]{y1, y2, y3};
        }
    }

    @Override
    public void chooseSize(String size) {
        switch (size) {
            case "small" -> {
                x1 = 35;
                x2 = 10;
                x3 = 60;

                y1 = 10;
                y2 = 60;
                y3 = 60;
            }
            case "medium" -> {
                x1 = 55;
                x2 = 10;
                x3 = 110;

                y1 = 10;
                y2 = 110;
                y3 = 110;
            }
            case "large" -> {
                x1 = 85;
                x2 = 10;
                x3 = 160;

                y1 = 10;
                y2 = 160;
                y3 = 160;
            }
            default -> throw new AssertionError();
        }
    }

    @Override
    public void createShape(GraphicsContext gc) {
        if(fill) {
            gc.setFill(color);
            gc.fillPolygon(xPoints, yPoints, 3);
        }
        gc.setStroke(Color.BLACK);
        gc.strokePolygon(xPoints, yPoints, 3);
    }


    /**
     * We are changing the method to fit for a triangle, but we are still checking if (x,y) is inside the shape.
     * If the sum of a, b, and c is equal to area, the point is inside the triangle.
     * @param x the x coordinate to be checked
     * @param y the y coordinate to be checked
     * @return true if (x,y) is inside the shape.
     */
    @Override
    public boolean containedCoordinates(int x, int y){
        double area = areaOfTriangle(x1, y1, x2, y2, x3, y3);

        double a = areaOfTriangle(x,y,x2,y2,x3,y3);

        double b = areaOfTriangle(x1, y1, x, y, x3, y3);

        double c = areaOfTriangle(x1, y1, x2, y2, x, y);

        return (area == a + b + c);
    }

    /**
     * We change the location of the left and top of the shape by taking the current left and top and adding x and y respectively.
     * @param x The amount of pixels the x coordinate will change (left)
     * @param y The amount of pixels the y coordinate will change (top)
     */
    @Override
    public void moveShape(int x, int y){
        x1 += x;
        x2 += x;
        x3 += x;

        y1 += y;
        y2 += y;
        y3 += y;

        xPoints = new double[]{x1, x2, x3};
        yPoints = new double[]{y1, y2, y3};
    }

    /**
     * Calculate the area of a triangle with the given coordinates
     * @param x1 x of the first set of coords
     * @param y1 y of the first set
     * @param x2 x of set 2
     * @param y2 y of set 2
     * @param x3 x of set 3
     * @param y3 y of set 3
     * @return the area of the triangle
     */
    private double areaOfTriangle(int x1, int y1, int x2, int y2, int x3, int y3){
        return Math.abs((x1*(y2-y3) + x2*(y3-y1)+
                x3*(y1-y2))/2.0);
    }
}
