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
	public String IP;					//IP���� ����
	public int PORT;					//Port���庯��
	
	@FXML public void moveAccept() {
		if(ip_textField.getText().equals("") || port_textField.getText().equals("")) {
			JOptionPane.showMessageDialog(null,"�����ǿ� ��Ʈ��ȣ�� Ȯ�����ּ���","�˸�",JOptionPane.ERROR_MESSAGE);
			main.SaveText("Error : Check up the IP/Port");
			return;
		}
		IP = ip_textField.getText().trim();
		PORT = Integer.parseInt(port_textField.getText().trim());
		
		main.IP = IP;
		main.PORT = PORT;
		
		System.out.println("option: "+main.IP+" "+main.PORT);
		System.out.println("���� IP�� ��Ʈ�� ����Ǿ����ϴ�");
		main.Network();
		Stage stage=(Stage)acceptBtn.getScene().getWindow();
		stage.close();
	}//moveAccept()�Լ� �� -> ������/��Ʈ��ȣ ���� �� main���� ����
	
	@FXML public void moveChange() {
		Stage stage=(Stage)changeBtn.getScene().getWindow();
		stage.close();
	}//moveChange()�Լ� ��
	
	@FXML public void moveCancel() {
		Stage stage=(Stage)cancelBtn.getScene().getWindow();
		stage.close();
	}
	
	public void Init(MainController main_controller) {
		main = main_controller;
		
	}
}
