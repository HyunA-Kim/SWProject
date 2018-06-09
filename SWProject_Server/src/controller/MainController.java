package controller;



import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JOptionPane;

import controller.option.Option1_PortController;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

public class MainController implements Initializable{
	@FXML Button sendMsg;						//메세지 보내기버튼
	@FXML Button receiveMsg;					//받은메세지 보기버튼
	@FXML Button command;						//명령어보기 버튼(Help)
	@FXML Button receiveFile;					//받은파일 보기 버튼
	@FXML Button option;						//환경설정(IP/Port설정)	버튼
	@FXML Button manageBtn;						
	@FXML Button startBtn;						//서버시작버튼(Server execute)
	@FXML Button stopBtn;						//서버중지버튼(Server stop)
	@FXML Button logBtn;						//로그보기버튼(Server view Log)
	@FXML Button saveBtn;						//서버로그저장버튼(Server log save)
	@FXML Button Manage_RedBtn;					
	@FXML Button Manage_OrangeBtn;
	@FXML Button Manage_YellowBtn;
	@FXML Button Manage_GreenBtn;
	@FXML Button Tina_ReceiveFile_Btn;			//Tina 파일받기버튼(GetFile)
	@FXML Button Net_ReceiveFile_Btn;			//Net 파일받기버튼(GetFile)
	@FXML Button Tina_AutoFile_Btn;				//Tina 자동보내기버튼
	@FXML Button Net_AutoFile_Btn;				//Net 자동보내기 버튼
	@FXML Button Tina_NotAuto_Btn;				//Tina 자동취소 버튼
	@FXML Button Net_NotAuto_Btn;				//Net 자동취소 버튼
	@FXML Button DB_Btn;						//데이터베이스 설정 버튼
	@FXML Button Update_Btn;					//업데이트 설정 버튼
	
	@FXML public Label ServerTime_lbl;			//Server 접속시간 정보
	@FXML public Label IP_lbl;					//Server IP 정보
	@FXML public Label Port_lbl;				//Server Port 정보

	@FXML CheckBox Tina_TotalBox;				//Tina 전체 체크박스
	@FXML CheckBox Net_TotalBox;				//Net 전체 체크 박스
	
	//************************Tina 접속 클라이언트 테이블 뷰 ****************************
	@FXML public TableView<TableRowDataModel> tina_List;			
	@FXML private TableColumn<TableRowDataModel,Integer> Tina_indexColumn;
	@FXML private TableColumn<TableRowDataModel,String> Tina_IDColumn;
	@FXML private TableColumn<TableRowDataModel,String> Tina_PassWordColumn;
	@FXML private TableColumn<TableRowDataModel,String> Tina_IPColumn;
	@FXML private TableColumn<TableRowDataModel,String> Tina_CheckColumn;
	//**************************************************************************
	
	//************************Net 접속 클라이언트 테이블 뷰 *****************************
	@FXML public TableView<TableRowDataModel> net_List;
	@FXML private TableColumn<TableRowDataModel,Integer> Net_indexColumn;
	@FXML private TableColumn<TableRowDataModel,String> Net_IDColumn;
	@FXML private TableColumn<TableRowDataModel,String> Net_PassWordColumn;
	@FXML private TableColumn<TableRowDataModel,String> Net_IPColumn;
	@FXML private TableColumn<TableRowDataModel,String> Net_CheckColumn;
	//**************************************************************************
	//***************************Database 변수***********************************
	public String driver = "com.mysql.jdbc.Driver";			// 드라이버
	public String DB_user="";								// 유저이름
	public String DB_password="";							// 패스워드
	public String dburl = "jdbc:mysql://127.0.0.1/userdb?useSSL=false&serverTimezone=UTC&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false";
	public boolean DBSave_Check=false;
	public Statement stmt = null; 							// Statement 객체 선언
	public ResultSet rs = null;
	//"jdbc:mysql://localhost:3306/사용할데이터베이스명?user=root&password=패스워드"
	//"jdbc:mysql://127.0.0.1/사용할데이터베이스명?user=root&password=패스워드" 
	public Connection conn = null;
	//**************************************************************************
	 
	//********************************시계***************************************
	boolean clock_stop=false;								//현재 시각, 연결시간 설정
	boolean runtime_stop=false;
	//**************************************************************************
	
	//******************************network 변수*********************************
	public String IP="";									// 서버아이피
	public int PORT=-1;										// 서버포트
	public ServerSocket server_Socket;						// 서버소켓
	public Socket socket;									// 클라이언트소켓
	public boolean UseDatabase=false;						// database 활용 여부
	//**************************************************************************
	
	//*****************************클라이언트 정보 벡터********************************
	public Vector Tina_Client_vc = new Vector();			// Tina 클라이언트의 모든 정보를 저장하고 있는 벡터
	public Vector Net_Client_vc = new Vector();				// Net 클라이언트의 모든 정보를 저장하고 있는 벡터
	public Vector Total_Client_vc = new Vector();			// 전체 클라이언트 벡터
	public StringTokenizer st; 								// 프로토콜을 구분하기위한 변수  ei) NoteSend/"명령어"
	//**************************************************************************
	
	public boolean auto=false;
	
	//***************************** 선택된 ID 저장 *********************************
	String Find_Tina_Client="";
	String Find_Net_Client="";
	public String Client_NickName;							// 테이블에서 선택된 클라이언트 이름
	//**************************************************************************
	
	//**************************연결된 클라이언트 갯수 **********************************
	int Tina_Count=0;										//Tina 연결갯수	
	int Net_Count=0;										//Net 연결갯수
	ArrayList<String> ID_List=new ArrayList<String>();		//ID존재여부 위해 만들어진 리스트
	@FXML public Label Connect_Lbl; 						//연결된 클라이언트 갯수 라벨
	//**************************************************************************
	
	//*************************자동보내기 클라이언트 갯수********************************
	int Auto_Tina_Count=0;									// Tina 자동설정 클라이언트 연결 갯수
	int Auto_Net_Count=0;									// Net 자동설정 클라이언트 연결 갯수
	@FXML public Label Auto_ConnectLbl;						// 자동설정 라벨
	String Period_number="";								// 얼만큼 자주 보낼 것인지 판단하는 주기 숫자
	String Period_text="";									// 보내는 주기 ei) Day/Hour/Min/Sec
	//**************************************************************************
	
	//***************************** Observable_List ************************************
	public ObservableList<TableRowDataModel_File> ob_File_List=FXCollections.observableArrayList();		 //받은 파일 리스트
	ObservableList<TableRowDataModel_Msg> ob_Receive_List= FXCollections.observableArrayList(); 	 //받은메세지 리스트
	ObservableList<TableRowDataModel_Log> ob_Log_List=FXCollections.observableArrayList();			 //로그 파일리스트
	ObservableList<TableRowDataModel> Tina_list= FXCollections.observableArrayList(); 				 //main Tina 리스트
	ObservableList<TableRowDataModel> Net_list= FXCollections.observableArrayList(); 				 //main net 리스트
	//**********************************************************************************
		
	int count=0;
	//**********************************컨트롤러 ***********************************
	@FXML public AnchorPane ap;
	
	@FXML Option1_PortController option1_PortController;
	@FXML SendMsg_Controller sendMsg_Controller;
	@FXML ReceiveMsg_Controller receiveMsg_Controller;
	@FXML CommandShow_Controller commandShow_Controller;
	@FXML ReceiveFile_Controller receiveFile_Controller;
	@FXML ManageController manageController;
	@FXML SetDB_Controller setDB_Controller;
	@FXML ServerLog_Controller serverLog_Controller;
	@FXML Help_Controller help_Controller;
	@FXML Period_Controller period_Controller;
	//**************************************************************************
	
	@FXML public void moveDB() {
		try {
			Stage primaryStage=(Stage)DB_Btn.getScene().getWindow();
			Stage dialog=new Stage(StageStyle.UTILITY);
			dialog.initOwner(primaryStage);
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SetDB.fxml"));
			Parent root = (Parent) loader.load();
			
			setDB_Controller = loader.getController(); // 포트컨트롤러 load
			setDB_Controller.Init(this);
			if(DBSave_Check==true) {
				setDB_Controller.ID_TextField.setText(DB_user);
				setDB_Controller.Password_TextField.setText(DB_password);
			}
			Stage stage = new Stage();
			stage.setTitle("DB setting");
			stage.setScene(new Scene(root));
			stage.show();
			
		}catch(IOException e) {
			//e.printStackTrace();
		}
	}//moveDB()함수 끝 -> DB설정버튼 동작함수
	
	@FXML public void move_Tina_ReceiveFile(ActionEvent event) {
		if(!startBtn.isDisable()) {
			JOptionPane.showMessageDialog(null,"서버의 연결이 필요합니다.","알림",JOptionPane.ERROR_MESSAGE);
			SaveText("Error : Don't click getfile. Server connection first");
			return ;
		}
		table_select();
		if(Tina_TotalBox.isSelected() && Net_TotalBox.isSelected()) {
			Client_NickName="Everyone";
			BroadCast("Everyone",Client_NickName+"/"+"Request_FileSend/"+"파일을 전송해주세요");
			System.out.println("everyone에게 파일받기 버튼입니다");
		}//모두 파일전송(broadcast)
		else if(Tina_TotalBox.isSelected() && !Net_TotalBox.isSelected()) {
			Client_NickName="All_TinaMember";
			BroadCast("All_TinaMember",Client_NickName+"/"+"Request_FileSend/"+"파일을 전송해주세요");
			System.out.println("Tina 전체에게 파일받기 버튼입니다");
		}//Tina 전체 파일전송(broadcast-TinaMember)
		else if(!Tina_TotalBox.isSelected() && Net_TotalBox.isSelected()) {
			JOptionPane.showMessageDialog(null,"Net 전체박스 해제 후 Tina 아이디를 선택해주세요 ","알림",JOptionPane.ERROR_MESSAGE);
			return;
		}
		else {
			if(Find_Tina_Client.equals("")) {
				JOptionPane.showMessageDialog(null,"Tina 아이디를 선택하세요","알림",JOptionPane.ERROR_MESSAGE);
				return ;
			}
			Client_NickName=Find_Tina_Client;
			for(int i=0;i<Total_Client_vc.size();i++) {
			ClientInfo u = (ClientInfo)Total_Client_vc.elementAt(i);
				if(Client_NickName.equals(u.Client_ID)) {
				u.send_Message("Request_FileSend/"+"파일을 전송해주세요");
				System.out.println(Client_NickName+"파일받기 버튼입니다.");
				}
			}
		}//Tina 아이디 파일전송
		
	}//move_Tina_ReceiveFile()함수 끝 -> Tina파일전송설정
	
	@FXML public void move_Net_ReceiveFile(ActionEvent event) {
		if(!startBtn.isDisable()) {
			JOptionPane.showMessageDialog(null,"서버의 연결이 필요합니다.","알림",JOptionPane.ERROR_MESSAGE);
			SaveText("Error : Don't click getfile. Server connection first");
			return ;
		}
		table_select();
		if(Tina_TotalBox.isSelected() && Net_TotalBox.isSelected()) {
			Client_NickName="Everyone";
			BroadCast("Everyone",Client_NickName+"/"+"Request_FileSend/"+"파일을 전송해주세요");
			System.out.println("everyone에게 파일받기 버튼입니다");
		}//모두 파일받기(broadcast)
		else if(!Tina_TotalBox.isSelected() && Net_TotalBox.isSelected()) {
			Client_NickName="All_NetMember";
			BroadCast("All_NetMember",Client_NickName+"/"+"Request_FileSend/"+"파일을 전송해주세요");
			System.out.println("Net 전체에게 파일받기 버튼입니다");
		}//Net 전체멤버 파일받기(broadcast-NetMember)
		else if(Tina_TotalBox.isSelected() && !Net_TotalBox.isSelected()) {
			JOptionPane.showMessageDialog(null,"Tina 전체박스 해제 후 Net 아이디를 선택해주세요 ","알림",JOptionPane.ERROR_MESSAGE);
			return;
		}
		else {
			if(Find_Net_Client.equals("")) {
				JOptionPane.showMessageDialog(null,"Net 아이디를 선택하세요","알림",JOptionPane.ERROR_MESSAGE);
				return ;
			}
			Client_NickName=Find_Net_Client;
			for(int i=0;i<Total_Client_vc.size();i++) {
			ClientInfo u = (ClientInfo)Total_Client_vc.elementAt(i);
				if(Client_NickName.equals(u.Client_ID)) {
				u.send_Message("Request_FileSend/"+"파일을 전송해주세요");
				System.out.println(Client_NickName+"파일받기 버튼입니다.");
				}
			}
		}//Net 아이디 파일받기
		
	}//move_Net_ReceiveFile()함수 끝 -> Net 파일받기
	
	@FXML public void move_Tina_AutoFile(ActionEvent event) {
		if(!startBtn.isDisable()) {
			JOptionPane.showMessageDialog(null,"서버의 연결이 필요합니다.","알림",JOptionPane.ERROR_MESSAGE);
			SaveText("Error : Don't click auto getfile. Server connection first");
			return ;
		}
			table_select();
		 if(!Tina_TotalBox.isSelected() && Net_TotalBox.isSelected()) {
				JOptionPane.showMessageDialog(null,"Net 전체박스 해제 후 Tina 아이디를 선택해주세요 ","알림",JOptionPane.ERROR_MESSAGE);
				return;
		 }
		 else if(!Tina_TotalBox.isSelected() && Find_Tina_Client.equals("")) {
				JOptionPane.showMessageDialog(null,"Tina 아이디를 선택하세요","알림",JOptionPane.ERROR_MESSAGE);
				return ;
		 }
		 try {
			Stage primaryStage=(Stage)Tina_AutoFile_Btn.getScene().getWindow();
			Stage dialog=new Stage(StageStyle.UTILITY);
			dialog.initOwner(primaryStage);
			dialog.setTitle("Set Period");
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Period.fxml"));
			Parent root = (Parent) loader.load();
			
			period_Controller = loader.getController(); // 포트컨트롤러 load 
			period_Controller.init(this);
			Stage stage = new Stage();
			stage.setTitle("Set Period");
			stage.setScene(new Scene(root));
			stage.show();
			
		}catch(IOException e) {
			//e.printStackTrace();
		}
		
	}//move_Tina_AutoFile() 함수 끝 -> 주기 GUI로 넘어감

	public void on_Tinaauto() {
		// TODO Auto-generated method stub
		
		if(Tina_TotalBox.isSelected() && Net_TotalBox.isSelected()) {
			for(int i=0; i<Tina_list.size(); i++) {
				Tina_list.get(i).setCheck("true");
			}
			tina_List.setItems(Tina_list);
			tina_List.refresh();
			for(int i=0; i<Net_list.size(); i++) {
				Net_list.get(i).setCheck("true");
			}
			net_List.setItems(Net_list);
			net_List.refresh();
			BroadCast("Everyone","Everyone/FileSend_Auto/"+Period_number+"/"+Period_text);
			SaveText("Set AutoFile : Everyone set auto file");
			
			//자동연결 클라이언트 갯수
			Auto_Tina_Count=Tina_list.size();
			Auto_Net_Count=Net_list.size();
			int num=Auto_Tina_Count+Auto_Net_Count;
			Auto_ConnectLbl.setText(""+num);
			Tina_AutoFile_Btn.setDisable(true);
			Net_AutoFile_Btn.setDisable(true);
			Tina_NotAuto_Btn.setDisable(false);
			Net_NotAuto_Btn.setDisable(false);
		}//전체 자동전송(BroadCast-Everymember)
		else if(Tina_TotalBox.isSelected() && !Net_TotalBox.isSelected()) {
			for(int i=0; i<Tina_list.size(); i++) {
				Tina_list.get(i).setCheck("true");
			}
			tina_List.setItems(Tina_list);
			tina_List.refresh();
			
			BroadCast("All TinaMember","All TinaMember/FileSend_Auto/"+Period_number+"/"+Period_text);
			SaveText("Set AutoFile : All TinaMember set auto file");
			
			//자동연결 클라이언트 갯수
			Auto_Tina_Count=Tina_list.size();
			int num=Auto_Tina_Count+Auto_Net_Count;
			Auto_ConnectLbl.setText(""+num);
			Tina_AutoFile_Btn.setDisable(true);
			Tina_NotAuto_Btn.setDisable(false);
			
		}//Tina 전체 자동전송(BroadCast-All TinaMember)
		else {
			table_select();
			Client_NickName=Find_Tina_Client;
			
			for(int i=0; i<Tina_list.size(); i++) {
				if(Tina_list.get(i).IDProperty().getValue().equals(Client_NickName)) {
					Tina_list.get(i).setCheck("true");
				}
				else {
					Tina_list.get(i).setCheck("false");
				}
			} 	
			tina_List.setItems(Tina_list);
			tina_List.refresh();	
			//아이디 선택 후 체크 하기
			
			for(int i=0;i<Total_Client_vc.size();i++) {
			ClientInfo u = (ClientInfo)Total_Client_vc.elementAt(i);
				if(Client_NickName.equals(u.Client_ID)) {
				u.send_Message("FileSend_Auto/"+Period_number+"/"+Period_text);
				System.out.println(Client_NickName+"파일자동받기 버튼입니다.");
				}
			}
			SaveText("Set AutoFile :"+Client_NickName+" set auto file");
			
			//자동연결 클라이언트 갯수
			Auto_Tina_Count=1;
			int num=Auto_Tina_Count+Auto_Net_Count;
			Auto_ConnectLbl.setText(""+num);
			Tina_AutoFile_Btn.setDisable(true);
			Tina_NotAuto_Btn.setDisable(false);
		}//Tina 아이디 자동파일전송
		Tina_ReceiveFile_Btn.setDisable(true);
	}//on_TinaAuto()함수 끝 -> Tina 자동전송 설정
	
	@FXML public void move_Net_AutoFile(ActionEvent event) {
			if(!startBtn.isDisable()) {
			JOptionPane.showMessageDialog(null,"서버의 연결이 필요합니다.","알림",JOptionPane.ERROR_MESSAGE);
			SaveText("Error : Don't click auto getfile. Server connection first");
			return ;
			}
			table_select();
			if(Tina_TotalBox.isSelected() && !Net_TotalBox.isSelected()) {
				JOptionPane.showMessageDialog(null,"Tina 전체박스 해제 후 Net 아이디를 선택해주세요 ","알림",JOptionPane.ERROR_MESSAGE);
				return;
			}
			else if(!Net_TotalBox.isSelected() && Find_Net_Client.equals("")) {
				JOptionPane.showMessageDialog(null,"Net 아이디를 선택하세요","알림",JOptionPane.ERROR_MESSAGE);
				return ;
			}
			try {
				Stage primaryStage=(Stage)Tina_AutoFile_Btn.getScene().getWindow();
				Stage dialog=new Stage(StageStyle.UTILITY);
				dialog.initOwner(primaryStage);
				dialog.setTitle("Set Period");
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Period.fxml"));
				Parent root = (Parent) loader.load();
				
				period_Controller = loader.getController(); // 포트컨트롤러 load 
				period_Controller.init(this);
				Stage stage = new Stage();
				stage.setTitle("Set Period");
				stage.setScene(new Scene(root));
				stage.show();
				
			}catch(IOException e) {
				//e.printStackTrace();
			}
			
	}//move_Net_Autofile()함수 끝 -> 주기로 넘어감
	
	public void on_Netauto() {
		// TODO Auto-generated method stub
		table_select();
		if(Tina_TotalBox.isSelected() && Net_TotalBox.isSelected()) {
			for(int i=0; i<Tina_list.size(); i++) {
				Tina_list.get(i).setCheck("true");
			}
			for(int i=0; i<Net_list.size(); i++) {
				Net_list.get(i).setCheck("true");
			}
			tina_List.setItems(Tina_list);
			tina_List.refresh();
			net_List.setItems(Net_list);
			net_List.refresh();
			BroadCast("Everyone","Everyone/FileSend_Auto/"+Period_number+"/"+Period_text);
			SaveText("Set AutoFile : Everyone set auto file");
			
			//자동연결 클라이언트 갯수
			Auto_Tina_Count=Tina_list.size();
			Auto_Net_Count=Net_list.size();
			int num=Auto_Tina_Count+Auto_Net_Count;
			Auto_ConnectLbl.setText(""+num);
			Tina_AutoFile_Btn.setDisable(true);
			Net_AutoFile_Btn.setDisable(true);
			Tina_NotAuto_Btn.setDisable(false);
			Net_NotAuto_Btn.setDisable(false);
		}//전체 자동전송 (BroadCast->Everyone)
		else if(!Tina_TotalBox.isSelected() && Net_TotalBox.isSelected()) {
			for(int i=0; i<Net_list.size(); i++) {
				Net_list.get(i).setCheck("true");
			}
			net_List.setItems(Net_list);
			net_List.refresh();
			
			BroadCast("All NetMember","All NetMember/FileSend_Auto/"+Period_number+"/"+Period_text);
			SaveText("Set AutoFile : All NetMember set auto file");
			System.out.println("!");
			//자동연결 클라이언트 갯수
			Auto_Net_Count=Net_list.size();
			int num=Auto_Tina_Count+Auto_Net_Count;
			Auto_ConnectLbl.setText(""+num);
			Net_AutoFile_Btn.setDisable(true);
			Net_NotAuto_Btn.setDisable(false);
		}//Net 전체 자동전송(BroadCast->All NetMember)
		else {
			table_select();

			Client_NickName=Find_Net_Client;
			
			for(int i=0; i<Net_list.size(); i++) {
				if(Net_list.get(i).IDProperty().getValue().equals(Client_NickName)) {
					Net_list.get(i).setCheck("true");
				}
				else {
					Net_list.get(i).setCheck("false");
				}
			} 	
			net_List.setItems(Net_list);
			net_List.refresh();
			for(int i=0;i<Total_Client_vc.size();i++) {
				ClientInfo u = (ClientInfo)Total_Client_vc.elementAt(i);
					if(Client_NickName.equals(u.Client_ID)) {
					u.send_Message("FileSend_Auto/"+Period_number+"/"+Period_text);
					System.out.println(Client_NickName+"파일자동받기 버튼입니다.");
					}
				}
			SaveText("Set AutoFile :"+Client_NickName+" set auto file");
			
			Auto_Net_Count=1;
			int num=Auto_Tina_Count+Auto_Net_Count;
			Auto_ConnectLbl.setText(""+num);
			Net_AutoFile_Btn.setDisable(true);
			Net_NotAuto_Btn.setDisable(false);
		}//Net 아이디 자동전송
		Net_ReceiveFile_Btn.setDisable(true);
	}//on_NetAuto()함수 끝 -> Net 자동전송설정
	
	@FXML public void move_Tina_NotAutoFile(ActionEvent event) {
		if(!startBtn.isDisable()) {
			JOptionPane.showMessageDialog(null,"서버의 연결이 필요합니다.","알림",JOptionPane.ERROR_MESSAGE);
			SaveText("Error : Don't click release autofile. Server connection first");
			return ;
		}
		table_select();
		if(Tina_TotalBox.isSelected() && Net_TotalBox.isSelected()) {
			for(int i=0; i<Tina_list.size(); i++) {
				Tina_list.get(i).setCheck("false");
			}
			for(int i=0; i<Net_list.size(); i++) {
				Net_list.get(i).setCheck("false");
			}
			tina_List.setItems(Tina_list);
			tina_List.refresh();
			net_List.setItems(Net_list);
			net_List.refresh();
			BroadCast("Everyone", "Everyone/FileSend_NotAuto");
			SaveText("Remove set AutoFile : Everyone remove set auto file"); 
			
			//자동연결 클라이언트 갯수
			Auto_Tina_Count=0;
			Auto_Net_Count=0;
			int num=Auto_Tina_Count+Auto_Net_Count;
			Auto_ConnectLbl.setText(""+num);
			Tina_AutoFile_Btn.setDisable(false);
			Net_AutoFile_Btn.setDisable(false);
			Tina_NotAuto_Btn.setDisable(true);
			Net_NotAuto_Btn.setDisable(true);
		}//전체 자동전송 취소(BroadCast->Everyone)
		else if(Tina_TotalBox.isSelected() && !Net_TotalBox.isSelected()) {
			for(int i=0; i<Tina_list.size(); i++) {
				Tina_list.get(i).setCheck("false");
			}
			tina_List.setItems(Tina_list);
			tina_List.refresh();
			BroadCast("All TinaMember", "All TinaMember/FileSend_NotAuto");
			SaveText("Remove set AutoFile : All TinaMember remove set auto file");
			//자동연결 클라이언트 갯수
			Auto_Tina_Count=0;
			int num=Auto_Tina_Count+Auto_Net_Count;
			Auto_ConnectLbl.setText(""+num);
			Tina_AutoFile_Btn.setDisable(false);
			Tina_NotAuto_Btn.setDisable(true);
		}//Tina 자동전송취소(BroadCast->All TinaMember)
		else if(!Tina_TotalBox.isSelected() && Net_TotalBox.isSelected()) {
			JOptionPane.showMessageDialog(null,"Net 전체박스 해제 후 Tina 아이디를 선택해주세요 ","알림",JOptionPane.ERROR_MESSAGE);
		}
		else {
			if(Find_Tina_Client.equals("")) {
				JOptionPane.showMessageDialog(null,"Tina 아이디를 선택하세요","알림",JOptionPane.ERROR_MESSAGE);
				return ;
			}
			Client_NickName=Find_Tina_Client;
			
			for(int i=0; i<Tina_list.size(); i++) {
				if(Tina_list.get(i).IDProperty().getValue().equals(Client_NickName)) {
					Tina_list.get(i).setCheck("false");
				}
			} 	
			tina_List.setItems(Tina_list);
			tina_List.refresh();
			for(int i=0;i<Total_Client_vc.size();i++) {
				ClientInfo u = (ClientInfo)Total_Client_vc.elementAt(i);
					if(Client_NickName.equals(u.Client_ID)) {
					u.send_Message("FileSend_NotAuto/"+"자동파일을 취소해주세요");
					System.out.println(Client_NickName+"파일자동받기 버튼입니다.");
					}
				}
			SaveText("Remove set AutoFile :"+Client_NickName+"remove set auto file");
			
			Auto_Tina_Count=0;
			int num=Auto_Tina_Count+Auto_Net_Count;
			Auto_ConnectLbl.setText(""+num);
			Tina_AutoFile_Btn.setDisable(false);
			Tina_NotAuto_Btn.setDisable(true);
		}//Tina 아이디 자동전송취소
		Tina_ReceiveFile_Btn.setDisable(false);
		
	}//move_Tina_NotAutoFile()함수 끝 -> Tina 자동파일받기 취소
	
	@FXML public void move_Net_NotAutoFile(ActionEvent event) {
		if(!startBtn.isDisable()) {
			JOptionPane.showMessageDialog(null,"서버의 연결이 필요합니다.","알림",JOptionPane.ERROR_MESSAGE);
			SaveText("Error : Don't click release autofile. Server connection first");
			return ;
		}
		table_select();
		if(Tina_TotalBox.isSelected() && Net_TotalBox.isSelected()) {
			for(int i=0; i<Tina_list.size(); i++) {
				Tina_list.get(i).setCheck("false");
			}
			for(int i=0; i<Net_list.size(); i++) {
				Net_list.get(i).setCheck("false");
			}
			tina_List.setItems(Tina_list);
			tina_List.refresh();
			net_List.setItems(Net_list);
			net_List.refresh();
			BroadCast("Everyone", "Everyone/FileSend_NotAuto");
			SaveText("Remove set AutoFile :Everyone remove set auto file");
			//자동연결 클라이언트 갯수
			Auto_Tina_Count=0;
			Auto_Net_Count=0;
			int num=Auto_Tina_Count+Auto_Net_Count;
			Auto_ConnectLbl.setText(""+num);
			Tina_AutoFile_Btn.setDisable(false);
			Net_AutoFile_Btn.setDisable(false);
			Tina_NotAuto_Btn.setDisable(true);
			Net_NotAuto_Btn.setDisable(true);
		}//전체 자동설정 취소(BroadCast->Everyone)
		else if(!Tina_TotalBox.isSelected() && Net_TotalBox.isSelected()) {
			for(int i=0; i<Net_list.size(); i++) {
				Net_list.get(i).setCheck("false");
			}
			net_List.setItems(Net_list);
			net_List.refresh();
			BroadCast("All NetMember", "All NetMember/FileSend_NotAuto");
			SaveText("Remove set AutoFile :All NetMember remove set auto file");
			//자동연결 클라이언트 갯수
			Auto_Net_Count=0;
			int num=Auto_Tina_Count+Auto_Net_Count;
			Auto_ConnectLbl.setText(""+num);
			Net_AutoFile_Btn.setDisable(false);
			Net_NotAuto_Btn.setDisable(true);
		}//Net 전체 자동설정 취소(BroadCast->All NetMember)
		else if(Tina_TotalBox.isSelected() && !Net_TotalBox.isSelected()) {
			JOptionPane.showMessageDialog(null,"Tina 전체박스 해제 후 Net 아이디를 선택해주세요 ","알림",JOptionPane.ERROR_MESSAGE);
		}
		else {
			if(Find_Net_Client.equals("")) {
				JOptionPane.showMessageDialog(null,"Net 아이디를 선택하세요","알림",JOptionPane.ERROR_MESSAGE);
				return ;
			}
			Client_NickName=Find_Net_Client;
			
			for(int i=0; i<Net_list.size(); i++) {
				if(Net_list.get(i).IDProperty().getValue().equals(Client_NickName)) {
					Net_list.get(i).setCheck("false");
				}
			} 	
			net_List.setItems(Net_list);
			net_List.refresh();
			for(int i=0;i<Total_Client_vc.size();i++) {
				ClientInfo u = (ClientInfo)Total_Client_vc.elementAt(i);
					if(Client_NickName.equals(u.Client_ID)) {
					u.send_Message("FileSend_NotAuto/"+"자동파일을 취소해주세요");
					System.out.println(Client_NickName+"파일자동받기 버튼입니다.");
					}
				}
			SaveText("Remove set AutoFile :"+Client_NickName+" remove set auto file");
		
			
			//자동연결 클라이언트 갯수
			Auto_Net_Count=0;
			int num=Auto_Tina_Count+Auto_Net_Count;
			Auto_ConnectLbl.setText(""+num);
			Net_AutoFile_Btn.setDisable(false);
			Net_NotAuto_Btn.setDisable(true);
			
		}//Net 아이디 자동설정취소
		Net_ReceiveFile_Btn.setDisable(false);
	}//move_Net_NotAutofile()함수 끝 -> Net 자동파일받기취소
	
	
	@FXML public void moveOption(ActionEvent event) {
	
		try {
			if(startBtn.isDisable()) {
				JOptionPane.showMessageDialog(null,"현재 서버가 실행중입니다","알림",JOptionPane.ERROR_MESSAGE);
				SaveText("Error: Don't click option. Server is running");
				return ;
			}
			Stage primaryStage=(Stage)option.getScene().getWindow();
			Stage dialog=new Stage(StageStyle.UTILITY);
			dialog.initOwner(primaryStage);
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/option/Option1_Port.fxml"));
			Parent root = (Parent) loader.load();
			
			option1_PortController = loader.getController(); // 포트컨트롤러 load
			option1_PortController.init(this);
			Stage stage = new Stage();
			stage.setTitle("Option");
			stage.setScene(new Scene(root));
			stage.show();
			
		}catch(IOException e) {
			//e.printStackTrace();
		}
	}//moveOption()함수 끝 -> 아이피/포트번호 설정
	
	@FXML public void moveSend(ActionEvent event) {
		if(!startBtn.isDisable()) {
			JOptionPane.showMessageDialog(null,"서버의 연결이 필요합니다.","알림",JOptionPane.ERROR_MESSAGE);
			SaveText("Error: Don't click sendmessage. Server connection first");
			return ;
		}
		table_select();
		if(Tina_TotalBox.isSelected() && Net_TotalBox.isSelected()) {
			Client_NickName="Everyone";
		}
		if(Tina_TotalBox.isSelected() && !Net_TotalBox.isSelected()) {
			Client_NickName="All TinaMember";
		}//티나 전체 방송
		if(!Tina_TotalBox.isSelected() && Net_TotalBox.isSelected()) {
			Client_NickName="All NetMember";
		}//전체 선택일 경우 broadcast
		if(!Tina_TotalBox.isSelected() && !Net_TotalBox.isSelected()) {
			if(Find_Net_Client.equals("")&&Find_Tina_Client.equals("")) {
				JOptionPane.showMessageDialog(null,"아이디를 선택하세요","알림",JOptionPane.ERROR_MESSAGE);
				return ;
			}
			else if(Find_Net_Client.equals("")) {Client_NickName=Find_Tina_Client;}
			else {Client_NickName=Find_Net_Client;}
		}
	 		
		try {
			Stage primaryStage=(Stage)sendMsg.getScene().getWindow();
			Stage dialog=new Stage(StageStyle.UTILITY);
			dialog.initOwner(primaryStage);
			dialog.setTitle("메세지 보내기");
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SendMsg.fxml"));
			Parent root = (Parent) loader.load();
			
			sendMsg_Controller = loader.getController(); // 포트컨트롤러 load 
			sendMsg_Controller.Init(this);
			
			Stage stage = new Stage();
			stage.setTitle("Sending message");
			stage.setScene(new Scene(root));			
			stage.show();
			
		}catch(IOException e) {
		
		}
	}//moveSend()함수 끝 -> 클라이언트로 메세지보내는 함수
	
	@FXML public void moveReceive(ActionEvent event) {
		if(!startBtn.isDisable()) {
			JOptionPane.showMessageDialog(null,"서버의 연결이 필요합니다.","알림",JOptionPane.ERROR_MESSAGE);
			SaveText("Error : Don't click receivemsg. Server connection first");
			return ;
		}
		try {
			Stage primaryStage=(Stage)receiveMsg.getScene().getWindow();
			Stage dialog=new Stage(StageStyle.UTILITY);
			dialog.initOwner(primaryStage);
			dialog.setTitle("받은 메세지");
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ReceiveMsg.fxml"));
			Parent root = (Parent) loader.load();
			
			receiveMsg_Controller = loader.getController(); // 포트컨트롤러 load 
			receiveMsg_Controller.init(this);
			if(Receive_Msg_SQL()==true) {
			Stage stage = new Stage();
			stage.setTitle("Receive message view");
			stage.setScene(new Scene(root));
			stage.show();
			}
			
		}catch(IOException e) {
			//e.printStackTrace();
		}
	
	}//moveReceive()함수 끝 -> 받은메세지 보여주는 함수
	
	private boolean Receive_Msg_SQL() {
		// TODO Auto-generated method stub
		String msg_searchSql;
		if(Tina_TotalBox.isSelected() && Net_TotalBox.isSelected()) {
			 msg_searchSql="select * from msgtbl";
		}
		else if(Tina_TotalBox.isSelected() && !Net_TotalBox.isSelected()) {
			msg_searchSql="select * from msgtbl where msgProgram='Tina'";
		}
		else if(!Tina_TotalBox.isSelected() && Net_TotalBox.isSelected()) {
			msg_searchSql="select * from msgtbl where msgProgram='Net'";
		}
		else{
			table_select();
			if(Find_Net_Client.equals("") && Find_Tina_Client.equals("")) {
				JOptionPane.showMessageDialog(null,"아이디를 선택하세요","알림",JOptionPane.ERROR_MESSAGE);
				return false;
			}//아무것도 선택되지 않았을 때 예외처리
			else if(Find_Net_Client.equals("")) {Client_NickName=Find_Tina_Client;} 
			else Client_NickName=Find_Net_Client;//ID 설정
			

			msg_searchSql="select * from msgtbl where msgID='"+Client_NickName+"'";
		}
		
		try {
			rs=stmt.executeQuery(msg_searchSql);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			//e1.printStackTrace();
		}//해당 결과를 rs에 저장
		try {
			while(rs.next()) {
				String Search_Program=rs.getString(1);
				String Search_ID=rs.getString(2);
				String Search_Msg=rs.getString(3);
				String Search_Time=rs.getString(4);
				ob_Receive_List.add(new TableRowDataModel_Msg(new SimpleStringProperty(Search_Program),new SimpleStringProperty(Search_ID),new SimpleStringProperty(Search_Msg),new SimpleStringProperty(Search_Time)));
				//해당 결과를 하나씩 받아와 list에 저장
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		return true;			
	}//Receive_Msg_SQL 함수 끝 -> SQL에서 메세지를 받아오는 함수(받은메세지보기에 필요)
	
	@FXML public void moveShow(ActionEvent event) {
		try {
			Stage primaryStage=(Stage)command.getScene().getWindow();
			Stage dialog=new Stage(StageStyle.UTILITY);
			dialog.initOwner(primaryStage);
			dialog.setTitle("명령어 보기");
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Help.fxml"));
			Parent root = (Parent) loader.load();
			
			help_Controller = loader.getController(); // 포트컨트롤러 load 
			help_Controller.init(this);
			Stage stage = new Stage();
			stage.setTitle("HELP");
			stage.setScene(new Scene(root));
			stage.show();
			
		}catch(IOException e) {
		//	e.printStackTrace();
		}
	
	}//moveShow()함수 끝 -> 명령어 보여주는 기능(HELP버튼)
	
	@FXML public void moveFile(ActionEvent event) {
		if(!startBtn.isDisable()) {
			JOptionPane.showMessageDialog(null,"서버의 연결이 필요합니다.","알림",JOptionPane.ERROR_MESSAGE);
			SaveText("Error : Don't click receivefile. Server connection first");
			return ;
		}
	
		try {
			Stage primaryStage=(Stage)receiveFile.getScene().getWindow();
			Stage dialog=new Stage(StageStyle.UTILITY);
			dialog.initOwner(primaryStage);
			dialog.setTitle("받은 파일");
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ReceiveFile.fxml"));
			Parent root = (Parent) loader.load();
			
			receiveFile_Controller = loader.getController(); // 포트컨트롤러 load 
			receiveFile_Controller.Init(this);
			Stage stage = new Stage();
			stage.setScene(new Scene(root));
			stage.setTitle("Receive file view");
			stage.show();
			
		}catch(IOException e) {
			//e.printStackTrace();
		}
	}//moveFile()함수 끝 -> 받은 파일을 보여주는 함수 
	
	@FXML public void moveStart(ActionEvent event) {
		
		table_initalize();	// 테이블 초기화
		table_select();		// 테이블 하나만 선택
		window_control(); 	//윈도우 X표시 컨트롤
	
		if(IP.equals("")||PORT==-1)	{JOptionPane.showMessageDialog(null,"아이피와 포트를 먼저 설정하세요","알림",JOptionPane.ERROR_MESSAGE); return;}
		if(UseDatabase==false) 	{JOptionPane.showMessageDialog(null,"데이터 베이스를 먼저 연결하세요","알림",JOptionPane.ERROR_MESSAGE); return;}
		
		Server_Start(); // 서버소켓을 생성하여 클라이언트 연결을 대기 하는 메소드
		File directory=new File("C:\\SWProject_Server");
		if(!directory.exists()) {
			directory.mkdirs();
			System.out.println("SWProject_Server 파일을 만들었습니다");
		}
	}//moveStart()함수 -> 서버execute 버튼 클릭시 서버 실행하게 됨
	
	// 테이블 초기화
	public void table_initalize() {
		
		Tina_indexColumn.setCellValueFactory(cellData->cellData.getValue().indexProperty().asObject());
		Tina_IDColumn.setCellValueFactory(cellData->cellData.getValue().IDProperty());
		Tina_PassWordColumn.setCellValueFactory(cellData->cellData.getValue().PassWordProperty());
		Tina_IPColumn.setCellValueFactory(cellData->cellData.getValue().IPProperty());
		Tina_CheckColumn.setCellValueFactory(cellData->cellData.getValue().SelectProperty());
		tina_List.setItems(Tina_list);
		
		
		Net_indexColumn.setCellValueFactory(cellData->cellData.getValue().indexProperty().asObject());
		Net_IDColumn.setCellValueFactory(cellData->cellData.getValue().IDProperty());
		Net_PassWordColumn.setCellValueFactory(cellData->cellData.getValue().PassWordProperty());
		Net_IPColumn.setCellValueFactory(cellData->cellData.getValue().IPProperty());
		Net_CheckColumn.setCellValueFactory(cellData->cellData.getValue().SelectProperty());
		net_List.setItems(Net_list);
		
	}//table_initalize()함수 -> 테이블 값을 사용할 수 있도록 설정

	public void table_select() {
		tina_List.setOnMouseClicked(event->{
			net_List.getSelectionModel().clearSelection();
			try {
			Find_Tina_Client=tina_List.getSelectionModel().getSelectedItem().IDProperty().getValue();
			}
			catch(NullPointerException npe) {
				Find_Tina_Client="";
			}
			Find_Net_Client="";
			Tina_TotalBox.setSelected(false);
			Net_TotalBox.setSelected(false);
			System.out.println(Find_Net_Client);
			
		});
		net_List.setOnMouseClicked(event->{
			tina_List.getSelectionModel().clearSelection();
			try {
			Find_Net_Client=net_List.getSelectionModel().getSelectedItem().IDProperty().getValue();
			}
			catch(NullPointerException npe) {
				Find_Net_Client="";
			}
			System.out.println("Selected Tina: "+Find_Tina_Client);	
			Find_Tina_Client="";
			Tina_TotalBox.setSelected(false);
			Net_TotalBox.setSelected(false);
		});
		Tina_TotalBox.setOnMouseClicked(event->{
			tina_List.getSelectionModel().clearSelection();
			net_List.getSelectionModel().clearSelection();
			Find_Tina_Client="";
			Find_Net_Client="";
		});
		Net_TotalBox.setOnMouseClicked(event->{
			tina_List.getSelectionModel().clearSelection();
			net_List.getSelectionModel().clearSelection();
			Find_Tina_Client="";
			Find_Net_Client="";
		});
	}//table_select()함수 끝 -> 테이블의 아이디를 선택하게 해주는 함수
	
	public void on_Database() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("Database's Driver Load Success");
			SaveText("Database's Driver Load Success");
			conn = DriverManager.getConnection(dburl,DB_user,DB_password);
			System.out.println("Database SQL Server Connection Success");
			SaveText("Database SQL Server Connection Success");
			stmt = conn.createStatement();	
			UseDatabase=true;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			SaveText("Error : Not found SQL");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			JOptionPane.showMessageDialog(null,"SQL 접속이 되지 않습니다. 아이디/비번 또는 컴퓨터에 연결되어있는지 확인하십시오.","알림",JOptionPane.ERROR_MESSAGE);
			SaveText("Error : SQl is not running or not connect in the computer");
		}
		
	}//on_Databae()함수 끝 -> 데이터베이스와 서버 연동시키는 함수
	
	
	public void Server_Start() {
		try {
			net_List.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
			tina_List.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
			
			server_Socket = new ServerSocket(PORT); // 서버소켓생성
			if(server_Socket!=null) { // 서버가 성공적으로 열렸을 경우
				startBtn.setDisable(true);
				stopBtn.setDisable(false);
				JOptionPane.showMessageDialog(null,"서버가 실행되었습니다","알림",JOptionPane.INFORMATION_MESSAGE);
				runtime(); //시계 동작
				Connection(); // 스트림을 설정하여 클라이언트 연결을 대기하는 함수
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,"이미 사용중인 포트입니다","알림",JOptionPane.ERROR_MESSAGE);
			SaveText("Error : 이미 사용중인 포트입니다");
		}
	}//Server_Start()함수 끝 -> 서버 시작 세부사항 표시 (테이블 뷰 상태 초기화, 소켓생성, 스트림 생성 등)
	
	public void Connection() {
		Thread th = new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					try {
						SaveText("Wait for Client connction");
						socket = server_Socket.accept();
						SaveText("Connected Client");
						ClientInfo new_Client = new ClientInfo(socket);
						new_Client.start();
						
					} catch (IOException e) {
						JOptionPane.showMessageDialog(null,"서버 소켓을 상실 하였습니다.","알림",JOptionPane.ERROR_MESSAGE);
						SaveText("서버 소켓을 상실 하였습니다.");
						e.printStackTrace();
						break;
					}// 클라이언트 연결 무한대기 
				}
			}
		});
		
		th.start(); 
	}//Connection()함수 끝 -> 서버 무한대기상태로 클라이언트 multithread 접속 할 수 있도록 함
	
	
	
	
	public class ClientInfo extends Thread{
		//***********Client 스트림 변수**********
		public InputStream is;				//스트림
		public OutputStream os;
		public DataInputStream dis;			//데이터스트림
		public DataOutputStream dos;
		public Socket Client_Socket;		//클라이언트 정보
		public String Client_ID;
		public String Client_Password;
		public String Client_IP;
		public String Client_Program;
		//************************************
		
		private ClientInfo(Socket soc) {
			this.Client_Socket = soc;	// 클라이언트의 소켓을 설정
			ClientNetWork();			// 스트림 설정 
		}
		
		private void ClientNetWork() {
			try {
				is = Client_Socket.getInputStream();
				os = Client_Socket.getOutputStream();
				dis = new DataInputStream(is);
				dos = new DataOutputStream(os);
			}
			catch(IOException e) {
				JOptionPane.showMessageDialog(null,"Error :setting stream","알림",JOptionPane.ERROR_MESSAGE);
				SaveText("error : Stream 설정 에러");
			}
		}//CLientNetWork()함수 끝 -> 스트림 설정함수
		
		public void run() {
			while(true) {
				try {
					String msg = dis.readUTF();
					System.out.println(Client_IP+": 로부터 들어온 메세지 :"+msg+"\n");
					InMessage(msg); // 메세지를 해석할 수 있는 함수  ei) 프로토콜 / 메세지 
				}
				catch(IOException e) {
					// 클라이언트가 강제 종료를 하였을 경우
					try {
						dos.close();
						dis.close();
						Client_Socket.close();
						Total_Client_vc.remove(this);
						if(Client_Program.equals("Tina")){
							   Tina_Client_vc.remove(this);
							   Tina_list.remove(Client_ID);
							   tina_List.setItems(Tina_list);
							  
						}
						else { Net_Client_vc.remove(this);
							   Net_list.remove(Client_ID);
							   net_List.setItems(Net_list);
						}

						// 리스트를 초기화(새로고침) Client_list.setListData(Client_Name_vc)
					} catch(Exception e2) { }
				}
			}
		}//run()함수 끝 -> 클라이언트로부터 메세지를 지속적으로 받아오는 함수
	
		/**
		 * @param msg
		 */
		public void InMessage(String msg) {
			st = new StringTokenizer(msg,"/");
		
			String protocol = st.nextToken(); // 프로토콜
			String message = st.nextToken();  // ID
			
			System.out.println("프로토콜: "+protocol);
			
			System.out.println("메세지: "+message);
			if(protocol.equals("Join")) {
				//message => 이름
				String Join_ID = st.nextToken();
				String Join_Password = st.nextToken();
				String Join_phoneNumber = st.nextToken();
				String Join_Address = st.nextToken();
				String Join_Program = st.nextToken();
				
				String insertSql = "insert into membertbl values"+"("
				+"'"+message+"'"+","					// 이름
				+"'"+Join_ID+"'"+","					// 아이디
				+"'"+Join_Password+"'"+","				// 패스워드
				+"'"+Join_Program+"'"+","				// 프로그램
				+"'"+Join_phoneNumber+"'"+","			// 연락처
				+"'"+Join_Address+"'"+")"+";";			// 주소 	
				
				try {
					stmt.executeUpdate(insertSql);
					SaveText("[Message] Sign-up: 이름/프로그램/아이피/패스워드/연락처/주소");
					SaveText("<"+message+"/"+Join_Program+"/"+Join_ID+"/"+Join_Password+"/"+Join_phoneNumber+"/"+Join_Address+">");
						} catch (SQLException e) {
					// TODO Auto-generated catch block
				} 
				
			}//회원가입 요청 프로토콜 끝

			else if(protocol.equals("Login")) {
				
				//아이디/비밀번호/사용중인 프로그램 => message
				Client_Program = message;				
				Client_ID = st.nextToken();
				Client_Password = st.nextToken();
				Client_IP=st.nextToken();
				SaveText("[Message] Login :"+Client_ID+"/"+Client_Password);
				
				String loginSql="select memberProgram, if(memberID='"+Client_ID+"'&&memberPassword='"+Client_Password+"','true','false') from membertbl where memberID='"+Client_ID+"';";
				String Login_Success="false"; //로그인 가능여부
				
				try {
					rs=stmt.executeQuery(loginSql);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}//해당 결과를 rs에 저장
				try {
					while(rs.next()) {
						Client_Program=rs.getString(1);
						Login_Success=rs.getString(2);
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}//해당결과를 하나씩 받아와 각각 저장 후 Client에 전달
				
				if(Login_Success.equals("false")) {
					send_Message("Login/Failed");
					SaveText("Login Failed :"+Client_Program+"/"+Client_ID+"/"+Client_Password);
				}
				else{
					if(ID_List.contains(Client_ID)) {
						send_Message("Login/Failed_Exist");
						SaveText("Login Failed :Already Exist -"+Client_ID);
					}
					else{System.out.println("Success");
					ID_List.add(Client_ID);
					send_Message("Login/Success/"+Client_Program);
					Total_Client_vc.add(this);
					if(Client_Program.equals("Tina")) {
						Tina_Client_vc.add(this);
						Tina_Count++;
						Tina_list.add(new TableRowDataModel(Tina_Count, Client_ID, Client_Password, Client_IP, " "));
						Platform.runLater(()->{
							Connect_Lbl.setText(Tina_Count+Net_Count+"");//연결된 클라이언트 갯수
							tina_List.setItems(Tina_list);
						});
					}
					
					else if(Client_Program.equals("Net")) {
						Net_Client_vc.add(this);
						Net_Count++;
						Net_list.add(new TableRowDataModel(Net_Count,Client_ID,Client_Password,Client_IP," "));
						Platform.runLater(()->{
							Connect_Lbl.setText(Tina_Count+Net_Count+"");//연결된 클라이언트 갯수
							net_List.setItems(Net_list);
						});
						
					}
					SaveText("Login Success :"+Client_Program+"/"+Client_ID+"/"+Client_Password);
					}
				}
			}//로그인 프로토콜 끝
			
			else if(protocol.equals("Connect")) {
				SaveText("[Message] Connect :"+message);
				System.out.println(message);
			}//Connect 프로토콜 끝 -> 서버와 클라이언트 connect
			
			else if(protocol.equals("Search")) {
				String Search_phoneNumber = st.nextToken();
				SaveText("[Message] Find ID/Password: name="+message+" phone="+Search_phoneNumber);	
				String searchSql="select memberID,memberPassword,memberProgram from membertbl where memberName='"+message+"' and memberPhone='"+Search_phoneNumber+"'";
				
				try {
					rs=stmt.executeQuery(searchSql);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}//해당 결과를 rs에 저장
				try {
					int exist=0;
					while(rs.next()) {
						exist++;
						String Search_ID=rs.getString(1);
						String Search_Password=rs.getString(2);
						String Search_Program=rs.getString(3);
						System.out.println("SQL : "+Search_ID+" "+Search_Password+" "+Search_Program);
						send_Message("Search"+"/"+Search_ID+"/"+Search_Password+"/"+Search_Program);
						SaveText("Search Success: "+Search_Program+"/"+Search_ID+"/"+Search_Password);
					}//해당 이름,전화번호에 맞는 아이디/비번 찾음
					
					if(exist==0) {
						send_Message("Search Failed"+"/"+message+"/"+Search_phoneNumber);
						SaveText("Search Failed: name="+message+" phone="+Search_phoneNumber);
					}//해당 이름,전화번호가 불일 치 
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					// e.printStackTrace();
				}//해당결과를 하나씩 받아와 각각 저장 후 Client에 전달	
			}// 아이디/비밀번호 찾기 프로토콜 끝

			else if(protocol.equals("Send_Msg")) {
				Date today=new Date();
				SimpleDateFormat date=new SimpleDateFormat("yyyy/MM/dd");
				SimpleDateFormat time=new SimpleDateFormat("kk:mm:ss");//날짜,시간 저장
				
				String SendMsg_Program=message;
				String SendMsg_ID=st.nextToken();
				String SendMsg_Msg=st.nextToken();
				String SendMsg_Time=date.format(today)+" "+time.format(today);
				String insertSql = "insert into msgtbl values"+"("
						+"'"+SendMsg_Program+"'"+","			// 프로그램
						+"'"+SendMsg_ID+"'"+","					// 아이디
						+"'"+SendMsg_Msg+"'"+","				// 메세지 내용
						+"'"+SendMsg_Time+"'"+")"+";";			// 시각
				try {
					stmt.executeUpdate(insertSql);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					// e.printStackTrace();
				}
				SaveText("[Mssage]ReceiveMsg: "+SendMsg_ID+"/"+SendMsg_Msg);
				
				System.out.println(ob_Receive_List.get(0).MsgProperty());
				
			}//클라이언트에서 받은 메세지 저장 프로토콜
			
			else if(protocol.equals("FileSend")) {
				// (protocol/program/id/FileName/ReadSize/FileSize)
				String ClientProgram=message;
				String ClientID=st.nextToken();
				String Client_FileName=st.nextToken();
				String Client_ReadSize=st.nextToken();
				String Client_FileSize=st.nextToken();
				Date today=new Date();
				SimpleDateFormat date=new SimpleDateFormat("yyyy/MM/dd");
				SimpleDateFormat time=new SimpleDateFormat("kk:mm:ss");
				try {
					;
					String filename = Client_ID+"_"+Client_FileName;
					FileOutputStream fos = new FileOutputStream("C:\\SWProject_Server\\"+filename,false);
					byte[] buffer = new byte[10000];
					double startTime = System.currentTimeMillis();
					int readBytes;
					readBytes=is.read(buffer);
					fos.write(buffer,0,readBytes);
					
					double endTime = System.currentTimeMillis();
					double diffTime = (endTime - startTime)/1000;
					System.out.println("time: "+diffTime+" seconds");
					SaveText("[Message]ReceiveFile: "+filename+"/"+diffTime+"seconds");
					ob_File_List.add(new TableRowDataModel_File(new SimpleStringProperty(ClientProgram), new SimpleStringProperty(ClientID),new SimpleStringProperty(Client_FileName),new SimpleStringProperty(Client_ReadSize+"/"+Client_FileSize+"Byte(s)"), new SimpleStringProperty(diffTime+"sec"),new SimpleStringProperty(date.format(today)+" "+time.format(today))));
					//파일 목록에 추가
					fos.close();//해당 파일스트림 닫기
				} catch (FileNotFoundException e) {
					System.out.println("FileSend Error");
				} catch (IOException e) {
					// e.printStackTrace();
				}

			}//파일 전송받기 프로토콜
			
			else if(protocol.equals("CheckID")) {
				String checkSql="select if(memberID='"+message+"','exist','non_exist') from membertbl where memberId='"+message+"';";
				SaveText("[Message]Check ID: "+message);
				try {
					rs=stmt.executeQuery(checkSql);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}//해당 결과를 rs에 저장
				try {
					while(rs.next()) {
						String Check=rs.getString(1);
						SaveText("ID_Check Success : "+Check);
						send_Message("CheckID"+"/"+Check);
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					// e.printStackTrace();
				}//해당결과를 하나씩 받아와 각각 저장 후 Client에 전달
						
			}//CheckID 프로토콜 끝 -> 중복아이디체크(Database)이용
			
			else if(protocol.equals("Terminate")) {
				//클라이언트에서 프로그램을 종료시킬떄
				//메세지 = "Terminate/프로그램/클라이언트ID"
				String Client_ID=st.nextToken();
				SaveText("[Message]Terminate: "+Client_ID);
				ID_List.remove(Client_ID);
				if(message.equals("Tina")) {
					ObservableList<TableRowDataModel> data=tina_List.getItems();
					ObservableList<TableRowDataModel> replace=FXCollections.observableArrayList();
					Tina_Count=0;
					Auto_Tina_Count=0;
					for(TableRowDataModel item:data) {
						if(!item.IDProperty().getValue().equals(Client_ID)) {
							Tina_Count++;	
							if(!item.SelectProperty().getValue().equals("   √")) {
								Auto_Tina_Count++;
							}
							replace.add(new TableRowDataModel(Tina_Count, item.IDProperty().getValue(), item.PassWordProperty().getValue(), item.IPProperty().getValue(), item.SelectProperty().getValue()));
						
						}
					}
					Tina_list.removeAll(Tina_list); //기존 List 전체 삭제후  ID제외된 나머지 출력
					Tina_list.addAll(replace);
					
					Platform.runLater(()->{
						Connect_Lbl.setText(Tina_Count+Net_Count+"");//연결된 클라이언트 갯수
						Auto_ConnectLbl.setText(Auto_Tina_Count+Auto_Net_Count+"");
						tina_List.setItems(Tina_list);
					});
				}
				else if(message.equals("Net")) {
					ObservableList<TableRowDataModel> data=net_List.getItems();
					ObservableList<TableRowDataModel> replace=FXCollections.observableArrayList();
					Net_Count=0;
					Auto_Net_Count=0;
					for(TableRowDataModel item:data) {
						if(!item.IDProperty().getValue().equals(Client_ID)) {
							Net_Count++;
							if(!item.SelectProperty().getValue().equals("   √")) {
								Auto_Net_Count++;
							}
							replace.add(new TableRowDataModel(Net_Count, item.IDProperty().getValue(), item.PassWordProperty().getValue(), item.IPProperty().getValue(), item.SelectProperty().getValue()));
						}
					}
					Net_list.removeAll(Net_list);//기존 List 전체 삭제후  ID제외된 나머지 출력
					Net_list.addAll(replace);
					
					Platform.runLater(()->{
						Connect_Lbl.setText(Tina_Count+Net_Count+""); //연결된 클라이언트 갯수
						Auto_ConnectLbl.setText(Auto_Tina_Count+Auto_Net_Count+"");
						net_List.setItems(Net_list);
					});
				}
			}//Terminate 프로토콜 끝 -> 클라이언트의 접속이 종료되었다는것을 알려줌(ID삭제)
			
		}//inmessage()함수 -> 받아온 메세지 해석함수(protocol/명령어/...)
	
		public void send_Message(String str) {
			try {
				dos.writeUTF(str);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}
		}//send_Message()함수 끝 ->각각의 아이디에게 메세지 보내기가능
		
		
	}// ClientInfo 클래스 끝
	
	public void BroadCast(String member, String str) { // 모든 클라이언트 들에게 프로토콜과 메세지를 전달 ei) 프로토콜 FileResend *파일재전송 
		if(member.equals("Everyone")){
			for(int i=0;i<Total_Client_vc.size();i++) {
				ClientInfo u = (ClientInfo)Total_Client_vc.elementAt(i);
				SaveText("(Send)Everyone :"+str);
				u.send_Message(str);
			}
		}//Tina, Net 전 멤버 broadcast
		else if(member.equals("All TinaMember")) {
			for(int i=0; i<Tina_Client_vc.size(); i++) {
				ClientInfo u=(ClientInfo)Tina_Client_vc.elementAt(i);
				SaveText("(Send)All TinaMember: "+str);
				u.send_Message(str);
			}
		}//Tina 전 멤버 broadcast
		else if(member.equals("All NetMember")) {
			for(int i=0; i<Net_Client_vc.size(); i++) {
				ClientInfo u=(ClientInfo)Net_Client_vc.elementAt(i);
				SaveText("(Send)All NetMember: "+str);
				u.send_Message(str);
			}
		}//Net 전 멤버 broadcast
	
	}//BroadCast()함수 끝 

	public void NoteSend(String msg) {
		if(msg!=null) {
			for(int i=0; i<Tina_Client_vc.size();i++) {
				ClientInfo u = (ClientInfo)Tina_Client_vc.elementAt(i);
				System.out.println("클라이언트ID: "+u.Client_ID);
				
				if(u.Client_ID == Client_NickName) {
					System.out.println("클라이언트를 찾았습니다");
					SaveText("(Send)"+Client_NickName+": "+msg);
					u.send_Message("Note/"+msg);
				}
			}
			for(int i=0;i<Net_Client_vc.size();i++) {
				ClientInfo u = (ClientInfo)Net_Client_vc.elementAt(i);
				System.out.println("클라이언트ID: "+u.Client_ID);
				String text="클라이언트ID: "+u.Client_ID+"        ";
				SaveText(text);
				if(u.Client_ID == Client_NickName) {
					System.out.println("클라이언트를 찾았습니다");
					SaveText("(Send)"+Client_NickName+": "+msg);
					u.send_Message("Note/"+msg);
				}
			}
		}
		
		else {
			JOptionPane.showMessageDialog(null,"메세지 내용이 없습니다","알림",JOptionPane.ERROR_MESSAGE);
			SaveText("Error: 메세지 내용이 없습니다");
		}
	}//NoteSend()함수 끝 -> 원하는 클라이언트에다가 메세지 보내기가능
	
	
	@FXML public void moveStop(ActionEvent event) {
		if(!startBtn.isDisable()) {
			JOptionPane.showMessageDialog(null,"서버의 연결이 필요합니다.","알림",JOptionPane.ERROR_MESSAGE);
			SaveText("Error : Don't click stopBtn. Server connection first");
			return ;
		}
		System.out.println("서버중지버튼클릭");
		BroadCast("Everyone","Everyone/Server_Terminate");
		runtime_stop=true;//시계 정지
		try {
			server_Socket.close();
			Tina_Client_vc.removeAllElements();
			Net_Client_vc.removeAllElements();
			Total_Client_vc.removeAllElements();
			tina_List.getItems().clear();
			net_List.getItems().clear();
			SaveText("Server Stop");
			
			startBtn.setDisable(false);
			stopBtn.setDisable(true);
			
			Tina_Count=0;
			Net_Count=0;
			Auto_Tina_Count=0;
			Auto_Net_Count=0;
			Platform.runLater(()->{
				Connect_Lbl.setText(Tina_Count+Net_Count+"");//연결된 클라이언트 갯수
				Auto_ConnectLbl.setText(Auto_Tina_Count+Auto_Net_Count+"");
			});
			//초기화
		}catch(IOException e) {
			
		}
	}//moveStop()함수 끝 -> 서버 정지 기능
	
	@FXML public void moveLog(ActionEvent event) {
		try {
			Stage primaryStage=(Stage)logBtn.getScene().getWindow();
			Stage dialog=new Stage(StageStyle.UTILITY);
			dialog.initOwner(primaryStage);
			dialog.setTitle("Log View");
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ServerLog.fxml"));
			Parent root = (Parent) loader.load();
			
			serverLog_Controller = loader.getController(); // 포트컨트롤러 load 
			serverLog_Controller.init(this);
			Stage stage = new Stage();
			stage.setTitle("Log View");
			stage.setScene(new Scene(root));
			stage.show();
			
		}catch(IOException e) {
			// e.printStackTrace();
		}
	}//moveLog()함수 끝 -> 서버로그 보여주는 기능 (테이블뷰를 이용해 GUI새로생성)
	
	@FXML public void moveSave(ActionEvent event) {
		JOptionPane.showMessageDialog(null,"Log를 저장하였습니다.","알림",JOptionPane.ERROR_MESSAGE);
		SaveText("Save Log in the file");
	}//moveSave()함수 끝 -> 서버로그 저장 기능

	@FXML public void moveRed(ActionEvent event) {
		if(!startBtn.isDisable()) {
			JOptionPane.showMessageDialog(null,"서버의 연결이 필요합니다.","알림",JOptionPane.ERROR_MESSAGE);
			SaveText("Error : Don't click button. Server connection first");
			return ;
		}
		/*try {
			Stage primaryStage=(Stage)Manage_RedBtn.getScene().getWindow();
			Stage dialog=new Stage(StageStyle.UTILITY);
			dialog.initOwner(primaryStage);
			dialog.setTitle("메니지먼트 관리 시스템-경고");
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Manage.fxml"));
			Parent root = (Parent) loader.load();
			
			manageController = loader.getController(); // 포트컨트롤러 load 
			manageController.ColorDot.setFill(Color.RED);
			Stage stage = new Stage();
			stage.setScene(new Scene(root));
			stage.show();
			
		}catch(IOException e) {
			e.printStackTrace();
		}*/
	}//moveRed()함수 끝 -> 알람 함수
	@FXML public void moveOrange(ActionEvent event) {
		if(!startBtn.isDisable()) {
			JOptionPane.showMessageDialog(null,"서버의 연결이 필요합니다.","알림",JOptionPane.ERROR_MESSAGE);
			SaveText("Error : Don't click button. Server connection first");
			return ;
		}
		/*try {
			Stage primaryStage=(Stage)Manage_RedBtn.getScene().getWindow();
			Stage dialog=new Stage(StageStyle.UTILITY);
			dialog.initOwner(primaryStage);
			dialog.setTitle("메니지먼트 관리 시스템-오렌지");
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Manage.fxml"));
			Parent root = (Parent) loader.load();
			
			manageController = loader.getController(); // 포트컨트롤러 load 
			manageController.ColorDot.setFill(Color.ORANGE);
			Stage stage = new Stage();
			stage.setScene(new Scene(root));
			stage.show();
			
		}catch(IOException e) {
			e.printStackTrace();
		}*/
	}//moveOrange()함수 끝 -> 알람함수
	@FXML public void moveYellow(ActionEvent event) {
		if(!startBtn.isDisable()) {
			JOptionPane.showMessageDialog(null,"서버의 연결이 필요합니다.","알림",JOptionPane.ERROR_MESSAGE);
			SaveText("Error : Don't click button. Server connection first");
			return ;
		}
	/*	try {
			Stage primaryStage=(Stage)Manage_RedBtn.getScene().getWindow();
			Stage dialog=new Stage(StageStyle.UTILITY);
			dialog.initOwner(primaryStage);
			dialog.setTitle("메니지먼트 관리 시스템-경고");
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Manage.fxml"));
			Parent root = (Parent) loader.load();
			
			manageController = loader.getController(); // 포트컨트롤러 load 
			manageController.ColorDot.setFill(Color.YELLOW);
			Stage stage = new Stage();
			stage.setScene(new Scene(root));
			stage.show();
			
		}catch(IOException e) {
			e.printStackTrace();
		}*/
	}//moveYellow()함수 끝 -> 알람함수
	@FXML public void moveGreen(ActionEvent event) {
		if(!startBtn.isDisable()) {
			JOptionPane.showMessageDialog(null,"서버의 연결이 필요합니다.","알림",JOptionPane.ERROR_MESSAGE);
			SaveText("Error : Don't click button. Server connection first");
			return ;
		}
		
	}//moveGreen()함수 끝 -> 알람함수
	
	
	public void runtime() {
		long time1=System.currentTimeMillis()/1000;
		runtime_stop=false;
		Thread thread=new Thread() {
			public void run() {
				while(!runtime_stop) {	
					Platform.runLater(()->{
						long time2=System.currentTimeMillis()/1000;
						long hour,min,sec;
						hour=(time2-time1)/3600;
						min=(time2-time1)%3600/60;
						sec=(time2-time1)%3600%60;
						String timer_hour=String.format("%02d",hour);
						String timer_min=String.format("%02d", min);
						String timer_sec=String.format("%02d", sec);
						ServerTime_lbl.setText(timer_hour+":"+timer_min+":"+timer_sec);
						
					});
					try {Thread.sleep(100);} 
					catch(InterruptedException e){
				}
			}
			}
		};//thread 구현
		thread.setDaemon(true);//백그라운드에서 실행
		thread.start();
	}//runtime()함수 -> 서버 연결시간 측정기능

	public void SaveText(String message) {
		Date today=new Date();
		SimpleDateFormat date=new SimpleDateFormat("yyyy/MM/dd"); //현재 날짜,시간 구하기
		SimpleDateFormat time=new SimpleDateFormat("kk:mm:ss");
		ob_Log_List.add(new TableRowDataModel_Log(new SimpleIntegerProperty(count+1),new SimpleStringProperty(date.format(today)+" "+time.format(today)),new SimpleStringProperty(message)));
		count++;
	}//SaveText()함수 끝 -> 로그를 tableview에 저장

	
	private void window_control() {
		// TODO Auto-generated method stub
		Stage stage=(Stage) ap.getScene().getWindow();
		stage.setOnHiding(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent arg0) {
				// TODO Auto-generated method stub
				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							if(!stopBtn.isDisable()) JOptionPane.showMessageDialog(null,"서버를 강제적으로 종료합니다.","알림",JOptionPane.ERROR_MESSAGE);
							BroadCast("Everyone", "Everyone/Quit");
							SaveText("Terminate Server");
							server_Socket.close();
							Tina_Client_vc.removeAllElements();
							Net_Client_vc.removeAllElements();
							Total_Client_vc.removeAllElements();
							tina_List.getItems().clear();
							net_List.getItems().clear();
							Platform.exit();
							System.exit(1);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							// e.printStackTrace();
						}
					
					}
					
				});
			}
			
		});
	}//window 창 닫았을 시 모든 연결,소켓 해제
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
}