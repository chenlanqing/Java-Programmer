package question;

/**
 * ◆题:实现一个链表LinkedList,要求使用链表机构实现,
 * 并提供相应的add(Object o),remove(Object o)这两个方法.
 * 
 */
public class LinkedList{
	//链表的头
	private Node head;
	//元素的总数
	private int size;
	
	public LinkedList(){
		
	}
	/**
	 * 向集合中添加元素
	 * @param o
	 */
	public void add(Object o){
		/**
		 * 首先查看是否包含头节点,若没有则说明当前集合没有元素;
		 */
		if(head == null){
			//创建第一个节点
			head = new Node();
			//使该节点保存当前add()方法要求保存的元素;
			head.setElement(o);
			//设置链表关系
			head.setNext(head);
			head.setPrev(head);
		}else{
			//向集合中添加一个新元素
			//创建节点
			Node newEnd = new Node();
			newEnd.setElement(o);
			//建立连接关系;
			Node oldEnd = head.getPrev();
			oldEnd.setNext(newEnd);
			newEnd.setPrev(oldEnd);
			
			newEnd.setNext(head);
			head.setPrev(newEnd);
		}
		size++;
	}
	/**
	 * 获取集合元素的数量
	 * @return
	 */
	public int size(){
		return size;
	}
	/**
	 * 根据给定的下标获取对应的元素
	 * @param index
	 * @return
	 */
	public Object get(int index){
		//判断index是否越界
		if(index < 0 || index >= size){
			throw new RuntimeException("下标越界");
		}
		Node target = head;
		for(int i=0;i<index;i++){
			target = target.getNext();
		}
		return target.getElement();		
	}
	/**
	 * toString()方法
	 * [,,,,,,,]
	 */
	public String toString(){
		StringBuilder builder = new StringBuilder("[");
		for(int i=0;i<size;i++){
			builder.append(get(i).toString());
			builder.append(", ");
		}
		builder.delete(builder.length()-2,builder.length());
		builder.append("]");
		return builder.toString();
	}
	/**
	 * 删除给定的元素
	 * @param o
	 * @return
	 */
	public boolean remove(Object o){
		//是否有头节点
		if(head == null){
			return false;
		}else{
			/**
			 * 思路:从头节点开始逐一判断给定元素是否与当前节点保存的元素equals为true,
			 * 若是,删除当前节点,重新建立链表关系;
			 * 若没有,链表寻找一遍之后仍然没找到,则返回false;
			 */
			Node current = head;
			do{	
				//查看当前节点保存的元素是否为要删除的元素
				if(current.getElement().equals(o)){
					//删除并重新建立连接
					current.getPrev().setNext(current.getNext());
					current.getNext().setPrev(current.getPrev());
					size--;
					return true;
				}
				current = current.getNext();
			}while(current != head);
			return false;
		}
		
	}
	/**
	 * 链表内部节点类
	 */
	private class Node{
		//此节点的上一个节点的引用
		private Node prev;
		//此节点的下一个节点的引用
		private Node next;
		//此节点要保存的元素
		private Object element;
		public Node getPrev() {
			return prev;
		}
		public void setPrev(Node prev) {
			this.prev = prev;
		}
		public Node getNext() {
			return next;
		}
		public void setNext(Node next) {
			this.next = next;
		}
		public Object getElement() {
			return element;
		}
		public void setElement(Object element) {
			this.element = element;
		}		
	}
	
}
