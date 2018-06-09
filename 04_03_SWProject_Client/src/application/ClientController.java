package application;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;


import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ClientController implements Initializable{
	@FXML Label ID_Lbl;
	@FXML Label IP_Lbl;
	@FXML Label Port_Lbl;
	@FXML Label Time_Lbl;
	@FXML public Label PeriodLbl;				//자동설정 주기 확인라벨
	@FXML public Label AutoLbl; 				//자동설정 확인하는 라벨
	@FXML public Label FilePath_Lbl; 			//파일경로
	@FXML public Label CurrentTime_Lbl;			//현재 시각
	@FXML public AnchorPane Background_Color;	//바탕화면 색깔
	@FXML Button Update_Btn;
	@FXML Button Msg_Btn;
	@FXML Button FileChange_Btn;
	@FXML Button FileSend_Btn;
	@FXML Button Option_Btn;
	@FXML Button Terminate_Btn;
	@FXML Button LogShow_Btn;
	@FXML Button FilePath_Btn;
	@FXML Button Logout_Btn;
	@FXML Button Period_Btn;
	@FXML ImageView ImageView;					//광운대 로고
	@FXML Circle ColorDot;						//서버 상태 알림이
	
	//************ 시계 설정 함수 ****************
	private boolean stop;						//연결시간 설정 변수
	private boolean current_stop;				//현재시간 설정 변수
	public String first_time;	
	//**************************************
	//**************정보저장 ******************
	String ID;									//main에서 받은 아이디
	String IP,Port;								//main에서 받은 아이피,포트번호
	//**************************************
	
	//**************컨트롤러 ******************
	private MainController main;
	Send_Message_Controller send_message_controller;
	FilePath_Controller filePath_Controller;
	ClientOption_Controller clientOption_Controller;
	ClientLog_Controller clientLog_Controller;
	//**************************************
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		Show_Logo();								//로고 보여주기	
		
		long time1=System.currentTimeMillis()/1000;	//전송시간 start
		
		stop=false;									
		current_stop=false;
		
		Thread thread=new Thread() {
			public void run() {
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd  HH:mm:ss");
				while(!stop) {	
					String strTime=sdf.format(new Date());
					Platform.runLater(()->{
						CurrentTime_Lbl.setText(strTime);  //현재시간 출력
						long time2=System.currentTimeMillis()/1000;
						long hour,min,sec;
						hour=(time2-time1)/3600;
						min=(time2-time1)%3600/60;
						sec=(time2-time1)%3600%60;
						String timer_hour=String.format("%02d",hour);
						String timer_min=String.format("%02d", min);
						String timer_sec=String.format("%02d", sec);
						Time_Lbl.setText(timer_hour+":"+timer_min+":"+timer_sec); //전송시간 출력
						
					});
					try {Thread.sleep(100);} //1초 후 다시 thread
					catch(InterruptedException e){
				}
			}
			}
		};
		thread.setDaemon(true); //백그라운드로 동작
		thread.start();
	}//initalize()함수 끝 -> 서버와의 연결시간,현재시간 나타내줌
	
	
	private void Show_Logo() {
		// TODO Auto-generated method stub
		ImageView.setImage(new Image(getClass().getResourceAsStream("/Logo.jpg")));
	}//그림 삽입 추가


	@FXML public void move_Msg() {
		
		try {
			Stage primaryStage=(Stage)Msg_Btn.getScene().getWindow();
			Stage dialog=new Stage(StageStyle.UTILITY);
			dialog.initOwner(primaryStage);
			dialog.setTitle("메니지먼트 관리 시스템-경고");
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Send_Message.fxml"));
			Parent root = (Parent) loader.load();
			
			send_message_controller = loader.getController(); // 포트컨트롤러 load 
			send_message_controller.Init(main,this);
			Stage stage = new Stage();
			stage.setTitle("Sending message");
			stage.setScene(new Scene(root));
			stage.show();
			
		}catch(IOException e) {
			e.printStackTrace();
		}
	}//move_Msg()함수 끝 ->메세지 보내기
	
	@FXML public void move_FileChange() {
		
	}//move_FileChange()함수 끝 -> 파일 변환버튼 동작
	@FXML public void move_FileSend() {
		main.File_Send();
	}//move_FileSend()함수 끝 -> 파일 보내기 버튼 동작
	
	@FXML public void move_LogShow() {
		try {
			Stage primaryStage=(Stage)LogShow_Btn.getScene().getWindow();
			Stage dialog=new Stage(StageStyle.UTILITY);
			dialog.initOwner(primaryStage);
			FXMLLoader loader = new FXMLLoader(getClass().getResource("ClientLog.fxml"));
			Parent root = (Parent) loader.load();
			
			clientLog_Controller = loader.getController(); // 포트컨트롤러 load 
			clientLog_Controller.Init(main,this);
			Stage stage = new Stage();
			stage.setTitle("Log View");
			stage.setScene(new Scene(root));
			stage.show();
			
		}catch(IOException e) {
			e.printStackTrace();
		}
	}//move_LogShow()함수 끝 -> 로그 보기 버튼 동작
	
	@FXML public void move_Option() {

		try {
			Stage primaryStage=(Stage)Option_Btn.getScene().getWindow();
			Stage dialog=new Stage(StageStyle.UTILITY);
			dialog.initOwner(primaryStage);
			dialog.setTitle("Set background color");
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Client_Option.fxml"));
			Parent root = (Parent) loader.load();
			
			clientOption_Controller = loader.getController(); // 포트컨트롤러 load 
			clientOption_Controller.Init(main,this);
			Stage stage = new Stage();
			stage.setTitle("Set background color");
			stage.setScene(new Scene(root));
			stage.show();
			
		}catch(IOException e) {
			e.printStackTrace();
		}
	}//move_Option 함수 끝 -> 백그라운드 색상설정 버튼 동작
	
	@FXML public void move_Terminate() {
		try {
				if(!ColorDot.getFill().equals("Color.RED")) {
					System.out.println("Color red");
					main.Send_Message("Terminate/"+main.Program+"/"+main.ID);
				}
				JOptionPane.showMessageDialog(null,"클라이언트를 종료합니다","알림", JOptionPane.ERROR_MESSAGE);
				main.SaveText(main.ID+" :Terminate ");
				main.os.close();
				main.is.close();
				main.dos.close();
				main.dis.close();
				main.socket.close();
				main.set_auto=false;
				System.out.println("시스템이 종료되었습니다.");
				System.exit(0);
				//스트림,소켓 닫기
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				JOptionPane.showMessageDialog(null,"서버와 연결종료 에러","알림", JOptionPane.ERROR_MESSAGE);
				main.SaveText("Error :Failed terminate "+main.ID);
			}
			
	}//move_Terminate()함수 끝 -> 종료 버튼 동작
	
	@FXML public void move_FilePath() {
		
		FileChooser filechooser=new FileChooser();
		File file=filechooser.showOpenDialog(null);
		main.FilePath=file.getAbsolutePath();
		FilePath_Lbl.setText(file.getAbsolutePath());
		main.SaveText("Set up the filepath: "+main.FilePath);
		
	}//move_FilePath()함수 끝 -> 파일경로 버튼 동작
	
	
	
	public void move_Logout() {
		try {
			main.Send_Message("Terminate/"+main.Program+"/"+main.ID);
			JOptionPane.showMessageDialog(null,"클라이언트 프로그램이 로그아웃 되었습니다","알림", JOptionPane.ERROR_MESSAGE);
			main.SaveText(main.ID+" : Logout this client program");
			main.os.close();
			main.is.close();
			main.dos.close();
			main.dis.close();
			main.socket.close();
			main.set_auto=false;//자동전송해제,스트림,소켓 닫기
			Stage primaryStage=(Stage)Logout_Btn.getScene().getWindow();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
			Parent root = (Parent) loader.load();
			Scene scene=new Scene(root);
			primaryStage.setTitle("Client connector");
			primaryStage.setScene(scene);
			main = loader.getController(); // 포트컨트롤러 load
			main.init(this);
			main.ID="";
			main.PassWord="";
			main.ID_TextField.setPromptText("Please enter your ID");
			main.Pass_TextField.setPromptText("Please enter your Password");
			}catch(IOException e) {
			e.printStackTrace();
		}
	}//move_Logout()함수 끝 -> 로그아웃 버튼 클릭시 동작

	public void alarm() {
		// TODO Auto-generated method stub
		JOptionPane.showMessageDialog(null,"서버와의 연결이 끊어졌습니다","알림", JOptionPane.ERROR_MESSAGE);
		main.SaveText("Break connection with server");
		// 서버와 연결이 끊어졌으므로 기능사용 금지
		
		FileChange_Btn.setDisable(true);
		FileSend_Btn.setDisable(true);
		Msg_Btn.setDisable(true);
		LogShow_Btn.setDisable(true);
		Option_Btn.setDisable(true);
		FilePath_Btn.setDisable(true);
		Update_Btn.setDisable(true);
		
		stop=true;
		IP_Lbl.setText("X");
		Port_Lbl.setText("X");	
	}//alarm()함수 끝 -> 서버와의 연결 끊어질시 정보 초기화


	public void controlQuit() {
		// TODO Auto-generated method stub
		try {
			JOptionPane.showMessageDialog(null,"서버로 부터 강제종료 되었습니다","알림", JOptionPane.ERROR_MESSAGE);
			main.SaveText(main.ID+" :Terminate ");
			main.os.close();
			main.is.close();
			main.dos.close();
			main.dis.close();
			main.socket.close();
			main.set_auto=false;
			System.out.println("시스템이 종료되었습니다.");
			System.exit(1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,"서버와 연결종료 에러","알림", JOptionPane.ERROR_MESSAGE);
			main.SaveText("Error :Failed terminate "+main.ID);
		}
	}//controlQuit()함수 끝 -> 서버로부터 강제종료
	
	public void init(MainController main_controller) {
		main=main_controller;
	}//init()함수 끝
}
