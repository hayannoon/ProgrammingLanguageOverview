package parser.parse;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;

import parser.ast.*;
import parser.ast.FunctionNode.FunctionType;

public class NodePrinter {
	//private final String OUTPUT_FILENAME = "output08.txt";
	private StringBuffer sb = new StringBuffer();
	private Node root;

	public NodePrinter(Node root){
		this.root = root;
	}

	private void printList(ListNode listNode) {
		if (listNode == ListNode.EMPTYLIST) {
			//sb.append("( )");
			return;
		}
		
		//listNode의 car 부분은 node로 printNode 함수에 넣어주고
		//cdr 부분은 car을 제외한 나머지 부분이므로 printList 함수에 넣어준다. 
		printNode(listNode.car());
		printList(listNode.cdr());		
	}

	private void printNode(QuoteNode quoteNode) {
		if (quoteNode.nodeInside() == null) 
			return;
		//'를 출력시켜주고 그 ' 안에 있는 노드를 printNode 함수에 넣어준다. 
		sb.append("\'");
		printNode(quoteNode.nodeInside());
      
	}
	
	private void printNode(Node node) {
		if (node == null) return;
		//node가 ListNode인 경우
		else if(node instanceof ListNode) {
			ListNode ln = (ListNode) node;
			//ListNode head 부분이 ' 인 경우
			if(ln.car() instanceof QuoteNode) {
				printNode((QuoteNode)ln.car());
			}
			//ListNode head 부분이 ' 아닌 경우 그 node를 ( )로 감싸줘서  출력시켜준다. 
			else {
				sb.append("( ");
				printList(ln);
				sb.append(") ");
			}	
		}
		else {
			//해당 노드의 종류에 맞는 형태의 toString을 출력시켜준다. 
			sb.append(node.toString()+" ");
		}
	}
   
	public void prettyPrint(){
		if(root instanceof ListNode) {
			if(!(((ListNode)root).car() instanceof QuoteNode)) {
				if (((ListNode)root).car() instanceof FunctionNode) {
					if (((FunctionNode)((ListNode)root).car()).funcType == FunctionType.LAMBDA) {
						
					} else sb.append("\'");
				} else sb.append("\'");
			} 
		}
		else if(root instanceof IdNode) sb.append("\'");
		printNode(root);
		if(root != null) {
			System.out.print("... ");
			System.out.println(sb.toString());
		}
//		try(FileWriter fw = new FileWriter(OUTPUT_FILENAME);
//			PrintWriter pw = new PrintWriter(fw)){
//			pw.write(sb.toString());
//		}catch (IOException e){
//			e.printStackTrace();
//		}
	}
}
