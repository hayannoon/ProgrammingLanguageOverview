package parser.parse;

import java.io.File;

import parser.ast.*;
import parser.ast.FunctionNode.FunctionType;

public class CuteInterpreter {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ClassLoader cloader = ParserMain.class.getClassLoader();
		File file = new File(cloader.getResource("as08.txt").getFile());
		CuteParser cuteParser = new CuteParser(file);
		CuteInterpreter interpreter = new CuteInterpreter();
		Node parseTree = cuteParser.parseExpr();
		Node resultNode = interpreter.runExpr(parseTree);
		NodePrinter nodePrinter = new NodePrinter(resultNode);
		nodePrinter.prettyPrint();

	}

	private void errLog(String err) {
		System.out.println(err);
	}

	public Node runExpr(Node rootExpr) {
		if (rootExpr == null)
			return null;
		if (rootExpr instanceof IdNode) // IdNode�ϰ�� rootExpr ��ȯ
			return rootExpr;
		else if (rootExpr instanceof IntNode) // IntNode�ϰ�� �׳� ��ȯ
			return rootExpr;
		else if (rootExpr instanceof BooleanNode) // BooleanNode�ϰ�� �׳� ��ȯ
			return rootExpr;
		else if (rootExpr instanceof ListNode) // ListNode�ϰ�� runList ȣ��
			return runList((ListNode) rootExpr);
		else
			errLog("run Expr error");
		return null;
	}

	// return ListNode.cons(new QuoteNode(((ListNode) isSuper).car()),
	// ListNode.EMPTYLIST);

	private Node runList(ListNode list) {
		if (list.equals(ListNode.EMPTYLIST)) // ����Ʈ�� ��������� �׳� ��ȯ
			return list;
		if (list.car() instanceof FunctionNode) { // ù��° ���Ұ� FunctionNode��� runFunction ����
			return runFunction((FunctionNode) list.car(), (ListNode) stripList(list.cdr()));
		}
		if (list.car() instanceof BinaryOpNode) { // ù��°���Ұ� BinaryOpNode��� runBinary ����
			return runBinary(list);
		}
		return list; // ����Ʈ ��ȯ
	}

	private Node runFunction(FunctionNode operator, ListNode operand) {
		switch (operator.funcType) { // functionType���� ���ǹ��� ������.
		// CAR,CDR,CONS � ���� ���� ����
		case CAR:
			if ((((ListNode) runQuote(((ListNode) operand.car()))).car()) instanceof ListNode)
				//operand�� car�� ListNode�� ���
				return new QuoteNode(((ListNode) runQuote(((ListNode) operand.car()))).car());
			// quote���� �ٲ㼭 ���
			return ((ListNode) runQuote(((ListNode) operand.car()))).car();
			//����Ʈ ��尡 �ƴҰ��, ��Ʈ�� ���ܼ� ù��° ���Ҹ� ��ȯ�Ѵ�.

		case CDR:
			return new QuoteNode(((ListNode) runQuote(((ListNode) operand.car()))).cdr());
			//�Է� ������ ���ٰ� ���������Ƿ�, ù��°����(car)�� ������ ������ cdr�� Quote�� ��� ��ȯ�Ѵ�.
		case CONS:
			Node a; //head
			ListNode b; //tail
			if (!(operand.car() instanceof ListNode)) //ù��°�� ����Ʈ ��尡 �ƴѰ��
				a = operand.car(); //a�� �״�� car�� �����ش�.
			else if (((ListNode) operand.car()).car() instanceof QuoteNode)//ù��°�� ����Ʈ ����̰�, �� head�� ��Ʈ����ΰ��
				a = (ListNode) runQuote((ListNode) operand.car()); //��Ʈ�� ���ܼ� �����Ѵ�.
			else
				a = ((ListNode) runQuote(((ListNode) operand.car()))).car(); //ù��°�� ����Ʈ����̰�, head�� ��Ʈ��尡 �ƴ϶��
				//�� ����Ʈ�� ��Ʈ����� �״�� �����Ѵ�.
			
			b = (ListNode) (runQuote((ListNode) (operand.cdr()).car()));//tail�� �ι�° ���Ҹ� ����Ʈ ���·� �����Ѵ�.
			return new QuoteNode(ListNode.cons(a, b)); //���� head�� tail�� ���ļ� ��Ʈ���� ��ȯ�Ѵ�.

		case COND:
			
			if( runExpr( ((ListNode)operand.car()).car()  ) == BooleanNode.TRUE_NODE )
				return (((ListNode)operand.car()).cdr()).car();
			//operand�� ù��°�� True����� �� �ٷ� �����ִ� ���� ��ȯ�Ѵ�.
			else return ( runFunction(operator,((ListNode)(operand.cdr())) ) );
			//�ƴ϶��, ���� ���� ���ȣ��

		case NOT:
			if ((runExpr(operand.car())) instanceof BooleanNode) { //���� ����� BooleanNode�ΰ��
				if ((runExpr(operand.car())) == BooleanNode.TRUE_NODE)
					return BooleanNode.FALSE_NODE; //True�� ��� False ��ȯ
				else
					return BooleanNode.TRUE_NODE; //False�� ��� True ��ȯ
			}
			
			else if (((ListNode) operand.car()).car() instanceof QuoteNode) { // ��Ʈ�ϰ��
				Node test = runQuote((ListNode) operand.car()); //��Ʈ ���� �� �ӽ� ����
				if (((ListNode) (test)).car() == BooleanNode.TRUE_NODE) 
					return BooleanNode.FALSE_NODE;
			else
					return BooleanNode.TRUE_NODE;
			}

		case ATOM_Q:
			Node test = runQuote((ListNode) operand.car()); //�ӽ� ������ operand�� ù��°���� ��Ʈ���ܼ� �����Ѵ�.
			if(test instanceof ListNode) { //ù��°�� ����Ʈ ����� ���
				if((((ListNode) test).car()) == null ) return BooleanNode.TRUE_NODE; //�� ����Ʈ �ȿ� ���� ���ٸ� true��ȯ
				else return BooleanNode.FALSE_NODE; //���� ����ִٸ� listNode�̹Ƿ� false��ȯ
			} else return BooleanNode.TRUE_NODE; // ù��°�� ����Ʈ��尡 �ƴ϶�� atom�̹Ƿ� true��ȯ
			
		case NULL_Q:
			if (((ListNode) runQuote(((ListNode) operand.car()))).car() == null) {
				//ù��° ������ ��Ʈ�� �����, �� car���� null�̸� true, �ƴϸ� false�� ��ȯ�Ѵ�.
				return BooleanNode.TRUE_NODE;
			} else
				return BooleanNode.FALSE_NODE;

		case EQ_Q:
			Node eq1 =  runExpr((runExpr(operand.car()))); //�񱳴��1�� ����
			Node eq2 =  runExpr((operand.cdr()).car()); //�񱳴�� 2�� ����
			eq1 = runQuote((ListNode)eq1); //��Ʈ �����.
			eq2 = runQuote((ListNode)eq2); //��Ʈ �����.
			if(eq1 instanceof IntNode) { //IntNode�� ���, IntNode�� equals ȣ��
				if(((IntNode)eq1).equals(eq2)) return BooleanNode.TRUE_NODE;
				else return BooleanNode.FALSE_NODE;
			} else if(eq2 instanceof IdNode) { //IdNode�� ���, IdNode�� equals ȣ��
				if(((IdNode)eq2).equals(eq1)) return BooleanNode.TRUE_NODE;
				else return BooleanNode.FALSE_NODE;
			} else return BooleanNode.FALSE_NODE;
			
//			if( !(runQuote((ListNode)eq1) instanceof ListNode) ) { //ù��°�� ����Ʈ�� �ƴҶ�
//			if( !(runQuote((ListNode)eq2) instanceof ListNode) ) { //�ι�°�� ����Ʈ�� �ƴҶ�
//				if(  ((runQuote((ListNode)eq1).toString()). hashCode() == ((runQuote((ListNode)eq2)).toString()). hashCode() ) )
//				
//						return BooleanNode.TRUE_NODE; //�Ѵ� ����Ʈ�� �ƴ϶�� �� ��尡 �����ִ� ���� hash���� ���ؼ� ���ٸ� true ��ȯ
//				else return BooleanNode.FALSE_NODE; //�ٸ��ٸ� false ��ȯ
//			} else { //�ι�°�� ����Ʈ�϶� (ù��°�� ����Ʈ�� �ƴϰ� �ι�°�� ����Ʈ)
//				return BooleanNode.FALSE_NODE; //�̰�� �翬�� false
//			}
//		}
//		
//		else { //ù��°�� ����Ʈ ����� ���
//			if( !(runQuote((ListNode)eq2) instanceof ListNode)) {
//				return BooleanNode.FALSE_NODE; //�ι�°�� ����Ʈ�� �ƴϸ� false
//			}
//			else if( (runQuote((ListNode)eq1)).equals(runQuote((ListNode)eq2)) ) //�ι�°�� ����Ʈ�ΰ�� equals ��
//				return BooleanNode.TRUE_NODE; //equls���� true��� true ��ȯ
//			else return BooleanNode.FALSE_NODE; //�ٸ��ٸ� false ��ȯ
//		}
			
		case LAMBDA:
		case DEFINE:
		default:
			break;
		}
		return null;
	}

	private Node stripList(ListNode node) {
		if (node.car() instanceof ListNode && node.car() == ListNode.EMPTYLIST) {
			Node listNode = node.car();
			return listNode;
		} else
			return node;
	}

	private Node runBinary(ListNode list) {
		BinaryOpNode operator = (BinaryOpNode) list.car();
		// operator(������)������ car�� ����
		Integer a; //operand1
		Integer b; //operand2
		Node returnNode;
		Integer parameter;
		if ((list.cdr()).car() instanceof ListNode) // ù��° ���� ListNode�� ���
			a = ((IntNode) runBinary((ListNode) ((list.cdr()).car()))).getValue();//��ͷ� ���� ���
			
		else
			a = ((IntNode) (list.cdr()).car()).getValue();//ListNode�� �ƴϸ� �� ��ȯ

		if ((((list.cdr()).cdr()).car()) instanceof ListNode) //�ι�° ���� ListNode�� ���
			b = ((IntNode) runBinary(((ListNode) (list.cdr()).cdr().car()))).getValue();//��ͷ� ���� ���
		else
			b = ((IntNode) ((list.cdr()).cdr()).car()).getValue();//ListNode�� �ƴϸ� �� ��ȯ
		
		// ������������ �ʿ��� ���� �� �Լ� �۾� ����
		switch (operator.binType) {
		// +,-,/ � ���� ���̳ʸ� ���� ���� ����
		case PLUS: //���ؼ� ������
			parameter = new Integer(a.intValue() + b.intValue());
			returnNode = new IntNode(parameter.toString());
			return returnNode;
		case MINUS: //���� ������
			parameter = new Integer(a.intValue() - b.intValue());
			returnNode = new IntNode(parameter.toString());
			return returnNode;
		case TIMES: //���ؼ� ������
			parameter = new Integer(a.intValue() * b.intValue());
			returnNode = new IntNode(parameter.toString());
			return returnNode;
		case DIV: // ������ ������
			parameter = new Integer(a.intValue() / b.intValue());
			returnNode = new IntNode(parameter.toString());
			return returnNode;
		case LT: //less than, ������ ũ�� False �ƴϸ� True
			if (a.intValue() > b.intValue())
				returnNode = BooleanNode.FALSE_NODE;
			else
				returnNode = BooleanNode.TRUE_NODE;
			return returnNode;
		case GT: //Great than, �������� ũ�� False �ƴϸ� True
			if (a.intValue() < b.intValue())
				returnNode = BooleanNode.FALSE_NODE;
			else
				returnNode = BooleanNode.TRUE_NODE;
			return returnNode;
		case EQ:// ������ True �ƴϸ� False
			if (a.intValue() == b.intValue())
				returnNode = BooleanNode.TRUE_NODE;
			else
				returnNode = BooleanNode.FALSE_NODE;
			return returnNode;

		default:
			break;
		}
		return null;
	}

	private Node runQuote(ListNode node) {
		return ((QuoteNode) node.car()).nodeInside();
	}
}
