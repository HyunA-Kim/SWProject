package application;

import javax.swing.JOptionPane;

import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class JoinController {

	@FXML TextField ID_TextField;
	@FXML TextField Password_TextField;
	@FXML TextField Name_TextField;
	@FXML TextField Phone_TextField;
	@FXML TextField Addr_TextField;
	
	@FXML RadioButton net_Btn;
	@FXML RadioButton tina_Btn;
	@FXML Button sendBtn;
    @FXML Button ID_checkBtn;
   
	
	private MainController main;
	
	public String ID;						// 가입 ID
	public String PassWord;					// 비밀번호
	public String Selected_Program; 		// 사용중인 소프트웨어
	public String Name;						// 이름
	public String Addr;						// 주소
	public String Phone;					// 연락처
	public String existID="non_exist";		// 아이디 존재 여부
	
	@FXML public void input_ID() {
		if(ID_TextField.getText().equals("아이디")) ID_TextField.setText("");
		
	}//input_ID()함수 끝
	
	@FXML public void input_Password() {
		if(Password_TextField.getText().equals("비밀번호")) Password_TextField.setText("");
	}//input_Password()함수 끝
	
	@FXML public void moveSend() {
		
		Set_JoinCont_UserInfo();	// 회원가입 컨트롤러의 입력정보를 변수에 저장
		if(!net_Btn.isSelected()&& !tina_Btn.isSelected()) return;
		if(net_Btn.isSelected()) Selected_Program="Net";
		else if(tina_Btn.isSelected()) Selected_Program="Tina";
		Set_MainCont_UserInfo(); 	// 메인컨트롤러의 회원가입 정보를 초기
		main.Set_UserInfo();		// 아이디와 비밀번호 textField 자동 입력 
		main.Send_Message("Join/"+main.Join_Client_Name+"/"+main.Join_Client_ID
				+"/"+main.Join_Client_Password+"/"+main.Join_Client_phoneNumber+"/"+main.Join_Client_Address
				+"/"+main.Join_Client_Program);
		main.SaveText("Send join member to server: "+main.Join_Client_Name+"/"+main.Join_Client_ID
				+"/"+main.Join_Client_Password+"/"+main.Join_Client_phoneNumber+"/"+main.Join_Client_Address
				+"/"+main.Join_Client_Program);
		Stage stage=(Stage)sendBtn.getScene().getWindow();
		stage.close();
		
		
		//mySql 구문 사용
		//이름, 아이디, 비밀번호, 전화번호, 주소, 사용프로그램,을 각각 String을 이용하여 보내준다.
		//mysql에서 select "이름" * from joinTable
	
	}//moveSend()함수 끝-> 회원가입시 정보를 server로 보내줌(mysql사용)
	
	@FXML public void selectTina() {
		Selected_Program="Net";
		net_Btn.setSelected(false);
		tina_Btn.setSelected(true);
	}//selectTina()함수 끝
	
	@FXML public void selectNet() {
		Selected_Program="Tina";
		net_Btn.setSelected(true);
		tina_Btn.setSelected(false);
	}//selectNet()함수 끝
	
	@FXML public void ID_Check() {
		if(ID_TextField.getText().trim().equals("")) {
			JOptionPane.showMessageDialog(null,"아이디를 입력하세요","알림",JOptionPane.ERROR_MESSAGE);
		}
		main.Send_Message("CheckID/"+ID_TextField.getText().trim());
		if(existID.equals("exist")) {
			JOptionPane.showMessageDialog(null,"이미 존재하는 아이디입니다.","알림",JOptionPane.ERROR_MESSAGE);
		}//이미 ID가 존재할 경우
		else {
			sendBtn.setDisable(false);
		}//이미 ID가 존재하지 않을 경우
		
	}//ID_ChecK()함수 끝 -> 아이디 중복 확인하는 함수
	
	public void Set_JoinCont_UserInfo() {
		ID=ID_TextField.getText();
		PassWord=Password_TextField.getText();
		Phone=Phone_TextField.getText();
		Addr=Addr_TextField.getText();
		Name=Name_TextField.getText();
	}//Set_JointCont_UserInfo()함수 ->변수에다 해당 정보저장
	
	public void Set_MainCont_UserInfo() {
		main.Join_Client_Name=Name;
		main.Join_Client_ID=ID;
		main.Join_Client_Password=PassWord;
		main.Join_Client_phoneNumber=Phone;
		main.Join_Client_Address = Addr;
		main.Join_Client_Program = Selected_Program;
	}//Set_MainCont_UserInfo()함수 -> main의 정보에다 저장
	
	public void Init(MainController main_controller) {
		main = main_controller;
		
	}//init()함수 끝
}
