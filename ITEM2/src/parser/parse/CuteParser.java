package parser.parse;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Stream;

import parser.ast.*;
import parser.ast.BinaryOpNode.BinType;
import parser.ast.FunctionNode.FunctionType;
import lexer.Scanner;
import lexer.ScannerMain;
import lexer.Token;
import lexer.TokenType;

public class CuteParser {
	private Iterator<Token> tokens;
	private int flag = 0;
	private static Node END_OF_LIST = new Node() {
	};

	public CuteParser(String string) { // 바뀐 생성자부분
		try {
			tokens = Scanner.scan(string);
			// Scanner클래스의 scan부분도 file입력이었는데 string으로 바꾸었다.
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private Token getNextToken() {
		if (!tokens.hasNext())
			return null;
		return tokens.next();
	}

	public Node parseExpr() {
		Token t = getNextToken();
		if (t == null) {
			System.out.println("No more token");
			return null;
		}
		TokenType tType = t.type();
		String tLexeme = t.lexme();

		switch (tType) {
		case ID:
			return new IdNode(tLexeme);
		case INT:
			if (tLexeme == null)
				System.out.println("???");
			return new IntNode(tLexeme);

		// BinaryOpNode에 대하여 작성
		// +, -, /, *가 해당
		case DIV:
		case EQ:
		case MINUS:
		case GT:
		case PLUS:
		case TIMES:
		case LT:
			BinaryOpNode opNode = new BinaryOpNode();// opNode객체생성
			opNode.setValue(tType);// value값 설정
			return opNode;// 완성된 객체 반환

		// FunctionNode에 대하여 작성
		// 키워드가 FunctionNode에 해당
		case ATOM_Q:
		case CAR:
		case CDR:
		case COND:
		case CONS:
		case DEFINE:
		case EQ_Q:
		case LAMBDA:
		case NOT:
		case NULL_Q:

			FunctionNode fNode = new FunctionNode();// FunctionNode객체생성
			fNode.setValue(tType);// value값 설정
			return fNode;// 완성된 객체 반환

		// BooleanNode에 대하여 작성
		case FALSE:
			return BooleanNode.FALSE_NODE;
		case TRUE:
			return BooleanNode.TRUE_NODE;

		// case L_PAREN일 경우와 case R_PAREN일 경우에 대해서 작성
		// L_PAREN일 경우 parseExprList()를 호출하여 처리
		case L_PAREN:
			return parseExprList();
		case R_PAREN:
			return END_OF_LIST;

		case APOSTROPHE:
			QuoteNode quoteNode = new QuoteNode(parseExpr());
			ListNode listNode = ListNode.cons(quoteNode, ListNode.EMPTYLIST);
			return listNode;
		case QUOTE:
			return new QuoteNode(parseExpr());
		default:
			// head의 next를 만들고 head를 반환하도록 작성
			System.out.println("Parsing Error!");
			return null;
		}

	}

	// List의 value를 생성하는 메소드
	private ListNode parseExprList() {
		Node head = parseExpr();
		// define을 처리하기위한 부분////////////
		if (head instanceof FunctionNode) {
			if (((FunctionNode) head).funcType == FunctionType.DEFINE) {
				flag = 1; // 같은 key를 재정의 해야할경우
			}
		}
		// head의 next 노드를 set하시오.
		if (head instanceof IdNode) { //head가 IdNode인경우
			IdNode compare; //비교할 변수
			Enumeration<Node> e = (CuteInterpreter.hashTable).keys();//반복자 설정
			while (e.hasMoreElements()) { //원소가 남을때까지 반복
				compare = (IdNode) e.nextElement(); // 다음 토큰 저장
				if (((IdNode) head).equals(compare)) { // 토큰이 같은경우
					if (flag == 1) { 
						CuteInterpreter.hashTable.remove(compare);
						// 재정의할경우 기존 키,값 삭제하고 빠져나온다.
						flag = 0;
						break;
					}
					head = CuteInterpreter.hashTable.get(compare);
					// 재정의가 아닌경우에, 테이블의 value값으로 head를 바꿔준다.
					break;
				}
			}
		}
		//////////////define을 처리하기위한 부분 끝//////////////
		if (head == null) // if next token is RPAREN
			return null;
		if (head == END_OF_LIST) 
			return ListNode.EMPTYLIST;
		
		ListNode tail = parseExprList();
		if (tail == null) 
			return null;
		return ListNode.cons(head, tail);
	}
}
