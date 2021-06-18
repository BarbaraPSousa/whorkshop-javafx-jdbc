package gui.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Utils {

	public static Stage currentStage(ActionEvent event) {// evento que o botão recebeu
		return (Stage) ((Node) event.getSource()).getScene().getWindow();
	}
	
	public static Integer tryParseToInt(String str) {//retorna o str para converter para inteiro
		try {
			return Integer.parseInt(str);
		}
		catch(NumberFormatException e) {//retornando null na exceçao 
			return null;
		}
	}
}
