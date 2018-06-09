package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class ClientLog_Controller implements Initializable{

	//*************************테이블뷰 *********************************
	@FXML TableView<TableRowDataModel_ClientLog> Log_List;					//테이블뷰 명
	@FXML TableColumn<TableRowDataModel_ClientLog,Integer> Num_Column;		//순서
	@FXML TableColumn<TableRowDataModel_ClientLog,String> LogDate_Column;	//시각
	@FXML TableColumn<TableRowDataModel_ClientLog, String> LogMsg_Column;	//메세지
	//*****************************************************************
	@FXML Button closeBtn;
	
	
	public Integer Index;			//인덱스
	public String date;				//날짜
	public String LogMsg;			//메세지
	
	//*************************컨트롤러 *********************************
	private MainController main;
	private ClientController client;
	//****************************************************************
	
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
	}//initialize()함수 끝 -> 테이블 뷰 설정

	public void Init(MainController main2, ClientController clientController) {
		// TODO Auto-generated method stub
		main=main2;
		client=clientController;
	}//init()함수 끝
	
}
