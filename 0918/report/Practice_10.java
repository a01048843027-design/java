// 10. (문제의 설명이 불분명하여 실행 사례를 기반으로 재구성)
// 4x4 크기의 2차원 정수 배열을 0~255 범위의 랜덤 정수로 초기화하고 출력합니다.
// 그 다음, 사용자로부터 정수 하나를 입력받습니다.
// 배열의 각 원소가 사용자가 입력한 값보다 크면 255로, 그렇지 않으면 0으로 변경한 후
// 수정된 배열을 다시 출력하는 프로그램을 작성하라.

import java.util.Scanner;

public class Practice_10 {

    public static void main(String[] args) {
        int[][] intArray = new int[4][4];

        for (int i = 0; i < intArray.length; i++) {
            for (int j = 0; j < intArray[i].length; j++) {
                intArray[i][j] = (int) (Math.random() * 256);
            }
        }

        System.out.println("4x4 배열에 랜덤한 값을 저장한 후 출력합니다.");
        for (int i = 0; i < intArray.length; i++) {
            for (int j = 0; j < intArray[i].length; j++) {
                System.out.print(intArray[i][j] + "\t");
            }
            System.out.println();
        }
        
        System.out.println(); 

        Scanner scanner = new Scanner(System.in);
        System.out.print("임의의 값>>");
        int threshold = scanner.nextInt();

        for (int i = 0; i < intArray.length; i++) {
            for (int j = 0; j < intArray[i].length; j++) {
                if (intArray[i][j] > threshold) {
                    intArray[i][j] = 255;
                } else {
                    intArray[i][j] = 0;
                }
            }
        }

        System.out.println(); 


        for (int i = 0; i < intArray.length; i++) {
            for (int j = 0; j < intArray[i].length; j++) {
                System.out.print(intArray[i][j] + "\t");
            }
            System.out.println();
        }

        scanner.close();
    }
}
