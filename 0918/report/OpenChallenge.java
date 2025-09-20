/*카드 번호 맞추기 게임(up&down 게임)
숨겨진 카드의 수를 맞추는 게임을 만들어보자. 
0에서 99까지의 임의의 수를 가진 카드를 한 장 숨기고 이 카드의 수를 맞추는 게임이다. 아래의 화면과 같이 카드 속의 수가 77인 경우를 보자. 
수를 맞추는 사람이 55라고 입력하면 "더 높게". 다시 70을 입력하면 "더 높게"라는 식으로 범위를 좁혀가면서 수를 맞춘다. 
게임을 반복하기 위해 y/n을 묻고, n인 경우 종료된다.*/

import java.util.Random;
import java.util.Scanner;

public class UpAndDownFor{
    public static void main(String args[]) {
        Random r = new Random();
        Scanner scanner = new Scanner(System.in);

        while(true) {
            int pc = r.nextInt(100); //0-99 사이의 난수 생성
            System.out.println("수를 결정하였습니다. 맞추어보세요");

            int count = 1, max = 99, min = 0;
            String option = "";

            while(true) {
                System.out.println(min+"-"+max);
                System.out.print(count+">>");
                int user = scanner.nextInt();

                if(user>pc){
                    System.out.println("더 낮게");
                    max = user;
                }
                else if(user<pc) {
                    System.out.println("더 높게");
                    min = user;
                }
                else {
                    System.out.println("맞았습니다.");
                    break;
                }
                count++;
            }
            System.out.print("다시하시겠습니까(y/n)>>");
            option = scanner.next();

            if(option.equals("y")) continue;
            else break;
        }

        System.out.print("프로그램을 종료합니다.");
        scanner.close();
        return;
    }
}

/*출력
수를 결정하였습니다. 맞추어보세요
0-99
1>>48
더 낮게
0-48
2>>20
더 높게
20-48
3>>35
더 낮게
20-35
4>>29
더 낮게
20-29
5>>25
더 높게
25-29
6>>28
맞았습니다.
다시하시겠습니까(y/n)>>y
수를 결정하였습니다. 맞추어보세요
0-99
1>>38
더 높게
38-99
2>>60
더 낮게
38-60
3>>48
더 낮게
38-48
4>>42
더 높게
42-48
5>>44
맞았습니다.
다시하시겠습니까(y/n)>>n
프로그램을 종료합니다.*/
