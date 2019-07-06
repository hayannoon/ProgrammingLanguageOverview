import java.util.*;
import java.io.*;

public class Scanner {

	public enum TokenType{		//enum����
		ID(3), INT(2);
		
		private final int finalState;	//���� final ����
		
		
		TokenType(int finalState) {		//������(�ʱ�ȭ)
			this.finalState = finalState;
		}
	}
	 
	
	public static class Token {
		public final TokenType type;		//��ū Ÿ���� ������  TokenType����
		public final String lexme;			//���ָ� ������ String ����

		Token(TokenType type, String lexme) {	//Token Ŭ���� ������
			this.type = type;
			this.lexme = lexme;
			//�������� �ʱ�ȭ ���ش�.
		}
		
		@Override
		public String toString() {
			return String.format("[%s: %s]", type.toString(), lexme);
		}	//toString �������̵��ؼ� ��ȯ�� [Ÿ��, ���ڿ�] �� ��ȯ���ش�.
	}

	
	private int transM[][];		//private���� ��ȯ matrix ����
	private String source;		//source�� ������ String ���� ����
	private int pos;
	StringTokenizer st;
	
	public Scanner(String source) {		//source�� �Ű������� �޴� ������
		this.transM = new int[4][128];	//trans Matrix ����
		this.source = source == null ? "" : source;		//source�� null�̸�  ������ �����ϰ�, �ƴϸ� sorce�� ����.
		initTM();
		
		 st = new StringTokenizer(this.source);
	}
	
	private void initTM() {
		/*
		for(int i = 0 ; i < 4 ; i++) {
			for(int j = 0 ; j < 128 ; j++) {
				this.transM[i][j]  = -1;			//�ʱⰪ ��簪 -1�� �ʱ�ȭ
			}
		}
		*/
		for(int i = 0 ; i < 4 ; i++)
		Arrays.fill(this.transM[i],-1);
		
		//������� : �ƽ�Ű�ڵ������
		//'1'~'9' : 49~57
		//'-' : 45
		//'A'~'Z' : 65~90
		//'a'~'z' : 97~122
		
		//ù���� ��(0��) �ʱ�ȭ ����
		for(int i= 48 ; i < 58 ; i++) {//������ �����
			transM[0][i] = 2;		//0���� Digit���ΰ����� 2�ΰ���.
		}
		transM[0][45] = 1;	//-�� ������ 1�ΰ���.
		
		for(int i = 65 ; i < 91 ; i++) {
			transM[0][i] = 3;		//�ҹ��� �ʱ�ȭ
		}
		
		for(int i = 97 ; i < 123 ; i++) {
			transM[0][i] = 3;		//�빮�� �ʱ�ȭ
		}
		
		
		//--------------0�� �ʱ�ȭ �� 1�� �ʱ�ȭ ����----------------
		
		
		for(int i = 48 ; i < 58 ; i++) {
			transM[1][i] = 2;		//���� �ʱ�ȭ
		}
		
		
		//1������ 2���� ������ �����Ƿ� ���⼭ ��
		
		
		//--------------1�� �ʱ�ȭ �� 2�� �ʱ�ȭ ����----------------
		
		
		for(int i = 48 ; i < 58 ; i++) {
			transM[2][i] = 2;		//����->���� �����ִ�. but ���ܷ̿δ� ������.�׷��Ƿ� ���⼭ ��
		}
		
		//--------------2�� �ʱ�ȭ �� 3�� �ʱ�ȭ ����----------------
		
		
		for(int i = 48 ; i < 58 ; i++) {
			transM[3][i] = 3;		//���ڿ��� ���ڷΰ��� 3
		}
		
		for(int i = 65 ; i < 91 ; i++) {
			transM[3][i] = 3;		//�ҹ��� �ʱ�ȭ
		}
		
		for(int i = 97 ; i < 123 ; i++) {
			transM[3][i] = 3;		//�빮�� �ʱ�ȭ
		}
		
		//---------------�ʱ�ȭ ����---------------------------------
		
	// transM[4][128] = { {...}, {...}, {...}, {...} };
	// values of entries:   -1, 0, 1, 2, 3 : next state
	// TransM[0]['0'] = 2, ..., TransM[0]['9'] = 2,
	// TransM[0]['-'] = 1,
	// TransM[0]['a'] = 3, ..., TransM[0]['z'] = 3,
	// TransM[1]['0'] = 2, ..., TransM[1]['9'] = 2,
	// TransM[2]['0'] = 2, ..., TransM[1]['9'] = 2,
	// TransM[3]['A'] = 3, ..., TransM[3]['Z'] = 3,
	// TransM[3]['a'] = 3, ..., TransM[3]['z'] = 3,
	// TransM[3]['0'] = 3, ..., TransM[3]['9'] = 3,
	// ...
	//     The values of the other entries are all -1.
	
	}
	
	
	private Token nextToken(){	
		
		int stateOld = 0, stateNew;
		//StringTokenizer st = new StringTokenizer(this.source);
		
		//��ū�� �� �ִ��� �˻�
		if(!st.hasMoreTokens()) return null;	//��ū�� ���ٸ� null ��ȯ
		
		
		//�� ���� ��ū�� ����
		String temp = st.nextToken();

		Token result = null;	
		
		for(int i = 0; i<temp.length();i++){
			char c = temp.charAt(i);
			stateNew = transM[stateOld][(int)c];	//������¿� TransM���� �������� �Ǻ�
			//���ڿ��� ���ڸ� �ϳ��� ������ ������¿� TransM�� �̿��Ͽ� ���� ���¸� �Ǻ�
			if(stateNew==-1) {
				System.out.println("reject!!!");
				return null;
			}//���� �Էµ� ������ ���°� reject �̸� �����޼��� ��� �� return��
			
			stateOld = stateNew;
			//���� ���� ���¸� ���� ���·� ����
		}
		
		for (TokenType t : TokenType.values()){
			if(t.finalState == stateOld){
				result = new Token(t, temp);
				break;
			}
		}			
		return result;	
}
	
	public List<Token> tokenize() {
		//Token ����Ʈ��ȯ, nextToken()�̿�..
		List<Token> list = new ArrayList<Token>(100);
		
		Token temp ;	//�ӽ÷� ��ū�� ������ �������� ����
		
		int count = st.countTokens();	//��� �ݺ��Ұ��� ���Ѵ�.
		
		for(int i = 0 ; i < count ; i++	) {	//�ݺ����� ���鼭
			temp = this.nextToken();	//temp�� ��ū ����
			if(temp==null) continue;	//���������� �������� �˻�,
			list.add(temp);				//�����̸� add
		}
		
		return list;
	}
	
	public static void main(String[] args) throws IOException{

		FileReader fr = new FileReader("as03.txt");		//������ �ҷ��´�.
		BufferedReader br = new BufferedReader(fr);		//�������� ����
		String source = br.readLine();					//source �ʱ�ȭ
		Scanner s = new Scanner(source);				//Scanner��ü ����
		List<Token> tokens = s.tokenize();				//Tokenize
		System.out.println(tokens);						//����Ʈ ���
	}
	
	
}
