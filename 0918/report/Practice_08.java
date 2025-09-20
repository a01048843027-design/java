// 8. 몇 개의 정수를 입력할 것인지 정수 개수를 입력받고, 그 만큼의 임의의 정수를 입력받아 배열에 저장한다.
//    단, 정수가 1~100 범위의 정수가 아니면 다시 입력받도록 하고, 배열에 저장된 정수들과 평균을 출력하라.

import java.util.Scanner;

public class Practice_08 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // 1. 배열의 크기를 사용자로부터 입력받음
        System.out.print("정수 몇 개>>");
        int count = scanner.nextInt();
        int[] intArray = new int[count];
        double sum = 0.0;

        // 2. 배열의 크기만큼 정수를 입력받음
        for (int i = 0; i < intArray.length; i++) {
            int num = scanner.nextInt();

            // 1~100 범위의 유효한 정수인지 확인
            if (num >= 1 && num <= 100) {
                intArray[i] = num; // 유효하면 배열에 저장
            } else {
                // 유효하지 않은 값이면, 현재 인덱스(i)에 대해 다시 입력받아야 함
                // for 루프가 끝나면 i가 1 증가하므로, 미리 1을 빼서 인덱스를 유지시킴
                i--;
            }
        }

        // 배열에 저장된 값들의 합을 계산
        for (int i = 0; i < intArray.length; i++) {
            sum += intArray[i];
        }

        // 3. 배열에 저장된 정수들 출력
        for (int i = 0; i < intArray.length; i++) {
            System.out.print(intArray[i] + " ");
        }
        System.out.println();

        // 4. 평균을 계산하여 출력
        System.out.println("평균은 " + (sum / intArray.length));

        scanner.close();
    }
}
