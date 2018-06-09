package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.stage.Stage;

public class ClientOption_Controller {
	
	@FXML public ColorPicker ColorBtn;			//���� ������ư
	@FXML public Button SaveBtn;				//�����ư
	@FXML public Button CloseBtn;				//�ݱ��ư
	@FXML public Button ChangeBtn;				//�����ư
	
	//************** ��Ʈ�ѷ� *************
	MainController main;
	ClientController client;
	//************** ��Ʈ�ѷ� *************
	
	@FXML public void moveSave(ActionEvent event) {
		client.Background_Color.setBackground(new Background(new BackgroundFill(ColorBtn.getValue(), CornerRadii.EMPTY, Insets.EMPTY)));
		main.SaveText(main.ID+" : Set background color");
		Stage stage=(Stage)SaveBtn.getScene().getWindow();
		stage.close();
	}//moveSave()�Լ� ��
	
	@FXML public void moveChange(ActionEvent event) {
		client.Background_Color.setBackground(new Background(new BackgroundFill(ColorBtn.getValue(), CornerRadii.EMPTY, Insets.EMPTY)));
		main.SaveText(main.ID+" : Change background color");
		Stage stage=(Stage)SaveBtn.getScene().getWindow();
		stage.close();
	}//moveChange()�Լ� ��
	
	@FXML public void moveClose(ActionEvent event) {
		Stage stage=(Stage)SaveBtn.getScene().getWindow();
		stage.close();
	}//moveClose()�Լ� ��
	
	public void Init(MainController main2, ClientController clientController) {
		// TODO Auto-generated method stub
		main=main2;
		client=clientController;
		
	}//init()�Լ� ��
}
