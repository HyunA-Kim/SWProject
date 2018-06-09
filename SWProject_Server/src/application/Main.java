package application;


import javafx.application.Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

	
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = (Parent)FXMLLoader.load(getClass().getResource("/view/Main.fxml"));
			Scene scene = new Scene(root,1236,426);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Server connector");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
