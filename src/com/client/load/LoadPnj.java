package com.client.load;

import java.util.ArrayList;
import com.client.entities.Orientation;
import com.client.entities.PNJ;
import com.gameplay.PNJ_discours;
import com.map.client.managers.MapManager;

/**
 * 
 * @author chelendil
 * Classe chargeant les informations de classe et de race du joueur (sort, competences ...)
 */
public class LoadPnj
{
	
	public static PNJ loadPnj(String str)
	{
		PNJ pnj;
		String[] args_pnj = str.split(";");
		PNJ_discours pnj_discours = new PNJ_discours(args_pnj[3], null, new ArrayList<PNJ_discours>(),0);
		pnj_discours.fillTree(4, args_pnj);
		pnj = new PNJ(
					args_pnj[2], 
					pnj_discours,
					Orientation.BAS, 
					MapManager.instance.getEntire_map().getGrille().get(Integer.parseInt(args_pnj[0])).get(Integer.parseInt(args_pnj[1])));
		System.out.println(str);
		return pnj;
	}
}
