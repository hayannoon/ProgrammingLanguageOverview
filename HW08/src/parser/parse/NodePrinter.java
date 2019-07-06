package parser.parse;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;

import parser.ast.*;

public class NodePrinter {
	private final String OUTPUT_FILENAME = "output08.txt";
	private StringBuffer sb = new StringBuffer();
	private Node root;

	public NodePrinter(Node root){
		this.root = root;
	}

	private void printList(ListNode listNode) {
		
		if(listNode == ListNode.EMPTYLIST) {
			return; //리스트의 끝이라면 함수 종료
		}
		//리스트에 원소가 있는경우
		printNode(listNode.car()); //첫번째원소 출력
		printList(listNode.cdr()); //나버지 뒤 원소 재귀적으로 출력
	}
	
	private void printNode(QuoteNode quoteNode) {
		if(quoteNode.nodeInside()==null) //쿼트 내부가 비어있으면 종료
			return;
		sb.append("\'"); // '를 붙여준다.
		printNode(quoteNode.nodeInside());
		//printNode 호출
	}
	

	private void printNode(Node node) {
		if (node == null) 
			return; //예외처리
      
		if(node instanceof ListNode)  { //리스트 노드인 경우
			if(((ListNode)node).car() instanceof QuoteNode) {
				printNode(((ListNode)node).car() );
			} //QuoteNode가 ListNode형태로 반환되기때문에, 예외처리를 해주어야한다.
			//ListNode 타입으로 들어왔어도, 처음 타입이 QuoteNode라면 괄호출력없이
			//QuoteNode형태로 출력
			else { 
			sb.append("( "); // 여는 괄호 출력
			printList((ListNode)node); //괄호 내부값 출력
			sb.append(") "); // 닫는괄호 출력 
			}
		}
		else if(node instanceof QuoteNode) {	//쿼트 노드인 경우
			printNode((QuoteNode)node); //쿼트노드 프린트함수 호출
			
		}else { //나머지 Value 노드인 경우
			//sb.append("[" + node + "]"); //괄호로 묶어서 값을 출력
			sb.append(node+" ");
		}
	}
   
	public void prettyPrint(){
		printNode(root);

		try(FileWriter fw = new FileWriter(OUTPUT_FILENAME);
			PrintWriter pw = new PrintWriter(fw)){
			pw.write(sb.toString());
		}catch (IOException e){
			e.printStackTrace();
		}
	}
}
