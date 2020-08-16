package utils;

import javafx.scene.image.Image;

public class Person {
	private int id;
	private String name;
	private String email;
	private int phone;
	private String gender;
	private String location;
	private String job;
	private String birthday;
	private int balance;
	private Image pic;
	private int card;
	private int pin;
	public Person(int id, String name, String email, int phone, String gender, String location, String job, String birthday, int balance, Image pic, int card, int pin) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.gender = gender;
		this.location = location;
		this.job = job;
		this.birthday = birthday;
		this.balance = balance;
		this.pic = pic;
		this.card = card;
		this.pin = pin;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getJob() {
		return job;
	}
	public void setJob(String job) {
		this.job = job;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public int getBalance() {
		return balance;
	}
	public void setBalance(int balance) {
		this.balance = balance;
	}
	public Image getPic() {
		return pic;
	}
	public void setPic(Image pic) {
		this.pic = pic;
	}
	public int getPhone() {
		return phone;
	}
	public void setPhone(int phone) {
		this.phone = phone;
	}
	public int getCard() {
		return card;
	}
	public void setCard(int card) {
		this.card = card;
	}
	public int getPin() {
		return pin;
	}
	public void setPin(int pin) {
		this.pin = pin;
	}
}
