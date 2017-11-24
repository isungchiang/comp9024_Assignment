import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

/**
 * COMP9024 17s1 Assignment4
 * @author yisong jiang z5123920
 */

public class CompactCompressedSuffixTrie {
	private Treenode root;
	private String content;
	
	/*
	 * First problem, we need to construct a compactcompressed suffix trie
	 * Assume the length of input is n.
	 * 1. Read the file content and check the input. O(n)
	 * 2. Construct n Treenodes and set them as the children of root. O(n)
	 * 3. method union().  O(n^2)
	 * 4. method compress(). O(n^2)
	 * Hence total time complexity to construct a trie is O(n^2).
	 */
	public CompactCompressedSuffixTrie(String f){
		root = new Treenode();
		content = "";
		Scanner filescanner = null;				
		try{
			filescanner = new Scanner(new BufferedReader(new FileReader(f)));
			while(filescanner.hasNextLine()){
				String line = filescanner.nextLine();
				content = content + line;
			}
		} catch (FileNotFoundException e) {
			System.out.println("file1 does not exist");
			System.exit(-1);
		}finally{ 
			filescanner.close(); 
		}
		if (content.length()==0){
			System.out.println("Empty file");
			System.exit(-1);
		}
		for (int i = 0;i<=content.length()-1;i++){
			char c = content.charAt(i);
			if (c!='A'&&c!='C'&&c!='G'&&c!='T'){
				System.out.println("Incorrect input!");
				System.exit(-1);
			}
		}
		for (int i = 0; i<=content.length()-1;i++){
			Treenode single = new Treenode();
			single.setstart(i);
			single.setedge(content.substring(i));
			root.addchild(single.getedge(),single);
		}
		root = union(root);
		compress(root);
	}
	
	/*
	 * union is a function that replace the original node to a unioned node.
	 * The meaning of unioned is that all of the child has different first char.
	 * We seek the children that has same beginning and make a new Treenode for them.
	 * After union one node, we need to union its children.	 
	 * Each union will set several treenode, edge and start of a new Treenode. Each cost constant time.
	 * In order to make the Treenode's children are unioned too, a recursion will performed at last.
	 * At at most we need perform union on n*n Treenodes (including all the children).
	 * Hence time complexity for union(root) is O(n^2).
	 */
	public Treenode union(Treenode mynode){
		Treenode rebuildnode = mynode.clonenode();
		Treenode finalnode = rebuildnode.clonenode();
		for (Treenode child:mynode.getchildren().values()){
			char childhead = child.getedge().charAt(0);
			for (Treenode otherchild:mynode.getchildren().values()){				
				if (otherchild.getstart()==child.getstart()){
					continue;
				}
				if (otherchild.getedge().charAt(0)==childhead&&otherchild.getedge().length()>1){
					Treenode sameheadnode = new Treenode();
					sameheadnode.setstart(otherchild.getstart()+1);
					sameheadnode.setedge(otherchild.getedge().substring(1));
					child.addchild(sameheadnode.getedge(),sameheadnode);
				}
			}
			if (child.getchildren().size()>=1&&child.getedge().length()>1){
				Treenode headnode = new Treenode();
				headnode.setstart(child.getstart()+1);
				headnode.setedge(child.getedge().substring(1));
				child.addchild(headnode.getedge(),headnode);				
			}
		} 
		for (Treenode child:mynode.getchildren().values()){
			if (!child.getchildren().isEmpty()){
				String kk = child.getedge().substring(0,1);
				child.setedge(kk);
			}
			rebuildnode.addchild(child.getedge(),child);
		}
		
		for(Treenode newchild:rebuildnode.getchildren().values()){
			if (!newchild.getchildren().isEmpty()){
				Treenode cccnode = new Treenode();
				cccnode = union(newchild);
				finalnode.addchild(cccnode.getedge(),cccnode);
			}
			finalnode.addchild(newchild.getedge().substring(0,1), newchild);

		}
		return finalnode;
	}

	/*
	 * Compress method will compress a Treenode that has only one child to a single node.
	 * In other words, if a node dont have branch, then the node's edge should extend to its child's edge.
	 * And let child's children become original node's children.
	 * Each compress will cost constant time.
	 * At most n^2 nodes will be compressed.
	 * Hence time complexity is O(n^2).
	 */
	public void compress(Treenode mynode){
		if (mynode.getchildren().isEmpty()){
			return;
		}
		for (Treenode child:mynode.getchildren().values()){
			compress(child);
		}
		if (mynode.getchildren().size()>=2){
			return;
		}
		

		for (Treenode child:mynode.getchildren().values()){
			compress(child);
			mynode.setchildren(child.getchildren());
			String newedge = mynode.getedge()+child.getedge();
			mynode.setedge(newedge);
		}
	}	
	
	
	/*
	 * Findstring(string) will return the start index of particular string in the trie.
	 * Start from the first char in the string to the last char.
	 * If some edge has a key equal to the char, then go on in this edge.
	 * Else search the children of current node and search if there is a child has such a key.
	 * If there is a one, then continue. Else, there is no such a string, return -1.
	 * Each match will cost constant time and we need to traverse the string.
	 * Hence, time complexity is O(|s|).  (|s|=length of the string)
	 */
	public int findString(String s)
	{
		Treenode currentnode = root;
		int index = -1;		
		boolean inEdge = false;
		int string_index = 0;
		int in_edge_index = 1;
		while(string_index<=s.length())
		{
			if(string_index==s.length())
			{
				index = currentnode.getstart()+in_edge_index-s.length();
				return index;
			}
			if(!inEdge)
			{
				currentnode = currentnode.getchildren().get(s.substring(string_index,string_index+1));
				if(currentnode == null)
					return -1;
				if(currentnode.getedge().length()>1)
				{
					inEdge = true;
					in_edge_index = 1;
				}
				string_index++;				
			}
			else
			{
				while(in_edge_index<currentnode.getedge().length()&&string_index<s.length())
				{
					if(s.charAt(string_index)!=currentnode.getedge().charAt(in_edge_index))
						return index;
					else
					{
						string_index++;
						in_edge_index++;
					}
					
				}
				inEdge = false;
			}			
		}		
		return index;
	}
	
	/*
	 * kLongestSubstrings will return k times longest substring, and they dont overlap each other.
	 * 1. Read two files and check if valid. O(n+m)
	 * 2. Build a dp table for the length of substring in each index in both two string.
	 * At the same time, obtain the longest one and store it in the output string.
	 * After that, we reomve the longest common substring from both strings.   O(mn).
	 * 3. repeat step2 k times.
	 * 4. Output the result. O(1).
	 * Hence, total time complexity is O(kmn).
	 */
	public static void kLongestSubstrings(String f1, String f2, String f3, int k){
		String first = "";
		String second = "";
		String output = "";
		Scanner filescanner1 = null, filescanner2 = null;				
		try{
			filescanner1 = new Scanner(new BufferedReader(new FileReader(f1)));
			filescanner2 = new Scanner(new BufferedReader(new FileReader(f2)));
			while(filescanner1.hasNextLine()){
				String line = filescanner1.nextLine();
				first = first + line;
			}
			while(filescanner2.hasNextLine()){
				String line = filescanner2.nextLine();
				second = second + line;
			}
		} catch (FileNotFoundException e) {
			System.out.println("file does not exist");
			System.exit(-1);
		}finally{ 
			filescanner1.close();
			filescanner2.close();
		}
		if (first.length()==0||second.length()==0){
			System.out.println("Empty file");
			System.exit(-1);
		}
		for (int i = 0;i<=first.length()-1;i++){
			char c = first.charAt(i);
			if (c!='A'&&c!='C'&&c!='G'&&c!='T'){
				System.out.println("Incorrect input!");
				System.exit(-1);
			}
		}
		for (int i = 0;i<=second.length()-1;i++){
			char c = second.charAt(i);
			if (c!='A'&&c!='C'&&c!='G'&&c!='T'){
				System.out.println("Incorrect input!");
				System.exit(-1);
			}
		}

		for (int count=1;count<=k;count++){
			if (first.length()==0 || second.length()==0){
				return;
			}
			int m = first.length();
			int n = second.length();
			int[][] dp = new int[m+1][n+1];		 
			int maxLen = 0, End1 = -1, End2 = -1;
			for(int i=1;i<m+1;i++) {
				for(int j=1;j<n+1;j++) {
					if(first.charAt(i-1) == second.charAt(j-1)) {
						dp[i][j] = dp[i-1][j-1] + 1;
						if(dp[i][j] > maxLen) {
							maxLen = dp[i][j];
							End1 = i;
							End2 = j;
						}
					}
					else
						dp[i][j] = 0;
				}
			}
			if (maxLen==0){
				break;
			}
			output = output +count+": "+first.substring(End1-maxLen,End1)+"\n";
			// Add two special chars into two strings to prevent a new substring appear after removing the longest one.
			first = first.substring(0,End1-maxLen)+"$"+first.substring(End1);
			second = second.substring(0,End2-maxLen)+"#"+second.substring(End2);
		}
		File writeto = new File(f3);
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter(writeto.getName(), false));
			bw.write(output);
			bw.close();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
}

/*
 * Treenode class
 * Represent the node in the trie.
 * Edge is its actually value and start is its index in the string.
 * Children are represented by a hashmap, which the first char of edge are keys.
 */
class Treenode{
	private String edge;
	private int start;
	private HashMap<String,Treenode> children;
	
	public Treenode clonenode(){
		Treenode cloned = new Treenode();
		cloned.setstart(this.start);
		cloned.setedge(this.edge);
		return cloned;
	}
	public Treenode(){
		this.children = new HashMap<String,Treenode>();
	}
	public String getedge(){
		return this.edge;
	} 
	public int getstart(){
		return this.start;
	}
	public HashMap<String,Treenode> getchildren(){
		return this.children;
	}
    public void setedge(String tx) {
        this.edge = tx;
    }
    public void setstart(int st) {
        this.start = st;
    }
	public void setchildren(HashMap<String,Treenode> ch){
		this.children = ch;
	}
	public void addchild(String k, Treenode v){
		if (this.children.get(k)==null){
			this.children.put(k, v);
		}
		else{
			if(v.getstart()<this.children.get(k).getstart()){
				this.children.put(k, v);
			}
		}
	}
	
}