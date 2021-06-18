package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentFormController implements Initializable{
	
	
	private Department entity;
	
	private DepartmentService service;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

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
	private void onBtSaveAction(ActionEvent event) {//salvando os dados no banco de dados
		if(entity == null) {//verifica se não está null
			throw new IllegalStateException("Entity was null");
		}
		if(service == null) {//verifica se não está null
			throw new IllegalStateException("Service was null");
		}
		try {
			entity = getFormData();
			service.saveOrUpdate(entity);//salva no banco de dados	
			notifyDataChengeListeners();//notifica que vai ser salvo algo
			Utils.currentStage(event).close();//fecha view
		}
		catch (DbException e) {
			Alerts.showAlert("Erro saving object", null, e.getMessage(),AlertType.ERROR);
		}
	}
	
	private void notifyDataChengeListeners() {//emetindo informações de evento na lista
		for(DataChangeListener listener : dataChangeListeners) {
			listener.onDataChenged();
		}		
	}

	private Department getFormData() {//pega os dados do formulario e retorna um novo obj
		Department obj = new Department();
		
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		obj.setName(txtName.getText());
		
		return obj;
	}
	
	@FXML
	private void onBtCancelAction(ActionEvent event) {
	Utils.currentStage(event).close();
	}
	
	//intanciando
	public void setDepartament(Department entity) {
		this.entity = entity;
	}
	
	public void setDepartmentService(DepartmentService service) {
		this.service = service;
	}
	
	public void subscribeDataChangeListener(DataChangeListener listener) {//add obj na lista de eventos
		dataChangeListeners.add(listener);
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
	
	public void updateFormData() {//metodo que popula as caixas de texto dos obj entity
		
		if(entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
	}
}
