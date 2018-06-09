package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class Help_Controller implements Initializable{
	@FXML ListView<String> Help_List; 			//���� ��Ÿ���� ����Ʈ
	@FXML TextArea Explain_TextArea;			//���� ��Ÿ���� area
	@FXML Button closeBtn;
	
	String name; 								//����Ʈ ����
	MainController main;
	
	ObservableList<String> list= FXCollections.observableArrayList("Request_FileSend","Request_Quit","Request_Connection","Quit");		//observe ����Ʈ ��
	//����Ʈ ���� ȿ��
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		Help_List.setItems(list);
		Help_List.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);		
		Help_List.setOnMouseClicked((MouseEvent)-> {
			name=Help_List.getSelectionModel().getSelectedItem().trim();
			if(name.equals("Request_FileSend")) {
			Explain_TextArea.setText("�������� Ŭ���̾�Ʈ�� �������� ��û �������� = Get file�� ���� ����̸� ���� �޼����� ���������� �����Ͽ� ������ �޾� �� �� �ֵ��� �ϴ� ����� �����̸�, ���� ������ �������θ� file send success/fail�� �޼����� Ŭ���̾�Ʈ�κ��� �޾� �� �� �ִ�.");
			}
			else if(name.equals("Request_Quit")) {
			Explain_TextArea.setText("Ŭ���̾�Ʈ ���� ��û ���������� ���ᰡ �Ǹ� Ŭ���̾�Ʈ�κ��� ���� �޽����� �޾� �� �� �ִ�.");
			}
			else if(name.equals("Request_Connection")) {
			Explain_TextArea.setText("Ŭ���̾�Ʈ���� �������� ���� ���θ� �޾ƿ� �� �ִ�. �������̶�� Connection OK, �ƴ϶�� Connection Error�� Ŭ���̾�Ʈ�κ��� �޽����� �޾� �� �� �ִ�.");
			}
			else if(name.equals("Quit")) {
			Explain_TextArea.setText("�������� �޼��� ������ ����� �̿��Ͽ� Ŭ���̾�Ʈ�� ���������� �����ų �� �ִ� �޼��� ��ɾ��̴�.");	
			}
		});
	}//��ɾ� ���� ���

	@FXML public void moveClose() {
		Stage stage=(Stage)closeBtn.getScene().getWindow();
		stage.close();
	}//moveClose()�Լ� ��
	
	public void init(MainController mainController) {
		// TODO Auto-generated method stub
		main=mainController;
	}//init()�Լ� ��

	
}
