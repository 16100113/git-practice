package URLExtractor;

import java.lang.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class statemachine {
	private int state = 0;
	private String url = "";
	public String baseURL = "";
	private int semistate = 0;
	public HashMap<Integer,String> list =new HashMap<Integer,String>();
	public int count = 1;
	public int depth = 0;

	public void main(char cur){
		cur = Character.toLowerCase(cur);
		switch(state){
		case 0: // check for <
//			State 0:
//			This is the reset state or default state. 
//			Here a '<' must be recieved to trigger a semi state which then will act accordingly to what it may recieve, which can be 'a', 's' or '!'.
//			This sends the machine into transition state, script handling state or comment handling state respectively.
			ResetState(cur);
			break;
		case 1: // 
			//State 1:
			//Wait to recieve href= in this exact sequence. if recieved sends 
			//the machine into print state
			transitionState(cur);
			break;
		case 2:
			//State 2:
//			Prints the url and adds it to stack (to remove duplicates later on). Also removes arguments, '#' (self refrences) and makes urls absolute.
//			after this send the machine back to reset state.
			catch_urlState(cur);
			break;
		case 3:
			//handle scripts
			ignore_state_script(cur);
			break;
		case 4:
			//handle comments
			ignore_state_comment(cur);
			break;
		}
	}
	//Default state searches for a < char and if it finds < then proceeds to see if it is a tag, script or comment
	public void ResetState(char cur){
		if(cur == '<' && semistate == 0){
			semistate = 1;
		}else if(cur == 'a' && semistate == 1){
			state = 1;
			semistate = 0;
		}else if(cur == 's' && semistate == 1){
			state = 3;
			semistate = 0;
		}else if(cur == '!' && semistate == 1){
			state = 4;
			semistate = 0;
		}else if(semistate == 1){
			state = 0;
			semistate = 0;
		}
	}
	//once <a is found it goes on to find href=
	public void transitionState(char cur){
		if(cur == 'h' && semistate == 0){
			semistate = 1;
		}else if(cur == 'r' && semistate == 1){
			semistate = 2;
		}else if(cur == 'e' && semistate == 2){
			semistate = 3;
		}else if(cur == 'f' && semistate == 3){
			semistate = 4;
		}else if(cur == '=' && semistate == 4){
			semistate = 0;
			state = 2;
		}else{
			semistate = 0;
		}
	}
	//prints the url and pushes it in list to check for duplicates later
	public void catch_urlState(char cur){
		if(cur == '"' && semistate == 0){
			semistate = 1;
		}else if((cur == '?' || cur == '<' || cur == '>') && semistate == 1){
			semistate = 2;
		}else if(cur != '"' && semistate == 1){
			url = url + cur;
		}else if(cur == '"' && semistate == 1){
			semistate = 2;
		}else if(cur == '>' && semistate == 2){ 
			boolean check_for_base = false;
			state = 0;
			semistate = 0;
			if(!url.contains("#")){
				if(!list.isEmpty()){
					Set set = list.entrySet();
					Iterator i = set.iterator();
				      // Display elements
				      while(i.hasNext()) {
				          Map.Entry me = (Map.Entry)i.next();
				    	  if(url.compareTo(me.getValue().toString()) == 0){
								check_for_base = true;
				    	  }

				      }
				}
				if(check_for_base == false){
					if(depth > 0){
						System.out.print('\t');
					}
					if(url.startsWith("http://") || url.startsWith("https://")){
						System.out.println(url);
						list.put(count, url);
					}else {
						url = baseURL + url;
						System.out.println(url);
						list.put(count, url);
					}
				}
			}
			url = "";
			count++;
		}
	}
	//used to process a script
	public void ignore_state_script(char cur){
		if(cur == 'c' && semistate == 0){
			semistate = 1;
		}else if(cur == 'r' && semistate == 1){
			semistate = 2;
		}else if(cur == 'i' && semistate == 2){
			semistate = 3;
		}else if(cur == 'p' && semistate == 3){
			semistate = 4;
		}else if(cur == 't' && semistate == 4){
			semistate = 5;
		}else if(cur == '/' && semistate == 5){
			semistate = 6;
		}else if(cur == 's' && semistate == 6){
			semistate = 7;
		}else if(cur == 'c' && semistate == 7){
			semistate = 8;
		}else if(cur == 'r' && semistate == 8){
			semistate = 9;
		}else if(cur == 'i' && semistate == 9){
			semistate = 10;
		}else if(cur == 'p' && semistate == 10){
			semistate = 11;
		}else if(cur == 't' && semistate == 11){
			semistate = 12;
		}else if(cur == '>' && semistate == 12){
			semistate = 0;
			state = 0;
		}else if(semistate < 4){
			semistate = 0;
			state = 0;
		}else if(semistate > 4){
			semistate = 5;
		}
	}
	//used to process a comment
	public void ignore_state_comment(char cur){
		if(cur == '-' && semistate == 0){
			semistate = 1;
		}else if(cur == '-' && semistate == 1){
			semistate = 2;
		}else if(cur == '-' && semistate == 2){
			semistate = 3;
		}else if(cur == '-' && semistate == 3){
			semistate = 4;
		}else if(cur == '>' && semistate == 4){
			semistate = 0;
			state = 0;
		}else if(semistate < 2 && cur != '-') {
			semistate = 0;
			state = 0;
		}else if(cur != '-' && semistate > 1 && semistate < 4){
			semistate = 2;
		}else if(cur != '>' && semistate == 4){
			semistate = 2;
		}
	}
}
