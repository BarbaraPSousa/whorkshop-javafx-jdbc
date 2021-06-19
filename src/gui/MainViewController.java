package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

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
import model.services.DepartmentService;
import model.services.SellerService;

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
		loadView("/gui/SellerList.fxml", (SellerListController controller) -> {
			controller.setSellerService(new SellerService());
			controller.updateTebleView();
		});
	}
	
	
	@FXML
	public void onMenuItemDepartment() {
		loadView("/gui/DepartmentList.fxml", (DepartmentListController controller) -> {
			controller.setDepartmentService(new DepartmentService());
			controller.updateTebleView();
		});
	}
	
	@FXML
	public void onMenuItemAboutAction() {
		loadView("/gui/About.fxml", x -> {});
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rd) {		
		
	}
	
	//função que abre outra tela
	private synchronized <T> void loadView(String absoluteName, Consumer<T> initializinAction) {
		try{
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVbox = loader.load();
			
			Scene mainScene = Main.getMainScene();
			VBox mainvBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();//Referencia para o VBox da janel aprincipal
			
			Node mainMenu = mainvBox.getChildren().get(0);// primeiro filho do vbox principal
			mainvBox.getChildren().clear();//Limpa todos os filhos do mainvbox
			mainvBox.getChildren().add(mainMenu);//add os filhos do vbox
			mainvBox.getChildren().addAll(newVbox.getChildren());//add filhos do newVbox
			
			//executa a função lambda da inicialização
			T controller = loader.getController();
			initializinAction.accept(controller);
		}
		catch(IOException e) {
		Alerts.showAlert("IO Exception","Erro loading view", e.getMessage(), AlertType.ERROR);
		}
	}
}
