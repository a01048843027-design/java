package Intro;

public class TestArg {
	int a, b, c, d;
	public TestArg(int a, int b, int c, int d) {
		this.a = a; this.b = b; this.c = c; this.d = d;
	}
	public TestArg(int a, int b, int c) {
		this(a, b, c, 10);
	}
	public TestArg(int a, int b) {
		this(a, b, 20, 10);
	}
	public TestArg(int a) {
		this(a, 30, 20, 10);
	}
	public TestArg() {
		this(40, 30, 20, 10);
	}
	public static void main(String[] args) {
		TestArg ta1 = new TestArg();
		System.out.println(ta1.a);
		int[] a = new int[5];    // int[] a =  int a[]
		a[0] = 1;
		
		Circle[] c = new Circle[5];
		c[0] = new Circle();
		
		
	}
}
