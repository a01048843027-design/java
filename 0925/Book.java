package Intro;

public class Book {
	String title, author;
	public Book(String t) {
		title = t;
		author = "작가 미상";
	}
	public Book(String t, String a) {
		title = t;
		author = a;
	}
	
	public static void main(String[] args) {
		Book littleprince =new Book("어린왕자", "생떼쥐베리");
		Book lovestory = new Book("춘향전");
		System.out.println(littleprince.author + " " + littleprince.title);
		System.out.println(lovestory.author + "  " + lovestory.title);
	}
}
