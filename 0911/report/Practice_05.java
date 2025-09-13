// 출석 점수가 총 100점일 때, 지각하면 3점 감점, 결석하면 8점을 감점시킨다.
// 다음 실행 사례와 같이 지각횟수, 결석횟수 순으로 입력할 때, 두 학생 중 누구의 출석 점수가 높은지 판단하는 프로그램을 작성하라.
// 점수가 같은 경우 "점수 동일"이라고 출력하라.

//학생1>>4 3
//학생2>>2 4
//김유진의 감점은 36, 김경미의 감점은 38
//김유진의 출석 점수가 더 높음. 김유진 출석 점수는 64

package basic;

import java.util.Scanner;

public class Attendance {
	
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		
		System.out.print("학생1>>");
		String student1 = s.next();
		int a = s.nextInt();
		int b = s.nextInt();
		int deduction1 = a * 3 + b * 8;
		int score1 = 100 - deduction1;
		
		System.out.print("학생2>>");
		String student2 = s.next();
		int x = s.nextInt();
		int y = s.nextInt();
		int deduction2 = x * 3 + y * 8;
		int score2 = 100 - deduction2;
		
		System.out.println(student1 + "의 감점은 " + deduction1 + ", " + student2 + "의 감점은 " + deduction2);
		
		if (score1 > score2) {
			System.out.println(student1 + "의 출석 점수가 더 높음. " + student1 + "출석 점수는 " + score1);
		} 
		else if (score1 < score2) {
			System.out.println(student2 + "의 출석 점수가 더 높음. " + student2 + "출석 점수는 " + score2);

		}
		else {
			System.out.println("점수 동일");
		}
		
		s.close();
	}
	
}
