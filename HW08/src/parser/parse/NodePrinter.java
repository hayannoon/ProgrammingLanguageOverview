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
			return; //����Ʈ�� ���̶�� �Լ� ����
		}
		//����Ʈ�� ���Ұ� �ִ°��
		printNode(listNode.car()); //ù��°���� ���
		printList(listNode.cdr()); //������ �� ���� ��������� ���
	}
	
	private void printNode(QuoteNode quoteNode) {
		if(quoteNode.nodeInside()==null) //��Ʈ ���ΰ� ��������� ����
			return;
		sb.append("\'"); // '�� �ٿ��ش�.
		printNode(quoteNode.nodeInside());
		//printNode ȣ��
	}
	

	private void printNode(Node node) {
		if (node == null) 
			return; //����ó��
      
		if(node instanceof ListNode)  { //����Ʈ ����� ���
			if(((ListNode)node).car() instanceof QuoteNode) {
				printNode(((ListNode)node).car() );
			} //QuoteNode�� ListNode���·� ��ȯ�Ǳ⶧����, ����ó���� ���־���Ѵ�.
			//ListNode Ÿ������ ���Ծ, ó�� Ÿ���� QuoteNode��� ��ȣ��¾���
			//QuoteNode���·� ���
			else { 
			sb.append("( "); // ���� ��ȣ ���
			printList((ListNode)node); //��ȣ ���ΰ� ���
			sb.append(") "); // �ݴ°�ȣ ��� 
			}
		}
		else if(node instanceof QuoteNode) {	//��Ʈ ����� ���
			printNode((QuoteNode)node); //��Ʈ��� ����Ʈ�Լ� ȣ��
			
		}else { //������ Value ����� ���
			//sb.append("[" + node + "]"); //��ȣ�� ��� ���� ���
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
