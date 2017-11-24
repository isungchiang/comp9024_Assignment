import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Jiggy on 19/04/2016.
 *
 * Assignment two
 *
 * This class has three main methods implemented with some helper classes and methods
 *
 * 1. Clone
 * 2. Merge
 * 3. Print (In this case, we needed to use the GUI component of Java and draw a tree
 *    The only requirement of this task was that the lines shouldn't cross.
 *
 *
 */
public class ExtendedAVLTree<K, V> extends AVLTree<K, V>{

    /**
     *
     * The main method or the starting point of my class
     *
     * @param args
     */

    public static void main(String[] args) {


        String values1[]={"Sydney", "Beijing","Shanghai", "New York", "Tokyo", "Berlin",
                "Athens", "Paris", "London", "Cairo"};
        int keys1[]={20, 8, 5, 30, 22, 40, 12, 10, 3, 5};
        String values2[]={"Fox", "Lion", "Dog", "Sheep", "Rabbit", "Fish"};
        int keys2[]={40, 7, 5, 32, 20, 30};

        AVLTree<Integer, String> tree1 = new AVLTree<Integer, String>();

        for ( int i=0; i<keys1.length; i++)
            tree1.insert(keys1[i], values1[i]);

        for ( int i=0; i<keys1.length; i++)
            tree1.insert(keys1[i], values1[i]);


        AVLTree<Integer, String> tree2=new AVLTree<Integer, String>();
//
        for ( int i=0; i<6; i++)
            tree2.insert(keys2[i], values2[i]);


        //extendedAVLTree.preOrderPrint(tree1,  tree1.root());
        AVLTree<Integer, String> clonedTree = ExtendedAVLTree.clone(tree1);
//
        //System.out.println(tree1.height(tree1.root()));
        //preOrderPrint(tree1, tree1.root());
//        System.out.println(" ");
//        System.out.println("Cloned Tree");
//        System.out.println(clonedTree.height(clonedTree.root()));
//        preOrderPrint(clonedTree, clonedTree.root());
//        System.out.println("Making changes in the main tree");
//        tree1.insert(50, "Test");
//        tree1.insert(60, "Test Test");
//        System.out.println("Print the Tree");
//        System.out.println(" ");
//        preOrderPrint(tree1, tree1.root());
//        System.out.println(" ");
//        System.out.println("  Cloned Tree ");
//        preOrderPrint(clonedTree, clonedTree.root());

        ExtendedAVLTree.print(tree1);

        //ExtendedAVLTree.merge(tree1, tree2);

    }

    /**
     *
     * This is the first method of the specification or this assignment.
     * We need to clone the input tree and returned the newly copied tree
     * We needed to make sure that the time complexicity of this task
     * mucst be O(n)
     *
     * This method uses the helper method which is being called recursively
     * and go through the each node of the input tree (excluding the root node).
     *
     * Another thing to notice, the AVL node object should be copies with all
     * the object level variables.
     *
     * The nature of AVL tree is that it balances the tree based on the height in a
     * way that the left and right tree height is not more than one! So we needed to
     * make sure that the height element or the field variable is copied for all
     * the nodes.
     *
     * In terms of the time complecixity
     *
     * 1. The creation of clone tree object is a primitive so O(1)
     * 2. The next line is calling the helper method so that line can be considered as
     * the primitive call However, that method is visiting all the nodes. So if the
     * number of nodes in the input tree is n then the time complexity would be O(n)
     *
     * Output of this method is the newly cloned / copied tree.!! which is just a return
     * statement. it is O(1)
     *
     * Needless to say K and V are the generics (The explanation of these two is outside of
     * this assignment scope). However, as per the requirement, it will be Integer and String
     * respectively.
     *
     *
     * @param tree
     * @param <K>
     * @param <V>
     * @return
     */

    public static<K,V> AVLTree<K, V> clone(AVLTree<K, V> tree){
        AVLTree<K, V> clonedTree = new AVLTree<K, V>();

        clonedTree.root =
                (BTPosition<Entry<K,V>>) cloneHelper(tree, (AVLNode<K, V>) tree.root(), null, clonedTree);
        clonedTree.numEntries = tree.numEntries;


        return clonedTree;
    }

    /**
     *
     * Right, so this is the helper method which is being called and visiting each and every
     * nodes of the input tree.
     *
     * Bit about the input variables
     *
     * the main tree is the tree object which is being copied or cloned
     * node is the which is being copied / cloned
     *
     * Parent - represent the parent of the newly created node
     *
     * tree is the newly created tree
     *
     * Time complexicity
     *
     * 1. Creating an object of AVLNode is considered as primitive operation O(1)
     * 2. Setting height is same - O(1)
     * 3. Next line is to call the same method again so the timeline complexicity
     * would be O(n/2)
     * 4. Same as point three for the right side of the tree O(n/2)
     *
     * getting left and right is to get the left and right object variable valuues
     * So these two operations is returning whatever is set node.left and node.right
     *
     * So over all time complexicity of this method is O(n/2 + n/2) (left and right tree)
     * so the total is O(n)
     *
     *
     *
     * @param theMainTree
     * @param node
     * @param parent
     * @param tree
     * @param <K>
     * @param <V>
     * @return
     */
    public static<K, V> AVLNode cloneHelper(AVLTree<K, V> theMainTree, AVLNode<K,V> node, AVLNode<K, V> parent, AVLTree<K,V> tree){
        AVLNode clonedNode;
        clonedNode = (AVLNode) tree.createNode(node.element(), parent, null, null);
        clonedNode.setHeight(node.getHeight());

        if(theMainTree.hasLeft(node)){
            clonedNode.setLeft(cloneHelper(theMainTree, (AVLNode<K, V>) theMainTree.left(node), clonedNode, tree));
        }
        if(theMainTree.hasRight(node)){
            clonedNode.setRight(cloneHelper(theMainTree, (AVLNode<K, V>)theMainTree.right(node), clonedNode, tree));
        }
        return clonedNode;

    }

    /**
     *
     * This is just helper method for my testing. It basically print
     * the list of tree in preorder fashion.
     *
     * Again it is traversing all the nodes from the input tree
     * so the complexity of this method would be O(n)
     *
     *
     * @param tree
     * @param node
     * @param <K>
     * @param <V>
     */

    private static<K,V> void preOrderPrint(AVLTree<K, V> tree, Position<Entry<K, V>> node){
        if(node.element() != null){
            System.out.print(node.element().getKey()+"   "+node.element().getValue()+"  "+tree.height(node)+"  "+tree.hasLeft(node)+"  "+tree.hasRight(node));
            System.out.println(" ");
        }

        if(tree.isInternal(node) && tree.hasLeft(node))
            preOrderPrint(tree, tree.left(node));

        if(tree.isInternal(node) && tree.hasRight(node)){
            preOrderPrint(tree, tree.right(node));
        }
    }

    /**
     *
     * This is the last task of this assignment which basically creates the JFrame object and draws
     * the tree. The tree is the input parametere to this method.
     *
     * Not sure, if we needed to provide any time complexicity analysis for this method. At least, it
     * would take O(n). Haven't considered any JAVA default calls for drawing compotent on JPanel or
     * the JFrame.
     *
     * @param tree
     * @param <K>
     * @param <V>
     */

    public static<K,V> void print(AVLTree<K, V> tree){
        JFrame theMainContainerToBeDisplayed = getMeTheFrame("Displaying AVL Tree");
        //JScrollPane scrollPane = new JScrollPane();
        //scrollPane.add(new TheUglyDesignOfMyPrintTreeClassThisShouldHaveBeenInADifferentFile());
        TheUglyDesignOfMyPrintTreeClassThisShouldHaveBeenInADifferentFile
                theUglyDesignOfMyPrintTreeClassThisShouldHaveBeenInADifferentFile
                 = new TheUglyDesignOfMyPrintTreeClassThisShouldHaveBeenInADifferentFile(tree);
        theMainContainerToBeDisplayed.add(theUglyDesignOfMyPrintTreeClassThisShouldHaveBeenInADifferentFile);

    }

    /**
     *
     * This is the helper method which creates the object of JFrame object
     *
     * @param titleOfTheFrame
     * @return
     */
    private static JFrame getMeTheFrame(String titleOfTheFrame){
        JFrame theMainContainerToBeDisplayed = new JFrame();
        theMainContainerToBeDisplayed.setTitle(titleOfTheFrame);
        theMainContainerToBeDisplayed.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        theMainContainerToBeDisplayed.setVisible(true);
        //theMainContainerToBeDisplayed.setResizable(false);
        theMainContainerToBeDisplayed.pack();
        theMainContainerToBeDisplayed.setLocationRelativeTo(null);
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        theMainContainerToBeDisplayed
                .setSize(dimension.width/2-theMainContainerToBeDisplayed.getSize().width/2,
                        dimension.height/2-theMainContainerToBeDisplayed.getSize().height/2);

        return theMainContainerToBeDisplayed;
    }

    /**
     *
     * Again this is the helper method to a list of nodes in In Order style. This checks each and every node
     * of the input tree so the time complexity would be O(n).
     *
      * @param tree
     * @param node
     * @param list
     * @param <K>
     * @param <V>
     */
    private static<K, V> void getNodesInOrderStyle(AVLTree<K, V> tree, Position<Entry<K, V>> node, ArrayList<Position<Entry<K, V>>> list){
        if(tree.isInternal(node) && tree.hasLeft(node))
            getNodesInOrderStyle(tree, tree.left(node), list);

        if(node.element() != null){
            list.add(node);
        }
        if(tree.isInternal(node) && tree.hasRight(node)){
            getNodesInOrderStyle(tree, tree.right(node), list);
        }
    }

    /**
     * This is the helper to merge two sorted Array List
     *
     * It is basically calling itself n number of times where n number of element
     * in the input array list.
     *
     * The input to this method is two array list (tree one and tree two's) data
     * and the return of this method is the merge array list
     * the merged array list contains n+m nodes (where n and m is number of nodes
     * in tree one and tree two respectively).
     *
     *
     * @param treeOneData
     * @param treeTwoData
     * @param <K>
     * @param <V>
     * @return
     */
    private static<K, V> ArrayList<Position<Entry<K, V>>>
                        getMeTheMergedList(ArrayList<Position<Entry<K, V>>> treeOneData,
                                           ArrayList<Position<Entry<K, V>>> treeTwoData){
        ArrayList<Position<Entry<K, V>>> mergedListContainsBothTreeNodes
                = new ArrayList<Position<Entry<K, V>>>();

        if(treeOneData != null && treeTwoData != null){
            int indexOne = 0;
            int indexTwo = 0;
            while(indexOne < treeOneData.size() && indexTwo < treeTwoData.size()){
                Position<Entry<K, V>> tempNodeFromListOne = treeOneData.get(indexOne);
                Position<Entry<K, V>> tempNodeFromListTwo = treeTwoData.get(indexTwo);

                if(tempNodeFromListOne == null || tempNodeFromListOne.element() == null){
                    indexOne += 1;
                    continue;
                }
                if(tempNodeFromListTwo == null || tempNodeFromListTwo.element() == null){
                    indexTwo += 1;
                    continue;
                }

                if(Integer.parseInt(tempNodeFromListOne.element().getKey().toString()) <=
                        Integer.parseInt(tempNodeFromListTwo.element().getKey().toString())){
                    mergedListContainsBothTreeNodes.add(tempNodeFromListOne);
                    indexOne += 1;
                }else{
                    mergedListContainsBothTreeNodes.add(tempNodeFromListTwo);
                    indexTwo += 1;
                }
            }
            if(indexOne < treeOneData.size()){
                for(int index = indexOne ; index < treeOneData.size() ; index++){
                    mergedListContainsBothTreeNodes.add(treeOneData.get(index));
                }
            }
            if(indexTwo < treeTwoData.size()){
                for(int index = indexTwo ; index < treeTwoData.size() ; index++){
                    mergedListContainsBothTreeNodes.add(treeTwoData.get(index));
                }
            }
        }
        return mergedListContainsBothTreeNodes;
    }

    /**
     *
     * As mentioned earlier, it is critical to set the height at each level. This
     * method calculate the height.
     *
     * We have to create the final tree from the merge array list. The middle of the array list
     * is the root and the left side of the middle element is the left tree of the root
     * and right size of the middle is the right tree of the root.
     *
     * So the height from the size of the merge array list
     * is
     *
     * log (Size of the array ) / log 2
     *
     * which log 2 of size of array.
     *
     * Time complexicity would be O(1)
     *
     * @param sizeOfTheSortedArrayList
     * @return
     */

    private static int calculateTheHeight(int sizeOfTheSortedArrayList){
        return (int)Math.ceil(Math.log(sizeOfTheSortedArrayList)/Math.log(2));
    }

    /**
     *
     * This is the second method which we were asked to implement. The main task
     * of this method of merge two input trees and return the result.
     *
     * The main bit was (as I explained earlier) height of the AVL tree at each nodes
     * and secondly, if we do in order traversal, we get a sorted array.
     *
     * So the logic is to get in order traversal array for both the trees and then
     * merge these two arrays and created the AVL tree from the resultant array.
     *
     * For time complexicity
     *
     * Tree one has n nodes
     *
     * Tree two has m nodes
     *
     * As metioned earlier first two lines are just creating objects so will not take
     * more than O(1)
     *
     * As per the logic we need to get in order nodes (in array list format). In order
     * to do that we need to visit each and every node.
     *
     * So for tree one - > O(n)
     * and for tree two -> O(m)
     *
     * Total complexicity so far is O(n+m)
     *
     * Next task is two merge two array list. In this we need to traverse both
     * lists
     *
     * So the time complexicty would be O(n+m)
     *
     * The merge helper is being recursively for the resultant array list which has
     * n+m nodes.
     *
     * So the total complexicity is
     *
     * O(n+m) + O(n+m) + O(n+m)
     *
     * which is O(n+m)
     *
     *
     * @param tree1
     * @param tree2
     * @param <K>
     * @param <V>
     */

    public static<K, V> void merge(AVLTree<K, V> tree1, AVLTree<K, V> tree2){
        ArrayList<Position<Entry<K, V>>> listOneForTreeOne = new ArrayList<Position<Entry<K, V>>>();
        ArrayList<Position<Entry<K, V>>> listTwoForTreeTwo = new ArrayList<Position<Entry<K, V>>>();

        getNodesInOrderStyle(tree1, tree1.root(), listOneForTreeOne);
        getNodesInOrderStyle(tree2, tree2.root(), listTwoForTreeTwo);

        ArrayList<Position<Entry<K, V>>> theMergeList = getMeTheMergedList(listOneForTreeOne, listTwoForTreeTwo);

        AVLTree<K, V> theMergedTree = new AVLTree<K, V>();

        theMergedTree.root = mergeHelper(theMergedTree, null, theMergeList, 0, theMergeList.size()-1);

        theMergedTree.numEntries = tree1.numEntries + tree2.numEntries;

        System.out.println(theMergedTree.isBalanced(theMergedTree.root()));
    }

    /**
     * This is the final helper method to complete the merge task. It is same as
     * the clone helper method. It calls itself for the n+m time which is the total
     * size of the merge array list (as mentioned n is tree one and m is tree two nodes).
     *
     * time complexicity would be O(c).
     *
     * c = n + m
     *
     * All other operations in this method take constant time.
     *
     * @param theMainTree
     * @param parent
     * @param theList
     * @param startPosition
     * @param endPosition
     * @param <K>
     * @param <V>
     * @return
     */
    public static<K, V> AVLNode mergeHelper(AVLTree<K, V> theMainTree, AVLNode<K, V> parent,
                                            ArrayList<Position<Entry<K, V>>> theList, int startPosition, int endPosition){

        if(startPosition > endPosition){
            return null;
        }else{
            int mid = (startPosition+endPosition)/2;

            AVLNode theNode = (AVLNode) theMainTree
                    .createNode(theList.get(mid).element(), parent, null, null);

            theNode.setHeight(calculateTheHeight(endPosition+1));

            theNode.setLeft(mergeHelper(theMainTree, (AVLNode)theNode, theList, startPosition, mid-1));
            theNode.setRight(mergeHelper(theMainTree, (AVLNode)theNode, theList, mid+1, endPosition));
            return theNode;
        }
    }



class NodeOvalSettings{
    private int HEIGHT;
    private int WIDTH;

    public NodeOvalSettings(int w, int h){
        HEIGHT = h;
        WIDTH = w;
    }

    public NodeOvalSettings(){
        this(50, 50);
    }
    public int getWIDTH(){
        return this.WIDTH;
    }
    public int getHEIGHT(){
        return this.HEIGHT;
    }

}

/**
 *
 * Right Ideally, this class should have been in a different file name. Oh well,!!!!!!????!!!!
 *
 * This class extends the JPanel class and draw the tree. I'm printing or drawing the tree
 * by calling the print tree method recurrecivaly.
 *
 * I'm not sure if we are needed to provide any time complexicity analysis for drawing methods.
 *
 * Even if we do need to at least it will take O(n) where n is the number of nodes in the
 * tree. I'm not considering other methods calls such as draw oval, fill oval, etc.
 *
 * @param <K>
 * @param <V>
 */
class TheUglyDesignOfMyPrintTreeClassThisShouldHaveBeenInADifferentFile<K, V> extends JPanel{

    private Graphics graphics = null;
    AVLTree<K, V> tree = null;
    private int STARTING_WIDTH;
    private int STARTING_HEIGHT;
    private int SPACE_BETWEEN_TWO_NODES_AND_LEVELS = 70;
//    private int LEFT_SPACE = 40;
//    private int RIGHT_SPACE = 30;
    private static int  TOTAL_TREE_HEIGHT;
    private enum NODE_TYPE{
        INTERNAL,
        EXTERNAL;
    }

    /**
     *
     * Overloaded method to draw the tree!!!
     *
     * @param graphics
     */

    @Override
    public void paintComponent(Graphics graphics) {
        System.out.println(this.getWidth());
        STARTING_WIDTH = this.getWidth()/2;
        STARTING_HEIGHT = 50;

        this.graphics = graphics;
        this.drawTheTree();
    }

    public TheUglyDesignOfMyPrintTreeClassThisShouldHaveBeenInADifferentFile(AVLTree<K, V> tree){
        this.tree = tree;
        TOTAL_TREE_HEIGHT = tree.height(tree.root());

    }
    private void drawTheTree(){
        NodeOvalSettings settings = new NodeOvalSettings();
        AVLTree.AVLNode theRoot = (AVLTree.AVLNode)tree.root();

        this.printNodes(tree, theRoot, STARTING_WIDTH, STARTING_HEIGHT, settings);

    }

    /**
     *
     * The main method which does the magic of drawing the tree. I exepct the fact that the leaves and
     * the external nodes are not being printed properly. However, it doesn't cross the line which was
     * the main requirement of this task.
     *
     * As mentioned, this method calls itself recursively and does the magic by calculating the X and Y
     * position of each node and each level.
     *
     * In the process of drawing the tree, it calls other helper methods which do the actual printing.
     * These methods are drawInternal, drawExternal etc.
     *
     * @param tree
     * @param node
     * @param xPosition
     * @param yPosition
     * @param settings
     */
    private void printNodes(AVLTree<K, V> tree
            ,Position<Entry<K, V>> node
            ,int xPosition
            ,int yPosition
            ,NodeOvalSettings settings){



        if(tree.hasLeft(node)){
            printNodes(tree, tree.left(node)
                    , xPosition-(int)(this.getWidth()/ Math.pow(2, (1+TOTAL_TREE_HEIGHT-tree.height(tree.left(node)))))
                    , yPosition+SPACE_BETWEEN_TWO_NODES_AND_LEVELS
                    , settings);
            if(tree.isInternal(tree.left(node))){
                drawALine(xPosition+settings.getWIDTH()/2
                        , yPosition+settings.getHEIGHT()
                        , xPosition-(int)(this.getWidth()/ Math.pow(2, (1+TOTAL_TREE_HEIGHT-tree.height(tree.left(node)))))+settings.getWIDTH()
                        , yPosition+SPACE_BETWEEN_TWO_NODES_AND_LEVELS+settings.getHEIGHT()/2);
            }
        }


        if(node.element() != null){
            if(tree.left(node).element() != null || tree.right(node).element() != null){
                this.drawInternalNode(xPosition, yPosition, (Integer)node.element().getKey(), settings);
            }else{
                this.drawExternalNode(xPosition, yPosition, (Integer)node.element().getKey(), settings);
            }
        }
        if(tree.hasRight(node)){
            printNodes(tree
                    , tree.right(node)
                    , xPosition+(int)(this.getWidth()/ Math.pow(2, (1+TOTAL_TREE_HEIGHT-tree.height(tree.right(node)))))
                    , yPosition+SPACE_BETWEEN_TWO_NODES_AND_LEVELS
                    , settings);
            if(tree.isInternal(tree.right(node))){
                drawALine(xPosition+settings.getWIDTH()/2
                        , yPosition+settings.getHEIGHT()
                        , xPosition+(int)(this.getWidth()/ Math.pow(2, (1+TOTAL_TREE_HEIGHT-tree.height(tree.right(node)))))
                        , yPosition+SPACE_BETWEEN_TWO_NODES_AND_LEVELS+settings.getHEIGHT()/2);
            }
        }
    }

    private void drawInternalNode(int xPosition, int yPosition, Integer key, NodeOvalSettings settings){
        this.drawNode(NODE_TYPE.INTERNAL, xPosition, yPosition, key, settings);
    }
    private void drawExternalNode(int xPosition, int yPosition, Integer key, NodeOvalSettings settings){
        this.drawNode(NODE_TYPE.EXTERNAL, xPosition, yPosition, key, settings);
    }
    private void drawNode(NODE_TYPE node_type, int xPosition, int yPosition, Integer key, NodeOvalSettings settings){
        if(node_type == NODE_TYPE.INTERNAL){
            graphics.setColor(Color.LIGHT_GRAY);
            graphics.fillOval(xPosition, yPosition, settings.getWIDTH(), settings.getHEIGHT());
        }else{
            graphics.setColor(Color.CYAN);
            graphics.fillRect(xPosition, yPosition, settings.getWIDTH()/2, settings.getHEIGHT()/2);
        }
        FontMetrics fm = graphics.getFontMetrics();
        double textWidth = fm.getStringBounds(String.valueOf(key), graphics).getWidth();
        Font font = new Font("Verdana", Font.PLAIN, 12);
        graphics.setFont(font);
        graphics.setColor(Color.BLACK);
        graphics.drawString(String.valueOf(key),
                xPosition+settings.getWIDTH()-(int)(2*textWidth),
                yPosition+((settings.getHEIGHT() + fm.getHeight() - fm.getMaxAscent()) / 2));
    }
    public void drawALine(int startX, int startY, int endX, int endY){
        graphics.setColor(Color.BLACK);
        graphics.drawLine(startX, startY, endX, endY);
    }
}