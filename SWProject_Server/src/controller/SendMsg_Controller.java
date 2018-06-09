package controller;
import javax.swing.JOptionPane;

import controller.MainController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SendMsg_Controller {
	@FXML Button sendBtn;
	@FXML Button closeBtn;
	@FXML TextField textId; 
	@FXML TextField textMsg;
	public String client_Nickname;	//main에서 받아온 아이디 
	private MainController main;	
	

	@FXML public void moveSend(ActionEvent event) {
		//선택한 이름과 메세지를 함께 notesend로 보내준다.
		// 전체 콤보박스를 클릭한 경우 broadcast를 이용해 그곳에서 클라이언트에게 메세지 전달
		
		String msg = textMsg.getText();
		
		if(msg!=null) {
			if(client_Nickname.equals("Everyone")) main.BroadCast("Everyone","Everyone/"+msg);
			else if(client_Nickname.equals("All TinaMember")) main.BroadCast("All TinaMember","All TinaMember/"+msg);
			else if(client_Nickname.equals("All NetMember")) main.BroadCast("All NetMember", "All NetMember/"+msg);
			else main.NoteSend(msg);
			System.out.println("메세지보내기버튼을 클릭하였습니다");
			main.SaveText("메세지보내기버튼을 클릭하였습니다");
		}
		else {
			JOptionPane.showMessageDialog(null,"메세지 내용이 없습니다","알림",JOptionPane.ERROR_MESSAGE);
			main.SaveText("메세지 내용이 없습니다");
		}
		
		Stage stage=(Stage)sendBtn.getScene().getWindow();
		stage.close();
	}//moveSend()함수 끝 -> 메세지를 클라이언트로 전달해주는 기능
	
	@FXML public void moveClose(ActionEvent event) {
		Stage stage=(Stage)closeBtn.getScene().getWindow();
		stage.close();
	}//moveClose()함수 끝 
	
	public void Init(MainController main_controller) {
		main = main_controller;			// main 객체를 저장
		client_Nickname = main.Client_NickName;
		System.out.println("클라이언트명: "+client_Nickname);
		String text="클라이언트명: "+client_Nickname+"       ";
		main.SaveText(text);
		textId.setText(client_Nickname);
	}//init()함수 끝 -> 클라이언트 명과 보낼 메세지 저장
	
	
}
