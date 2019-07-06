package lexer;

import java.util.HashMap;
import java.util.Map;


public class Token {
	private final TokenType type;
	private final String lexme;
	
	static Token ofName(String lexme) {
		TokenType type = KEYWORDS.get(lexme);
		if ( type != null ) {	//타입이 있으면 그 타입대로 토큰만들기
			return new Token(type, lexme);
		}
		else if ( lexme.endsWith("?") ) { // 타입이 null이면서 ?로 끝날경우
			if ( lexme.substring(0, lexme.length()-1).contains("?") ) {//마지막이 아닌곳에 ?가 또 포함되어있으면 예외처리
				throw new ScannerException("invalid ID=" + lexme);
			}
			
			return new Token(TokenType.QUESTION, lexme);	//마지막이 물음표라면 Question타입으로 토큰 생성및 반환
		}
		else if ( lexme.contains("?") ) {	//마지막이 아니면서 ?가 포함되어있는경우
			throw new ScannerException("invalid ID=" + lexme);	//예외처리
		}
		else {
			return new Token(TokenType.ID, lexme);	//?가 안끼어있는경우에는 ID
		}
	}
	
	Token(TokenType type, String lexme) {
		this.type = type;
		this.lexme = lexme;
	}
	
	public TokenType type() {
		return this.type;
	}
	
	public String lexme() {
		return this.lexme;
	}
	
	@Override
	public String toString() {
		return String.format("%s(%s)", type, lexme);
	}
	
	private static final Map<String,TokenType> KEYWORDS = new HashMap<>();
	static {
		KEYWORDS.put("define", TokenType.DEFINE);
		KEYWORDS.put("lambda", TokenType.LAMBDA);
		KEYWORDS.put("cond", TokenType.COND);
		KEYWORDS.put("quote", TokenType.QUOTE);
		KEYWORDS.put("not", TokenType.NOT);
		KEYWORDS.put("cdr", TokenType.CDR);
		KEYWORDS.put("car", TokenType.CAR);
		KEYWORDS.put("cons", TokenType.CONS);
		KEYWORDS.put("eq?", TokenType.EQ_Q);
		KEYWORDS.put("null?", TokenType.NULL_Q);
		KEYWORDS.put("atom?", TokenType.ATOM_Q);
	}
}
