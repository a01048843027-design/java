package Intro;

public class CallByReference {
	public static void main(String[] args) {
		Circle pizza = new Circle(10);
		
		increase(pizza);
		System.out.println(pizza.radius);
	}
	public static void increase(Circle m) {
		m.radius++;
	}
}
