package com.npdevs.healthcastle;

import java.util.ArrayList;

public class Users {
	private String name,mob,password;
	private int age,weight,height,sex;
	private ArrayList<Integer> steps,calorie,heartbeat;
	private ArrayList<String> family;

	public Users() {
	}

	public Users(String name, String mob, String password, int age, int weight, int height, int sex, ArrayList<Integer> steps, ArrayList<Integer> calorie, ArrayList<Integer> heartbeat, ArrayList<String> family) {
		this.name = name;
		this.mob = mob;
		this.password = password;
		this.age = age;
		this.weight = weight;
		this.height = height;
		this.sex = sex;
		this.steps = steps;
		this.calorie = calorie;
		this.heartbeat = heartbeat;
		this.family=family;
	}

	public ArrayList<String> getFamily() {
		return family;
	}

	public void setFamily(ArrayList<String> family) {
		this.family = family;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMob() {
		return mob;
	}

	public void setMob(String mob) {
		this.mob = mob;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public ArrayList<Integer> getSteps() {
		return steps;
	}

	public void setSteps(ArrayList<Integer> steps) {
		this.steps = steps;
	}

	public ArrayList<Integer> getCalorie() {
		return calorie;
	}

	public void setCalorie(ArrayList<Integer> calorie) {
		this.calorie = calorie;
	}

	public ArrayList<Integer> getHeartbeat() {
		return heartbeat;
	}

	public void setHeartbeat(ArrayList<Integer> heartbeat) {
		this.heartbeat = heartbeat;
	}
}
