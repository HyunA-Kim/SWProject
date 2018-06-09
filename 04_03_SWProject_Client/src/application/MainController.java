package application;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JOptionPane;

import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

public class MainController {
	@FXML Button optionBtn;
	@FXML Button loginBtn;
	@FXML Button sendBtn;
	@FXML Button joinBtn;
	@FXML Button Search_Btn;
	@FXML TextField ID_TextField;
	@FXML TextField Pass_TextField;
	
	//************Network ����************
	public Socket socket;
	public String IP="";
	public int PORT;
	public String Program;
	public String ID;
	public String PassWord;
	//***********************************
	
	//*************Stream����**************
	public InputStream is;
	public OutputStream os;
	public DataInputStream dis;
	public DataOutputStream dos;
	//***********************************
	
	//*********ȸ������ Ŭ���̾�Ʈ ����*********** 
	public String Join_Client_Name;								// ���� �̸�
	public String Join_Client_ID;								// ���� ���̵�
	public String Join_Client_Password;							// ���� ��й�ȣ
	public String Join_Client_phoneNumber;						// ���� ����ó
	public String Join_Client_Address;							// ���� �ּ�
	public String Join_Client_Program;							// ���� ������α׷�
	//************************************
	
	//***************���� ��� ***************
	public String FilePath="C:\\event.txt";
	//************************************
	
	//******���̵�/��й�ȣ ã�� Ŭ���̾�Ʈ ����****** 
	public String Search_Client_Name;							// ã�� �̸�
	public String Search_Client_ID;								// ã�� ���̵�
	public String Search_Client_Password;						// ã�� ��й�ȣ
	public String Search_Client_phoneNumber;					// ã�� ����ó
	//************************************
	//************** ��Ÿ���� ****************
	public boolean set_auto = false;							// �ڵ� ���� ����(true=�ڵ�����, false=�ڵ��������)
	private StringTokenizer st;									// ��ū������
	public boolean on_Connection = false;						// �������� ���� ������ Ȯ�� ei) ����=true, ����x=false 
	public int count=0;
	boolean gotoLogin=false;									// �α����� �� �� �ִ����� ���� true= �α������� , false=�α��� �Ұ�
	//*************************************
	
	
	//************FXML Controller**************
	OptionController op_Controller=null; 						// ȯ�漳�� â
	Send_Message_Controller send_Controller=null;				// �޼��� ������â 
	JoinController joinController=null;							// ȸ������ â
	ClientController clientController=null;						// Ŭ���̾�Ʈ ���ӱ� â
	Search_UserInfo_Controller Search_Controller=null;			// ���̵�/��й�ȣ ã�� â
	ClientLog_Controller clientLog_Controller=null; 			// �α׺��� â
	//*****************************************
	
	//************observable list ****************
	ObservableList<TableRowDataModel_ClientLog> ob_Log_List=FXCollections.observableArrayList();	//Ŭ���̾�Ʈ �α� ����Ʈ
	//********************************************
	
	//************* ������ ���� ���� ���� �ֱ� *************
	String Period_time="2";										// ����(�ֱ�)
	String Period_txt="";										// ���� : sec,min,hour,day
	TimerTask m_task;											// ���� �ڵ����� ���� ������ m_task,m_timer
	Timer m_timer;
	//*************************************
	
	
	
	@FXML public void moveOption() {
		
		try {
			Stage primaryStage=(Stage)optionBtn.getScene().getWindow();
			Stage dialog=new Stage(StageStyle.UTILITY);
			dialog.initOwner(primaryStage);
			dialog.setTitle("ȯ�� ����");
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Option.fxml"));
			Parent root = (Parent) loader.load();
			
			op_Controller = loader.getController(); 
			op_Controller.Init(this);
			Stage stage = new Stage();
			stage.setScene(new Scene(root));
			stage.setTitle("Option");
			stage.show();
			
		}catch(IOException e) {
			e.printStackTrace();
		}
	} //IP,Port ��ȣ ���� â ��
	
	@FXML public void moveLogin() {
		
		if(IP.equals("")) {
			JOptionPane.showMessageDialog(null,"�����ǿ� ��Ʈ ��ȣ�� ���� �Է��ϼ���","�˸�",JOptionPane.ERROR_MESSAGE);
			SaveText("Error : Enter the IP, Port first");
			return;
		}
		ID = ID_TextField.getText().trim();
		PassWord=Pass_TextField.getText().trim();
		
		InetAddress thisIp;
		String thisIpAddress="";
		
		try {
			thisIp = InetAddress.getLocalHost();
			thisIpAddress=thisIp.getHostAddress().toString();
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}//ȣ��Ʈ IP ���� ��
		
		Send_Message("Login"+"/"+Program+"/"+ID+"/"+PassWord+"/"+thisIpAddress); //�α���+ȣ��Ʈ������
		SaveText("Send login to server :"+ ID+"/"+PassWord+"/"+thisIpAddress);
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(gotoLogin==true) Start_Client(); //Ŭ���̾�Ʈ â ����
	}//�α��� ���� â ��
	
	public void Network() {
		try {
			socket = new Socket(IP,PORT);
			if(socket!=null) {
				on_Connection = true;
				System.out.println(IP+": ������ �����Ͽ����ϴ�");
				SaveText(IP+": Scccess connection setup");
				Connection(); // ��Ʈ���� �����Ͽ� ������ �����͸� �ְ���� �� �ִ� �Լ� 
			}
		} catch (UnknownHostException e) {
			JOptionPane.showMessageDialog(null,"�������� ������ �����Ͽ����ϴ�","�˸�",JOptionPane.ERROR_MESSAGE);
			SaveText("Error : Failed connection server");
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,"�������� �������� �ƴմϴ�","�˸�",JOptionPane.ERROR_MESSAGE);
			SaveText("Error : Not connection with server now");
		}
	}//Ŭ���̾�Ʈ ���ϰ� ���� ���� ,��Ʈ�� ���� ��
	
	public void Send(String str) {
		System.out.println(ID+" "+Program);
		System.out.println(str);
	}//�޼��� ������ ��� ��	
	
	public void Connection() {
		try {
			is = socket.getInputStream();
			os = socket.getOutputStream();
			dis = new DataInputStream(is);
			dos = new DataOutputStream(os);
		}catch(IOException e) {
			JOptionPane.showMessageDialog(null,"��Ʈ�� ���� ����","�˸�",JOptionPane.ERROR_MESSAGE);
			SaveText("Error : Failed setup stream");
		}//��Ʈ�� ���� ��
		
		Send_Message("Connect"+"/"+IP+"/"+PORT);
		SaveText("Send connect to server:"+IP+"/"+PORT);
		Thread th = new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					try {
					System.out.println(ID+": �����κ��ͺ��� �޽��� ���� ������Դϴ� "+"\n");
					SaveText(ID+": Waiting for message receive from server");
					String msg = dis.readUTF();
					System.out.println("�����κ��� ���ŵ� �޼���: "+msg+"\n");
					InMessage(msg); // ei) ��������/����
					}catch(IOException e) {
						try {
							JOptionPane.showMessageDialog(null,"������ ������ ���������ϴ�","�˸�", JOptionPane.ERROR_MESSAGE);
							SaveText("Error : break connection with server");
							os.close();
							is.close();
							dos.close();
							dis.close();
							socket.close();
						}catch(Exception e2) {}
						break;
					}//������ �������� ���, ��� ��Ʈ���� ���� �ݱ�
				} // while�� �� 
				
			}
		});
		th.start();
	}//Connection() �Լ� ��
	
	
	public void Start_Client() {
		try {
			Stage primaryStage=(Stage)loginBtn.getScene().getWindow();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Client.fxml"));
			Parent root = (Parent) loader.load();
			Scene scene=new Scene(root);
			primaryStage.setTitle("Client");
			primaryStage.setScene(scene);
			primaryStage.setOnHiding(new EventHandler<WindowEvent>() {

				@Override
				public void handle(WindowEvent arg0) {
					// TODO Auto-generated method stub
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							try {
								Send_Message("Terminate/"+Program+"/"+ID);
								JOptionPane.showMessageDialog(null,"Ŭ���̾�Ʈ ���α׷��� �����մϴ�.","�˸�",JOptionPane.ERROR_MESSAGE);
								os.close();
								is.close();
								dos.close();
								dis.close();
								socket.close();
								set_auto=false;
								System.out.println("�ý����� ����Ǿ����ϴ�.");
								System.exit(0);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						
							
						}
						
					});
				}
				
			});//â"X" �ݱ� ��ư ������ ��� ��Ʈ�� ����
			clientController = loader.getController(); // ��Ʈ��Ʈ�ѷ� load
			clientController.init(this);
			clientController.ID_Lbl.setText(ID);
			clientController.IP_Lbl.setText(IP);
			String port=PORT+"";
			clientController.Port_Lbl.setText(port);
			clientController.ColorDot.setFill(Color.BLUE);
		
			}catch(IOException e) {
			e.printStackTrace();
		}
	}//Start_Client()�Լ� ��
	
	public String Current_Date(String select) {
		Date today=new Date();
		SimpleDateFormat date=new SimpleDateFormat("yyyy/MM/dd");
		SimpleDateFormat time=new SimpleDateFormat("kk:mm:ss");//���� ��¥ �ð� �޾ƿ���
		if(select.equals("TimeInfo")) {
			return time.format(today);
		}
		else if(select.equals("DateInfo")) {
			return date.format(today);
		}
		else if (select.equals("DayInfo")) {
			return date.format(today)+" "+time.format(today);
		}
		else {
			return "����";
		}
	}//Current_Date()�Լ� ��
	
	private void InMessage(String str) {
		st = new StringTokenizer(str,"/");
		String protocol = st.nextToken();
		String message = st.nextToken();
		
		System.out.println("��������: "+protocol+"/"+message);
	
		if(protocol.equals("Note")) {
			if(message.equals("Logout")) {
				clientController.move_Logout();
				SaveText("[Message] Logout client program.");
			}//�����α׾ƿ�
			else if(message.equals("Quit")) {
				clientController.controlQuit();
				SaveText("[Message] Terminate client program");
			}//��������
			else{
				System.out.println("�����κ��� ���� �޼���: "+message);
				SaveText("[Message] Server :"+message);
			}//������ �޼��� �ޱ�
		}//Note �������� ��(������ �޼��������� ���)
		else if(protocol.equals("CheckID")) {
			joinController.existID=message;
			System.out.println("�����κ��� ���̵� �ߺ�Ȯ�� : "+message);
			SaveText("[Message] Server :"+ "Check exist ID ="+message);
		}//���̵� �ߺ�Ȯ�� �������� ��
		else if(protocol.equals("Search")) {
			String Search_Password=st.nextToken();
			String Search_Program=st.nextToken();
			System.out.println("�����κ��� ã�� ���̵�: "+message);
			System.out.println("�����κ��� ã�� ��й�ȣ: "+Search_Password);
			Search_Controller.Search_ID_TextField.setText(message);
			Search_Controller.Search_Pass_TextField.setText(Search_Password);
			Program=Search_Program;
			SaveText("[Message] Success search ID/Password");
		}//���̵�,��� ã�� �������� ��
		else if(protocol.equals("Search Failed")) {
			Search_Controller.failSearch();
			SaveText("Error : Failed search ID/Password");
		}//���̵�,��� �������� ã�� ��� = ���� ��ȯ
		else if(protocol.equals("Request_FileSend")) {
			System.out.println("�������� �������ۿ�û�� �ֽ��ϴ�");
			File_Send(); 
			SaveText("[Message]  Request file send");
		}//�������� getfile��ư Ŭ���� ����Ǵ� �������ݳ�

		else if(protocol.equals("Everyone")) {
			
			if(message.equals("Request_FileSend")) {
				System.out.println("��ü�޼��� : �������� �������ۿ�û�� �ֽ��ϴ�");
				File_Send();
				SaveText("[Message] Everyone: request file send");
			}//������ getfile ��ư Ŭ����
			else if(message.equals("Server_Terminate")) {
				clientController.ColorDot.setFill(Color.RED);
				clientController.alarm();
				SaveText("[Message] Everyone: server terminate");
				
			}//������ ���� �Ǿ��� ��
			else if(message.equals("FileSend_Auto")) {
				Period_time=st.nextToken();
				Period_txt=st.nextToken();
				FileSend_Auto();
				SaveText("[Message] Everyone: request file send auto");
			}//������ �����ڵ����� ��ɻ��
			else if(message.equals("FileSend_NotAuto")) {
				set_auto=false;
				SaveText("[Message] Everyone: stop file send auto");
			}//������ �����ڵ����� ��� ��� ���
			else if(message.equals("Quit")) {
				clientController.ColorDot.setFill(Color.RED);
				Platform.runLater(()->{
				clientController.controlQuit();
				SaveText("[Message] Terminate client program");
				});
			}//���� ���� ����� ���
			else if(message.equals("Logout")) {
				clientController.move_Logout();
				SaveText("[Message] Logout client program.");
			}//������ Ŭ���̾�Ʈ �α׾ƿ� ��û�� ���
			else {
				System.out.println("��ü�޼���: "+message);
				SaveText("[Message] Everyone: "+message);
			}//������ �޼��� �������� -> ��� Ŭ���̾�Ʈ���� �޼��� ����
			
		}//���Ŭ���̾�Ʈ���� �޼��� -> server�� broadcast(everyone)��� �������� ��
		else if(protocol.equals("All TinaMember")) {
			
			if(message.equals("Request_FileSend")) {
				System.out.println("All Tina�޼���: �������� �������ۿ�û�� �ֽ��ϴ�");
				File_Send();
				SaveText("[Message] All TinaMember:"+"Request file send");
			}//������ �������ۿ�û
			else if(message.equals("FileSend_Auto")) {
				Period_time=st.nextToken();
				Period_txt=st.nextToken();
				FileSend_Auto();
				SaveText("[Message] All TinaMember:"+"Request auto file send");
			}//������ ���� �ڵ����� ��û
			else if(message.equals("FileSend_NotAuto")) {
				set_auto=false;
				SaveText("[Message] All TinaMember:"+"Request stop atuo file send");
			}//������ ���� �ڵ����� ��� ��û
			else if(message.equals("Quit")) {
				clientController.ColorDot.setFill(Color.RED);
				Platform.runLater(()->{
				clientController.controlQuit();
				SaveText("[Message] Terminate client program");
				});
			}//������ Ŭ���̾�Ʈ ���� �޼��� ���۽�
			
		}//Tina �� ������� �޼��� -> server�� broadcast(All TinaMember)��� �������� ��
		else if(protocol.equals("All NetMember")) {
			
			if(message.equals("Request_FileSend")) {
				System.out.println("All Net�޼���: �������� �������ۿ�û�� �ֽ��ϴ�");
				File_Send();
				SaveText("[Message] All NetMember:Request file send");
			}//������ �������� ��û
			else if(message.equals("FileSend_Auto")) {
				Period_time=st.nextToken();
				Period_txt=st.nextToken();
				FileSend_Auto();
				SaveText("[Message] All NetMember:Request auto file send");
			}//������ ���� �ڵ� ���� ��û
			else if(message.equals("FileSend_NotAuto")) {
				set_auto=false;
				SaveText("[Message] All NetMember:Request stop auto file send");
			}//������ ���� �ڵ����� ��û ���
			else if(message.equals("Quit")) {
				clientController.ColorDot.setFill(Color.RED);
				Platform.runLater(()->{
				clientController.controlQuit();
				SaveText("[Message] Terminate client program");
				});
			}//������ ���� �޼��� ��û
			
		}//Net �� ������� �޼��� -> server�� broadcast(All NetMembeer)��� �������� ��
		
		else if(protocol.equals("Login")) {
			if(message.equals("Failed")) {
				JOptionPane.showMessageDialog(null,"������ �ùٸ��� �ʽ��ϴ�.","�˸�",JOptionPane.ERROR_MESSAGE);
				SaveText("Error :Login wrong ID/Password");
			}//�α��� ������ ���
			else if(message.equals("Failed_Exist")) {
				JOptionPane.showMessageDialog(null,"�̹� ���ӵ� ���̵� �Դϴ�.","�˸�",JOptionPane.ERROR_MESSAGE);
				SaveText("Error :Login with already connection ID");
			}//�̹� ���� �Ǿ��ִ� ���̵��� ���
			else if(message.equals("Success")) {
				String Client_Program=st.nextToken();
				Program=Client_Program;
				gotoLogin=true;
				SaveText("[Message] Login success");
			}//�α��� ������ ���
		}//�α��� �������� ��
		
		else if(protocol.equals("FileSend_Auto")) {
			// message => ~�� �ڿ� ����
			// time => �ð� ���� ���� 
			set_auto = true;
			Period_time=message;
			Period_txt=st.nextToken();
		
			FileSend_Auto();
			SaveText("[Message] :Request auto file send");
		}//�����ڵ����� �������� ��
		
		else if(protocol.equals("FileSend_NotAuto")) {
			set_auto=false;
			SaveText("[Message] :Request stop auto file send");
		}//�����ڵ����� ��� �������� ��
		
	}//Inmessage()�Լ� ��
	
	public void File_Send() {
		int DEFAULT_BUFFER_SIZE=10000; 
		File file = new File(FilePath); 		// ���� ��� ������ �ʿ��ϴٴ� �� 
		if(!file.exists()) {
			JOptionPane.showMessageDialog(null,"������ �������� �����Ƿ� Ŭ���̾�Ʈ���� ��θ� �������ּ���.","�˸�",JOptionPane.ERROR_MESSAGE);
			SaveText("Error : Not exist file");
			return ;
		}
		
		try {
			FileInputStream fis = new FileInputStream(file);
			long filesize = file.length(); // ���� ������
			long totalReadBytes = 0;	   // ���� ������ ũ��
			byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
			int readBytes;				   // ���� ������ ����Ʈ ũ��
			double startTime;			   // ���� ���� �ð� 
			
			
			SaveText(ID+":"+"  Send File to server");
			startTime = System.currentTimeMillis(); // ���� �ð� ����
		
			readBytes = fis.read(buffer);	
			Send_Message("FileSend/"+Program+"/"+ID+"/"+file.getName()+"/"+readBytes+"/"+filesize);
			dos.write(buffer,0,readBytes);
			totalReadBytes +=readBytes;
			System.out.println("In progress: "+totalReadBytes+"/"+filesize+"Byte(s)("+
			(totalReadBytes*100/filesize)+"%)");	//����� Ȯ��
			
			
			System.out.println("File transfer completed");
			SaveText("Progress: "+(totalReadBytes*100/filesize)+"% Complete file transfer");
			double endTime = System.currentTimeMillis();		// ����ð�
			double diffTime = (endTime-startTime)/1000;			// ���۽ð� 
			double transferSpeed = (filesize/1000)/diffTime;	// ���ۼӵ�
			
			System.out.println("time: "+diffTime+"second(s)");
			System.out.println("Average transfer speed: "+transferSpeed+"KB/s");
			fis.close();										// ���� ��Ʈ�� �ݱ�
		}catch(IOException e ) {
			System.out.println("������ �������� �ƴմϴ�");
			SaveText("Error : Not connection with server");
		}
	} // File_Send()��
	
	public void FileSend_Auto() {
		set_auto=true;
		System.out.println("�ڵ������� ���۵Ǿ����ϴ�");
		SaveText("[Auto file] : Start");
		Platform.runLater(()->{
			clientController.AutoLbl.setText("File sending  :  Auto ");							//�ڵ� ������ �̷������ �ִ��� üũ
			clientController.PeriodLbl.setText("Cycle  :  Every "+Period_time+" "+Period_txt);  //�ڵ� ���� �ֱ� üũ
		});
		
		//������
	    m_timer = new Timer();
		m_task = new TimerTask() {
			@Override
			public void run() {
				System.out.println("Set auot��: "+set_auto);
				if(set_auto==true) {
					File_Send(); //�ڵ����� �������� true�� ��� ���� �������� ���
				} //���� �ڵ� ���� ��
				
				else if(set_auto==false){
					m_timer.cancel(); // Ŭ���̾�Ʈ�� ���� => set_auto=false; *��� ����ó�� ����
					System.out.println("�ڵ������� ����Ǿ����ϴ�");
					SaveText("[Auto file] : Stop");
					Platform.runLater(()->{
						clientController.AutoLbl.setText("File sending  :  Not Auto");
						clientController.PeriodLbl.setText("Cycle  :  (Not Auto)");
					});
					
				} //���� �ڵ� ���� ���� ��
			}
		};//TimerTask() ��
		
		long time;
		time=Integer.parseInt(Period_time.trim());
		if(Period_txt.equals("sec")) {	
			time=time*1000;
		}
		else if(Period_txt.equals("min")) {
			time=time*1000*60;
		}
		else if(Period_txt.equals("hour")) {
			time=time*1000*60*60;
		}
		else if(Period_txt.equals("day")) {
			time=time*1000*60*60*24;
		}
		m_timer.schedule(m_task, 2000,time); // �������� ����ð����� (3��°����)
		// m_task(���� �� �޼ҵ�), 2�ʵڿ�����, 2�ʸ��� run()�޼ҵ� ȣ��
	}
	
	public void Send_Message(String str) {
		try {
			dos.writeUTF(str);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,"������ ���� ���� �ƴϰų� �޼����� �������� ���Ͽ����ϴ�","�˸�",JOptionPane.ERROR_MESSAGE);
			SaveText("Error : Failed connection with server or Failed transmite message");
		}
	}//Send_Message() �Լ� ��
	
	public void moveJoin() {
		if(IP.equals("")) {
			JOptionPane.showMessageDialog(null,"IP�� ��Ʈ�� ���� �����ϼ���","�˸�",JOptionPane.ERROR_MESSAGE);
			SaveText("Error : Set up the IP/Port first");
			return;
		}
		try {
			Stage primaryStage=(Stage)joinBtn.getScene().getWindow();
			Stage dialog=new Stage(StageStyle.UTILITY);
			dialog.initOwner(primaryStage);
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Join.fxml"));dialog.setTitle("ȸ�� ����");
			Parent root = (Parent) loader.load();
			
			joinController = loader.getController(); // ��Ʈ��Ʈ�ѷ� load
			joinController.Init(this);
			
			Stage stage = new Stage();
			stage.setTitle("Sign-up");
			stage.setScene(new Scene(root));
			stage.show();
			
		}catch(IOException e) {
			e.printStackTrace();
		}
		
	
	} //moveJoin()�Լ� ��

	public void moveSearch() {
		if(IP.equals("")) {
			JOptionPane.showMessageDialog(null,"IP�� ��Ʈ�� ���� �����ϼ���","�˸�",JOptionPane.ERROR_MESSAGE);
			SaveText("Error : Set up the IP/Port first");
			return;
		}
		try {
			Stage primaryStage=(Stage)Search_Btn.getScene().getWindow();
			Stage dialog=new Stage(StageStyle.UTILITY);
			dialog.initOwner(primaryStage);
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Search_UserInfo.fxml"));
			Parent root = (Parent) loader.load();
			
			Search_Controller = loader.getController(); // ��Ʈ��Ʈ�ѷ� load
			Search_Controller.Init(this);
			
			Stage stage = new Stage();
			stage.setTitle("Find ID/Password");
			stage.setScene(new Scene(root));
			stage.show();
			
		}catch(IOException e) {
			e.printStackTrace();
		}
	}	//moveSearch()�Լ� �� =���̵� ��й�ȣ ã��
	
	public void Set_UserInfo() {
		ID_TextField.setText(Join_Client_ID);
		Pass_TextField.setText(Join_Client_Password);
	}//Set_UserInfo()�Լ� �� -> ȸ�����Խ� ����ߴ� ���̵�,��� �α���â�� ����
	
	//*************ClientLog view �� �޼��� ����****************
	public void SaveText(String message) {
		Date today=new Date();
		SimpleDateFormat date=new SimpleDateFormat("yyyy/MM/dd");
		SimpleDateFormat time=new SimpleDateFormat("kk:mm:ss");
		ob_Log_List.add(new TableRowDataModel_ClientLog(new SimpleIntegerProperty(count+1),new SimpleStringProperty(date.format(today)+" "+time.format(today)),new SimpleStringProperty(message)));
		count++;
		
	}//SaveText()�Լ� ��
	
	public void init(ClientController clientController2) {
		// TODO Auto-generated method stub
		clientController=clientController2;
	}
}
