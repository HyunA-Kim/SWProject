package controller.option;


import javax.swing.JOptionPane;

import controller.MainController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Option1_PortController extends MainController {
	
	private MainController main;
	
	public String IP="";
	public int PORT=-1;
	@FXML Option1_PortController option1_PortController;
	@FXML private Button saveBtn;
	@FXML private Button closeBtn;
	@FXML private Button changeBtn; //서버 또한 포트와 아이피를변화하나?
	
	@FXML public TextField IP_textField;
	@FXML public TextField Port_textField;

	
	@FXML public void moveSave(ActionEvent event) {
		if(IP_textField.getText().trim().equals("") || Port_textField.getText().trim().equals("")) {JOptionPane.showMessageDialog(null,"IP와 포트번호를 확인해 주세요.","알림",JOptionPane.ERROR_MESSAGE);}
		else{
			IP=IP_textField.getText().trim();			// 현재 서버 아이피 설정
			PORT=Integer.parseInt(Port_textField.getText().trim()); // 현재 서버 포트번호 설정
			main.IP=IP;
			main.PORT=PORT;
			main.IP_lbl.setText(IP);
			main.Port_lbl.setText(PORT+"");
			System.out.println("현재 서버 아이피: "+IP);
			System.out.println("현재 서버 포트번호: "+PORT);
			main.SaveText("Current Server IP: "+main.IP+" /Port number : "+main.PORT);
			Stage stage=(Stage)saveBtn.getScene().getWindow();
			stage.close();
		}
	}
	@FXML private void moveClose(ActionEvent event) {
		Stage stage=(Stage)closeBtn.getScene().getWindow();
		stage.close();
	}
	@FXML private void moveChange(ActionEvent event) {
		
	}
	public void init(MainController mainController) {
		// TODO Auto-generated method stub
		main=mainController;
	}
	
}
