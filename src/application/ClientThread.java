package application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import javafx.application.Platform;

public class ClientThread extends Thread {
	private ClientController cc;
	public ClientThread(ClientController cc) {
		this.cc = cc;
	}
	
	public void run(){
		boolean isStop = false;
		while(!isStop){  
			try {
				//server => client
				//msg는 두가지 종류
				//1. 1#id#message => 일반메세지
				//2. 2#id         => 종료메세지
				String msg = cc.getBufferedReader().readLine();
				String[] temp = msg.split("#");
				if(temp[0].equals("0")){
					//접속메세지
					if(!temp[1].equals(cc.getId())){
						//서버에서 보낸 아이디(temp[1])와 ClientController에 있는 아이디가
						//일치하지 않는 경우에만 메세지를 출력한다.즉, 자기자신 
						cc.getTextArea().appendText(
								temp[1]+" 님이 입장하였습니다."+
								System.getProperty("line.separator"));
					}
				}else if(temp[0].equals("1")){
					//일반 메세지
					//TextArea에 서버에서 보낸 메세지 출력
					cc.getTextArea().appendText(
						temp[1]+" : "+temp[2]+
						System.getProperty("line.separator"));
				}else if(temp[0].equals("2")){
					
					if(temp[1].equals(cc.getId())){
						//종료 메세지
						//스레드 종료
						isStop = true;
						
					}else{
						cc.getTextArea().appendText(
								temp[1]+" 님이 종료하셨습니다."+
								System.getProperty("line.separator"));
					}
					
					
				}else if(temp[0].equals("3")){
					//3#id1,id2,id3,id4,....
					//temp[0] => 3
					//temp[1] => id1,id2,id3,id4,....
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							cc.getUserlist().getItems().clear();
							String[] members = temp[1].split(",");
							cc.getUserlist().getItems().addAll(Arrays.asList(members));	
						}
					});
				}
			} catch (IOException e) {
				isStop = true;
				
			}
		}//end while
		
	}//end run

}
