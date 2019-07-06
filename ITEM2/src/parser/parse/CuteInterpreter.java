package parser.parse;
import java.util.Hashtable;
import java.util.Scanner;
import java.io.File;

import parser.ast.*;
import parser.ast.FunctionNode.FunctionType;

public class CuteInterpreter {

	public static Hashtable<Node, Node> hashTable = new Hashtable(50); //테이블 생성
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		String _string;
	    
		
		while(true) { //반복적으로 입력을 받는다.
		System.out.print("> "); 
		_string = sc.nextLine(); //입력받아서 저장
		if(_string.contentEquals("exit")) break; //프로그램 종료조건(임의로 exit으로 만들었다.)
		CuteParser cuteParser = new CuteParser(_string); 
		//cuteParser 객체 생성(매개변수로 File타입에서 String타입으로 바꾼것이 중요)
		CuteInterpreter interpreter = new CuteInterpreter(); //여기부터는 바꿀필요 없다.
		Node parseTree = cuteParser.parseExpr(); 
		Node resultNode = interpreter.runExpr(parseTree);
		NodePrinter nodePrinter = new NodePrinter(resultNode);
		nodePrinter.prettyPrint(); //prettyPrint부분을 콘솔 출력으로 수정했다.
		}
		System.out.println("END PROGRAM");
	}

	private void errLog(String err) {
		System.out.println(err);
	}

	public Node runExpr(Node rootExpr) {
		
		if (rootExpr == null)
			return null;
		if (rootExpr instanceof IdNode) // IdNode일경우 rootExpr 반환
			return rootExpr;
		else if (rootExpr instanceof IntNode) // IntNode일경우 그냥 반환
			return rootExpr;
		else if (rootExpr instanceof BooleanNode) // BooleanNode일경우 그냥 반환
			return rootExpr;
		else if (rootExpr instanceof ListNode) // ListNode일경우 runList 호출
			return runList((ListNode) rootExpr);
		else
			errLog("run Expr error");
		return null;
	}

	// return ListNode.cons(new QuoteNode(((ListNode) isSuper).car()),
	// ListNode.EMPTYLIST);

	private Node runList(ListNode list) {
		if (list.equals(ListNode.EMPTYLIST)) // 리스트가 비어있으면 그냥 반환
			return list;
		if (list.car() instanceof FunctionNode) { // 첫번째 원소가 FunctionNode라면 runFunction 실행
			return runFunction((FunctionNode) list.car(), (ListNode) stripList(list.cdr()));
		}
		if (list.car() instanceof BinaryOpNode) { // 첫번째원소가 BinaryOpNode라면 runBinary 실행
			return runBinary(list);
		}
		return list; // 리스트 반환
	}

	private Node runFunction(FunctionNode operator, ListNode operand) {
		switch (operator.funcType) { // functionType으로 조건문을 돌린다.
		// CAR,CDR,CONS 등에 대한 동작 구현
		case CAR:
			if ((((ListNode) runQuote(((ListNode) operand.car()))).car()) instanceof ListNode)
				// operand의 car이 ListNode일 경우
				return new QuoteNode(((ListNode) runQuote(((ListNode) operand.car()))).car());
			// quote노드로 바꿔서 출력
			return ((ListNode) runQuote(((ListNode) operand.car()))).car();
		// 리스트 노드가 아닐경우, 쿼트를 벗겨서 첫번째 원소를 반환한다.

		case CDR:
			return new QuoteNode(((ListNode) runQuote(((ListNode) operand.car()))).cdr());
		// 입력 오류는 없다고 가정했으므로, 첫번째원소(car)을 제외한 나머지 cdr을 Quote로 묶어서 반환한다.
		case CONS:
			Node a; // head
			ListNode b; // tail
			if (!(operand.car() instanceof ListNode)) // 첫번째가 리스트 노드가 아닌경우
				a = operand.car(); // a는 그대로 car로 정해준다.
			else if (((ListNode) operand.car()).car() instanceof QuoteNode)// 첫번째가 리스트 노드이고, 그 head가 쿼트노드인경우
				a = (ListNode) runQuote((ListNode) operand.car()); // 쿼트를 벗겨서 저장한다.
			else
				a = ((ListNode) runQuote(((ListNode) operand.car()))).car(); // 첫번째가 리스트노드이고, head가 쿼트노드가 아니라면
			// 그 리스트를 쿼트벗기고 그대로 저장한다.

			b = (ListNode) (runQuote((ListNode) (operand.cdr()).car()));// tail은 두번째 원소를 리스트 형태로 저장한다.
			return new QuoteNode(ListNode.cons(a, b)); // 구한 head와 tail을 합쳐서 쿼트노드로 반환한다.

		case COND:

			if (runExpr(((ListNode) operand.car()).car()) == BooleanNode.TRUE_NODE)
				return (((ListNode) operand.car()).cdr()).car();
			// operand의 첫번째가 True노드라면 그 바로 옆에있는 값을 반환한다.
			else
				return (runFunction(operator, ((ListNode) (operand.cdr()))));
			// 아니라면, 다음 노드로 재귀호출

		case NOT:
			if ((runExpr(operand.car())) instanceof BooleanNode) { // 연산 결과가 BooleanNode인경우
				if ((runExpr(operand.car())) == BooleanNode.TRUE_NODE)
					return BooleanNode.FALSE_NODE; // True인 경우 False 반환
				else
					return BooleanNode.TRUE_NODE; // False인 경우 True 반환
			}
			else if (((ListNode) operand.car()).car() instanceof QuoteNode) { // 쿼트일경우
				Node test = runQuote((ListNode) operand.car()); // 쿼트 벗긴 값 임시 저장
				if (((ListNode) (test)).car() == BooleanNode.TRUE_NODE)
					return BooleanNode.FALSE_NODE;
				else
					return BooleanNode.TRUE_NODE;
			}

		case ATOM_Q:
			
			Node test = runQuote((ListNode) operand.car()); // 임시 변수에 operand의 첫번째값을 쿼트벗겨서 저장한다.
			if (test instanceof ListNode) { // 첫번째가 리스트 노드인 경우
				if ((((ListNode) test).car()) == null)
					return BooleanNode.TRUE_NODE; // 그 리스트 안에 값이 없다면 true반환
				else
					return BooleanNode.FALSE_NODE; // 값이 들어있다면 listNode이므로 false반환
			} else
				return BooleanNode.TRUE_NODE; // 첫번째가 리스트노드가 아니라면 atom이므로 true반환

		case NULL_Q:
			if (((ListNode) runQuote(((ListNode) operand.car()))).car() == null) {
				// 첫번째 원소의 쿼트를 벗기고, 그 car값이 null이면 true, 아니면 false를 반환한다.
				return BooleanNode.TRUE_NODE;
			} else
				return BooleanNode.FALSE_NODE;

		case EQ_Q:
			Node eq1 = runExpr((runExpr(operand.car()))); // 비교대상1번 저장
			Node eq2 = runExpr((operand.cdr()).car()); // 비교대상 2번 저장
			eq1 = runQuote((ListNode) eq1); // 쿼트 벗긴다.
			eq2 = runQuote((ListNode) eq2); // 쿼트 벗긴다.
			if (eq1 instanceof IntNode) { // IntNode일 경우, IntNode의 equals 호출
				if (((IntNode) eq1).equals(eq2))
					return BooleanNode.TRUE_NODE;
				else
					return BooleanNode.FALSE_NODE;
			} else if (eq2 instanceof IdNode) { // IdNode일 경우, IdNode의 equals 호출
				if (((IdNode) eq2).equals(eq1))
					return BooleanNode.TRUE_NODE;
				else
					return BooleanNode.FALSE_NODE;
			} else
				return BooleanNode.FALSE_NODE;


		case LAMBDA:
			
		case DEFINE:
            Node def1 = operand.car(); // 저장할대상 저장
            Node def2 = null; // 대상을 저장할 값 저장
            if(!(operand.cdr().car() instanceof QuoteNode)) {
               def2 = operand.cdr().car();
            }
            else
               def2 = runExpr((operand.cdr()).car());
            
            hashTable.put(def1, def2); //테이블에 추가
            break;
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
		// operator(연산자)변수에 car값 저장
		Integer a; // operand1
		Integer b; // operand2
		Node returnNode;
		Integer parameter;
		if ((list.cdr()).car() instanceof ListNode) // 첫번째 값이 ListNode일 경우
			a = ((IntNode) runBinary((ListNode) ((list.cdr()).car()))).getValue();// 재귀로 값을 계산

		else
			a = ((IntNode) (list.cdr()).car()).getValue();// ListNode가 아니면 값 반환

		if ((((list.cdr()).cdr()).car()) instanceof ListNode) // 두번째 값이 ListNode일 경우
			b = ((IntNode) runBinary(((ListNode) (list.cdr()).cdr().car()))).getValue();// 재귀로 값을 계산
		else
			b = ((IntNode) ((list.cdr()).cdr()).car()).getValue();// ListNode가 아니면 값 반환

		// 구현과정에서 필요한 변수 및 함수 작업 가능
		switch (operator.binType) {
		// +,-,/ 등에 대한 바이너리 연산 동작 구현
		case PLUS: // 더해서 노드생성
			parameter = new Integer(a.intValue() + b.intValue());
			returnNode = new IntNode(parameter.toString());
			return returnNode;
		case MINUS: // 뺴서 노드생성
			parameter = new Integer(a.intValue() - b.intValue());
			returnNode = new IntNode(parameter.toString());
			return returnNode;
		case TIMES: // 곱해서 노드생성
			parameter = new Integer(a.intValue() * b.intValue());
			returnNode = new IntNode(parameter.toString());
			return returnNode;
		case DIV: // 나눠서 노드생성
			parameter = new Integer(a.intValue() / b.intValue());
			returnNode = new IntNode(parameter.toString());
			return returnNode;
		case LT: // less than, 왼쪽이 크면 False 아니면 True
			if (a.intValue() > b.intValue())
				returnNode = BooleanNode.FALSE_NODE;
			else
				returnNode = BooleanNode.TRUE_NODE;
			return returnNode;
		case GT: // Great than, 오른쪽이 크면 False 아니면 True
			if (a.intValue() < b.intValue())
				returnNode = BooleanNode.FALSE_NODE;
			else
				returnNode = BooleanNode.TRUE_NODE;
			return returnNode;
		case EQ:// 같으면 True 아니면 False
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
