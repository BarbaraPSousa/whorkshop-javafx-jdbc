package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainView.fxml"));// intanciando o metodo passando o caminho
			Parent parent = loader.load();
			
			Scene mainScene = new Scene(parent);//instanciando a sena principal
			primaryStage.setScene(mainScene);
			primaryStage.setTitle("Sample JavaFX application");//titulo do palco
			primaryStage.show();//mostra o palco
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
