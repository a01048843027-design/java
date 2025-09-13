// 2차원 평면에서 사각형은 두 점으로 표현할 수 있다. 사용자로부터 사각형을 구성하는 두 점 (x1, y1), (x2, y2)를 입력받고
// 이 사각형이 (10, 10)과 (200, 300)의 사각형 안에 완전히 포함되는지, "포함된다.", 아니면 "포함되지 않는다."를 출력하는 프로그램을 작성하라.

//점 (x1, y1), (x2, y2)의 좌표 입력>>50 30 150 200
//(50, 30), (150, 200) 사각형은 (10, 10), (200, 300) 사각형에 포함된다.

package basic;

import java.util.Scanner;

public class Rect2 {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        System.out.print("(x1, y1), (x2, y2)의 좌표 입력>>");

        int x1 = s.nextInt();
        int y1 = s.nextInt();
        int x2 = s.nextInt();
        int y2 = s.nextInt();
        System.out.print("(" + x1 + ", " + y1 + ") (" + x2 + "," + y2 + ") ");

        // 입력 순서와 관계없이 실제 사각형의 최소, 최대 좌표를 구한다.
        int minX = Math.min(x1, x2);
        int minY = Math.min(y1, y2);
        int maxX = Math.max(x1, x2);
        int maxY = Math.max(y1, y2);

        // 실제 좌표를 기준으로 포함 여부를 판단한다.
        if (minX >= 10 && minY >= 10 && maxX <= 200 && maxY <= 300) {
            System.out.println("사각형은 (10,10) (200,300) 사각형에 포함된다.");
        } else {
            System.out.println("사각형은 (10,10) (200,300) 사각형에 포함되지 않는다.");
        }
        s.close();
    }
}
