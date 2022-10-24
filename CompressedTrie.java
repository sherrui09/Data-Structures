// Java implementation of search and insert operations
// on Trie
public class CompressedTrie {

    // Alphabet size (# of symbols)
    static final int ALPHABET_SIZE = 26;
    protected String[] strings;

    static class CompressedTrieNode
    {
        CompressedTrieNode[] children = new CompressedTrieNode[ALPHABET_SIZE];
        StringBuilder label = new StringBuilder();
        StringBuilder[] labels = new StringBuilder[ALPHABET_SIZE];
        CompressedTrieNode parent;
        // isEndOfWord is true if the node represents
        // end of a word
        Integer position;

        public CompressedTrieNode(CompressedTrieNode parent, char key){
            position = null;
            this.parent = parent;
            for (int i = 0; i < ALPHABET_SIZE; i++){
                children[i] = null;
                labels[i] = null;
            }
        }
        int numChildren(){
            int count = 0;
            for (CompressedTrieNode e:children){
                if(e != null)
                    count++;
            }
            return count;
        }
    };

    static CompressedTrieNode root = new CompressedTrieNode((CompressedTrieNode) null,' ');
    public void getAllKeys(){
        getKeys(root);
    }
    public void getKeys(CompressedTrieNode p){
        for (int i=0;i<ALPHABET_SIZE;i++){
            if(p.children[i] != null){
                p.children[i].label = p.labels[i];
                getKeys(p.children[i]);
            }
        }
    }

    public void print()
    {
        printUtil(root, new StringBuilder());
    }




    public boolean search(String query){
        CompressedTrieNode node = root;
        for(int i=0;i<query.length();i++){
            int index = query.charAt(i) - 'a';
            if(node.children[index] == null) return false;
            node = node.children[index];
            for(int j=0;j<node.label.length();j++){
                if(i+j == query.length()) return false;
                if( node.label.charAt(j) != query.charAt(i+j)) return false;
            }
            i += node.label.length()-1;
        }
        if (node.position == null ) return false;
        else return true;
    }


    public boolean search_alternative(String query){
        int index = 0;
        CompressedTrieNode node = root;
        while(index <query.length()){
            if (node.children[query.charAt(index)-'a']==null){
                return false;
            }
            node = node.children[query.charAt(index)-'a'];
            int j = 0;
            while (j<node.label.length()){
                if (index == query.length() || query.charAt(index) != node.label.charAt(j)){
                    return false;
                }
                index++;
                j++;
            }
        }
        if(node.position == null) return false;
        else return true;
    }

    // Function to print the word
    // starting from the given node
    private void printUtil(
            CompressedTrieNode node, StringBuilder str)
    {
        if (node.position != null) {
            System.out.println(str);
        }

        for (int i = 0;
             i < node.labels.length; ++i) {

            // If edgeLabel is not
            // NULL
            if (node.labels[i] != null) {
                int length = str.length();

                str = str.append(node.labels[i]);
                printUtil(node.children[i], str);
                str = str.delete(length, str.length());
            }
        }
    }

    public void insert(String word)
    {
        // Store the root
        CompressedTrieNode trav = root;
        int i = 0;

        // Iterate i less than word
        // length
        while (i < word.length()
                && trav.labels[word.charAt(i) - 'a']
                != null) {

            // Find the index
            int index = word.charAt(i) - 'a', j = 0;
            StringBuilder label = trav.labels[index];

            // Iterate till j is less
            // than label length
            while (j < label.length() && i < word.length()
                    && label.charAt(j) == word.charAt(i)) {
                ++i;
                ++j;
            }

            // If is the same as the
            // label length
            if (j == label.length()) {
                trav = trav.children[index];
            }
            else {

                // Inserting a prefix of
                // the existing word
                if (i == word.length()) {
                    CompressedTrieNode existingChild
                            = trav.children[index];
                    CompressedTrieNode newChild = new CompressedTrieNode((CompressedTrieNode) null,' ');
                    newChild.position = 1;
                    StringBuilder remainingLabel
                            = strCopy(label, j);

                    // Making "facebook"
                    // as "face"
                    label.setLength(j);

                    // New node for "face"
                    trav.children[index] = newChild;
                    newChild
                            .children[remainingLabel.charAt(0)
                            - 'a']
                            = existingChild;
                    newChild
                            .labels[remainingLabel.charAt(0)
                            - 'a']
                            = remainingLabel;
                }
                else {

                    // Inserting word which has
                    // a partial match with
                    // existing word
                    StringBuilder remainingLabel
                            = strCopy(label, j);

                    CompressedTrieNode newChild = new CompressedTrieNode((CompressedTrieNode) null,' ');
                    newChild.position = null;
                    StringBuilder remainingWord
                            = strCopy(word, i);

                    // Store the trav in
                    // temp node
                    CompressedTrieNode temp = trav.children[index];

                    label.setLength(j);
                    trav.children[index] = newChild;
                    newChild
                            .labels[remainingLabel.charAt(0)
                            - 'a']
                            = remainingLabel;
                    newChild
                            .children[remainingLabel.charAt(0)
                            - 'a']
                            = temp;
                    newChild
                            .labels[remainingWord.charAt(0)
                            - 'a']
                            = remainingWord;
                    newChild
                            .children[remainingWord.charAt(0)
                            - 'a']
                            = new CompressedTrieNode((CompressedTrieNode) null,' ');
                    newChild
                            .children[remainingWord.charAt(0)
                            - 'a'].position = 1;
                }

                return;
            }
        }
        // Insert new node for new word
        if (i < word.length()) {
            trav.labels[word.charAt(i) - 'a']
                    = strCopy(word, i);
            trav.children[word.charAt(i) - 'a']
                    = new CompressedTrieNode((CompressedTrieNode) null,' ');
            trav.children[word.charAt(i) - 'a'].position = 1;
        }
        else {

            // Insert "there" when "therein"
            // and "thereafter" are existing
            trav.position = 1;
        }
    }
    // Driver
    private StringBuilder strCopy(
            CharSequence str, int index)
    {
        StringBuilder result
                = new StringBuilder(100);

        while (index != str.length()) {
            result.append(str.charAt(index++));
        }

        return result;
    }
    public static void main(String[] args)
    {
        CompressedTrie trie = new CompressedTrie();

        // Insert words
        trie.insert("facebook");
        trie.insert("face");
        trie.insert("this");
        trie.insert("there");
        trie.insert("then");

        // Print inserted words
        trie.print();
        trie.getAllKeys();
        // Check if these words
        // are present or not
        System.out.println(
                trie.search("there"));
        System.out.println(
                trie.search("therein"));
    }
}
// This code is contributed by Sumit Ghosh

