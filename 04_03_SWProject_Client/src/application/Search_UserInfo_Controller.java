package application;


import javax.swing.JOptionPane;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Search_UserInfo_Controller {
	@FXML TextField Search_Name_TextField;			//�̸� �ؽ�Ʈ�ʵ�
	@FXML TextField Search_Phone_TextField;			//��ȭ��ȣ �ؽ�Ʈ�ʵ�
	@FXML TextField Search_ID_TextField;			//ã�� ���̵� �ؽ�Ʈ�ʵ�
	@FXML TextField Search_Pass_TextField;			//ã�� ��� �ؽ�Ʈ�ʵ�
	
	@FXML Button Find_Btn;							//ã�� ��ư
	@FXML Button Save_Btn;							//���� ��ư
	
	public MainController main;
	
	@FXML public void moveFind(){
		main.Search_Client_Name=Search_Name_TextField.getText().trim();
		main.Search_Client_phoneNumber=Search_Phone_TextField.getText().trim();	
		if(main.on_Connection==true) {
			main.Send_Message("Search"+"/"+main.Search_Client_Name+"/"+main.Search_Client_phoneNumber);
			main.SaveText("Send search ID/Pass to server");
			main.SaveText("Search name: "+main.Search_Client_Name+" Search phone:"+main.Search_Client_phoneNumber);
		}//server�� �ش� �޼��� ������ -> �� �� ���̵�/���������
		else {
			JOptionPane.showMessageDialog(null,"������ �������� �ƴմϴ�","�˸�",JOptionPane.ERROR_MESSAGE);
			main.SaveText("Error : Not connection with server");
		}
	}//moveFind()�Լ� �� -> mysql�� �̿��� �̸�,��ȭ��ȣ�� ���� ���̵�/��� ��������
	
	@FXML public void moveSave() {
		main.ID_TextField.setText(Search_ID_TextField.getText());
		main.Pass_TextField.setText(Search_Pass_TextField.getText());
		main.ID=Search_ID_TextField.getText();
		main.PassWord=Search_Pass_TextField.getText();
		Stage stage=(Stage)Save_Btn.getScene().getWindow();
		stage.close();
	}//moveSave()�Լ� �� -> �޾ƿ� ���̵�/����� login�� �ڵ� Ÿ����
	
	public void Init(MainController main_controller) {
		main = main_controller;
		
	}//init()�Լ� ��
	
	public void failSearch() {
		// TODO Auto-generated method stub
		JOptionPane.showMessageDialog(null,"������ �ùٸ��� �ʽ��ϴ�.","�˸�",JOptionPane.ERROR_MESSAGE);
		main.SaveText("Error : Wrong name/phone information");
	}//failSearch()�Լ� ��
}
