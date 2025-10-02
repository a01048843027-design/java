package Intro;

public class ArrayPassing {
	public static void main(String[] args) {
		int a[] = {1, 2, 3, 4, 5};
		
		increase(a);
		
		for (int i = 0; i < a.length; i++) {
			System.out.println(a[i]);
		}
	}
	public static void increase(int[] b) {
		for (int i = 0; i < b.length; i++) {
			b[i]++;
		}
	}
}
