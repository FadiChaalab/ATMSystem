package application;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

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
import utils.Mail;

public class InfoController implements Initializable{
	private double xOffset;
	private double yOffset;
	private int id;
	private boolean changed = false;
	BufferedImage bf;
	FileInputStream fi;
	@FXML
	private Button btnsaveandexit;
	private Button btnclose;
	private Button btnminimize;
	@FXML
	private TextField phone;
	@FXML
	private TextField location;
	@FXML
	private TextField job;
	@FXML
	private TextField amount;
	@FXML
	private DatePicker birth;
	@FXML
	private ComboBox<String> gender;
	ObservableList<String> genderList = FXCollections.observableArrayList("Male", "Female");
	@FXML
	private Label usrinfo;
	@FXML
	private Label emailinfo;
	@FXML
	private ImageView pic;
	Connection connection = null;
    PreparedStatement preparedStatement = null;
    int result;
    public InfoController() {
        connection = ConnectionsUtils.connectDB();
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
	void transfer(String username, String mail, int userid) {		
		usrinfo.setText(username);
		emailinfo.setText(mail);
		id = userid;
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
			int cardnb;
			int pincard;
			int balance = Integer.parseInt(amount.getText()) + 25;			
			if(phone.getText().isEmpty() || location.getText().isEmpty() || job.getText().isEmpty() || amount.getText().isEmpty() || birth.getValue().toString().isEmpty() || changed == false) {
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
				String sql = "UPDATE users SET userphone= ?, userlocation= ?, userjob= ?, userbirthday= ?, usergender= ?, userbalance= ?, userpic= ?, firsttime= ?, usercard= ?, userpin= ? WHERE id= ?";
				cardnb = 80000000 + id;
            	pincard = 5000 + id;
		        try{		        	
		            preparedStatement = connection.prepareStatement(sql);
		            preparedStatement.setInt(1, Integer.parseInt(phone.getText()));
		            preparedStatement.setString(2, location.getText());
		            preparedStatement.setString(3, job.getText());
		            preparedStatement.setString(4, birth.getValue().toString());
		            preparedStatement.setString(5, gender.getValue());
		            preparedStatement.setInt(6, balance);
		            preparedStatement.setBinaryStream(7, fi);
		            preparedStatement.setInt(8, 0);
		            preparedStatement.setInt(9, cardnb);
		            preparedStatement.setInt(10, pincard);
		            preparedStatement.setInt(11, id);
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
		            	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
		                Date date = new Date(); 
		            	try {
		            		String g;
		            		if(gender.getValue().toString() == "Male") {
		            			g = "Mr";
		            		}else {
		            			g = "Mrs";
		            		}
		            		String content = "Congratulations " + g + " " + usrinfo.getText() + " , your card is ready to use. you got 25$ for free as a gift from us. \n Informations: \n username: " + usrinfo.getText() + "\n email: " + emailinfo.getText() + "\n card n°: " + String.valueOf(cardnb) + "\n pin: " + String.valueOf(pincard) + "\n recieved at: " + formatter.format(date);
		            		Mail.sendMail(emailinfo.getText(), content);
		            	}catch(Exception ex) {
		            		System.out.println(ex.getMessage());
		            	}
		            	FXMLLoader loader = new FXMLLoader(getClass().getResource("Atm.fxml"));		            	
		            	Parent reset = loader.load();
		            	AtmController controller = loader.<AtmController>getController();
	            		controller.transferToProfile(usrinfo.getText(), emailinfo.getText(), Integer.parseInt(phone.getText()), location.getText(), job.getText(), birth.getValue().toString(), gender.getValue(), Integer.parseInt(amount.getText()), pic.getImage(), id);
	            		controller.transfer(cardnb, pincard);
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
		        catch(Exception e){
		            e.printStackTrace();
		        }
			}
	}	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		gender.setValue("Male");
		gender.setItems(genderList);
		amount.setTextFormatter(new TextFormatter<>(c -> {
		    if (!c.getControlNewText().matches("\\d*")) 
		        return null;
		    else
		        return c;
		    }
		));
		phone.setTextFormatter(new TextFormatter<>(c -> {
		    if (!c.getControlNewText().matches("\\d*")) 
		        return null;
		    else
		        return c;
		    }
		));
	}
	
}
