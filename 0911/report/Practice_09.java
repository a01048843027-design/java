// 2차원 평면에서 사각형은 두 점으로 표현할 수 있다. (10, 10)과 (200, 300)의 두 점으로 이루어진 사각형이 있을 때, 
// 정수 x와 y 값을 입력받고 점 (x, y)가 이 사각형 안에 있는지, 사각형 선 상에 있는지, 사각형 외부에 있는지를 판별하는 프로그램을 작성하라.
// 다음은 서로 다른 3가지 경우에 대한 실행 사례이다.

//점 (x, y)의 좌표 입력>>50 50
//(50,50)는 사각형 안에 있습니다.

//점 (x, y)의 좌표 입력>>10 250
//(10,250)는 사각형 선 상에 있습니다.

//점 (x, y)의 좌표 입력>>10 320
//(10,320)는 사각형 밖에 있습니다.

package basic;

import java.util.Scanner;

public class Rect {

	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		System.out.print("점 (x,y)의 좌표 입력>>");
		int x = s.nextInt();
		int y = s.nextInt();
		
        if ((x == 10 || x == 200) && (y >= 10 && y <= 300) 
        		|| (y == 10 || y == 300) && (x >= 10 && x <= 200)) {
			System.out.println("("+ x + "," + y + ")는 사각형 선 상에 있습니다.");
		}
		else if (x>10 && x<200 && y>10 && y<300) {
			System.out.println("("+ x + "," + y + ")는 사각형 안에 있습니다.");
		}
		else {
			System.out.println("("+ x + "," + y + ")는 사각형 밖에 있습니다.");
		}
		
		s.close();
	}

}
