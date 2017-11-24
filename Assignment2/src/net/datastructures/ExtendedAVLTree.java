package net.datastructures;

import java.awt.*;
import javax.swing.*;



/**
 * Assignment2
 * @author yisong jiang
 * @author z5123920
 * @category COMP9024
 */


public class ExtendedAVLTree<K, V> extends AVLTree<K, V>{
	public static<K,V> void main(String[] args) {
        String values1[]={"Sydney", "Beijing","Shanghai", "New York", "Tokyo", "Berlin",
       "Athens", "Paris", "London", "Cairo"}; 
        int keys1[]={20, 8, 5, 30, 22, 40, 12, 10, 3, 5};
        String values2[]={"Fox", "Lion", "Dog", "Sheep", "Rabbit", "Fish"}; 
        int keys2[]={40, 7, 5, 32, 20, 30};
           
        /* Create the first AVL tree with an external node as the root and the
       default comparator */ 
           
          AVLTree<Integer, String> tree1=new AVLTree<Integer, String>();

        // Insert 10 nodes into the first tree
           
          for ( int i=0; i<10; i++)
              tree1.insert(keys1[i], values1[i]);
         
        /* Create the second AVL tree with an external node as the root and the
       default comparator */
           
          AVLTree<Integer, String> tree2=new AVLTree<Integer, String>();
         
        // Insert 6 nodes into the tree
           
          for ( int i=0; i<6; i++)
              tree2.insert(keys2[i], values2[i]);
           
          ExtendedAVLTree.print(tree1);
          ExtendedAVLTree.print(tree2); 
          ExtendedAVLTree.print(ExtendedAVLTree.clone(tree1));
          ExtendedAVLTree.print(ExtendedAVLTree.clone(tree2));
          
          ExtendedAVLTree.print(ExtendedAVLTree.merge(ExtendedAVLTree.clone(tree1), 
          ExtendedAVLTree.clone(tree2)));

	}
	
	/**
	 * Next two method positionlistprint and printarray are functions to print these list to check.
	 * positionlistprint can print NodePositionList
	 * printarray can print IndexList
	 */
    public static<K,V> void positionlistprint(NodePositionList<Position<Entry<K,V>>> pos){
    	DNode<Position<Entry<K,V>>> pointer = pos.header;
    	while(pointer.getNext()!= pos.trailer){
    		pointer = pointer.getNext();
    		if (pointer.element().element()!=null){
    			System.out.println(pointer.element().element().getKey());
    		}
    		else{
    			System.out.println("Leaf");
    		}
    	}
    }
    public static<K,V> void printarray(ArrayIndexList<Position<Entry<K,V>>> pos){
    	for(int i =0;i<pos.size();i++){
    		int kkk = (int)pos.get(i).element().getKey();
    		System.out.println(kkk);
    	}
    }
	/**
	 * This class method creates an identical copy of the AVL tree specified by the
	 * parameter and returns a reference to the new AVL tree.
	 * @param tree   the original tree which we want to clone
	 * @return the reference to the new AVL tree
	 * Firstly clone the root, takes O(1)
	 * Then use the method clone_childtree to clone the subtree.
	 * clone_childtree will have a recursion.
	 * Each recursion will set one or two nodes ,so takes O(1) 
	 * there will be [n/2,n] recursion , so the method costs O(n)
	 * Total time complexity: O(n).
	 */
	public static <K, V> AVLTree<K, V> clone(AVLTree<K,V> tree){
		AVLTree<K, V> clonedtree = new AVLTree<K, V>();
		clonedtree.addRoot(tree.root.element());
		// tree.root will return as BTPosition, but tree.root() will return as Position
		clone_childtree(tree.root,clonedtree.root);
		clonedtree.numEntries = tree.numEntries;
		return clonedtree;
	}


	public static <K,V> void clone_childtree(BTPosition<Entry<K, V>> original, BTPosition<Entry<K,V>> duplicate){
		// The height is set directly from the original tree node.
		// alternatively  we can use hasLeft() and hasRight()
		// But the two methods only takes Position type
		// so we use getLeft and getRight to avoid type convert
		((AVLNode<K,V>)duplicate).setHeight(((AVLNode<K,V>)original).height);
		if (original.getLeft() != null){
			AVLNode<K,V> left_childtree = new AVLNode<K,V>();
			duplicate.setLeft(left_childtree);
			left_childtree.setParent(duplicate);
			left_childtree.setElement(original.getLeft().element());
			clone_childtree(original.getLeft(),left_childtree);
		}
		if (original.getRight() != null){
			AVLNode<K,V> right_childtree = new AVLNode<K,V>();
			duplicate.setRight(right_childtree);
			right_childtree.setParent(duplicate);
			right_childtree.setElement(original.getRight().element());
			clone_childtree(original.getRight(),right_childtree);
		}

	}
	/**
	 * merges two AVL trees, tree1 and tree2, into a new tree, and destroys tree1 and tree2.
	 * @param tree1
	 * @param tree2
	 * @return tree1 merge tree2
	 * assume size of tree1 is n, size of tree2 is m
	 * Firstly, we get all the elements in two trees and put them into NodePositionList. O(n)+O(m)
	 * Then put them together into a Arraylist. Since method inorderPosition will produce The NodePositionList
	 * with a sorted linked list. We will use usual insertSort to get the Arraylist. InserSort is a typical sort method
	 * and in this case it just need to traverse two PositionList once, so O(m+n).
	 * After we got the Arraylist, we began to build the tree.
	 * Use recursion method btree to build tree.
	 * This method start from the middle of one arraylist and continue divide list to two parts.
	 * Each recursion will cost O(1) and there will be log2(m+n) times recursion, so build tree takes O(log(m+n))
	 * So the total time complexity of merging two trees is O(m+n)
	 */
	public static <K, V> AVLTree<K, V> merge(AVLTree<K,V> tree1, AVLTree<K,V> tree2 ){
		NodePositionList<Position<Entry<K,V>>> elementlist1 = new NodePositionList<Position<Entry<K,V>>>();
		NodePositionList<Position<Entry<K,V>>> elementlist2 = new NodePositionList<Position<Entry<K,V>>>();
        tree1.inorderPositions(tree1.root(),elementlist1);
        tree2.inorderPositions(tree2.root(),elementlist2);
        ArrayIndexList<Position<Entry<K,V>>> mergelist = insertSort(elementlist1,elementlist2);
        //positionlistprint(elementlist1);
		//printarray(mergelist);
		AVLTree<K, V> mergetree = new AVLTree<K, V>();
		mergetree.root = btree(mergelist,0,mergelist.size()-1);
		mergetree.numEntries = tree1.numEntries+tree2.numEntries;
		tree1=null;
		tree2=null;
		return mergetree;
	}
	public static<K,V> ArrayIndexList<Position<Entry<K,V>>> insertSort(NodePositionList<Position<Entry<K,V>>> original_list1, 
			NodePositionList<Position<Entry<K,V>>> original_list2){
		// we use arrayindex list instead of NodePositionList next, because when we build tree, we need a convenience way to
		// get the node in certain position. Arraylist method get() can do that well.
		ArrayIndexList<Position<Entry<K,V>>> sorted_list = new ArrayIndexList<Position<Entry<K,V>>>();
		DNode<Position<Entry<K,V>>> pointer1 = original_list1.header;
		DNode<Position<Entry<K,V>>> pointer2 = original_list2.header;
		pointer1 = pointer1.getNext();
		pointer2 = pointer2.getNext();
		// compare elements of two sorted list, add the smaller to the arrylist
		while((pointer1.getNext()!= original_list1.trailer)&&(pointer2.getNext()!= original_list2.trailer)){
			if(pointer1 == original_list1.trailer||pointer2 ==original_list2.trailer){
				break;
			}
			if(pointer1.element().element()==null){
				pointer1 = pointer1.getNext();
			}

			if(pointer2.element().element()==null){
				pointer2 = pointer2.getNext();
			}

			int key1 = (int) pointer1.element().element().getKey();
			int key2 = (int) pointer2.element().element().getKey();
			if (key1<=key2){
				sorted_list.add(sorted_list.size(),pointer1.element());
				pointer1 = pointer1.getNext();
			}
			else{
				sorted_list.add(sorted_list.size(),pointer2.element());
				pointer2 = pointer2.getNext();
			}				
		}
		// There must be one list that is not totally added to arrylist here
		// so then push the rest of them into the arrylist.
		while(pointer1!= original_list1.trailer && pointer1!=null){
			if(pointer1.element().element()==null){
				if (pointer1.getNext()==original_list1.trailer){
					break;
				}
				else{
					pointer1 = pointer1.getNext();					
				}
			}
			sorted_list.add(sorted_list.size(),pointer1.element());
			pointer1 = pointer1.getNext();
		}
		while(pointer2!= original_list2.trailer&& pointer2!=null){
			if(pointer2.element().element()==null){
				if (pointer2.getNext()==original_list2.trailer){
					break;
				}
				else{
					pointer2 = pointer2.getNext();
				}
			}
			sorted_list.add(sorted_list.size(),pointer2.element());
			pointer2 = pointer2.getNext();
		}		
		
		return sorted_list;
	}

	/**
	 * The main idea to build AVLTree is keep taking out the middle element of the arraylist.
	 * To its right , all elements are bigger than it
	 * To its left , all elements are smaller than it
	 * keep dividing the arraylist from middle, so it takes log2(n+m) times recursion
	 * Each recursion just set some attributes, so each recursion takes constant time.
	 * Hence, The total build tree method time complexity is O(log(n+m))
	 */
	public static<K,V> AVLNode<K,V> btree(ArrayIndexList<Position<Entry<K,V>>> nodelist, int start, int end){
		AVLNode<K, V> needednode = new AVLNode<K, V>();
		if (start>end){
			return needednode;
		}
		int mid = start+(end-start)/2;
		needednode.setElement(nodelist.get(mid).element());
		AVLNode<K, V> leftnode = btree(nodelist,start,mid-1);
		if (leftnode!=null){
			leftnode.setParent(needednode);
		}
		needednode.setLeft(leftnode);
		AVLNode<K, V> rightnode = btree(nodelist,mid+1,end);
		if (rightnode!=null){
			rightnode.setParent(needednode);
		}
		needednode.setRight(rightnode);
		// node height is the bigger height of its left node and right node.
		if (leftnode.height>rightnode.height){
			needednode.setHeight(leftnode.height+1);
		}
		else{
			needednode.setHeight(rightnode.height+1);
		}
		return needednode;
	}
	
	/**
	 * This class method creates a new window and prints the AVL tree specified by the parameter on the new window.
	 * Each internal node is displayed by a circle containing its key and each external node is displayed by a rectangle.
	 * @param tree
	 * Create a JFrame and set normal attributes of it. The size is set as the almost the size of screen size.
	 * Then print the avltree start from root by method printsubtree. 
	 */
	public static<K,V> void print(AVLTree<K, V> tree){
		JFrame myframe = new JFrame();
		myframe.setVisible(true);
		myframe.setTitle("COMP9024 Assignment2");
		myframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		myframe.setSize(dimension.width-100,dimension.height-100);
		printSubTree(tree.root, myframe, dimension.width/2, 0, dimension.height/2, 0, 35, 32, 32);		
	}

	/**
	 * print one tree node as well as its "legs"
	 * @param position the tree node
	 * @param myframe the new window
	 * @param xpos x-position
	 * @param ypos y-position
	 * @param gap  the distance between two node tree 
	 * @param height  the height of node. This height is not same as the node height. It starts from 0 in root. 
	 * @param circle_D  the diameter of circle (internal node)
	 * @param rec_height the height of rectangle (external node)
	 * @param rec_width the width of rectangle (external node)
	 * There is no special principle to design the size. I try many times to get a proper size of everything
	 */
    public static <K, V> void printSubTree(BTPosition<Entry<K,V>> position, JFrame myframe,
    		int xpos, int ypos, int gap, int height, int circle_D, int rec_height, int rec_width) {
    	//create a internal node in the new window, including a circle, the key string and its legs to child nodes.
        myframe.getRootPane().getContentPane().add(new Internal(xpos-circle_D/2, ypos, circle_D));
        myframe.setVisible(true);
        myframe.getRootPane().getContentPane().add(new Text(position.element().getKey().toString(), xpos, ypos-height));
        myframe.setVisible(true);
        myframe.getRootPane().getContentPane().add(new Line(xpos, ypos + circle_D, xpos - gap/2, ypos + 75));
        myframe.setVisible(true);
        myframe.getRootPane().getContentPane().add(new Line(xpos, ypos + circle_D, xpos + gap/2, ypos + 75));
        myframe.setVisible(true);
        
        // if a treenode has left or right tree and the child treenode is internal
        // Then get into the recursion to get to the bottom of the tree that is a external node
        // create a rectangle for each external node
        if (position.getLeft() != null && position.getLeft().element() != null) {
            printSubTree(position.getLeft(), myframe, xpos - gap/2, ypos + 75, gap/2, 
            		height + 1, circle_D - height, rec_height - height*2, rec_width - height*2);
        } else {
            myframe.getRootPane().getContentPane().add(new External(xpos - gap/2 - rec_height/2, ypos + 75, rec_height, rec_width));
            myframe.setVisible(true);
        }

        if (position.getRight() != null && position.getRight().element() != null) { 
            printSubTree(position.getRight(), myframe, xpos + gap/2, ypos + 75, gap/2,
            		height + 1, circle_D - height, rec_height - height*2, rec_width - height*2);
        } else { 
            myframe.getRootPane().getContentPane().add(new External(xpos + gap/2 - rec_height/2, ypos + 75, rec_height, rec_width));
            myframe.setVisible(true);
        }        
    }
    // The last is some Component classes to help produce graphics, including circle, rectangle, line and words.
    // Each static class will override the paintComponent.
    public static class Internal extends JComponent {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		int xpos = 0;
        int ypos = 0;
        int height = 0;
        int width = 0;        
        Internal(int x, int y, int d) {
            this.xpos = x;
            this.ypos = y;
            this.height = d;
            this.width = d;
        }        
        @Override
        protected void paintComponent(Graphics g) {
        	float lineWidth = 3.0f;
            ((Graphics2D)g).setStroke(new BasicStroke(lineWidth));
            g.setColor(new Color(0,119,0));
            g.drawOval(xpos, ypos, height, width);
        }
    }
    
    public static class External extends JComponent {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		int xpos = 0;
        int ypos = 0;
        int height = 0;
        int width = 0;        
        External(int x, int y, int h, int w) {
            this.xpos = x; 
            this.ypos = y;
            this.height = h;
            this.width = w;
        }        
        @Override
        protected void paintComponent(Graphics g) {
        	g.setColor(Color.BLUE);
            g.fillRect(xpos, ypos, height, width);
        }
    }
    
    public static class Line extends JComponent {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		int xpos1 = 0;
        int ypos1 = 0;
        int xpos2 = 0;
        int ypos2 = 0;
        
        Line(int x1, int y1, int x2, int y2) {
            this.xpos1 = x1;
            this.ypos1 = y1;
            this.xpos2 = x2;
            this.ypos2 = y2;
        }
        
        @Override
        protected void paintComponent(Graphics g) {
        	float lineWidth = 2.0f;
            ((Graphics2D)g).setStroke(new BasicStroke(lineWidth));
            g.setColor(new Color(0,119,0));
            ((Graphics2D)g).drawLine(xpos1, ypos1, xpos2, ypos2);
        }
    }

    public static class Text extends JComponent {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		String key;
        int xpos = 0;
        int ypos = 0;
        
        Text(String kk, int x, int y) {
            this.key = kk;
            this.xpos = x;
            this.ypos = y;
        }
        
        @Override
        protected void paintComponent(Graphics g) {
        	FontMetrics ff = g.getFontMetrics();
        	double tw = ff.getStringBounds(key, g).getWidth();
        	int fh = ff.getMaxAscent();
            g.setColor(Color.BLACK);
            g.drawString(key, (int)(xpos-tw/2), ypos+ff.getHeight()+fh/2);
        }
    }
}

