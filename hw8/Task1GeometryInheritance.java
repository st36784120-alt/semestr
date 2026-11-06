import java.util.Locale;

class Circle {
    private double r;

    public Circle(double r) {
        this.r = (r > 0) ? r : 1.0;
    }

    public double getR() { return r; }
    public double getArea() { return Math.PI * r * r; }
    public double getCirc() { return 2 * Math.PI * r; }

    @Override
    public String toString() {
        return String.format(Locale.US, "Circle[r=%.2f, S=%.2f, C=%.2f]", r, getArea(), getCirc());
    }
}

class Cyl extends Circle {
    private double h;

    public Cyl(double r, double h) {
        super(r);
        this.h = (h > 0) ? h : 1.0;
    }

    public double getVol() { return getArea() * h; }
    public double getFullArea() { return 2 * getArea() + getCirc() * h; }

    @Override
    public String toString() {
        return String.format(Locale.US, "Cylinder[base=%s, h=%.2f, V=%.2f, TotalArea=%.2f]",
                super.toString(), h, getVol(), getFullArea());
    }
}

class Cone extends Circle {
    private double h;

    public Cone(double r, double h) {
        super(r);
        this.h = (h > 0) ? h : 1.0;
    }

    public double getL() { return Math.sqrt(getR() * getR() + h * h); }
    public double getVol() { return getArea() * h / 3.0; }
    public double getFullArea() { return getArea() + Math.PI * getR() * getL(); }

    @Override
    public String toString() {
        return String.format(Locale.US, "Cone[base=%s, h=%.2f, l=%.2f, V=%.2f, TotalArea=%.2f]",
                super.toString(), h, getL(), getVol(), getFullArea());
    }
}

public class Task1GeometryInheritance {
    public static void main(String[] args) {
        System.out.println("================ ГЕОМЕТРІЯ ТІЛ ОБЕРТАННЯ ================");

        System.out.println("Базове коло:\n" + new Circle(5.0));
        System.out.println("\nЦиліндр (успадковано від Circle):\n" + new Cyl(5.0, 10.0));
        System.out.println("\nКонус (успадковано від Circle):\n" + new Cone(3.0, 4.0));

        System.out.println("=========================================================");
    }
}