package com;

import javax.swing.text.Document;
import javax.xml.ws.Response;

public class PiloteJoueur extends Pilote{
	
	private int prochainPneu;

	public PiloteJoueur(String nom, int minInf, int minSup, int maxInf,
			int maxSup,int depassement,Team team,String champion,int age,int victoire,int podium,int pole) {
		super(nom, minInf, minSup, maxInf, maxSup,depassement,team,champion,age,victoire,podium,pole);
		joueur=true;
		
		prochainPneu = 0;
		// TODO Auto-generated constructor stub
	}
	
	public void ajoutDistance(int tour,int position,Pilote devant, int tourTotal, boolean controle){
		infoPilote="";
		score[3]=0;
		arretStand=false;
		
		if(!abandon)
		{
			dernierePosition=position;
			for (int i = 0; i < 3; i++) {
				score[i] = GameState.nombreAleaBorne(min, max)
						+ performancePneu + tour * 50;

			}
			for (int i = 0; i < 3; i++) {
				distance += score[i];
				score[3] += score[i];
			}
			System.out.println(nom+" "+score[3]);
			if (devant != null && devant.distance > this.distance
					&& (devant.distance - this.distance) < 1000 && tour>0) {
				int temp=GameState.nombreAleaBorne(100, 1000);
				distance -= temp;
				score[3] -= temp;
				
				System.out.println("Ralenti "+temp);
			}
			if (pneu == 0) {
				tauxUsure += 4;
				if (GameState.nombreAleaBorne(0, 100) < tauxUsure) {
					performancePneu -= GameState.nombreAleaBorne(0, 150);
				}
			} else if (pneu == 1) {
				tauxUsure += 2;
				if (GameState.nombreAleaBorne(0, 100) < tauxUsure) {
					performancePneu -= GameState.nombreAleaBorne(0, 100);
				}
			}
			//Tentative de dépassement échoué
			if(devant!=null && devant.distance<this.distance && tour>0 && !devant.arretStand && !devant.abandon && depassement<GameState.nombreAleaBorne(0, 100))
			{
				int alea=GameState.nombreAleaBorne(100,500);
				int diff=this.distance-devant.distance+alea;
				score[3]=score[3]-diff;
				this.distance=devant.distance-alea;
				
			}
			
			//accident avec vehicule devant
			if(devant!=null && tour>0 && devant.distance<this.distance && !devant.arretStand && !devant.abandon && this.distance-devant.distance<1000 && GameState.nombreAleaBorne(0, 100)<5)
			{
				this.infoPilote+="|| accident avec "+devant.nom+" ||";
				devant.infoPilote+="|| accident avec "+this.nom+" ||";
				int alea=GameState.nombreAleaBorne(1, 100);
				if(alea>90)
				{
					this.abandon=true;
					distance=0;
				}
				else if(alea>80)
					arretStand(40000, 80000);
				else if(alea>50)
					arretStand(20000, 40000);
				else if(alea>20)
					arretStand(15000, 25000);
				alea=GameState.nombreAleaBorne(1, 100);
				usureVoiture +=alea;
				
				alea=GameState.nombreAleaBorne(1, 100);
				if(alea>90)
				{
					devant.abandon=true;
					devant.distance=0;
				}
				else if(alea>80)
					devant.arretStand(40000, 80000);
				else if(alea>50)
					devant.arretStand(20000, 40000);
				else if(alea>20)
					devant.arretStand(15000, 20000);
				
				alea=GameState.nombreAleaBorne(1, 100);
				devant.usureVoiture +=alea;
				
			}
			if (score[3] > mTemps) {
				mTemps = score[3];
				nomMTemps = this.nom;
			}
			if(score[3]>meilleurTemps){
				meilleurTemps = score[3];
			}
			
			if(GameState.nombreAleaBorne(1, 10000)<tour)
			{
				abandon=true;
				distance=0;
				this.infoPilote="|| Abandon ||";
			}
		}
	}
	
	public void changePneu()
	{
			changePneu(this.prochainPneu);
	}
	
	public void arretStand(int min,int max,boolean pneuTendre){
		int alea = GameState.nombreAleaBorne(0, 100);
		int arret;
		if(alea<50)
			arret=GameState.nombreAleaBorne(min, (max-min)*20/100+min);
		else if(alea<70)
			arret=GameState.nombreAleaBorne((max-min)*20/100+min, (max-min)*40/100+min);
		else if(alea<80)
			arret=GameState.nombreAleaBorne((max-min)*40/100+min, (max-min)*60/100+min);
		else if(alea<90)
			arret=GameState.nombreAleaBorne((max-min)*60/100+min, (max-min)*80/100+min);
		else
			arret=GameState.nombreAleaBorne((max-min)*80/100+min, max);
		infoPilote += "|| Arret " + (++nbArret) + " : "+arret+" || ";
		distance -= arret;
		arretStand=true;
		tauxUsure = 0;
		if (pneuTendre) {
			changePneu(0);
		} else {
			changePneu(1);
		}
	}

	public int getProchainPneu() {
		return prochainPneu;
	}

	public void setProchainPneu(int prochainPneu) {
		this.prochainPneu = prochainPneu;
	}

}
