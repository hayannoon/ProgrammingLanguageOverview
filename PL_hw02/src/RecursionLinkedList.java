public class RecursionLinkedList {
	private Node head; // head ��带 ������ �������� ����
	private static char UNDEF = Character.MIN_VALUE; // ��� ������ item���� �ʱ�ȭ��ų static ���� ����

	/**
	 * ���Ӱ� ������ ��带 ����Ʈ�� ó������ ����
	 */
	private void linkFirst(char element) {
		head = new Node(element, head); // ����Ʈ�� ��������� ���ο� ��带 ����� ���� ����
	}

	/**
	 * ���� (1) �־��� Node x �� ���������� ����� Node �� �������� ���Ӱ� ������ ��带 ����
	 *
	 * @param element ������
	 * @param x       ���
	 */
	private void linkLast(char element, Node x) {
		// ä���� ���, recursion ���

		if (x.next == null) { // ��� Ż�� ����(x�� next�� null �� ���)
			x.next = new Node(element, null);// x�� next�� ���ο� ��� �����ؼ� x�� next�� ���δ�.
		} else
			linkLast(element, x.next); // ���� �������� ���ȣ��
	}

	/**
	 * ���� Node �� ���� Node �� ���Ӱ� ������ ��带 ����
	 *
	 * @param element ����
	 * @param pred    �������
	 */
	private void linkNext(char element, Node pred) { // ���� ��� ���̿� ���ο� ��带 �����ִ´�.
		Node next = pred.next; // pred�� ������带 ����
		pred.next = new Node(element, next); // pred�� ������带 ���ο� ���� ����(���ο� ����� next�� ������ pred.nexet�� ����)
	}

	/**
	 * ����Ʈ�� ù��° ���� ����(����)
	 *
	 * @return ù��° ������ ������
	 */
	private char unlinkFirst() { // ù��°���� ���� �Լ�
		Node x = head; // ������ ��� ����
		char element = x.item; // ��ȯ�� char�� element ����
		head = head.next; // head�� head�� �������� ����
		x.item = UNDEF; // ������ ��� item �ʱ�ȭ
		x.next = null; // ������ ��� next �ʱ�ȭ
		return element; // ������ element ��ȯ
	}

	/**
	 * ���� Node �� ���� Node ���� ����(����)
	 *
	 * @param pred �������
	 * @return ��������� ������
	 */
	private char unlinkNext(Node pred) { // pred��� �������� ����
		Node x = pred.next; // ������ ��� ����
		Node next = x.next; // ������ ����� ������� ����
		char element = x.item; // ������ ����� element�� ����
		x.item = UNDEF; // ������ ��� item �ʱ�ȭ
		x.next = null; // ������ ��� next �ʱ�ȭ
		pred.next = next; // pred�� next�� ������ ����� �������� ����
		return element; // ������ ��� element ��ȯ
	}

	/**
	 * ���� (2) x ��忡�� index ��ŭ ������ Node ��ȯ
	 */
	private Node node(int index, Node x) {
// ä���� ���, recursion ���
		if (index == 1) { // next ���� 1�̸� �ٷ� ������� ��ȯ
			return x.next;
		}
		return node(index - 1, x.next); // next�� 1�� �ƴϸ� -1�ؼ� ���ȣ��

	}

	/**
	 * ���� (3) ���κ��� �������� ����Ʈ�� ��� ���� ��ȯ
	 */
	private int length(Node x) { // ��� ���� ��ȯ
// ä���� ���, recursion ���
		if (x == null)
			return 0; // x�� null�̸� ���̰� 0�̴�.

		return length(x.next) + 1; // ���� ��尡 ���������� ��ȯ�� 1������Ű�� ���ȣ��
	}

	/**
	 * ���� (4) ���κ��� �����ϴ� ����Ʈ�� ���� ��ȯ
	 */
	private String toString(Node x) {
// ä���� ���, recursion ���
		if (x == null)
			return " "; // x�� null�̸� �����ȯ

		return String.valueOf(x.item) + toString(x.next); // char���� String type���� �ٲٰ� ���ȣ���ؼ� ���ڿ��� �����Ѵ�.
	}

	/**
	 * �߰� ���� (5) ���� ����� ���� ������ ����Ʈ�� �������� �Ųٷ� ���� ex)��尡 [s]->[t]->[r]�� ��, reverse ����
	 * �� [r]->[t]->[s]
	 * 
	 * @param x    ���� ���
	 * @param pred �������� ���� ���
	 */
	private void reverse(Node x, Node pred) { // ����Ʈ�� �������ִ� �Լ�(�Ű������� ���ӵ� ��� 2���� �޾ƿ´�.)
// ä���� ���, recursion ���

		if (x == null) { // ��� x�� null�̶�� ����Ʈ�� ���� �����Ѱ��̹Ƿ�, ��带 �������ְ� �Լ��� �������´�.
			this.head = pred;
			return;
		}

		reverse(x.next, x); // null�� �ƴ϶�� ����Ʈ�� �ڿ� �� ���ұ⶧���� next�� ���ڷ� �־ ���ȣ���Ѵ�.
		x.next = pred;
	}

	/**
	 * ����Ʈ�� �Ųٷ� ����
	 */
	public void reverse() { // reverse�� ���� ȣ�����ִ� �Լ�(�����ε�)
		reverse(head, null);
	}

	/**
	 * �߰� ���� (6) �� ����Ʈ�� ��ħ ( A + B ) ex ) list1 =[l]->[o]->[v]->[e] , list2=[p]->[l]
	 * �� ��, list1.addAll(list2) ���� �� [l]->[o]->[v]->[e]-> [p]->[l]
	 * 
	 * @param x list1�� ���
	 * @param y list2 �� head
	 */
	private void addAll(Node x, Node y) { // ��� 2���� ���ڷ� �޾Ƽ� �̾��ִ��Լ�(��������� ����Ʈ�� �̾����� �ȴ�.)
// ä���� ���, recursion ���
		if (x.next == null) { // next�� null�̶��, ����Ʈ�� ���� �����Ѱ��̹Ƿ� next�� y���� �������ش�.(����� ���������� �ȴ�.)
			x.next = y;
		} else
			addAll(x.next, y); // null�� �ƴ϶�� ���ڿ� next�� �ְ� ���ȣ�����ش�.
	}

	/**
	 * �� ����Ʈ�� ��ħ ( this + B )
	 */
	public void addAll(RecursionLinkedList list) {
		addAll(this.head, list.head); // addAll�� ���� ȣ���ϴ� �Լ�
	}

	/**
	 * ���Ҹ� ����Ʈ�� �������� �߰�
	 */
	public boolean add(char element) { // add�Լ�
		if (head == null) { // head�� null�̶��(����Ʈ�� ����ִٸ�)
			linkFirst(element); // linkFirst�� ȣ���Ѵ�.
		} else {
			linkLast(element, head); // null�� �ƴ϶�� linkLast�� ȣ���Ѵ�.
		}
		return true;
	}

	/**
	 * ���Ҹ� �־��� index ��ġ�� �߰�
	 *
	 * @param index   ����Ʈ���� �߰��� ��ġ
	 * @param element �߰��� ������
	 */
	public void add(int index, char element) { // ���ڿ� index�������� �־� ���° �ε����� � ��带 �����ϴ� �Լ�(�����ε�)
		if (!(index >= 0 && index <= size())) // ����ó��
			throw new IndexOutOfBoundsException("" + index);
		if (index == 0) // index�� 0�̸� �� �տ� �����ϴ°��̱⶧���� linkFirst�Լ��� ȣ���Ѵ�.
			linkFirst(element);
		else // 0�� �ƴ϶�� linkNext�� ���� �����̿� ��带 �����Ѵ�.
			linkNext(element, node(index - 1, head));
	}

	/**
	 * ����Ʈ���� index ��ġ�� ���� ��ȯ
	 */
	public char get(int index) { // index������ �޾Ƽ� �� item���� ��ȯ�ϴ� �Լ�
		if (!(index >= 0 && index < size())) // ����ó��
			throw new IndexOutOfBoundsException("" + index);
		return node(index, head).item; // node�Լ��� ȣ���ؼ� ��带 ã��, �� item���� ��ȯ�Ѵ�.
	}

	/**
	 * ����Ʈ���� index ��ġ�� ���� ����
	 */
	public char remove(int index) { // index�� �޾Ƽ� �� ���Ҹ� �����ϴ� �Լ�
		if (!(index >= 0 && index < size())) // ����ó��
			throw new IndexOutOfBoundsException("" + index);
		if (index == 0) { // index�� 0�̶��
			return unlinkFirst(); // unlinkFirst �Լ��� ȣ���ϰ�
		}
		return unlinkNext(node(index - 1, head));
	} // 0�� �ƴ϶�� unlinkNext�Լ��� ȣ���Ѵ�.

	/**
	 * ����Ʈ�� ���� ���� ��ȯ
	 */
	public int size() { // length�Լ��� ȣ���ؼ� head���� �̾��� ����� ������ ��ȯ�Ѵ�.
		return length(head);
	}

	@Override
	public String toString() { // ��� ����� ¥������ �������̵��� �Լ�
		if (head == null)
			return "[]";
		return "[ " + toString(head) + "]";
	} // ��ο���� String���� ��ȯ���ش�.

	/**
	 * ����Ʈ�� ���� �ڷᱸ��
	 */
	private static class Node { // Node Ŭ����
		char item; // char�� ������ ���� item
		Node next; // next Node�� ������ ���� next

		Node(char element, Node next) { // ������
			this.item = element; // item�� �ʱ�ȭ
			this.next = next; // next�� �ʱ�ȭ
		}
	}
}