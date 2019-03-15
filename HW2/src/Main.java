import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner sc = new Scanner(System.in); // Scanner 객체 생성
		System.out.print("input : "); // input 메시지 출력
		int input = sc.nextInt(); // 차수 입력
		System.out.println("output : "); // output 메시지 출력

		callFaery(input); // ferri 제귀함수 호출
	}

	static void callFaery(int n) { // ferri를 반복적으로 호출하고싶은데 반복문 대신에 제귀를 사용하기위해 덮어놓은 함수
		if (n < 1)
			return; // 재귀 탈출조건
		callFaery(n - 1); // 탈출조건을 충족못하면 1을 빼서 다시 호출한다.
		Faery(n); // ferri 함수 호출
	}
	
	static void Faery(int n) { // recursion을 호출하기 전에 출력 양식을 만들기위해 만든 함수
		System.out.print("F" + n + " : [ "); // 출력 양식 (예를들어 F3 : [ a b c] 이런식으로 출력하기위함)
		print(0, 1); // 모든항의 공통인 0/1 출력
		recursion(0, 1, 1, 1, n);
		print(1, 1); // 모든항의 공통인 1/1 출력
		System.out.println("]");
	}

	static void recursion(int a, int b, int c, int d, int N) { // 가장 중요한 재귀출력을 진행하는 함수
		int e = a + c; // 바보덧셈(분자끼리 더하기)
		int f = b + d; // 바보덧셈(분모끼리 더하기)
		if (f > N) // 분모가 차수N보다 크면 안되기때문에 크다면 빠져나온다.
			return;

		recursion(a, b, e, f, N); // 재귀호출(바보덧셈을통해 만든 분수를 다시 앞의 분수와 함께 작업한다.)
		print(e, f); // 바보덧셈을통해 만든 분수 출력
		recursion(e, f, c, d, N); // 재귀호출(바보덧셈을 통해 만든 분수를 다시 뒤의 분수와 함께 작업한다.)
	}

	static void print(int a, int b) // "a/b" 양식의 호출을 자주 진행하기때문에 만들어놓은 함수
	{
		System.out.print(a + "/" + b + ", ");
	}
		
}