package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Send_Message_Controller {
	@FXML MainController main;
	@FXML ClientController client;
	
	@FXML Button sendBtn;
	@FXML TextField textMsg;
	public String msg;				// ������ ���۵Ǵ� �޼��� 
	
	@FXML public void moveSend() {
		msg = textMsg.getText().trim();
		main.Send_Message("Send_Msg"+"/"+main.Program+"/"+main.ID+"/"+msg); //server�� �޼��� ������ (main �߰�����)
		main.SaveText("Send message to server : "+msg);
		Stage stage=(Stage)sendBtn.getScene().getWindow();
		stage.close();
	}//moveSend()�Լ� ��
	
	public void Init(MainController main_controller, ClientController client_controller) {
		// TODO Auto-generated method stub
		main=main_controller;
		client=client_controller;
	}//init()�Լ� ��

}
