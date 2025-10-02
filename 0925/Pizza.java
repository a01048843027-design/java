package Intro;

public class Pizza {
	int radius;
	String name;
	
	double getArea() {
		return 3.14 * radius * radius;
	}
	public Pizza() {
		
	}
	public Pizza(int radius, String name) {
		this.radius = radius;
		this.name = name;
	}
	public static void main(String[] args) {
		Pizza p1 = new Pizza(5, "청담피자");
		Pizza p2 = new Pizza();
		
		//p1.radius = 5;	
		p2.radius = 10;
		//p1.name = "청담피자";
		p2.name = "도미노피자";
		
		System.out.println(p1.name+"의 크기는 = "+p1.getArea());
		System.out.println(p2.name+"의 크기는 = "+p2.getArea());
	}
}
