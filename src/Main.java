import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/views/Login.fxml"));
        primaryStage.setTitle("NSBM: Welcome");
        Scene scene = new Scene(root, 500, 500);
        scene.getStylesheets().add(getClass().getResource("/assets/css/login.css").toExternalForm());
//        scene.getStylesheets().add(getClass().getResource("/assets/css/bootstrap.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) { launch(args); }
}
