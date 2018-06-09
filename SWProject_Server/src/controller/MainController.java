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
	@FXML Button sendMsg;						//�޼��� �������ư
	@FXML Button receiveMsg;					//�����޼��� �����ư
	@FXML Button command;						//��ɾ�� ��ư(Help)
	@FXML Button receiveFile;					//�������� ���� ��ư
	@FXML Button option;						//ȯ�漳��(IP/Port����)	��ư
	@FXML Button manageBtn;						
	@FXML Button startBtn;						//�������۹�ư(Server execute)
	@FXML Button stopBtn;						//����������ư(Server stop)
	@FXML Button logBtn;						//�α׺����ư(Server view Log)
	@FXML Button saveBtn;						//�����α������ư(Server log save)
	@FXML Button Manage_RedBtn;					
	@FXML Button Manage_OrangeBtn;
	@FXML Button Manage_YellowBtn;
	@FXML Button Manage_GreenBtn;
	@FXML Button Tina_ReceiveFile_Btn;			//Tina ���Ϲޱ��ư(GetFile)
	@FXML Button Net_ReceiveFile_Btn;			//Net ���Ϲޱ��ư(GetFile)
	@FXML Button Tina_AutoFile_Btn;				//Tina �ڵ��������ư
	@FXML Button Net_AutoFile_Btn;				//Net �ڵ������� ��ư
	@FXML Button Tina_NotAuto_Btn;				//Tina �ڵ���� ��ư
	@FXML Button Net_NotAuto_Btn;				//Net �ڵ���� ��ư
	@FXML Button DB_Btn;						//�����ͺ��̽� ���� ��ư
	@FXML Button Update_Btn;					//������Ʈ ���� ��ư
	
	@FXML public Label ServerTime_lbl;			//Server ���ӽð� ����
	@FXML public Label IP_lbl;					//Server IP ����
	@FXML public Label Port_lbl;				//Server Port ����

	@FXML CheckBox Tina_TotalBox;				//Tina ��ü üũ�ڽ�
	@FXML CheckBox Net_TotalBox;				//Net ��ü üũ �ڽ�
	
	//************************Tina ���� Ŭ���̾�Ʈ ���̺� �� ****************************
	@FXML public TableView<TableRowDataModel> tina_List;			
	@FXML private TableColumn<TableRowDataModel,Integer> Tina_indexColumn;
	@FXML private TableColumn<TableRowDataModel,String> Tina_IDColumn;
	@FXML private TableColumn<TableRowDataModel,String> Tina_PassWordColumn;
	@FXML private TableColumn<TableRowDataModel,String> Tina_IPColumn;
	@FXML private TableColumn<TableRowDataModel,String> Tina_CheckColumn;
	//**************************************************************************
	
	//************************Net ���� Ŭ���̾�Ʈ ���̺� �� *****************************
	@FXML public TableView<TableRowDataModel> net_List;
	@FXML private TableColumn<TableRowDataModel,Integer> Net_indexColumn;
	@FXML private TableColumn<TableRowDataModel,String> Net_IDColumn;
	@FXML private TableColumn<TableRowDataModel,String> Net_PassWordColumn;
	@FXML private TableColumn<TableRowDataModel,String> Net_IPColumn;
	@FXML private TableColumn<TableRowDataModel,String> Net_CheckColumn;
	//**************************************************************************
	//***************************Database ����***********************************
	public String driver = "com.mysql.jdbc.Driver";			// ����̹�
	public String DB_user="";								// �����̸�
	public String DB_password="";							// �н�����
	public String dburl = "jdbc:mysql://127.0.0.1/userdb?useSSL=false&serverTimezone=UTC&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false";
	public boolean DBSave_Check=false;
	public Statement stmt = null; 							// Statement ��ü ����
	public ResultSet rs = null;
	//"jdbc:mysql://localhost:3306/����ҵ����ͺ��̽���?user=root&password=�н�����"
	//"jdbc:mysql://127.0.0.1/����ҵ����ͺ��̽���?user=root&password=�н�����" 
	public Connection conn = null;
	//**************************************************************************
	 
	//********************************�ð�***************************************
	boolean clock_stop=false;								//���� �ð�, ����ð� ����
	boolean runtime_stop=false;
	//**************************************************************************
	
	//******************************network ����*********************************
	public String IP="";									// ����������
	public int PORT=-1;										// ������Ʈ
	public ServerSocket server_Socket;						// ��������
	public Socket socket;									// Ŭ���̾�Ʈ����
	public boolean UseDatabase=false;						// database Ȱ�� ����
	//**************************************************************************
	
	//*****************************Ŭ���̾�Ʈ ���� ����********************************
	public Vector Tina_Client_vc = new Vector();			// Tina Ŭ���̾�Ʈ�� ��� ������ �����ϰ� �ִ� ����
	public Vector Net_Client_vc = new Vector();				// Net Ŭ���̾�Ʈ�� ��� ������ �����ϰ� �ִ� ����
	public Vector Total_Client_vc = new Vector();			// ��ü Ŭ���̾�Ʈ ����
	public StringTokenizer st; 								// ���������� �����ϱ����� ����  ei) NoteSend/"��ɾ�"
	//**************************************************************************
	
	public boolean auto=false;
	
	//***************************** ���õ� ID ���� *********************************
	String Find_Tina_Client="";
	String Find_Net_Client="";
	public String Client_NickName;							// ���̺��� ���õ� Ŭ���̾�Ʈ �̸�
	//**************************************************************************
	
	//**************************����� Ŭ���̾�Ʈ ���� **********************************
	int Tina_Count=0;										//Tina ���᰹��	
	int Net_Count=0;										//Net ���᰹��
	ArrayList<String> ID_List=new ArrayList<String>();		//ID���翩�� ���� ������� ����Ʈ
	@FXML public Label Connect_Lbl; 						//����� Ŭ���̾�Ʈ ���� ��
	//**************************************************************************
	
	//*************************�ڵ������� Ŭ���̾�Ʈ ����********************************
	int Auto_Tina_Count=0;									// Tina �ڵ����� Ŭ���̾�Ʈ ���� ����
	int Auto_Net_Count=0;									// Net �ڵ����� Ŭ���̾�Ʈ ���� ����
	@FXML public Label Auto_ConnectLbl;						// �ڵ����� ��
	String Period_number="";								// ��ŭ ���� ���� ������ �Ǵ��ϴ� �ֱ� ����
	String Period_text="";									// ������ �ֱ� ei) Day/Hour/Min/Sec
	//**************************************************************************
	
	//***************************** Observable_List ************************************
	public ObservableList<TableRowDataModel_File> ob_File_List=FXCollections.observableArrayList();		 //���� ���� ����Ʈ
	ObservableList<TableRowDataModel_Msg> ob_Receive_List= FXCollections.observableArrayList(); 	 //�����޼��� ����Ʈ
	ObservableList<TableRowDataModel_Log> ob_Log_List=FXCollections.observableArrayList();			 //�α� ���ϸ���Ʈ
	ObservableList<TableRowDataModel> Tina_list= FXCollections.observableArrayList(); 				 //main Tina ����Ʈ
	ObservableList<TableRowDataModel> Net_list= FXCollections.observableArrayList(); 				 //main net ����Ʈ
	//**********************************************************************************
		
	int count=0;
	//**********************************��Ʈ�ѷ� ***********************************
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
			
			setDB_Controller = loader.getController(); // ��Ʈ��Ʈ�ѷ� load
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
	}//moveDB()�Լ� �� -> DB������ư �����Լ�
	
	@FXML public void move_Tina_ReceiveFile(ActionEvent event) {
		if(!startBtn.isDisable()) {
			JOptionPane.showMessageDialog(null,"������ ������ �ʿ��մϴ�.","�˸�",JOptionPane.ERROR_MESSAGE);
			SaveText("Error : Don't click getfile. Server connection first");
			return ;
		}
		table_select();
		if(Tina_TotalBox.isSelected() && Net_TotalBox.isSelected()) {
			Client_NickName="Everyone";
			BroadCast("Everyone",Client_NickName+"/"+"Request_FileSend/"+"������ �������ּ���");
			System.out.println("everyone���� ���Ϲޱ� ��ư�Դϴ�");
		}//��� ��������(broadcast)
		else if(Tina_TotalBox.isSelected() && !Net_TotalBox.isSelected()) {
			Client_NickName="All_TinaMember";
			BroadCast("All_TinaMember",Client_NickName+"/"+"Request_FileSend/"+"������ �������ּ���");
			System.out.println("Tina ��ü���� ���Ϲޱ� ��ư�Դϴ�");
		}//Tina ��ü ��������(broadcast-TinaMember)
		else if(!Tina_TotalBox.isSelected() && Net_TotalBox.isSelected()) {
			JOptionPane.showMessageDialog(null,"Net ��ü�ڽ� ���� �� Tina ���̵� �������ּ��� ","�˸�",JOptionPane.ERROR_MESSAGE);
			return;
		}
		else {
			if(Find_Tina_Client.equals("")) {
				JOptionPane.showMessageDialog(null,"Tina ���̵� �����ϼ���","�˸�",JOptionPane.ERROR_MESSAGE);
				return ;
			}
			Client_NickName=Find_Tina_Client;
			for(int i=0;i<Total_Client_vc.size();i++) {
			ClientInfo u = (ClientInfo)Total_Client_vc.elementAt(i);
				if(Client_NickName.equals(u.Client_ID)) {
				u.send_Message("Request_FileSend/"+"������ �������ּ���");
				System.out.println(Client_NickName+"���Ϲޱ� ��ư�Դϴ�.");
				}
			}
		}//Tina ���̵� ��������
		
	}//move_Tina_ReceiveFile()�Լ� �� -> Tina�������ۼ���
	
	@FXML public void move_Net_ReceiveFile(ActionEvent event) {
		if(!startBtn.isDisable()) {
			JOptionPane.showMessageDialog(null,"������ ������ �ʿ��մϴ�.","�˸�",JOptionPane.ERROR_MESSAGE);
			SaveText("Error : Don't click getfile. Server connection first");
			return ;
		}
		table_select();
		if(Tina_TotalBox.isSelected() && Net_TotalBox.isSelected()) {
			Client_NickName="Everyone";
			BroadCast("Everyone",Client_NickName+"/"+"Request_FileSend/"+"������ �������ּ���");
			System.out.println("everyone���� ���Ϲޱ� ��ư�Դϴ�");
		}//��� ���Ϲޱ�(broadcast)
		else if(!Tina_TotalBox.isSelected() && Net_TotalBox.isSelected()) {
			Client_NickName="All_NetMember";
			BroadCast("All_NetMember",Client_NickName+"/"+"Request_FileSend/"+"������ �������ּ���");
			System.out.println("Net ��ü���� ���Ϲޱ� ��ư�Դϴ�");
		}//Net ��ü��� ���Ϲޱ�(broadcast-NetMember)
		else if(Tina_TotalBox.isSelected() && !Net_TotalBox.isSelected()) {
			JOptionPane.showMessageDialog(null,"Tina ��ü�ڽ� ���� �� Net ���̵� �������ּ��� ","�˸�",JOptionPane.ERROR_MESSAGE);
			return;
		}
		else {
			if(Find_Net_Client.equals("")) {
				JOptionPane.showMessageDialog(null,"Net ���̵� �����ϼ���","�˸�",JOptionPane.ERROR_MESSAGE);
				return ;
			}
			Client_NickName=Find_Net_Client;
			for(int i=0;i<Total_Client_vc.size();i++) {
			ClientInfo u = (ClientInfo)Total_Client_vc.elementAt(i);
				if(Client_NickName.equals(u.Client_ID)) {
				u.send_Message("Request_FileSend/"+"������ �������ּ���");
				System.out.println(Client_NickName+"���Ϲޱ� ��ư�Դϴ�.");
				}
			}
		}//Net ���̵� ���Ϲޱ�
		
	}//move_Net_ReceiveFile()�Լ� �� -> Net ���Ϲޱ�
	
	@FXML public void move_Tina_AutoFile(ActionEvent event) {
		if(!startBtn.isDisable()) {
			JOptionPane.showMessageDialog(null,"������ ������ �ʿ��մϴ�.","�˸�",JOptionPane.ERROR_MESSAGE);
			SaveText("Error : Don't click auto getfile. Server connection first");
			return ;
		}
			table_select();
		 if(!Tina_TotalBox.isSelected() && Net_TotalBox.isSelected()) {
				JOptionPane.showMessageDialog(null,"Net ��ü�ڽ� ���� �� Tina ���̵� �������ּ��� ","�˸�",JOptionPane.ERROR_MESSAGE);
				return;
		 }
		 else if(!Tina_TotalBox.isSelected() && Find_Tina_Client.equals("")) {
				JOptionPane.showMessageDialog(null,"Tina ���̵� �����ϼ���","�˸�",JOptionPane.ERROR_MESSAGE);
				return ;
		 }
		 try {
			Stage primaryStage=(Stage)Tina_AutoFile_Btn.getScene().getWindow();
			Stage dialog=new Stage(StageStyle.UTILITY);
			dialog.initOwner(primaryStage);
			dialog.setTitle("Set Period");
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Period.fxml"));
			Parent root = (Parent) loader.load();
			
			period_Controller = loader.getController(); // ��Ʈ��Ʈ�ѷ� load 
			period_Controller.init(this);
			Stage stage = new Stage();
			stage.setTitle("Set Period");
			stage.setScene(new Scene(root));
			stage.show();
			
		}catch(IOException e) {
			//e.printStackTrace();
		}
		
	}//move_Tina_AutoFile() �Լ� �� -> �ֱ� GUI�� �Ѿ

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
			
			//�ڵ����� Ŭ���̾�Ʈ ����
			Auto_Tina_Count=Tina_list.size();
			Auto_Net_Count=Net_list.size();
			int num=Auto_Tina_Count+Auto_Net_Count;
			Auto_ConnectLbl.setText(""+num);
			Tina_AutoFile_Btn.setDisable(true);
			Net_AutoFile_Btn.setDisable(true);
			Tina_NotAuto_Btn.setDisable(false);
			Net_NotAuto_Btn.setDisable(false);
		}//��ü �ڵ�����(BroadCast-Everymember)
		else if(Tina_TotalBox.isSelected() && !Net_TotalBox.isSelected()) {
			for(int i=0; i<Tina_list.size(); i++) {
				Tina_list.get(i).setCheck("true");
			}
			tina_List.setItems(Tina_list);
			tina_List.refresh();
			
			BroadCast("All TinaMember","All TinaMember/FileSend_Auto/"+Period_number+"/"+Period_text);
			SaveText("Set AutoFile : All TinaMember set auto file");
			
			//�ڵ����� Ŭ���̾�Ʈ ����
			Auto_Tina_Count=Tina_list.size();
			int num=Auto_Tina_Count+Auto_Net_Count;
			Auto_ConnectLbl.setText(""+num);
			Tina_AutoFile_Btn.setDisable(true);
			Tina_NotAuto_Btn.setDisable(false);
			
		}//Tina ��ü �ڵ�����(BroadCast-All TinaMember)
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
			//���̵� ���� �� üũ �ϱ�
			
			for(int i=0;i<Total_Client_vc.size();i++) {
			ClientInfo u = (ClientInfo)Total_Client_vc.elementAt(i);
				if(Client_NickName.equals(u.Client_ID)) {
				u.send_Message("FileSend_Auto/"+Period_number+"/"+Period_text);
				System.out.println(Client_NickName+"�����ڵ��ޱ� ��ư�Դϴ�.");
				}
			}
			SaveText("Set AutoFile :"+Client_NickName+" set auto file");
			
			//�ڵ����� Ŭ���̾�Ʈ ����
			Auto_Tina_Count=1;
			int num=Auto_Tina_Count+Auto_Net_Count;
			Auto_ConnectLbl.setText(""+num);
			Tina_AutoFile_Btn.setDisable(true);
			Tina_NotAuto_Btn.setDisable(false);
		}//Tina ���̵� �ڵ���������
		Tina_ReceiveFile_Btn.setDisable(true);
	}//on_TinaAuto()�Լ� �� -> Tina �ڵ����� ����
	
	@FXML public void move_Net_AutoFile(ActionEvent event) {
			if(!startBtn.isDisable()) {
			JOptionPane.showMessageDialog(null,"������ ������ �ʿ��մϴ�.","�˸�",JOptionPane.ERROR_MESSAGE);
			SaveText("Error : Don't click auto getfile. Server connection first");
			return ;
			}
			table_select();
			if(Tina_TotalBox.isSelected() && !Net_TotalBox.isSelected()) {
				JOptionPane.showMessageDialog(null,"Tina ��ü�ڽ� ���� �� Net ���̵� �������ּ��� ","�˸�",JOptionPane.ERROR_MESSAGE);
				return;
			}
			else if(!Net_TotalBox.isSelected() && Find_Net_Client.equals("")) {
				JOptionPane.showMessageDialog(null,"Net ���̵� �����ϼ���","�˸�",JOptionPane.ERROR_MESSAGE);
				return ;
			}
			try {
				Stage primaryStage=(Stage)Tina_AutoFile_Btn.getScene().getWindow();
				Stage dialog=new Stage(StageStyle.UTILITY);
				dialog.initOwner(primaryStage);
				dialog.setTitle("Set Period");
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Period.fxml"));
				Parent root = (Parent) loader.load();
				
				period_Controller = loader.getController(); // ��Ʈ��Ʈ�ѷ� load 
				period_Controller.init(this);
				Stage stage = new Stage();
				stage.setTitle("Set Period");
				stage.setScene(new Scene(root));
				stage.show();
				
			}catch(IOException e) {
				//e.printStackTrace();
			}
			
	}//move_Net_Autofile()�Լ� �� -> �ֱ�� �Ѿ
	
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
			
			//�ڵ����� Ŭ���̾�Ʈ ����
			Auto_Tina_Count=Tina_list.size();
			Auto_Net_Count=Net_list.size();
			int num=Auto_Tina_Count+Auto_Net_Count;
			Auto_ConnectLbl.setText(""+num);
			Tina_AutoFile_Btn.setDisable(true);
			Net_AutoFile_Btn.setDisable(true);
			Tina_NotAuto_Btn.setDisable(false);
			Net_NotAuto_Btn.setDisable(false);
		}//��ü �ڵ����� (BroadCast->Everyone)
		else if(!Tina_TotalBox.isSelected() && Net_TotalBox.isSelected()) {
			for(int i=0; i<Net_list.size(); i++) {
				Net_list.get(i).setCheck("true");
			}
			net_List.setItems(Net_list);
			net_List.refresh();
			
			BroadCast("All NetMember","All NetMember/FileSend_Auto/"+Period_number+"/"+Period_text);
			SaveText("Set AutoFile : All NetMember set auto file");
			System.out.println("!");
			//�ڵ����� Ŭ���̾�Ʈ ����
			Auto_Net_Count=Net_list.size();
			int num=Auto_Tina_Count+Auto_Net_Count;
			Auto_ConnectLbl.setText(""+num);
			Net_AutoFile_Btn.setDisable(true);
			Net_NotAuto_Btn.setDisable(false);
		}//Net ��ü �ڵ�����(BroadCast->All NetMember)
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
					System.out.println(Client_NickName+"�����ڵ��ޱ� ��ư�Դϴ�.");
					}
				}
			SaveText("Set AutoFile :"+Client_NickName+" set auto file");
			
			Auto_Net_Count=1;
			int num=Auto_Tina_Count+Auto_Net_Count;
			Auto_ConnectLbl.setText(""+num);
			Net_AutoFile_Btn.setDisable(true);
			Net_NotAuto_Btn.setDisable(false);
		}//Net ���̵� �ڵ�����
		Net_ReceiveFile_Btn.setDisable(true);
	}//on_NetAuto()�Լ� �� -> Net �ڵ����ۼ���
	
	@FXML public void move_Tina_NotAutoFile(ActionEvent event) {
		if(!startBtn.isDisable()) {
			JOptionPane.showMessageDialog(null,"������ ������ �ʿ��մϴ�.","�˸�",JOptionPane.ERROR_MESSAGE);
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
			
			//�ڵ����� Ŭ���̾�Ʈ ����
			Auto_Tina_Count=0;
			Auto_Net_Count=0;
			int num=Auto_Tina_Count+Auto_Net_Count;
			Auto_ConnectLbl.setText(""+num);
			Tina_AutoFile_Btn.setDisable(false);
			Net_AutoFile_Btn.setDisable(false);
			Tina_NotAuto_Btn.setDisable(true);
			Net_NotAuto_Btn.setDisable(true);
		}//��ü �ڵ����� ���(BroadCast->Everyone)
		else if(Tina_TotalBox.isSelected() && !Net_TotalBox.isSelected()) {
			for(int i=0; i<Tina_list.size(); i++) {
				Tina_list.get(i).setCheck("false");
			}
			tina_List.setItems(Tina_list);
			tina_List.refresh();
			BroadCast("All TinaMember", "All TinaMember/FileSend_NotAuto");
			SaveText("Remove set AutoFile : All TinaMember remove set auto file");
			//�ڵ����� Ŭ���̾�Ʈ ����
			Auto_Tina_Count=0;
			int num=Auto_Tina_Count+Auto_Net_Count;
			Auto_ConnectLbl.setText(""+num);
			Tina_AutoFile_Btn.setDisable(false);
			Tina_NotAuto_Btn.setDisable(true);
		}//Tina �ڵ��������(BroadCast->All TinaMember)
		else if(!Tina_TotalBox.isSelected() && Net_TotalBox.isSelected()) {
			JOptionPane.showMessageDialog(null,"Net ��ü�ڽ� ���� �� Tina ���̵� �������ּ��� ","�˸�",JOptionPane.ERROR_MESSAGE);
		}
		else {
			if(Find_Tina_Client.equals("")) {
				JOptionPane.showMessageDialog(null,"Tina ���̵� �����ϼ���","�˸�",JOptionPane.ERROR_MESSAGE);
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
					u.send_Message("FileSend_NotAuto/"+"�ڵ������� ������ּ���");
					System.out.println(Client_NickName+"�����ڵ��ޱ� ��ư�Դϴ�.");
					}
				}
			SaveText("Remove set AutoFile :"+Client_NickName+"remove set auto file");
			
			Auto_Tina_Count=0;
			int num=Auto_Tina_Count+Auto_Net_Count;
			Auto_ConnectLbl.setText(""+num);
			Tina_AutoFile_Btn.setDisable(false);
			Tina_NotAuto_Btn.setDisable(true);
		}//Tina ���̵� �ڵ��������
		Tina_ReceiveFile_Btn.setDisable(false);
		
	}//move_Tina_NotAutoFile()�Լ� �� -> Tina �ڵ����Ϲޱ� ���
	
	@FXML public void move_Net_NotAutoFile(ActionEvent event) {
		if(!startBtn.isDisable()) {
			JOptionPane.showMessageDialog(null,"������ ������ �ʿ��մϴ�.","�˸�",JOptionPane.ERROR_MESSAGE);
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
			//�ڵ����� Ŭ���̾�Ʈ ����
			Auto_Tina_Count=0;
			Auto_Net_Count=0;
			int num=Auto_Tina_Count+Auto_Net_Count;
			Auto_ConnectLbl.setText(""+num);
			Tina_AutoFile_Btn.setDisable(false);
			Net_AutoFile_Btn.setDisable(false);
			Tina_NotAuto_Btn.setDisable(true);
			Net_NotAuto_Btn.setDisable(true);
		}//��ü �ڵ����� ���(BroadCast->Everyone)
		else if(!Tina_TotalBox.isSelected() && Net_TotalBox.isSelected()) {
			for(int i=0; i<Net_list.size(); i++) {
				Net_list.get(i).setCheck("false");
			}
			net_List.setItems(Net_list);
			net_List.refresh();
			BroadCast("All NetMember", "All NetMember/FileSend_NotAuto");
			SaveText("Remove set AutoFile :All NetMember remove set auto file");
			//�ڵ����� Ŭ���̾�Ʈ ����
			Auto_Net_Count=0;
			int num=Auto_Tina_Count+Auto_Net_Count;
			Auto_ConnectLbl.setText(""+num);
			Net_AutoFile_Btn.setDisable(false);
			Net_NotAuto_Btn.setDisable(true);
		}//Net ��ü �ڵ����� ���(BroadCast->All NetMember)
		else if(Tina_TotalBox.isSelected() && !Net_TotalBox.isSelected()) {
			JOptionPane.showMessageDialog(null,"Tina ��ü�ڽ� ���� �� Net ���̵� �������ּ��� ","�˸�",JOptionPane.ERROR_MESSAGE);
		}
		else {
			if(Find_Net_Client.equals("")) {
				JOptionPane.showMessageDialog(null,"Net ���̵� �����ϼ���","�˸�",JOptionPane.ERROR_MESSAGE);
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
					u.send_Message("FileSend_NotAuto/"+"�ڵ������� ������ּ���");
					System.out.println(Client_NickName+"�����ڵ��ޱ� ��ư�Դϴ�.");
					}
				}
			SaveText("Remove set AutoFile :"+Client_NickName+" remove set auto file");
		
			
			//�ڵ����� Ŭ���̾�Ʈ ����
			Auto_Net_Count=0;
			int num=Auto_Tina_Count+Auto_Net_Count;
			Auto_ConnectLbl.setText(""+num);
			Net_AutoFile_Btn.setDisable(false);
			Net_NotAuto_Btn.setDisable(true);
			
		}//Net ���̵� �ڵ��������
		Net_ReceiveFile_Btn.setDisable(false);
	}//move_Net_NotAutofile()�Լ� �� -> Net �ڵ����Ϲޱ����
	
	
	@FXML public void moveOption(ActionEvent event) {
	
		try {
			if(startBtn.isDisable()) {
				JOptionPane.showMessageDialog(null,"���� ������ �������Դϴ�","�˸�",JOptionPane.ERROR_MESSAGE);
				SaveText("Error: Don't click option. Server is running");
				return ;
			}
			Stage primaryStage=(Stage)option.getScene().getWindow();
			Stage dialog=new Stage(StageStyle.UTILITY);
			dialog.initOwner(primaryStage);
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/option/Option1_Port.fxml"));
			Parent root = (Parent) loader.load();
			
			option1_PortController = loader.getController(); // ��Ʈ��Ʈ�ѷ� load
			option1_PortController.init(this);
			Stage stage = new Stage();
			stage.setTitle("Option");
			stage.setScene(new Scene(root));
			stage.show();
			
		}catch(IOException e) {
			//e.printStackTrace();
		}
	}//moveOption()�Լ� �� -> ������/��Ʈ��ȣ ����
	
	@FXML public void moveSend(ActionEvent event) {
		if(!startBtn.isDisable()) {
			JOptionPane.showMessageDialog(null,"������ ������ �ʿ��մϴ�.","�˸�",JOptionPane.ERROR_MESSAGE);
			SaveText("Error: Don't click sendmessage. Server connection first");
			return ;
		}
		table_select();
		if(Tina_TotalBox.isSelected() && Net_TotalBox.isSelected()) {
			Client_NickName="Everyone";
		}
		if(Tina_TotalBox.isSelected() && !Net_TotalBox.isSelected()) {
			Client_NickName="All TinaMember";
		}//Ƽ�� ��ü ���
		if(!Tina_TotalBox.isSelected() && Net_TotalBox.isSelected()) {
			Client_NickName="All NetMember";
		}//��ü ������ ��� broadcast
		if(!Tina_TotalBox.isSelected() && !Net_TotalBox.isSelected()) {
			if(Find_Net_Client.equals("")&&Find_Tina_Client.equals("")) {
				JOptionPane.showMessageDialog(null,"���̵� �����ϼ���","�˸�",JOptionPane.ERROR_MESSAGE);
				return ;
			}
			else if(Find_Net_Client.equals("")) {Client_NickName=Find_Tina_Client;}
			else {Client_NickName=Find_Net_Client;}
		}
	 		
		try {
			Stage primaryStage=(Stage)sendMsg.getScene().getWindow();
			Stage dialog=new Stage(StageStyle.UTILITY);
			dialog.initOwner(primaryStage);
			dialog.setTitle("�޼��� ������");
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SendMsg.fxml"));
			Parent root = (Parent) loader.load();
			
			sendMsg_Controller = loader.getController(); // ��Ʈ��Ʈ�ѷ� load 
			sendMsg_Controller.Init(this);
			
			Stage stage = new Stage();
			stage.setTitle("Sending message");
			stage.setScene(new Scene(root));			
			stage.show();
			
		}catch(IOException e) {
		
		}
	}//moveSend()�Լ� �� -> Ŭ���̾�Ʈ�� �޼��������� �Լ�
	
	@FXML public void moveReceive(ActionEvent event) {
		if(!startBtn.isDisable()) {
			JOptionPane.showMessageDialog(null,"������ ������ �ʿ��մϴ�.","�˸�",JOptionPane.ERROR_MESSAGE);
			SaveText("Error : Don't click receivemsg. Server connection first");
			return ;
		}
		try {
			Stage primaryStage=(Stage)receiveMsg.getScene().getWindow();
			Stage dialog=new Stage(StageStyle.UTILITY);
			dialog.initOwner(primaryStage);
			dialog.setTitle("���� �޼���");
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ReceiveMsg.fxml"));
			Parent root = (Parent) loader.load();
			
			receiveMsg_Controller = loader.getController(); // ��Ʈ��Ʈ�ѷ� load 
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
	
	}//moveReceive()�Լ� �� -> �����޼��� �����ִ� �Լ�
	
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
				JOptionPane.showMessageDialog(null,"���̵� �����ϼ���","�˸�",JOptionPane.ERROR_MESSAGE);
				return false;
			}//�ƹ��͵� ���õ��� �ʾ��� �� ����ó��
			else if(Find_Net_Client.equals("")) {Client_NickName=Find_Tina_Client;} 
			else Client_NickName=Find_Net_Client;//ID ����
			

			msg_searchSql="select * from msgtbl where msgID='"+Client_NickName+"'";
		}
		
		try {
			rs=stmt.executeQuery(msg_searchSql);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			//e1.printStackTrace();
		}//�ش� ����� rs�� ����
		try {
			while(rs.next()) {
				String Search_Program=rs.getString(1);
				String Search_ID=rs.getString(2);
				String Search_Msg=rs.getString(3);
				String Search_Time=rs.getString(4);
				ob_Receive_List.add(new TableRowDataModel_Msg(new SimpleStringProperty(Search_Program),new SimpleStringProperty(Search_ID),new SimpleStringProperty(Search_Msg),new SimpleStringProperty(Search_Time)));
				//�ش� ����� �ϳ��� �޾ƿ� list�� ����
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		return true;			
	}//Receive_Msg_SQL �Լ� �� -> SQL���� �޼����� �޾ƿ��� �Լ�(�����޼������⿡ �ʿ�)
	
	@FXML public void moveShow(ActionEvent event) {
		try {
			Stage primaryStage=(Stage)command.getScene().getWindow();
			Stage dialog=new Stage(StageStyle.UTILITY);
			dialog.initOwner(primaryStage);
			dialog.setTitle("��ɾ� ����");
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Help.fxml"));
			Parent root = (Parent) loader.load();
			
			help_Controller = loader.getController(); // ��Ʈ��Ʈ�ѷ� load 
			help_Controller.init(this);
			Stage stage = new Stage();
			stage.setTitle("HELP");
			stage.setScene(new Scene(root));
			stage.show();
			
		}catch(IOException e) {
		//	e.printStackTrace();
		}
	
	}//moveShow()�Լ� �� -> ��ɾ� �����ִ� ���(HELP��ư)
	
	@FXML public void moveFile(ActionEvent event) {
		if(!startBtn.isDisable()) {
			JOptionPane.showMessageDialog(null,"������ ������ �ʿ��մϴ�.","�˸�",JOptionPane.ERROR_MESSAGE);
			SaveText("Error : Don't click receivefile. Server connection first");
			return ;
		}
	
		try {
			Stage primaryStage=(Stage)receiveFile.getScene().getWindow();
			Stage dialog=new Stage(StageStyle.UTILITY);
			dialog.initOwner(primaryStage);
			dialog.setTitle("���� ����");
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ReceiveFile.fxml"));
			Parent root = (Parent) loader.load();
			
			receiveFile_Controller = loader.getController(); // ��Ʈ��Ʈ�ѷ� load 
			receiveFile_Controller.Init(this);
			Stage stage = new Stage();
			stage.setScene(new Scene(root));
			stage.setTitle("Receive file view");
			stage.show();
			
		}catch(IOException e) {
			//e.printStackTrace();
		}
	}//moveFile()�Լ� �� -> ���� ������ �����ִ� �Լ� 
	
	@FXML public void moveStart(ActionEvent event) {
		
		table_initalize();	// ���̺� �ʱ�ȭ
		table_select();		// ���̺� �ϳ��� ����
		window_control(); 	//������ Xǥ�� ��Ʈ��
	
		if(IP.equals("")||PORT==-1)	{JOptionPane.showMessageDialog(null,"�����ǿ� ��Ʈ�� ���� �����ϼ���","�˸�",JOptionPane.ERROR_MESSAGE); return;}
		if(UseDatabase==false) 	{JOptionPane.showMessageDialog(null,"������ ���̽��� ���� �����ϼ���","�˸�",JOptionPane.ERROR_MESSAGE); return;}
		
		Server_Start(); // ���������� �����Ͽ� Ŭ���̾�Ʈ ������ ��� �ϴ� �޼ҵ�
		File directory=new File("C:\\SWProject_Server");
		if(!directory.exists()) {
			directory.mkdirs();
			System.out.println("SWProject_Server ������ ��������ϴ�");
		}
	}//moveStart()�Լ� -> ����execute ��ư Ŭ���� ���� �����ϰ� ��
	
	// ���̺� �ʱ�ȭ
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
		
	}//table_initalize()�Լ� -> ���̺� ���� ����� �� �ֵ��� ����

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
	}//table_select()�Լ� �� -> ���̺��� ���̵� �����ϰ� ���ִ� �Լ�
	
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
			JOptionPane.showMessageDialog(null,"SQL ������ ���� �ʽ��ϴ�. ���̵�/��� �Ǵ� ��ǻ�Ϳ� ����Ǿ��ִ��� Ȯ���Ͻʽÿ�.","�˸�",JOptionPane.ERROR_MESSAGE);
			SaveText("Error : SQl is not running or not connect in the computer");
		}
		
	}//on_Databae()�Լ� �� -> �����ͺ��̽��� ���� ������Ű�� �Լ�
	
	
	public void Server_Start() {
		try {
			net_List.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
			tina_List.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
			
			server_Socket = new ServerSocket(PORT); // �������ϻ���
			if(server_Socket!=null) { // ������ ���������� ������ ���
				startBtn.setDisable(true);
				stopBtn.setDisable(false);
				JOptionPane.showMessageDialog(null,"������ ����Ǿ����ϴ�","�˸�",JOptionPane.INFORMATION_MESSAGE);
				runtime(); //�ð� ����
				Connection(); // ��Ʈ���� �����Ͽ� Ŭ���̾�Ʈ ������ ����ϴ� �Լ�
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,"�̹� ������� ��Ʈ�Դϴ�","�˸�",JOptionPane.ERROR_MESSAGE);
			SaveText("Error : �̹� ������� ��Ʈ�Դϴ�");
		}
	}//Server_Start()�Լ� �� -> ���� ���� ���λ��� ǥ�� (���̺� �� ���� �ʱ�ȭ, ���ϻ���, ��Ʈ�� ���� ��)
	
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
						JOptionPane.showMessageDialog(null,"���� ������ ��� �Ͽ����ϴ�.","�˸�",JOptionPane.ERROR_MESSAGE);
						SaveText("���� ������ ��� �Ͽ����ϴ�.");
						e.printStackTrace();
						break;
					}// Ŭ���̾�Ʈ ���� ���Ѵ�� 
				}
			}
		});
		
		th.start(); 
	}//Connection()�Լ� �� -> ���� ���Ѵ����·� Ŭ���̾�Ʈ multithread ���� �� �� �ֵ��� ��
	
	
	
	
	public class ClientInfo extends Thread{
		//***********Client ��Ʈ�� ����**********
		public InputStream is;				//��Ʈ��
		public OutputStream os;
		public DataInputStream dis;			//�����ͽ�Ʈ��
		public DataOutputStream dos;
		public Socket Client_Socket;		//Ŭ���̾�Ʈ ����
		public String Client_ID;
		public String Client_Password;
		public String Client_IP;
		public String Client_Program;
		//************************************
		
		private ClientInfo(Socket soc) {
			this.Client_Socket = soc;	// Ŭ���̾�Ʈ�� ������ ����
			ClientNetWork();			// ��Ʈ�� ���� 
		}
		
		private void ClientNetWork() {
			try {
				is = Client_Socket.getInputStream();
				os = Client_Socket.getOutputStream();
				dis = new DataInputStream(is);
				dos = new DataOutputStream(os);
			}
			catch(IOException e) {
				JOptionPane.showMessageDialog(null,"Error :setting stream","�˸�",JOptionPane.ERROR_MESSAGE);
				SaveText("error : Stream ���� ����");
			}
		}//CLientNetWork()�Լ� �� -> ��Ʈ�� �����Լ�
		
		public void run() {
			while(true) {
				try {
					String msg = dis.readUTF();
					System.out.println(Client_IP+": �κ��� ���� �޼��� :"+msg+"\n");
					InMessage(msg); // �޼����� �ؼ��� �� �ִ� �Լ�  ei) �������� / �޼��� 
				}
				catch(IOException e) {
					// Ŭ���̾�Ʈ�� ���� ���Ḧ �Ͽ��� ���
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

						// ����Ʈ�� �ʱ�ȭ(���ΰ�ħ) Client_list.setListData(Client_Name_vc)
					} catch(Exception e2) { }
				}
			}
		}//run()�Լ� �� -> Ŭ���̾�Ʈ�κ��� �޼����� ���������� �޾ƿ��� �Լ�
	
		/**
		 * @param msg
		 */
		public void InMessage(String msg) {
			st = new StringTokenizer(msg,"/");
		
			String protocol = st.nextToken(); // ��������
			String message = st.nextToken();  // ID
			
			System.out.println("��������: "+protocol);
			
			System.out.println("�޼���: "+message);
			if(protocol.equals("Join")) {
				//message => �̸�
				String Join_ID = st.nextToken();
				String Join_Password = st.nextToken();
				String Join_phoneNumber = st.nextToken();
				String Join_Address = st.nextToken();
				String Join_Program = st.nextToken();
				
				String insertSql = "insert into membertbl values"+"("
				+"'"+message+"'"+","					// �̸�
				+"'"+Join_ID+"'"+","					// ���̵�
				+"'"+Join_Password+"'"+","				// �н�����
				+"'"+Join_Program+"'"+","				// ���α׷�
				+"'"+Join_phoneNumber+"'"+","			// ����ó
				+"'"+Join_Address+"'"+")"+";";			// �ּ� 	
				
				try {
					stmt.executeUpdate(insertSql);
					SaveText("[Message] Sign-up: �̸�/���α׷�/������/�н�����/����ó/�ּ�");
					SaveText("<"+message+"/"+Join_Program+"/"+Join_ID+"/"+Join_Password+"/"+Join_phoneNumber+"/"+Join_Address+">");
						} catch (SQLException e) {
					// TODO Auto-generated catch block
				} 
				
			}//ȸ������ ��û �������� ��

			else if(protocol.equals("Login")) {
				
				//���̵�/��й�ȣ/������� ���α׷� => message
				Client_Program = message;				
				Client_ID = st.nextToken();
				Client_Password = st.nextToken();
				Client_IP=st.nextToken();
				SaveText("[Message] Login :"+Client_ID+"/"+Client_Password);
				
				String loginSql="select memberProgram, if(memberID='"+Client_ID+"'&&memberPassword='"+Client_Password+"','true','false') from membertbl where memberID='"+Client_ID+"';";
				String Login_Success="false"; //�α��� ���ɿ���
				
				try {
					rs=stmt.executeQuery(loginSql);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}//�ش� ����� rs�� ����
				try {
					while(rs.next()) {
						Client_Program=rs.getString(1);
						Login_Success=rs.getString(2);
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}//�ش����� �ϳ��� �޾ƿ� ���� ���� �� Client�� ����
				
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
							Connect_Lbl.setText(Tina_Count+Net_Count+"");//����� Ŭ���̾�Ʈ ����
							tina_List.setItems(Tina_list);
						});
					}
					
					else if(Client_Program.equals("Net")) {
						Net_Client_vc.add(this);
						Net_Count++;
						Net_list.add(new TableRowDataModel(Net_Count,Client_ID,Client_Password,Client_IP," "));
						Platform.runLater(()->{
							Connect_Lbl.setText(Tina_Count+Net_Count+"");//����� Ŭ���̾�Ʈ ����
							net_List.setItems(Net_list);
						});
						
					}
					SaveText("Login Success :"+Client_Program+"/"+Client_ID+"/"+Client_Password);
					}
				}
			}//�α��� �������� ��
			
			else if(protocol.equals("Connect")) {
				SaveText("[Message] Connect :"+message);
				System.out.println(message);
			}//Connect �������� �� -> ������ Ŭ���̾�Ʈ connect
			
			else if(protocol.equals("Search")) {
				String Search_phoneNumber = st.nextToken();
				SaveText("[Message] Find ID/Password: name="+message+" phone="+Search_phoneNumber);	
				String searchSql="select memberID,memberPassword,memberProgram from membertbl where memberName='"+message+"' and memberPhone='"+Search_phoneNumber+"'";
				
				try {
					rs=stmt.executeQuery(searchSql);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}//�ش� ����� rs�� ����
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
					}//�ش� �̸�,��ȭ��ȣ�� �´� ���̵�/��� ã��
					
					if(exist==0) {
						send_Message("Search Failed"+"/"+message+"/"+Search_phoneNumber);
						SaveText("Search Failed: name="+message+" phone="+Search_phoneNumber);
					}//�ش� �̸�,��ȭ��ȣ�� ���� ġ 
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					// e.printStackTrace();
				}//�ش����� �ϳ��� �޾ƿ� ���� ���� �� Client�� ����	
			}// ���̵�/��й�ȣ ã�� �������� ��

			else if(protocol.equals("Send_Msg")) {
				Date today=new Date();
				SimpleDateFormat date=new SimpleDateFormat("yyyy/MM/dd");
				SimpleDateFormat time=new SimpleDateFormat("kk:mm:ss");//��¥,�ð� ����
				
				String SendMsg_Program=message;
				String SendMsg_ID=st.nextToken();
				String SendMsg_Msg=st.nextToken();
				String SendMsg_Time=date.format(today)+" "+time.format(today);
				String insertSql = "insert into msgtbl values"+"("
						+"'"+SendMsg_Program+"'"+","			// ���α׷�
						+"'"+SendMsg_ID+"'"+","					// ���̵�
						+"'"+SendMsg_Msg+"'"+","				// �޼��� ����
						+"'"+SendMsg_Time+"'"+")"+";";			// �ð�
				try {
					stmt.executeUpdate(insertSql);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					// e.printStackTrace();
				}
				SaveText("[Mssage]ReceiveMsg: "+SendMsg_ID+"/"+SendMsg_Msg);
				
				System.out.println(ob_Receive_List.get(0).MsgProperty());
				
			}//Ŭ���̾�Ʈ���� ���� �޼��� ���� ��������
			
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
					//���� ��Ͽ� �߰�
					fos.close();//�ش� ���Ͻ�Ʈ�� �ݱ�
				} catch (FileNotFoundException e) {
					System.out.println("FileSend Error");
				} catch (IOException e) {
					// e.printStackTrace();
				}

			}//���� ���۹ޱ� ��������
			
			else if(protocol.equals("CheckID")) {
				String checkSql="select if(memberID='"+message+"','exist','non_exist') from membertbl where memberId='"+message+"';";
				SaveText("[Message]Check ID: "+message);
				try {
					rs=stmt.executeQuery(checkSql);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}//�ش� ����� rs�� ����
				try {
					while(rs.next()) {
						String Check=rs.getString(1);
						SaveText("ID_Check Success : "+Check);
						send_Message("CheckID"+"/"+Check);
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					// e.printStackTrace();
				}//�ش����� �ϳ��� �޾ƿ� ���� ���� �� Client�� ����
						
			}//CheckID �������� �� -> �ߺ����̵�üũ(Database)�̿�
			
			else if(protocol.equals("Terminate")) {
				//Ŭ���̾�Ʈ���� ���α׷��� �����ų��
				//�޼��� = "Terminate/���α׷�/Ŭ���̾�ƮID"
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
							if(!item.SelectProperty().getValue().equals("   ��")) {
								Auto_Tina_Count++;
							}
							replace.add(new TableRowDataModel(Tina_Count, item.IDProperty().getValue(), item.PassWordProperty().getValue(), item.IPProperty().getValue(), item.SelectProperty().getValue()));
						
						}
					}
					Tina_list.removeAll(Tina_list); //���� List ��ü ������  ID���ܵ� ������ ���
					Tina_list.addAll(replace);
					
					Platform.runLater(()->{
						Connect_Lbl.setText(Tina_Count+Net_Count+"");//����� Ŭ���̾�Ʈ ����
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
							if(!item.SelectProperty().getValue().equals("   ��")) {
								Auto_Net_Count++;
							}
							replace.add(new TableRowDataModel(Net_Count, item.IDProperty().getValue(), item.PassWordProperty().getValue(), item.IPProperty().getValue(), item.SelectProperty().getValue()));
						}
					}
					Net_list.removeAll(Net_list);//���� List ��ü ������  ID���ܵ� ������ ���
					Net_list.addAll(replace);
					
					Platform.runLater(()->{
						Connect_Lbl.setText(Tina_Count+Net_Count+""); //����� Ŭ���̾�Ʈ ����
						Auto_ConnectLbl.setText(Auto_Tina_Count+Auto_Net_Count+"");
						net_List.setItems(Net_list);
					});
				}
			}//Terminate �������� �� -> Ŭ���̾�Ʈ�� ������ ����Ǿ��ٴ°��� �˷���(ID����)
			
		}//inmessage()�Լ� -> �޾ƿ� �޼��� �ؼ��Լ�(protocol/��ɾ�/...)
	
		public void send_Message(String str) {
			try {
				dos.writeUTF(str);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}
		}//send_Message()�Լ� �� ->������ ���̵𿡰� �޼��� �����Ⱑ��
		
		
	}// ClientInfo Ŭ���� ��
	
	public void BroadCast(String member, String str) { // ��� Ŭ���̾�Ʈ �鿡�� �������ݰ� �޼����� ���� ei) �������� FileResend *���������� 
		if(member.equals("Everyone")){
			for(int i=0;i<Total_Client_vc.size();i++) {
				ClientInfo u = (ClientInfo)Total_Client_vc.elementAt(i);
				SaveText("(Send)Everyone :"+str);
				u.send_Message(str);
			}
		}//Tina, Net �� ��� broadcast
		else if(member.equals("All TinaMember")) {
			for(int i=0; i<Tina_Client_vc.size(); i++) {
				ClientInfo u=(ClientInfo)Tina_Client_vc.elementAt(i);
				SaveText("(Send)All TinaMember: "+str);
				u.send_Message(str);
			}
		}//Tina �� ��� broadcast
		else if(member.equals("All NetMember")) {
			for(int i=0; i<Net_Client_vc.size(); i++) {
				ClientInfo u=(ClientInfo)Net_Client_vc.elementAt(i);
				SaveText("(Send)All NetMember: "+str);
				u.send_Message(str);
			}
		}//Net �� ��� broadcast
	
	}//BroadCast()�Լ� �� 

	public void NoteSend(String msg) {
		if(msg!=null) {
			for(int i=0; i<Tina_Client_vc.size();i++) {
				ClientInfo u = (ClientInfo)Tina_Client_vc.elementAt(i);
				System.out.println("Ŭ���̾�ƮID: "+u.Client_ID);
				
				if(u.Client_ID == Client_NickName) {
					System.out.println("Ŭ���̾�Ʈ�� ã�ҽ��ϴ�");
					SaveText("(Send)"+Client_NickName+": "+msg);
					u.send_Message("Note/"+msg);
				}
			}
			for(int i=0;i<Net_Client_vc.size();i++) {
				ClientInfo u = (ClientInfo)Net_Client_vc.elementAt(i);
				System.out.println("Ŭ���̾�ƮID: "+u.Client_ID);
				String text="Ŭ���̾�ƮID: "+u.Client_ID+"        ";
				SaveText(text);
				if(u.Client_ID == Client_NickName) {
					System.out.println("Ŭ���̾�Ʈ�� ã�ҽ��ϴ�");
					SaveText("(Send)"+Client_NickName+": "+msg);
					u.send_Message("Note/"+msg);
				}
			}
		}
		
		else {
			JOptionPane.showMessageDialog(null,"�޼��� ������ �����ϴ�","�˸�",JOptionPane.ERROR_MESSAGE);
			SaveText("Error: �޼��� ������ �����ϴ�");
		}
	}//NoteSend()�Լ� �� -> ���ϴ� Ŭ���̾�Ʈ���ٰ� �޼��� �����Ⱑ��
	
	
	@FXML public void moveStop(ActionEvent event) {
		if(!startBtn.isDisable()) {
			JOptionPane.showMessageDialog(null,"������ ������ �ʿ��մϴ�.","�˸�",JOptionPane.ERROR_MESSAGE);
			SaveText("Error : Don't click stopBtn. Server connection first");
			return ;
		}
		System.out.println("����������ưŬ��");
		BroadCast("Everyone","Everyone/Server_Terminate");
		runtime_stop=true;//�ð� ����
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
				Connect_Lbl.setText(Tina_Count+Net_Count+"");//����� Ŭ���̾�Ʈ ����
				Auto_ConnectLbl.setText(Auto_Tina_Count+Auto_Net_Count+"");
			});
			//�ʱ�ȭ
		}catch(IOException e) {
			
		}
	}//moveStop()�Լ� �� -> ���� ���� ���
	
	@FXML public void moveLog(ActionEvent event) {
		try {
			Stage primaryStage=(Stage)logBtn.getScene().getWindow();
			Stage dialog=new Stage(StageStyle.UTILITY);
			dialog.initOwner(primaryStage);
			dialog.setTitle("Log View");
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ServerLog.fxml"));
			Parent root = (Parent) loader.load();
			
			serverLog_Controller = loader.getController(); // ��Ʈ��Ʈ�ѷ� load 
			serverLog_Controller.init(this);
			Stage stage = new Stage();
			stage.setTitle("Log View");
			stage.setScene(new Scene(root));
			stage.show();
			
		}catch(IOException e) {
			// e.printStackTrace();
		}
	}//moveLog()�Լ� �� -> �����α� �����ִ� ��� (���̺�並 �̿��� GUI���λ���)
	
	@FXML public void moveSave(ActionEvent event) {
		JOptionPane.showMessageDialog(null,"Log�� �����Ͽ����ϴ�.","�˸�",JOptionPane.ERROR_MESSAGE);
		SaveText("Save Log in the file");
	}//moveSave()�Լ� �� -> �����α� ���� ���

	@FXML public void moveRed(ActionEvent event) {
		if(!startBtn.isDisable()) {
			JOptionPane.showMessageDialog(null,"������ ������ �ʿ��մϴ�.","�˸�",JOptionPane.ERROR_MESSAGE);
			SaveText("Error : Don't click button. Server connection first");
			return ;
		}
		/*try {
			Stage primaryStage=(Stage)Manage_RedBtn.getScene().getWindow();
			Stage dialog=new Stage(StageStyle.UTILITY);
			dialog.initOwner(primaryStage);
			dialog.setTitle("�޴�����Ʈ ���� �ý���-���");
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Manage.fxml"));
			Parent root = (Parent) loader.load();
			
			manageController = loader.getController(); // ��Ʈ��Ʈ�ѷ� load 
			manageController.ColorDot.setFill(Color.RED);
			Stage stage = new Stage();
			stage.setScene(new Scene(root));
			stage.show();
			
		}catch(IOException e) {
			e.printStackTrace();
		}*/
	}//moveRed()�Լ� �� -> �˶� �Լ�
	@FXML public void moveOrange(ActionEvent event) {
		if(!startBtn.isDisable()) {
			JOptionPane.showMessageDialog(null,"������ ������ �ʿ��մϴ�.","�˸�",JOptionPane.ERROR_MESSAGE);
			SaveText("Error : Don't click button. Server connection first");
			return ;
		}
		/*try {
			Stage primaryStage=(Stage)Manage_RedBtn.getScene().getWindow();
			Stage dialog=new Stage(StageStyle.UTILITY);
			dialog.initOwner(primaryStage);
			dialog.setTitle("�޴�����Ʈ ���� �ý���-������");
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Manage.fxml"));
			Parent root = (Parent) loader.load();
			
			manageController = loader.getController(); // ��Ʈ��Ʈ�ѷ� load 
			manageController.ColorDot.setFill(Color.ORANGE);
			Stage stage = new Stage();
			stage.setScene(new Scene(root));
			stage.show();
			
		}catch(IOException e) {
			e.printStackTrace();
		}*/
	}//moveOrange()�Լ� �� -> �˶��Լ�
	@FXML public void moveYellow(ActionEvent event) {
		if(!startBtn.isDisable()) {
			JOptionPane.showMessageDialog(null,"������ ������ �ʿ��մϴ�.","�˸�",JOptionPane.ERROR_MESSAGE);
			SaveText("Error : Don't click button. Server connection first");
			return ;
		}
	/*	try {
			Stage primaryStage=(Stage)Manage_RedBtn.getScene().getWindow();
			Stage dialog=new Stage(StageStyle.UTILITY);
			dialog.initOwner(primaryStage);
			dialog.setTitle("�޴�����Ʈ ���� �ý���-���");
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Manage.fxml"));
			Parent root = (Parent) loader.load();
			
			manageController = loader.getController(); // ��Ʈ��Ʈ�ѷ� load 
			manageController.ColorDot.setFill(Color.YELLOW);
			Stage stage = new Stage();
			stage.setScene(new Scene(root));
			stage.show();
			
		}catch(IOException e) {
			e.printStackTrace();
		}*/
	}//moveYellow()�Լ� �� -> �˶��Լ�
	@FXML public void moveGreen(ActionEvent event) {
		if(!startBtn.isDisable()) {
			JOptionPane.showMessageDialog(null,"������ ������ �ʿ��մϴ�.","�˸�",JOptionPane.ERROR_MESSAGE);
			SaveText("Error : Don't click button. Server connection first");
			return ;
		}
		
	}//moveGreen()�Լ� �� -> �˶��Լ�
	
	
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
		};//thread ����
		thread.setDaemon(true);//��׶��忡�� ����
		thread.start();
	}//runtime()�Լ� -> ���� ����ð� �������

	public void SaveText(String message) {
		Date today=new Date();
		SimpleDateFormat date=new SimpleDateFormat("yyyy/MM/dd"); //���� ��¥,�ð� ���ϱ�
		SimpleDateFormat time=new SimpleDateFormat("kk:mm:ss");
		ob_Log_List.add(new TableRowDataModel_Log(new SimpleIntegerProperty(count+1),new SimpleStringProperty(date.format(today)+" "+time.format(today)),new SimpleStringProperty(message)));
		count++;
	}//SaveText()�Լ� �� -> �α׸� tableview�� ����

	
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
							if(!stopBtn.isDisable()) JOptionPane.showMessageDialog(null,"������ ���������� �����մϴ�.","�˸�",JOptionPane.ERROR_MESSAGE);
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
	}//window â �ݾ��� �� ��� ����,���� ����
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
}