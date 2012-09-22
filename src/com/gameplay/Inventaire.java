package com.gameplay;

import java.util.ArrayList;

import com.gameplay.items.Item;


public class Inventaire
{
	public ArrayList<Item> items;
	
	public Inventaire(ArrayList<Item> items)
	{
		this.items = items;
	}
	
	public Inventaire()
	{
		this.items = new ArrayList<Item>();
	}
	
	public void addItem(Item obj)
	{
		this.items.add(obj);
	}

	public ArrayList<Item> getItems() {
		return items;
	}

	public void setObjets(ArrayList<Item> items) {
		this.items = items;
	}
}
