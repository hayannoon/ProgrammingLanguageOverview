package interpreter;

import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Scanner;

import parser.ast.*;
import parser.ast.FunctionNode.FunctionType;
import parser.parse.*;

class _Node {
	Node node;
	Node key;

	_Node(Node node, Node key){
		this.node = node;
		this.key = key;
	}
}

public class CuteInterpreter {
	public static Hashtable<Node, Node> hashTable = new Hashtable(50); //���̺� ����(define�� ����)
	_Node array[] = new _Node[2]; //lambda�� ����Ҷ� IdNode�� ������ ���� �������ִ� ��� Ŭ������ array �迭 (���ٸ� �� ���忡�� �ι� ����Ҷ� ����)
	Node array1 = null; //�迭���� ��� �������� ����(���߿� ��� ������� �ϱ� ���� ���)
	Node array2 = null; //�迭���� ��� �������� ����(���߿� ��� ������� �ϱ� ���� ���)
	int number = 1; //cond���� ����� ������ ���ǹ��� �ΰ� �̻����� �ƴ����� �˷��� �����̴�. 
	
	public static void main(String args[]) {		
		//ClassLoader cloader = ParserMain.class.getClassLoader();
		//File file = new File(cloader.getResource("interpreter/as08.txt").getFile());
		while(true) {
			Scanner sc = new Scanner(System.in);
			System.out.print("> ");
			String str = sc.nextLine();
			if(str.contentEquals("exit")) {//���α׷� ��������(���Ƿ� exit���� �������.)
				System.out.println("���α׷��� �����մϴ�.");
				break; 
			}
			CuteParser cuteParser = new CuteParser(str);
			CuteInterpreter interpreter = new CuteInterpreter();
			Node parseTree = cuteParser.parseExpr();
			interpreter.runFirst();
			Node resultNode = interpreter.runExpr(parseTree);
			NodePrinter nodePrinter = new NodePrinter(resultNode);
			nodePrinter.prettyPrint();
		}		
	}

	private void errorLog(String err) {
		System.out.println(err);
	}
	
	public void runFirst() { //array �迭 �ʱ�ȭ�����ش�. 
		for(int i=0 ; i<array.length ; i++) {
			array[i] = new _Node(null,null);
		}
	}
	
	public Node runExpr(Node rootExpr) {
		if(rootExpr == null) return null;
		if(rootExpr instanceof IdNode) {
			
			/**** lambda�� ���ǵǾ��ִ� IdNode���� ã���ش�. *****/
			//array �迭�� ��尡 null�� �ƴ� ��
			for(int i=0 ; i<array.length ; i++) {
				if(array[i].node != null) {
					//array �迭�� ���� ��ġ�ϴ� IdNode�� �� 
					if((array[i].node).equals(rootExpr)) {
						if(array1 == null) { 
							array1 = array[i].node;
						}
						else {
							array2 = array[i].node;
						}
						rootExpr = array[i].key; //��ġ�ϴ� ����� ������ ��������ش�. 
						return rootExpr;
					}
				}
			}
			/*******/
		
			/**** define���� ���ǵǾ��ִ� IdNode���� ã���ش�. *****/
			IdNode compare;
			Enumeration<Node> e = (CuteInterpreter.hashTable).keys();
			while(e.hasMoreElements()) {
				compare = (IdNode)e.nextElement();
				if ((rootExpr).equals(compare)) { // ��ū�� �������
					rootExpr = hashTable.get(compare);
				}
			}
			/*******/
			
			return rootExpr;
		}
		else if(rootExpr instanceof IntNode) return rootExpr;
		else if(rootExpr instanceof BooleanNode) return rootExpr;
		else if(rootExpr instanceof ListNode) return runList((ListNode)rootExpr);
		else errorLog("run Expr error"); 
		return null;
	}

	private Node runList(ListNode list) {
		if(list.equals(ListNode.EMPTYLIST)) return list; //����Ʈ�� ��������� �״�� ��ȯ
		if(list.car() instanceof FunctionNode) { 
			//list.car() �� lambda �϶� ���� ���Ұ� ���ٴ� ���̹Ƿ� �״�� return �����ش�. 
			if (((FunctionNode)list.car()).funcType == FunctionType.LAMBDA) {
				return list; //function Type�� Lambda��� list�� ��ȯ
			}
			else
				return runFunction((FunctionNode) list.car(), (ListNode) stripList(list.cdr()));
		}		//Lambda�� �ƴϸ� runFunction ȣ��
		if(list.car() instanceof BinaryOpNode) return runBinary(list); //binaryOpNode�� runBinary ����
		if(list.car() instanceof ListNode) { //List����ΰ��
			if(!(((ListNode)list.car()).car() instanceof QuoteNode)) { //��Ʈ�� �ƴϰ�
				if((((ListNode)list.car()).car() instanceof FunctionNode)) { //functionNode���
					if(((FunctionNode)(((ListNode)list.car()).car())).funcType == FunctionType.LAMBDA) { //�����ΰ��
						Node head = ((ListNode)(((ListNode)list.car()).cdr().car())).car(); //head��(x��) ã�Ƽ� ����
						Node lamb2 = runExpr(list.cdr().car()); //���ε����ִ� ���� lamb2�� ����
						if (head instanceof IdNode) { //head�� IdNode�ΰ��
							for(int i=0 ; i<array.length ; i++) { 
								if(array[i].node == null) { //����ִ� array �κп� ��������ش�. 
									array[i].node = head;
									array[i].key = lamb2;
									break;
								}
							}
						}
						return runExpr((ListNode)(((ListNode)list.car()).cdr().cdr().car()));
					}	
				}
				else if((((ListNode)list.car()).car() instanceof BinaryOpNode)) return runBinary((ListNode)list.car());
			}
		}
		return list;
	}
	
	private Node runFunction(FunctionNode operator, ListNode operand) {
		switch(operator.funcType) {
		case CAR: //runQuote �Լ��� car�� ������ ����Ʈ�� �־� �� ����Ʈ���� car �κ��� ã�Ƴ��� ���Ͻ�Ų��. 
			//list�� �ƴ� �����Ϳ� ���ؼ� error�� ����.
			if(!(operand instanceof ListNode)) {
				errorLog("error");
				return null;
			}
			if(((ListNode)operand).car() instanceof QuoteNode) 
				return ((ListNode)runQuote(operand)).car();
			else {
				//list�ȿ� functionNode�� ���� ���
				if(operand.car() instanceof FunctionNode) {
					return ((ListNode)runExpr(operand)).car(); 
				}else { //QuoteNode�� ���� list�� �� ��� error��� ��¹��� ��Ÿ����.
					errorLog("error");
					return null;
				}
			}
		case CDR: //runQuote �Լ��� cdr�� ������ ����Ʈ�� �־� �� ����Ʈ���� cdr �κ��� ���Ͻ�Ų��. 
			//list�� �ƴ� �����Ϳ� ���ؼ� error�� ����.
			if(!(operand instanceof ListNode)) {
				errorLog("error");
				return null;
			}
			if(operand.car() instanceof QuoteNode) 
				return ((ListNode)runQuote(operand)).cdr();
			else {
				//list�ȿ� functionNode�� ���� ���
				if(operand.car() instanceof FunctionNode)
					return ((ListNode)runExpr(operand)).cdr();
				else {  //QuoteNode�� ���� list�� �� ��� error��� ��¹��� ��Ÿ����.
					errorLog("error");
					return null;
				}
			}
		case CONS: //runQuote �Լ��� cons�� ������ ����Ʈ�� �־� head �κ��� IntNode���� �ƴ����� ������ head�� ����Ʈ tail�� �پ� ���ο� ����Ʈ�� ����� �����Ѵ�. 
			//list �� tail �κ��� list�� �ƴ� �����Ϳ� ���ؼ� error�� ����. 
			if(!(operand.cdr().car() instanceof ListNode)) {
				errorLog("error"); return null;
			}
			ListNode tail;
			//list�� tail �κп� QuoteNode�� �ִ� ���
			if(((ListNode)operand.cdr().car()).car() instanceof QuoteNode) 
				tail = (ListNode)runQuote((ListNode)operand.cdr().car());
			else {
				//list�� tail �κп� QuoteNode�� ������ FunctionNode�� �ִ� ��� �ش� FunctionNode�� �°Բ� ����� ����� �������� ���ش�. 
				tail = (ListNode)runExpr(operand.cdr().car());
			}		

			//operand�� car() �κ��� IntNode�� ��� 
			if(operand.car() instanceof IntNode || operand.car() instanceof BooleanNode)
				return ListNode.cons(operand.car(), tail);
			//operand�� car() �κ��� list�� ���
			else {
				Node head = operand.car();
				if(!(((ListNode)head).car() instanceof QuoteNode)) {
					//'�� ���� list������ functionNode�� BinaryOpNode�� �ִ� ���
					head = runExpr(head);
				}
				//idNode�� ����Ʈ�� ���
				else head = runQuote((ListNode)head);
				return ListNode.cons(head,tail);
			}

		case ATOM_Q: //list�� �ƴϸ� ��� atom �̹Ƿ� list �� ���� false, �ƴ� ���� true�� �����Ѵ�. 
			//idNode �̰ų� null list�� ��� true
			if(operand.car() instanceof QuoteNode) {
				if(runQuote(operand) instanceof IdNode || ((ListNode)runQuote(operand)).car() == null)
					return BooleanNode.TRUE_NODE;
				//list�� ��� false
				else return BooleanNode.FALSE_NODE;
			}
			else return BooleanNode.TRUE_NODE;

		case COND: //���ǹ��� true�̸� �� �ش��ϴ� ���ڸ� ��½�Ű�� �ƴϸ� ���� ���ǹ��� ����ȴ�. 

			BooleanNode nodeValue; //���ǹ��� ���� true���� false���� ��� ����
			//���ǹ��� �ϳ��� ���� ��
			if(number==1 && (!(operand.cdr().car() instanceof ListNode) || (((ListNode)operand.cdr().car()).car() instanceof BinaryOpNode))) {
				nodeValue = (BooleanNode)runExpr(((ListNode)operand).car()); //�� ���ǹ��� ��(true���� false����)
				if(nodeValue.value)
					return new IntNode(Integer.toString(((IntNode)runExpr(((ListNode)operand).cdr().car())).getValue()));
				else 
					return null;
			}
			else { //���ǹ��� 2�� �̻��� ��
				Node node = operand.car(); //�����ų ���ǹ�
				nodeValue = (BooleanNode)runExpr(((ListNode)node).car()); 
				if(!nodeValue.value) { //�� ���ǹ��� ���� false�̸�
					number = 2;
					return runFunction(operator,operand.cdr()); 
					//runFunction�Լ��� ���� ���ǹ��� �����ų ������ �־ ��ͷ� ���� �����Ų��. 
				}
				int result = ((IntNode)runExpr(((ListNode)node).cdr().car())).getValue(); 
				//�ش� ���ǹ��� true�̸� �ش��ϴ� ������ ���� ������� �ش�. 
				return new IntNode(Integer.toString(result));	
			}				

		case EQ_Q: //�� ��带 ���Ͽ� ���� ���� ��ü�� �����ϴ��� ��ȯ�Ѵ�. 
			Node eq1 = operand.car(); //eq1�� ���� ù��° ��
			Node eq2 = operand.cdr().car(); //eq2�� ���� �ι�° ��
			
			if(eq1 instanceof ListNode) { //��Ʈ��� ��Ʈ �����.
				if(((ListNode)eq1).car() instanceof QuoteNode) { //eq1�� ù��°�� ��Ʈ�ΰ��
					eq1 = runQuote((ListNode)eq1);
				}
				else eq1 = runExpr(eq1); //��Ʈ�� �ƴϸ� ���ΰ� ������ش�.
			}
			if(eq2 instanceof ListNode) { //��Ʈ��� ��Ʈ �����.
				if(((ListNode)eq2).car() instanceof QuoteNode) 
					eq2 = runQuote((ListNode)eq2); 
				else eq2 = runExpr(eq2); //eq2�� ���ΰ� ������ش�.
			}

			//eq1�̳� eq2�� FunctionNode�̰ų� BooleanNode�̰ų� BinaryOpNode�϶�
			if(eq1 instanceof FunctionNode || eq1 instanceof BooleanNode || eq1 instanceof BinaryOpNode) {
				String id = eq1.toString();
				String id2 = eq2.toString();
				if(id.equals(id2)) return new BooleanNode(true);
				//if(id == id2) return new BooleanNode(true);
				else return new BooleanNode(false);
			}
			//eq1�� IdNode�� IndNode�϶� 
			else if(eq1 instanceof IntNode || eq1 instanceof IdNode) {
				return new BooleanNode(eq1.equals(eq2));
			}
			//List�� �ƴ� ���, �⺻ ������ Ÿ���� ��ü�� ����ϱ⶧���� �� ���� equals�� �����ش�.
			
			else { //����Ʈ�ΰ�쿡�� �⺻������Ÿ���� �ƴϹǷ� equals�� �ƴ϶�, == ���� ��ü�����ش�.
				if(eq1 == eq2) return new BooleanNode(true);
				else return new BooleanNode(false);
			}
		case NOT: //operator�� ������ �κ��� Boolean���� �ݴ��� ���� �����Ѵ�. 			
			BooleanNode node1;
			if(operand.car() instanceof BooleanNode)
				node1 = (BooleanNode)runExpr(operand.car());
			else node1 = (BooleanNode)runExpr(operand);
			return new BooleanNode(!node1.value);

		case NULL_Q: //list�� null���� �˻��Ѵ�. 
			if(operand.car() instanceof QuoteNode) {
				if(((ListNode)runQuote(operand)).car() == null) return BooleanNode.TRUE_NODE;
				else return BooleanNode.FALSE_NODE;
			}
			else return BooleanNode.FALSE_NODE;

		case DEFINE:
			Node def1 = operand.car(); // �����Ҵ�� ����
			Node def2 = null; // ����� ������ �� ����
			if(operand.cdr().car() instanceof ListNode) {
				//IdNode�� ��������� ���� ��Ʈ��尡 ���� ����Ʈ�� ��
				if(!(((ListNode)operand.cdr().car()).car() instanceof QuoteNode)) {
					//�� ����Ʈ�� ù ���Ұ� FunctionNode �϶�
					if(((ListNode)operand.cdr().car()).car() instanceof FunctionNode){
						//�� ����Ʈ�� ù ���Ұ� LAMBDA �϶�
						if(((FunctionNode)(((ListNode)operand.cdr().car()).car())).funcType == FunctionType.LAMBDA) {
							def2 = operand.cdr().car(); 
							
							//def2�� define�� �� ������ ��� (��ø �Լ��� ���� ����)
							if(((ListNode)((ListNode)def2).cdr().cdr().car()).car() instanceof FunctionNode) { 
								runExpr(((ListNode)def2).cdr().cdr().car()); //runExpr�� ���� def2�� define�� �� ������ ��� �� ������ �����ش�. 
								Node n1 = ((ListNode)operand.cdr().car()).car(); //def2�� ù ����
								Node n2 = ((ListNode)operand.cdr().car()).cdr().car(); //def2�� �ι�° ����
								ListNode node = ListNode.cons((ListNode)((ListNode)def2).cdr().cdr().cdr().car(),ListNode.EMPTYLIST); //define�� ������ �� �κ��� EMPTYLIST�� cons�� �ϳ��� list�� �����ش�.
								node = ListNode.cons((ListNode)n2,node); //n2�� node�� �ϳ��� list�� �����ش�.
								def2 = ListNode.cons(n1,node); //n1�� node�� �ϳ��� list�� �����ش�. 
							}
						}
					}
					else def2 = runExpr(operand.cdr().car());
				}
				else def2 = runExpr(operand.cdr().car());
			}
			else
				def2 = runExpr((operand.cdr()).car());

			hashTable.put(def1, def2); //���̺� �߰�
			break;

		case LAMBDA:

		default:
			break;				
		}
		return null;
	}

	private Node stripList(ListNode node) {
		if(node.car() instanceof ListNode && node.cdr() == ListNode.EMPTYLIST) {
			Node listNode = node.car();
			return listNode;
		}else {
			return node;
		}
	}

	private Node runBinary(ListNode list) {
		BinaryOpNode operator = (BinaryOpNode) list.car(); 
		int n1;
		//idNode�� �ִ� ����Ʈ����� ���
		if(list.cdr().car() instanceof ListNode && ((ListNode)list.cdr().car()).car() instanceof IdNode) {
			Node l1= runExpr(((ListNode)list.cdr().car()).car());
			ListNode l2 = ListNode.cons(l1,(ListNode)((ListNode)list.cdr().car()).cdr());
			n1 = ((IntNode)runExpr(l2)).getValue();
		}
		else n1 = ((IntNode)runExpr(list.cdr().car())).getValue(); //operator�� �����ϰ� ù��°�� �ִ� ����
		int n2 = ((IntNode)runExpr(list.cdr().cdr().car())).getValue(); //operator�� �����ϰ� �ι�°�� �ִ� ����
		if(array2 != null) { //array2�� null�� �ƴ϶�� ���� array2�� ���� �ִٴ� ���̹Ƿ� �̹� ������ ����� �Ͽ����� ���� null �����ش�. 
			array2 = null; array[1].node = null;
		}
		else {
			if(array1 != null) { //array1�� null�� �ƴ϶�� ���� array1�� ���� �ִٴ� ���̹Ƿ� �̹� ������ ����� �Ͽ����� ���� null �����ش�. 
				array1 = null; array[0].node = null;
			}
		}

		switch(operator.binType) {
		case PLUS: //�� ���ڸ� ���� ���� ��½�Ų��.
			return new IntNode(Integer.toString(n1+n2));
		case MINUS: //�� ���ڸ� �� ���� ��½�Ų��.
			return new IntNode(Integer.toString(n1-n2));
		case TIMES: //�� ���ڸ� ���� ���� ��½�Ų��.
			return new IntNode(Integer.toString(n1*n2));
		case DIV: //ù��° ���ڸ� �ι�° ���ڷ� ���� ���� ��½�Ų��. 
			return new IntNode(Integer.toString(n1/n2));
		case LT: //ù��° ���ڰ� �ι�° ���ں��� ������ true, �ƴϸ� false
			if(n1<n2) return BooleanNode.TRUE_NODE;
			else return BooleanNode.FALSE_NODE;
		case GT: //ù��° ���ڰ� �ι�° ���ں��� Ŭ �� true, �ƴϸ� false
			if(n1>n2) return BooleanNode.TRUE_NODE;
			else return BooleanNode.FALSE_NODE;
		case EQ: //�� ������ ���� ������ true, �ƴϸ� false
			if(n1==n2) return BooleanNode.TRUE_NODE;
			else return BooleanNode.FALSE_NODE;
		default:
			break;
		}
		return null;
	}

	private Node runQuote(ListNode node) {
		return ((QuoteNode) node.car()).nodeInside();
	}
}
