package blocking_priority_queue;



import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import redirect.redirect;
import Robots.Robots;
import URLExtractor.statemachine;

public class worker_thread implements Runnable{
	private static int port = 80;
	private static ArrayList<String> check_list = new ArrayList<String>();
	private static statemachine my_state_machine = new statemachine();
	blocking_queue bq = new blocking_queue();
	worker_thread(blocking_queue bq){
		this.bq = bq;
	}
	public void run(){
		while(true){
			String current_url = bq.get();
			Robots my_robot = new Robots();
			redirect check_redirect = new redirect();
			String red_flag = null;
			ArrayList<String> new_arr = new ArrayList<String>();
			try {
				red_flag = check_redirect.redirect_main(current_url);
				new_arr = my_robot.robots_main(current_url);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if(red_flag == null || red_flag == "error"){
				continue;
			}
			crawl_main(current_url);
			boolean flag_disallow = false;
			for(int i = 0; i < check_list.size(); i++){
				for(int j = 0; j < new_arr.size(); j++){
					if(check_list.get(i).equals(new_arr.get(j))){
						flag_disallow = true;
					}
				}
				if(!flag_disallow){
					bq.put(check_list.get(i));
				}
			}
			check_list.clear();
			if(bq.b_queue.size() >= 100){
				break;
			}
		}
}
	
		public void crawl_main(String current_url){
			my_state_machine.list.put(0,current_url);
			try {
				crawler(current_url);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			my_state_machine.depth++;
			//System.out.println(my_state_machine.depth);
			Set set = my_state_machine.list.entrySet();
			Iterator iter = set.iterator();
			while(iter.hasNext()){
				Map.Entry me = (Map.Entry)iter.next();
				current_url = me.getValue().toString();
				check_list.add(current_url);
			}
			for(int i = 0; i < check_list.size(); i++){
				//System.out.println("ARGS : " + check_list.get(i));
				try {
					crawler(check_list.get(i));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		public void crawler(String current_url) throws InterruptedException{
			try {
				URL URL = new URL(current_url);
				String host = URL.getHost();
				Socket s = new Socket(InetAddress.getByName(host), port);
				DataOutputStream dataOut = new DataOutputStream(s.getOutputStream());
				dataOut.writeBytes("GET /\r\n");
				//Thread.sleep(5000);
				// buffered reader
				InputStreamReader input_reader =  new InputStreamReader(s.getInputStream());
				BufferedReader buff_reader = new BufferedReader(input_reader,100);
				// init buffer
				
				char[] buffer = new char[100];
				buff_reader.read(buffer);
				//statemachine my_state_machine = new statemachine();
				my_state_machine.baseURL = current_url;
				String terminate_string = "";
				boolean end_condition = true;
				while (end_condition){
					terminate_string = "";
					for (int i =0; i < 100; i++)
					{
						if(buffer[i] != ' '){
							my_state_machine.main(buffer[i]);
							terminate_string = terminate_string + buffer[i];
							if(terminate_string.contains("</html>")){
								end_condition = false;
							}
						}
					}
					buff_reader.read(buffer);
				}
				buff_reader.close();
				s.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
			}
		}
}

