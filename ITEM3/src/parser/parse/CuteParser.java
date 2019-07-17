package parser.parse;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Stream;

import interpreter.CuteInterpreter;
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
	private static Node END_OF_LIST = new Node() {};

	public CuteParser(String str) {
		tokens = Scanner.scan(str);
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

			// BinaryOpNode�� ���Ͽ� �ۼ�
			// +, -, /, *�� �ش�
		case DIV:
		case EQ:
		case MINUS:
		case GT:
		case PLUS:
		case TIMES:
		case LT:
			BinaryOpNode binNode = new BinaryOpNode();
			binNode.setValue(tType);
			return binNode;
			/*
			���� ä��ÿ�.
			 */

			// FunctionNode�� ���Ͽ� �ۼ�
			// Ű���尡 FunctionNode�� �ش�
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

			FunctionNode funNode = new FunctionNode();
			funNode.setValue(tType);
			return funNode;
			/*
			���� ä��ÿ�.
			 */

			// BooleanNode�� ���Ͽ� �ۼ�
		case FALSE:
			return BooleanNode.FALSE_NODE;
		case TRUE:
			return BooleanNode.TRUE_NODE;

			// case L_PAREN�� ���� case R_PAREN�� ��쿡 ���ؼ� �ۼ�
			// L_PAREN�� ��� parseExprList()�� ȣ���Ͽ� ó��
		case L_PAREN:
			return  parseExprList();

		case R_PAREN:
			return END_OF_LIST;

		case APOSTROPHE:
			QuoteNode quoteNode = new QuoteNode(parseExpr());
			ListNode listNode = ListNode.cons(quoteNode,ListNode.EMPTYLIST);
			return listNode;

		case QUOTE:
			return new QuoteNode(parseExpr());

		default:
			// head�� next�� ����� head�� ��ȯ�ϵ��� �ۼ�
			System.out.println("Parsing Error!");
			return null;
		}

	}

	// List�� value�� �����ϴ� �޼ҵ�
	Node def = null;
	private ListNode parseExprList() {
		Node head = parseExpr();
		
		//define �ϰ� �� ���� id�� def�� �����Ų��.
		if (flag == 1) {
			def = head;
			flag=0;
		}
		
		// define�� ó���ϱ����� �κ�////////////
		if (head instanceof FunctionNode) {
			if (((FunctionNode) head).funcType == FunctionType.DEFINE) {
				flag = 1; // ���� key�� ������ �ؾ��Ұ��
			}
		}
		
		// head�� next ��带 set�Ͻÿ�.
		if (head instanceof IdNode) { //head�� IdNode�ΰ��
				IdNode compare; //���� ����
				Enumeration<Node> e = (CuteInterpreter.hashTable).keys();//�ݺ��� ����
				while (e.hasMoreElements()) { //���Ұ� ���������� �ݺ�
					compare = (IdNode) e.nextElement(); // ���� ��ū ����
					if( def != null) {
						if (((IdNode) def).equals(compare)) { // ��ū�� �������
							CuteInterpreter.hashTable.remove(compare);
								// �������Ұ�� ���� Ű,�� �����ϰ� �������´�.
//							if(CuteInterpreter.hashTable.get(compare) instanceof ListNode)
//								head = CuteInterpreter.hashTable.get(compare);
//								break;
						}
						else {
							if (((IdNode)head).equals(compare)) {
								if(CuteInterpreter.hashTable.get(compare) instanceof ListNode)
									head = CuteInterpreter.hashTable.get(compare);
									break;
							}
						}
					}
					else {
						if (((IdNode)head).equals(compare)) {
							if(CuteInterpreter.hashTable.get(compare) instanceof ListNode) {
								head = CuteInterpreter.hashTable.get(compare);
								break;
							}
						}
					}
					
						//if(CuteInterpreter.hashTable.get(compare) instanceof ListNode)
						//	head = CuteInterpreter.hashTable.get(compare);
						// �����ǰ� �ƴѰ�쿡, ���̺��� value������ head�� �ٲ��ش�.
					}
				}
			
		
		//////////////define�� ó���ϱ����� �κ� ��//////////////

		// head�� next ��带 set�Ͻÿ�.
		if (head == null) // if next token is RPAREN
			return null;
		if(head==END_OF_LIST)
			return ListNode.EMPTYLIST;
		ListNode tail = (ListNode) parseExprList();
		if(tail==null) return null;
		return ListNode.cons(head,tail);
	}
}