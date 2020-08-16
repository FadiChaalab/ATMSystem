package application;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import utils.ConnectionsUtils;
import utils.Constante;
import utils.Mail;
import utils.TransitionPrinter;

public class AtmController implements Initializable{
	private double xOffset, yOffset;
	private String usr, email, location, job, birth, gender;
	private int phone, amount, cardn, pinC, id;
	private Image pic;
	private boolean inserted = false;
	@FXML
	private Button BtnDrawSwitcher;
	private Button BtnBalanceSwitcher;
	private Button BtnTransferSwitcher;
	private Button BtnDepositSwitcher;
	private Button btnclose;
	private Button btnminimize;
	private Button BtnDepositSend;
	private Button BtnTransferSend;
	private Button BtnDrawSend;
	private Button BtnCheckPin;
	@FXML
	private TextField card;
	@FXML
	private TextField amountdeposit;
	@FXML
	private TextField amountdraw;
	@FXML
	private TextField reciever;
	@FXML
	private TextField amountsend;
	@FXML
	private TextField pin;
	@FXML
	private ImageView btn1;
	@FXML
	private ImageView btn2;
	@FXML
	private ImageView btn3;
	@FXML
	private ImageView btn4;
	@FXML
	private ImageView btn5;
	@FXML
	private ImageView btn6;
	@FXML
	private ImageView btn7;
	@FXML
	private ImageView btn8;
	@FXML
	private ImageView btn9;
	@FXML
	private ImageView btn0;
	private ImageView cancel;
	private ImageView clear;
	private ImageView enter;
	@FXML
	private AnchorPane deposit_pn;
	@FXML
	private AnchorPane balance_pn;
	@FXML
	private AnchorPane draw_pn;
	@FXML
	private AnchorPane transfer_pn;
	@FXML
	private AnchorPane insert_pn;
	@FXML
	private AnchorPane remove_pn;
	@FXML
	private AnchorPane none_pn;
	@FXML
	private AnchorPane pin_pn;
	@FXML
	private AnchorPane video_pn;
	@FXML
	private Label balanceamount;
	@FXML 
	private MediaView videoView;
	private TextArea area = new TextArea();
	Connection connection = null;
    PreparedStatement preparedStatement = null;
    PreparedStatement p = null;
    PreparedStatement ps = null;
    int result, secondResult, thirdResult;
    ResultSet resultSet = null;
    public AtmController() {
        connection = ConnectionsUtils.connectDB();
    }
	@FXML
	private void btnDrawSwitcher(MouseEvent event) {				
		if(inserted == true) {
			pin_pn.setOpacity(0.0);		
			transfer_pn.setOpacity(0.0);
			deposit_pn.setOpacity(0.0);
			balance_pn.setOpacity(0.0);
			video_pn.setOpacity(0.0);
			draw_pn.setOpacity(1.0);
			draw_pn.toFront();
			amountdraw.setDisable(false);
			amountdraw.setText("");
			amountdeposit.setDisable(true);
			reciever.setDisable(true);
			amountsend.setDisable(true);
		}
	}
	@FXML
	private void btnBalanceSwitcher(MouseEvent event) {		
		if(inserted == true) {
			pin_pn.setOpacity(0.0);		
			transfer_pn.setOpacity(0.0);
			deposit_pn.setOpacity(0.0);
			draw_pn.setOpacity(0.0);
			video_pn.setOpacity(0.0);
			balance_pn.setOpacity(1.0);
			balance_pn.toFront();
			amountdraw.setDisable(true);
			amountdeposit.setDisable(true);
			reciever.setDisable(true);
			amountsend.setDisable(true);
		}
	}
	@FXML
	private void btnDepositSwitcher(MouseEvent event) {		
		if(inserted == true) {
			pin_pn.setOpacity(0.0);		
			transfer_pn.setOpacity(0.0);
			balance_pn.setOpacity(0.0);
			draw_pn.setOpacity(0.0);
			video_pn.setOpacity(0.0);
			deposit_pn.setOpacity(1.0);
			deposit_pn.toFront();
			amountdraw.setDisable(true);
			amountdeposit.setText("");
			amountdeposit.setDisable(false);
			reciever.setDisable(true);
			amountsend.setDisable(true);
		}
	}
	@FXML
	private void btnTransferSwitcher(MouseEvent event) {		
		if(inserted == true) {
			pin_pn.setOpacity(0.0);		
			deposit_pn.setOpacity(0.0);
			balance_pn.setOpacity(0.0);
			draw_pn.setOpacity(0.0);
			video_pn.setOpacity(0.0);
			transfer_pn.setOpacity(1.0);
			transfer_pn.toFront();
			amountdraw.setDisable(true);			
			amountdeposit.setDisable(true);
			reciever.setDisable(false);
			amountsend.setDisable(false);
			amountsend.setText("");
		}
	}
	@FXML
	private void btnDepositSend(MouseEvent event) {		
		if(amountdeposit.getText().isEmpty()) {
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
			int a = Integer.parseInt(amountdeposit.getText());
			if(a >= 50) {
				String sql = "UPDATE users SET userbalance= ? WHERE id= ?";
				amount += a;
				balanceamount.setText("$" + String.valueOf(amount));
	            try {
	            	preparedStatement = connection.prepareStatement(sql);
					preparedStatement.setInt(1, amount);
					preparedStatement.setInt(2, id);
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
		            	String sql2 = "INSERT INTO deposit (id, username, useremail, amoutdeposit, time) VALUES(?, ?, ?, ?, ?)";
		            	try {
		            		ps = connection.prepareStatement(sql2);
				            ps.setInt(1, id);
				            ps.setString(2, usr);
				            ps.setString(3, email);
				            ps.setInt(4, a);
				            java.sql.Timestamp date = new java.sql.Timestamp(new java.util.Date().getTime());
				            ps.setTimestamp(5, date);
				            secondResult = ps.executeUpdate();
				            if(secondResult == 0) {
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
				            	area.appendText(
				            			"========================================"+
				            			"\n          Transition Records\n"+
				            			"========================================\n"+
				            			"\nService: Deposit\nUsername: " + usr +
				            			"\nEmail: " + email +
				            			"\nAmount: $" + a +
				            			"\n\nModified at: " + date
				            			);
				            	Alert alert = new Alert(AlertType.CONFIRMATION);
								alert.setTitle("Printer");
								alert.setHeaderText(null);
								alert.setContentText("Would you like to save your information?");

								Optional<ButtonType> result = alert.showAndWait();
								if (result.get() == ButtonType.OK){
									TransitionPrinter.print(area);
									a = 0;
									amountdeposit.setText("");
								} else {
								    alert.close();
								}
				            }
		            	}catch (SQLException e) {
							e.printStackTrace();
						}		            	
		            }
				} catch (SQLException e) {
					e.printStackTrace();
				}	            	            
			}else {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Validating...");
				alert.setHeaderText(null);
				alert.setContentText("Amount must be at leat 50");
				alert.showAndWait();
			}
		}
		
	}
	@FXML
	private void btnTransferSend(MouseEvent event) {		
		if(reciever.getText().isEmpty() || amountsend.getText().isEmpty()) {
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
		if(reciever.getText().isEmpty() == false) {
			if(validate(reciever.getText()) == false || reciever.getText() == email) {
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
		if(reciever.getText().isEmpty() == false && amountsend.getText().isEmpty() == false && validate(reciever.getText()) == true && reciever.getText() != email) {
			String sql = "SELECT * FROM users WHERE useremail= ?";
			try {
				preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setString(1, reciever.getText());
				resultSet = preparedStatement.executeQuery();
	            if(!resultSet.next()) {
	            	Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Fetching...");
					alert.setHeaderText(null);
					alert.setContentText("No person with this email was found");
					alert.showAndWait();
	            }else {
	            	int recieverid = resultSet.getInt("id");
	            	int recieveramount = resultSet.getInt("userbalance");
	            	String recievername = resultSet.getString("username");
	            	String recievergender = resultSet.getString("usergender");
	            	int a = Integer.parseInt(amountsend.getText());
	    			if(a >= 50 && a <= amount) {
	    				String sql2 = "UPDATE users SET userbalance= ? WHERE id= ?";
	    				amount -= a;
	    				balanceamount.setText("$" + String.valueOf(amount));
	    	            try {
	    	            	preparedStatement = connection.prepareStatement(sql2);
	    					preparedStatement.setInt(1, amount);
	    					preparedStatement.setInt(2, id);
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
	    		            	recieveramount += a;
	    		            	String sql3 = "UPDATE users SET userbalance= ? WHERE id= ?";
	    		            	p = connection.prepareStatement(sql3);
		    					p.setInt(1, recieveramount);
		    					p.setInt(2, recieverid);
		    		            secondResult = p.executeUpdate();
		    		            if(secondResult == 0) {
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
		    		            	String sql4 = "INSERT INTO transfer (senderid, recieverid, sendername, senderemail, recievername, recieveremail, amountsend, time) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
		    		            	try {
		    		            		ps = connection.prepareStatement(sql4);
		    				            ps.setInt(1, id);
		    				            ps.setInt(2, recieverid);
		    				            ps.setString(3, usr);
		    				            ps.setString(4, email);
		    				            ps.setString(5, recievername);
		    				            ps.setString(6, reciever.getText());
		    				            ps.setInt(7, a);
		    				            java.sql.Timestamp date = new java.sql.Timestamp(new java.util.Date().getTime());
		    				            ps.setTimestamp(8, date);
		    				            thirdResult = ps.executeUpdate();
		    				            if(thirdResult == 0) {
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
		    				            	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
		    				                Date daten = new Date(); 
		    				            	try {
		    				            		String g;
		    				            		if(recievergender == "Male") {
		    				            			g = "Mr";
		    				            		}else {
		    				            			g = "Mrs";
		    				            		}
		    				            		String content = "Congratulations " + g + " " + recievername + " , you recieved $"+ a +" from:  \nInformations: \nusername: " + usr + "\nemail: " + email + "\nrecieved at: " + formatter.format(daten);
		    				            		Mail.sendMail(reciever.getText(), content);
		    				            	}catch(Exception ex) {
		    				            		System.out.println(ex.getMessage());
		    				            	}
		    				            	area.appendText(
		    				            			"========================================"+
		    				            			"\n          Transition Records\n"+
		    				            			"========================================\n"+
		    				            			"\nService: Transfer\nSender name: " + usr +
		    				            			"\nSender email: " + email +
		    				            			"\nReciever name: " + recievername +
		    				            			"\nReciever email: " + reciever.getText() +
		    				            			"\nAmount: $" + a +
		    				            			"\n\nModified at: " + date
		    				            			);
		    				            	Alert alert = new Alert(AlertType.CONFIRMATION);
		    								alert.setTitle("Printer");
		    								alert.setHeaderText(null);
		    								alert.setContentText("Would you like to save your information?");

		    								Optional<ButtonType> result = alert.showAndWait();
		    								if (result.get() == ButtonType.OK){
		    									TransitionPrinter.print(area);
		    									a = 0;
		    									amountsend.setText("");
		    									reciever.setText("");
		    								} else {
		    								    alert.close();
		    								}		    								
		    				            }
		    		            	}catch (SQLException e) {
		    							e.printStackTrace();
		    						}
		    		            }	    		            			            	
	    		            }
	    				} catch (SQLException e) {
	    					e.printStackTrace();
	    				}	            	            
	    			}else {
	    				Alert alert = new Alert(AlertType.WARNING);
	    				alert.setTitle("Validating...");
	    				alert.setHeaderText(null);
	    				alert.setContentText("Amount must be at leat 50 and less than your balance");
	    				alert.showAndWait();
	    			}
	            }
			} catch (SQLException e) {
				e.printStackTrace();
			}			
		}
	}
	@FXML
	private void btnDrawSend(MouseEvent event) {		
		if(amountdraw.getText().isEmpty()) {
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
			int a = Integer.parseInt(amountdraw.getText());
			if(a >= 50 && a <= amount) {
				String sql = "UPDATE users SET userbalance= ? WHERE id= ?";
				amount -= a;
				balanceamount.setText("$" + String.valueOf(amount));
	            try {
	            	preparedStatement = connection.prepareStatement(sql);
					preparedStatement.setInt(1, amount);
					preparedStatement.setInt(2, id);
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
		            	String sql2 = "INSERT INTO withdraw (id, username, useremail, amountdrawed, time) VALUES(?, ?, ?, ?, ?)";
		            	try {
		            		ps = connection.prepareStatement(sql2);
				            ps.setInt(1, id);
				            ps.setString(2, usr);
				            ps.setString(3, email);
				            ps.setInt(4, a);
				            java.sql.Timestamp date = new java.sql.Timestamp(new java.util.Date().getTime());
				            ps.setTimestamp(5, date);
				            secondResult = ps.executeUpdate();
				            if(secondResult == 0) {
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
				            	area.appendText(
				            			"========================================"+
				            			"\n          Transition Records\n"+
				            			"========================================\n"+
				            			"\nService: WithDraw\nUsername: " + usr +
				            			"\nEmail: " + email +
				            			"\nAmount: $" + a +
				            			"\n\nModified at: " + date
				            			);
				            	Alert alert = new Alert(AlertType.CONFIRMATION);
								alert.setTitle("Printer");
								alert.setHeaderText(null);
								alert.setContentText("Would you like to save your information?");

								Optional<ButtonType> result = alert.showAndWait();
								if (result.get() == ButtonType.OK){
									TransitionPrinter.print(area);
									a = 0;
									amountdraw.setText("");
								} else {
								    alert.close();
								}
				            }
		            	}catch (SQLException e) {
							e.printStackTrace();
						}		            	
		            }
				} catch (SQLException e) {
					e.printStackTrace();
				}	            	            
			}else {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Validating...");
				alert.setHeaderText(null);
				alert.setContentText("Amount must be at leat 50 and less than your balance");
				alert.showAndWait();
			}
		}	
	}
	@FXML
	private void btnClear(MouseEvent event) {		
			if(card.isDisable() == false) {
				card.setText("");
			}
			if(amountdraw.isDisable() == false) {
				amountdraw.setText("");
			}
			if(amountdeposit.isDisable() == false) {
				amountdeposit.setText("");
			}
			if(amountsend.isDisable() == false) {
				amountsend.setText("");
			}
			if(pin.isDisable() == false) {
				pin.setText("");
			}
	}
	@FXML
	private void btnNumbers(MouseEvent event) {		
			if(card.isDisable() == false) {
				if(event.getSource() == btn1) {
					String input = card.getText();
					card.setText(input + "1");
				}
				if(event.getSource() == btn2) {
					String input = card.getText();
					card.setText(input + "2");
				}
				if(event.getSource() == btn3) {
					String input = card.getText();
					card.setText(input + "3");
				}
				if(event.getSource() == btn4) {
					String input = card.getText();
					card.setText(input + "4");
				}
				if(event.getSource() == btn5) {
					String input = card.getText();
					card.setText(input + "5");
				}
				if(event.getSource() == btn6) {
					String input = card.getText();
					card.setText(input + "6");
				}
				if(event.getSource() == btn7) {
					String input = card.getText();
					card.setText(input + "7");
				}
				if(event.getSource() == btn8) {
					String input = card.getText();
					card.setText(input + "8");
				}
				if(event.getSource() == btn9) {
					String input = card.getText();
					card.setText(input + "9");
				}
				if(event.getSource() == btn0) {
					String input = card.getText();
					card.setText(input + "0");
				}
			}
			if(pin.isDisable() == false) {
				if(event.getSource() == btn1) {
					String input = pin.getText();
					pin.setText(input + "1");
				}
				if(event.getSource() == btn2) {
					String input = pin.getText();
					pin.setText(input + "2");
				}
				if(event.getSource() == btn3) {
					String input = pin.getText();
					pin.setText(input + "3");
				}
				if(event.getSource() == btn4) {
					String input = pin.getText();
					pin.setText(input + "4");
				}
				if(event.getSource() == btn5) {
					String input = pin.getText();
					pin.setText(input + "5");
				}
				if(event.getSource() == btn6) {
					String input = pin.getText();
					pin.setText(input + "6");
				}
				if(event.getSource() == btn7) {
					String input = pin.getText();
					pin.setText(input + "7");
				}
				if(event.getSource() == btn8) {
					String input = pin.getText();
					pin.setText(input + "8");
				}
				if(event.getSource() == btn9) {
					String input = pin.getText();
					pin.setText(input + "9");
				}
				if(event.getSource() == btn0) {
					String input = pin.getText();
					pin.setText(input + "0");
				}
			}
			if(amountdraw.isDisable() == false) {
				if(event.getSource() == btn1) {
					String input = amountdraw.getText();
					amountdraw.setText(input + "1");
				}
				if(event.getSource() == btn2) {
					String input = amountdraw.getText();
					amountdraw.setText(input + "2");
				}
				if(event.getSource() == btn3) {
					String input = amountdraw.getText();
					amountdraw.setText(input + "3");
				}
				if(event.getSource() == btn4) {
					String input = amountdraw.getText();
					amountdraw.setText(input + "4");
				}
				if(event.getSource() == btn5) {
					String input = amountdraw.getText();
					amountdraw.setText(input + "5");
				}
				if(event.getSource() == btn6) {
					String input = amountdraw.getText();
					amountdraw.setText(input + "6");
				}
				if(event.getSource() == btn7) {
					String input = amountdraw.getText();
					amountdraw.setText(input + "7");
				}
				if(event.getSource() == btn8) {
					String input = amountdraw.getText();
					amountdraw.setText(input + "8");
				}
				if(event.getSource() == btn9) {
					String input = amountdraw.getText();
					amountdraw.setText(input + "9");
				}
				if(event.getSource() == btn0) {
					String input = amountdraw.getText();
					amountdraw.setText(input + "0");
				}
			}
			if(amountdeposit.isDisable() == false) {
				if(event.getSource() == btn1) {
					String input = amountdeposit.getText();
					amountdeposit.setText(input + "1");
				}
				if(event.getSource() == btn2) {
					String input = amountdeposit.getText();
					amountdeposit.setText(input + "2");
				}
				if(event.getSource() == btn3) {
					String input = amountdeposit.getText();
					amountdeposit.setText(input + "3");
				}
				if(event.getSource() == btn4) {
					String input = amountdeposit.getText();
					amountdeposit.setText(input + "4");
				}
				if(event.getSource() == btn5) {
					String input = amountdeposit.getText();
					amountdeposit.setText(input + "5");
				}
				if(event.getSource() == btn6) {
					String input = amountdeposit.getText();
					amountdeposit.setText(input + "6");
				}
				if(event.getSource() == btn7) {
					String input = amountdeposit.getText();
					amountdeposit.setText(input + "7");
				}
				if(event.getSource() == btn8) {
					String input = amountdeposit.getText();
					amountdeposit.setText(input + "8");
				}
				if(event.getSource() == btn9) {
					String input = amountdeposit.getText();
					amountdeposit.setText(input + "9");
				}
				if(event.getSource() == btn0) {
					String input = amountdeposit.getText();
					amountdeposit.setText(input + "0");
				}
			}
			if(amountsend.isDisable() == false) {
				if(event.getSource() == btn1) {
					String input = amountsend.getText();
					amountsend.setText(input + "1");
				}
				if(event.getSource() == btn2) {
					String input = amountsend.getText();
					amountsend.setText(input + "2");
				}
				if(event.getSource() == btn3) {
					String input = amountsend.getText();
					amountsend.setText(input + "3");
				}
				if(event.getSource() == btn4) {
					String input = amountsend.getText();
					amountsend.setText(input + "4");
				}
				if(event.getSource() == btn5) {
					String input = amountsend.getText();
					amountsend.setText(input + "5");
				}
				if(event.getSource() == btn6) {
					String input = amountsend.getText();
					amountsend.setText(input + "6");
				}
				if(event.getSource() == btn7) {
					String input = amountsend.getText();
					amountsend.setText(input + "7");
				}
				if(event.getSource() == btn8) {
					String input = amountsend.getText();
					amountsend.setText(input + "8");
				}
				if(event.getSource() == btn9) {
					String input = amountsend.getText();
					amountsend.setText(input + "9");
				}
				if(event.getSource() == btn0) {
					String input = amountsend.getText();
					amountsend.setText(input + "0");
				}
			}
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
	private void btnProfile(MouseEvent event) throws IOException {	
		Stage atm = (Stage) ((Node)event.getSource()).getScene().getWindow();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Profile.fxml"));			            	
		Parent reset = loader.load();
		ProfileController controller = loader.<ProfileController>getController();
		controller.transferProfile(usr, email, phone, location, job, birth,gender, amount, pic, id, atm);
		Scene scene = new Scene(reset);
		Stage s = new Stage();
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
		s.initStyle(StageStyle.TRANSPARENT);
		s.getIcons().add(new Image("file:///" + Constante.icon));
		s.setTitle("ATM System");
		s.setScene(scene);		
		s.show();
		Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        s.setX((primScreenBounds.getWidth() - s.getWidth()) / 2);
        s.setY((primScreenBounds.getHeight() - s.getHeight()) / 2);
	}
	public static final Pattern VALIDEMAIL = 
	        Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

	    public static boolean validate(String email) {
	            Matcher matcher = VALIDEMAIL.matcher(email);
	            return matcher.find();
	    }
	public void transfer(int usercard, int userpin) {
	    	cardn = usercard;
	    	pinC = userpin;
	}
	public void transferToProfile(String username, String useremail, int userphone, String userlocation, String userjob, String userbirthday, String usergender, int userbalance, Image userpic, int userid) {
			usr = username;
			email = useremail;
			phone = userphone;
			location = userlocation;
			job = userjob;
			birth = userbirthday;
			gender = usergender;
			amount = userbalance;
			pic = userpic;
			id = userid;
	}
	@FXML
	private void btnEnter(MouseEvent event) {
			if(card.getText().isEmpty()) {
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
				if(card.getLength() > 8 || card.getLength() < 8) {
					Platform.runLater(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							String path = Constante.card;
							Media sound = new Media(new File(path).toURI().toString());
							MediaPlayer mediaPlayer = new MediaPlayer(sound);
							mediaPlayer.play();	
						}});	
				}else
					if(Integer.parseInt(card.getText()) == cardn) {
					video_pn.setOpacity(0.0);		
					deposit_pn.setOpacity(0.0);
					balance_pn.setOpacity(0.0);
					draw_pn.setOpacity(0.0);
					transfer_pn.setOpacity(0.0);
					pin_pn.setOpacity(1.0);
					pin_pn.toFront();
					pin.setDisable(false);
					card.setDisable(true);					
				}
			}			
				
	}
	@FXML
	private void btnCheckPin(MouseEvent event) {
			if(pin.getText().isEmpty() == false) {
				if(Integer.parseInt(pin.getText()) == pinC) {
					inserted = true;
					video_pn.setOpacity(0.0);		
					deposit_pn.setOpacity(0.0);
					pin_pn.setOpacity(0.0);
					draw_pn.setOpacity(0.0);
					transfer_pn.setOpacity(0.0);
					balance_pn.setOpacity(1.0);
					balance_pn.toFront();
					none_pn.setOpacity(0.0);
					remove_pn.setOpacity(0.0);
					insert_pn.setOpacity(1.0);
					insert_pn.toFront();
					pin.setDisable(true);
					balanceamount.setText("$" + String.valueOf(amount));							
				}else {
    				Alert alert = new Alert(AlertType.WARNING);
    				alert.setTitle("Matching...");
    				alert.setHeaderText(null);
    				alert.setContentText("Wrong pin");
    				alert.showAndWait();
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
	private void btnCancel(MouseEvent event) {		
			pin_pn.setOpacity(0.0);		
			deposit_pn.setOpacity(0.0);
			balance_pn.setOpacity(0.0);
			draw_pn.setOpacity(0.0);
			transfer_pn.setOpacity(0.0);
			video_pn.setOpacity(1.0);
			video_pn.toFront();
			card.setDisable(false);
			card.setText("");
			pin.setText("");
			if(inserted == true) {
				insert_pn.setOpacity(0.0);
				none_pn.setOpacity(0.0);
				remove_pn.setOpacity(1.0);
				remove_pn.toFront();			
			}
			inserted = false;
	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		Media media = new Media("file:///" + Constante.video);
		MediaPlayer mediaPlayer = new MediaPlayer(media);
		mediaPlayer.isMute();
		mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
		mediaPlayer.setAutoPlay(true);
		videoView.setMediaPlayer(mediaPlayer);
		videoView.setOpacity(1.0);
		videoView.toFront();
		pin_pn.setOpacity(0.0);
		draw_pn.setOpacity(0.0);
		transfer_pn.setOpacity(0.0);
		deposit_pn.setOpacity(0.0);
		balance_pn.setOpacity(0.0);
		insert_pn.setOpacity(0.0);
		remove_pn.setOpacity(0.0);
		pin.setDisable(true);
		card.setTextFormatter(new TextFormatter<>(c -> {
		    if (!c.getControlNewText().matches("\\d*")) 
		        return null;
		    else
		        return c;
		    }
		));
		pin.setTextFormatter(new TextFormatter<>(c -> {
		    if (!c.getControlNewText().matches("\\d*")) 
		        return null;
		    else
		        return c;
		    }
		));
		amountdeposit.setTextFormatter(new TextFormatter<>(c -> {
		    if (!c.getControlNewText().matches("\\d*")) 
		        return null;
		    else
		        return c;
		    }
		));
		amountdraw.setTextFormatter(new TextFormatter<>(c -> {
		    if (!c.getControlNewText().matches("\\d*")) 
		        return null;
		    else
		        return c;
		    }
		));
		amountsend.setTextFormatter(new TextFormatter<>(c -> {
		    if (!c.getControlNewText().matches("\\d*")) 
		        return null;
		    else
		        return c;
		    }
		));
	}
	
}
