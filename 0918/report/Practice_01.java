//1. 다음 프로그램에 대해 물음에 답하라

int sum = 0, i = 0;
while (i < 100) {
	sum = sum + i;
	i += 2;
}
System.out.println(sum);

/*3-1-1. 무엇을 계산하는 코드이며 실행 결과 출력되는 내용은?
= while문 안에서 2의 배수를 계속 합산해 가는 프로그램이며 i가 100이 되면 멈추고 2450이 출력된다.*/

//3-1-2. 위의 코드를 mail() 메소드로 만들고 WhileTest 클래스로 완성하라
public class WhileTest {
	public static void main(String[] args) {
		int sum = 0, i = 0;
		while (i < 100) {
			sum = sum + i;
			i += 2;
		}
		System.out.println(sum);
	}
}

//3-1-3. for문을 이용하여 동일하게 실행되는 ForTest 클래스를 작성하라

public class ForTest {
	public static void main(String[] args) {
		int sum = 0;
		for(int i=0; i<100; i+=2) {
			sum = sum + i;
		}
		System.out.print(sum);
	}
}

//3-1-4. do-while 문을 이용하여 동일하게 실행되는 DoWhileTest 클래스를 작성하라

public class DoWhileTest {
	public static void main(String[] args) {
		int sum = 0, i = 0;
		do {
			sum = sum + i;
			i += 2;
		} while (i<100);
		System.out.print(sum);
	}
}
