package com.gameplay;
import org.newdawn.slick.geom.Vector2f;

public class QueteObjectif
{
		private String description;
		private boolean accompli;
		private Object objectif;
		private String type;
		
		public QueteObjectif(String description, String text_objectif)
		{
			this.description = description;
			
			if(text_objectif.contains("tile"))
			{
				String[] pos = text_objectif.substring(5, text_objectif.length()-1).split(",");
				this.objectif = new Vector2f(Float.parseFloat(pos[0]), Float.parseFloat(pos[1]));
				type = "endroit";
			}
		}
		
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public boolean isAccompli() {
			return accompli;
		}
		public void setAccompli(boolean accompli) {
			this.accompli = accompli;
		}

		public Object getObjectif() {
			return objectif;
		}

		public void setObjectif(Object objectif) {
			this.objectif = objectif;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}
}