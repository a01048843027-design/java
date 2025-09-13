// 생일 케이크에 사용할 초를 준비하려고 한다. 빨간 초는 10살, 파란 초는 5살, 노란 초는 1살을 나타낼 때, 
// 다음 실행 사례를 참고하여 나이에 맞는 각 초의 개수를 계산하는 프로그램을 작성하라.
// 이 때 전체 초의 개수가 최소가 되게 하라(빨간 초, 파란 초, 노란 초 순으로 개수를 계산하면 된다).
// 나이에 0이나 음수가 입력되면, "나이는 양수만 입력하세요."를 출력하고 프로그램 종료하라.

//나이를 입력하세요>>27
//빨간 초 2개, 파란 초 1개, 노란 초 2개. 총 5개가 필요합니다.

package basic;

import java.util.Scanner;

public class Candle {

	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		
		System.out.print("나이를 입력하세요>>");
		int age = s.nextInt();
		if (age <= 0) {
			System.out.println("나이는 양수로만 입력하세요.");
			System.exit(0); // 프로그램 종료
		}
		
		int red = age / 10;
		int blue = age % 10 / 5;
		int yellow = age % 5;
		int sum = red + blue + yellow;
		
		System.out.print("빨간 초 " + red + "개, 파란 초 " + blue + "개, 노란 초 " + yellow + "개. ");
		System.out.println("총 " + sum + "개가 필요합니다.");
		
		s.close();
	}

}
