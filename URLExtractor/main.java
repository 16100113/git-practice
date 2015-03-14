package URLExtractor;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class main {
	private static int port = 80;
	private static ArrayList<String> check_list = new ArrayList<String>();
	private static statemachine my_state_machine = new statemachine();
	public static void main(String[] args) throws InterruptedException {
		String current_url = "http://lums.edu.pk";
		my_state_machine.list.put(0,current_url);
		crawler(current_url);
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
			crawler(check_list.get(i));
		}
	}
	public static void crawler(String current_url) throws InterruptedException{
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
			//System.out.println("Number of urls: " + salsa.list.size());
			buff_reader.close();
			s.close();
			//e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
	}
	//public static void 

}
