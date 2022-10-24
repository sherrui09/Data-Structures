import java.util.*;

public class UnsortedPriorityQueue<K, V> {
    public static class Entry<K, V> {
        // Instance vars
        public K k;
        public V v;

        // Constructor
        public Entry(K key, V value) {
            k = key;
            v = value;
        }
    }

    // Instance var
    private Comparator<K> comp;
    private DoublyLinkedList<Entry<K, V>> list = new DoublyLinkedList<>();

    // Constructors
    public UnsortedPriorityQueue(Comparator<K> comp) {
        this.comp = comp;
    }
    public UnsortedPriorityQueue() {
        this(new DefaultComparator<>());
    }

    // Methods
    public DoublyLinkedList.Node<Entry<K, V>> findMin() { // O(n)
        DoublyLinkedList.Node<Entry<K, V>> min = list.head.next;
        DoublyLinkedList.Node<Entry<K, V>> cur = list.head.next;
        while (cur != null) {
            if (comp.compare(cur.element.k, min.element.k) < 0) {
                min = cur;
            }
            cur = cur.next;
        }
        return min;
    }

    public Entry<K, V> min() { // O(n)
        if (list.size == 0) {
            return null;
        }
        return findMin().element;
    }

    public Entry<K,V> removeMin() { // O(n)
        if (list.size == 0) {
            return null;
        }
        return list.delete(findMin());
    }

    public Entry<K,V> insert(K k, V v) { // O(1)
        Entry<K,V> entry = new Entry<>(k, v);
        list.insert(entry, list.tail.previous, list.tail);
        return entry;
    }
    public static void main(String[] args) {
        UnsortedPriorityQueue<Integer, String> test = new UnsortedPriorityQueue<>();
        test.insert(1, "1");
        test.insert(0, "0");
        test.insert(2, "2");
        System.out.println(test.list.size);
    }
}
