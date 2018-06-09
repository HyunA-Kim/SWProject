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
	
	@FXML TableView<TableRowDataModel_File> File_List;						//���� ��� ����Ʈ
	@FXML TableColumn<TableRowDataModel_File,String> FileProgram_Column;	//���α׷���
	@FXML TableColumn<TableRowDataModel_File,String> FileID_Column;			//���̵��
	@FXML TableColumn<TableRowDataModel_File,String> FileMsg_Column;		//�޼�������
	@FXML TableColumn<TableRowDataModel_File,String> FileTime_Column;		//���۽ð�
	@FXML TableColumn<TableRowDataModel_File,String> FileSize_Column;		//����ũ��
	@FXML TableColumn<TableRowDataModel_File,String> FileDay_Column;		//���۹����ð�
	
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
	}//initialize()�Լ� �� -> ���̺�� �ʱ�ȭ ����
	
}
