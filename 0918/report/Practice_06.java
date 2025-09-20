// 6. 양의 정수 10개를 입력받아 일차원 배열에 저장하라. 그리고 일차원 배열에서 각 수를 분석하여
//    자리수의 합이 9인 것을 가려내어 출력하라. 예를 들어 27은 자리수의 합이 9(2+7)인 수이다.

import java.util.Scanner;

public class Practice_06 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int[] intArray = new int[10];

        System.out.print("양의 정수 10개 입력>>");
        // 사용자로부터 정수 10개를 입력받아 배열에 저장합니다.
        for (int i = 0; i < intArray.length; i++) {
            intArray[i] = scanner.nextInt();
        }

        System.out.print("자리수의 합이 9인 것은 ");

        // 배열의 각 원소를 순회합니다.
        for (int i = 0; i < intArray.length; i++) {
            int number = intArray[i]; // 현재 검사할 숫자
            int temp = number;        // 계산을 위해 숫자를 임시 변수에 복사
            int sum = 0;              // 자리수의 합을 저장할 변수

            // while문을 이용해 각 자리수를 더합니다.
            while (temp > 0) {
                sum += temp % 10; // 숫자의 마지막 자리수를 sum에 더함
                temp /= 10;       // 숫자의 마지막 자리수를 제거
            }

            // 자리수의 합이 9와 같으면 원래 숫자를 출력합니다.
            if (sum == 9) {
                System.out.print(number + " ");
            }
        }

        scanner.close();
    }
}
