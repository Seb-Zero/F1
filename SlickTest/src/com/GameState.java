package com;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;
import java.util.jar.JarEntry;

import javax.swing.text.Document;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
//import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.gui.*;
/**
 ** C'est l'état principal du jeu, c'est ici que l'on codera l'action du jeu !
 */
public class GameState extends BasicGameState
{
    public static final int ID = 1;
    Input input;
    ArrayList pilotes;
    PiloteJoueur piloteJoueur;
    ArrayList reserves;
    ArrayList teams;
    int tour;
    int tourTotal;
    static int minute;
    static int seconde;
    static int milliSeconde;
    int [] points;
    int numGP;
    int annee;
    boolean chargement;
    
    boolean stat;
    
    @Override
    public int getID() {return ID;}
    /* (non-Javadoc)
     * @see org.newdawn.slick.state.GameState#init(org.newdawn.slick.GameContainer, org.newdawn.slick.state.StateBasedGame)
     */
    
    public void chargement()
    {
    	
    	tour=0;
    	annee = Integer.parseInt(Variable.annee);
    	
    	initTeam();
    	
    	//Pilotes officiels
    	pilotes=new ArrayList();
    	reserves=new ArrayList();
    	BufferedReader buff;
		try {
			buff = new BufferedReader(new FileReader("data/"+Variable.annee+"/driver"));
		
	    	String str = null;
	    	 String[] mots = null ;
	    	 while((str = buff.readLine()) != null){
	    	     // On traite la ligne obtenue
	    	     mots = str.split(";") ;
	    	     if(mots[0].equalsIgnoreCase(Variable.pilote))
	    	     {
	    	    	 this.piloteJoueur = new PiloteJoueur(mots[0],Integer.parseInt(mots[1]),Integer.parseInt(mots[2]),
	    	    			 Integer.parseInt(mots[3]),Integer.parseInt(mots[4]),Integer.parseInt(mots[5]),getTeam(mots[6]),mots[7],Integer.parseInt(mots[8]),
	    	    			 Integer.parseInt(mots[9]),Integer.parseInt(mots[10]),Integer.parseInt(mots[11]));
	    	    	 pilotes.add(this.piloteJoueur);
	    	     }
    	    	 else
    	    		 pilotes.add(new Pilote(mots[0],Integer.parseInt(mots[1]),Integer.parseInt(mots[2]),
	    	    			 Integer.parseInt(mots[3]),Integer.parseInt(mots[4]),Integer.parseInt(mots[5]),getTeam(mots[6]),mots[7],Integer.parseInt(mots[8]),
	    	    			 Integer.parseInt(mots[9]),Integer.parseInt(mots[10]),Integer.parseInt(mots[11])));

	    	 }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try{
			buff = new BufferedReader(new FileReader("data/"+Variable.annee+"/reserve"));
			
	    	String str = null;
	    	 String[] mots = null ;
	    	 while((str = buff.readLine()) != null){
	    	     // On traite la ligne obtenue
	    	     mots = str.split(";") ;
    	    		 reserves.add(new Pilote(mots[0],Integer.parseInt(mots[1]),Integer.parseInt(mots[2]),
	    	    			 Integer.parseInt(mots[3]),Integer.parseInt(mots[4]),Integer.parseInt(mots[5]),getTeam(mots[6]),mots[7],Integer.parseInt(mots[8]),
	    	    			 Integer.parseInt(mots[9]),Integer.parseInt(mots[10]),Integer.parseInt(mots[11])));

	    	 }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	
    	
    	
    	points=new int[10];
    	points[0]=25;
    	points[1]=18;
    	points[2]=15;
    	points[3]=12;
    	points[4]=10;
    	points[5]=8;
    	points[6]=6;
    	points[7]=4;
    	points[8]=2;
    	points[9]=1;
    	
    	numGP=1;
    	tourTotal=this.nombreAleaBorne(40, 80);
    	milliSeconde = this.nombreAleaBorne(0, 999);
    	seconde = this.nombreAleaBorne(30, 59);
    	minute = 1;
    	Pilote.etat = 7;
    	this.stat = false;
    }
    
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
    	
    	input=container.getInput();
    	chargement= false;
    }
    
    public void nouvelleSaison()
    {
    	numGP=1;
    	Collections.sort(teams);
    	Collections.sort(pilotes);
    	
    	//On remplace les derniers pilotes du classement par ceux de réserve qui ont un contrat
    	int compteur = pilotes.size()-1;
    	for(int i=0;i<reserves.size();i++)
    	{
    		Pilote reserveTemp = ((Pilote)reserves.get(i));
    		if(reserveTemp.getNewTeam() != "")
    		{
	    		boolean trouve = false;
	    		while(!trouve)
	    		{
	    			Pilote piloteTemp = ((Pilote)pilotes.get(compteur));
	    			if(piloteTemp.getNewTeam() == "")
	    			{
	    				pilotes.set(compteur, reserveTemp);
	    				reserves.set(i, piloteTemp);
	    				trouve=true;
	    			}
	    			compteur--;
	    		}
    		}
    	}
    	
    	for(int i=0;i<pilotes.size();i++)
    	{
			Pilote piloteTemp=((Pilote)pilotes.get(i));
			if(piloteTemp.getNewTeam() == "")
			{
				for(int j=0;j<teams.size();j++)
				{
					Team teamTemp = (Team)teams.get(j);
					if(teamTemp.getNombrePilote() <2)
					{
						piloteTemp.setNewTeam(teamTemp.getNom());
						teamTemp.setNombrePilote(teamTemp.getNombrePilote()+1);
						break;
					}
				}
			}
			piloteTemp.setTeam(getTeam(piloteTemp.getNewTeam()));
			piloteTemp.reset();
    	}
    	for(int j=0;j<teams.size();j++)
		{
			Team teamTemp = (Team)teams.get(j);
			teamTemp.reset(800-j*25);
		}
    }
    
    public Team getTeam(String nom)
    {
    	for(int i=0;i<teams.size();i++)
    	{
    		Team team = (Team)teams.get(i);
    		if(nom.equalsIgnoreCase(team.getNom()))
    			return team;
    	}
    	return null;
    }
    
    public void initTeam()
    {
    	teams = new ArrayList();
    	Team team;
    	BufferedReader buff;
		try {
			buff = new BufferedReader(new FileReader("data/"+Variable.annee+"/team"));
		
	    	String str = null;
	    	 String[] mots = null ;
	    	 while((str = buff.readLine()) != null){
	    	     // On traite la ligne obtenue
	    	     mots = str.split(";") ;
	    	     team = new Team(mots[0],Integer.parseInt(mots[1]),Integer.parseInt(mots[2]),Integer.parseInt(mots[3]));
	    	     teams.add(team);
	    	 }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    	
    	
    	
    	
    }
    
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
    	
        g.setColor(Color.white);
    	g.fillRect(0, 0, container.getWidth(), container.getHeight());
    	g.setColor(Color.black);
    	Collections.sort(pilotes);
    	
    	//Prochain pneu 
    	String pneu = (piloteJoueur.getProchainPneu() == 0)?"Option (Touche P pour changer)":"Prime (Touche O pour changer)";
    	g.drawString("Pneu "+pneu, 400, 10);
    	
    	if(Pilote.etat == 0)
    	{
    		g.drawString("Q1",50,10);
	    	for(int i=0;i<pilotes.size();i++)
	    	{
	    		Pilote pil=(Pilote)(pilotes.get(i));
	    		g.drawString((i+1)+" "+pil.toString()+" -> "+temps(pil.q1), 10, 70+20*i);
	    	}
	    	
    	}
    	
    	else if(Pilote.etat == 1)
    	{
    		Collections.sort(pilotes);
    		g.drawString("Q2",50,10);
	    	for(int i=0;i<(pilotes.size()+10)/2;i++)
	    	{
	    		Pilote pil=(Pilote)(pilotes.get(i));
	    		g.drawString((i+1)+" "+pil.toString()+" -> "+temps(pil.q1)+" -> "+temps(pil.q2), 10, 70+20*i);
	    	}
	    	for(int i=(pilotes.size()+10)/2;i<pilotes.size();i++)
	    	{
	    		Pilote pil=(Pilote)(pilotes.get(i));
	    		g.drawString((i+1)+" "+pil.toString()+" -> "+temps(pil.q1), 10, 70+20*i);
	    	}
    	}
    	else if(Pilote.etat == 2)
    	{
    		Collections.sort(pilotes);
    		g.drawString("Q3",50,10);
	    	for(int i=0;i<10;i++)
	    	{
	    		Pilote pil=(Pilote)(pilotes.get(i));
	    		pneu = (pil.pneu == 0)?"Option":"Prime";
	    		g.drawString((i+1)+" "+pil.toString()+" -> "+temps(pil.q1)+" -> "+temps(pil.q2)+ " -> "+temps(pil.q3)+" (Pneu "+pneu+")", 10, 70+20*i);
	    		
	    	}
	    	for(int i=10;i<(pilotes.size()+10)/2;i++)
	    	{
	    		Pilote pil=(Pilote)(pilotes.get(i));
	    		pneu = (pil.pneu == 0)?"Option":"Prime";
	    		g.drawString((i+1)+" "+pil.toString()+" -> "+temps(pil.q1)+" -> "+temps(pil.q2)+" (Pneu "+pneu+")", 10, 70+20*i);
	    	}
	    	for(int i=(pilotes.size()+10)/2;i<pilotes.size();i++)
	    	{
	    		Pilote pil=(Pilote)(pilotes.get(i));
	    		pneu = (pil.pneu == 0)?"Option":"Prime";
	    		g.drawString((i+1)+" "+pil.toString()+" -> "+temps(pil.q1)+" (Pneu "+pneu+")", 10, 70+20*i);
	    	}
    	}
    	else if(Pilote.etat==3)
    	{
    		Collections.sort(pilotes);
	    		g.drawString(Integer.toString(tour)+"/"+Integer.toString(tourTotal),50,10);
	    	if(Pilote.mTemps>0)
	    		g.drawString("Meilleur temps : "+Pilote.nomMTemps+" --> "+temps(Pilote.mTemps), 50, 30);
	    	for(int i=0;i<pilotes.size();i++)
	    	{
	    		Pilote pil=(Pilote)(pilotes.get(i));
	    		g.drawString((i+1)+"/ ("+pil.dernierePosition+" - "+pil.positionDepart+")"+pil.toString(), 10, 70+20*i);
	    	}
    	}
    	else if(Pilote.etat==4)
    	{
	    		g.drawString("Resultat final",50,10);
	    	if(Pilote.mTemps>0)
	    		g.drawString("Meilleur temps : "+Pilote.nomMTemps+" --> "+temps(Pilote.mTemps), 50, 30);
	    	for(int i=0;i<pilotes.size();i++)
	    	{
	    		Pilote pil=(Pilote)(pilotes.get(i));
	    		g.drawString((i+1)+"/ "+pil.toString(), 10, 70+20*i);
	    	}
    	}
    	else if(Pilote.etat==5){
    		g.drawString("Nombre de grand prix : "+(numGP-1), 10, 30);
    		for(int i=0;i<pilotes.size();i++)
	    	{
    			
	    		Pilote pil=(Pilote)(pilotes.get(i));
	    		
	    		g.drawString((i+1)+"/ "+pil.toString(), 10, 70+30*i);
	    		if(!this.stat)
	    		{
	    				g.drawString(pil.point[0]+" points", 400, 70+30*i);
		    			g.drawString(Integer.toString(pil.evolution), 10, 85+30*i);
		    			
		    		for(int j=0;j<numGP-1;j++){
		    			g.drawString(Integer.toString(pil.point[j+1]), 500+30*j, 70+30*i);
		    		}
	    		}
	    		else
	    		{
	    			g.drawString("Victoire : "+pil.getVictoire()+" Podium : "+pil.getPodium()+" Pole : "+pil.getPole(), 400, 70+30*i);
	    			g.drawString(pil.getChampion(), 10, 85+30*i);
	    			
	    		}
	    	}
    		
    	}
    	else if(Pilote.etat == 6)
    	{
    		Collections.sort(teams);
    		g.drawString("Nombre de grand prix : "+(numGP-1), 10, 30);
    		for(int i=0;i<teams.size();i++)
	    	{
    			
	    		Team team=(Team)(teams.get(i));
	    		g.drawString((i+1)+"/ "+team.toString(), 10, 70+20*i);
	    		for(int j=0;j<numGP-1;j++){
	    			g.drawString(Integer.toString(team.getPoint()[j+1]), 400+40*j, 70+20*i);
	    		}
	    	}
    	}
    	else if(Pilote.etat == 7)
    	{
    		g.drawString("Transfert", 10, 30);
    		for(int j=0;j<teams.size();j++)
    		{
    			Team team = (Team)(teams.get(j));
    			g.drawString((j+1)+"/ "+team.getNom(), 10, 70+60*j);
    			g.drawRect(0, 70+60*j, 3000, 60);
    			int k=0;
	    		for(int i=0;i<pilotes.size();i++)
		    	{
	    			
		    		Pilote pil=(Pilote)(pilotes.get(i));
		    		if(pil.getNewTeam() == team.getNom())
		    		{
		    			String lib;
		    			if(i==0)
		    				lib = " er";
		    			else
		    				lib=" ème";
		    			g.drawString(pil.nom+ " ("+pil.getTeam().getNom()+") : "+(i+1)+lib, 200, 70+60*j+k*20);
		    			k++;
		    		}
		    		
		    		
		    	}
	    		for(int i=0;i<reserves.size();i++)
		    	{
	    			
		    		Pilote pil=(Pilote)(reserves.get(i));
		    		if(pil.getNewTeam() == team.getNom())
		    		{
		    			
		    			g.drawString(pil.nom+ " (Sans écurie)", 200, 70+60*j+k*20);
		    			k++;
		    		}
		    		
		    		
		    	}
	    		
    		}
    	}
    	else if(Pilote.etat == 8)
    	{
    		Collections.sort(teams);
    		g.drawString("Nombre de grand prix : "+(numGP-1), 10, 30);
    		for(int i=0;i<teams.size();i++)
	    	{
    			
	    		Team team=(Team)(teams.get(i));
	    		g.drawString((i+1)+"/ "+team.toString(), 10, 70+40*i);
	    		g.drawString("Min : "+Integer.toString(team.getMinPerformance())+ " " +
	    				"- Ancien : "+Integer.toString(team.getAncienMinPerformance())+" " +
	    						"- Evolution : +"+Integer.toString(team.getMinPerformance()-team.getAncienMinPerformance()), 440, 70+40*i);
	    		g.drawString("Max : "+Integer.toString(team.getMaxPerformance())+ 
	    				" - Ancien : "+Integer.toString(team.getAncienMaxPerformance())+
	    				"- Evolution : +"+Integer.toString(team.getMaxPerformance()-team.getAncienMaxPerformance()), 440, 70+40*i+20);
	    	}
    	}
    	
    	
    	
    }
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
    	if(!chargement)
    	{
    		chargement= true;
    		chargement();
    	}
    		
    	/*
    	 * Différents états
    	 * Etat 0 : q1
    	 * Etat 1 : q2
    	 * Etat 2 : q3
    	 * Etat 3 : GP
    	 * Etat 4 : Resultat final
    	 * Etat 5 : classement pilote
    	 * Etat 6 : Classement constructeur
    	 * Etat 7 : Evolution
    	 * 
    	 * 
    	 */
    	boolean bas=false;
    	boolean droite=false;
    	boolean gauche=false;
    	boolean haut = false;
    	boolean passe = false;
    	boolean option = false;
    	boolean prime = false;
    	
    	if(input.isKeyPressed(Keyboard.KEY_ESCAPE))
    	{
    		chargement = false;
    		game.enterState(Variable.menu.getID());
    	}
    	
    	
    	
    	if(input.isKeyPressed(Keyboard.KEY_DOWN) || input.isKeyPressed(Keyboard.KEY_S))
    		bas=true;
    	if(input.isKeyPressed(Keyboard.KEY_LEFT) || input.isKeyPressed(Keyboard.KEY_Q))
    		gauche=true;
    	if(input.isKeyPressed(Keyboard.KEY_RIGHT) || input.isKeyPressed(Keyboard.KEY_D))
    		droite=true;
    	if(input.isKeyPressed(Keyboard.KEY_UP) || input.isKeyPressed(Keyboard.KEY_Z))
    		haut=true;
    	if(input.isKeyPressed(Keyboard.KEY_SPACE))
    		passe=true;
    	if(input.isKeyPressed(Keyboard.KEY_P))
    		prime=true;
    	if(input.isKeyPressed(Keyboard.KEY_O))
    		option=true;
    	
    	
    	//Prochain pneu
    	if(option || prime)
    	{
        	if(option)
        		piloteJoueur.setProchainPneu(0);
        	else if(prime)
        		piloteJoueur.setProchainPneu(1);
    	}
    	
        
        if (bas || droite || gauche || haut || passe)
        {
	        	//Après la Q1
	        	if(Pilote.etat == 0)
	        	{
	        		q2();
	        		Pilote.etat = 1;
	        		
	        	}
	        	else if(Pilote.etat == 1)
	        	{
	        		q3();
	        		Pilote.etat = 2;
	        	}
	        	
	        	else if(Pilote.etat ==2)
	        	{
	        		grilleDepart();
	        		Pilote.etat = 3;
	        	}
	        	
	        	if(Pilote.etat==3)
	        	{
	        		if(haut)
	        		{
	        			while(tour<tourTotal)
	            		{
	    		        	for(int i=0;i<pilotes.size();i++)
	    		        	{
	    		        		Pilote piloteTemp=((Pilote)pilotes.get(i));
	    		        		Pilote piloteDevant=null;
	    		        		if(i>0)
	    		        			piloteDevant=((Pilote)pilotes.get(i-1));
	    		        		piloteTemp.ajoutDistance(tour,i+1,piloteDevant,tourTotal);
	    		
	    		        		
	    		        	}
	    		        	Collections.sort(pilotes);
	    		        	Pilote premier=((Pilote)pilotes.get(0));
	    		        	for(int i=0;i<pilotes.size();i++)
	    		        	{
	    		        		Pilote piloteTemp=((Pilote)pilotes.get(i));
	    		        		piloteTemp.calculEcart(premier);
	    		
	    		        		
	    		        	}
	    		        	tour++;
	            		}
	    	        	if(tour>tourTotal){
	    	        		attributionPoint();
	    	        		Pilote.etat = 4;
	    	        	}
	        		}
	        		else
	        		{
	        			for(int i=0;i<pilotes.size();i++)
	    	        	{
	    	        		Pilote piloteTemp=((Pilote)pilotes.get(i));
	    	        		Pilote piloteDevant=null;
	    	        		if(i>0)
	    	        			piloteDevant=((Pilote)pilotes.get(i-1));
	    	        		
	    	        		if(piloteTemp.joueur)
	    	        		{
	    	        			
	    	        			PiloteJoueur joueurTemp=(PiloteJoueur)piloteTemp;
	    	        			joueurTemp.ajoutDistance(tour,i+1,piloteDevant,tourTotal,true);
	    	        			if(gauche || droite)
	    	        				joueurTemp.arretStand(Variable.minArret, Variable.maxArret, (joueurTemp.getProchainPneu() == 0)?true:false);
	    	        			
	    	        		}
	    	        		else
	    	        			piloteTemp.ajoutDistance(tour,i+1,piloteDevant,tourTotal);
	    	
	    	        		
	    	        	}
	    	        	Collections.sort(pilotes);
	    	        	Pilote premier=((Pilote)pilotes.get(0));
	    	        	for(int i=0;i<pilotes.size();i++)
	    	        	{
	    	        		Pilote piloteTemp=((Pilote)pilotes.get(i));
	    	        		piloteTemp.calculEcart(premier);
	    	
	    	        		
	    	        	}
	    	        	tour++;
	    	        	if(tour>tourTotal){
	    	        		attributionPoint();
	    	        		Pilote.etat = 4;
	    	        	}
	        		}
	        	}
	        	else if(Pilote.etat == 4)
	        	{
	        		Pilote.etat = 5;
	        	}
	        	else if(Pilote.etat==5)
	        	{
	        		if(bas || passe)
	        		{
		        		classementConstructeur();
		        		Pilote.etat = 6;
		        		this.stat = false;
	        		}
	        		else if(gauche || droite)
	        		{
	        			if(this.stat)
	        				this.stat = false;
	        			else
	        				this.stat=true;
	        		}
	        	}
	        	else if(Pilote.etat == 6)
	        	{
	        		evolution();
	        		Pilote.etat = 7;
	        	}
	        	else if(Pilote.etat == 7)
	        	{
	        		transfert();
	        		if(numGP>20)
	        		{
	        			((Pilote)pilotes.get(0)).addChampion(Integer.toString(this.annee));
	        			this.annee++;
	        			nouvelleSaison();
	        		}
	        		Pilote.etat = 8;
	        			
	        	}
	        	else if(Pilote.etat ==8)
	        	{
	        		q1();
	        	}
        }        
    }
    
    private void grilleDepart()
    {
    	for(int i=0;i<pilotes.size();i++)
    	{
			Pilote piloteTemp=((Pilote)pilotes.get(i));
			piloteTemp.distance = 100*(24-i);
			piloteTemp.positionDepart = i+1;
    	}
    	Collections.sort(pilotes);
    }
    
    private void q1() {
		// TODO Auto-generated method stub
    	
    	debutCourse();
		Pilote.etat=0;
		for(int i=0;i<pilotes.size();i++)
    	{
			Pilote piloteTemp=((Pilote)pilotes.get(i));
			piloteTemp.ajoutQ1();
    	}
		Collections.sort(pilotes);
	}
    
    private void q2()
    {
    	
		for(int i=0;i<(pilotes.size()+10)/2;i++)
    	{
			Pilote piloteTemp=((Pilote)pilotes.get(i));
			piloteTemp.ajoutQ2();
    	}
		for(int i=(pilotes.size()+10)/2;i<pilotes.size();i++)
    	{
			Pilote piloteTemp=((Pilote)pilotes.get(i));
			piloteTemp.distance = 0;
    	}
		Collections.sort(pilotes);
    }
    
    private void q3()
    {
		for(int i=0;i<10;i++)
    	{
			Pilote piloteTemp=((Pilote)pilotes.get(i));
			piloteTemp.ajoutQ3();
    	}
		for(int i=10;i<pilotes.size();i++)
    	{
			Pilote piloteTemp=((Pilote)pilotes.get(i));
			piloteTemp.distance = 0;
    	}
		Collections.sort(pilotes);
		Pilote piloteTemp=((Pilote)pilotes.get(0));
		piloteTemp.addPole();
    }
    
	public void classementConstructeur()
    {
    	
		Pilote.etat=6;
    }
	
	public void transfert()
	{
		//Algorithme de transfert
		Collections.sort(pilotes);
		Collections.sort(teams);
		Pilote piloteTemp;
		for(int i=0;i<pilotes.size();i++)
		{
			piloteTemp = (Pilote)pilotes.get(i);
			if(piloteTemp.getNewTeam() == "")
			{
				for(int j=0;j<teams.size();j++)
				{
					Team teamTemp = (Team)teams.get(j);
					int affinite = 1;
					if(piloteTemp.getTeam().getNom() == teamTemp.getNom())
						affinite = 15;
					int chance = nombreAleaBorne(0, 100000);
					if(chance<((teamTemp.getPoint()[0]*4+piloteTemp.point[0]*4+1)*affinite*25/piloteTemp.getAge()) && teamTemp.getNombrePilote()<2)
					{
						piloteTemp.setNewTeam(teamTemp.getNom());
						teamTemp.setNombrePilote(teamTemp.getNombrePilote()+1);
						break;
					}
				}
			}
		}
		for(int i=0;i<reserves.size();i++)
		{
			piloteTemp = (Pilote)reserves.get(i);
			if(piloteTemp.getNewTeam() == "")
			{
				for(int j=0;j<teams.size();j++)
				{
					Team teamTemp = (Team)teams.get(j);
					int affinite = 1;
					int chance = nombreAleaBorne(0, 100000);
					if(chance<((teamTemp.getPoint()[0]*4)*affinite)*25/piloteTemp.getAge() && teamTemp.getNombrePilote()<2)
					{
						piloteTemp.setNewTeam(teamTemp.getNom());
						teamTemp.setNombrePilote(teamTemp.getNombrePilote()+1);
						break;
					}
				}
			}
		}
		Pilote.etat=8;
	}
    
    public void evolution()
    {
    	for(int i=0;i<teams.size();i++)
    	{
    		Team team=(Team)(teams.get(i));
    		team.evolution(numGP);
    	}
    	Pilote.etat = 7;
    	
    }
    
    public void attributionPoint()
    {
    	Pilote piloteTemp;
		
		for(int i=0;i<10;i++)
		{
			piloteTemp=((Pilote)pilotes.get(i));
			piloteTemp.point[0]+=points[i];
			piloteTemp.point[numGP]=points[i];
			
			piloteTemp.getTeam().ajoutPoint(points[i], numGP);
			if(i==0)
				piloteTemp.addVictoire();
			if(i<3)
				piloteTemp.addPodium();
				
		}
		
		numGP++;
    }
    
    public void debutCourse()
    {
    	tourTotal=this.nombreAleaBorne(40, 80);
		tour=0;
		milliSeconde = this.nombreAleaBorne(0, 999);
    	seconde = this.nombreAleaBorne(30, 59);
    	minute = 1;
    	for(int i=0;i<pilotes.size();i++)
    	{
    		Pilote piloteTemp=((Pilote)pilotes.get(i));
    		
    		piloteTemp.debutCourse();
    	}
    }
    
    
    public static int nombreAleaBorne(int min,int max){
		Random r = new Random();
		int valeur = min + r.nextInt(max - min);
		return valeur;

	}
    
    public static String temps(int score)
    {
    	int tempMilli = milliSeconde;
    	int tempSec = seconde;
    	int tempMinu = minute;
    	if(tempMilli-(score%1000)<0)
    	{
    		tempMilli+=1000;
    		
    		tempSec-=1;
    	}
    	
    	tempMilli-=score%1000;
    	
    	if(tempSec-(score/1000)<0)
    	{
    		tempSec+=60;
    		tempMinu -=1;
    	}
    	
    	tempSec-=score/1000;
    	
    	String retour = tempMinu+":";
    	
    	if(tempSec>9)
    		retour +=tempSec;
    	else
    		retour += "0"+tempSec;
    	
    	retour += ".";
    	
    	if(tempMilli>99)
    	{
    		retour += tempMilli;
    	}
    	else if(tempMilli>9)
    		retour += "0"+tempMilli;
    	else
    		retour += "00"+tempMilli;
    	
    	return retour;
    }
}
