package application;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Screen;
import javafx.stage.Stage;
import utils.ConnectionsUtils;
import utils.Constante;
import utils.CryptwithMD5;
import utils.Person;

public class MainController implements Initializable{
	private double xOffset;
	private double yOffset;
	Person p = new Person(0, "", "", 0, "", "", "", "", 0, null, 0, 0);
	@FXML
	private Button BtnLoginSwitcher;
	private Button BtnSignupSwitcher;
	private Button BtnResetSwitcher;
	private Button btnclose;
	private Button btnminimize;
	private Button btnlogin;
	private Button btnsignup;
	@FXML
	private AnchorPane login_pn;
	@FXML
	private AnchorPane signup_pn;
	@FXML
	private TextField usr, email, usr2;
	@FXML
	private PasswordField pwd, pwd2;
	@FXML
	private CheckBox accept;
	Connection connection = null;
    PreparedStatement preparedStatement = null;
    PreparedStatement ps = null;
    int result;
    ResultSet resultSet = null;
 
    public MainController() {
        connection = ConnectionsUtils.connectDB();
    }
	@FXML
	private void btnLoginSwitcher(MouseEvent event) {		
			signup_pn.setOpacity(0.0);
			login_pn.setOpacity(1.0);
			login_pn.toFront();
	}
	@FXML
	private void btnSignupSwitcher(MouseEvent event) {				
		login_pn.setOpacity(0.0);
		signup_pn.setOpacity(1.0);
		signup_pn.toFront();
	}
	@FXML
	private void btnResetSwitcher(MouseEvent event) throws IOException{				
		Parent reset = FXMLLoader.load(getClass().getResource("Reset.fxml"));
		Scene scene = new Scene(reset);
		Stage s = (Stage) ((Node)event.getSource()).getScene().getWindow();
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
		s.setScene(scene);
		s.show();
		Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        s.setX((primScreenBounds.getWidth() - s.getWidth()) / 2);
        s.setY((primScreenBounds.getHeight() - s.getHeight()) / 2);
	}
	@FXML
	private void btnClose(MouseEvent event) {				
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Exit");
		alert.setHeaderText(null);
		alert.setContentText("Are you sure?");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			Stage s = (Stage) ((Node)event.getSource()).getScene().getWindow();
			s.close();
		} else {
		    alert.close();
		}	
	}
	@FXML
	private void btnMinimize(MouseEvent event) {				
			Stage s = (Stage) ((Node)event.getSource()).getScene().getWindow();
			s.setIconified(true);
	}
	@FXML
	private void btnLogin(MouseEvent event) {				
			if(usr.getText().isEmpty() || pwd.getText().isEmpty()) {
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
			if(usr.getText().isEmpty() == false && pwd.getText().isEmpty() == false) {
				String sql = "SELECT * FROM users WHERE useremail=? AND userpwd=?";
		        
		        try{
		        	String pwdcrypted = CryptwithMD5.cryptPwdWithMD5(pwd.getText());
		            preparedStatement = connection.prepareStatement(sql);
		            preparedStatement.setString(1, usr.getText());
		            preparedStatement.setString(2, pwdcrypted);
		            resultSet = preparedStatement.executeQuery();
		            if(!resultSet.next()){
		            	Alert alert = new Alert(AlertType.WARNING);
						alert.setTitle("Fetching...");
						alert.setHeaderText(null);
						alert.setContentText("email or password incorrect.");
						alert.showAndWait();
		            }else{
		            	int first;
		            	InputStream is = null;
		            	p.setId(resultSet.getInt("id"));
		            	p.setName(resultSet.getString("username"));
		            	p.setEmail(resultSet.getString("useremail"));
		            	p.setPhone(resultSet.getInt("userphone"));
		            	p.setGender(resultSet.getString("usergender"));
		            	p.setLocation(resultSet.getString("userlocation"));
		            	p.setJob(resultSet.getString("userjob"));
		            	p.setBirthday(resultSet.getString("userbirthday"));
		            	p.setBalance(resultSet.getInt("userbalance"));
		            	p.setCard(resultSet.getInt("usercard"));
		            	p.setPin(resultSet.getInt("userpin"));
		            	is = resultSet.getBinaryStream("userpic");
		            	if(is != null) {
		            		BufferedImage bf = ImageIO.read(is);
			            	Image image = SwingFXUtils.toFXImage(bf, null);
			            	p.setPic(image);
		            	}		            	
		            	first = resultSet.getInt("firsttime");		            	
		            	if(first == 1) {
		            		FXMLLoader loader = new FXMLLoader(getClass().getResource("Info.fxml"));
		            		Parent reset = (Parent)loader.load();
		            		InfoController controller = loader.<InfoController>getController();
		            		controller.transfer(p.getName(), p.getEmail(), p.getId());
		            		Scene scene = new Scene(reset);
		            		Stage s = (Stage) ((Node)event.getSource()).getScene().getWindow();
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
		            		s.setScene(scene);
		            		s.show();
		            		Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
		                    s.setX((primScreenBounds.getWidth() - s.getWidth()) / 2);
		                    s.setY((primScreenBounds.getHeight() - s.getHeight()) / 2);
		            	}else {
		            		FXMLLoader loader = new FXMLLoader(getClass().getResource("Atm.fxml"));			            	
		            		Parent reset = loader.load();
		            		AtmController controller = loader.<AtmController>getController();
		            		controller.transferToProfile(p.getName(), p.getEmail(), p.getPhone(), p.getLocation(), p.getJob(), p.getBirthday(), p.getGender(), p.getBalance(), p.getPic(), p.getId());
		            		controller.transfer(p.getCard(), p.getPin());
		            		Scene scene = new Scene(reset);
		            		Stage s = (Stage) ((Node)event.getSource()).getScene().getWindow();
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
		            		s.setScene(scene);
		            		s.show();
		            		Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
		                    s.setX((primScreenBounds.getWidth() - s.getWidth()) / 2);
		                    s.setY((primScreenBounds.getHeight() - s.getHeight()) / 2);
		            	}
		            }
		        }
		        catch(Exception e){
		            e.printStackTrace();
		        }
			}
	}
	@FXML
	private void btnSignup(MouseEvent event) {				
			if(usr2.getText().isEmpty() || pwd2.getText().isEmpty() || email.getText().isEmpty()) {
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
			if(accept.isSelected() == false) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Checking terms...");
				alert.setHeaderText(null);
				alert.setContentText("you didn't accept the terms of service.");
				alert.showAndWait();
			}
			if(usr2.getText().isEmpty() == false && pwd2.getText().isEmpty() == false && email.getText().isEmpty() == false && accept.isSelected()) {
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
				}else {
					String sql = "SELECT * FROM users WHERE useremail= ?";
					try {
						ps = connection.prepareStatement(sql);
						ps.setString(1, email.getText());
						resultSet = ps.executeQuery();
			            if(!resultSet.next()) {
			            	String sql2 = "INSERT INTO users (username, useremail, userpwd, firsttime) VALUES(?, ?, ?, ?)";
					        
					        try{
					        	String pwdcrypted = CryptwithMD5.cryptPwdWithMD5(pwd2.getText());
					            preparedStatement = connection.prepareStatement(sql2);
					            preparedStatement.setString(1, usr2.getText());
					            preparedStatement.setString(2, email.getText());
					            preparedStatement.setString(3, pwdcrypted);
					            preparedStatement.setString(4, "1");
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
					            }else{
					            	signup_pn.setOpacity(0.0);
					            	login_pn.setOpacity(1.0);
					            	login_pn.toFront();
					            }
					        }
					        catch(Exception e){
					            e.printStackTrace();
					        }
			            }else {
			            	Alert alert = new Alert(AlertType.WARNING);
							alert.setTitle("Fetching...");
							alert.setHeaderText(null);
							alert.setContentText("This email was already used.");
							alert.showAndWait();
			            }								
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}					
				}				
			}
	}	
	public static final Pattern VALIDEMAIL = 
	        Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

	    public static boolean validate(String email) {
	            Matcher matcher = VALIDEMAIL.matcher(email);
	            return matcher.find();
	    }
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		signup_pn.setOpacity(0.0);		
	}
	
}
