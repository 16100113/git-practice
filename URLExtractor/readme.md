URLExtractor

There are two main classes which implement URL Extractor.
Main :
The main class handles the socket programming part and inputs data into the buffer of fixed size 100 bytes.
It sends character by character input to the stateMachine class main method. Hence it acts as the Engine for the state machine.

stateMachine:
stateMachine maintains state and decides what to do after recieving each character. The main method calls the apropriate function according to the state the machine is in. It has five helper functions to handle the five states the machine can be in.
State 0:
This is the reset state or default state. Here a '<' must be recieved to trigger a semi state which then will act accordingly to what it may recieve, which can be 'a', 's' or '!'. This sends the machine into transition state, script handling state or comment handling state respectively.
State 1:
Wait to recieve href= in this exact sequence. if recieved sends the machine into print state
State 2:
Prints the url and adds it to stack (to remove duplicates later on). Also removes arguments, '#' (self refrences) and makes urls absolute.
after this send the machine back to reset state.
State 3:
handles scripts.
state 4:
handle comments.



