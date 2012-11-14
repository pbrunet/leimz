package com.map;

import java.util.ArrayList;

public class Grille extends ArrayList<ArrayList<Tile>>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ArrayList<Group_tiles> groups_tiles; 
	
	public Grille()
	{
		groups_tiles = new ArrayList<Group_tiles>();
	}

	public ArrayList<Group_tiles> getGroups_tiles() {
		return groups_tiles;
	}

	public void setGroups_tiles(ArrayList<Group_tiles> groupsTiles) {
		groups_tiles = groupsTiles;
	}
	
	
}
