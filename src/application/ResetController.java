package application;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.application.Platform;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Screen;
import javafx.stage.Stage;
import utils.ConnectionsUtils;
import utils.Constante;
import utils.CryptwithMD5;
import utils.Mail;

public class ResetController implements Initializable{
	private double xOffset;
	private double yOffset;
	private Random r = new Random();
	String randomcode, oldpwd;
	@FXML
	private Button BtnCancel;
	private Button BtnResetSwitcher;
	@FXML
	private Button BtnBackSwitcher;
	private Button btnclose;
	private Button btnminimize;
	private Button btnresetpwd;
	private Button btnsendcode;
	@FXML
	private AnchorPane sendCode_pn;
	@FXML
	private AnchorPane resetpwd_pn;
	@FXML
	private TextField email, code;
	@FXML
	private PasswordField pwd, pwd2;
	Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    int result;
    public ResetController() {
        connection = ConnectionsUtils.connectDB();
    }
	@FXML
	private void btnBackSwitcher(MouseEvent event) {		
			resetpwd_pn.setOpacity(0.0);
			sendCode_pn.setOpacity(1.0);
			BtnBackSwitcher.setOpacity(0.0);
			sendCode_pn.toFront();					
	}
	@FXML
	private void btnCancel(MouseEvent event) throws IOException {				
		Parent reset = FXMLLoader.load(getClass().getResource("Main.fxml"));
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
	private void btnSendCode(MouseEvent event) throws Exception {			
			if(email.getText().isEmpty()) {
				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						String path = Constante.fill;
						Media sound = new Media(new File(path).toURI().toString());
						MediaPlayer mediaPlayer = new MediaPlayer(sound);
						mediaPlayer.play();	
					}});				
			}else {
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
					preparedStatement = connection.prepareStatement(sql);
					preparedStatement.setString(1, email.getText());
					resultSet = preparedStatement.executeQuery();
		            if(!resultSet.next()) {
		            	Alert alert = new Alert(AlertType.WARNING);
						alert.setTitle("Fetching...");
						alert.setHeaderText(null);
						alert.setContentText("No person with this email was found");
						alert.showAndWait();
		            }else {
		            	oldpwd = resultSet.getString("userpwd");
		            	randomcode = String.format("%04d", r.nextInt(10000));
						String content = "Your code for reset is " + randomcode + ", use it before it expires.";
		        		Mail.sendMail(email.getText(), content);
						sendCode_pn.setOpacity(0.0);
						BtnBackSwitcher.setOpacity(1.0);
						resetpwd_pn.setOpacity(1.0);
						resetpwd_pn.toFront();
		            }		
				}
			}		
	}
	@FXML
	private void btnResetPassword(MouseEvent event) {				
			if(pwd.getText().isEmpty() || pwd2.getText().isEmpty() || code.getText().isEmpty()) {
				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						String path = Constante.fill;
						Media sound = new Media(new File(path).toURI().toString());
						MediaPlayer mediaPlayer = new MediaPlayer(sound);
						mediaPlayer.play();	
					}});				
			}else {
				if(code.getText().equals(randomcode) == false) {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Matching...");
					alert.setHeaderText(null);
					alert.setContentText("Incorrect code");
					alert.showAndWait();
				}else {
					if(pwd.getText().equals(pwd2.getText())) {
						String sql2 = "UPDATE users SET userpwd= ? WHERE useremail= ?";
						String newpwd = CryptwithMD5.cryptPwdWithMD5(pwd.getText());
						if(oldpwd.equals(newpwd)) {
							Alert alert = new Alert(AlertType.WARNING);
							alert.setTitle("Matching...");
							alert.setHeaderText(null);
							alert.setContentText("You entered your old password");
							alert.showAndWait();
						}else {
							try {
								preparedStatement = connection.prepareStatement(sql2);
								preparedStatement.setString(1, newpwd);
		    					preparedStatement.setString(2, email.getText());
		    		            result = preparedStatement.executeUpdate();
		    		            if(result == 0) {
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
		    		            	code.setText("");
		    		            	pwd.setText("");
		    		            	pwd2.setText("");
		    		            	Alert alert = new Alert(AlertType.INFORMATION);
		    						alert.setTitle("Success");
		    						alert.setHeaderText(null);
		    						alert.setContentText("Password was changed with success");
		    						alert.showAndWait();
		    		            }
							} catch (SQLException e) {
								e.printStackTrace();
							}    	
						}										
					}else {
						Alert alert = new Alert(AlertType.WARNING);
						alert.setTitle("Matching...");
						alert.setHeaderText(null);
						alert.setContentText("Password doesn't match");
						alert.showAndWait();
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
		resetpwd_pn.setOpacity(0.0);
		BtnBackSwitcher.setOpacity(0.0);
		code.setTextFormatter(new TextFormatter<>(c -> {
		    if (!c.getControlNewText().matches("\\d*")) 
		        return null;
		    else
		        return c;
		    }
		));
	}
	
}
