package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class MainViewController implements Initializable {
	
	//Atributos
	@FXML
	private MenuItem menuItemSeller;

	@FXML
	private MenuItem menuItemDepartment;
	
	@FXML
	private MenuItem menuItemAbout;
	
	//metodos de tratar o menu
	@FXML
	public void onMenuItemSellerAction() {
		System.out.println("Ok Seller");
	}
	
	@FXML
	public void onMenuItemDepartment() {
		System.out.println("Ok Department");
	}
	
	@FXML
	public void onMenuItemAboutAction() {
		loadView("/gui/About.fxml");
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rd) {		
		
	}
	
	//função que abre outra tela
	private synchronized void loadView(String absoluteName) {
		try{
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVbox = loader.load();
			
			Scene mainScene = Main.getMainScene();
			VBox mainvBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();//Referencia para o VBox da janel aprincipal
			
			Node mainMenu = mainvBox.getChildren().get(0);// primeiro filho do vbox principal
			mainvBox.getChildren().clear();//Limpa todos os filhos do mainvbox
			mainvBox.getChildren().add(mainMenu);//add os filhos do vbox
			mainvBox.getChildren().addAll(newVbox.getChildren());//add filhos do newVbox
		}
		catch(IOException e) {
		Alerts.showAlert("IO Exception","Erro loading view", e.getMessage(), AlertType.ERROR);
		}
	}
}
