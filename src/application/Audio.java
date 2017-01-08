package application;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@Entity
@Table(name="audio")
public class Audio {

	private int id;	
	private StringProperty name = new SimpleStringProperty();	
	private StringProperty artist = new SimpleStringProperty();
	private StringProperty album = new SimpleStringProperty();
	private StringProperty genre = new SimpleStringProperty();
	private StringProperty path = new SimpleStringProperty();
	
	public Audio() {

	}
	
	public Audio(String name, String artist, String album, String genre, String path) {
		this.name = new SimpleStringProperty(name);
		this.artist = new SimpleStringProperty(artist);
		this.album = new SimpleStringProperty(album);
		this.genre = new SimpleStringProperty(genre);
		this.path = new SimpleStringProperty(path);
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="aid")
	public int getId() {
		return id;
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	@Column(name="aname")
	public String getName() {
		return name.get();
	}
	
	public void setName(String name) {
		this.name.set(name);
	}
	
	public StringProperty nameProperty() {
		return this.name;
	}
	
	@Column(name="artist")
	public String getArtist() {
		return artist.get();
	}
	
	public void setArtist(String artist) {
		this.artist.set(artist);
	}
	
	public StringProperty artistProperty() {
		return this.artist; 
	}
	
	@Column(name="album")
	public String getAlbum() {
		return album.get();
	}
	
	public void setAlbum(String album) {
		this.album.set(album);
	}
	
	public StringProperty albumProperty() {
		return this.album;
	}
	
	@Column(name="genre")
	public String getGenre() {
		return genre.get();
	}
	
	public void setGenre(String genre) {
		this.genre.set(genre);
	}
	
	public StringProperty genreProperty() {
		return this.genre;
	}
	
	@Column(name="path")
	public String getPath() {
		return path.get();
	}
	
	public void setPath(String path) {
		this.path.set(path);
	}
	
	public StringProperty pathProperty() {
		return this.path;
	}
	
	@Override
	public String toString() {
		return this.getName();
	}
}
