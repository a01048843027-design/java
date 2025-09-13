// 숫자를 입력받아 3~5는 "따뜻한 봄", 6~8은 "바다가 즐거운 여름", 9~11은 "낙엽이 지는 아름다운 가을", 12,1,2의 경우 "눈 내리는 하얀 겨울"을 출력한다.
// 그 외의 숫자가 입력된 경우 발생하는 오류를 처리하는 프로그램을 작성하라.

// (1) if-else 문을 이용하여 프로그램을 작성하라.
// (2) switch 문을 이용하여 프로그램을 작성하라.

//월을 입력하세요(1~12)>>12
//눈 내리는 하얀 겨울

//(1) if-else 문을 이용하여 프로그램을 작성하라.

package basic;

import java.util.Scanner;

public class Season {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        System.out.print("월을 입력하세요(1~12)>>");
        int month = s.nextInt();

        if (month >= 3 && month <= 5)
            System.out.println("따뜻한 봄");
        else if (month >= 6 && month <= 8)
            System.out.println("바다가 즐거운 여름");
        else if (month >= 9 && month <= 11)
            System.out.println("낙엽이 지는 아름다운 가을");
        else if (month == 12 || month == 1 || month == 2) // 수정된 부분
            System.out.println("눈 내리는 하얀 겨울");
        else
            System.out.println("1~12만 입력하세요.");
        s.close();
    }
}

//(2) switch 문을 이용하여 프로그램을 작성하라.

package basic;

import java.util.Scanner;

public class Season {

	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		System.out.print("월을 입력하세요(1~12)>>");
		int month = s.nextInt();
		
		switch(month) {
			case 3: case 4: case 5: 
				System.out.println("따뜻한 봄");
				break;
			case 6:	case 7: case 8:
				System.out.println("바다가 즐거운 여름");
				break;
			case 9: case 10: case 11:
				System.out.println("낙엽이 지는 아름다운 가을");
				break;
			case 12: case 1: case 2:
				System.out.println("눈 내리는 하얀 겨울");
				break;
			default: 
				System.out.println("1~12만 입력하세요.");
		}
		s.close();
	}

}
