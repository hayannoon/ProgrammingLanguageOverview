package lexer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Scanner {
	
    public static Iterator<Token> scan(String string) throws FileNotFoundException {
        ScanContext context = new ScanContext(string);
        //ScanContext객체를 생성한다.(입력한 string값이용)
        return new TokenIterator(context); //토큰반복자 객체 생성
    }
    
    
    public static Stream<Token> stream(String string) throws FileNotFoundException {
        Iterator<Token> tokens = scan(string); //토큰 반복자 배열에 String값을 기반으로 Tokenize한다.
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(tokens, Spliterator.ORDERED), false);
    }
    
    // return tokens as an Iterator
//    public static Iterator<Token> scan(File file) throws FileNotFoundException {
//        ScanContext context = new ScanContext(file);
//        return new TokenIterator(context);
//    }
	
    // return tokens as a Stream 
//  public static Stream<Token> stream(File file) throws FileNotFoundException {
//      Iterator<Token> tokens = scan(file);
//      return StreamSupport.stream(
//              Spliterators.spliteratorUnknownSize(tokens, Spliterator.ORDERED), false);
//  }
    

	
}