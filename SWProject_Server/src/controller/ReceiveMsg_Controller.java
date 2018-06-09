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
public class ReceiveMsg_Controller implements Initializable {
	
	@FXML TableView<TableRowDataModel_Msg> Receive_List;			//메세지 테이블 뷰 명
	@FXML TableColumn<TableRowDataModel_Msg,String> Program_Column;	//프로그램 명 저장
	@FXML TableColumn<TableRowDataModel_Msg,String> ID_Column;		//아이디 저장
	@FXML TableColumn<TableRowDataModel_Msg,String> Msg_Column;		//내용 저장
	@FXML TableColumn<TableRowDataModel_Msg,String> Time_Column;	//시간 저장
	
	@FXML Button closeBtn;
	
	public String date;			// 날짜변수 저장
	public String time;			// 시간변수 저장
	public String ID;			// 아이디 저장
	public String Msg;			// 메세지 저장
	public String Program;		// 프로그램 명 저장
	
	private MainController main;
	
	
	@FXML public void moveClose() {
		main.ob_Receive_List.clear();
		Stage stage=(Stage)closeBtn.getScene().getWindow();
		stage.close();
	}//moveClose() 함수 끝 
	
	public void init(MainController mainController) {
		// TODO Auto-generated method stub
		main=mainController;
	}//init() 함수 끝
	
	@Override
	
	
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		Program_Column.setCellValueFactory(cellData->cellData.getValue().ProgramProperty());
		ID_Column.setCellValueFactory(cellData->cellData.getValue().IDProperty());
		Msg_Column.setCellValueFactory(cellData->cellData.getValue().MsgProperty());
		Time_Column.setCellValueFactory(cellData->cellData.getValue().TimeProperty());
		Platform.runLater(()->{
			Receive_List.setItems(main.ob_Receive_List);
		});
	}//테이블 뷰 초기화 설정
}
