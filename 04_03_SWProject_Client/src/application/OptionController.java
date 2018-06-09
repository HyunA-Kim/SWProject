package application;




import javax.swing.JOptionPane;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class OptionController {
	@FXML Button acceptBtn;
	@FXML Button changeBtn;
	@FXML Button cancelBtn;
	
	@FXML TextField ip_textField;
	@FXML TextField port_textField; 
	
	public MainController main;
	public String IP;					//IP저장 변수
	public int PORT;					//Port저장변수
	
	@FXML public void moveAccept() {
		if(ip_textField.getText().equals("") || port_textField.getText().equals("")) {
			JOptionPane.showMessageDialog(null,"아이피와 포트번호를 확인해주세요","알림",JOptionPane.ERROR_MESSAGE);
			main.SaveText("Error : Check up the IP/Port");
			return;
		}
		IP = ip_textField.getText().trim();
		PORT = Integer.parseInt(port_textField.getText().trim());
		
		main.IP = IP;
		main.PORT = PORT;
		
		System.out.println("option: "+main.IP+" "+main.PORT);
		System.out.println("서버 IP와 포트가 저장되었습니다");
		main.Network();
		Stage stage=(Stage)acceptBtn.getScene().getWindow();
		stage.close();
	}//moveAccept()함수 끝 -> 아이피/포트번호 저장 후 main으로 전달
	
	@FXML public void moveChange() {
		Stage stage=(Stage)changeBtn.getScene().getWindow();
		stage.close();
	}//moveChange()함수 끝
	
	@FXML public void moveCancel() {
		Stage stage=(Stage)cancelBtn.getScene().getWindow();
		stage.close();
	}
	
	public void Init(MainController main_controller) {
		main = main_controller;
		
	}
}
