package gui;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entities.Department;
import model.entities.Seller;
import model.exception.ValidationException;
import model.services.DepartmentService;
import model.services.SellerService;

public class SellerFormController implements Initializable {

	// atributos
	private Seller entity;

	private SellerService service;

	private DepartmentService departmentService;

	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	// Atributo de referencia
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
	private ComboBox<Department> comboBoxDepartment;

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

	// Tratamento de evento de Button
	@FXML
	private void onBtSaveAction(ActionEvent event) {// salvando os dados no banco de dados
		if (entity == null) {// verifica se não está null
			throw new IllegalStateException("Entity was null");
		}
		if (service == null) {// verifica se não está null
			throw new IllegalStateException("Service was null");
		}
		try {
			entity = getFormData();
			service.saveOrUpdate(entity);// salva no banco de dados
			notifyDataChengeListeners();// notifica que vai ser salvo algo
			Utils.currentStage(event).close();// fecha view
		} catch (ValidationException e) {
			setErrorMassages(e.getErrors());
		} catch (DbException e) {
			Alerts.showAlert("Erro saving object", null, e.getMessage(), AlertType.ERROR);
		}
	}

	private void notifyDataChengeListeners() {// emetindo informações de evento na lista
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChenged();
		}
	}

	private Seller getFormData() {// pega os dados do formulario e retorna um novo obj
		Seller obj = new Seller();

		ValidationException exception = new ValidationException("Validation error");

		obj.setId(Utils.tryParseToInt(txtId.getText()));

		if (txtName.getText() == null || txtName.getText().trim().equals(" ")) {
			exception.addError("name", "Field can't be empty");
		}
		obj.setName(txtName.getText());

		if (txtEmail.getText() == null || txtEmail.getText().trim().equals(" ")) {
			exception.addError("email", "Field can't be empty");
		}
		obj.setEmail(txtEmail.getText());
	
		if(dpBirthDate.getValue() == null) {
			exception.addError("birthDate", "Field can't be empty");
		}
		else {
			Instant instant = Instant.from(dpBirthDate.getValue().atStartOfDay(ZoneId.systemDefault())); 
			obj.setBirthDate(Date.from(instant));
		}
		
		if (txtBaseSalary.getText() == null || txtBaseSalary.getText().trim().equals(" ")) {
			exception.addError("baseSalary","Field can't be empty");
		}
		obj.setBaseSalary(Utils.tryParseToDouble(txtBaseSalary.getText()));
		
		obj.setDepartment(comboBoxDepartment.getValue());
		
		if (exception.getErrors().size() > 0) {
			throw exception;
		}
		return obj;
	}

	@FXML
	private void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	private ObservableList<Department> obsList;

	// intanciando
	public void setSeller(Seller entity) {
		this.entity = entity;
	}

	public void setSellerServices(SellerService service, DepartmentService departmentService) {
		this.service = service;
		this.departmentService = departmentService;
	}

	public void subscribeDataChangeListener(DataChangeListener listener) {// add obj na lista de eventos
		dataChangeListeners.add(listener);
	}

	// Inicialização
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 80);
		Constraints.setTextFieldDouble(txtBaseSalary);
		Constraints.setTextFieldMaxLength(txtEmail, 100);
		Utils.formatDatePicker(dpBirthDate, "dd/MM/yyyy");
		
		initializeComboBoxDepartment();//inicia o comboBox
	}

	public void updateFormData() {// metodo que popula as caixas de texto dos obj entity
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
		txtEmail.setText(entity.getEmail());
		Locale.setDefault(Locale.US);
		txtBaseSalary.setText(String.format("%.2f", entity.getBaseSalary()));
		if (entity.getBirthDate() != null) {
			dpBirthDate.setValue(LocalDate.ofInstant(entity.getBirthDate().toInstant(), ZoneId.systemDefault()));// pega																											// usuario
		}
		if(entity.getDepartment() == null) {//cadastrando novo
			comboBoxDepartment.getSelectionModel().selectFirst();
		}
		else {
			comboBoxDepartment.setValue(entity.getDepartment());
		}	
	}

	//carrega os dp dos bancos de dados na list do comboBox
	public void loadAssociatedObjects() {
		if (departmentService == null) {
			throw new IllegalStateException("DepartamenteService was null");
		}
		List<Department> list = departmentService.findAll();
		obsList = FXCollections.observableArrayList(list);
		comboBoxDepartment.setItems(obsList);
	}

	// tratamento do campo erro
	private void setErrorMassages(Map<String, String> errors) {

		Set<String> fields = errors.keySet();

		labelErroName.setText((fields.contains("name") ? errors.get("name") : ""));
		labelErroEmail.setText((fields.contains("email") ? errors.get("email") : ""));
		labelErroBirthDate.setText((fields.contains("birthDate") ? errors.get("birthDate") : ""));
		labelErroBaseSalary.setText((fields.contains("baseSalary") ? errors.get("baseSalary") : ""));		
	}

	//metodo que inicializa o comboBox
	private void initializeComboBoxDepartment() {
		Callback<ListView<Department>, ListCell<Department>> factory = lv -> new ListCell<Department>() {
			@Override
			protected void updateItem(Department item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getName());
			}
		};
		comboBoxDepartment.setCellFactory(factory);
		comboBoxDepartment.setButtonCell(factory.call(null));
	}
}
