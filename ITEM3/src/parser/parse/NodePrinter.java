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
		
		//listNode�� car �κ��� node�� printNode �Լ��� �־��ְ�
		//cdr �κ��� car�� ������ ������ �κ��̹Ƿ� printList �Լ��� �־��ش�. 
		printNode(listNode.car());
		printList(listNode.cdr());		
	}

	private void printNode(QuoteNode quoteNode) {
		if (quoteNode.nodeInside() == null) 
			return;
		//'�� ��½����ְ� �� ' �ȿ� �ִ� ��带 printNode �Լ��� �־��ش�. 
		sb.append("\'");
		printNode(quoteNode.nodeInside());
      
	}
	
	private void printNode(Node node) {
		if (node == null) return;
		//node�� ListNode�� ���
		else if(node instanceof ListNode) {
			ListNode ln = (ListNode) node;
			//ListNode head �κ��� ' �� ���
			if(ln.car() instanceof QuoteNode) {
				printNode((QuoteNode)ln.car());
			}
			//ListNode head �κ��� ' �ƴ� ��� �� node�� ( )�� �����༭  ��½����ش�. 
			else {
				sb.append("( ");
				printList(ln);
				sb.append(") ");
			}	
		}
		else {
			//�ش� ����� ������ �´� ������ toString�� ��½����ش�. 
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
