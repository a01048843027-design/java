// 9. 4x4 크기의 2차원 정수 배열을 생성하고, 0~255 범위의 정수 16개를 랜덤하게 생성하여
//    저장한 후, 배열을 실행 사례와 같이 출력하라.

public class Practice_09 {

    public static void main(String[] args) {
        // 4x4 크기의 2차원 정수 배열을 생성
        int[][] intArray = new int[4][4];

        // 중첩 for문을 사용하여 배열의 모든 요소를 순회
        for (int i = 0; i < intArray.length; i++) { // intArray.length는 행의 개수 (4)
            for (int j = 0; j < intArray[i].length; j++) { // intArray[i].length는 열의 개수 (4)
                
                // 0부터 255까지의 범위에 있는 랜덤 정수를 생성하여 배열에 저장
                // Math.random() : 0.0 이상 1.0 미만
                // * 256         : 0.0 이상 256.0 미만
                // (int)         : 0 ~ 255 사이의 정수
                intArray[i][j] = (int) (Math.random() * 256);
            }
        }
        
        System.out.println("4x4 배열에 랜덤한 값을 저장한 후 출력합니다.");

        // 중첩 for문을 사용하여 배열의 모든 요소를 출력
        for (int i = 0; i < intArray.length; i++) {
            for (int j = 0; j < intArray[i].length; j++) {
                // 각 요소를 출력하고, 탭(\t)을 이용해 일정한 간격으로 띄움
                System.out.print(intArray[i][j] + "\t");
            }
            // 한 행의 출력이 끝나면 다음 줄로 넘어감
            System.out.println();
        }
    }
}
