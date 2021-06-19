package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListController implements Initializable, DataChangeListener {

	// Atributos de referencias
	private DepartmentService service;

	@FXML
	private TableView<Department> tableViewDepartment;

	@FXML
	private TableColumn<Department, Integer> tableColumnId;

	@FXML
	private TableColumn<Department, String> tableColumnName;

	@FXML
	private TableColumn<Department, Department> tableColumnEDIT;

	@FXML
	private TableColumn<Department, Department> tableColumnRemove;

	@FXML
	private Button btNew;

	private ObservableList<Department> obsList;

	// tratamento de eventos button
	public void onBtnewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Department obj = new Department();// instanciando obj vazio
		createDialogForm(obj, "/gui/DepartmentForm.fxml", parentStage);
	}

	public void setDepartmentService(DepartmentService service) {
		this.service = service;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {

		/* inicia o comportamento das coluna */
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));

		/* Corrigindo a largura e altiura da view */
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());
	}

	public void updateTebleView() {
		if (service == null) {// teste p/ ter certesa de service não esta null
			throw new IllegalStateException("Service was null");
		}
		List<Department> list = service.findAll();// recuperando dados DP mock
		obsList = FXCollections.observableArrayList(list);// carregando list mock
		tableViewDepartment.setItems(obsList);// carregando os dados e mostrando na tela
		initEditButtons(); // edit
		initRemoveButtons();//remove
	}

	private void createDialogForm(Department obj, String absoluteName, Stage paraStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));// instanciando nova janela de
																						// dialogo
			Pane pene = loader.load();

			// injetando dados vazio para cadastro
			DepartmentFormController controller = loader.getController();
			controller.setDepartament(obj);
			controller.setDepartmentService(new DepartmentService());
			controller.subscribeDataChangeListener(this);// subescrevendo na tabela
			controller.updateFormData();

			// injetando dados da nova view
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter Department data");
			dialogStage.setScene(new Scene(pene));
			dialogStage.setResizable(false);
			dialogStage.initOwner(paraStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.show();
		} catch (IOException e) {
			Alerts.showAlert("IO Exception", "Erro loanding view", e.getMessage(), AlertType.ERROR);
		}
	}

	// metodo que atualiza os dados na tabela
	@Override
	public void onDataChenged() {
		updateTebleView();
	}

	// metodo que cria uma tela de cadastro ja com dadpos preenchidos automatico
	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Department, Department>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Department obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/DepartmentForm.fxml", Utils.currentStage(event)));
			}
		});
	}
	
	//metodo que cria um remove em cada linha,e chama o remove entityes
	private void initRemoveButtons() {
		tableColumnRemove.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnRemove.setCellFactory(param -> new TableCell<Department, Department>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(Department obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
		});
	}
	
	//função que remove entity conforme a resposta ok, mostrando arlert
	private void removeEntity(Department obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Are you sure to delete?");
	
		if(result.get() == ButtonType.OK) {
			if(service == null) {
				throw new IllegalStateException("Service was null");
			}
			try {
				service.remove(obj);
				updateTebleView();
			}
			catch (DbIntegrityException e) {
				Alerts.showAlert("Error removing object", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}
}
