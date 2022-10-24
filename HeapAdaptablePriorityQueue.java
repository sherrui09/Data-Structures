import java.util.*;
public class HeapAdaptablePriorityQueue<K,V> extends HeapPriorityQueue<K,V> {
    protected static class AdaptableEntry<K,V> extends Entry<K,V>{
        private int index;
        public AdaptableEntry(K key, V value, int j){
            super(key,value);
            index = j;
        }
        public int getIndex(){return index;}
        public void setIndex(int j){index = j;}
    }
    public HeapAdaptablePriorityQueue(Comparator<K> c) {super(c);}
    public HeapAdaptablePriorityQueue(){super();}
    @Override
    public void swap(int i, int j){
        super.swap(i, j);
        ((AdaptableEntry<K,V>) heap.get(i)).setIndex(i);
        ((AdaptableEntry<K,V>) heap.get(j)).setIndex(j);
    }
    public AdaptableEntry<K,V> insert(K key, V value){
        AdaptableEntry<K,V> newest = new AdaptableEntry<>(key,value,heap.size());
        heap.add(heap.size(), newest);
        swim(heap.size()-1);
        return newest;
    }

    protected AdaptableEntry<K,V> validate(AdaptableEntry<K,V> entry){
        int j = entry.index;

        if (j >= heap.size()) {System.out.println(j);System.out.println(heap.size());throw new IllegalArgumentException("Invalid entry1");}
        else if( heap.get(j).k != entry.k ) throw new IllegalArgumentException("Invalid entry2");
        else if (heap.get(j).v != entry.v)
            throw new IllegalArgumentException("Invalid entry3");
        return entry;
    }
    public void remove(AdaptableEntry<K,V> entry){
        AdaptableEntry<K,V> locator = validate(entry);
        int j = locator.getIndex();
        swap(j,heap.size()-1);
        heap.remove(heap.size()-1);
        bubble(j);
    }
    public void bubble(int j){
        if (j>0 && j < heap.size() && comp.compare(heap.get(j).k,heap.get(parent(j)).k)<0){
            swim(j);
        }
        else sink(j);
    }
    public void replaceKey(AdaptableEntry<K,V> entry, K key){
        AdaptableEntry<K,V> locator = validate(entry);
        heap.get(locator.index).k=key;
        bubble(locator.index);
    }
    public void replaceValue(AdaptableEntry<K,V> entry, V value){
        AdaptableEntry<K,V> locator = validate(entry);
        heap.get(locator.index).v=value;
    }
    public Entry<K, V> removeMin(){
//        if(heap.size()==0) return null;
        swap(0,heap.size()-1);
        heap.remove(heap.size()-1);
        sink(0);
//        return answer;
        return null;
    }
    public AdaptableEntry<K,V> min(){
        if(heap.size()==0) return null;
        return (AdaptableEntry<K,V>)heap.get(0);
    }
}
