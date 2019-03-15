import java.math.BigInteger;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner sc = new Scanner(System.in);
		BigInteger num = new BigInteger("1"); // BigInteger �ν��Ͻ� ����
		
		System.out.print("input : "); // ��¹�
		num = sc.nextBigInteger(); // ���� �Է¹޾Ƽ� ����
		
		BigInteger result = doubleFactorial(num); // result�� �Լ� ��°� ����
		
		System.out.println("output : " + result); // ����� ���
	}
		
	static BigInteger doubleFactorial(BigInteger n) { // �Լ�
		BigInteger one = new BigInteger("1"); // ����� BigInteger �ν��Ͻ��� ����
		BigInteger two = new BigInteger("2"); // ����� BigInteger �ν��Ͻ��� ����
		
		if (n.compareTo(one) == 0)
			return one; // n�� 1�̸� 1 ��ȯ(��� Ż������)
		else if (n.compareTo(two) == 0)
			return two; // n�� 2��� 2 ��ȯ(��� Ż������)
		
		return n.multiply(doubleFactorial(n.subtract(two)));
		// 1���ƴϰ� 2�� �ƴ϶�� �׼��ڿ��� 2�� �� ������ �ٽ� ��� ȣ��
	}	
}
