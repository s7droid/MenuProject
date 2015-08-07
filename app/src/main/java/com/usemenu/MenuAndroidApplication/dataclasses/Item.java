package com.usemenu.MenuAndroidApplication.dataclasses;

public class Item implements Cloneable {

	public String name;
	public String image;
	public String smalllabel;
	public String largelabel;
	public double smallprice;
	public double largeprice;
	public int smalltag;
	public int largetag;
	public String currency;
	public String category;
	public String description;
	public String ingredients;
	public int quantityLarge = 0;
	public int quantitySmall = 0;
	public double price;
	public int amount = 1;
	public boolean smallAdded = false;
//	public String commentSmall;
//	public String commentLarge;
	public String comment;
	
	
	public Item getSmall() {
		// Item item = getCopy();
		++quantitySmall;
		return this;
	}

	public Item getLarge() {
		// Item item = getCopy();
		++quantityLarge;
		return this;
	}

	private Item getCopy() {

		Item item = new Item();
		item.name = name;
		item.image = image;
		item.smalllabel = smalllabel;
		item.largelabel = largelabel;
		item.smallprice = smallprice;
		item.largeprice = largeprice;
		item.smalltag = smalltag;
		item.largetag = largetag;
		item.currency = currency;
		item.category = category;
		item.price = price;
		item.amount = amount;

		return item;
	}

	public Object clone() {
		try {
			return super.clone();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public String toString() {
		return "Item [name=" + name + ", image=" + image + ", smalllabel=" + smalllabel + ", largelabel=" + largelabel + ", smallprice=" + smallprice
				+ ", largeprice=" + largeprice + ", smalltag=" + smalltag + ", largetag=" + largetag + ", currency=" + currency + ", category="
				+ category + ", description=" + description + ", ingredients=" + ingredients + ", quantityLarge=" + quantityLarge
				+ ", quantitySmall=" + quantitySmall + ", price=" + price + ", amount=" + amount + ", smallAdded=" + smallAdded + ", comment="
				+ comment + "]";
	}

	
	
}