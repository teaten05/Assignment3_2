import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class RedBlackBST <Key extends Comparable <Key>, Value>{

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private Node root;

    private class Node{

        private Key key;
        private Value value;
        private Node left, right;
        private boolean color;
        private int n;


        public Node(Key key, Value value, boolean color, int n){

            this.key = key;
            this.value = value;
            this.color = color;
            this.n = n;

        }

    }

    private int size(Node node){

        if(node == null) return 0;
        return node.n;

    }

    public int size(){

        return size(root);

    }

    public RedBlackBST(){


    }

    public ProductRecord search(Key key){

        if(key == null) throw new IllegalArgumentException("search(key) is null.");
        return search(root, key);

    }

    public ProductRecord search(Node node, Key key){

        while(node != null){

            int compare = key.compareTo(node.key);
            if(compare < 0) node = node.left;
            else if (compare > 0) node = node.right;
            else return (ProductRecord) node.value;

        }
        return null;
    }

    public void insert(Key key, Value value){

        if(key == null) throw new IllegalArgumentException(("insert(key, value) is null"));
        if(value == null){

            delete(key);
            return;

        }

        if(search(key) != null){

            System.out.println("This key already exists.");
            return;

        }

        root = insert(root, key, value);
        root.color = BLACK;

    }

    private Node insert(Node node, Key key, Value value){

        if(node == null) return new Node(key, value, RED, 1);

        int compare = key.compareTo(node.key);
        if (compare < 0) node.left = insert(node.left, key, value);
        else if (compare > 0) node.right = insert(node.right, key, value);
        else node.value = value;

        return balance(node);

    }

    private boolean isRed(Node x){

        if (x == null) return false;
        return x.color == RED;

    }

    public boolean isEmpty(){

        return root == null;

    }

    public Value get(Key key){

        if (key == null) throw new IllegalArgumentException("get() is null.");
        return get(root, key);

    }

    public Value get(Node node, Key key){

        while(node != null){

            int compare = key.compareTo(node.key);
            if (compare < 0) node = node.left;
            else if(compare > 0) node = node.right;
            else return node.value;

        }

        return null;
    }

    public boolean contains(Key key){

        return get(key) != null;

    }

    public void delete(Key key){

        if (key == null) throw new IllegalArgumentException("delete() is null.");
        if (!contains(key)) return;

        if (!isRed(root.left) && !isRed(root.right)) root.color = RED;

        root = delete(root,key);
        if (!isEmpty()) root.color = BLACK;

    }

    private Node delete(Node node, Key key){

        if(key.compareTo(node.key) < 0){

            if(!isRed(node.left) && !isRed(node.left.left)){

                node = moveRedLeft(node);

            }
            node.left = delete(node.left, key);

        } else {

            if(isRed(node)) node = rotateRight(node);
            if(key.compareTo(node.key) == 0 && (node.right == null)) return null;
            if(!isRed(node.right) && !isRed(node.right.left)) node = moveRedRight(node);
            if(key.compareTo(node.key) == 0){

                Node n = min(node.right);
                node.key = n.key;
                node.value = n.value;
                node.right = deleteMin(node.right);

            } else node.right = delete(node.right, key);

        }
        return balance(node);
    }

    public void put(Key key, Value value){

        if (key == null) throw new IllegalArgumentException("Put is null");
        if (value == null){

            delete(key);
            return;

        }

        root = put(root, key, value);
        root.color = BLACK;

    }

    public Node put (Node node, Key key, Value value){

        if (node == null) return new Node(key, value, RED, 1);

        int compare = key.compareTo(node.key);
        if (compare < 0) node.left = put(node.left, key, value);
        else if (compare > 0) node.right = put(node.right, key, value);
        else node.value = value;

        if (isRed(node.right) && !isRed(node.left)) node = rotateLeft(node);
        if (isRed(node.left) && isRed(node.left.left)) node = rotateRight(node);
        if(isRed(node.left) && isRed(node.right)) flipColors(node);
        node.n = size(node.left) + size(node.right) + 1;

        return node;

    }

    private Node rotateRight(Node node){

        if (node == null || node.right == null) return node;

        Node n = node.right;
        node.right = n.left;
        n.left = node;
        n.color = node.color;
        node.color = RED;
        n.n = node.n;
        node.n = size(node.left) + size(node.right) + 1;

        return n;

    }
    private Node rotateLeft(Node node){

        if (node == null || node.left == null) return node;

        Node n = node.left;
        node.left = n.right;
        n.right = node;
        n.color = node.color;
        node.color = RED;
        n.n = node.n;
        node.n = size(node.left) + size(node.right) + 1;

        return n;

    }

    private void flipColors(Node node){

        node.color = !node.color;
        node.left.color = !node.left.color;
        node.right.color = !node.right.color;

    }

    private Node moveRedLeft(Node node){

        flipColors(node);
        if(isRed(node.left.left)){

            node.right = rotateRight(node);
            node = rotateLeft(node);
            flipColors(node);

        }
        return node;

    }

    private Node moveRedRight(Node node){

        flipColors(node);
        if(isRed(node.left.left)){

            node = rotateRight(node);
            flipColors(node);

        }
        return node;
    }

    private Node balance(Node node){

        if (node == null) return null;

        if(isRed(node.right) && !isRed(node.left)) node = rotateLeft(node);
        if(isRed(node.left) && isRed(node.left.left)) node = rotateRight(node);
        if(isRed(node.left) && isRed(node.right)) flipColors(node);

        node.n = size(node.left) + size(node.right) + 1;
        return node;

    }

    private Node min(Node node){

        if(node.left == null) return node;
        else return min(node.left);

    }

    private Node deleteMin(Node node){

        if(node.left == null) return null;
        if(!isRed(node.left) && !isRed(node.left.left)) node = moveRedLeft(node);

        node.left = deleteMin(node.left);
        return balance(node);

    }

    public static RedBlackBST<String, ProductRecord> getCsv(String fileName){

        RedBlackBST<String, ProductRecord> bst = new RedBlackBST<>();
        long totalTime = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))){

            String line;
            reader.readLine();

            while((line = reader.readLine()) != null){

                String[] characters = line.split(",");
                if(characters.length != 4) continue;

                String productId = characters[0].trim();
                String name = characters[1].trim();
                String category = characters[2].trim();
                String price = characters[3].trim();

                ProductRecord product = new ProductRecord(productId, name, category, price);

                // THE PRODUCT ID IS THE KEY!!!!
                bst.insert(productId, product);

            }

        } catch (IOException e){

            e.printStackTrace();

        }

        return bst;

    }

    public static void main(String[] args){

        RedBlackBST<String, ProductRecord> bst = getCsv("lib/amazon-product-data.csv");


        /* PRACTICE INPUT!!!
        bst.insert("4c69b61db", new ProductRecord("4c69b61db", "DB", "Longboard Sports", 237.68));
        bst.insert("66d49bbed", new ProductRecord("66d49bbed", "Electronic", "Toy & Games", 99.95));
        bst.insert("2c55cae26", new ProductRecord("2c55cae26","3Doodler", "Toys & Games", 34.99));

        System.out.println(bst.search("4c69b61db"));
        System.out.println(bst.search("66d49bbed"));
        System.out.println(bst.search("1"));*/

        Scanner input = new Scanner(System.in);

        for(int i = 0; i < 3; i++) {
            System.out.println("Enter a Product ID: ");

            String searchInquiry = input.nextLine();
            ProductRecord result = bst.search(searchInquiry);

            //Use: a11d9462309527143094a0f68bce0a58
            // 1ecccb43e0f5c0162218371916ffa553
            // and, 25a1fcc6db40f86566c5f2d902407b15

            if(result != null) {
                System.out.println("ProductID: " + result.productId
                        + "\nName: " + result.name
                        + "\nCategory: " + result.category
                        + "\nPrice: " + result.price);
            } else {

                System.out.println("This product ID was not found.");

            }

        }

        bst.insert("123", new ProductRecord("123", "abc", "xyz", "$10.00"));
        bst.insert("1ecccb43e0f5c0162218371916ffa553", new ProductRecord("1ecccb43e0f5c0162218371916ffa553", "C&D Visionary DC Comic Originals Flash Logo Sticker"
                , "Toys & Games | Arts & Crafts | Craft Kits", "$4.99"));

    }

}