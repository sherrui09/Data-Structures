import java.util.Comparator;
import java.util.PriorityQueue;

public class HeapPriorityQueue<K, V> {
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
    // Instance vars
    public ArrayList<Entry<K, V>> heap = new ArrayList<>();
    public Comparator<K> comp;

    // Constructors
    public HeapPriorityQueue(Comparator<K> comp) {
        this.comp = comp;
    }
    public HeapPriorityQueue() {
        this(new DefaultComparator<>());
    }

    // Methods
    public int leftchild(int j) { // Return index of left child of element at int j
        return 2 * j + 1;
    }
    public int rightchild(int j) { // Return index of right child of element at int j
        return 2 * j + 2;
    }
    public int parent(int j) { // Return the index of the parent of element at int j
        return (j - 1) / 2;
    }
    public boolean hasLeft(int j) {
        return leftchild(j) < heap.size();
    }
    public boolean hasRight(int j) {
        return rightchild(j) < heap.size();
    }
    public void swap(int i, int j) {
        Entry<K,V> temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }

    public Entry<K,V> insert(K k, V v) { // O(log n)
        Entry<K,V> entry = new Entry<>(k, v);
        heap.push(entry);
        swim(heap.size() - 1);
        return entry;
    }
    public void swim(int j) {
        while (j > 0) {
            if (comp.compare(heap.get(j).k, heap.get(parent(j)).k) < 0) {
                swap(j, parent(j));
                j = parent(j);
            } else {
                break;
            }
        }
    }

    public Entry<K,V> removeMin() { // O(log n)
        if (heap.size() == 0) {
            return null;
        }
        Entry<K,V> removed = heap.get(0);
        heap.set(0,heap.get(heap.size() - 1));
        heap.remove(heap.size() - 1);
        sink(0);
        return removed;
    }
    public void sink(int j) {
        while (hasLeft(j)) { // stop condition: when current entry is already at bottom
            int minchild = leftchild(j);
            if (hasRight(j)){
                if (comp.compare(heap.get(leftchild(j)).k, heap.get(rightchild(j)).k)> 0) {
                    minchild = rightchild(j);
                }
            }
            if(comp.compare(heap.get(j).k, heap.get(minchild).k) > 0) {
                swap(j, minchild);
                j = minchild;
            } else {
                break;
            }
        }
    }
    public void print() {
        System.out.print("[");
        for (int k = 0; k < heap.size - 1; k++) {
            System.out.print(heap.data[k].k + ", ");
        }
        System.out.println(heap.data[heap.size - 1].k + "]");
    }

    public static void main(String[] args) {
        HeapPriorityQueue<Integer, Integer> test = new HeapPriorityQueue();
        test.insert(5, 5);
        test.insert(4, 4);
        test.insert(7, 7);
        test.insert(1, 1);
        test.removeMin();
        test.insert(3, 3);
        test.insert(0, 0);
        test.removeMin();
        test.removeMin();
        test.insert(8, 8);
        test.removeMin();
        test.insert(2, 2);
        test.print();
    }
}
