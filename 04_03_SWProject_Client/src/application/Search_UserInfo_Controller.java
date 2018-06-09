package application;


import javax.swing.JOptionPane;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Search_UserInfo_Controller {
	@FXML TextField Search_Name_TextField;			//이름 텍스트필드
	@FXML TextField Search_Phone_TextField;			//전화번호 텍스트필드
	@FXML TextField Search_ID_TextField;			//찾을 아이디 텍스트필드
	@FXML TextField Search_Pass_TextField;			//찾을 비번 텍스트필드
	
	@FXML Button Find_Btn;							//찾기 버튼
	@FXML Button Save_Btn;							//저장 버튼
	
	public MainController main;
	
	@FXML public void moveFind(){
		main.Search_Client_Name=Search_Name_TextField.getText().trim();
		main.Search_Client_phoneNumber=Search_Phone_TextField.getText().trim();	
		if(main.on_Connection==true) {
			main.Send_Message("Search"+"/"+main.Search_Client_Name+"/"+main.Search_Client_phoneNumber);
			main.SaveText("Send search ID/Pass to server");
			main.SaveText("Search name: "+main.Search_Client_Name+" Search phone:"+main.Search_Client_phoneNumber);
		}//server로 해당 메세지 보내기 -> 그 후 아이디/비번가져옴
		else {
			JOptionPane.showMessageDialog(null,"서버와 연결중이 아닙니다","알림",JOptionPane.ERROR_MESSAGE);
			main.SaveText("Error : Not connection with server");
		}
	}//moveFind()함수 끝 -> mysql를 이용해 이름,전화번호를 통해 아이디/비번 가져오기
	
	@FXML public void moveSave() {
		main.ID_TextField.setText(Search_ID_TextField.getText());
		main.Pass_TextField.setText(Search_Pass_TextField.getText());
		main.ID=Search_ID_TextField.getText();
		main.PassWord=Search_Pass_TextField.getText();
		Stage stage=(Stage)Save_Btn.getScene().getWindow();
		stage.close();
	}//moveSave()함수 끝 -> 받아온 아이디/비번을 login시 자동 타이핑
	
	public void Init(MainController main_controller) {
		main = main_controller;
		
	}//init()함수 끝
	
	public void failSearch() {
		// TODO Auto-generated method stub
		JOptionPane.showMessageDialog(null,"정보가 올바르지 않습니다.","알림",JOptionPane.ERROR_MESSAGE);
		main.SaveText("Error : Wrong name/phone information");
	}//failSearch()함수 끝
}
