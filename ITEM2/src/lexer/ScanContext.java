package lexer;

import java.io.File;
import java.io.FileNotFoundException;


class ScanContext {
	private final CharStream input;
	private StringBuilder builder;
	
//	ScanContext(File file) throws FileNotFoundException {
//		this.input = CharStream.from(file);
//		this.builder = new StringBuilder();
//	}
	
	ScanContext(String string) throws FileNotFoundException {
		this.input = CharStream.from(string);
		//CharStream.from을통해 CharStream을 불러온다.
		this.builder = new StringBuilder();
	}
	
	CharStream getCharStream() {
		return input;
	}
	
	String getLexime() {
		String str = builder.toString();
		builder.setLength(0);
		return str;
	}
	
	void append(char ch) {
		builder.append(ch);
	}
}
