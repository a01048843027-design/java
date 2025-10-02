package Intro;
class Triangle{
	int height, width;
	
	double getArea() {
		return 0.5 * height * width;
	}
}
public class TriangleTest {
	public static void main(String[] args) {
		Triangle t1 = new Triangle();
		t1.height = 10;
		t1.width = 20;
		System.out.println("삼각형의 면적 = " + t1.getArea());
	}
}
