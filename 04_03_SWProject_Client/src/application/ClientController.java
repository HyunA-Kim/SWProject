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
	@FXML public Label PeriodLbl;				//�ڵ����� �ֱ� Ȯ�ζ�
	@FXML public Label AutoLbl; 				//�ڵ����� Ȯ���ϴ� ��
	@FXML public Label FilePath_Lbl; 			//���ϰ��
	@FXML public Label CurrentTime_Lbl;			//���� �ð�
	@FXML public AnchorPane Background_Color;	//����ȭ�� ����
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
	@FXML ImageView ImageView;					//����� �ΰ�
	@FXML Circle ColorDot;						//���� ���� �˸���
	
	//************ �ð� ���� �Լ� ****************
	private boolean stop;						//����ð� ���� ����
	private boolean current_stop;				//����ð� ���� ����
	public String first_time;	
	//**************************************
	//**************�������� ******************
	String ID;									//main���� ���� ���̵�
	String IP,Port;								//main���� ���� ������,��Ʈ��ȣ
	//**************************************
	
	//**************��Ʈ�ѷ� ******************
	private MainController main;
	Send_Message_Controller send_message_controller;
	FilePath_Controller filePath_Controller;
	ClientOption_Controller clientOption_Controller;
	ClientLog_Controller clientLog_Controller;
	//**************************************
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		Show_Logo();								//�ΰ� �����ֱ�	
		
		long time1=System.currentTimeMillis()/1000;	//���۽ð� start
		
		stop=false;									
		current_stop=false;
		
		Thread thread=new Thread() {
			public void run() {
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd  HH:mm:ss");
				while(!stop) {	
					String strTime=sdf.format(new Date());
					Platform.runLater(()->{
						CurrentTime_Lbl.setText(strTime);  //����ð� ���
						long time2=System.currentTimeMillis()/1000;
						long hour,min,sec;
						hour=(time2-time1)/3600;
						min=(time2-time1)%3600/60;
						sec=(time2-time1)%3600%60;
						String timer_hour=String.format("%02d",hour);
						String timer_min=String.format("%02d", min);
						String timer_sec=String.format("%02d", sec);
						Time_Lbl.setText(timer_hour+":"+timer_min+":"+timer_sec); //���۽ð� ���
						
					});
					try {Thread.sleep(100);} //1�� �� �ٽ� thread
					catch(InterruptedException e){
				}
			}
			}
		};
		thread.setDaemon(true); //��׶���� ����
		thread.start();
	}//initalize()�Լ� �� -> �������� ����ð�,����ð� ��Ÿ����
	
	
	private void Show_Logo() {
		// TODO Auto-generated method stub
		ImageView.setImage(new Image(getClass().getResourceAsStream("/Logo.jpg")));
	}//�׸� ���� �߰�


	@FXML public void move_Msg() {
		
		try {
			Stage primaryStage=(Stage)Msg_Btn.getScene().getWindow();
			Stage dialog=new Stage(StageStyle.UTILITY);
			dialog.initOwner(primaryStage);
			dialog.setTitle("�޴�����Ʈ ���� �ý���-���");
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Send_Message.fxml"));
			Parent root = (Parent) loader.load();
			
			send_message_controller = loader.getController(); // ��Ʈ��Ʈ�ѷ� load 
			send_message_controller.Init(main,this);
			Stage stage = new Stage();
			stage.setTitle("Sending message");
			stage.setScene(new Scene(root));
			stage.show();
			
		}catch(IOException e) {
			e.printStackTrace();
		}
	}//move_Msg()�Լ� �� ->�޼��� ������
	
	@FXML public void move_FileChange() {
		
	}//move_FileChange()�Լ� �� -> ���� ��ȯ��ư ����
	@FXML public void move_FileSend() {
		main.File_Send();
	}//move_FileSend()�Լ� �� -> ���� ������ ��ư ����
	
	@FXML public void move_LogShow() {
		try {
			Stage primaryStage=(Stage)LogShow_Btn.getScene().getWindow();
			Stage dialog=new Stage(StageStyle.UTILITY);
			dialog.initOwner(primaryStage);
			FXMLLoader loader = new FXMLLoader(getClass().getResource("ClientLog.fxml"));
			Parent root = (Parent) loader.load();
			
			clientLog_Controller = loader.getController(); // ��Ʈ��Ʈ�ѷ� load 
			clientLog_Controller.Init(main,this);
			Stage stage = new Stage();
			stage.setTitle("Log View");
			stage.setScene(new Scene(root));
			stage.show();
			
		}catch(IOException e) {
			e.printStackTrace();
		}
	}//move_LogShow()�Լ� �� -> �α� ���� ��ư ����
	
	@FXML public void move_Option() {

		try {
			Stage primaryStage=(Stage)Option_Btn.getScene().getWindow();
			Stage dialog=new Stage(StageStyle.UTILITY);
			dialog.initOwner(primaryStage);
			dialog.setTitle("Set background color");
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Client_Option.fxml"));
			Parent root = (Parent) loader.load();
			
			clientOption_Controller = loader.getController(); // ��Ʈ��Ʈ�ѷ� load 
			clientOption_Controller.Init(main,this);
			Stage stage = new Stage();
			stage.setTitle("Set background color");
			stage.setScene(new Scene(root));
			stage.show();
			
		}catch(IOException e) {
			e.printStackTrace();
		}
	}//move_Option �Լ� �� -> ��׶��� ������ ��ư ����
	
	@FXML public void move_Terminate() {
		try {
				if(!ColorDot.getFill().equals("Color.RED")) {
					System.out.println("Color red");
					main.Send_Message("Terminate/"+main.Program+"/"+main.ID);
				}
				JOptionPane.showMessageDialog(null,"Ŭ���̾�Ʈ�� �����մϴ�","�˸�", JOptionPane.ERROR_MESSAGE);
				main.SaveText(main.ID+" :Terminate ");
				main.os.close();
				main.is.close();
				main.dos.close();
				main.dis.close();
				main.socket.close();
				main.set_auto=false;
				System.out.println("�ý����� ����Ǿ����ϴ�.");
				System.exit(0);
				//��Ʈ��,���� �ݱ�
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				JOptionPane.showMessageDialog(null,"������ �������� ����","�˸�", JOptionPane.ERROR_MESSAGE);
				main.SaveText("Error :Failed terminate "+main.ID);
			}
			
	}//move_Terminate()�Լ� �� -> ���� ��ư ����
	
	@FXML public void move_FilePath() {
		
		FileChooser filechooser=new FileChooser();
		File file=filechooser.showOpenDialog(null);
		main.FilePath=file.getAbsolutePath();
		FilePath_Lbl.setText(file.getAbsolutePath());
		main.SaveText("Set up the filepath: "+main.FilePath);
		
	}//move_FilePath()�Լ� �� -> ���ϰ�� ��ư ����
	
	
	
	public void move_Logout() {
		try {
			main.Send_Message("Terminate/"+main.Program+"/"+main.ID);
			JOptionPane.showMessageDialog(null,"Ŭ���̾�Ʈ ���α׷��� �α׾ƿ� �Ǿ����ϴ�","�˸�", JOptionPane.ERROR_MESSAGE);
			main.SaveText(main.ID+" : Logout this client program");
			main.os.close();
			main.is.close();
			main.dos.close();
			main.dis.close();
			main.socket.close();
			main.set_auto=false;//�ڵ���������,��Ʈ��,���� �ݱ�
			Stage primaryStage=(Stage)Logout_Btn.getScene().getWindow();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
			Parent root = (Parent) loader.load();
			Scene scene=new Scene(root);
			primaryStage.setTitle("Client connector");
			primaryStage.setScene(scene);
			main = loader.getController(); // ��Ʈ��Ʈ�ѷ� load
			main.init(this);
			main.ID="";
			main.PassWord="";
			main.ID_TextField.setPromptText("Please enter your ID");
			main.Pass_TextField.setPromptText("Please enter your Password");
			}catch(IOException e) {
			e.printStackTrace();
		}
	}//move_Logout()�Լ� �� -> �α׾ƿ� ��ư Ŭ���� ����

	public void alarm() {
		// TODO Auto-generated method stub
		JOptionPane.showMessageDialog(null,"�������� ������ ���������ϴ�","�˸�", JOptionPane.ERROR_MESSAGE);
		main.SaveText("Break connection with server");
		// ������ ������ ���������Ƿ� ��ɻ�� ����
		
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
	}//alarm()�Լ� �� -> �������� ���� �������� ���� �ʱ�ȭ


	public void controlQuit() {
		// TODO Auto-generated method stub
		try {
			JOptionPane.showMessageDialog(null,"������ ���� �������� �Ǿ����ϴ�","�˸�", JOptionPane.ERROR_MESSAGE);
			main.SaveText(main.ID+" :Terminate ");
			main.os.close();
			main.is.close();
			main.dos.close();
			main.dis.close();
			main.socket.close();
			main.set_auto=false;
			System.out.println("�ý����� ����Ǿ����ϴ�.");
			System.exit(1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,"������ �������� ����","�˸�", JOptionPane.ERROR_MESSAGE);
			main.SaveText("Error :Failed terminate "+main.ID);
		}
	}//controlQuit()�Լ� �� -> �����κ��� ��������
	
	public void init(MainController main_controller) {
		main=main_controller;
	}//init()�Լ� ��
}
