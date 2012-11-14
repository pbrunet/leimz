package com.client.utils.gui;

import org.lwjgl.Sys;

import org.newdawn.slick.geom.Vector2f;

import com.client.display.gui.GUI_Manager;
import com.client.entities.Joueur;
import com.client.entities.MainJoueur;
import com.client.network.NetworkListener;
import com.client.network.NetworkManager;
import com.game_entities.managers.EntitiesManager;

import de.matthiasmann.twl.DialogLayout;
import de.matthiasmann.twl.EditField;
import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.ScrollPane;
import de.matthiasmann.twl.TextArea;
import de.matthiasmann.twl.Widget;
import de.matthiasmann.twl.textarea.HTMLTextAreaModel;
import de.matthiasmann.twl.textarea.SimpleTextAreaModel;


public class ChatFrame extends ResizableFrame implements NetworkListener
{
	private final StringBuilder sb;
    private final HTMLTextAreaModel textAreaModel;
    private final TextArea textArea;
    

	private final EditField editField;
    private final ScrollPane scrollPane;
    private String curColor = "black";
    
    

    public ChatFrame(Vector2f size) {

        
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
    		appendRow("font_red","/n/nPosition : " + "[" + MainJoueur.instance.getTile().getPos().x + "][" + MainJoueur.instance.getTile().getPos().y + "]");
    		appendRow(path, "/n/n");
    	}
    	else
    	{
    		NetworkManager.instance.sendToServer("sa;"+MainJoueur.instance.getPerso().getNom()+";"+editField.getText());
    	}
    }
    

	@Override
	public void receiveMessage(String str) 
	{
		String[] temp = str.split(";");
		
		if(temp[0].matches("sa"))
    	{
    		appendRow("default", temp[1] + " : " + temp[2]);
    		createBulle(temp[1], temp[2]);
    	}
	}
	
	private void createBulle(String nom_perso, String text)
	{
		Joueur joueur = EntitiesManager.instance.getPlayers_manager().getJoueur(nom_perso);

		ResizableFrame container = new ResizableFrame();
		container.setTheme("/resizableframe");
		
		HTMLTextAreaModel model = new HTMLTextAreaModel();
		TextArea bulle = new TextArea(model);
		bulle.setTheme("/textarea");
		model.setHtml("<div style=\"word-wrap: break-word; font-family: default; \">"+text+"</div>");
		/*ScrollPane scroll = new ScrollPane(bulle);
		scroll.setTheme("/scrollpane");*/

		container.add(bulle);
		container.adjustSize();
		GUI_Manager.instance.getRoot().add(container);
		
		
	
		container.setPosition((int)(joueur.getPos_real_on_screen().x-
				bulle.getWidth())+28, (int)(joueur.getPos_real_on_screen().y-
						bulle.getHeight()));
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