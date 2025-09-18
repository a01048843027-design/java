package array;

import java.util.Scanner;

public class ArrayExam01 {
	public static void name(String[] args) {
		int intArray[] = new int[5];
		int sum = 0;
		Scanner scn = new Scanner(System.in);
		
		for (int i = 0; i < intArray.length; i++) {
			intArray[i] = scn.nextInt();
			sum += intArray[i];
		}
/*		for (int i = 0; i < intArray.length; i++) {
			System.out.println("intArray["+i+"] = " +intArray[i]); 
		}*/		
		for(int m:intArray)
			System.out.println(m);
		System.out.println("Sum = " + sum);
		System.out.println("Mean = " + (double)sum/intArray.length);
	}
}
