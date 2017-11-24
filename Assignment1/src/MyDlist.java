import java.io.*;
import java.util.Scanner;
/*
 * assignment1 of comp9024
 * written by yisong jiang (z5123920)
 */
public class MyDlist extends DList{
	//creates an empty doubly linked list.
	public MyDlist() {super();}
	
	//creates a doubly linked list by reading all strings from a text file
	public MyDlist(String f) throws IOException{
		super();
		Scanner StdInput = null, FileInput = null, lineScanner = null;
		try{
			// get data from standard input
			if(f == "stdin"){
				StdInput = new Scanner(System.in);
				while(true){
					String line1 = StdInput.nextLine();
					if(line1.equals("")){
						break;
					}
					else{
						DNode aNode = new DNode(line1,null,null);
						this.addLast(aNode);
					}
				}
			}
			// get data from text file
			else{
				FileInput = new Scanner(new BufferedReader(new FileReader(f)));
				while(FileInput.hasNextLine()){
					String line2 = FileInput.nextLine();
					lineScanner = new Scanner(line2);
					while(lineScanner.hasNext()){
						DNode bNode = new DNode(lineScanner.next(),null,null);
						this.addLast(bNode);
					}
				}
			}
		} finally{
			// close the scanners that are used
			if(StdInput != null){
				StdInput.close();
			}
			if(FileInput != null){
				FileInput.close();
			}
			if(lineScanner != null){
				lineScanner.close();
			}
		}
	}
	
	//prints all elements of a list on the standard output
	public static void printList(MyDlist u){
		if (!u.isEmpty()){
			DNode currentnode = u.getFirst();
			while (currentnode!=u.trailer){
				System.out.println(currentnode.getElement());
				currentnode = currentnode.getNext();
			}
		}
	}
	/* 
	 * creates an identical copy of a doubly linked list	u and returns the resulting doubly linked list.
	 * getFirst function take only constant time O(1) and each check condition takes O(1).
	 * The loop actually executed n-1 times(n is the list size) and each takes constant time.
	 * The total time complexity is O(n).	
	*/
	public static MyDlist cloneList(MyDlist u){
		MyDlist duplicateList = new MyDlist();
		if (!u.isEmpty()){
			DNode currentnode = u.getFirst();
			while(currentnode!=u.trailer){
				DNode eachNode = new DNode(currentnode.getElement(),null,null);
				duplicateList.addLast(eachNode);
				currentnode = currentnode.getNext();
			}
		}
		return duplicateList;		
	}
	/*
	 * The method "has" is a function that check if a MyDlist has a particular value DNode in its nodes.
	 * This is being used as part of union and intersection methods.
	 * getFirst function take only constant time O(1) and each check condition takes O(1).
	 * It breaks if a same node is found. But in worst case, the loop will be executed n times (n is the list size).
	 * So the time complexity of the method is O(n).
	 */
	public static boolean has(DNode checkNode,MyDlist checkList){
        boolean has = false;
        DNode currentnode = checkList.getFirst();
        while(currentnode!=checkList.trailer){
            if(currentnode.getElement().equals(checkNode.getElement())){
                has = true;
                break;
            }
            currentnode = currentnode.getNext();
        };
        return has;
    }
	/*
	 * Assume that u.size = n, v.size = m.
	 * cloneList method cost O(n).
	 * getFirst function take only constant time O(1).
	 * The while loop will executed m times.
	 * has method cost O(n)	and other process in the loop always have constant time consuming.
	 * Total time complexity would be O(1)+O(n) + m * (O(n)+O(1)) = O(1) + O(m) + O(n) + O(mn);
	 * dropping the O(n), O(1), O(m), so the time complexity is O(mn).
	 */
	public static MyDlist union(MyDlist u, MyDlist v){
		MyDlist unionlist = cloneList(u);		
		DNode pointerv = v.getFirst();

		while(pointerv!=v.trailer){
			if (has(pointerv, unionlist)){
				pointerv = pointerv.getNext();
				continue;
			}
			else{
				DNode eachnode = new DNode(pointerv.getElement(),null,null);
				unionlist.addLast(eachnode);
			}
			pointerv = pointerv.getNext();
		}
		return unionlist;
	}
	/*
	 * Assume that u.size = n, v.size = m.
	 * getFirst function take only constant time O(1).
	 * The loop will executed n times.
	 * has method cost O(m) and other process in the loop always have constant time consuming.
	 * Total time complexity would be O(1) + n * (O(m)+O(1)) = O(1) + O(mn) + O(n).
	 * dropping the O(n), O(1), so the time complexity is O(mn).
	 */
	public static MyDlist intersection(MyDlist u, MyDlist v){
		MyDlist intersectionlist = new MyDlist();
		DNode pointeru = u.getFirst();
		while(pointeru!=u.trailer){
			if (has(pointeru, v)){
				DNode eachnode = new DNode(pointeru.getElement(),null,null);
				intersectionlist.addLast(eachnode);
			}			
			pointeru = pointeru.getNext();
		}
		return intersectionlist;
	}
	public static void main(String[] args) throws Exception{
		

		System.out.println("please type some strings, one string each line and an empty line for the end of input:");
		/** Create the first doubly linked list
	by reading all the strings from the standard input. */
		MyDlist firstList = new MyDlist("stdin");

		/** Print all elements in firstList */
		printList(firstList);

		/** Create the second doubly linked list                         
	by reading all the strings from the file myfile that contains some strings. */

		/** Replace the argument by the full path name of the text file */  
		MyDlist secondList=new MyDlist("myfile.txt");

		/** Print all elements in secondList */                     
		printList(secondList);

		/** Clone firstList */
		MyDlist thirdList = cloneList(firstList);

		/** Print all elements in thirdList. */
		printList(thirdList);

		/** Clone secondList */
		MyDlist fourthList = cloneList(secondList);

		/** Print all elements in fourthList. */
		printList(fourthList);

		/** Compute the union of firstList and secondList */
		MyDlist fifthList = union(firstList, secondList);

		/** Print all elements in thirdList. */ 
		printList(fifthList); 

		/** Compute the intersection of thirdList and fourthList */
		MyDlist sixthList = intersection(thirdList, fourthList);

		/** Print all elements in fourthList. */
		printList(sixthList);
	}
}