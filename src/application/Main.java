package application;
	
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.fxml.FXMLLoader;

public class Main extends Application {
	
	private Stage window;
	
	private MainController mc;

	//the aid used to persist audio
	private final int AID = 1;

	@Override
	public void start(Stage primaryStage) {
		try {
			System.out.println("Start!!! ");
			window = primaryStage;
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/Main.fxml"));
			
			Parent root = (Parent) loader.load();
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.show();
			
			//add connection here!
			mc = loader.getController();
			mc.setMain(this);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void stop(){
		
		Audio AudioToPersist = mc.getAudio();
		if(AudioToPersist==null) {
			AudioToPersist = new Audio(" ", " ", " ", " ", " ");
		}
		SessionFactory fac = mc.getFactory();
		Session session = fac.getCurrentSession();			
		Transaction trans = session.beginTransaction();
		try {
			
			session.createQuery("update Audio set "
								+"aname = :name,  "
								+"album = :album, "
								+"artist = :arti, "
								+"genre = :genre, "
								+"path = :path    "
								+"where aid = :int ")
			.setString("name", AudioToPersist.getName())
			.setString("album", AudioToPersist.getAlbum())
			.setString("arti", AudioToPersist.getArtist())
			.setString("genre", AudioToPersist.getGenre())
			.setString("path", AudioToPersist.getPath())
			.setInteger("int", AID)
			.executeUpdate();
			trans.commit();
		}
		finally {
			fac.close();
		}
	    // Save file
		System.out.println("closing!!!");
	}	
	
    public Stage getPrimaryStage() {
        return window;
    }	

    public int getAID() {
    	return AID;
    }
	
	public static void main(String[] args) {
		launch(args);
	}
}
