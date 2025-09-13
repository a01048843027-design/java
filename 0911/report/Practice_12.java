// 자동차 제어 장치에는 자동차의 상태를 나타내는 정수형 변수가 있다. 이 변수의 각 비트는 다음과 같이 자동차의 상태를 나타낸다고 하자.
// - 비트 0~5 : 현재 자동차 내 온도 값으로, 십진수로는 0~31의 값
// - 비트 6 : 값이 0이면 에어컨이 꺼져 있는 상태, 1이면 켜져 있는 상태
// - 비트 7 : 값이 0이면 자동차가 정지 상태, 1이면 달리는 상태
// - 비트 8 이상 : 아무 의미 없음
//
// 예를 들어, 자동차의 상태를 나타내는 정수형 변수의 값이 139이라면, 이 값은 이진수 10001011이므로
// 비트 7의 값이 1이고, 비트 6의 값이 0이며, 비트 0~5의 값이 십진수로 11이다.
// 그러므로 자동차는 '달리는 상태'이고, 에어컨은 '꺼진 상태'이며, 자동차 내 온도는 '11도'이다.
//
// 자동차 상태를 나타내는 정수를 입력받아 자동차의 상태를 화면에 출력하는 프로그램을 작성하라.

//자동차 상태 입력>>139
//자동차는 달리는 상태이고 에어컨이 꺼진 상태이고 온도는 11도이다.

//자동차 상태 입력>>88
//자동차는 정지 상태이고 에어컨이 켜진 상태이고 온도는 16도이다.

package basic;

import java.util.Scanner;

public class CarStatus {

	public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        System.out.print("자동차 상태 입력>> ");
        int status = s.nextInt();

        // 비트 7
        if ((status & 0b10000000) != 0) System.out.print("자동차는 달리는 상태이고, ");
        else System.out.print("자동차는 정지 상태이고, ");
        
        // 비트 6
        if ((status & 0b01000000) != 0) System.out.print("에어컨이 켜진 상태이고 ");
        else System.out.print("에어컨이 꺼진 상태이며 ");

        // 비트 0~5
        int temperature = status & 0b00111111;  // 비트 AND 연산
        System.out.println("온도는 " + temperature + "도이다.");
        
        
        s.close();
	}

}
