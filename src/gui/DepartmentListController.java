package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListController implements Initializable {

	//Atributos de referencias
	private DepartmentService service;
	
	@FXML
	private TableView<Department> tableViewDepartment;
	
	@FXML
	private TableColumn<Department, Integer> tableColumnId;
	
	@FXML
	private TableColumn<Department, String> tableColumnName;
	
	@FXML
	private Button btNew;
	
	private ObservableList<Department> obsList;
	
	// tratamento de eventos
	public void onBtnewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		createDialogForm("/gui/DepartmentForm.fxml", parentStage);
	}
	
	public void setDepartmentService(DepartmentService service) {
		this.service = service; 
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}
	
	private void initializeNodes() {
		
		/*inicia o comportamento das coluna*/
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		
		/*Corrigindo a largura e altiura da view*/
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());	
	}

	public void updateTebleView() {
		if(service == null) {//teste p/ ter certesa de service n�o esta null
			throw new IllegalStateException("Service was null");
		}		
		List<Department> list = service.findAll();//recuperando dados DP mock
		obsList=FXCollections.observableArrayList(list);//carregando list mock
		tableViewDepartment.setItems(obsList);//carregando os dados e mostrando na tela
	}
	
	private void createDialogForm(String absoluteName, Stage paraStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));//instanciando nova janela de dialogo
			Pane pene = loader.load();
			
			Stage dialogStage = new Stage();//novo palco na frente do outro
			dialogStage.setTitle("Enter Department data");//titulo da janela
			dialogStage.setScene(new Scene(pene));//sena do  o
			dialogStage.setResizable(false);//n�o deixa ser redimencionada
			dialogStage.initOwner(paraStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);//informa que a janaela vai ficar travada
			dialogStage.show();//iniciar
		}
		catch (IOException e) {
			Alerts.showAlert("IO Exception", "Erro loanding view", e.getMessage(), AlertType.ERROR);
		}
	}
}
