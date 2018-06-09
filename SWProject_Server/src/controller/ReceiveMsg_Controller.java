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
	
	@FXML TableView<TableRowDataModel_Msg> Receive_List;			//�޼��� ���̺� �� ��
	@FXML TableColumn<TableRowDataModel_Msg,String> Program_Column;	//���α׷� �� ����
	@FXML TableColumn<TableRowDataModel_Msg,String> ID_Column;		//���̵� ����
	@FXML TableColumn<TableRowDataModel_Msg,String> Msg_Column;		//���� ����
	@FXML TableColumn<TableRowDataModel_Msg,String> Time_Column;	//�ð� ����
	
	@FXML Button closeBtn;
	
	public String date;			// ��¥���� ����
	public String time;			// �ð����� ����
	public String ID;			// ���̵� ����
	public String Msg;			// �޼��� ����
	public String Program;		// ���α׷� �� ����
	
	private MainController main;
	
	
	@FXML public void moveClose() {
		main.ob_Receive_List.clear();
		Stage stage=(Stage)closeBtn.getScene().getWindow();
		stage.close();
	}//moveClose() �Լ� �� 
	
	public void init(MainController mainController) {
		// TODO Auto-generated method stub
		main=mainController;
	}//init() �Լ� ��
	
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
	}//���̺� �� �ʱ�ȭ ����
}
