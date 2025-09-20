// 7. 정수를 10개 저장하는 배열을 만들고, 11~19 범위의 정수를 랜덤하게 생성하여 배열에 저장하라.
//    그리고 배열에 들어 있는 정수들과 평균을 출력하라.

public class Practice_07 {

    public static void main(String[] args) {
        // 10개의 정수를 저장할 배열 생성
        int[] intArray = new int[10];
        
        // 평균 계산을 위해 합계를 저장할 변수. 정확한 계산을 위해 double 타입 사용
        double sum = 0.0;

        // 배열 길이만큼 반복하여 랜덤 정수를 생성하고 저장
        for (int i = 0; i < intArray.length; i++) {
            // 11~19 범위의 랜덤 정수 생성
            // Math.random() : 0.0 이상 1.0 미만의 double 값 반환
            // * 9           : 0.0 이상 9.0 미만
            // (int)         : 0 ~ 8 사이의 정수
            // + 11          : 11 ~ 19 사이의 정수
            int randomNum = (int) (Math.random() * 9) + 11;
            
            intArray[i] = randomNum; // 배열에 랜덤 정수 저장
            sum += intArray[i];      // 생성된 정수를 sum에 더함
        }

        // 배열에 저장된 정수들 출력
        System.out.print("랜덤한 정수들 : ");
        for (int i = 0; i < intArray.length; i++) {
            System.out.print(intArray[i] + " ");
        }
        System.out.println();

        // 평균을 계산하고 출력
        double average = sum / intArray.length;
        System.out.println("평균은 " + average);
    }
}
