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

	//*************************���̺��� *********************************
	@FXML TableView<TableRowDataModel_ClientLog> Log_List;					//���̺��� ��
	@FXML TableColumn<TableRowDataModel_ClientLog,Integer> Num_Column;		//����
	@FXML TableColumn<TableRowDataModel_ClientLog,String> LogDate_Column;	//�ð�
	@FXML TableColumn<TableRowDataModel_ClientLog, String> LogMsg_Column;	//�޼���
	//*****************************************************************
	@FXML Button closeBtn;
	
	
	public Integer Index;			//�ε���
	public String date;				//��¥
	public String LogMsg;			//�޼���
	
	//*************************��Ʈ�ѷ� *********************************
	private MainController main;
	private ClientController client;
	//****************************************************************
	
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
	}//initialize()�Լ� �� -> ���̺� �� ����

	public void Init(MainController main2, ClientController clientController) {
		// TODO Auto-generated method stub
		main=main2;
		client=clientController;
	}//init()�Լ� ��
	
}