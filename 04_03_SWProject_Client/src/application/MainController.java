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
	
	//************Network 변수************
	public Socket socket;
	public String IP="";
	public int PORT;
	public String Program;
	public String ID;
	public String PassWord;
	//***********************************
	
	//*************Stream변수**************
	public InputStream is;
	public OutputStream os;
	public DataInputStream dis;
	public DataOutputStream dos;
	//***********************************
	
	//*********회원가입 클라이언트 정보*********** 
	public String Join_Client_Name;								// 가입 이름
	public String Join_Client_ID;								// 가입 아이디
	public String Join_Client_Password;							// 가입 비밀번호
	public String Join_Client_phoneNumber;						// 가입 연락처
	public String Join_Client_Address;							// 가입 주소
	public String Join_Client_Program;							// 가입 사용프로그램
	//************************************
	
	//***************파일 경로 ***************
	public String FilePath="C:\\event.txt";
	//************************************
	
	//******아이디/비밀번호 찾기 클라이언트 정보****** 
	public String Search_Client_Name;							// 찾을 이름
	public String Search_Client_ID;								// 찾을 아이디
	public String Search_Client_Password;						// 찾을 비밀번호
	public String Search_Client_phoneNumber;					// 찾을 연락처
	//************************************
	//************** 기타변수 ****************
	public boolean set_auto = false;							// 자동 전송 여부(true=자동전송, false=자동전송취소)
	private StringTokenizer st;									// 토큰라이저
	public boolean on_Connection = false;						// 서버와의 연결 유무를 확인 ei) 연걸=true, 연결x=false 
	public int count=0;
	boolean gotoLogin=false;									// 로그인을 할 수 있는지의 여부 true= 로그인접속 , false=로그인 불가
	//*************************************
	
	
	//************FXML Controller**************
	OptionController op_Controller=null; 						// 환경설정 창
	Send_Message_Controller send_Controller=null;				// 메세지 보내기창 
	JoinController joinController=null;							// 회원가입 창
	ClientController clientController=null;						// 클라이언트 접속기 창
	Search_UserInfo_Controller Search_Controller=null;			// 아이디/비밀번호 찾기 창
	ClientLog_Controller clientLog_Controller=null; 			// 로그보기 창
	//*****************************************
	
	//************observable list ****************
	ObservableList<TableRowDataModel_ClientLog> ob_Log_List=FXCollections.observableArrayList();	//클라이언트 로그 리스트
	//********************************************
	
	//************* 서버로 부터 받은 파일 주기 *************
	String Period_time="2";										// 숫자(주기)
	String Period_txt="";										// 단위 : sec,min,hour,day
	TimerTask m_task;											// 파일 자동전송 위한 쓰레드 m_task,m_timer
	Timer m_timer;
	//*************************************
	
	
	
	@FXML public void moveOption() {
		
		try {
			Stage primaryStage=(Stage)optionBtn.getScene().getWindow();
			Stage dialog=new Stage(StageStyle.UTILITY);
			dialog.initOwner(primaryStage);
			dialog.setTitle("환경 설정");
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
	} //IP,Port 번호 설정 창 끝
	
	@FXML public void moveLogin() {
		
		if(IP.equals("")) {
			JOptionPane.showMessageDialog(null,"아이피와 포트 번호를 먼저 입력하세요","알림",JOptionPane.ERROR_MESSAGE);
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
		}//호스트 IP 설정 끝
		
		Send_Message("Login"+"/"+Program+"/"+ID+"/"+PassWord+"/"+thisIpAddress); //로그인+호스트아이피
		SaveText("Send login to server :"+ ID+"/"+PassWord+"/"+thisIpAddress);
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(gotoLogin==true) Start_Client(); //클라이언트 창 접속
	}//로그인 접속 창 끝
	
	public void Network() {
		try {
			socket = new Socket(IP,PORT);
			if(socket!=null) {
				on_Connection = true;
				System.out.println(IP+": 서버에 접속하였습니다");
				SaveText(IP+": Scccess connection setup");
				Connection(); // 스트림을 설정하여 서버와 데이터를 주고받을 수 있는 함수 
			}
		} catch (UnknownHostException e) {
			JOptionPane.showMessageDialog(null,"서버와의 연결을 실패하였습니다","알림",JOptionPane.ERROR_MESSAGE);
			SaveText("Error : Failed connection server");
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,"서버와의 연결중이 아닙니다","알림",JOptionPane.ERROR_MESSAGE);
			SaveText("Error : Not connection with server now");
		}
	}//클라이언트 소켓과 서버 소켓 ,스트림 설정 끝
	
	public void Send(String str) {
		System.out.println(ID+" "+Program);
		System.out.println(str);
	}//메세지 보내기 기능 끝	
	
	public void Connection() {
		try {
			is = socket.getInputStream();
			os = socket.getOutputStream();
			dis = new DataInputStream(is);
			dos = new DataOutputStream(os);
		}catch(IOException e) {
			JOptionPane.showMessageDialog(null,"스트림 설정 실패","알림",JOptionPane.ERROR_MESSAGE);
			SaveText("Error : Failed setup stream");
		}//스트림 설정 끝
		
		Send_Message("Connect"+"/"+IP+"/"+PORT);
		SaveText("Send connect to server:"+IP+"/"+PORT);
		Thread th = new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					try {
					System.out.println(ID+": 서버로부터부터 메시지 수신 대기중입니다 "+"\n");
					SaveText(ID+": Waiting for message receive from server");
					String msg = dis.readUTF();
					System.out.println("서버로부터 수신된 메세지: "+msg+"\n");
					InMessage(msg); // ei) 프로토콜/내용
					}catch(IOException e) {
						try {
							JOptionPane.showMessageDialog(null,"서버와 연결이 끊어졌습니다","알림", JOptionPane.ERROR_MESSAGE);
							SaveText("Error : break connection with server");
							os.close();
							is.close();
							dos.close();
							dis.close();
							socket.close();
						}catch(Exception e2) {}
						break;
					}//서버와 끊어졌을 경우, 모든 스트림과 소켓 닫기
				} // while문 끝 
				
			}
		});
		th.start();
	}//Connection() 함수 끝
	
	
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
								JOptionPane.showMessageDialog(null,"클라이언트 프로그램을 종료합니다.","알림",JOptionPane.ERROR_MESSAGE);
								os.close();
								is.close();
								dos.close();
								dis.close();
								socket.close();
								set_auto=false;
								System.out.println("시스템이 종료되었습니다.");
								System.exit(0);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						
							
						}
						
					});
				}
				
			});//창"X" 닫기 버튼 눌렀을 경우 스트림 해제
			clientController = loader.getController(); // 포트컨트롤러 load
			clientController.init(this);
			clientController.ID_Lbl.setText(ID);
			clientController.IP_Lbl.setText(IP);
			String port=PORT+"";
			clientController.Port_Lbl.setText(port);
			clientController.ColorDot.setFill(Color.BLUE);
		
			}catch(IOException e) {
			e.printStackTrace();
		}
	}//Start_Client()함수 끝
	
	public String Current_Date(String select) {
		Date today=new Date();
		SimpleDateFormat date=new SimpleDateFormat("yyyy/MM/dd");
		SimpleDateFormat time=new SimpleDateFormat("kk:mm:ss");//현재 날짜 시각 받아오기
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
			return "에러";
		}
	}//Current_Date()함수 끝
	
	private void InMessage(String str) {
		st = new StringTokenizer(str,"/");
		String protocol = st.nextToken();
		String message = st.nextToken();
		
		System.out.println("프로토콜: "+protocol+"/"+message);
	
		if(protocol.equals("Note")) {
			if(message.equals("Logout")) {
				clientController.move_Logout();
				SaveText("[Message] Logout client program.");
			}//강제로그아웃
			else if(message.equals("Quit")) {
				clientController.controlQuit();
				SaveText("[Message] Terminate client program");
			}//강제종료
			else{
				System.out.println("서버로부터 받은 메세지: "+message);
				SaveText("[Message] Server :"+message);
			}//서버의 메세지 받기
		}//Note 프로토콜 끝(서버의 메세지보내기 기능)
		else if(protocol.equals("CheckID")) {
			joinController.existID=message;
			System.out.println("서버로부터 아이디 중복확인 : "+message);
			SaveText("[Message] Server :"+ "Check exist ID ="+message);
		}//아이디 중복확인 프로토콜 끝
		else if(protocol.equals("Search")) {
			String Search_Password=st.nextToken();
			String Search_Program=st.nextToken();
			System.out.println("서버로부터 찾은 아이디: "+message);
			System.out.println("서버로부터 찾은 비밀번호: "+Search_Password);
			Search_Controller.Search_ID_TextField.setText(message);
			Search_Controller.Search_Pass_TextField.setText(Search_Password);
			Program=Search_Program;
			SaveText("[Message] Success search ID/Password");
		}//아이디,비번 찾기 프로토콜 끝
		else if(protocol.equals("Search Failed")) {
			Search_Controller.failSearch();
			SaveText("Error : Failed search ID/Password");
		}//아이디,비번 프로토콜 찾기 결과 = 실패 반환
		else if(protocol.equals("Request_FileSend")) {
			System.out.println("서버에서 파일전송요청이 있습니다");
			File_Send(); 
			SaveText("[Message]  Request file send");
		}//서버에서 getfile버튼 클릭시 실행되는 프로토콜끝

		else if(protocol.equals("Everyone")) {
			
			if(message.equals("Request_FileSend")) {
				System.out.println("전체메세지 : 서버에서 파일전송요청이 있습니다");
				File_Send();
				SaveText("[Message] Everyone: request file send");
			}//서버의 getfile 버튼 클릭시
			else if(message.equals("Server_Terminate")) {
				clientController.ColorDot.setFill(Color.RED);
				clientController.alarm();
				SaveText("[Message] Everyone: server terminate");
				
			}//서버가 종료 되었을 시
			else if(message.equals("FileSend_Auto")) {
				Period_time=st.nextToken();
				Period_txt=st.nextToken();
				FileSend_Auto();
				SaveText("[Message] Everyone: request file send auto");
			}//서버의 파일자동전송 기능사용
			else if(message.equals("FileSend_NotAuto")) {
				set_auto=false;
				SaveText("[Message] Everyone: stop file send auto");
			}//서버의 파일자동전송 취소 기능 사용
			else if(message.equals("Quit")) {
				clientController.ColorDot.setFill(Color.RED);
				Platform.runLater(()->{
				clientController.controlQuit();
				SaveText("[Message] Terminate client program");
				});
			}//서버 연결 끊길시 사용
			else if(message.equals("Logout")) {
				clientController.move_Logout();
				SaveText("[Message] Logout client program.");
			}//서버의 클라이언트 로그아웃 요청시 사용
			else {
				System.out.println("전체메세지: "+message);
				SaveText("[Message] Everyone: "+message);
			}//서버의 메세지 보내기기능 -> 모든 클라이언트에게 메세지 전송
			
		}//모든클라이언트에게 메세지 -> server의 broadcast(everyone)사용 프로토콜 끝
		else if(protocol.equals("All TinaMember")) {
			
			if(message.equals("Request_FileSend")) {
				System.out.println("All Tina메세지: 서버에서 파일전송요청이 있습니다");
				File_Send();
				SaveText("[Message] All TinaMember:"+"Request file send");
			}//서버의 파일전송요청
			else if(message.equals("FileSend_Auto")) {
				Period_time=st.nextToken();
				Period_txt=st.nextToken();
				FileSend_Auto();
				SaveText("[Message] All TinaMember:"+"Request auto file send");
			}//서버의 파일 자동전송 요청
			else if(message.equals("FileSend_NotAuto")) {
				set_auto=false;
				SaveText("[Message] All TinaMember:"+"Request stop atuo file send");
			}//서버의 파일 자동전송 취소 요청
			else if(message.equals("Quit")) {
				clientController.ColorDot.setFill(Color.RED);
				Platform.runLater(()->{
				clientController.controlQuit();
				SaveText("[Message] Terminate client program");
				});
			}//서버의 클라이언트 종료 메세지 전송시
			
		}//Tina 전 멤버에게 메세지 -> server의 broadcast(All TinaMember)사용 프로토콜 끝
		else if(protocol.equals("All NetMember")) {
			
			if(message.equals("Request_FileSend")) {
				System.out.println("All Net메세지: 서버에서 파일전송요청이 있습니다");
				File_Send();
				SaveText("[Message] All NetMember:Request file send");
			}//서버의 파일전송 요청
			else if(message.equals("FileSend_Auto")) {
				Period_time=st.nextToken();
				Period_txt=st.nextToken();
				FileSend_Auto();
				SaveText("[Message] All NetMember:Request auto file send");
			}//서버의 파일 자동 전송 요청
			else if(message.equals("FileSend_NotAuto")) {
				set_auto=false;
				SaveText("[Message] All NetMember:Request stop auto file send");
			}//서버의 파일 자동전송 요청 취소
			else if(message.equals("Quit")) {
				clientController.ColorDot.setFill(Color.RED);
				Platform.runLater(()->{
				clientController.controlQuit();
				SaveText("[Message] Terminate client program");
				});
			}//서버의 종료 메세지 요청
			
		}//Net 전 멤버에게 메세지 -> server의 broadcast(All NetMembeer)사용 프로토콜 끝
		
		else if(protocol.equals("Login")) {
			if(message.equals("Failed")) {
				JOptionPane.showMessageDialog(null,"정보가 올바르지 않습니다.","알림",JOptionPane.ERROR_MESSAGE);
				SaveText("Error :Login wrong ID/Password");
			}//로그인 실패일 경우
			else if(message.equals("Failed_Exist")) {
				JOptionPane.showMessageDialog(null,"이미 접속된 아이디 입니다.","알림",JOptionPane.ERROR_MESSAGE);
				SaveText("Error :Login with already connection ID");
			}//이미 접속 되어있는 아이디일 경우
			else if(message.equals("Success")) {
				String Client_Program=st.nextToken();
				Program=Client_Program;
				gotoLogin=true;
				SaveText("[Message] Login success");
			}//로그인 성공일 경우
		}//로그인 프로토콜 끝
		
		else if(protocol.equals("FileSend_Auto")) {
			// message => ~초 뒤에 실행
			// time => 시간 간격 설정 
			set_auto = true;
			Period_time=message;
			Period_txt=st.nextToken();
		
			FileSend_Auto();
			SaveText("[Message] :Request auto file send");
		}//파일자동전송 프로토콜 끝
		
		else if(protocol.equals("FileSend_NotAuto")) {
			set_auto=false;
			SaveText("[Message] :Request stop auto file send");
		}//파일자동전송 취소 프로토콜 끝
		
	}//Inmessage()함수 끝
	
	public void File_Send() {
		int DEFAULT_BUFFER_SIZE=10000; 
		File file = new File(FilePath); 		// 파일 경로 설정이 필요하다는 것 
		if(!file.exists()) {
			JOptionPane.showMessageDialog(null,"파일이 존재하지 않으므로 클라이언트에서 경로를 설정해주세요.","알림",JOptionPane.ERROR_MESSAGE);
			SaveText("Error : Not exist file");
			return ;
		}
		
		try {
			FileInputStream fis = new FileInputStream(file);
			long filesize = file.length(); // 파일 사이즈
			long totalReadBytes = 0;	   // 읽은 데이터 크기
			byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
			int readBytes;				   // 읽은 파일의 바이트 크기
			double startTime;			   // 전송 시작 시간 
			
			
			SaveText(ID+":"+"  Send File to server");
			startTime = System.currentTimeMillis(); // 시작 시간 설정
		
			readBytes = fis.read(buffer);	
			Send_Message("FileSend/"+Program+"/"+ID+"/"+file.getName()+"/"+readBytes+"/"+filesize);
			dos.write(buffer,0,readBytes);
			totalReadBytes +=readBytes;
			System.out.println("In progress: "+totalReadBytes+"/"+filesize+"Byte(s)("+
			(totalReadBytes*100/filesize)+"%)");	//진행률 확인
			
			
			System.out.println("File transfer completed");
			SaveText("Progress: "+(totalReadBytes*100/filesize)+"% Complete file transfer");
			double endTime = System.currentTimeMillis();		// 종료시간
			double diffTime = (endTime-startTime)/1000;			// 전송시간 
			double transferSpeed = (filesize/1000)/diffTime;	// 전송속도
			
			System.out.println("time: "+diffTime+"second(s)");
			System.out.println("Average transfer speed: "+transferSpeed+"KB/s");
			fis.close();										// 파일 스트림 닫기
		}catch(IOException e ) {
			System.out.println("서버와 연결중이 아닙니다");
			SaveText("Error : Not connection with server");
		}
	} // File_Send()끝
	
	public void FileSend_Auto() {
		set_auto=true;
		System.out.println("자동전송이 시작되었습니다");
		SaveText("[Auto file] : Start");
		Platform.runLater(()->{
			clientController.AutoLbl.setText("File sending  :  Auto ");							//자동 전송이 이루어지고 있는지 체크
			clientController.PeriodLbl.setText("Cycle  :  Every "+Period_time+" "+Period_txt);  //자동 전송 주기 체크
		});
		
		//쓰레드
	    m_timer = new Timer();
		m_task = new TimerTask() {
			@Override
			public void run() {
				System.out.println("Set auot값: "+set_auto);
				if(set_auto==true) {
					File_Send(); //자동전송 설정값이 true일 경우 파일 보내기기능 사용
				} //파일 자동 전송 끝
				
				else if(set_auto==false){
					m_timer.cancel(); // 클라이언트가 종료 => set_auto=false; *모든 예외처리 포함
					System.out.println("자동전송이 종료되었습니다");
					SaveText("[Auto file] : Stop");
					Platform.runLater(()->{
						clientController.AutoLbl.setText("File sending  :  Not Auto");
						clientController.PeriodLbl.setText("Cycle  :  (Not Auto)");
					});
					
				} //파일 자동 전송 해제 끝
			}
		};//TimerTask() 끝
		
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
		m_timer.schedule(m_task, 2000,time); // 서버에서 실행시간간격 (3번째변수)
		// m_task(실행 할 메소드), 2초뒤에실행, 2초마다 run()메소드 호출
	}
	
	public void Send_Message(String str) {
		try {
			dos.writeUTF(str);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,"서버와 연결 중이 아니거나 메세지를 전송하지 못하였습니다","알림",JOptionPane.ERROR_MESSAGE);
			SaveText("Error : Failed connection with server or Failed transmite message");
		}
	}//Send_Message() 함수 끝
	
	public void moveJoin() {
		if(IP.equals("")) {
			JOptionPane.showMessageDialog(null,"IP와 포트를 먼저 설정하세요","알림",JOptionPane.ERROR_MESSAGE);
			SaveText("Error : Set up the IP/Port first");
			return;
		}
		try {
			Stage primaryStage=(Stage)joinBtn.getScene().getWindow();
			Stage dialog=new Stage(StageStyle.UTILITY);
			dialog.initOwner(primaryStage);
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Join.fxml"));dialog.setTitle("회원 가입");
			Parent root = (Parent) loader.load();
			
			joinController = loader.getController(); // 포트컨트롤러 load
			joinController.Init(this);
			
			Stage stage = new Stage();
			stage.setTitle("Sign-up");
			stage.setScene(new Scene(root));
			stage.show();
			
		}catch(IOException e) {
			e.printStackTrace();
		}
		
	
	} //moveJoin()함수 끝

	public void moveSearch() {
		if(IP.equals("")) {
			JOptionPane.showMessageDialog(null,"IP와 포트를 먼저 설정하세요","알림",JOptionPane.ERROR_MESSAGE);
			SaveText("Error : Set up the IP/Port first");
			return;
		}
		try {
			Stage primaryStage=(Stage)Search_Btn.getScene().getWindow();
			Stage dialog=new Stage(StageStyle.UTILITY);
			dialog.initOwner(primaryStage);
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Search_UserInfo.fxml"));
			Parent root = (Parent) loader.load();
			
			Search_Controller = loader.getController(); // 포트컨트롤러 load
			Search_Controller.Init(this);
			
			Stage stage = new Stage();
			stage.setTitle("Find ID/Password");
			stage.setScene(new Scene(root));
			stage.show();
			
		}catch(IOException e) {
			e.printStackTrace();
		}
	}	//moveSearch()함수 끝 =아이디 비밀번호 찾기
	
	public void Set_UserInfo() {
		ID_TextField.setText(Join_Client_ID);
		Pass_TextField.setText(Join_Client_Password);
	}//Set_UserInfo()함수 끝 -> 회원가입시 기록했던 아이디,비번 로그인창에 셋팅
	
	//*************ClientLog view 에 메세지 저장****************
	public void SaveText(String message) {
		Date today=new Date();
		SimpleDateFormat date=new SimpleDateFormat("yyyy/MM/dd");
		SimpleDateFormat time=new SimpleDateFormat("kk:mm:ss");
		ob_Log_List.add(new TableRowDataModel_ClientLog(new SimpleIntegerProperty(count+1),new SimpleStringProperty(date.format(today)+" "+time.format(today)),new SimpleStringProperty(message)));
		count++;
		
	}//SaveText()함수 끝
	
	public void init(ClientController clientController2) {
		// TODO Auto-generated method stub
		clientController=clientController2;
	}
}
