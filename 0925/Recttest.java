package Intro;

public class Recttest{
	public static void main(String[] args) {
		Rectangle r1 = new Rectangle();
		r1.height = 10;
		r1.width = 20;
		System.out.println("사각형의 면적 = " + r1.getArea());
	}
}