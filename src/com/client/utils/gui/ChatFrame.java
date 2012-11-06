package com.client.utils.gui;

import org.lwjgl.Sys;

import org.newdawn.slick.geom.Vector2f;

import com.client.network.NetworkManager;
import com.game_entities.Joueur;

import de.matthiasmann.twl.DialogLayout;
import de.matthiasmann.twl.EditField;
import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.ScrollPane;
import de.matthiasmann.twl.TextArea;
import de.matthiasmann.twl.textarea.HTMLTextAreaModel;


public class ChatFrame extends ResizableFrame
{
	private final StringBuilder sb;
	private final HTMLTextAreaModel textAreaModel;
	private final TextArea textArea;


	private final EditField editField;
	private final ScrollPane scrollPane;
	private String curColor = "black";

	private Joueur main_player;


	public ChatFrame(Vector2f size, Joueur main_player) {

		this.main_player = main_player;

		this.sb = new StringBuilder();
		this.textAreaModel = new HTMLTextAreaModel();
		this.textArea = new TextArea(textAreaModel);
		this.textArea.setTheme("/textarea");
		this.editField = new EditField();
		this.editField.setTheme("/editfield");


		editField.addCallback(new EditField.Callback() {
			public void callback(int key) {
				if(key == Event.KEY_RETURN) {
					// cycle through 3 different colors/font styles
					String path = null;
					if(curColor.equals("black"))
					{
						path = "default";
					}
					else
					{
						path = "font_"+curColor;                    	
					}

					appendWhenCallBack(path);

					editField.setText("");
					curColor = "black";
				}
			}
		});

		textArea.addCallback(new TextArea.Callback() {
			public void handleLinkClicked(String href) {
				Sys.openURL(href);
			}
		});

		scrollPane = new ScrollPane(textArea);
		scrollPane.setTheme("/scrollpane");
		scrollPane.setFixed(ScrollPane.Fixed.HORIZONTAL);
		scrollPane.setPosition(70, 20);

		DialogLayout l = new DialogLayout();
		l.setTheme("/dialoglayout");
		l.setHorizontalGroup(l.createParallelGroup(scrollPane, editField));
		l.setVerticalGroup(l.createSequentialGroup(scrollPane, editField));

		this.add(l);

	}

	public void appendRow(String font, String text) {
		sb.append("<div style=\"word-wrap: break-word; font-family: ").append(font).append("; \">");
		// not efficient but simple
		for(int i=0,l=text.length() ; i<l ; i++) {
			char ch = text.charAt(i);
			switch(ch) {
			case '<': sb.append("&lt;"); break;
			case '>': sb.append("&gt;"); break;
			case '&': sb.append("&amp;"); break;
			case '"': sb.append("&quot;"); break;
			case ':':
				if(text.startsWith(":)", i)) {
					sb.append("<img src=\"smiley\" alt=\":)\"/>");
					i += 1; // skip one less because of i++ in the for loop
					break;
				}
				sb.append(ch);
				break;
			case 'h':
				if(text.startsWith("http://", i)) {
					int end = i + 7;
					while(end < l && isURLChar(text.charAt(end))) {
						end++;
					}
					String href = text.substring(i, end);
					sb.append("<a style=\"font: link\" href=\"").append(href)
					.append("\" >").append(href)
					.append("</a>");
					i = end - 1; // skip one less because of i++ in the for loop
					break;
				}
			case '/':
				if(text.startsWith("/n", i)) {
					sb.append("<br/>");
					i+=1;
					break;
				}
				// fall through:
			default:
				sb.append(ch);
			}
		}
		sb.append("</div>");

		boolean isAtEnd = scrollPane.getMaxScrollPosY() == scrollPane.getScrollPositionY();

		textAreaModel.setHtml(sb.toString());

		if(isAtEnd) {
			scrollPane.validateLayout();
			scrollPane.setScrollPositionY(scrollPane.getMaxScrollPosY());
		}
	}

	public void appendWhenCallBack(String path)
	{
		System.out.println("Texte de l'�ditfield : " + editField.getText());
		/*if(editField.getText().contains("\\i"))
    	{
    		appendRow("font_red", "/nInventaire");
    		appendRow(path, "---------------------------");
    		appendRow(path, "/n");
			for(int i = 0; i < main_player.getPerso().getInventaire().getObjets().size(); i++)
			{
				appendRow("font_red", "Objet " + i);
				appendRow(path, "--> NOM : " + main_player.getPerso().getInventaire().getObjets().get(i).getNom());
				appendRow(path, "--> DESCRIPTION : " + main_player.getPerso().getInventaire().getObjets().get(i).getDescription());
				appendRow(path, "--> TYPE : " + main_player.getPerso().getInventaire().getObjets().get(i).getType());

				if(main_player.getPerso().getInventaire().getObjets().get(i).getEffet() != null)
					appendRow(path, "--> EFFET : " + main_player.getPerso().getInventaire().getObjets().get(i).getEffet().get(0).toString() + ", " + main_player.getPerso().getInventaire().getObjets().get(i).getEffet().get(0));

				else
					appendRow(path, "--> EFFET : Aucun effet");
				appendRow(path, "/n");
			}
			appendRow(path, "-----------------------------");
    	}*/
		if(editField.getText().contains("\\pos"))
		{
			appendRow("font_red","/n/nPosition : " + "[" + main_player.getTile().getPos().x + "][" + main_player.getTile().getPos().y + "]");
			appendRow(path, "/n/n");
		}
		else
		{
			System.out.println("envoi !!");
			NetworkManager.instance.sendToServer("sa;"+"fazega;"+editField.getText());
		}
	}


	public void refresh(Joueur main_player)
	{
		this.main_player = main_player;

		String receive = NetworkManager.instance.receiveFromServer("sa");
		if(receive != null)
		{
			String[] temp = receive.split(";");
			appendRow("normal", temp[1] + " : " + temp[2]);
		}
	}

	private boolean isURLChar(char ch) {
		return (ch == '.') || (ch == '/') || (ch == '%') ||
				(ch >= '0' && ch <= '9') ||
				(ch >= 'a' && ch <= 'z') ||
				(ch >= 'A' && ch <= 'Z');
	}

	@Override
	protected void layout() {
		super.layout();
	}


	public String getCurColor() {
		return curColor;
	}

	public void setCurColor(String curColor) {
		this.curColor = curColor;
	}

}