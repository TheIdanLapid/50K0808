/**
 * 
 */
package mvvm.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mvvm.model.AdminModel;
import mvvm.viewmodel.DashboardViewModel;
import server.Handler;
import server.SokobanServer;

/**
 * @author שדמה
 * The main that runs the server
 */
public class App extends Application {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
    	SokobanServer server = new SokobanServer(12345, new Handler());
        try {
        	new Thread(new Runnable() {

				@Override
				public void run() {
					try {
				    	server.start();
				    	System.out.println("Server is open and waiting for clients...");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			
        	}).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        launch(args);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		
		AdminModel model = AdminModel.getInstance();
		
		DashboardViewModel vm = new DashboardViewModel(model);
		
		model.addObserver(vm);
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminDashboard.fxml"));
		Parent root = (Parent)loader.load();
		DashboardController controller = loader.getController();
		
        controller.setViewModel(vm);
        Scene scene = new Scene(root, 300, 275);
    
        stage.setTitle("Admin Dashboard");
        stage.setScene(scene);
        stage.show();
	}

}
