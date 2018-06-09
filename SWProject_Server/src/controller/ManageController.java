package controller;

import java.io.IOException;
import controller.MainController;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import controller.ManageDetail_Controller;
public class ManageController {
	@FXML Label TinaLbl;
	@FXML Label NetLbl;
	@FXML Label TotalLbl;
	@FXML RadioButton TinaBtn;
	@FXML RadioButton NetBtn;
	@FXML RadioButton TotalBtn;
	@FXML Circle ColorDot;
	@FXML Button BeforeBtn;
	@FXML Button NextBtn;
	
	@FXML ManageDetail_Controller manageDetail_Controller;
	private MainController main;
	@FXML public void CheckTina() {
		TinaBtn.setSelected(true);NetBtn.setSelected(false);
		TotalBtn.setSelected(false);
	}
	@FXML public void CheckNet() {
		
		TinaBtn.setSelected(false);
		TotalBtn.setSelected(false);NetBtn.setSelected(true);
	}
	@FXML public void CheckTotal() {
		TinaBtn.setSelected(false);
		NetBtn.setSelected(false);
		TotalBtn.setSelected(true);
	}
	@FXML public void moveBefore() {
		Stage stage=(Stage)BeforeBtn.getScene().getWindow();
		stage.close();
	}
	@FXML public void moveNext() {
		if(TinaBtn.isSelected()==false && NetBtn.isSelected()==false && TotalBtn.isSelected()==false) 
			{System.out.println("하나를 클릭해주세요"); main.SaveText("하나를 클릭해주세요");}
		try {
			Stage primaryStage=(Stage)NextBtn.getScene().getWindow();
			Stage dialog=new Stage(StageStyle.UTILITY);
			dialog.initOwner(primaryStage);
			dialog.setTitle("메니지먼트 관리 시스템-정상");
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ManageDetail.fxml"));
			Parent root = (Parent) loader.load();
			
			manageDetail_Controller = loader.getController(); // 포트컨트롤러 load 
			
			Stage stage = new Stage();
			stage.setScene(new Scene(root));
			stage.show();
			
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
}
