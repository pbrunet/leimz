package com.client.utils.gui;

import com.game_entities.PNJ;

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
		String s = pnj.getPnjDiscours().get(0).getDiscours();
		String d = s.replaceAll("#n", "<br/>");
		System.out.println(d);
		String sb = "<div style=\"word-wrap: break-word; font-family: default; \"><p>"+d+"</p></div>";

		textAreaModel.setHtml(sb);

		SimpleChangableListModel<String> lm = new SimpleChangableListModel<String>(
				pnj.getPnjDiscours().get(0).getReponses());
		final ListBox<String> lb = new ListBox<String>(lm);
		lb.setTheme("/listbox");
		lb.addCallback(new CallbackWithReason<ListBox.CallbackReason>() {

			@Override
			public void callback(CallbackReason cbreason) 
			{
				if(cbreason == CallbackReason.MOUSE_CLICK) 
				{
					int s = lb.getSelected()+1;
					for(int k = 0; k < pnj.getPnjDiscours().size(); k++)
					{
						System.out.println(s+" -- "+pnj.getPnjDiscours().get(k).getReponses().get(0));
						if(pnj.getPnjDiscours().get(k).getTree_reponses().get(pnj.getPnjDiscours().get(k).getTree_reponses().size()-1) == s)
						{
							String d = pnj.getPnjDiscours().get(k).getDiscours().replaceAll("#n", "<br/>");
							String sb = "<div style=\"word-wrap: break-word; font-family: default; \"><p>"+d+"</p></div>";
							textAreaModel.setHtml(sb);

							if(pnj.getPnjDiscours().get(k).getReponses() == null || pnj.getPnjDiscours().get(k).getReponses().size()==0)
							{
								setVisible(false);
								return;
							}
							else
							{
								SimpleChangableListModel<String> sclm = new SimpleChangableListModel<String>(
										pnj.getPnjDiscours().get(k).getReponses());
								lb.setModel(sclm);
								return;
							}
						}
					}
					setVisible(false);
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
