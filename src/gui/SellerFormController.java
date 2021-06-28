package gui;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Seller;
import model.exception.ValidationException;
import model.services.SellerService;

public class SellerFormController implements Initializable{
	
	
	private Seller entity;
	
	private SellerService service;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	//Atributo de referencia 
	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtName;
	
	@FXML
	private TextField txtEmail;
	
	@FXML
	private DatePicker dpBirthDate;
	
	@FXML
	private TextField txtBaseSalary;
	
	@FXML
	private Label labelErroName;
	
	@FXML
	private Label labelErroEmail;
	
	@FXML
	private Label labelErroBirthDate;
	
	@FXML
	private Label labelErroBaseSalary;

	@FXML
	private Button btSeve;
	
	@FXML
	private Button btCancel;
	
	//Tratamento de evento de Button
	@FXML
	private void onBtSaveAction(ActionEvent event) {//salvando os dados no banco de dados
		if(entity == null) {//verifica se n�o est� null
			throw new IllegalStateException("Entity was null");
		}
		if(service == null) {//verifica se n�o est� null
			throw new IllegalStateException("Service was null");
		}
		try {
			entity = getFormData();
			service.saveOrUpdate(entity);//salva no banco de dados	
			notifyDataChengeListeners();//notifica que vai ser salvo algo
			Utils.currentStage(event).close();//fecha view
		}
		catch (ValidationException e) {
			setErrorMassages(e.getErrors());
		}
		catch (DbException e) {
			Alerts.showAlert("Erro saving object", null, e.getMessage(),AlertType.ERROR);
		}
	}
	
	private void notifyDataChengeListeners() {//emetindo informa��es de evento na lista
		for(DataChangeListener listener : dataChangeListeners) {
			listener.onDataChenged();
		}		
	}

	private Seller getFormData() {//pega os dados do formulario e retorna um novo obj
		Seller obj = new Seller();
		
		ValidationException exception = new ValidationException("Validation error");
		
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		
		if(txtName.getText() == null || txtName.getText().trim().equals(" ")) {
			exception.addError("name", "Field can't be empty");
		}
		obj.setName(txtName.getText());
		
		if(exception.getErrors().size() > 0) {
			throw exception;
		}
		return obj;
	}
	
	@FXML
	private void onBtCancelAction(ActionEvent event) {
	Utils.currentStage(event).close();
	}
	
	//intanciando
	public void setDepartament(Seller entity) {
		this.entity = entity;
	}
	
	public void setSellerService(SellerService service) {
		this.service = service;
	}
	
	public void subscribeDataChangeListener(DataChangeListener listener) {//add obj na lista de eventos
		dataChangeListeners.add(listener);
	}
	
	//Inicializa��o
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}
	
	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 80);
		Constraints.setTextFieldDouble(txtBaseSalary);
		Constraints.setTextFieldMaxLength(txtEmail, 70);
		Utils.formatDatePicker(dpBirthDate, "dd/MM/yyyy");
	}
	
	public void updateFormData() {//metodo que popula as caixas de texto dos obj entity
		
		if(entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
		txtEmail.setText(entity.getEmail());
		Locale.setDefault(Locale.US);
		txtBaseSalary.setText(String.format("%.2f", entity.getBaseSalary()));
		if(entity.getBirthDate() != null) {
			dpBirthDate.setValue(LocalDate.ofInstant(entity.getBirthDate().toInstant(), ZoneId.systemDefault()));//pega a data e fuso horario do computador do usuario
		}
		
	}
	
	//tratamento do campo erro 
	private void setErrorMassages(Map<String, String> errors) {
		
		Set<String> fields = errors.keySet();
		
		if(fields.contains("name")) {
			labelErroName.setText(errors.get("name"));
		}
	}
}