/*

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Test {
	public static void main(String[] args) throws FileNotFoundException {
		RecursionLinkedList list = new RecursionLinkedList();
		FileReader fr;
		try {
			fr = new FileReader("C:\\Users\\minu2\\OneDrive\\바탕 화면\\hw01.txt");
			BufferedReader br = new BufferedReader(fr);
			String inputString = br.readLine();
			for (int i = 0; i < inputString.length(); i++)
				list.add(inputString.charAt(i));
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(list.toString());
		list.add(3, 'b');
		System.out.println(list.toString());
		list.reverse();
		System.out.println(list.toString());
// 등등 구현한 기능 추가해서 사용
	}
}


*/
