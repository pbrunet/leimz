package com.client.utils.gui;

import com.client.entities.Joueur;

import de.matthiasmann.twl.DialogLayout;
import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.Widget;

public class FenCaracs extends ResizableFrame
{
	private Joueur joueur;
	
	public FenCaracs(Joueur joueur)
	{
		this.joueur = joueur;
		init();
	}
	
	private void init()
	{
		Widget i = new Widget()
		{
			@Override
			protected void paintWidget(GUI gui) 
			{
				joueur.getCurrent_img_repos().draw(this.getX(), this.getY());
			}
			
			 @Override
			    public int getPreferredInnerWidth() {
			        return joueur.getCurrent_img_repos().getWidth();
			    }

			    @Override
			    public int getPreferredInnerHeight() {
			        return joueur.getCurrent_img_repos().getHeight();
			    }
		};
		
		Label labNom = new Label(joueur.getPerso().getNom());
		labNom.setTheme("/label");
		
		Label labTitre = new Label("Vagabond");
		labTitre.setTheme("/label");
		
		DialogLayout l = new DialogLayout();
		l.setTheme("/dialoglayout");
		l.setHorizontalGroup(l.createSequentialGroup().addWidget(i).addGroup(l.createParallelGroup(labNom, labTitre)));
		l.setVerticalGroup(l.createParallelGroup().addWidget(i).addGroup(l.createSequentialGroup(labNom, labTitre)));
		
		this.add(l);
		
		//TabbedPane onglets = new TabbedPane();
		
	}
}
