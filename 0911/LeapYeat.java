package basic;

import java.util.Scanner;

public class LeapYear {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int year;
		
		System.out.println("년도 입력: ");
		year = scanner.nextInt();
		
		if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) {
			System.out.println(year + "년은 윤년(leap year)입니다.");
		} else { 
			System.out.println(year + "년은 평년(common year)입니다.");
		}
		
		scanner.close();
	}
}