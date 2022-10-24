public class Partition<E> {
    protected static class Node<E>{
        public E element;
        public Node<E> parent;
        public int size;
        public Node(E e){
            element = e;
            parent = this;
            size = 1;
        }
    }
    public Node<E> makeComponent(E e){
        return new Node<E>(e);
    }
    public Node<E> find(Node<E> p){
        if(p != p.parent){
            p.parent = find(p.parent);
        }
        return p.parent;
    }
    public void union(Node<E> p, Node<E> q){
        Node<E> a = find(p);
        Node<E> b = find(q);
        if(a!=b){
            if(a.size>b.size){
                b.parent = a;
                a.size += b.size;
            }
            else{
                a.parent = b;
                b.size += a.size;
            }
        }
    }
}
