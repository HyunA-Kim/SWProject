package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;

import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Period_Controller {
	@FXML public TextField Period_TextField;	
	@FXML public CheckBox Default_CheckBox;			//주기 - 초기화 체크박스(2/sec)으로 초기화해놓음
	@FXML public RadioButton Day_RadioBtn;			//주기 - 날짜간격
	@FXML public RadioButton Hour_RadioBtn;			//주기 - 시간간격
	@FXML public RadioButton Min_RadioBtn;			//주기 - 분 간격
	@FXML public RadioButton Sec_RadioBtn;			//주기 - 초 간격
	@FXML public Button SaveBtn;
	@FXML public Button CloseBtn;
	
	private MainController main;
	
	@FXML public void move_Default(ActionEvent event) {
		Sec_RadioBtn.setSelected(true);
		Min_RadioBtn.setSelected(false);
		Hour_RadioBtn.setSelected(false);
		Day_RadioBtn.setSelected(false);
		Period_TextField.setText("2");
	}//move_Default()함수 끝 -> 주기를 2초로 해놓음(초기주기)
	
	@FXML public void move_RadioBtn(ActionEvent event) {
		if(Sec_RadioBtn.isSelected()) {
			Min_RadioBtn.setSelected(false);
			Hour_RadioBtn.setSelected(false);
			Day_RadioBtn.setSelected(false);
		}
		else if(Min_RadioBtn.isSelected()) {
			Sec_RadioBtn.setSelected(false);
			Hour_RadioBtn.setSelected(false);
			Day_RadioBtn.setSelected(false);
		}
		else if(Hour_RadioBtn.isSelected()) {
			Sec_RadioBtn.setSelected(false);
			Min_RadioBtn.setSelected(false);
			Day_RadioBtn.setSelected(false);
		}
		else if(Day_RadioBtn.isSelected()) {
			Sec_RadioBtn.setSelected(false);
			Min_RadioBtn.setSelected(false);
			Hour_RadioBtn.setSelected(false);
		}
	}//move_RadioBtn()함수 끝 -> 원하는 시간/날짜/분/초를 클릭
	
	@FXML public void moveSave(ActionEvent event) {
		main.Period_number=Period_TextField.getText().trim();
		if(Sec_RadioBtn.isSelected()) {
			main.Period_text="sec";
		}
		else if(Min_RadioBtn.isSelected()) {
			main.Period_text="min";
		}
		else if(Hour_RadioBtn.isSelected()){
			main.Period_text="hour";
		}
		else if(Day_RadioBtn.isSelected()) {
			main.Period_text="day";
		}
		
		if(main.Tina_TotalBox.isSelected() && !main.Net_TotalBox.isSelected()) {
			System.out.println("Tina 고르기");
			main.on_Tinaauto();
		}
		else if(main.Find_Net_Client.equals("") && !main.Net_TotalBox.isSelected()) {
			System.out.println("Tina 고르기");
			main.on_Tinaauto();
		}else {
			System.out.println("Net 고르기");
			main.on_Netauto();
		}
		Stage stage=(Stage)SaveBtn.getScene().getWindow();
		stage.close();
	}//moveSave()함수 끝 -> 주기정보 입력한 후, 해당 아이디로 자동설정보내기
	
	@FXML public void moveClose(ActionEvent event) {
		Stage stage=(Stage)CloseBtn.getScene().getWindow();
		stage.close();
	}//moveClose()함수 끝 
	
	public void init(MainController mainController) {
		// TODO Auto-generated method stub
		main=mainController;
	}//init()함수 끝
}
