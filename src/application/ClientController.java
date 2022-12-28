package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;
import java.util.UUID;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class ClientController implements Initializable {
	private Socket socket;
	private BufferedReader br;
	private BufferedWriter bw;

	private String ip;
	private String id;

	
	
	@FXML
	private TextArea textArea;
	@FXML
	private ListView<Object> userList;
	@FXML
	private TextField textField;
	@FXML
	private Button sendBtn;
	@FXML
	private Label label;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		UUID uniqueKey = UUID.randomUUID();
		String ranId =uniqueKey.toString();
		try {
			InetAddress ia = InetAddress.getLocalHost();
			ip = ia.getHostAddress();
			id = "guest@" + ranId.substring(ranId.length()-5, ranId.length());
			
			label.setText("Usage ID :"+id+"("+ip+")");
			
			initSocket();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private void initSocket() throws  IOException{
		socket = new Socket(ip,9898);
		br = new BufferedReader(
				new InputStreamReader(
						socket.getInputStream()));
		bw = new BufferedWriter(
				new OutputStreamWriter(
						socket.getOutputStream()));
		// client => server 접속시도 
		// 0#id
		bw.write("0#"+id);
		bw.newLine();
		bw.flush();
		//서버에서 메세지를 수신하기위해 필요한 쓰레드 클래스
		ClientThread ct = 
				new ClientThread(this);
		
		ct.start();
		
	}
	public void exit(){
		System.exit(0);
	}
	public void closeAll(){
		
		try {
			//client => server
			//종료 메세지를 보낸다.(2#id)
			bw.write("2#"+id);
			bw.newLine();
			bw.flush();
			
			if(br != null) br.close();
			if(bw != null) bw.close();
			if(socket != null && !socket.isClosed()) {
				socket.close();
			}
			
			exit();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	@FXML
	public void onKeyPressHandle(KeyEvent ke) {
        if (ke.getCode().equals(KeyCode.ENTER)) {	// TextField 엔터키 이벤트
        	sendMsg();
        }
    }

	@FXML
	public void onclickSendMsg(ActionEvent event) {	// 버튼클릭 이벤트
		sendMsg();
	}

	// Message 전송 메소드
	private void sendMsg() {
		String msg = textField.getText();
		
		if (textField.getText() == null || textField.getText().length() == 0)
			return;

		try {
			// client => server
			// 일반메세지(1#id#message)
			bw.write("1#" + id + "#" + msg);
			bw.newLine();
			bw.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		// 입력한 내용 삭제.
		textField.setText("");
	}

	public BufferedReader getBufferedReader(){
		return br;
	}
	public TextArea getTextArea(){
		return textArea;
	}
	public String getId(){
		return id;
	}
	public ListView<Object> getUserlist(){
		return userList;
	}
	
	

}
