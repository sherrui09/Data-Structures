import java.util.*;

public class SortedPriorityQueue<K, V> {
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
    public SortedPriorityQueue(Comparator<K> comp) {
        this.comp = comp;
    }
    public SortedPriorityQueue() {
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

    public Entry<K, V> min() { // O(1)
        if (list.size == 0) {
            return null;
        }
        return list.head.next.element;
    }

    public Entry<K,V> removeMin() { // O(1)
        if (list.size == 0) {
            return null;
        }
        return list.delete(list.head.next);
    }

    public Entry<K,V> insert(K k, V v) { // O(n)
        Entry<K,V> entry = new Entry<>(k, v);
        DoublyLinkedList.Node<Entry<K,V>> cur = list.tail.previous;
        while (cur.previous != null && comp.compare(entry.k, cur.element.k) < 0) {
            cur = cur.previous;
        }
        list.insert(entry, cur, cur.next);
        return entry;
    }
    public static void main(String[] args) {
        SortedPriorityQueue<Integer, String> test = new SortedPriorityQueue<>();
        test.insert(1, "1");
        test.insert(0, "0");
        test.removeMin();
        System.out.println(test.list.size);
        System.out.println(test.min().v);
    }
}
