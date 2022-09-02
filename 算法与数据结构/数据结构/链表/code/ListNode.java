public class ListNode {
	public int val;
	public ListNode next;

	public ListNode(int val) {
		super();
		this.val = val;
	}

	public ListNode(int val, ListNode next) {
		super();
		this.val = val;
		this.next = next;
	}

	@Override
	public String toString() {
		return "ListNode [val=" + val + "]";
	}

	public static void printList(ListNode head) {
		ListNode p = head;
		while (p != null) {
			System.out.print(p.val + " ");
			p = p.next;
		}
		System.out.println();
	}

	public static ListNode arrayToList(int[] array) {
		ListNode head = new ListNode(0);
		ListNode p = head;
		for (int value : array) {
			p.next = new ListNode(value);
			p = p.next;
		}
		return head.next;
	}

	/**
	 * 创建约瑟夫环
	 */
	public static ListNode arrayToCircle(int[] array) {
		ListNode head = new ListNode(0);
		ListNode p = head;
		for (int value : array) {
			p.next = new ListNode(value);
			p = p.next;
		}
		p.next = head.next;
		return head.next;
	}

	/**
     * 创建相交的两个链表
     * @param startIndex 相交链表的起始索引
     */
    public static ListNode[] arrayToIntersection(int[] arr1, int[] arr2, int startIndex) {
        ListNode head1 = new ListNode(0);
        ListNode head2 = new ListNode(0);
        int m = arr1.length;
        int n = arr2.length;
        ListNode p1 = head1;
        ListNode p2 = head2;
        ListNode intersection = null;
        for (int i = 0; i < m; i++) {
            ListNode p = new ListNode(arr1[i]);
            p1.next = p;
            p1 = p1.next;
            if (i == startIndex - 1) { // i 从 0 开始计算， startIndex 从 1 开始计算
                intersection = p1;
            }
        }
        for (int j : arr2) {
            ListNode p = new ListNode(j);
            p2.next = p;
            p2 = p2.next;
        }
        p2.next = intersection;
        return new ListNode[]{head1.next, head2.next};
    }
}
