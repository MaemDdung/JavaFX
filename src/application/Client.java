package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class Client extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Client.fxml"));
			BorderPane root = (BorderPane)loader.load();
			ClientController cc = (ClientController)loader.getController();
			Scene scene = new Scene(root,600,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setTitle("JavaFX Chat");
			primaryStage.setScene(scene);
			primaryStage.show();
			
			// 창 종료시 실행
			primaryStage.setOnCloseRequest(event -> cc.closeAll());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
