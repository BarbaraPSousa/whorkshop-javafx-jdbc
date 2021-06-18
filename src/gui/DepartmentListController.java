package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entites.Department;
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
	public void onBtnewAction() {
		System.out.println("Ok New");
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
}
