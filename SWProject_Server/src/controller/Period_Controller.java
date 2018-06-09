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
	@FXML public CheckBox Default_CheckBox;			//�ֱ� - �ʱ�ȭ üũ�ڽ�(2/sec)���� �ʱ�ȭ�س���
	@FXML public RadioButton Day_RadioBtn;			//�ֱ� - ��¥����
	@FXML public RadioButton Hour_RadioBtn;			//�ֱ� - �ð�����
	@FXML public RadioButton Min_RadioBtn;			//�ֱ� - �� ����
	@FXML public RadioButton Sec_RadioBtn;			//�ֱ� - �� ����
	@FXML public Button SaveBtn;
	@FXML public Button CloseBtn;
	
	private MainController main;
	
	@FXML public void move_Default(ActionEvent event) {
		Sec_RadioBtn.setSelected(true);
		Min_RadioBtn.setSelected(false);
		Hour_RadioBtn.setSelected(false);
		Day_RadioBtn.setSelected(false);
		Period_TextField.setText("2");
	}//move_Default()�Լ� �� -> �ֱ⸦ 2�ʷ� �س���(�ʱ��ֱ�)
	
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
	}//move_RadioBtn()�Լ� �� -> ���ϴ� �ð�/��¥/��/�ʸ� Ŭ��
	
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
			System.out.println("Tina ����");
			main.on_Tinaauto();
		}
		else if(main.Find_Net_Client.equals("") && !main.Net_TotalBox.isSelected()) {
			System.out.println("Tina ����");
			main.on_Tinaauto();
		}else {
			System.out.println("Net ����");
			main.on_Netauto();
		}
		Stage stage=(Stage)SaveBtn.getScene().getWindow();
		stage.close();
	}//moveSave()�Լ� �� -> �ֱ����� �Է��� ��, �ش� ���̵�� �ڵ�����������
	
	@FXML public void moveClose(ActionEvent event) {
		Stage stage=(Stage)CloseBtn.getScene().getWindow();
		stage.close();
	}//moveClose()�Լ� �� 
	
	public void init(MainController mainController) {
		// TODO Auto-generated method stub
		main=mainController;
	}//init()�Լ� ��
}
