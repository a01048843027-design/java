package Intro;

public class Circle {
	int radius;
	String name;
	public Circle() {
	}
	public Circle(int radius) {
		this.radius = radius;
	}
	double getArea() {
		return Math.PI * radius * radius;
	}
	public static void main(String[] args) {
		Circle c1 = new Circle();
		c1.radius = 5;
		double c1Area = c1.getArea();
		System.out.println("원의 면적 = "+c1Area);
	}
}
