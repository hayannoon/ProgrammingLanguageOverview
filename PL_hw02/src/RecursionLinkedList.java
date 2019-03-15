public class RecursionLinkedList {
	private Node head; // head 노드를 저장할 지역변수 선언
	private static char UNDEF = Character.MIN_VALUE; // 노드 삭제시 item값을 초기화시킬 static 변수 선언

	/**
	 * 새롭게 생성된 노드를 리스트의 처음으로 연결
	 */
	private void linkFirst(char element) {
		head = new Node(element, head); // 리스트가 비어있을때 새로운 노드를 만들고 헤드로 설정
	}

	/**
	 * 과제 (1) 주어진 Node x 의 마지막으로 연결된 Node 의 다음으로 새롭게 생성된 노드를 연결
	 *
	 * @param element 데이터
	 * @param x       노드
	 */
	private void linkLast(char element, Node x) {
		// 채워서 사용, recursion 사용

		if (x.next == null) { // 재귀 탈출 조건(x의 next가 null 일 경우)
			x.next = new Node(element, null);// x의 next로 새로운 노드 생성해서 x의 next로 붙인다.
		} else
			linkLast(element, x.next); // 조건 미충족시 재귀호출
	}

	/**
	 * 이전 Node 의 다음 Node 로 새롭게 생성된 노드를 연결
	 *
	 * @param element 원소
	 * @param pred    이전노드
	 */
	private void linkNext(char element, Node pred) { // 노드와 노드 사이에 새로운 노드를 끼워넣는다.
		Node next = pred.next; // pred의 다음노드를 저장
		pred.next = new Node(element, next); // pred의 다음노드를 새로운 노드로 저장(새로운 노드의 next는 기존의 pred.nexet로 저장)
	}

	/**
	 * 리스트의 첫번째 원소 해제(삭제)
	 *
	 * @return 첫번째 원소의 데이터
	 */
	private char unlinkFirst() { // 첫번째원소 삭제 함수
		Node x = head; // 삭제할 노드 설정
		char element = x.item; // 반환할 char형 element 저장
		head = head.next; // head를 head의 다음노드로 설정
		x.item = UNDEF; // 삭제할 노드 item 초기화
		x.next = null; // 삭제할 노드 next 초기화
		return element; // 삭제한 element 반환
	}

	/**
	 * 이전 Node 의 다음 Node 연결 해제(삭제)
	 *
	 * @param pred 이전노드
	 * @return 다음노드의 데이터
	 */
	private char unlinkNext(Node pred) { // pred노드 다음연결 삭제
		Node x = pred.next; // 삭제할 노드 저장
		Node next = x.next; // 삭제할 노드의 다음노드 저장
		char element = x.item; // 삭제할 노드의 element값 저장
		x.item = UNDEF; // 삭제할 노드 item 초기화
		x.next = null; // 삭제할 노드 next 초기화
		pred.next = next; // pred의 next를 삭제할 노드의 다음노드로 설정
		return element; // 삭제한 노드 element 반환
	}

	/**
	 * 과제 (2) x 노드에서 index 만큼 떨어진 Node 반환
	 */
	private Node node(int index, Node x) {
// 채워서 사용, recursion 사용
		if (index == 1) { // next 값이 1이면 바로 다음노드 반환
			return x.next;
		}
		return node(index - 1, x.next); // next가 1이 아니면 -1해서 재귀호출

	}

	/**
	 * 과제 (3) 노드로부터 끝까지의 리스트의 노드 갯수 반환
	 */
	private int length(Node x) { // 노드 길이 반환
// 채워서 사용, recursion 사용
		if (x == null)
			return 0; // x가 null이면 길이가 0이다.

		return length(x.next) + 1; // 뒤의 노드가 남아있으면 반환값 1증가시키고 재귀호출
	}

	/**
	 * 과제 (4) 노드로부터 시작하는 리스트의 내용 반환
	 */
	private String toString(Node x) {
// 채워서 사용, recursion 사용
		if (x == null)
			return " "; // x가 null이면 공백반환

		return String.valueOf(x.item) + toString(x.next); // char값을 String type으로 바꾸고 재귀호출해서 문자열을 생성한다.
	}

	/**
	 * 추가 과제 (5) 현재 노드의 이전 노드부터 리스트의 끝까지를 거꾸로 만듬 ex)노드가 [s]->[t]->[r]일 때, reverse 실행
	 * 후 [r]->[t]->[s]
	 * 
	 * @param x    현재 노드
	 * @param pred 현재노드의 이전 노드
	 */
	private void reverse(Node x, Node pred) { // 리스트를 뒤집어주는 함수(매개변수로 연속된 노드 2개를 받아온다.)
// 채워서 사용, recursion 사용

		if (x == null) { // 노드 x가 null이라면 리스트의 끝에 도달한것이므로, 헤드를 설정해주고 함수를 빠져나온다.
			this.head = pred;
			return;
		}

		reverse(x.next, x); // null이 아니라면 리스트가 뒤에 더 남았기때문에 next를 인자로 넣어서 재귀호출한다.
		x.next = pred;
	}

	/**
	 * 리스트를 거꾸로 만듬
	 */
	public void reverse() { // reverse를 최초 호출해주는 함수(오버로딩)
		reverse(head, null);
	}

	/**
	 * 추가 과제 (6) 두 리스트를 합침 ( A + B ) ex ) list1 =[l]->[o]->[v]->[e] , list2=[p]->[l]
	 * 일 때, list1.addAll(list2) 실행 후 [l]->[o]->[v]->[e]-> [p]->[l]
	 * 
	 * @param x list1의 노드
	 * @param y list2 의 head
	 */
	private void addAll(Node x, Node y) { // 노드 2개를 인자로 받아서 이어주는함수(결과적으로 리스트가 이어지게 된다.)
// 채워서 사용, recursion 사용
		if (x.next == null) { // next가 null이라면, 리스트의 끝에 도달한것이므로 next를 y노드로 설정해준다.(재귀의 종료조건이 된다.)
			x.next = y;
		} else
			addAll(x.next, y); // null이 아니라면 인자에 next를 넣고 재귀호출해준다.
	}

	/**
	 * 두 리스트를 합침 ( this + B )
	 */
	public void addAll(RecursionLinkedList list) {
		addAll(this.head, list.head); // addAll을 최초 호출하는 함수
	}

	/**
	 * 원소를 리스트의 마지막에 추가
	 */
	public boolean add(char element) { // add함수
		if (head == null) { // head가 null이라면(리스트가 비어있다면)
			linkFirst(element); // linkFirst를 호출한다.
		} else {
			linkLast(element, head); // null이 아니라면 linkLast를 호출한다.
		}
		return true;
	}

	/**
	 * 원소를 주어진 index 위치에 추가
	 *
	 * @param index   리스트에서 추가될 위치
	 * @param element 추가될 데이터
	 */
	public void add(int index, char element) { // 인자에 index정보까지 넣어 몇번째 인덱스에 어떤 노드를 삽입하는 함수(오버로딩)
		if (!(index >= 0 && index <= size())) // 예외처리
			throw new IndexOutOfBoundsException("" + index);
		if (index == 0) // index가 0이면 맨 앞에 삽입하는것이기때문에 linkFirst함수를 호출한다.
			linkFirst(element);
		else // 0이 아니라면 linkNext로 노드와 노드사이에 노드를 삽입한다.
			linkNext(element, node(index - 1, head));
	}

	/**
	 * 리스트에서 index 위치의 원소 반환
	 */
	public char get(int index) { // index정보를 받아서 그 item값을 반환하는 함수
		if (!(index >= 0 && index < size())) // 예외처리
			throw new IndexOutOfBoundsException("" + index);
		return node(index, head).item; // node함수를 호출해서 노드를 찾고, 그 item값을 반환한다.
	}

	/**
	 * 리스트에서 index 위치의 원소 삭제
	 */
	public char remove(int index) { // index를 받아서 그 원소를 삭제하는 함수
		if (!(index >= 0 && index < size())) // 예외처리
			throw new IndexOutOfBoundsException("" + index);
		if (index == 0) { // index가 0이라면
			return unlinkFirst(); // unlinkFirst 함수를 호출하고
		}
		return unlinkNext(node(index - 1, head));
	} // 0이 아니라면 unlinkNext함수를 호출한다.

	/**
	 * 리스트의 원소 갯수 반환
	 */
	public int size() { // length함수를 호출해서 head부터 이어진 노드의 개수를 반환한다.
		return length(head);
	}

	@Override
	public String toString() { // 출력 양식을 짜기위해 오버라이딩한 함수
		if (head == null)
			return "[]";
		return "[ " + toString(head) + "]";
	} // 요로요로케 String값을 반환해준다.

	/**
	 * 리스트에 사용될 자료구조
	 */
	private static class Node { // Node 클래스
		char item; // char값 저장할 변수 item
		Node next; // next Node값 저장할 변수 next

		Node(char element, Node next) { // 생성자
			this.item = element; // item값 초기화
			this.next = next; // next값 초기화
		}
	}
}