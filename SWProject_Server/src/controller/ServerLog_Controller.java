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
	@FXML TableView<TableRowDataModel_Log> Log_List;					//�����α� ���� ���̺�� ��
	@FXML TableColumn<TableRowDataModel_Log,Integer> Num_Column;		//�ε��� ����
	@FXML TableColumn<TableRowDataModel_Log,String> LogDate_Column;		//��¥ ����
	@FXML TableColumn<TableRowDataModel_Log, String> LogMsg_Column;		//�α� ��������
	@FXML Button closeBtn;
	
	
	public Integer Index;		//�ε��� ������ ����
	public String date;			//��¥ ������ ����
	public String LogMsg;		//�޼��� ������ ����
	

	private MainController main;
	
	@FXML public void moveClose() {
		Stage stage=(Stage)closeBtn.getScene().getWindow();
		stage.close();
	}//moveClose()�Լ� ��

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		Num_Column.setCellValueFactory(cellData->cellData.getValue().IndexProperty().asObject());
		LogDate_Column.setCellValueFactory(cellData->cellData.getValue().TimeProperty());
		LogMsg_Column.setCellValueFactory(cellData->cellData.getValue().MsgProperty());
		Platform.runLater(()->{
			Log_List.setItems(main.ob_Log_List);
		});
	}//initialize()�Լ� �� -> ���̺� �� �ʱ�ȭ

	public void init(MainController mainController) {
		// TODO Auto-generated method stub
		main=mainController;
	}
	
}
