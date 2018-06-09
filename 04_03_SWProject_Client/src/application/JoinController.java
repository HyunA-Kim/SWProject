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
	
	public String ID;						// ���� ID
	public String PassWord;					// ��й�ȣ
	public String Selected_Program; 		// ������� ����Ʈ����
	public String Name;						// �̸�
	public String Addr;						// �ּ�
	public String Phone;					// ����ó
	public String existID="non_exist";		// ���̵� ���� ����
	
	@FXML public void input_ID() {
		if(ID_TextField.getText().equals("���̵�")) ID_TextField.setText("");
		
	}//input_ID()�Լ� ��
	
	@FXML public void input_Password() {
		if(Password_TextField.getText().equals("��й�ȣ")) Password_TextField.setText("");
	}//input_Password()�Լ� ��
	
	@FXML public void moveSend() {
		
		Set_JoinCont_UserInfo();	// ȸ������ ��Ʈ�ѷ��� �Է������� ������ ����
		if(!net_Btn.isSelected()&& !tina_Btn.isSelected()) return;
		if(net_Btn.isSelected()) Selected_Program="Net";
		else if(tina_Btn.isSelected()) Selected_Program="Tina";
		Set_MainCont_UserInfo(); 	// ������Ʈ�ѷ��� ȸ������ ������ �ʱ�
		main.Set_UserInfo();		// ���̵�� ��й�ȣ textField �ڵ� �Է� 
		main.Send_Message("Join/"+main.Join_Client_Name+"/"+main.Join_Client_ID
				+"/"+main.Join_Client_Password+"/"+main.Join_Client_phoneNumber+"/"+main.Join_Client_Address
				+"/"+main.Join_Client_Program);
		main.SaveText("Send join member to server: "+main.Join_Client_Name+"/"+main.Join_Client_ID
				+"/"+main.Join_Client_Password+"/"+main.Join_Client_phoneNumber+"/"+main.Join_Client_Address
				+"/"+main.Join_Client_Program);
		Stage stage=(Stage)sendBtn.getScene().getWindow();
		stage.close();
		
		
		//mySql ���� ���
		//�̸�, ���̵�, ��й�ȣ, ��ȭ��ȣ, �ּ�, ������α׷�,�� ���� String�� �̿��Ͽ� �����ش�.
		//mysql���� select "�̸�" * from joinTable
	
	}//moveSend()�Լ� ��-> ȸ�����Խ� ������ server�� ������(mysql���)
	
	@FXML public void selectTina() {
		Selected_Program="Net";
		net_Btn.setSelected(false);
		tina_Btn.setSelected(true);
	}//selectTina()�Լ� ��
	
	@FXML public void selectNet() {
		Selected_Program="Tina";
		net_Btn.setSelected(true);
		tina_Btn.setSelected(false);
	}//selectNet()�Լ� ��
	
	@FXML public void ID_Check() {
		if(ID_TextField.getText().trim().equals("")) {
			JOptionPane.showMessageDialog(null,"���̵� �Է��ϼ���","�˸�",JOptionPane.ERROR_MESSAGE);
		}
		main.Send_Message("CheckID/"+ID_TextField.getText().trim());
		if(existID.equals("exist")) {
			JOptionPane.showMessageDialog(null,"�̹� �����ϴ� ���̵��Դϴ�.","�˸�",JOptionPane.ERROR_MESSAGE);
		}//�̹� ID�� ������ ���
		else {
			sendBtn.setDisable(false);
		}//�̹� ID�� �������� ���� ���
		
	}//ID_ChecK()�Լ� �� -> ���̵� �ߺ� Ȯ���ϴ� �Լ�
	
	public void Set_JoinCont_UserInfo() {
		ID=ID_TextField.getText();
		PassWord=Password_TextField.getText();
		Phone=Phone_TextField.getText();
		Addr=Addr_TextField.getText();
		Name=Name_TextField.getText();
	}//Set_JointCont_UserInfo()�Լ� ->�������� �ش� ��������
	
	public void Set_MainCont_UserInfo() {
		main.Join_Client_Name=Name;
		main.Join_Client_ID=ID;
		main.Join_Client_Password=PassWord;
		main.Join_Client_phoneNumber=Phone;
		main.Join_Client_Address = Addr;
		main.Join_Client_Program = Selected_Program;
	}//Set_MainCont_UserInfo()�Լ� -> main�� �������� ����
	
	public void Init(MainController main_controller) {
		main = main_controller;
		
	}//init()�Լ� ��
}
