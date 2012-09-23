package com.client.utils.gui;

import java.util.ArrayList;

import com.game_entities.PNJ;
import com.gameplay.PNJ_discours;

import de.matthiasmann.twl.Alignment;
import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.CallbackWithReason;
import de.matthiasmann.twl.DialogLayout;
import de.matthiasmann.twl.ListBox;
import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.TextArea;
import de.matthiasmann.twl.ListBox.CallbackReason;
import de.matthiasmann.twl.model.SimpleChangableListModel;
import de.matthiasmann.twl.textarea.HTMLTextAreaModel;

public class PnjDialogFrame extends ResizableFrame
{
	private PNJ pnj;

	public PnjDialogFrame(PNJ pnj)
	{
		this.pnj = pnj;

		this.setTheme("/resizableframe");
		this.setTitle(pnj.getNom());

		init();
	}

	private void init()
	{
		final HTMLTextAreaModel textAreaModel = new HTMLTextAreaModel();
		TextArea area = new TextArea(textAreaModel);
		area.setTheme("/textarea");
		PNJ_discours discour = pnj.getPnjDiscours();
		String s = discour.getDiscours();
		final ArrayList<Integer> path = new ArrayList<Integer>();
		String d = s.replaceAll("#n", "<br/>");
		System.out.println(d);
		String sb = "<div style=\"word-wrap: break-word; font-family: default; \"><p>"+d+"</p></div>";

		textAreaModel.setHtml(sb);

		SimpleChangableListModel<String> lm = new SimpleChangableListModel<String>(
				discour.getReponsesString());
		final ListBox<String> lb = new ListBox<String>(lm);
		lb.setTheme("/listbox");
		lb.addCallback(new CallbackWithReason<ListBox.CallbackReason>() {

			@Override
			public void callback(CallbackReason cbreason) 
			{
				if(cbreason == CallbackReason.MOUSE_CLICK) 
				{
					PNJ_discours curr = pnj.getPnjDiscours();
					path.add(lb.getSelected());
					for(int i =0;i<path.size();i++)
					{
						if(curr.getReponses().get(path.get(i)).getReponses() == null || curr.getReponses().get(path.get(i)).getReponses().size()==0)
						{
							setVisible(false);
							path.clear();
							return;
						}
						curr = curr.getReponses().get(path.get(i)).getReponses().get(0);
					}
					String d = curr.getDiscours().replaceAll("#n", "<br/>");
					String sb = "<div style=\"word-wrap: break-word; font-family: default; \"><p>"+d+"</p></div>";
					textAreaModel.setHtml(sb);
					
					SimpleChangableListModel<String> sclm = new SimpleChangableListModel<String>(
							curr.getReponsesString());
					lb.setModel(sclm);
					return;
				}
			}

		});

		Button aButton = new Button("Annuler");
		aButton.setTheme("/button");
		aButton.addCallback(new Runnable()
		{

			@Override
			public void run() {
				setVisible(false);
			}

		});


		DialogLayout l = new DialogLayout();
		l.setTheme("/dialoglayout");
		l.setHorizontalGroup(l.createParallelGroup().addWidget(area).addWidget(lb).addWidget(aButton, Alignment.CENTER));
		l.setVerticalGroup(l.createSequentialGroup().addWidget(area).addGap(40).addWidget(lb).addGap().addWidget(aButton));

		this.add(l);
	}
}
