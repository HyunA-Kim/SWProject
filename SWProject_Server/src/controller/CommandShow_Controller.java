package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class CommandShow_Controller {
	@FXML Button closeBtn;
	@FXML TextArea Command_TextArea; //��ɾ� textArea
	
	@FXML public void moveClose() {
		Stage stage=(Stage)closeBtn.getScene().getWindow();
		stage.close();
	}//moveClose()�Լ� �� -> �ݱ� ��ư
	
}//��ɾ� ���� ��Ʈ�ѷ�
