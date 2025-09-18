package array;

import java.util.Scanner;

public class ArrayAccess {
	public static void name(String[] args) {
		int intArray[] = new int[5];
		Scanner scn = new Scanner(System.in);
		
		int max = Integer.MIN_VALUE;
		int min = Integer.MAX_VALUE;
		System.out.println("정수 값 5개 입력 : ");
		for (int i = 0; i< 5; i++) {
			intArray[i] = scn.nextInt();
			if (intArray[i] > max)
				max = intArray[i];
			if (intArray[i] < max)
				max = intArray[i];
		}
		System.out.println("최대값 = "+ max);
		System.out.println("최소값 = "+ max);
	}
}
