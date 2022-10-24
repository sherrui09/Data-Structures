public class ArrayStack<E> {
    // Instance vars
    private int capacity;
    private E[] data;
    private int t = -1;

    // Constructors
    public ArrayStack(int capacity) {
        this.capacity = capacity;
        data = (E[]) new Object[capacity];
    }
    public ArrayStack() {
        capacity = 1000;
        data = (E[]) new Object[capacity];
    }

    // Methods
    public boolean isEmpty() {
        return t == -1;
    }

    public int size() {
        return t + 1;
    }

    public E top() { // O(1)
        if (isEmpty()) {
            return null;
        }
        return data[t];
    }

    public E pop() { // O(1)
        if (isEmpty()) {
            return null;
        }
        E temp = data[t];
        data[t] = null;
        t--;
        return temp;
    }

    public void push(E e) throws IllegalStateException { // O(1)
        if (size() == data.length) {
            throw new IllegalStateException("Stack is full.");
        }
        data[t + 1] = e;
        t++;
    }

    public static void main(String[] args) {
        ArrayStack<Integer> test = new ArrayStack<>();
        test.push(0);
        test.push(1);
        test.push(2);
        test.push(3);
        test.pop();
        System.out.println(test.top());
    }
}
