package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;

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
		System.out.println("Ok About");
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rd) {		
		
	}
}
