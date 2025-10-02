package Intro;
class CircleEx{
	int radius;
	
	public CircleEx(int radius) {
		this.radius = radius;
	}
	public double getArea() {
		return 3.14*radius*radius;
	}
}
public class CircleArray {
	public static void main(String[] args) {
		CircleEx[] ce = new CircleEx[5];
		for (int i = 0; i < ce.length; i++) {
			ce[i] = new CircleEx(i+1);
		}
	}
}
