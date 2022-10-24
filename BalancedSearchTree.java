import java.util.*;
public class BalancedSearchTree<K,V> {
    protected static class Node<E>{
        public E element;
        public int height=0;
        public Node<E> parent;
        public Node<E> left;
        public Node<E> right;
        public Node(E e, Node<E> above, Node<E> leftChild, Node<E> rightChild){
            element = e;
            parent = above;
            left = leftChild;
            right = rightChild;
        }
    }
    protected Comparator<K> comp;
    protected Node<Entry<K,V>> root = null;
    private int size = 0;
    public BalancedSearchTree(){comp = new DefaultComparator<K>();}
    public int size(){return size;}
    public Node<Entry<K,V>> root(){return root;}
    public int numChildren(Node<Entry<K,V>> p){
        int count = 0;
        if(p.left != null)
            count++;
        if(p.right != null)
            count++;
        return count;
    }
    public boolean isInternal(Node<Entry<K,V>> p) { return numChildren(p) > 0; }
    public boolean isExternal(Node<Entry<K,V>> p) { return numChildren(p) == 0; }
    public boolean isRoot(Node<Entry<K,V>> p) { return p == root( ); }
    public boolean isEmpty( ) { return size( ) == 0; }
    public Node<Entry<K,V>> addLeft(Node<Entry<K,V>> p, Entry<K,V> e){
        if(p.left != null)
            throw new IllegalArgumentException("p already has a left child");
        Node<Entry<K,V>> child = new Node<>(e,p,null,null);
        p.left = child;
        p.height=Math.max(1,p.height);
        size++;
        return child;
    }
    public Node<Entry<K,V>> addRight(Node<Entry<K,V>> p, Entry<K,V> e){
        if(p.right != null)
            throw new IllegalArgumentException("p already has a right child");
        Node<Entry<K,V>> child = new Node<>(e,p,null,null);
        p.right = child;
        p.height=Math.max(1,p.height);
        size++;
        return child;
    }
    public Node<Entry<K,V>> addRoot(Entry<K,V> e){
        if(!isEmpty()) throw new IllegalStateException("Tree is not empty");
        root = new Node<Entry<K,V>>(e,null,null,null);
        size = 1;
        return root;
    }
    public List<Node<Entry<K,V>>> traversal(String typeTraversal){
        List<Node<Entry<K,V>>> snapshot = (List<Node<Entry<K, V>>>) new ArrayList<Node<Entry<K,V>>>();
        if(!isEmpty()){
            if (typeTraversal == "preorder") {preorderSubtree(root,snapshot);}
            else if (typeTraversal == "postorder") {postorderSubtree(root,snapshot);}
            else if (typeTraversal == "inorder") {inorderSubtree(root,snapshot);}
            else {breathfirst(snapshot);}
        }
        return snapshot;
    }
    protected void preorderSubtree(Node<Entry<K,V>> p, List<Node<Entry<K,V>>> snapshot) {
        snapshot.add(p);
        if (p.left != null) preorderSubtree(p.left,snapshot);
        if (p.right != null) preorderSubtree(p.right,snapshot);
    }
    protected void postorderSubtree(Node<Entry<K,V>> p, List<Node<Entry<K,V>>> snapshot) {
        if (p.left != null) postorderSubtree(p.left,snapshot);
        if (p.right != null) postorderSubtree(p.right,snapshot);
        snapshot.add(p);
    }
    protected void inorderSubtree(Node<Entry<K,V>> p, List<Node<Entry<K,V>>> snapshot) {
        if (p.left != null) inorderSubtree(p.left,snapshot);
        snapshot.add(p);
        if (p.right != null) inorderSubtree(p.right,snapshot);

    }
    protected void breathfirst(List<Node<Entry<K,V>>> snapshot){
        if(!isEmpty()){
            LinkedListQueue<Node<Entry<K,V>>> queue = new LinkedListQueue<>();
            queue.enqueue(root);

            while(!queue.isEmpty()){

                Node<Entry<K,V>> current = queue.dequeue();
                snapshot.add(current);
                if (current.left != null) queue.enqueue(current.left);
                if (current.right != null) queue.enqueue(current.right);
            }
        }
    }
    public Entry<K,V> remove(Node<Entry<K,V>> p){
        if(numChildren(p) == 2)
            throw new IllegalArgumentException("p has two children");
        Node<Entry<K,V>> child = (p.left != null ? p.left: p.right);
        if(child != null)
            child.parent = p.parent;
        if(p == root)
            root = child;
        else{
            if (p.parent.left == p) p.parent.left = child;
            if (p.parent.right == p) p.parent.right = child;
        }
        Entry<K,V> temp = p.element;
        p.parent = p;
        p.left = null;
        p.right = null;
        p.element = null;
        size--;
        return temp;
    }
    public void printTree(){

        List<Node<Entry<K,V>>> currentList = traversal("inorder");

        for (Node<Entry<K,V>> c: currentList){
            if(c.element != null) System.out.println(c.element.key);
            else System.out.println(c.element);
            System.out.println(c.height);
        }
    }
    public Node<Entry<K,V>> treeSearch(Node<Entry<K,V>> p, K key){
        if(isExternal(p)) return p;
        if(comp.compare(key,p.element.key)>0) return treeSearch(p.right,key);
        else if(comp.compare(key,p.element.key)<0) return treeSearch(p.left,key);
        else return p;
    }//finally{;return p;}
    //}
    public void expandExternal(Node<Entry<K,V>> p, Entry<K,V> e){
        p.element = e;
        p.left = new Node<Entry<K,V>>(null,p,null,null);
        p.right = new Node<Entry<K,V>>(null,p,null,null);
    }
    public void insert(K key, V value){
        Node<Entry<K,V>> p = treeSearch(root,key);
        if(isExternal(p)){
            Entry<K,V> entry = new Entry<>(key,value);
            expandExternal(p,entry);
        }
        else{
            p.element.value = value;
        }
        rebalance(p);
    }
    public Node<Entry<K,V>> treeMax(Node<Entry<K,V>> p){
        if(isExternal(p.right)) return p;
        return treeMax(p.right);
    }
    public void delete(K key){
        Node<Entry<K,V>> p = treeSearch(root,key);
        if(isExternal(p)) return;
        if(isExternal(p.left) || isExternal(p.right)){
            delete01(p);
        }
        else{
            Node<Entry<K,V>> replacement = treeMax(p.left);
            p.element = replacement.element;
            delete01(replacement);
        }
        if(!isRoot(p))
            rebalance(p.parent);
    }
    public Node<Entry<K,V>> get(K key){
        Node<Entry<K,V>> p = treeSearch(root,key);
        if(isExternal(p)) return null;
        else return p;
    }
    private void delete01(Node<Entry<K,V>> p){
        Node<Entry<K,V>> leaf = (isExternal(p.left) ? p.left : p.right);
        remove(leaf);
        remove(p);
    }
    private void relink(Node<Entry<K,V>> parent, Node<Entry<K,V>> child, boolean makeLeftChild){
        child.parent = parent;
        if(makeLeftChild == true) parent.left = child;
        else
            parent.right =child;

    }
    public void rotate(Node<Entry<K,V>> p) {
        Node<Entry<K,V>> parent = p.parent;
        Node<Entry<K,V>> grandparent = parent.parent;
        if(grandparent != null) {
            relink( grandparent, p, grandparent.left == parent);
        }
        else {
            root = p;
            p.parent = null;
        }
        if(p == parent.left){ // our current example
            relink(parent,p.right,true);
            relink(p,parent,false);
        }
        else{ //mirrored
            relink(parent,p.left,false);
            relink(p,parent,true);
        }
    }
    public Node<Entry<K,V>> restructure(Node<Entry<K,V>> x){
        Node<Entry<K,V>> parent = x.parent;
        Node<Entry<K,V>> grandparent = parent.parent;
// straightline -- > single rotation
// true true
// false false
        if((grandparent.left == parent) == (parent.left == x))
        {
            rotate(parent);
            return parent;
        }
// zigzag --> double rotation
        else{
            rotate(x);
            rotate(x);
            return x;
        }

    }

    protected void recomputeHeight(Node<Entry<K,V>> p){
        p.height = 1+Math.max(p.left.height,p.right.height);
    }
    protected Node<Entry<K,V>> tallerChild(Node<Entry<K,V>> p) {
        if(p.left.height > p.right.height) return p.left;
        else if(p.left.height < p.right.height) return p.right;
        else {
            if (p.parent == null) return p.left;
            if(p == p.parent.left) return p.left;
            else return p.right;
        }
    }

    protected void rebalance(Node<Entry<K,V>> p){
        int oldHeight, newHeight;
        do{
            oldHeight = p.height;
            if(p.left.height > p.right.height + 1 || p.left.height < p.right.height - 1) {
                p = restructure(tallerChild(tallerChild(p)));
                recomputeHeight(p.left);
                recomputeHeight(p.right);

            }
            else recomputeHeight(p);
            newHeight = p.height;
            p = p.parent;
        } while( oldHeight==newHeight && p != null);

    }

}


