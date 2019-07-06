package lexer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

class CharStream {//CharStream
	private final Reader reader;
	private Character cache;
	
//	static CharStream from(File file) throws FileNotFoundException {
//		return new CharStream(new FileReader(file));
//	}
	
	static CharStream from(String string) throws FileNotFoundException {
		return new CharStream(new StringReader(string)); 
		//StringReader 객체를 Reader로 저장
	}
	
	CharStream(Reader reader) {//생성자
		this.reader = reader;//
		this.cache = null;
	}
	
	Char nextChar() { 
		if ( cache != null ) {
			char ch = cache;
			cache = null;
			//cache媛� null�씠 �븘�땲硫� �깉濡쒖슫 char媛앹껜 留뚮뱾�뼱�꽌 諛섑솚�븳�떎.
			return Char.of(ch);
		}
		else {	//cache媛� null�씤寃쎌슦
			try {
				int ch = reader.read();//�븯�굹 �씫�뼱�꽌 ch�뿉 ���옣
				if ( ch == -1 ) {	//ch媛� -1�씠硫� 醫낅즺
					return Char.end();
				}
				else {	//-1 �븘�땲硫� �깉濡쒖슫 媛앹껜 留뚮뱾�뼱�꽌 諛섑솚
					return Char.of((char)ch);
				}
			}
			catch ( IOException e ) { //�삁�쇅泥섎━
				throw new ScannerException("" + e);
			}
		}
	}
	
	void pushBack(char ch) {
		cache = ch;
	}
}
