package com.gameplay;

import java.util.ArrayList;

public class PNJ_discours 
{
	private String discours;
	private ArrayList<Integer> tree_reponses;
	private ArrayList<String> reponses;
	
	public PNJ_discours(String discours, ArrayList<Integer> treeReponses, ArrayList<String> reponses) 
	{
		this.discours = discours;
		this.tree_reponses = treeReponses;
		this.reponses = reponses;
	}

	public String getDiscours() {
		return discours;
	}

	public void setDiscours(String discours) {
		this.discours = discours;
	}

	public ArrayList<Integer> getTree_reponses() {
		return tree_reponses;
	}

	public void setTree_reponses(ArrayList<Integer> treeReponses) {
		tree_reponses = treeReponses;
	}

	public ArrayList<String> getReponses() {
		return reponses;
	}

	public void setReponses(ArrayList<String> reponses) {
		this.reponses = reponses;
	}
}
