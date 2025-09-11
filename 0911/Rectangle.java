package  basic;

import java.util.Scanner;

public class Rectangle {
	public static void main(String[] args) {
		/* 사각형의 height의 width를 입력받아 면적을 출력하는 프로그램 작성 */
		Scanner scanner = new Scanner(System.in);
		int height, width, area;
		System.out.print("높이 입력 : ");
		height = scanner.nextInt();
		System.out.print("너비 입력 : ");
		width = scanner.nextInt();
		area = height * width;
		System.out.println("높이 = " + height + ", 너비 = " + width + "인 사각형의 면적 = " + area);
		scanner.close();
	}
}