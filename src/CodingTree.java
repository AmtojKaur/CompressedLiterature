import java.util.*;
import java.util.Map.Entry;

/**
 * CodingTree follows Huffman coding algorithm to compress the message
 * being sent to its constructor
 *
 * @author Angelynna Pyeatt
 * @author Amtoj Kaur
 * @author Khoi Nguyen
 * @version May 6 2022
 */
public class CodingTree {

    /** a map of characters and their binary codes*/
    public Map<Character, String> codes;

    /** map for characters and their frequency*/
    public Map<Character, Integer> myCharFreq;

    /** priority queue of tree nodes*/
    public PriorityQueue<TreeNode> myPQ;

    /** holds stringbuilder for huffman codes*/
    public StringBuilder bits;

    /** holds stringbuilder for encoded text*/
    public StringBuilder encodedText;

    /*
     * a constructor that takes the text of a message to be compressed.
     * The constructor is responsible for calling all private methods
     * that carry out the Huffman coding algorithm.
     *
     * @param String message
     */
    public CodingTree(String message) {
        bits = new StringBuilder();
        encodedText = new StringBuilder();
        codes = new HashMap<Character, String>();
        myCharFreq = new HashMap<Character, Integer>();
        myPQ = new PriorityQueue<TreeNode>();

        //parse through message:
        parse(message);

        //transfer characters in HashMap to priority queue
        transfer();

        //build Huffman tree:
        build();

    }

    /**
     * returns the string associated with the character sent into the
     * methods parameters
     *
     * @param c: Character
     * @return codes.get(c): String
     */
    public String decode(Character c) {
        return codes.get(c);
    }


    /**
     * OPTIONAL
     * this method will take the output of Huffman’s encoding
     * and produce the original text.
     *
     * @param bits: String
     * @param codes: Map<Character, String>
     * @return decoded.toString(): String
     */
    public String decode(String bits, Map<Character, String> codes) {
        StringBuilder decoded = new StringBuilder();
        StringBuilder temp = new StringBuilder();
        Map<String, Character> reverse = new HashMap<String, Character>();

        //map.entry source:
        //https://www.geeksforgeeks.org/map-entry-interface-java-example/
        for(Entry<Character, String> entry: codes.entrySet()) {
            reverse.put(entry.getValue(), entry.getKey());
        }

        Character t;
        for(Character c: bits.toCharArray()) {
            temp.append(c);
            t = reverse.get(temp.toString());

            if(t != null) {
                decoded.append(t);
                temp.setLength(0);
            }
        }
        return decoded.toString();

    }

    /**
     * returns String version encoded text
     *
     * @param sb: StringBuilder
     * @return encodedTest.toString()
     */
    public String encodeText(StringBuilder sb) {
        for(Character c : sb.toString().toCharArray()) {
            encodedText.append(this.codes.get(c));
        }
        return encodedText.toString();
    }

    //private helper methods:

    /**
     * parses through chars in message
     *
     * @param message : String
     */
    private void parse(String message) {
        for(Character c : message.toCharArray()) {

            if(myCharFreq.get(c) == null) {	//adds new char
                myCharFreq.put(c, 1);
            } else {					//updates int frequency of char
                myCharFreq.put(c, myCharFreq.get(c).intValue() + 1);
            }
        }
    }

    /**
     * transfers from hashmap to priority queue
     */
    private void transfer() {
        for(Character c : myCharFreq.keySet()) {
            TreeNode node = new TreeNode(c, myCharFreq.get(c), null, null);
            myPQ.add(node);
        }
    }

    /**
     * builds Huffman Tree
     */
    private void build() {
        while(myPQ.size() > 1) {
            TreeNode n1 = myPQ.poll();
            TreeNode n2 = myPQ.poll();
            myPQ.add(combineWeights(n1, n2));

        }
        myPQ.peek();
        buildHuffmanCode(myPQ.poll());
    }
    /**
     * Creates a new treenode by combining the weights of two nodes, and
     * adding each other to its left and right subtree
     *
     * @param Node1: TreeNode
     * @param Node2: TreeNode
     * @return node: TreeNode
     */
    private TreeNode combineWeights(TreeNode Node1, TreeNode Node2) {
        int freq = Node1.freq + Node2.freq;
        TreeNode node = new TreeNode(null, freq, Node1, Node2);
        return node;
    }

    /**
     * Builds Huffman Codes by recursively traversing the root,
     * generating a '0' if left traverse or '1' if right traverse.
     * Once a leaf node is found, the nodes path is mapped to the
     * generated code traversal.
     * If the Huffman code length is greater than 0, the last 1 or 0 to
     * be added or deleted.
     *
     * @param node: TreeNode
     */
    private void buildHuffmanCode(TreeNode node) {
        if(node == null) {
            return;
        }
        //left traversal
        if(node.left != null) {
            bits.append(0);
        }
        buildHuffmanCode(node.left);

        //right traversal
        if(node.right != null) {
            bits.append(1);
        }
        buildHuffmanCode(node.right);

        //leaves
        if(isLeaf(node)) {
            codes.put((Character) node.data, bits.toString());
        }
        if(bits.length() > 0) {
            bits.deleteCharAt(bits.length() - 1);
        }

        return;

    }

    /**
     * determines whether or not the node sent in is a leaf
     *
     * @param node: TreeNode
     * @return boolean
     */
    private boolean isLeaf(TreeNode node) {
        if(node.left == null && node.right == null) {
            return true;
        } else {
            return false;
        }
    }

}

/**
 * Node class for each node stored in Huffman Tree
 *
 * @author Angelynna Pyeatt
 * @author Amtoj Kaur
 * @author Khoi Nguyen
 * @version May 6 2022
 *
 */
class TreeNode implements Comparable<Object> {
    /** holds data*/
    public Character data;
    /** holds the data frequency*/
    public int freq;
    /** holds left child*/
    public TreeNode left;
    /** holds right child*/
    public TreeNode right;
    /**
     * Constructor: stores character, frequency, and left and right child
     * nodes.
     *
     * @param c: Character
     * @param f: int
     * @param left: TreeNode
     * @param right: TreeNode
     */
    public TreeNode(Character c, int f, TreeNode left, TreeNode right) {
        data = c;
        freq = f;
        this.left = left;
        this.right = right;
    }

    /**
     * Compares node frequency
     *
     * @param o: Object
     * @return int
     */
    @Override
    public int compareTo(Object o) {
        TreeNode t = (TreeNode) o;
        if(freq > t.freq) {
            return 1;
        }else if(freq == t.freq) {
            return 0;
        } else {
            return -1;
        }
    }


}

