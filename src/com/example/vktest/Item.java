package com.example.vktest;

public class Item {
	String name;
	String url;
	
	Item(String n, String u)
	{
		this.name = n;
		this.url = u;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}
