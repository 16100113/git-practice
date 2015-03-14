package blocking_priority_queue;

public class main {

	public static void main(String[] args) {
		String url = "http:\\lums.edu.pk";
		 blocking_queue bq = new blocking_queue();
		 bq.put(url);
		 worker_thread crw1 = new worker_thread(bq);
		 crw1.run();
		 System.out.println(bq.b_queue.size());
		 for(int i = 0; i < bq.b_queue.size(); i++){
			 System.out.println(bq.b_queue.get(i));
		 }

	}

}
