/*package control;

import java.util.Scanner;

public class ForExam01 {
	public static void name(String[] args) {
		Scnner nscn = new Scanner(System.in);
		int i=1, sum = 0; number;
		System.out.println("정수 입력 : ");
		number = scn.nextInt();
		for (i=1; i<number ; i++) {
			sum = sum + i; //sum += i;
			if(i == 10)
				break;
			i++;
		}
		System.out.println(sum);
	}
}
*/
package control;

import java.util.Scanner;

public class ForExam01 {
	public static void main(String[] args) {
		Scanner scn = new Scanner(System.in);
		int i, sum = 0, number; 
		
		System.out.print("정수 입력 : "); 
		number = scn.nextInt();
		
		for (i = 1; i <= number; i++) {
			sum += i;
		}
		
		System.out.println("1부터 " + number + "까지의 합: " + sum);
		
		scn.close();
	}
}