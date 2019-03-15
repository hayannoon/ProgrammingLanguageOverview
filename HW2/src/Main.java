import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner sc = new Scanner(System.in); // Scanner ��ü ����
		System.out.print("input : "); // input �޽��� ���
		int input = sc.nextInt(); // ���� �Է�
		System.out.println("output : "); // output �޽��� ���

		callFaery(input); // ferri �����Լ� ȣ��
	}

	static void callFaery(int n) { // ferri�� �ݺ������� ȣ���ϰ������ �ݺ��� ��ſ� ���͸� ����ϱ����� ������� �Լ�
		if (n < 1)
			return; // ��� Ż������
		callFaery(n - 1); // Ż�������� �������ϸ� 1�� ���� �ٽ� ȣ���Ѵ�.
		Faery(n); // ferri �Լ� ȣ��
	}
	
	static void Faery(int n) { // recursion�� ȣ���ϱ� ���� ��� ����� ��������� ���� �Լ�
		System.out.print("F" + n + " : [ "); // ��� ��� (������� F3 : [ a b c] �̷������� ����ϱ�����)
		print(0, 1); // ������� ������ 0/1 ���
		recursion(0, 1, 1, 1, n);
		print(1, 1); // ������� ������ 1/1 ���
		System.out.println("]");
	}

	static void recursion(int a, int b, int c, int d, int N) { // ���� �߿��� �������� �����ϴ� �Լ�
		int e = a + c; // �ٺ�����(���ڳ��� ���ϱ�)
		int f = b + d; // �ٺ�����(�и𳢸� ���ϱ�)
		if (f > N) // �и� ����N���� ũ�� �ȵǱ⶧���� ũ�ٸ� �������´�.
			return;

		recursion(a, b, e, f, N); // ���ȣ��(�ٺ����������� ���� �м��� �ٽ� ���� �м��� �Բ� �۾��Ѵ�.)
		print(e, f); // �ٺ����������� ���� �м� ���
		recursion(e, f, c, d, N); // ���ȣ��(�ٺ������� ���� ���� �м��� �ٽ� ���� �м��� �Բ� �۾��Ѵ�.)
	}

	static void print(int a, int b) // "a/b" ����� ȣ���� ���� �����ϱ⶧���� �������� �Լ�
	{
		System.out.print(a + "/" + b + ", ");
	}
		
}