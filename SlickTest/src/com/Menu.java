package com;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JComboBox;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class Menu extends BasicGameState{

	public static final int ID = 2;
	ArrayList annee;
	ArrayList pilote;
	int positionAnnee;
	int positionMenu;
	int positionPilote;
	private Input input;
	
	
	@Override
	public void init(GameContainer container, StateBasedGame arg1)
			throws SlickException {
		// TODO Auto-generated method stub
		File f=new File("data");
        String[] liste=f.list();
        annee = new ArrayList();
        for (int i=0; i<liste.length; i++) {
         File ff=new File(liste[i]);
         annee.add(liste[i]);
         
        }
        
        positionAnnee = 0;
        positionMenu = 0;
        
        
    	getPilotes();
        
        
        
        input=container.getInput();
        
        
        
	}

	private void getPilotes() {
		positionPilote=0;
		pilote=new ArrayList();
    	
    	BufferedReader buff;
		try {
			buff = new BufferedReader(new FileReader("data/"+(String)annee.get(positionAnnee)+"/driver"));
		
	    	String str = null;
	    	 String[] mots = null ;
	    	 while((str = buff.readLine()) != null){
	    	     // On traite la ligne obtenue
	    	     mots = str.split(";") ;
	    	     pilote.add(mots[0]);

	    	 }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void render(GameContainer container, StateBasedGame arg1, Graphics g)
			throws SlickException {
		g.setColor(Color.white);
    	g.fillRect(0, 0, container.getWidth(), container.getHeight());
    	g.setColor(Color.black);
		// TODO Auto-generated method stub
		if(positionMenu == 0)
			g.drawString("->", 170, 200);
		else
			g.drawString("->", 170, 250);
		g.drawString("Choix de l'année : "+(String)annee.get(positionAnnee), 200, 200);
		g.drawString("Choix du pilote : "+(String)pilote.get(positionPilote), 200, 250);
	}

	@Override
	public void update(GameContainer arg0, StateBasedGame arg1, int arg2)
			throws SlickException {
		// TODO Auto-generated method stub
		
		if(input.isKeyPressed(Keyboard.KEY_DOWN))
		{
			positionMenu++;
			if(positionMenu>1)
				positionMenu=0;
			
		}
		
		if(input.isKeyPressed(Keyboard.KEY_UP))
		{
			positionMenu--;
			if(positionMenu<0)
				positionMenu=1;		
		}
    	if(input.isKeyPressed(Keyboard.KEY_LEFT))
    	{
    		if(positionMenu == 0)
    		{
    			positionAnnee--;
    			if(positionAnnee<0)
    				positionAnnee = annee.size()-1;
    			getPilotes();
    		}
    		else if(positionMenu == 1)
    		{
    			positionPilote--;
    			if(positionPilote<0)
    				positionPilote = pilote.size()-1;
    		}
    	}
    	if(input.isKeyPressed(Keyboard.KEY_RIGHT))
    	{
    		if(positionMenu == 0)
    		{
    			positionAnnee++;
    			if(positionAnnee>(annee.size()-1))
    				positionAnnee = 0;
    			getPilotes();
    		}
    		else if(positionMenu == 1)
    		{
    			positionPilote++;
    			if(positionPilote>(pilote.size()-1))
    				positionPilote = 0;
    		}
    	}
    	if(input.isKeyPressed(Keyboard.KEY_SPACE))
    	{
    		Variable.annee = (String)annee.get(positionAnnee);
    		Variable.pilote = (String)pilote.get(positionPilote);
    		arg1.addState(Variable.jeu);
    		arg1.enterState(Variable.jeu.getID());
    	}

		
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return 2;
	}

}
