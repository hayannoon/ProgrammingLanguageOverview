package lexer;

import lexer.Char.CharacterType;

class Char {
	private final char value;		//char형변수 value
	private final CharacterType type;	//CharacterType형 변수 type

	enum CharacterType { //CharacterType 열거형 정의
		LETTER, DIGIT, SPECIAL_CHAR, WS, END_OF_STREAM,
	}
	
	static Char of(char ch) {	//char형 매개변수받아서, char 객체 반환 
		return new Char(ch, getType(ch));
	}
	
	static Char end() {		//끝낼떄 호출
		return new Char(Character.MIN_VALUE, CharacterType.END_OF_STREAM);
	}
	
	private Char(char ch, CharacterType type) {
		this.value = ch;
		this.type = type;
	}//생성자 
	
	char value() {
		return this.value;
	}	//value반환(char형태)
	
	CharacterType type() {
		return this.type;
	}	//type반환(characterType형태)
	
	private static CharacterType getType(char ch) {
		int code = (int)ch;
		if ((code >= (int) 'A' && code <= (int) 'Z') || (code >= (int) 'a' && code <= (int) 'z')) {
			return CharacterType.LETTER;
		}//아스키코드상으로 A~Z , a~z 인경우 LETTER 타입을 반환
		
		if ( Character.isDigit(ch) ) {
			return CharacterType.DIGIT;
		}//char타입이 정수면 digit 반환
		
		switch ( ch ) {
			case '-': case '+': case '*': case '/':
			case '(': case ')':
			case '<': case '=': case '>':
			case '#': case '\'':
				return CharacterType.SPECIAL_CHAR;
			case '?':
				return CharacterType.LETTER;
		}	//그 이외에 이러한 특수문자인경우 Special_Char반환
		
		if ( Character.isWhitespace(ch) ) {
			return CharacterType.WS;
		}//공백인경우 WS 반환
		
		throw new IllegalArgumentException("input=" + ch);
	}	//모두다 아닌경우 예외처리
}
