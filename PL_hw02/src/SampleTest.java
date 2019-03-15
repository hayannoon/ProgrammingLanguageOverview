
public class SampleTest {
	public static void main(String[] args) {
// TODO Auto-generated method stub
		RecursionLinkedList list = new RecursionLinkedList();
		RecursionLinkedList list2 = new RecursionLinkedList();
		list.add('a');
		list.add('b');
		list.add('c');
		list.add('d');
		list.add('e');
		System.out.println(list);
		
		list2.add('f');
		list2.add('g');
		list2.add('h');
		System.out.println(list2);
		
		list.add(0, 'z');
		System.out.println(list);
		System.out.println(list.get(4));
		System.out.println(list.remove(0));
		System.out.println(list);
		
		list.reverse();
		System.out.println(list);
		
		list.addAll(list2);
		System.out.println(list);
	}
}

