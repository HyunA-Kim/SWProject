package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class FilePath_Controller {
	@FXML TextField FilePath_TextField;			//파일경로 텍스트필드
	public String get_FilePath;					//파일경로 스트링변수
	
	@FXML Button SendBtn;						//설정 버튼
	@FXML private MainController main;			//메인 변수 가져오기
	@FXML private ClientController client;		//클라이언트 변수 가져오기
	
	@FXML public void moveSend() {
		get_FilePath=FilePath_TextField.getText().trim();
		main.FilePath=get_FilePath;
		Stage stage=(Stage)SendBtn.getScene().getWindow();
		stage.close();
	}//moveSend()함수 끝 -> 파일경로를 보여주고 설정해주는 함수입니다.

	public void Init(MainController main_controller, ClientController clientController) {
		// TODO Auto-generated method stub
		main=main_controller;
		client=clientController;
	}//init()함수 끝
}
