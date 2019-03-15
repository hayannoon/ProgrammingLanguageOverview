import java.math.BigInteger;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner sc = new Scanner(System.in);
		BigInteger num = new BigInteger("1"); // BigInteger 인스턴스 생성
		
		System.out.print("input : "); // 출력문
		num = sc.nextBigInteger(); // 숫자 입력받아서 저장
		
		BigInteger result = doubleFactorial(num); // result에 함수 출력값 저장
		
		System.out.println("output : " + result); // 결과값 출력
	}
		
	static BigInteger doubleFactorial(BigInteger n) { // 함수
		BigInteger one = new BigInteger("1"); // 사용할 BigInteger 인스턴스값 생성
		BigInteger two = new BigInteger("2"); // 사용할 BigInteger 인스턴스값 생성
		
		if (n.compareTo(one) == 0)
			return one; // n이 1이면 1 반환(재귀 탈출조건)
		else if (n.compareTo(two) == 0)
			return two; // n이 2라면 2 반환(재귀 탈출조건)
		
		return n.multiply(doubleFactorial(n.subtract(two)));
		// 1도아니고 2도 아니라면 그숫자에서 2를 뺀 값으로 다시 재귀 호출
	}	
}
