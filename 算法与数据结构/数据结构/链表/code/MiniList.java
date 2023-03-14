public class MiniList<T> {
	private ListNode<T> head = new ListNode<T>(null, null);
	public void arrayToList(T[] array){
		ListNode<T> p = head;
		for(T t : array){
			ListNode<T> node = new ListNode<T>(t, null);
			p.next = node;
			p = node;
		}
	}
	
	public void printList(){
		ListNode<T> p = head.next;
		while(p != null){
			System.out.print(p.value + " ");
			p = p.next;
		}
		System.out.println();
	}
	/**
	 * 在第index节点后插入value，index节点从0开始
	 * @param index
	 * @param value
	 */
	public void insert(int index, T value){
		ListNode<T> p = head;
		for(int i = 0;i<=index;i++){
			p = p.next; // 循环，直到找到 第 index 个节点
		}
		ListNode<T> node = new ListNode<T>(value, null);
		node.next = p.next;
		p.next = node;
	}
	
	public T remove(int index){
		ListNode<T> pre = head;
		for(int i = 0;i<index;i++){
			pre = pre.next;
		}
		ListNode<T> p = pre.next;
		pre.next = p.next;
		return p.value;
	}
	
	public T get(int index){
		ListNode<T> p = head;
		for(int i = 0;i<=index;i++){
			p = p.next; // 循环，直到找到 第 index 个节点
		}
		return p.value;
	}
	public void set(int index, T value){
		ListNode<T> p = head;
		for(int i = 0;i<=index;i++){
			p = p.next; // 循环，直到找到 第 index 个节点
		}
		p.value = value;
	}
}	
