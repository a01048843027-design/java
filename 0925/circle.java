package intro;

public class Circle {
    int radius;
    String name;
    public Circle() {

    }

    double getArea() {
        return 3.14 * radius * radius;
    }
    public static void main(String[] args){
        Circle c1 = new Circle()
        c1.radius = 5;
        double c1area = c1.getArea();
        System.out.println("원의 면적 = "+c1Area);
    }
}