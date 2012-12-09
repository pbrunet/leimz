package com.gameplay;

import java.util.ArrayList;
import org.newdawn.slick.geom.Vector2f;
import com.client.entities.Joueur;
import com.client.network.NetworkManager;
import de.matthiasmann.twl.Alignment;
import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.DialogLayout;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.ListBox;
import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.model.SimpleChangableListModel;

public class Quete 
{
	private String nom;
	private ArrayList<QueteObjectif> objectifs;
	
	public Quete(String nom, ArrayList<QueteObjectif> objectifs)
	{
		this.nom = nom;
		
		this.objectifs = objectifs;
	}
	
	public void testObjectifs(Joueur main_player)
	{
		for(int i = 0; i < objectifs.size(); i++)
		{
			if(!objectifs.get(i).isAccompli())
			{
				if(objectifs.get(i).getType().equals("endroit"))
				{
					if(((float)main_player.getTile().getPos().x) == (((Vector2f)objectifs.get(i).getObjectif()).getX()) && ((float)main_player.getTile().getPos().y) == (((Vector2f)objectifs.get(i).getObjectif()).getY()))
					{
						objectifs.get(i).setAccompli(true);
						NetworkManager.instance.sendToServer("qo;"+this.getNom()+";"+i);
						System.out.println("Objectif accompli !");
					}
				}
			}
		}
	}
	
	public static ResizableFrame newQueteFrame(Quete quete)
	{
		final ResizableFrame frame = new ResizableFrame();
		frame.setTitle("Nouvelle quête !");
		
		Label labN = new Label("Nom de la quête : "+quete.getNom());
		labN.setTheme("/label");
		
		ArrayList<String> o_s = new ArrayList<String>();
		for(int i = 0; i < quete.getObjectifs().size(); i++)
		{
			o_s.add(quete.getObjectifs().get(i).getDescription());
		}
		SimpleChangableListModel<String> lm = new SimpleChangableListModel<String>(o_s);
		@SuppressWarnings("rawtypes")
		ListBox lb = new ListBox<String>(lm);
		lb.setTheme("/listbox");
		
		Button okButton = new Button("OK");
		okButton.setTheme("/button");
		okButton.addCallback(new Runnable() {
			
			@Override
			public void run() {
				frame.setVisible(false);
			}
		});
		
		
		DialogLayout l = new DialogLayout();
		l.setTheme("/dialoglayout");
		l.setHorizontalGroup(l.createParallelGroup().addWidget(labN).addWidget(lb).addWidget(okButton, Alignment.CENTER));
		l.setVerticalGroup(l.createSequentialGroup().addWidget(labN).addGap(40).addWidget(lb).addGap().addWidget(okButton));
		
		frame.add(l);
		
		return frame;
		
	}
	
	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public ArrayList<QueteObjectif> getObjectifs() {
		return objectifs;
	}

	public void setObjectifs(ArrayList<QueteObjectif> objectifs) {
		this.objectifs = objectifs;
	}
}
