import java.util.Scanner;

public class Practice_05 {

    public static void main(String[] args) {
        // Scanner 객체 생성
        Scanner scanner = new Scanner(System.in);

        // 10개의 정수를 저장할 배열 생성
        int[] intArray = new int[10];

        System.out.print("양의 정수 10개 입력>>");

        // 사용자로부터 정수 10개를 입력받아 배열에 저장
        for (int i = 0; i < intArray.length; i++) {
            intArray[i] = scanner.nextInt();
        }

        System.out.print("3의 배수는 ");

        // 배열의 모든 원소를 확인
        for (int i = 0; i < intArray.length; i++) {
            // 원소가 3의 배수인지 확인
            if (intArray[i] % 3 == 0) {
                // 3의 배수이면 출력
                System.out.print(intArray[i] + " ");
            }
        }

        scanner.close();
    }
}
