// 여행 경비를 계산하는 프로그램을 작성한다. 방 하나에는 2명까지 투숙 가능하며 1명 투숙해도 1방의 비용을 지불한다.

//여행지>>태국 치앙마이
//인원수>>5
//숙박일>>4
//1인당 항공료>>250000
//1방 숙박비>>150000
//5명의 태국 치앙마이 4박 5일 여행에는 방이 3개 필요하며 경비는 3050000원입니다.

package basic;

import java.util.Scanner;

public class TravelCost {

	public static void main(String[] args) {
		System.out.print("여행지>>");
		Scanner s = new Scanner(System.in);
		String area = s.nextLine();
		
		System.out.print("인원수>>");
		int people = s.nextInt();
		int rooms = people / 2 + people % 2;
		System.out.print("숙박일>>");
		int sleep = s.nextInt();
		int days = sleep + 1;
		
		System.out.print("1인당 항공료>>");
		int airfare = s.nextInt();
		
		System.out.print("1방 숙박비>>");
		int accommodation = s.nextInt();
		int cost = airfare * people + accommodation * rooms * sleep;
		
		System.out.print(people + "명의 " + area + " " + sleep + "박 " + days + 
				"일 여행에는 방이 " + rooms + "개 필요하며 경비는 " + cost + "원입니다.");
		s.close();
	}

}
