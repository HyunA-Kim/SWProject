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

public class ReceiveFile_Controller implements Initializable{
	@FXML Button closeBtn;
	
	@FXML TableView<TableRowDataModel_File> File_List;						//파일 목록 리스트
	@FXML TableColumn<TableRowDataModel_File,String> FileProgram_Column;	//프로그램명
	@FXML TableColumn<TableRowDataModel_File,String> FileID_Column;			//아이디명
	@FXML TableColumn<TableRowDataModel_File,String> FileMsg_Column;		//메세지내용
	@FXML TableColumn<TableRowDataModel_File,String> FileTime_Column;		//전송시간
	@FXML TableColumn<TableRowDataModel_File,String> FileSize_Column;		//파일크기
	@FXML TableColumn<TableRowDataModel_File,String> FileDay_Column;		//전송받은시각
	
	private MainController main;
	
	public void Init(MainController main_controller) {
		main=main_controller;
	}
	@FXML public void moveClose() {
		Stage stage=(Stage)closeBtn.getScene().getWindow();
		stage.close();
	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		FileProgram_Column.setCellValueFactory(cellData->cellData.getValue().ProgramProperty());
		FileID_Column.setCellValueFactory(cellData->cellData.getValue().IDProperty());
		FileMsg_Column.setCellValueFactory(cellData->cellData.getValue().MsgProperty());
		FileSize_Column.setCellValueFactory(cellData->cellData.getValue().SizeProeprty());
		FileTime_Column.setCellValueFactory(cellData->cellData.getValue().TimeProperty());
		FileDay_Column.setCellValueFactory(cellData->cellData.getValue().DayProperty());
		Platform.runLater(()->{
			File_List.setItems(main.ob_File_List);
		});
	}//initialize()함수 끝 -> 테이블뷰 초기화 설정
	
}
