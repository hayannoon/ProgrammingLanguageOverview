import java.util.*;
import java.io.*;

public class Scanner {

	public enum TokenType{		//enum선언
		ID(3), INT(2);
		
		private final int finalState;	//변수 final 선언
		
		
		TokenType(int finalState) {		//생성자(초기화)
			this.finalState = finalState;
		}
	}
	 
	
	public static class Token {
		public final TokenType type;		//토큰 타입을 저장할  TokenType변수
		public final String lexme;			//어휘를 저장할 String 변수

		Token(TokenType type, String lexme) {	//Token 클래스 생성자
			this.type = type;
			this.lexme = lexme;
			//변수들을 초기화 해준다.
		}
		
		@Override
		public String toString() {
			return String.format("[%s: %s]", type.toString(), lexme);
		}	//toString 오버라이딩해서 반환을 [타입, 문자열] 로 반환해준다.
	}

	
	private int transM[][];		//private으로 변환 matrix 생성
	private String source;		//source를 저장할 String 변수 생성
	private int pos;
	StringTokenizer st;
	
	public Scanner(String source) {		//source를 매개변수로 받는 생성자
		this.transM = new int[4][128];	//trans Matrix 생성
		this.source = source == null ? "" : source;		//source가 null이면  공백을 저장하고, 아니면 sorce를 저장.
		initTM();
		
		 st = new StringTokenizer(this.source);
	}
	
	private void initTM() {
		/*
		for(int i = 0 ; i < 4 ; i++) {
			for(int j = 0 ; j < 128 ; j++) {
				this.transM[i][j]  = -1;			//초기값 모든값 -1로 초기화
			}
		}
		*/
		for(int i = 0 ; i < 4 ; i++)
		Arrays.fill(this.transM[i],-1);
		
		//참고사항 : 아스키코드상으로
		//'1'~'9' : 49~57
		//'-' : 45
		//'A'~'Z' : 65~90
		//'a'~'z' : 97~122
		
		//첫번쨰 행(0행) 초기화 시작
		for(int i= 48 ; i < 58 ; i++) {//정수로 갈경우
			transM[0][i] = 2;		//0에서 Digit으로갈때는 2로간다.
		}
		transM[0][45] = 1;	//-를 만나면 1로간다.
		
		for(int i = 65 ; i < 91 ; i++) {
			transM[0][i] = 3;		//소문자 초기화
		}
		
		for(int i = 97 ; i < 123 ; i++) {
			transM[0][i] = 3;		//대문자 초기화
		}
		
		
		//--------------0행 초기화 끝 1행 초기화 시작----------------
		
		
		for(int i = 48 ; i < 58 ; i++) {
			transM[1][i] = 2;		//정수 초기화
		}
		
		
		//1에서는 2말고 갈곳이 없으므로 여기서 끝
		
		
		//--------------1행 초기화 끝 2행 초기화 시작----------------
		
		
		for(int i = 48 ; i < 58 ; i++) {
			transM[2][i] = 2;		//정수->정수 갈수있다. but 그이외로는 못간다.그러므로 여기서 끝
		}
		
		//--------------2행 초기화 끝 3행 초기화 시작----------------
		
		
		for(int i = 48 ; i < 58 ; i++) {
			transM[3][i] = 3;		//문자에서 숫자로갈땐 3
		}
		
		for(int i = 65 ; i < 91 ; i++) {
			transM[3][i] = 3;		//소문자 초기화
		}
		
		for(int i = 97 ; i < 123 ; i++) {
			transM[3][i] = 3;		//대문자 초기화
		}
		
		//---------------초기화 종료---------------------------------
		
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
		
		//토큰이 더 있는지 검사
		if(!st.hasMoreTokens()) return null;	//토큰이 없다면 null 반환
		
		
		//그 다음 토큰을 받음
		String temp = st.nextToken();

		Token result = null;	
		
		for(int i = 0; i<temp.length();i++){
			char c = temp.charAt(i);
			stateNew = transM[stateOld][(int)c];	//현재상태와 TransM으로 다음상태 판별
			//문자열의 문자를 하나씩 가져와 현재상태와 TransM를 이용하여 다음 상태를 판별
			if(stateNew==-1) {
				System.out.println("reject!!!");
				return null;
			}//만약 입력된 문자의 상태가 reject 이면 에러메세지 출력 후 return함
			
			stateOld = stateNew;
			//새로 얻은 상태를 현재 상태로 저장
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
		//Token 리스트반환, nextToken()이용..
		List<Token> list = new ArrayList<Token>(100);
		
		Token temp ;	//임시로 토큰을 저장할 참조변수 선언
		
		int count = st.countTokens();	//몇번 반복할건지 정한다.
		
		for(int i = 0 ; i < count ; i++	) {	//반복문을 돌면서
			temp = this.nextToken();	//temp에 토큰 저장
			if(temp==null) continue;	//오류값인지 정상값인지 검사,
			list.add(temp);				//정상값이면 add
		}
		
		return list;
	}
	
	public static void main(String[] args) throws IOException{

		FileReader fr = new FileReader("as03.txt");		//파일을 불러온다.
		BufferedReader br = new BufferedReader(fr);		//리더버퍼 생성
		String source = br.readLine();					//source 초기화
		Scanner s = new Scanner(source);				//Scanner객체 생성
		List<Token> tokens = s.tokenize();				//Tokenize
		System.out.println(tokens);						//리스트 출력
	}
	
	
}
