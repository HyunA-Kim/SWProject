package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class ServerLog_Controller implements Initializable{
	@FXML TableView<TableRowDataModel_Log> Log_List;					//서버로그 저장 테이블뷰 명
	@FXML TableColumn<TableRowDataModel_Log,Integer> Num_Column;		//인덱스 저장
	@FXML TableColumn<TableRowDataModel_Log,String> LogDate_Column;		//날짜 저장
	@FXML TableColumn<TableRowDataModel_Log, String> LogMsg_Column;		//로그 내용저장
	@FXML Button closeBtn;
	
	
	public Integer Index;		//인덱스 변수로 저장
	public String date;			//날짜 변수로 저장
	public String LogMsg;		//메세지 변수로 저장
	

	private MainController main;
	
	@FXML public void moveClose() {
		Stage stage=(Stage)closeBtn.getScene().getWindow();
		stage.close();
	}//moveClose()함수 끝

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		Num_Column.setCellValueFactory(cellData->cellData.getValue().IndexProperty().asObject());
		LogDate_Column.setCellValueFactory(cellData->cellData.getValue().TimeProperty());
		LogMsg_Column.setCellValueFactory(cellData->cellData.getValue().MsgProperty());
		Platform.runLater(()->{
			Log_List.setItems(main.ob_Log_List);
		});
	}//initialize()함수 끝 -> 테이블 뷰 초기화

	public void init(MainController mainController) {
		// TODO Auto-generated method stub
		main=mainController;
	}
	
}
