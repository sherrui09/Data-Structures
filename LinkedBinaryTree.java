import java.util.*;
public class LinkedBinaryTree<K,V> {
    protected static class Node<E>{
        public E element;
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
    public LinkedBinaryTree(){comp = new DefaultComparator<K>();}
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
        size++;
        return child;
    }
    public Node<Entry<K,V>> addRight(Node<Entry<K,V>> p, Entry<K,V> e){
        if(p.right != null)
            throw new IllegalArgumentException("p already has a right child");
        Node<Entry<K,V>> child = new Node<>(e,p,null,null);
        p.right = child;
        size++;
        return child;
    }
    public Node<Entry<K,V>> addRoot(Entry<K,V> e){
        if(!isEmpty()) throw new IllegalStateException("Tree is not empty");
        root = new Node<Entry<K,V>>(e,null,null,null);
        size = 1;
        return root;
    }
    public int depth(Node<Entry<K,V>> p){
        if (p.parent==null) return 0;
        return depth(p.parent)+1;
    }
    public int height(Node<Entry<K,V>> p){
        int maxH = 0;
        if(p.left != null) maxH = height(p.left);
        if(p.right !=null) {
            int rightH = height(p.right);
            if(maxH <rightH) maxH = rightH;
        }
        return maxH+1;
    }
    public List<Node<Entry<K,V>>> traversal(String typeTraversal){
        List<Node<Entry<K,V>>> record = (List<Node<Entry<K, V>>>) new ArrayList<Node<Entry<K,V>>>();
        if(!isEmpty()){
            if (typeTraversal == "preorder") {preorderSubtree(root,record);}
            else if (typeTraversal == "postorder") {postorderSubtree(root,record);}
            else if (typeTraversal == "inorder") {inorderSubtree(root,record);}
            else {breathfirst(record);}
        }
        return record;
    }
    protected void breathfirst(List<Node<Entry<K,V>>> record){

    }
    protected void preorderSubtree(Node<Entry<K,V>> p, List<Node<Entry<K,V>>> record) {
        record.add(p);//visit root
        //go into left child's branch
        if(p.left != null) preorderSubtree(p.left,record);
        //go into right child's branch
        if(p.right != null) preorderSubtree(p.right,record);
    }
    protected void postorderSubtree(Node<Entry<K,V>> p, List<Node<Entry<K,V>>> record) {
        // leftsubtree
        if(p.left != null)  postorderSubtree(p.left,record);
        // rightsubtree
        if(p.right != null) postorderSubtree(p.right, record);
        // root
        record.add(p);
    }
    protected void inorderSubtree(Node<Entry<K,V>> p, List<Node<Entry<K,V>>> record) {
        if(p.left != null) inorderSubtree(p.left,record);
        record.add(p);
        if(p.right != null) inorderSubtree(p.right, record);
    }



    public Node<Entry<K,V>> treeSearch(Node<Entry<K,V>> p, K key){
        // terminal: 
        if(isExternal(p)) return p;
        // key > root's key
        if(comp.compare(key,p.element.key) > 0) {
            return  treeSearch(p.right,key);
        }
        // key < root's key
        else if(comp.compare(key, p.element.key) < 0) {
            return treeSearch(p.left,key);
        }
        // key == root's key
        else {
            return p;
        }
    }
    public Entry<K,V> remove_old(Node<Entry<K,V>> p){
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
            if(c.element != null) System.out.print(c.element.key);
            else System.out.print(c.element);
        }
        int h = height_new(root);
        System.out.print(h);
        System.out.print("here");
        int length = (int) Math.pow(2,h+1)-1;
        List<K> lk = (List<K>) new ArrayList<K>(length);
        //List<K> lk = new ArrayList<>();
        formList(0,root,lk);
        for(int i = 0;i<=h;i++){
            int indexI_1 = (int) Math.pow(2,i)-1;
            int indexI = (int) Math.pow(2,i+1)-1;
            for(int j = indexI_1; j<indexI;j++){
                System.out.print(lk.get(j));
            }
            System.out.print("\n");
        }
    }
    public void formList(int index, Node<Entry<K,V>> p, List<K> lk){
        if(p.element == null) lk.set(index,null);
        else{lk.set(index,p.element.key);
            formList(index*2+1,p.left,lk);
            formList(index*2+2,p.right,lk);}
    }

    //finally{;return p;}
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
    }
    public Node<Entry<K,V>> treeMax(Node<Entry<K,V>> p){
        if(isExternal(p.right)) return p;
        return treeMax(p.right);
    }

    public Node<Entry<K,V>> get(K key){
        Node<Entry<K,V>> p = treeSearch(root,key);
        if(isExternal(p)) return null;
        else return p;
    }

    private void relink(Node<Entry<K,V>> parent, Node<Entry<K,V>> child, boolean makeLeftChild){
        child.parent = parent;
        if(makeLeftChild){
            parent.left = child;
        }
        else
            parent.right = child;
    }
    public void rotate(Node<Entry<K,V>> p){
        // we assume node a has parent as otherwise it is trivial to handle by wrapper.
        Node<Entry<K,V>> q = p.parent;
        Node<Entry<K,V>> grandparent = q.parent;
        if(grandparent == null){
            root = p;
            p.parent = null;
        }
        else{
            relink(grandparent,p,q==grandparent.left);
        }
        if(p == q.left){
            relink(q,p.right,true);
            relink(p,q,false);
        }
        else{
            relink(q,p.left,false);
            relink(p,q,true);
        }
    }
    public Node<Entry<K,V>> restructure(Node<Entry<K,V>> x){
        // we assume node x has parent and grandparent as otherwise it is trivial to handle by wrapper.
        Node<Entry<K,V>> y = x.parent;
        Node<Entry<K,V>> z = y.parent;
        if((x==y.left) == (y==z.left)){
            rotate(y);
            return y;
        }
        else{
            rotate(x);
            rotate(x);
            return x;
        }
    }
    private void splay(Node<Entry<K,V>> p){
        while(p.parent != null) {
            // two node
            if(p.parent.parent == null) {
                rotate(p);
                break;
            }

            // trinode straightline
            if(        (p.parent.left == p) == (p.parent.parent.left == p.parent)){
                rotate(p.parent);
                rotate(p);
            }

            // trinode zigzag
            else {
                rotate(p);
                rotate(p);
            }




        }




    }


    // public void deleteSplay(K key){
    //     Node<Entry<K,V>> p = treeSearch(root,key);
    //     if(isExternal(p)) return;
    //     if(isExternal(p.left) || isExternal(p.right)){
    //         delete01(p);
    //     }
    //     else{
    //         Node<Entry<K,V>> replacement = treeMax(p.left);
    //         p.element = replacement.element;
    //         delete01(replacement);
    //         p = replacement;
    //     }
    //     splay(p.parent);
    // }
    public void insertSplay(K key, V value){
        Node<Entry<K,V>> p = treeSearch(root,key);
        if(isExternal(p)){
            Entry<K,V> entry = new Entry<>(key,value);
            expandExternal(p,entry);
        }
        else{
            p.element.value = value;
        }
        splay(p);
    }
    public void accessSplay(K key){
        Node<Entry<K,V>> p = treeSearch(root,key);
        if(isExternal(p)) p = p.parent;
        if(p!=null) splay(p);
    }
    public void attach(Node<Entry<K,V>> p, LinkedBinaryTree<K,V> t1, LinkedBinaryTree<K,V> t2){
        if (isInternal(p)) throw new IllegalArgumentException("p must be a leaf");
        if(t1 != null){
            p.left = t1.root;
            t1.root = p;
            size += t1.size;
        }
        if(t2 != null){
            p.right = t2.root;
            t2.root = p;
            size += t2.size;
        }
    }
    public Entry<K,V> remove_new(Node<Entry<K,V>> p){
        if(p.left != null && p.right != null) throw new IllegalArgumentException("two children!");
        // let the p.child.parent point to p.parent
        // no child?
        // with child?
        Node<Entry<K,V>> child = null;
        if (p.left != null) child = p.left;
        if (p.right != null) child = p.right;
        if(child != null) child.parent = p.parent;
        // let p.parent.child point to p.child
        // p.parent == null? p.parent != null?
        if(p.parent == null) // namely p is root
            root = child;
        else{
            //p.parent.left =? or p.parent.right =??
            if(p.parent.left == p) p.parent.left = child;
            if(p.parent.right == p) p.parent.right = child;
        }
        // recycle p's space.
        p.parent = null;
        p.left = null;
        p.right = null;
        Entry<K,V> returned = p.element;
        p.element = null;
        size--;
        return returned;
    }
    public int depth_new(Node<Entry<K,V>> p){
        if (p.parent == null) return 0;
        return depth_new(p.parent)+1;
    }
    public int height_new(Node<Entry<K,V>> p){
        int max_height = -1;
        if(p.left != null) max_height = height_new(p.left);
        if(p.right != null) {
            int right_height = height_new(p.right);
            if (max_height < right_height){
                max_height = right_height;
            }
        }
        return max_height+1;
    }

    public static void main(String[] args) {
        LinkedBinaryTree<Integer,String> test = new LinkedBinaryTree<>();
        LinkedBinaryTree.Node<Entry<Integer,String>> root = test.addRoot(new Entry<Integer,String>(8,"a"));
        LinkedBinaryTree.Node<Entry<Integer,String>> n1 = test.addLeft(root, new Entry<Integer,String>(5,"b"));
        LinkedBinaryTree.Node<Entry<Integer,String>> n2 = test.addRight(root, new Entry<Integer,String>(13,"d"));
        LinkedBinaryTree.Node<Entry<Integer,String>> n3 = test.addLeft(n2, new Entry<Integer,String>(10,"c"));
        LinkedBinaryTree.Node<Entry<Integer,String>> n4 = test.addRight(n2, new Entry<Integer,String>(18,"e"));
        LinkedBinaryTree.Node<Entry<Integer,String>> n5 = test.addLeft(n3, new Entry<Integer,String>(9,"f"));
        LinkedBinaryTree.Node<Entry<Integer,String>> n6 = test.addRight(n3, new Entry<Integer,String>(12,"g"));
        LinkedBinaryTree.Node<Entry<Integer,String>> n7 = test.addLeft(n4, new Entry<Integer,String>(15,"h"));
        LinkedBinaryTree.Node<Entry<Integer,String>> n8 = test.addLeft(n6, new Entry<Integer,String>(11,"i"));
        // test.addLeft(n1, null);
        // test.addRight(n1, null);
        // test.addLeft(n5, null);
        // test.addRight(n5, null);
        // test.addLeft(n8, null);
        // test.addRight(n8, null);
        // test.addRight(n6, null);
        // test.addLeft(n7, null);
        // test.addRight(n7, null);
        // test.addRight(n4, null);
        System.out.println(test.height_new(test.root));
        System.out.println(test.depth_new(test.root));
        //test.restructure(n3);
        //System.out.print(test.get(19));
        //test.insert(0,"");
        /*test.delete(11);
        LinkedBinaryTree.Node<Entry<Integer,String>> a = test.treeSearch(root,19);
        if(a.element != null) System.out.print(a.element.key);
        else System.out.print(a.element);
        test.delete(11);*/

        //test.printTree();

        //System.out.print(test.height(test.root));
    }
}