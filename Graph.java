import java.util.*;

import javax.sound.sampled.BooleanControl;
import javax.swing.text.Position;

public class Graph<V,E> {
    public class Vertex<V> {
        public V element;
        public DoublyLinkedList.Node<Vertex<V>> position; // position in the vertex list;
        public Map<Vertex<V>,Edge<E>> outgoing, incoming;
        public Vertex(V element, boolean graphIsDirected){
            this.element = element;
            outgoing = new HashMap<>();
            if(graphIsDirected)
                incoming = new HashMap<>();
            else
                incoming = outgoing;
        }
        public boolean validate(Graph<V,E> g){
            if(Graph.this == g) {
                return true;
            }
            else
                return false;
        }
    }
    public class Edge<E>{
        public int[] element;
        public DoublyLinkedList.Node<Edge<E>> position;
        public Vertex<V>[] endpoints;
        public Edge(Vertex<V> u, Vertex<V> v, int[] element){
            this.element = element;
            endpoints = new Vertex[]{u,v};
        }
        public boolean validate(Graph<V,E> g){
            if(Graph.this == g) {
                return true;
            }
            else
                return false;
        }
    }
    private boolean isDirected;
    private DoublyLinkedList<Vertex<V>> vertices = new DoublyLinkedList<>();
    private DoublyLinkedList<Edge<E>> edges = new DoublyLinkedList<>();
    public Graph(boolean isDirected){this.isDirected = isDirected;}
    public int numVertices() {return vertices.size;}
    public int numEdges() {return edges.size;}
    public int outDegree(Vertex<V> v){ // put into the place where 'degree' is introduced.
        return v.outgoing.size();
    }
    public int inDegree(Vertex<V> v){
        return v.incoming.size();
    }
    public Edge<E> getEdge(Vertex<V> u, Vertex<V> v){
        // edge of (u,v)
        return  u.outgoing.get(v);
    }
    public Vertex<V> opposite(Vertex<V> v, Edge<E> e){
        // get both two endpoints of e
        Vertex<V> origin = e.endpoints[0];
        Vertex<V> destination = e.endpoints[1];
        // check 
        if(origin == v) return destination;
        else if(destination == v) return origin;
        else throw new IllegalArgumentException("v doesn not match");
    }
    public Vertex<V> insertVertex(V element){
        Vertex<V> newVertex = new Vertex(element,isDirected);
        newVertex.position = vertices.addLast(newVertex);
        return newVertex;
    }
    public Edge<E> insertEdge(Vertex<V> u, Vertex<V> v, int[] element){
        if(getEdge(u,v) == null){
            // instantiate the edge
            Edge<E> newEdge = new Edge(u,v,element);
            // u's outgoing
            u.outgoing.put(v,newEdge);
            //v's incoming
            v.incoming.put(u,newEdge);
            // edge list
            newEdge.position = edges.addLast(newEdge);
            return newEdge;
        }
        else throw new IllegalArgumentException("already exists");
    }
    public void removeEdge(Edge<E> e){
        Vertex<V> origin = e.endpoints[0];
        Vertex<V> destination = e.endpoints[1];
        // outgoing of some verteice, incoming of some vertices
        origin.outgoing.remove(destination);
        destination.incoming.remove(origin);
        // edge list
        edges.delete(e.position);
    }
    public void removeVertex(Vertex<V> v){
        // outgoing
        for (Edge<E> e: v.outgoing.values()){
            removeEdge(e);
        }
        // incoming
        for (Edge<E> e: v.incoming.values()){
            removeEdge(e);
        }
        // vertex list
        vertices.delete(v.position);
    }



    public void print(Vertex<V> u){
        System.out.println(u.outgoing);
    }
    // public HashMap<Vertex<V>,Edge<E>> DFS(Vertex<V> v){
    //     HashMap<Vertex<V>,Edge<E>> forest = new HashMap<>();
    //     forest.put(v,null);
    //     DFSUnit(v,forest);
    //     //printForest(forest);
    //     return forest;
    // }

    public HashMap<Vertex<V>,Edge<E>> disconnectedGraphDFS(){
        HashMap<Vertex<V>,Edge<E>> forest = new HashMap<>();
        DoublyLinkedList.Node<Vertex<V>> curVertex = vertices.head.next;
        while(curVertex.next != null){
            Vertex<V> u = curVertex.element;
            if (forest.containsKey(u)==false){
                HashMap<Vertex<V>,Edge<E>> curForest = DFSWrapper(u);
                for (Vertex<V> v:curForest.keySet()){
                    forest.put(v,curForest.get(v));
                }
            }
            curVertex = curVertex.next;
        }
        return forest;
    }

    public void printForest(HashMap<Vertex<V>,Edge<E>> forest){
        System.out.print(forest.values().size());
        for(Edge<E> e: forest.values()){
            if(e!=null){
                System.out.print(e.endpoints[0].element);
                System.out.print(",");
                System.out.print(e.endpoints[1].element);
                System.out.print("\n");
            }
        }
    }

    public HashMap<Vertex<V>,Edge<E>> DFSWrapper(Vertex<V> u){
        HashMap<Vertex<V>,Edge<E>> forest = new HashMap<>();
        forest.put(u,null);
        DFS(u,forest);
        return forest;
    }
    public void DFS(Vertex<V> u, HashMap<Vertex<V>,Edge<E>> forest){
        for(Vertex<V> v:u.outgoing.keySet()){
            if(forest.containsKey(v)==false){
                forest.put(v,getEdge(u,v));
                DFS(v,forest);
            }
        }
    }


















    public boolean cycleDector(Vertex<V> v){
        HashMap<Vertex<V>,Edge<E>> forest = new HashMap<>();
        forest.put(v,null);
        HashMap<Vertex<V>,Edge<E>> ancestors = new HashMap<Vertex<V>,Edge<E>>();
        ancestors.put(v,null);
        return cycleDetect(v,forest,ancestors);

    }
    public boolean cycleDetect(Vertex<V> u, HashMap<Vertex<V>,Edge<E>> forest, HashMap<Vertex<V>,Edge<E>> ancestors){
        for(Vertex<V> v:u.outgoing.keySet()){
            if(ancestors.containsKey(v))
                return true;
            else{
                if(forest.containsKey(v)==false){
                    forest.put(v,getEdge(u,v));
                    ancestors.put(v,null);
                    boolean flag = cycleDetect(v,forest,ancestors);
                    ancestors.remove(v,null);
                    if(flag == true)return true;
                }
            }

        }
        return false;
    }












    public DoublyLinkedList<Edge<E>> constructPath(Vertex<V> source, Vertex<V> target){
        HashMap<Vertex<V>,Edge<E>> record = DFSWrapper(source);
        DoublyLinkedList<Edge<E>> path = new DoublyLinkedList<Edge<E>>();
        if(!record.containsKey(target)) return null;
        else{
            Vertex<V> curVertex = target;
            while(record.get(curVertex)!=null){
                Edge<E> curEdge = record.get(curVertex);
                path.insert(curEdge,path.head,path.head.next);
                curVertex = this.opposite(curVertex,curEdge);
            }
            return path;
        }
    }



    public void printPath(DoublyLinkedList<Edge<E>> path){
        DoublyLinkedList.Node<Edge<E>> curNode = path.head.next;
        while(curNode.element != null){
            System.out.print(curNode.element.element);
            curNode = curNode.next;
        }
    }

    public void BFS(Vertex<V> u){
        HashMap<Vertex<V>,Edge<E>> record = new HashMap<>();
        record.put(u,null);
        ArrayList<Vertex<V>> curLevel = new ArrayList<>();
        curLevel.add(u);
        while(curLevel.size != 0){
            ArrayList<Vertex<V>> newLevel = new ArrayList<>();
            for(Vertex<V> x:curLevel){
                for(Vertex<V> v:x.outgoing.keySet()){
                    if(!record.containsKey(v)){
                        record.put(v,x.outgoing.get(v));
                        newLevel.add(v);
                    }
                }
            }
            curLevel = newLevel;
        }

    }





    public HashMap<Vertex<V>,Edge<E>> augBFS(Vertex<V> u){
        HashMap<Vertex<V>,Edge<E>> record = new HashMap<>();
        record.put(u,null);
        ArrayList<Vertex<V>> curLevel = new ArrayList<>();
        curLevel.add(u);
        while(curLevel.size != 0){
            ArrayList<Vertex<V>> newLevel = new ArrayList<>();
            for(Vertex<V> x:curLevel){
                for(Vertex<V> v:x.outgoing.keySet()){
                    Edge<E> e = getEdge(x,v);
                    if(e.element[1]<e.element[0]){
                        if(!record.containsKey(v)){
                            record.put(v,x.outgoing.get(v));
                            newLevel.add(v);
                        }
                    }
                }
                for(Vertex<V> v:x.incoming.keySet()){
                    Edge<E> e = getEdge(v,x);
                    if(e.element[1]>0){
                        if(!record.containsKey(v)){

                            record.put(v,x.outgoing.get(v));
                            newLevel.add(v);
                        }
                    }
                }
            }
            curLevel = newLevel;
        }
        return record;
    }



    public int FordFulkersonAlg(Vertex<V> s, Vertex<V> t){
        int maxflow = 0;
        HashMap<Vertex<V>,Edge<E>> record = augBFS(s);
        while (record.containsKey(t)){
            // Step 1: get bottleneck
            int bottleneck = Integer.MAX_VALUE;
            Vertex<V> v = t;
            while(v != s){
                Edge<E> e = record.get(v);
                // if forward 
                if(e.endpoints[1] == v){
                    // what is the remaining copacity
                    int curCap = e.element[0] - e.element[1];
                    bottleneck = Math.min(bottleneck,curCap);
                }
                else {
                    int curCap = e.element[1];
                    bottleneck = Math.min(bottleneck,curCap);
                }
                v = opposite(v,e);
                // or backwar
            }
            // Step 2: increase flow on that path by bottleneck capacity
            v = t;
            while (v != s){
                Edge<E> e = record.get(v);
                // update the edge's flow
                // if forward
                if(e.endpoints[1] == v){
                    e.element[1] += bottleneck;
                }
                else{
                    e.element[1] -= bottleneck;
                }
                v = opposite(v,e);
            }
            maxflow += bottleneck;
            // Step 3:
            record = augBFS(s);
        }
        return maxflow;
    }











    public ArrayList<Vertex<V>> topologicalSort(){
        // find the no-incoming vertices
        ArrayList<Vertex<V>> ordering = new ArrayList<>();
        HashMap<Vertex<V>,Integer> inCount = new HashMap<>();
        DoublyLinkedList.Node<Vertex<V>> curVertex = vertices.head.next;
        while(curVertex.next != null){
            Vertex<V> v = curVertex.element;
            int inDegree = v.incoming.size();
            inCount.put(v,inDegree);
            if(inDegree == 0) ordering.add(v);
            curVertex = curVertex.next;
        }
        // for (Vertex<V> v:vertices){
        //     int inDegree = v.incoming.size();
        //     inCount.put(v,inDegree);
        //     if(inDegree == 0) ordering.add(v);
        // }
        //System.out.print("here");
        //printTopologicalOrdering(ordering);
        for(int i = 0;i<vertices.size;i++){
            if (i>=ordering.size()) break;
            else{
                Vertex<V> curV = ordering.get(i);
                for (Vertex<V> v: curV.outgoing.keySet()){
                    int newCount = inCount.get(v)-1;
                    inCount.put(v,newCount);
                    if(newCount == 0) ordering.add(v);
                }
            }
        }
        //get the orderings
        return ordering;
    }
    public void printTopologicalOrdering(ArrayList<Vertex<V>> ordering){
        for(Vertex<V> v:ordering){
            //System.out.print("here");
            System.out.print(v.element);
        }
    }

}