package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class DepartmentFormController implements Initializable{
	
	//Atributo de referencia 
	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtName;
	
	@FXML
	private Label labelErroName;
	
	@FXML
	private Button btSeve;
	
	@FXML
	private Button btCancel;
	
	//Tratamento de evento de Button
	@FXML
	private void onBtSaveAction() {
		System.out.println("ok Save");
	}
	
	@FXML
	private void onBtCancelAction() {
		System.out.println("ok Cancel");
	}
	
	//Inicialização
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}
	
	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 30);
	}
}
