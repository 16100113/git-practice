package blocking_priority_queue;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class blocking_queue {
	public ArrayList<String> b_queue = new ArrayList<String>();
	public int n = 0;
	
	synchronized String get(){
		while(b_queue.isEmpty()){
			try { 
				wait(); 
			} catch(InterruptedException e) { 
				System.out.println("InterruptedException caught"); 
			} 
		}
		String url =  b_queue.get(b_queue.size() - 1);
		notifyAll();
		return url;
	}
	synchronized void put(String str){
		while(b_queue.size() >= 100){
			try { 
				wait(); 
			} catch(InterruptedException e) { 
				System.out.println("InterruptedException caught"); 
			} 
		}
		b_queue.add(str);
		notifyAll();
	}
}
