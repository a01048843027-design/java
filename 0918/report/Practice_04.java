// 4. 다음 2차원 배열 n을 실행 사례와 같이 출력하는 프로그램을 작성하라.
// int n[][] = {{1,2,3}, {1,2}, {1}, {1,2,3,4}};

public class Practice_04 {

    public static void main(String[] args) {
        // 문제에 주어진 2차원 배열 초기화
        int n[][] = {{1, 2, 3}, {1, 2}, {1}, {1, 2, 3, 4}};

        // n.length는 배열의 행(row) 개수만큼 반복합니다. (총 4번)
        for (int i = 0; i < n.length; i++) {
            // n[i].length는 현재 행의 열(column) 개수만큼 반복합니다.
            for (int j = 0; j < n[i].length; j++) {
                // 배열의 원소를 출력하고 한 칸 띄웁니다.
                System.out.print(n[i][j] + " ");
            }
            // 한 행의 출력이 끝나면 줄을 바꿉니다.
            System.out.println();
        }
    }
}
