package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class FilePath_Controller {
	@FXML TextField FilePath_TextField;			//���ϰ�� �ؽ�Ʈ�ʵ�
	public String get_FilePath;					//���ϰ�� ��Ʈ������
	
	@FXML Button SendBtn;						//���� ��ư
	@FXML private MainController main;			//���� ���� ��������
	@FXML private ClientController client;		//Ŭ���̾�Ʈ ���� ��������
	
	@FXML public void moveSend() {
		get_FilePath=FilePath_TextField.getText().trim();
		main.FilePath=get_FilePath;
		Stage stage=(Stage)SendBtn.getScene().getWindow();
		stage.close();
	}//moveSend()�Լ� �� -> ���ϰ�θ� �����ְ� �������ִ� �Լ��Դϴ�.

	public void Init(MainController main_controller, ClientController clientController) {
		// TODO Auto-generated method stub
		main=main_controller;
		client=clientController;
	}//init()�Լ� ��
}
