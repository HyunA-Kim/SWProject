package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class CommandShow_Controller {
	@FXML Button closeBtn;
	@FXML TextArea Command_TextArea; //명령어 textArea
	
	@FXML public void moveClose() {
		Stage stage=(Stage)closeBtn.getScene().getWindow();
		stage.close();
	}//moveClose()함수 끝 -> 닫기 버튼
	
}//명령어 보기 컨트롤러
