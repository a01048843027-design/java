package control;

public class DoWhileExam01 {
	public static void name(String[] args) {
		char c ='a';
		
		do {
			System.out.print(c);
			c = (char)(c + 1);
		} while (c <= 'z');
	}
}
