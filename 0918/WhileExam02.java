package control;

import java.util.Scanner;

public class WhileExam02 {
    public static void main(String[] args) {
        int count = 0;
        int sum = 0;
        Scanner scn = new Scanner(System.in);

        System.out.println("-1을 입력하기 전까지의 합과 평균을 구합니다.");
        System.out.println("정수 입력 : ");

        int number = scn.nextInt();

        while (number != -1) {
            sum += number;
            count++;

            System.out.println("정수 입력 : ");
            number = scn.nextInt();
        }

        if (count > 0) {
            double avg = (double) sum / count;
            System.out.println("입력된 수의 합: " + sum);
            System.out.println("입력된 수의 평균: " + avg);
        } else {
            System.out.println("입력된 정수가 없습니다.");
        }

        scn.close();
    }
}