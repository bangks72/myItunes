package application;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import org.apache.tika.exception.TikaException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.xml.sax.SAXException;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;

public class MainController {
	
	@FXML
	private Button btn1;
	
	@FXML
	private Button btn2;
	
	@FXML
	private Button btn3;
	
	@FXML
	private ListView<Audio> listview;
	
	@FXML
	private Label NameLabel;
	
	@FXML
	private Label ArtistLabel;
	
	@FXML
	private Label AlbumLabel;
	
	@FXML
	private Label GenreLabel;
	
	private Audio AudioToPersist;
	
	private SessionFactory factory;
	
	private Main main;
	
		
	public void Button1Action(ActionEvent event) throws Exception, IOException, SAXException, TikaException {
		FileChooser fc = new FileChooser();
		fc.setInitialDirectory(new File("E:\\music"));
		//fc.getExtensionFilters().addAll(new ExtensionFilter("PDF FILES", "*.pdf"));
		File selectedFile = fc.showOpenDialog(null);		
		addAudio(selectedFile);
	}

	private void addAudio(File selectedFile) throws Exception, IOException, SAXException, TikaException {
		if(selectedFile!=null) {
			Mp3Parse mp3parse = new Mp3Parse(selectedFile);
			Audio audio = mp3parse.Parse();
			removeAud(audio);
			
			listview.getItems().add(audio);
			
			Session session = factory.getCurrentSession();			
			Transaction trans = session.beginTransaction();	
			
			try {
				session.createQuery("delete Audio where "
									+"path = :path and aid != :aid")
									.setString("path", audio.getPath())
									.setInteger("aid", main.getAID())
									.executeUpdate();
				session.save(audio);
				trans.commit();
			}
			finally {

			}
			
		}else {
			System.out.println("File is not valid");
		}
	}

	private void removeAud(Audio audio) {
		for(int i = 0; i<listview.getItems().size(); i++) {
			if(listview.getItems().get(i).getPath().equals(audio.getPath())) {
				listview.getItems().remove(i);
				break;
			}
		}
	}
	
	public void Button2Action(ActionEvent event) throws Exception, IOException, SAXException, TikaException {
		FileChooser fc = new FileChooser();
		//fc.setInitialDirectory(new File("E:\\work"));
		//fc.getExtensionFilters().addAll(new ExtensionFilter("PDF FILES", "*.pdf"));
		List<File> selectedFile = fc.showOpenMultipleDialog(null);
		
		if(selectedFile!=null) {
			for(int i = 0; i<selectedFile.size(); i++){
				addAudio(selectedFile.get(i));
			}
			
		} else {
			System.out.println("Files are not valid");
		}		
	}
	
	public void handleDeleteAudio(ActionEvent event) {
		int selectedIndex = listview.getSelectionModel().getSelectedIndex();
		if(selectedIndex >= 0) {
			Audio aud = listview.getItems().get(selectedIndex);
			removeAud(aud);	
			
			Session session = factory.getCurrentSession();			
			Transaction trans = session.beginTransaction();	
			
			try {
				session.createQuery("delete Audio where "
									+"path = :path and aid != :aid")
									.setString("path", aud.getPath())
									.setInteger("aid", main.getAID())
									.executeUpdate();
				trans.commit();
			}
			finally {

			}			
			
		}else {
            // Nothing selected.
			System.out.println("Nothing selected");
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(main.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Audio Selected");
            alert.setContentText("Please select an audio in the table.");

            alert.showAndWait();
        }
	}
	
	private void showAudioDetails(Audio audio){
	    if (audio != null) {
	        NameLabel.setText(audio.getName());
	        ArtistLabel.setText(audio.getArtist());
	        AlbumLabel.setText(audio.getAlbum());
	        GenreLabel.setText(audio.getGenre());
	        AudioToPersist = new Audio(audio.getName(), audio.getArtist(), audio.getAlbum(), audio.getGenre(), audio.getPath());
	    }else {
	        NameLabel.setText("");
	        ArtistLabel.setText("");
	        AlbumLabel.setText("");
	        GenreLabel.setText("");
	    }
	}
		
	@FXML
	private void initialize() {
		factory = new Configuration()
				.configure("hibernate.cfg.xml")
				.addAnnotatedClass(Audio.class)
				.buildSessionFactory();		
		
		ConnectDataBase();
			
		List<Audio> ob = getPersistentList();
		setListView(ob);
		
		if(ob!=null&&ob.size()!=0) {
			AudioToPersist = listview.getItems().remove(0);		
		}
		
		showAudioDetails(AudioToPersist);
		listview.getSelectionModel().selectedItemProperty().addListener(
	          (observable, oldValue, newValue) -> showAudioDetails((Audio) newValue));
	}
	
	public void setMain(Main main) {
		this.main = main;
	}
	
	public ListView<Audio> getListView(){
		return listview;
	}
	
	public void setListView(List<Audio> listview) {
		if(listview==null) return;
		ObservableList<Audio> ob = this.listview.getItems();
		for(int i = 0; i<listview.size(); i++) {
			ob.add(listview.get(i));
		}
	}
	
	public Audio getAudio() {
		return AudioToPersist;
	}
	
	public void setAudio(Audio audio) {
		if(audio==null) return;
		AudioToPersist = new Audio(audio.getName(), audio.getArtist(), audio.getAlbum(), audio.getGenre(),audio.getPath());
	}
	
	public SessionFactory getFactory() {
		return factory;
	}
	
	private void ConnectDataBase() {
		String jdbcUrl = "jdbc:mysql://localhost:3306/itunes?useSSL=false";
		String user = "root";
		String pass = "";
		
		try{
			System.out.println("Connecting to datatbase: "+jdbcUrl);
			
			Connection myConn = DriverManager.getConnection(jdbcUrl, user, pass);
			
			System.out.println("Connection successful!");
		}
		catch(Exception exc){
			exc.printStackTrace();
		}
	}
	
	private List<Audio> getPersistentList() {
		SessionFactory factory = new Configuration()
				.configure("hibernate.cfg.xml")
				.addAnnotatedClass(Audio.class)
				.buildSessionFactory();
		Session session = factory.getCurrentSession();			
		Transaction trans = session.beginTransaction();	
		
		try {
			List<Audio> list = session.createQuery("from Audio order by id").list();
			trans.commit();
			return list;
		}
		finally {
			factory.close();
		}		
	}
}
