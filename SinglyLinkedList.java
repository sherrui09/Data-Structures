public class SinglyLinkedList<E> {
    private static class Node<E> {
        // Instance vars
        private E elem;
        private Node<E> next;

        // Constructors
        public Node(E elem, Node<E> next) {
            this.elem = elem;
            this.next = next;
        }
        public Node(E elem) {
            this.elem = elem;
            this.next = null;
        }
    }
    // Instance vars
    private Node<E> head, tail = null;
    private int size = 0;

    // Constructor
    public SinglyLinkedList() {};

    // Methods
    public boolean isEmpty() {
        return size == 0;
    }

    public void addHead(E elem) { // O(1)
        Node<E> node = new Node<>(elem);
        if (isEmpty()) {
            head = tail = node;
        } else {
            node.next = head;
            head = node;
        }
        size++;
    }

    public void addTail(E elem) { // O(1)
        Node<E> node = new Node<>(elem);
        if (isEmpty()) {
            head = tail = node;
        } else {
            tail.next = node;
            tail = node;
        }
        size++;
    }

    public void removeHead() throws IndexOutOfBoundsException { // O(1)
        if (isEmpty()) {
            throw new IndexOutOfBoundsException();
        }
        head = head.next;
        size--;
    }

    public void removeTail() throws IndexOutOfBoundsException { // O(n)
        if (isEmpty()) {
            throw new IndexOutOfBoundsException();
        }
        if (size == 1) {
            head = tail = null;
        } else {
            Node<E> temp = head;
            while (temp.next != tail) {
                temp = temp.next;
            }
            tail = temp;
            temp.next = null;
        }
        size--;
    }

    public void remove(E elem) throws IndexOutOfBoundsException { // O(n)
        if (isEmpty()) {
            throw new IndexOutOfBoundsException();
        }
        if (size == 1) {
            head = tail = null;
            size--;
        } else {
            Node<E> cur = head;
            Node<E> prev = null;
            while (cur.elem != elem) {
                prev = cur;
                cur = cur.next;
            }
            if (cur == tail) {
                removeTail();
            } else {
                prev.next = cur.next;
                size--;
            }
        }
    }

    public void print() {
        if (isEmpty()) {
            System.out.println("[]");
        } else {
            System.out.print("[");
            Node<E> temp = head;
            for (int k = 0; k < size - 1; k++) {
                System.out.print(temp.elem + ", ");
                temp = temp.next;
            }
            System.out.println(tail.elem + "]");
        }
    }

    public static void main(String[] args) {
        SinglyLinkedList<Integer> test = new SinglyLinkedList<>();
        test.addHead(0);
        test.addTail(1);
        test.addTail(2);
        test.addTail(3);
        test.addTail(4);
        test.removeHead();
        test.removeTail();
        test.remove(3);
        test.print();
    }
}
