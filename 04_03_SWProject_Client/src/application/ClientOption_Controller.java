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
	
	@FXML public ColorPicker ColorBtn;			//색깔 설정버튼
	@FXML public Button SaveBtn;				//저장버튼
	@FXML public Button CloseBtn;				//닫기버튼
	@FXML public Button ChangeBtn;				//변경버튼
	
	//************** 컨트롤러 *************
	MainController main;
	ClientController client;
	//************** 컨트롤러 *************
	
	@FXML public void moveSave(ActionEvent event) {
		client.Background_Color.setBackground(new Background(new BackgroundFill(ColorBtn.getValue(), CornerRadii.EMPTY, Insets.EMPTY)));
		main.SaveText(main.ID+" : Set background color");
		Stage stage=(Stage)SaveBtn.getScene().getWindow();
		stage.close();
	}//moveSave()함수 끝
	
	@FXML public void moveChange(ActionEvent event) {
		client.Background_Color.setBackground(new Background(new BackgroundFill(ColorBtn.getValue(), CornerRadii.EMPTY, Insets.EMPTY)));
		main.SaveText(main.ID+" : Change background color");
		Stage stage=(Stage)SaveBtn.getScene().getWindow();
		stage.close();
	}//moveChange()함수 끝
	
	@FXML public void moveClose(ActionEvent event) {
		Stage stage=(Stage)SaveBtn.getScene().getWindow();
		stage.close();
	}//moveClose()함수 끝
	
	public void Init(MainController main2, ClientController clientController) {
		// TODO Auto-generated method stub
		main=main2;
		client=clientController;
		
	}//init()함수 끝
}
