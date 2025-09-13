// 실행 사례와 같이 실수(double)에 대한 사칙 연산을 수행하는 프로그램을 작성하라.
// 연산은 "더하기", "빼기", "곱하기", "나누기"로 하고, 계산식은 "2.3 더하기 3.6"과 같이 빈 칸으로 분리하여 입력한다.
// 0으로 나눌 수 없으니, 0으로 나누기가 입력되면, "0으로 나눌 수 없습니다."를 출력하고, 
// 연산 명령이 "더하기", "빼기", "곱하기", "나누기"가 아닌 경우, "사칙연산이 아닙니다."를 출력하고 종료한다.

//연산 입력>>25 곱하기 2.7
//25.0 곱하기 2.7의 계산 결과는 67.5

// (1) if-else 문을 이용하여 프로그램을 작성하라.

package basic;

import java.util.Scanner;

public class OperationIf {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);

        System.out.print("연산 입력>>");
        double x = s.nextDouble();
        String op = s.next();
        double y = s.nextDouble();
        double result;

        if (op.equals("더하기")) {
            result = x + y;
        } else if (op.equals("빼기")) {
            result = x - y;
        } else if (op.equals("곱하기")) {
            result = x * y;
        } else if (op.equals("나누기")) {
            // --- 이 부분이 추가되었습니다 ---
            if (y == 0) {
                System.out.println("0으로 나눌 수 없습니다.");
                s.close();
                return; // 프로그램 종료
            }
            // ---------------------------
            result = x / y;
        } else {
            System.out.println("사칙연산이 아닙니다.");
            s.close();
            return; // 프로그램 종료
        }

        System.out.println(x + " " + op + " " + y + "의 계산 결과는 " + result);
        s.close();
    }
}

// (2) switch 문을 이용하여 프로그램을 작성하라.

package basic;

import java.util.Scanner;

public class OperationSwitch {

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);

        System.out.print("연산 입력>>");
        double x = s.nextDouble();
        String op = s.next();
        double y = s.nextDouble();
        double result;

        switch (op) {
            case "더하기":
                result = x + y;
                break;
            case "빼기":
                result = x - y;
                break;
            case "곱하기":
                result = x * y;
                break;
            case "나누기":
                // --- 이 부분이 추가되었습니다 ---
                if (y == 0) {
                    System.out.println("0으로 나눌 수 없습니다.");
                    s.close();
                    return; // 프로그램 종료
                }
                // ---------------------------
                result = x / y;
                break;
            default:
                System.out.println("사칙연산이 아닙니다.");
                s.close();
                return; // 프로그램 종료
        }

        System.out.println(x + " " + op + " " + y + "의 계산 결과는 " + result);
        s.close();
    }
}
