// Java implementation of search and insert operations
// on Trie
public class Trie {

    // Alphabet size (# of symbols)
    static final int ALPHABET_SIZE = 26;

    // trie node
    static class TrieNode
    {
        TrieNode[] children = new TrieNode[ALPHABET_SIZE];
        char key;
        TrieNode parent;
        // isEndOfWord is true if the node represents
        // end of a word
        Integer position;

        TrieNode(TrieNode parent, char key){
            position = null;
            this.parent = parent;
            this.key = key;
            for (int i = 0; i < ALPHABET_SIZE; i++)
                children[i] = null;
        }
        int numChildren(){
            int count = 0;
            for (TrieNode e:children){
                if(e != null)
                    count++;
            }
            return count;
        }
    };

    static TrieNode root;


    static TrieNode search(String  query){
        TrieNode n = root;
        for (int i=0;i<query.length();i++){
            char s = query.charAt(i);
            int index = s - 'a';
            if (n.children[index] != null) {
                n = n.children[index];
            }
            else{
                return null;
            }
        }
        if(n.position == null){
            return null;
        }
        else
            return n;
    }












    // If not present, inserts key into trie
    // If the key is prefix of trie node,
    // just marks leaf node
    static void insert(String key, Integer value){
        TrieNode node = root;
        for (int i = 0; i < key.length(); i++)
        {
            int index = key.charAt(i) - 'a';
            if (node.children[index] == null)
                node.children[index] = new TrieNode(node,key.charAt(i));
            node = node.children[index];
        }
        node.position = value; // mark last node as leaf
    }



    // Returns true if key presents in trie, else false
    // static TrieNode search(String key)
    // {
    // 	TrieNode node = root;

    // 	for (int i = 0; i < key.length(); i++)
    // 	{
    // 		int index = key.charAt(i) - 'a';

    // 		if (node.children[index] == null)
    // 			return null;

    // 		node = node.children[index];
    // 	}

    // 	if(node.position == null)
    // 		return null;
    // 	else
    // 		return node;
    // }


    static boolean remove(String key)
    {
        TrieNode node = search(key);
        if (node == null || node.position == null)
            return false;

        if (node.numChildren()!=0){
            node.position = null;
            return true;
        }
        TrieNode parent = node.parent;
        while (parent != null){
            parent.children[node.key] = null;
            if (parent.numChildren()!=0||parent.position != null)
                break;
            node = parent;
            parent = node.parent;
        }
        return true;
    }

    // Driver
    public static void main(String args[])
    {
        System.out.println("hello world");
        // Input keys (use only 'a' through 'z' and lower case)
        String keys[] = {"the", "a", "there", "answer", "any",
                "by", "bye", "their"};

        String output[] = {"Not present in trie", "Present in trie"};


        root = new TrieNode(null,(char) 0);

        // Construct trie
        int i;
        for (i = 0; i < keys.length ; i++)
            insert(keys[i],i);
        //remove("them");
        // Search for different keys
        if(search("th")!= null)
            System.out.println("th --- " + output[1]);
        else System.out.println("th --- " + output[0]);

        if(search("these")!= null && search("these").position != null)
            System.out.println("these --- " + output[1]);
        else System.out.println("these --- " + output[0]);

        if(search("their")!= null && search("their").position != null)
            System.out.println("their --- " + output[1]);
        else System.out.println("their --- " + output[0]);

        if(search("thaw")!= null && search("thaw").position != null)
            System.out.println("thaw --- " + output[1]);
        else System.out.println("thaw --- " + output[0]);

    }
}

