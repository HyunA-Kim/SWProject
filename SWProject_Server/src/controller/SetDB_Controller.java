package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SetDB_Controller {
	@FXML TextField ID_TextField;			//DB 아이디 텍스트필드
	@FXML TextField Password_TextField;		//DB 패스워드 텍스트필드
	@FXML CheckBox Save_CheckBox;			//체크박스
	@FXML Button connectBtn;				//연결버튼
	
	private MainController main;
	String DB_ID;							//DB 아이디 변수 저장	-> main으로 넘겨줌
	String DB_Password;						//DB 패스워드 변수 저장 -> main으로 넘겨줌
	
	public void Init(MainController main_controller) {
		main = main_controller;			// main 객체를 저장
	}//init()함수 끝
	
	@FXML public void moveConnect() {
		if(Save_CheckBox.isSelected()) main.DBSave_Check=true;
		else main.DBSave_Check=false;  //DB 정보 저장 여부
		DB_ID=ID_TextField.getText().trim();
		DB_Password=Password_TextField.getText().trim();
		main.DB_user=DB_ID;
		main.DB_password=DB_Password;
		main.on_Database();
		Stage stage=(Stage)connectBtn.getScene().getWindow();
		stage.close();
	}//moveConnect()함수 끝 -> DB 아이디와 패스워드 메인으로 넘긴 후, 데이터베이스 연동 실행
	
}
