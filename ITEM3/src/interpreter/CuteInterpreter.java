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
	public static Hashtable<Node, Node> hashTable = new Hashtable(50); //테이블 생성(define을 위한)
	_Node array[] = new _Node[2]; //lambda를 사용할때 IdNode의 임의의 값을 저장해주는 노드 클래스의 array 배열 (람다를 한 문장에서 두번 사용할때 가정)
	Node array1 = null; //배열에서 노드 저장해줄 변수(나중에 노드 사라지게 하기 위해 사용)
	Node array2 = null; //배열에서 노드 저장해줄 변수(나중에 노드 사라지게 하기 위해 사용)
	int number = 1; //cond에서 사용할 변수로 조건문이 두개 이상인지 아닌지를 알려줄 변수이다. 
	
	public static void main(String args[]) {		
		//ClassLoader cloader = ParserMain.class.getClassLoader();
		//File file = new File(cloader.getResource("interpreter/as08.txt").getFile());
		while(true) {
			Scanner sc = new Scanner(System.in);
			System.out.print("> ");
			String str = sc.nextLine();
			if(str.contentEquals("exit")) {//프로그램 종료조건(임의로 exit으로 만들었다.)
				System.out.println("프로그램을 종료합니다.");
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
	
	public void runFirst() { //array 배열 초기화시켜준다. 
		for(int i=0 ; i<array.length ; i++) {
			array[i] = new _Node(null,null);
		}
	}
	
	public Node runExpr(Node rootExpr) {
		if(rootExpr == null) return null;
		if(rootExpr instanceof IdNode) {
			
			/**** lambda로 정의되어있는 IdNode값을 찾아준다. *****/
			//array 배열의 노드가 null이 아닐 때
			for(int i=0 ; i<array.length ; i++) {
				if(array[i].node != null) {
					//array 배열의 노드와 일치하는 IdNode일 때 
					if((array[i].node).equals(rootExpr)) {
						if(array1 == null) { 
							array1 = array[i].node;
						}
						else {
							array2 = array[i].node;
						}
						rootExpr = array[i].key; //일치하는 노드의 값으로 저장시켜준다. 
						return rootExpr;
					}
				}
			}
			/*******/
		
			/**** define으로 정의되어있는 IdNode값을 찾아준다. *****/
			IdNode compare;
			Enumeration<Node> e = (CuteInterpreter.hashTable).keys();
			while(e.hasMoreElements()) {
				compare = (IdNode)e.nextElement();
				if ((rootExpr).equals(compare)) { // 토큰이 같은경우
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
		if(list.equals(ListNode.EMPTYLIST)) return list; //리스트가 비어있으면 그대로 반환
		if(list.car() instanceof FunctionNode) { 
			//list.car() 가 lambda 일때 이후 원소가 없다는 것이므로 그대로 return 시켜준다. 
			if (((FunctionNode)list.car()).funcType == FunctionType.LAMBDA) {
				return list; //function Type이 Lambda라면 list를 반환
			}
			else
				return runFunction((FunctionNode) list.car(), (ListNode) stripList(list.cdr()));
		}		//Lambda가 아니면 runFunction 호출
		if(list.car() instanceof BinaryOpNode) return runBinary(list); //binaryOpNode면 runBinary 실행
		if(list.car() instanceof ListNode) { //List노드인경우
			if(!(((ListNode)list.car()).car() instanceof QuoteNode)) { //쿼트가 아니고
				if((((ListNode)list.car()).car() instanceof FunctionNode)) { //functionNode라면
					if(((FunctionNode)(((ListNode)list.car()).car())).funcType == FunctionType.LAMBDA) { //람다인경우
						Node head = ((ListNode)(((ListNode)list.car()).cdr().car())).car(); //head값(x값) 찾아서 저장
						Node lamb2 = runExpr(list.cdr().car()); //바인딩해주는 값을 lamb2에 저장
						if (head instanceof IdNode) { //head가 IdNode인경우
							for(int i=0 ; i<array.length ; i++) { 
								if(array[i].node == null) { //비어있는 array 부분에 저장시켜준다. 
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
		case CAR: //runQuote 함수에 car을 제외한 리스트를 넣어 그 리스트에서 car 부분을 찾아내고 리턴시킨다. 
			//list가 아닌 데이터에 대해서 error를 낸다.
			if(!(operand instanceof ListNode)) {
				errorLog("error");
				return null;
			}
			if(((ListNode)operand).car() instanceof QuoteNode) 
				return ((ListNode)runQuote(operand)).car();
			else {
				//list안에 functionNode가 오는 경우
				if(operand.car() instanceof FunctionNode) {
					return ((ListNode)runExpr(operand)).car(); 
				}else { //QuoteNode가 없는 list가 올 경우 error라고 출력문이 나타난다.
					errorLog("error");
					return null;
				}
			}
		case CDR: //runQuote 함수에 cdr을 제외한 리스트를 넣어 그 리스트에서 cdr 부분을 리턴시킨다. 
			//list가 아닌 데이터에 대해서 error를 낸다.
			if(!(operand instanceof ListNode)) {
				errorLog("error");
				return null;
			}
			if(operand.car() instanceof QuoteNode) 
				return ((ListNode)runQuote(operand)).cdr();
			else {
				//list안에 functionNode가 오는 경우
				if(operand.car() instanceof FunctionNode)
					return ((ListNode)runExpr(operand)).cdr();
				else {  //QuoteNode가 없는 list가 올 경우 error라고 출력문이 나타난다.
					errorLog("error");
					return null;
				}
			}
		case CONS: //runQuote 함수에 cons를 제외한 리스트를 넣어 head 부분이 IntNode인지 아닌지로 구분해 head를 리스트 tail와 붙어 새로운 리스트를 만들어 리턴한다. 
			//list 의 tail 부분이 list가 아닌 데이터에 대해서 error를 낸다. 
			if(!(operand.cdr().car() instanceof ListNode)) {
				errorLog("error"); return null;
			}
			ListNode tail;
			//list의 tail 부분에 QuoteNode가 있는 경우
			if(((ListNode)operand.cdr().car()).car() instanceof QuoteNode) 
				tail = (ListNode)runQuote((ListNode)operand.cdr().car());
			else {
				//list의 tail 부분에 QuoteNode는 없지만 FunctionNode가 있는 경우 해당 FunctionNode에 맞게끔 실행된 결과가 나오도록 해준다. 
				tail = (ListNode)runExpr(operand.cdr().car());
			}		

			//operand의 car() 부분이 IntNode인 경우 
			if(operand.car() instanceof IntNode || operand.car() instanceof BooleanNode)
				return ListNode.cons(operand.car(), tail);
			//operand의 car() 부분이 list인 경우
			else {
				Node head = operand.car();
				if(!(((ListNode)head).car() instanceof QuoteNode)) {
					//'가 없는 list이지만 functionNode나 BinaryOpNode가 있는 경우
					head = runExpr(head);
				}
				//idNode나 리스트인 경우
				else head = runQuote((ListNode)head);
				return ListNode.cons(head,tail);
			}

		case ATOM_Q: //list가 아니면 모두 atom 이므로 list 인 경우는 false, 아닌 경우는 true를 리턴한다. 
			//idNode 이거나 null list인 경우 true
			if(operand.car() instanceof QuoteNode) {
				if(runQuote(operand) instanceof IdNode || ((ListNode)runQuote(operand)).car() == null)
					return BooleanNode.TRUE_NODE;
				//list인 경우 false
				else return BooleanNode.FALSE_NODE;
			}
			else return BooleanNode.TRUE_NODE;

		case COND: //조건문이 true이면 그 해당하는 숫자를 출력시키고 아니면 다음 조건문이 실행된다. 

			BooleanNode nodeValue; //조건문의 값이 true인지 false인지 담는 변수
			//조건문이 하나만 있을 때
			if(number==1 && (!(operand.cdr().car() instanceof ListNode) || (((ListNode)operand.cdr().car()).car() instanceof BinaryOpNode))) {
				nodeValue = (BooleanNode)runExpr(((ListNode)operand).car()); //그 조건문의 값(true인지 false인지)
				if(nodeValue.value)
					return new IntNode(Integer.toString(((IntNode)runExpr(((ListNode)operand).cdr().car())).getValue()));
				else 
					return null;
			}
			else { //조건문이 2개 이상일 때
				Node node = operand.car(); //실행시킬 조건문
				nodeValue = (BooleanNode)runExpr(((ListNode)node).car()); 
				if(!nodeValue.value) { //그 조건문의 값이 false이면
					number = 2;
					return runFunction(operator,operand.cdr()); 
					//runFunction함수에 다음 조건문의 실행시킬 문장을 넣어서 재귀로 돌려 실행시킨다. 
				}
				int result = ((IntNode)runExpr(((ListNode)node).cdr().car())).getValue(); 
				//해당 조건문이 true이면 해당하는 숫자의 값을 저장시켜 준다. 
				return new IntNode(Integer.toString(result));	
			}				

		case EQ_Q: //두 노드를 비교하여 값이 같은 객체를 참조하는지 반환한다. 
			Node eq1 = operand.car(); //eq1은 비교할 첫번째 값
			Node eq2 = operand.cdr().car(); //eq2는 비교할 두번째 값
			
			if(eq1 instanceof ListNode) { //쿼트라면 쿼트 벗긴다.
				if(((ListNode)eq1).car() instanceof QuoteNode) { //eq1의 첫번째가 쿼트인경우
					eq1 = runQuote((ListNode)eq1);
				}
				else eq1 = runExpr(eq1); //쿼트가 아니면 내부값 계산해준다.
			}
			if(eq2 instanceof ListNode) { //쿼트라면 쿼트 벗긴다.
				if(((ListNode)eq2).car() instanceof QuoteNode) 
					eq2 = runQuote((ListNode)eq2); 
				else eq2 = runExpr(eq2); //eq2의 내부값 계산해준다.
			}

			//eq1이나 eq2가 FunctionNode이거나 BooleanNode이거나 BinaryOpNode일때
			if(eq1 instanceof FunctionNode || eq1 instanceof BooleanNode || eq1 instanceof BinaryOpNode) {
				String id = eq1.toString();
				String id2 = eq2.toString();
				if(id.equals(id2)) return new BooleanNode(true);
				//if(id == id2) return new BooleanNode(true);
				else return new BooleanNode(false);
			}
			//eq1이 IdNode나 IndNode일때 
			else if(eq1 instanceof IntNode || eq1 instanceof IdNode) {
				return new BooleanNode(eq1.equals(eq2));
			}
			//List가 아닌 경우, 기본 데이터 타입을 객체로 사용하기때문에 그 값을 equals로 비교해준다.
			
			else { //리스트인경우에는 기본데이터타입이 아니므로 equals가 아니라, == 으로 객체비교해준다.
				if(eq1 == eq2) return new BooleanNode(true);
				else return new BooleanNode(false);
			}
		case NOT: //operator를 제외한 부분의 Boolean값의 반대의 값을 리턴한다. 			
			BooleanNode node1;
			if(operand.car() instanceof BooleanNode)
				node1 = (BooleanNode)runExpr(operand.car());
			else node1 = (BooleanNode)runExpr(operand);
			return new BooleanNode(!node1.value);

		case NULL_Q: //list가 null인지 검사한다. 
			if(operand.car() instanceof QuoteNode) {
				if(((ListNode)runQuote(operand)).car() == null) return BooleanNode.TRUE_NODE;
				else return BooleanNode.FALSE_NODE;
			}
			else return BooleanNode.FALSE_NODE;

		case DEFINE:
			Node def1 = operand.car(); // 저장할대상 저장
			Node def2 = null; // 대상을 저장할 값 저장
			if(operand.cdr().car() instanceof ListNode) {
				//IdNode에 저장시켜줄 값이 쿼트노드가 없는 리스트일 때
				if(!(((ListNode)operand.cdr().car()).car() instanceof QuoteNode)) {
					//그 리스트의 첫 원소가 FunctionNode 일때
					if(((ListNode)operand.cdr().car()).car() instanceof FunctionNode){
						//그 리스트의 첫 원소가 LAMBDA 일때
						if(((FunctionNode)(((ListNode)operand.cdr().car()).car())).funcType == FunctionType.LAMBDA) {
							def2 = operand.cdr().car(); 
							
							//def2에 define이 또 나오는 경우 (중첩 함수를 위한 구현)
							if(((ListNode)((ListNode)def2).cdr().cdr().car()).car() instanceof FunctionNode) { 
								runExpr(((ListNode)def2).cdr().cdr().car()); //runExpr를 통해 def2에 define이 또 나오는 경우 그 문장을 돌려준다. 
								Node n1 = ((ListNode)operand.cdr().car()).car(); //def2의 첫 원소
								Node n2 = ((ListNode)operand.cdr().car()).cdr().car(); //def2의 두번째 원소
								ListNode node = ListNode.cons((ListNode)((ListNode)def2).cdr().cdr().cdr().car(),ListNode.EMPTYLIST); //define을 제외한 뒷 부분을 EMPTYLIST와 cons로 하나의 list로 합쳐준다.
								node = ListNode.cons((ListNode)n2,node); //n2와 node를 하나의 list로 합쳐준다.
								def2 = ListNode.cons(n1,node); //n1과 node를 하나의 list로 합쳐준다. 
							}
						}
					}
					else def2 = runExpr(operand.cdr().car());
				}
				else def2 = runExpr(operand.cdr().car());
			}
			else
				def2 = runExpr((operand.cdr()).car());

			hashTable.put(def1, def2); //테이블에 추가
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
		//idNode가 있는 리스트노드일 경우
		if(list.cdr().car() instanceof ListNode && ((ListNode)list.cdr().car()).car() instanceof IdNode) {
			Node l1= runExpr(((ListNode)list.cdr().car()).car());
			ListNode l2 = ListNode.cons(l1,(ListNode)((ListNode)list.cdr().car()).cdr());
			n1 = ((IntNode)runExpr(l2)).getValue();
		}
		else n1 = ((IntNode)runExpr(list.cdr().car())).getValue(); //operator를 제외하고 첫번째에 있는 숫자
		int n2 = ((IntNode)runExpr(list.cdr().cdr().car())).getValue(); //operator를 제외하고 두번째에 있는 숫자
		if(array2 != null) { //array2가 null이 아니라는 것은 array2에 값이 있다는 것이므로 이미 위에서 사용을 하였으니 값을 null 시켜준다. 
			array2 = null; array[1].node = null;
		}
		else {
			if(array1 != null) { //array1이 null이 아니라는 것은 array1에 값이 있다는 것이므로 이미 위에서 사용을 하였으니 값을 null 시켜준다. 
				array1 = null; array[0].node = null;
			}
		}

		switch(operator.binType) {
		case PLUS: //두 숫자를 더한 값을 출력시킨다.
			return new IntNode(Integer.toString(n1+n2));
		case MINUS: //두 숫자를 뺀 값을 출력시킨다.
			return new IntNode(Integer.toString(n1-n2));
		case TIMES: //두 숫자를 곱한 값을 출력시킨다.
			return new IntNode(Integer.toString(n1*n2));
		case DIV: //첫번째 숫자를 두번째 숫자로 나눈 값을 출력시킨다. 
			return new IntNode(Integer.toString(n1/n2));
		case LT: //첫번째 숫자가 두번째 숫자보다 작을때 true, 아니면 false
			if(n1<n2) return BooleanNode.TRUE_NODE;
			else return BooleanNode.FALSE_NODE;
		case GT: //첫번째 숫자가 두번째 숫자보다 클 때 true, 아니면 false
			if(n1>n2) return BooleanNode.TRUE_NODE;
			else return BooleanNode.FALSE_NODE;
		case EQ: //두 숫자의 값일 같을때 true, 아니면 false
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
