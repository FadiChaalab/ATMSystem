package application;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import utils.ConnectionsUtils;
import utils.Constante;

public class ProfileController implements Initializable{
	private double xOffset;
	private double yOffset;
	private boolean changed = false;
	private Stage atm;
	BufferedImage bf;
	FileInputStream fi;
	@FXML
	private Button btnsaveandexit;
	private Button btnclose;
	private Button btnminimize;
	private Button BtnDelete;
	@FXML
	private TextField phone;
	@FXML
	private TextField location;
	@FXML
	private TextField job;
	@FXML
	private Label amount;
	@FXML
	private TextField email;
	@FXML
	private TextField usr;
	@FXML
	private DatePicker birth;
	@FXML
	private ComboBox<String> gender;
	ObservableList<String> genderList = FXCollections.observableArrayList("Male", "Female");
	@FXML
	private ImageView pic;
	Connection connection = null;
    PreparedStatement preparedStatement = null;
    int result;
    private int  id;
	public ProfileController() {
        connection = ConnectionsUtils.connectDB();
    }
	@FXML
	private void btnClose(MouseEvent event) {				
		Stage s = (Stage) ((Node)event.getSource()).getScene().getWindow();
		s.close();
	}
	@FXML
	private void btnMinimize(MouseEvent event) {				
			Stage s = (Stage) ((Node)event.getSource()).getScene().getWindow();
			s.setIconified(true);
	}
	@FXML
	private void btnGetPic(MouseEvent event) throws IOException {
		Stage s = (Stage) ((Node)event.getSource()).getScene().getWindow();
		 FileChooser f = new FileChooser();
         FileChooser.ExtensionFilter ext1 = new FileChooser.ExtensionFilter("JPG files(*.jpg)","*.JPG");
         FileChooser.ExtensionFilter ext2 = new FileChooser.ExtensionFilter("PNG files(*.png)","*.PNG");
         f.getExtensionFilters().addAll(ext1,ext2);
         File file = f.showOpenDialog(s);
         bf = ImageIO.read(file);
         Image image = SwingFXUtils.toFXImage(bf, null);
         pic.setImage(image);
         fi = new FileInputStream(file);
         changed = true;
	}
	@FXML
	private void btnSaveAndExit(MouseEvent event) {	
			if(email.getText().isEmpty() == false) {
				if(validate(email.getText()) == false) {
					Platform.runLater(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							String path = Constante.valid;
							Media sound = new Media(new File(path).toURI().toString());
							MediaPlayer mediaPlayer = new MediaPlayer(sound);
							mediaPlayer.play();	
						}});	
				}
			}
			if(birth.getValue().toString().isEmpty() == false && phone.getText().isEmpty() == false && location.getText().isEmpty() == false && job.getText().isEmpty() == false && usr.getText().isEmpty() == false && email.getText().isEmpty() == false && validate(email.getText()) == true && changed == true) {
				String sql = "UPDATE users SET username= ?, useremail= ?, userphone= ?, userlocation= ?, userjob= ?, userbirthday= ?, usergender= ?, userpic= ? WHERE id= ?";	
				try {
					preparedStatement = connection.prepareStatement(sql);
					preparedStatement.setString(1, usr.getText());
					preparedStatement.setString(2, email.getText());
		            preparedStatement.setInt(3, Integer.parseInt(phone.getText()));
		            preparedStatement.setString(4, location.getText());
		            preparedStatement.setString(5, job.getText());
		            preparedStatement.setString(6, birth.getValue().toString());
		            preparedStatement.setString(7, gender.getValue());
		            preparedStatement.setBinaryStream(8, fi);
		            preparedStatement.setInt(9, id);
		            result = preparedStatement.executeUpdate();
		            if(result == 0){
		            	Platform.runLater(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								String path = Constante.error;
								Media sound = new Media(new File(path).toURI().toString());
								MediaPlayer mediaPlayer = new MediaPlayer(sound);
								mediaPlayer.play();	
							}});	
		            }else {
		            	System.out.println("success");
		            	Alert alert = new Alert(AlertType.INFORMATION);
		        		alert.setTitle("Save & Exit");
		        		alert.setHeaderText(null);
		        		alert.setContentText("Changes will be visible next time you log in");
		        		Optional<ButtonType> result = alert.showAndWait();
		        		if (result.get() == ButtonType.OK){
		        			Stage s = (Stage) ((Node)event.getSource()).getScene().getWindow();
		        			s.close();
		        		}
		            }
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}else {
				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						String path = Constante.fill;
						Media sound = new Media(new File(path).toURI().toString());
						MediaPlayer mediaPlayer = new MediaPlayer(sound);
						mediaPlayer.play();	
					}});	
			}
	}
	@FXML
	private void btnOnDelete(MouseEvent event) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Delete");
		alert.setHeaderText(null);
		alert.setContentText("Are you sure?");
		Optional<ButtonType> resultBox = alert.showAndWait();
		if (resultBox.get() == ButtonType.OK){
			String sql = "DELETE FROM users WHERE id= ?";	
			try {
				preparedStatement = connection.prepareStatement(sql);				
	            preparedStatement.setInt(1, id);
	            result = preparedStatement.executeUpdate();
	            if(result == 0){
	            	Platform.runLater(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							String path = Constante.error;
							Media sound = new Media(new File(path).toURI().toString());
							MediaPlayer mediaPlayer = new MediaPlayer(sound);
							mediaPlayer.play();	
						}});	
	            }else {
	            	Stage s = (Stage) ((Node)event.getSource()).getScene().getWindow();
	        		s.close();
	        		atm.close();
	        		FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));		            	
	            	Parent reset = loader.load();
	        		Scene scene = new Scene(reset);
	        		Stage ps = (Stage) ((Node)event.getSource()).getScene().getWindow();
	        		reset.setOnMousePressed(new EventHandler<MouseEvent>() {
	        			@Override
	        			public void handle(MouseEvent event) {
	        				// TODO Auto-generated method stub
	        				xOffset = event.getSceneX();
	        				yOffset = event.getSceneY();
	        			}
	        			
	        		});
	        		reset.setOnMouseDragged(new EventHandler<MouseEvent>() {
	        			@Override
	        			public void handle(MouseEvent event) {
	        				// TODO Auto-generated method stub
	        				s.setX(event.getScreenX() - xOffset);
	        				s.setY(event.getScreenY() - yOffset);
	        			}
	        			
	        		});
	        		ps.setScene(scene);
	        		ps.show();
	        		Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
	                ps.setX((primScreenBounds.getWidth() - ps.getWidth()) / 2);
	                ps.setY((primScreenBounds.getHeight() - ps.getHeight()) / 2);
	            }
			}catch(Exception ex) {
				ex.printStackTrace();
			}
		} else {
		    alert.close();
		}		
	}
	public static final Pattern VALIDEMAIL = 
	        Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

	    public static boolean validate(String email) {
	            Matcher matcher = VALIDEMAIL.matcher(email);
	            return matcher.find();
	    }
	public void transferProfile(String username, String useremail, int userphone, String userlocation, String userjob, String userbirthday, String usergender, int userbalance, Image userpic, int userid, Stage s) {
		usr.setText(username);
		email.setText(useremail);
		String p = String.valueOf(userphone);
		phone.setText(p);
		location.setText(userlocation);
		job.setText(userjob);
		LocalDate d = LocalDate.parse(userbirthday);
		birth.setValue(d);
		gender.setValue(usergender);
		String b = String.valueOf(userbalance);
		amount.setText("$" + b);
		pic.setImage(userpic);
		id = userid;
		atm = s;
	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		gender.setValue("Male");
		gender.setItems(genderList);
		
	}
	
}
