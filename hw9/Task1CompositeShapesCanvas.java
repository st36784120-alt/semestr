import java.util.Arrays;

class Point2D {
    private double x;
    private double y;

    public Point2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point2D(Point2D other) {
        if (other != null) {
            this.x = other.x;
            this.y = other.y;
        }
    }

    public double getX() { return x; }
    public double getY() { return y; }

    public void translate(double dx, double dy) {
        this.x += dx;
        this.y += dy;
    }

    public double distanceTo(Point2D other) {
        if (other == null) return 0.0;
        return Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2));
    }

    @Override
    public String toString() {
        return String.format("(%.2f; %.2f)", x, y);
    }
}

abstract class AbstractShape2D {
    private final String name;
    private final Point2D anchorPoint;

    public AbstractShape2D(String name, Point2D anchorPoint) {
        this.name = (name == null || name.isBlank()) ? "Невідома фігура" : name;
        this.anchorPoint = (anchorPoint != null) ? new Point2D(anchorPoint) : new Point2D(0, 0);
    }

    public String getName() {
        return name;
    }

    public Point2D getAnchorPoint() {
        return new Point2D(anchorPoint);
    }

    public void moveBy(double dx, double dy) {
        this.anchorPoint.translate(dx, dy);
    }

    public abstract double getArea();
    public abstract double getPerimeter();

    @Override
    public String toString() {
        return String.format("%s[name='%s', center=%s | S=%.2f, P=%.2f]",
                this.getClass().getSimpleName(), name, anchorPoint, getArea(), getPerimeter());
    }
}

class Circle2D extends AbstractShape2D {
    private final double radius;

    public Circle2D(String name, Point2D anchorPoint, double radius) {
        super(name, anchorPoint);
        this.radius = Math.max(0.1, radius);
    }

    @Override
    public double getArea() {
        return Math.PI * radius * radius;
    }

    @Override
    public double getPerimeter() {
        return 2 * Math.PI * radius;
    }
}

class Rectangle2D extends AbstractShape2D {
    private final double width;
    private final double height;

    public Rectangle2D(String name, Point2D anchorPoint, double width, double height) {
        super(name, anchorPoint);
        this.width = Math.max(0.1, width);
        this.height = Math.max(0.1, height);
    }

    @Override
    public double getArea() {
        return width * height;
    }

    @Override
    public double getPerimeter() {
        return 2 * (width + height);
    }
}

class RightTriangle2D extends AbstractShape2D {
    private final double legA;
    private final double legB;

    public RightTriangle2D(String name, Point2D anchorPoint, double legA, double legB) {
        super(name, anchorPoint);
        this.legA = Math.max(0.1, legA);
        this.legB = Math.max(0.1, legB);
    }

    public double getHypotenuse() {
        return Math.sqrt(legA * legA + legB * legB);
    }

    @Override
    public double getArea() {
        return 0.5 * legA * legB;
    }

    @Override
    public double getPerimeter() {
        return legA + legB + getHypotenuse();
    }
}

class GeometryCanvas {
    private final String canvasTitle;
    private final AbstractShape2D[] shapes;
    private int shapeCount;

    public GeometryCanvas(String canvasTitle, int maxShapes) {
        this.canvasTitle = canvasTitle;
        this.shapes = new AbstractShape2D[maxShapes];
        this.shapeCount = 0;
    }

    public boolean addShape(AbstractShape2D shape) {
        if (shape == null || shapeCount >= shapes.length) {
            return false;
        }
        shapes[shapeCount++] = shape;
        return true;
    }

    public double getTotalArea() {
        double total = 0;
        for (int i = 0; i < shapeCount; i++) {
            total += shapes[i].getArea();
        }
        return total;
    }

    public double getTotalPerimeter() {
        double total = 0;
        for (int i = 0; i < shapeCount; i++) {
            total += shapes[i].getPerimeter();
        }
        return total;
    }

    public AbstractShape2D findLargestShape() {
        if (shapeCount == 0) return null;
        AbstractShape2D largest = shapes[0];
        for (int i = 1; i < shapeCount; i++) {
            if (shapes[i].getArea() > largest.getArea()) {
                largest = shapes[i];
            }
        }
        return largest;
    }

    public void moveAllShapes(double dx, double dy) {
        for (int i = 0; i < shapeCount; i++) {
            shapes[i].moveBy(dx, dy);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Полотно: '%s' (Фігур: %d/%d)\n", canvasTitle, shapeCount, shapes.length));
        for (int i = 0; i < shapeCount; i++) {
            sb.append(String.format("  %d. %s\n", (i + 1), shapes[i].toString()));
        }
        return sb.toString();
    }
}

public class Task1CompositeShapesCanvas {

    public static void main(String[] args) {
        GeometryCanvas canvas = new GeometryCanvas("Інженерне креслення №1", 5);

        Point2D centerA = new Point2D(0, 0);
        Circle2D circle = new Circle2D("Коло-А", centerA, 5.0);

        Point2D centerB = new Point2D(10, 5);
        Rectangle2D rect = new Rectangle2D("Плита-Б", centerB, 4.0, 6.0);

        Point2D centerC = new Point2D(-3, 2);
        RightTriangle2D triangle = new RightTriangle2D("Клин-В", centerC, 3.0, 4.0);

        canvas.addShape(circle);
        canvas.addShape(rect);
        canvas.addShape(triangle);

        System.out.println("================ ГЕОМЕТРИЧНЕ ПОЛОТНО (КОМПОЗИЦІЯ + ABSTRACT) ================");
        System.out.print(canvas);
        System.out.println("-----------------------------------------------------------------------------");
        System.out.printf("Сумарна площа креслення:    %.2f кв.од.\n", canvas.getTotalArea());
        System.out.printf("Сумарний периметр:          %.2f од.\n", canvas.getTotalPerimeter());

        AbstractShape2D largest = canvas.findLargestShape();
        if (largest != null) {
            System.out.printf("Найбільша фігура:           %s (S = %.2f)\n", largest.getName(), largest.getArea());
        }

        System.out.println("-> Переміщуємо всі фігури полотна на вектор (+5.0; -2.0)...");
        canvas.moveAllShapes(5.0, -2.0);
        System.out.printf("Новий центр 'Коло-А': %s\n", circle.getAnchorPoint());
        System.out.println("=============================================================================");
    }
}