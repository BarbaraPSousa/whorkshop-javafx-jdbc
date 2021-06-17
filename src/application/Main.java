package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

public class Main extends Application {

	private static Scene mainScene;//atributo da sena
	
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainView.fxml"));// intanciando o metodo passando o caminho
			ScrollPane scrollPene = loader.load();
			
			//ajuste janela
			scrollPene.setFitToHeight(true);
			scrollPene.setFitToWidth(true);
			
			mainScene = new Scene(scrollPene);//instanciando a sena principal
			primaryStage.setScene(mainScene);
			primaryStage.setTitle("Sample JavaFX application");//titulo do palco
			primaryStage.show();//mostra o palco
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Scene getMainScene() {//metodo que passa a referencia da sena
		return mainScene;
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
