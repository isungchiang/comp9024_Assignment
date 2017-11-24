package net.datastructures;

import java.io.*;
import java.util.Comparator;
import java.util.Scanner;


/**
 * @author yisong jiang  z5123920
 * comp9024 Assignment3
 */
public class TaskScheduler {
	
	/**
	 * getinformation() is a function that can read the file and get all the information of tasks.
	 * Store these information into a HeapPriorityQueue.
	 * @param filename
	 * @return HeapPriorityQueue that use task release time as the key
	 * We need to insert all the tasks into a HeapPriorityQueue.
	 * Hence Time complexity: O(nlogn);
	 */
	private static HeapPriorityQueue<Integer,Task> getinformation(String filename){
		HeapPriorityQueue<Integer,Task> myPQu = new HeapPriorityQueue<Integer,Task>();
		Scanner filescanner = null, linescanner = null;
		try{
			filescanner = new Scanner(new BufferedReader(new FileReader(filename)));
			while(filescanner.hasNextLine()){
				// linescanner will scan each line
				String line = filescanner.nextLine();
				linescanner = new Scanner(line);
				while (linescanner.hasNext()){
					Task mytask = new Task();
					// The first element is the name, and it cannot be a digit
					if (linescanner.hasNextInt()){
						linescanner.close();
						System.out.println("input error when reading the attribute of the task"+linescanner.next());
						System.exit(0);
					}
					String tempname = linescanner.next();
					// the task name cannot start will a number
					if (Character.isDigit(tempname.charAt(0))){
						System.out.println("input error when reading the attribute of the task"+tempname);
						System.exit(-1);
					}
					mytask.set_T(tempname);
					
					// Execution ,release time and deadline are all integer.
					if (!linescanner.hasNextInt()){
						System.out.println("input error when reading the attribute of the task"+tempname);
						System.exit(-1);
					}
					mytask.set_e(linescanner.nextInt());
					if (!linescanner.hasNextInt()){
						System.out.println("input error when reading the attribute of the task"+tempname);
						System.exit(-1);
					}
					mytask.set_r(linescanner.nextInt());
					if (!linescanner.hasNextInt()){
						System.out.println("input error when reading the attribute of the task"+tempname);
						System.exit(-1);
					}
					mytask.set_d(linescanner.nextInt());
					// release time can be 0, but deadline and execution time must >0.
					if (mytask.get_e()<=0 || mytask.get_d()<=0 || mytask.get_r()<0){
						System.out.println("input error when reading the attribute of the task"+tempname);
						System.exit(-1);
					}
					// insert tasks into a HeapPriorityQueue.
					myPQu.insert(mytask.get_r(), mytask);
					//System.out.println(mytask.get_T()+" "+mytask.get_r());
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("file1 does not exist");
			System.exit(-1);
		}finally{
				filescanner.close();
				linescanner.close();
		}
		return myPQu;
		
	}
	/**
	 * Scheduler() is the main function to deal with the tasks.
	 * @param file1: the file we read
	 * @param file2: the file we write
	 * @param m: the number of Cores
	 * Firstly, receive the releaseheap from file1(O(nlogn)) and create heaps for deadline/execution and a heap for core(O(1)).
	 * We create a new comparator too. It can select the task with smallest deadline and biggest execution time if deadline is same.
	 * Initialize the coreheap, set all core's available time as 0 and insert them into heap.(m*O(logn)=O(logn)).
	 * Then we go into for loop. The while loop1 will remove the tasks that are ready from releaseheap and insert them into deadlineheap,
	 * Because of the total number of tasks is n, so the time complexity for while loop1(including outside for loop) is O(nlogn).
	 * Next, we get the tasks that has smallest deadline at current time point from deadlineheap. 
	 * Take this task out and assign an earliest available core for it, reset the available time of the core and insert it back to 
	 * coreheap. Functions that use in while loop1&2 are several insert and remove, and the total number of tasks in the whole loop(including outside for loop)
	 * is actually n. In other words, each task will be inserted to heap and removed from heap several times.So time complexity for the main for loop is O(nlogn). 
	 * Write a string to file2 to output result cost O(1).
	 * Hence, total time complexity for this method is O(nlogn)+O(logn)+O(nlogn)+O(1)=O(nlogn)
	 */
	static void scheduler(String file1, String file2, int m) {
		HeapPriorityQueue<Integer,Task> releaseheap = TaskScheduler.getinformation(file1);
		MyComparator mycompar = new MyComparator();
		// the deadline heap will select a task with larger execution time if several tasks have same smallest deadline.
		HeapPriorityQueue<Task,Task> deadlineheap = new HeapPriorityQueue<Task,Task>(mycompar);
		HeapPriorityQueue<Integer,Core> coreheap = new HeapPriorityQueue<Integer,Core>();
		String resultstring = "";
		for(int i=0;i<m;i++){
			Core tcore = new Core();
			tcore.set_name("Core"+(i+1));
			tcore.set_time(0);
			coreheap.insert(tcore.get_time(), tcore);
		}
		for(int max_timepoint =0;!releaseheap.isEmpty();max_timepoint++){
			// while loop1
			while (!releaseheap.isEmpty()&&max_timepoint>=releaseheap.min().getKey()){
				Task rtask = releaseheap.removeMin().getValue();
				deadlineheap.insert(rtask, rtask);
			}
			if (max_timepoint<coreheap.min().getKey()&&!releaseheap.isEmpty()){
				continue;
			}
			// while loop2
			while(!deadlineheap.isEmpty()){
				Core mcore = coreheap.removeMin().getValue();
				Task mtask = deadlineheap.removeMin().getValue();
				if (mcore.get_time()>mtask.get_d()-mtask.get_e()){
					System.out.println("No feasible schedule exists");
					return;
				}
				if (mtask.get_r()<mcore.get_time()){
					mtask.set_s(mcore.get_time());
				}else{
					mtask.set_s(mtask.get_r());
				}
				mtask.set_c(mcore.get_name());
				mcore.set_time(mtask.get_s()+mtask.get_e());
				coreheap.insert(mcore.get_time(), mcore);
				resultstring = resultstring + (mtask.get_T()+" "+mtask.get_c()+ " "+mtask.get_s()+ " ");
			}
		}	
		//System.out.println(resultstring);
		File writeto = new File(file2+".txt");
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter(writeto.getName(), false));
			bw.write(resultstring);
			bw.close();
		}catch (IOException e) {
			e.printStackTrace();
		}
	};

	public static void main(String[] args) {
		
		TaskScheduler.scheduler("samplefile1.txt", "feasibleschedule1", 4);
		/** There is a feasible schedule on 4 cores */      
		TaskScheduler.scheduler("samplefile1.txt", "feasibleschedule2", 3);
		/** There is no feasible schedule on 3 cores */
		TaskScheduler.scheduler("samplefile2.txt", "feasibleschedule3", 5);
		/** There is a feasible scheduler on 5 cores */ 
		TaskScheduler.scheduler("samplefile2.txt", "feasibleschedule4", 4);
		/** There is no feasible schedule on 4 cores */

		/** There is no feasible scheduler on 2 cores */ 
		TaskScheduler.scheduler("samplefile3.txt", "feasibleschedule5", 2);
		/** There is a feasible scheduler on 2 cores */ 
		TaskScheduler.scheduler("samplefile4.txt", "feasibleschedule6", 2);
	}

}

/**
 * MyComparator is a comparator that can help build our heap.
 * It let the heap.min is the task that has minimal deadline.
 * And if some tasks has same deadline, the one with largest execution time will be the heap.min.
 */

class MyComparator implements Comparator<Task> {
	@Override  
	public int compare(Task task1, Task task2) {  

		if(task1.get_d()>task2.get_d()){
			return 1;  
		}else if(task1.get_d()<task2.get_d()){  
			return -1;  
		}else if (task1.get_d()==task2.get_d()){  
			if (task1.get_e()<task2.get_e()){
				return 1;
			}
			else if (task1.get_e()>task2.get_e()){
				return -1;
			}

		}
		return 0;
	}  
}
/**
 * Additional classes
 * Create classes Task and Core so we can insert them into priority queue.
 */
class Task{
	// class Task has some different time attributes as well as its name. Then create setter and getter for them.
	private String Taskname;
	private int execution_time;
	private int release_time;
	private int deadline;
	private int start_time;
	private String perform_core;
	public Task(){};
	public void set_T(String t){
		this.Taskname = t;
	}
	public void set_e(int e){
		this.execution_time = e;
	}
	public void set_r(int r){
		this.release_time = r;
	}
	public void set_d(int d){
		this.deadline = d;
	}
	public void set_s(int s){
		this.start_time = s;
	}
	public void set_c(String c){
		this.perform_core = c;
	}
	public String get_T(){
		return this.Taskname;
	}
	public int get_e(){
		return this.execution_time;
	}
	public int get_r(){
		return this.release_time;
	}
	public int get_d(){
		return this.deadline;
	}
	public int get_s(){
		return this.start_time;
	}
	public String get_c(){
		return this.perform_core;
	}
}
class Core{
	// The class Core is a simple object with only its name and a time attribute.
	private String Corename;
	private int availabletime;
	public Core(){};
	public void set_name(String t){
		this.Corename = t;
	}
	public void set_time(int i){
		this.availabletime = i;
	}
	public String get_name(){
		return this.Corename;
	}
	public int get_time(){
		return this.availabletime;
	}
}
