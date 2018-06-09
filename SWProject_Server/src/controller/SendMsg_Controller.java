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
	public String client_Nickname;	//main���� �޾ƿ� ���̵� 
	private MainController main;	
	

	@FXML public void moveSend(ActionEvent event) {
		//������ �̸��� �޼����� �Բ� notesend�� �����ش�.
		// ��ü �޺��ڽ��� Ŭ���� ��� broadcast�� �̿��� �װ����� Ŭ���̾�Ʈ���� �޼��� ����
		
		String msg = textMsg.getText();
		
		if(msg!=null) {
			if(client_Nickname.equals("Everyone")) main.BroadCast("Everyone","Everyone/"+msg);
			else if(client_Nickname.equals("All TinaMember")) main.BroadCast("All TinaMember","All TinaMember/"+msg);
			else if(client_Nickname.equals("All NetMember")) main.BroadCast("All NetMember", "All NetMember/"+msg);
			else main.NoteSend(msg);
			System.out.println("�޼����������ư�� Ŭ���Ͽ����ϴ�");
			main.SaveText("�޼����������ư�� Ŭ���Ͽ����ϴ�");
		}
		else {
			JOptionPane.showMessageDialog(null,"�޼��� ������ �����ϴ�","�˸�",JOptionPane.ERROR_MESSAGE);
			main.SaveText("�޼��� ������ �����ϴ�");
		}
		
		Stage stage=(Stage)sendBtn.getScene().getWindow();
		stage.close();
	}//moveSend()�Լ� �� -> �޼����� Ŭ���̾�Ʈ�� �������ִ� ���
	
	@FXML public void moveClose(ActionEvent event) {
		Stage stage=(Stage)closeBtn.getScene().getWindow();
		stage.close();
	}//moveClose()�Լ� �� 
	
	public void Init(MainController main_controller) {
		main = main_controller;			// main ��ü�� ����
		client_Nickname = main.Client_NickName;
		System.out.println("Ŭ���̾�Ʈ��: "+client_Nickname);
		String text="Ŭ���̾�Ʈ��: "+client_Nickname+"       ";
		main.SaveText(text);
		textId.setText(client_Nickname);
	}//init()�Լ� �� -> Ŭ���̾�Ʈ ��� ���� �޼��� ����
	
	
}
