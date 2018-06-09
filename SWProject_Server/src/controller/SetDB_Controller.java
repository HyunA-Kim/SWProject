package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SetDB_Controller {
	@FXML TextField ID_TextField;			//DB ���̵� �ؽ�Ʈ�ʵ�
	@FXML TextField Password_TextField;		//DB �н����� �ؽ�Ʈ�ʵ�
	@FXML CheckBox Save_CheckBox;			//üũ�ڽ�
	@FXML Button connectBtn;				//�����ư
	
	private MainController main;
	String DB_ID;							//DB ���̵� ���� ����	-> main���� �Ѱ���
	String DB_Password;						//DB �н����� ���� ���� -> main���� �Ѱ���
	
	public void Init(MainController main_controller) {
		main = main_controller;			// main ��ü�� ����
	}//init()�Լ� ��
	
	@FXML public void moveConnect() {
		if(Save_CheckBox.isSelected()) main.DBSave_Check=true;
		else main.DBSave_Check=false;  //DB ���� ���� ����
		DB_ID=ID_TextField.getText().trim();
		DB_Password=Password_TextField.getText().trim();
		main.DB_user=DB_ID;
		main.DB_password=DB_Password;
		main.on_Database();
		Stage stage=(Stage)connectBtn.getScene().getWindow();
		stage.close();
	}//moveConnect()�Լ� �� -> DB ���̵�� �н����� �������� �ѱ� ��, �����ͺ��̽� ���� ����
	
}
