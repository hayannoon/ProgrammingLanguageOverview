package lexer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

class CharStream {//CharStream 클래스 선언
	private final Reader reader;
	private Character cache;
	
	static CharStream from(File file) throws FileNotFoundException {
		return new CharStream(new FileReader(file));
	}
	
	CharStream(Reader reader) {//생성자
		this.reader = reader;//
		this.cache = null;
	}
	
	Char nextChar() { 
		if ( cache != null ) {
			char ch = cache;
			cache = null;
			//cache가 null이 아니면 새로운 char객체 만들어서 반환한다.
			return Char.of(ch);
		}
		else {	//cache가 null인경우
			try {
				int ch = reader.read();//하나 읽어서 ch에 저장
				if ( ch == -1 ) {	//ch가 -1이면 종료
					return Char.end();
				}
				else {	//-1 아니면 새로운 객체 만들어서 반환
					return Char.of((char)ch);
				}
			}
			catch ( IOException e ) { //예외처리
				throw new ScannerException("" + e);
			}
		}
	}
	
	void pushBack(char ch) {
		cache = ch;
	}
}
