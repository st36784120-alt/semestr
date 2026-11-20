import java.util.Locale;
public class Task1PolymorphicShapesAndTransforms {

    public static void main(String[] args) {
        AbstractShape2D[] shapes = {
                new CircleShape("Коло-Лінза", 0.0, 0.0, 5.0),
                new RectangleShape("Рамка-А", 2.0, 3.0, 4.0, 6.0),
                new ImmutableReferenceSquare("Еталон-Квадрат", 10.0, 10.0, 5.0)
        };

        System.out.println("================ ПОЛІМОРФНА ГЕОМЕТРІЯ ТА ІНТЕРФЕЙСИ ТРАНСФОРМАЦІЙ ================");
        System.out.println("[СТАН СЦЕНИ ДО ТРАНСФОРМАЦІЇ]:");
        printScene(shapes);
        System.out.printf(Locale.US, "Сумарна площа сцени: %.2f кв.од.%n%n", calculateTotalArea(shapes));

        System.out.println("-> Виконуємо пакетну трансформацію (scale x2.0, rotate +45.0°) через instanceof...");
        transformScene(shapes, 2.0, 45.0);

        System.out.println("\n[СТАН СЦЕНИ ПІСЛЯ ТРАНСФОРМАЦІЇ]:");
        printScene(shapes);
        System.out.printf(Locale.US, "Нова сумарна площа сцени: %.2f кв.од.%n", calculateTotalArea(shapes));
        System.out.println("==================================================================================");
    }

    public static double calculateTotalArea(AbstractShape2D[] shapes) {
        double total = 0.0;
        for (AbstractShape2D shape : shapes) {
            total += shape.getArea();
        }
        return total;
    }

    public static void transformScene(AbstractShape2D[] shapes, double scaleFactor, double rotateDeg) {
        for (AbstractShape2D shape : shapes) {
            boolean modified = false;
            if (shape instanceof Scalable2D s) {
                s.scale(scaleFactor);
                modified = true;
                if (shape instanceof CircleShape c) {
                    System.out.printf(Locale.US, "   [Scalable2D] '%s' масштабовано в %.1f разів (новий r=%.2f)%n",
                            shape.getName(), scaleFactor, c.getRadius());
                } else if (shape instanceof RectangleShape rect) {
                    System.out.printf(Locale.US, "   [Scalable2D] '%s' масштабовано в %.1f разів (%.2fx%.2f)%n",
                            shape.getName(), scaleFactor, rect.getWidth(), rect.getHeight());
                }
            }
            if (shape instanceof Rotatable2D r) {
                r.rotate(rotateDeg);
                modified = true;
                System.out.printf(Locale.US, "   [Rotatable2D] '%s' повернуто на +%.1f° (поточний кут: %.1f°)%n",
                        shape.getName(), rotateDeg, r.getRotationAngle());
            }
            if (!modified) {
                System.out.printf(Locale.US, "   [Info] '%s' (final class) пропущено — не підтримує трансформації.%n",
                        shape.getName());
            }
        }
    }

    private static void printScene(AbstractShape2D[] shapes) {
        for (int i = 0; i < shapes.length; i++) {
            System.out.println("  " + (i + 1) + ". " + shapes[i]);
        }
    }
}

interface Scalable2D {
    double MIN_SCALE_FACTOR = 0.1;

    void scale(double factor);
}

interface Rotatable2D {
    void rotate(double angleDegrees);

    double getRotationAngle();
}

abstract class AbstractShape2D {
    private final String name;
    private double centerX;
    private double centerY;

    public AbstractShape2D(String name, double centerX, double centerY) {
        this.name = name;
        this.centerX = centerX;
        this.centerY = centerY;
    }

    public String getName() {
        return name;
    }

    public double getCenterX() {
        return centerX;
    }

    public double getCenterY() {
        return centerY;
    }

    public abstract double getArea();

    public abstract double getPerimeter();

    public final double getIsoperimetricQuotient() {
        double p = getPerimeter();
        if (p == 0.0) {
            return 0.0;
        }
        return (4.0 * Math.PI * getArea()) / (p * p);
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "%s['%s', center=(%.1f; %.1f) | S=%.2f, P=%.2f, Q=%.4f]",
                getClass().getSimpleName(), name, centerX, centerY, getArea(), getPerimeter(), getIsoperimetricQuotient());
    }
}

class CircleShape extends AbstractShape2D implements Scalable2D {
    private double radius;

    public CircleShape(String name, double centerX, double centerY, double radius) {
        super(name, centerX, centerY);
        this.radius = radius > 0 ? radius : MIN_SCALE_FACTOR;
    }

    public double getRadius() {
        return radius;
    }

    @Override
    public double getArea() {
        return Math.PI * radius * radius;
    }

    @Override
    public double getPerimeter() {
        return 2.0 * Math.PI * radius;
    }

    @Override
    public void scale(double factor) {
        double safeFactor = factor < MIN_SCALE_FACTOR ? MIN_SCALE_FACTOR : factor;
        radius *= safeFactor;
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "CircleShape['%s', center=(%.1f; %.1f), r=%.2f | S=%.2f, P=%.2f, Q=%.4f]",
                getName(), getCenterX(), getCenterY(), radius, getArea(), getPerimeter(), getIsoperimetricQuotient());
    }
}

class RectangleShape extends AbstractShape2D implements Scalable2D, Rotatable2D {
    private double width;
    private double height;
    private double angleDegrees;

    public RectangleShape(String name, double centerX, double centerY, double width, double height) {
        super(name, centerX, centerY);
        this.width = width > 0 ? width : MIN_SCALE_FACTOR;
        this.height = height > 0 ? height : MIN_SCALE_FACTOR;
        this.angleDegrees = 0.0;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    @Override
    public double getArea() {
        return width * height;
    }

    @Override
    public double getPerimeter() {
        return 2.0 * (width + height);
    }

    @Override
    public void scale(double factor) {
        double safeFactor = factor < MIN_SCALE_FACTOR ? MIN_SCALE_FACTOR : factor;
        width *= safeFactor;
        height *= safeFactor;
    }

    @Override
    public void rotate(double angleDegrees) {
        this.angleDegrees = (this.angleDegrees + angleDegrees) % 360.0;
        if (this.angleDegrees < 0.0) {
            this.angleDegrees += 360.0;
        }
    }

    @Override
    public double getRotationAngle() {
        return angleDegrees;
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "RectangleShape['%s', center=(%.1f; %.1f), %.2fx%.2f, angle=%.1f° | S=%.2f, P=%.2f, Q=%.4f]",
                getName(), getCenterX(), getCenterY(), width, height, angleDegrees, getArea(), getPerimeter(), getIsoperimetricQuotient());
    }
}

final class ImmutableReferenceSquare extends AbstractShape2D {
    private final double side;

    public ImmutableReferenceSquare(String name, double centerX, double centerY, double side) {
        super(name, centerX, centerY);
        this.side = side;
    }

    @Override
    public double getArea() {
        return side * side;
    }

    @Override
    public double getPerimeter() {
        return 4.0 * side;
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "ImmutableReferenceSquare['%s', center=(%.1f; %.1f), side=%.2f | S=%.2f, P=%.2f, Q=%.4f]",
                getName(), getCenterX(), getCenterY(), side, getArea(), getPerimeter(), getIsoperimetricQuotient());
    }
}