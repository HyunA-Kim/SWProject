package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class Help_Controller implements Initializable{
	@FXML ListView<String> Help_List; 			//도움말 나타내는 리스트
	@FXML TextArea Explain_TextArea;			//설명 나타내는 area
	@FXML Button closeBtn;
	
	String name; 								//리스트 순서
	MainController main;
	
	ObservableList<String> list= FXCollections.observableArrayList("Request_FileSend","Request_Quit","Request_Connection","Quit");		//observe 리스트 뷰
	//리스트 설정 효과
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		Help_List.setItems(list);
		Help_List.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);		
		Help_List.setOnMouseClicked((MouseEvent)-> {
			name=Help_List.getSelectionModel().getSelectedItem().trim();
			if(name.equals("Request_FileSend")) {
			Explain_TextArea.setText("서버에서 클라이언트로 파일전송 요청 프로토콜 = Get file과 같은 기능이며 단지 메세지로 프로토콜을 전달하여 파일을 받아 올 수 있도록 하는 기능의 차이이며, 파일 전송의 성공여부를 file send success/fail의 메세지를 클라이언트로부터 받아 올 수 있다.");
			}
			else if(name.equals("Request_Quit")) {
			Explain_TextArea.setText("클라이언트 종료 요청 성공적으로 종료가 되면 클라이언트로부터 종료 메시지를 받아 올 수 있다.");
			}
			else if(name.equals("Request_Connection")) {
			Explain_TextArea.setText("클라이언트에서 서버와의 연결 여부를 받아올 수 있다. 연결중이라면 Connection OK, 아니라면 Connection Error로 클라이언트로부터 메시지를 받아 올 수 있다.");
			}
			else if(name.equals("Quit")) {
			Explain_TextArea.setText("서버에서 메세지 보내기 기능을 이용하여 클라이언트를 강제적으로 종료시킬 수 있는 메세지 명령어이다.");	
			}
		});
	}//명령어 도움말 기능

	@FXML public void moveClose() {
		Stage stage=(Stage)closeBtn.getScene().getWindow();
		stage.close();
	}//moveClose()함수 끝
	
	public void init(MainController mainController) {
		// TODO Auto-generated method stub
		main=mainController;
	}//init()함수 끝

	
}
