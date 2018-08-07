/**
 * 
 */
package mvvm.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import mvvm.viewmodel.DashboardViewModel;

/**
 * @author שדמה
 * A window controller for the admin gui
 */
public class DashboardController {

	    @FXML
	    private Button button;
	    
	    @FXML
	    private Label label;

	    @FXML
	    private ListView<String> myListView;

	    private DashboardViewModel vm;
	    
	    
	    @FXML
	    private void handleButtonAction(ActionEvent event) {
	    	myListView.getItems().remove(myListView.getSelectionModel().getSelectedItem());
	    }

	    public void setViewModel(DashboardViewModel viewmodel)
	    {
	    	vm = viewmodel;
	    	myListView.itemsProperty().bind(vm.clientsList);
	    	
	    }
}
