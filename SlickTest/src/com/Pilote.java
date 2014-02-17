package com;

import java.util.ArrayList;

public class Pilote implements Comparable{
	
	String nom;
	static int etat;
	int pneu;
	private int victoire;
	String infoPilote;
	int performancePneu;
	boolean abandon;
	int q1;
	int q2;
	int q3;
	int evolution;
	int tauxUsure;
	int nbArret;
	int minInf;
	int minSup;
	int min;
	int maxInf;
	int maxSup;
	int max;
	int meilleurTemps;
	boolean joueur;
	boolean arretStand;
	int [] point;
	int dernierePosition;
	int positionDepart;
	int [] score;
	int distance;
	int depassement;
	public static int mTemps;
	public static String nomMTemps;
	private Team team;
	private String newTeam;
	private String champion;
	private int age;
	int usureVoiture;
	
	private int pole;
	private int podium;

	public Pilote(String nom,int minInf,int minSup,int maxInf,int maxSup,int depassement,Team team,String champion,int age,int victoire,int podium,int pole)
	{
		this.pole = pole;
		this.podium = podium;
		this.victoire = victoire;
		
		this.nom=nom;
		mTemps=0;
		etat=0;
		this.minInf=minInf;
		this.minSup=minSup;
		this.maxInf=maxInf;
		this.maxSup=maxSup;
		this.team = team;
		this.champion = champion;
		score=new int[4] ;
		distance=0;
		evolution = 0;
		point=new int[21];
		point[0]=0;
		infoPilote="";
		joueur=false;
		this.age = age;
		
		this.depassement=depassement;
		newTeam = "";
		if(this.team != null)
			this.debutCourse();
		
	}
	
	public void reset()
	{
		//Evolution du pilote
		int evol;
		evolution =0;
		if(age>30)
		{
			evol = point[0]/(20+age-30);
			evolution = evol;
			minInf += evol;
			maxInf +=evol;
			minSup +=evol;
			maxSup +=evol;
		}
		else
		{
			evol = GameState.nombreAleaBorne(0, (31-age)*5)+point[0]/20;
			evolution = evol;
			minInf += evol;
			maxInf +=evol;
			minSup +=evol;
			maxSup +=evol;
		}
		age++;
		
		point=new int[21];
		point[0]=0;
		newTeam = "";
		this.debutCourse();
	}
	
	public void changePneu()
	{
		if(GameState.nombreAleaBorne(0, 100)<50)
		{
			changePneu(0);
		}
		else
		{
			changePneu(1);
		}
	}
	
	public void changePneu(int type)
	{
		this.pneu = type;
		if(type == 0)
		{
			this.performancePneu = 1200;
		}
		else if(type == 1)
		{
			this.performancePneu = 1100;
		}
	}
	
	public void debutCourse()
	{
		int teamPerf = team.aleaPerformance();
		min=(GameState.nombreAleaBorne(minInf, minSup)+teamPerf*3/2)/2;
		max=(GameState.nombreAleaBorne(maxInf, maxSup)+teamPerf*3/2)/2;
		distance=0;
		mTemps=0;
		tauxUsure=0;
		usureVoiture=0;
		nbArret=0;
		this.meilleurTemps = 0;
		dernierePosition=0;
		abandon=false;
		
		changePneu();
		
	}
	
	public void ajoutQ2() {
		q2=0;
		changePneu(0);
		for (int j = 0; j < 2; j++) {
			score[3]=0;
			for (int i = 0; i < 3; i++) {
				score[i] = GameState.nombreAleaBorne(min, max)
						+ performancePneu;

			}
			for (int i = 0; i < 3; i++) {
				score[3] += score[i];
			}
			if(score[3]>q2)
			{
				q2 = score[3];
				distance = q2;
			}
		}
		changePneu();
	}
	
	public void ajoutQ3() {
		changePneu();
		q3=0;
		for (int j = 0; j < 1; j++) {
			score[3]=0;
			for (int i = 0; i < 3; i++) {
				score[i] = GameState.nombreAleaBorne(min, max)
						+ performancePneu;

			}
			for (int i = 0; i < 3; i++) {
				score[3] += score[i];
			}
			if(score[3]>q3)
			{
				q3 = score[3];
				distance = q3;
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
		}
		
		
	}

	public int getPole() {
		return pole;
	}

	public void setPole(int nbPole) {
		this.pole = nbPole;
	}
	
	public void addPole() {
		this.pole++;
	}

	public int getPodium() {
		return podium;
	}

	public void setPodium(int nbPodium) {
		this.podium = nbPodium;
	}
	
	public void addPodium() {
		this.podium++;
	}

	public void ajoutQ1(){
		q1=0;
		changePneu(0);
		for (int j = 0; j < 3; j++) {
			score[3]=0;
			for (int i = 0; i < 3; i++) {
				score[i] = GameState.nombreAleaBorne(min, max)
						+ performancePneu;

			}
			for (int i = 0; i < 3; i++) {
				score[3] += score[i];
			}
			if(score[3]>q1)
			{
				q1 = score[3];
				distance = q1;
			}
		}
		changePneu();
		
	}
	
	public void ajoutDistance(int tour,int position,Pilote devant,int tourTotal){
		arretStand=false;
		infoPilote="";
		score[3]=0;
			
		if(!abandon)
		{
			dernierePosition=position;
			for (int i = 0; i < 3; i++) {
				score[i] = GameState.nombreAleaBorne(min, max)
						+ performancePneu + tour * 50-usureVoiture;

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
			if(devant!=null && tour>0 && !devant.arretStand && !devant.abandon && devant.distance<this.distance && this.distance-devant.distance<1000 && GameState.nombreAleaBorne(0, 100)<5)
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
			
			//Changement de pneu
			if (performancePneu == 0
					|| (performancePneu * 15 < GameState.nombreAleaBorne(0,
							10000) && (tour < tourTotal-10 || performancePneu < (tourTotal - tour) * 100))) {
				arretStand(Variable.minArret,Variable.maxArret);
			}
			if (score[3] > mTemps && tour>0) {
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
	
	public void arretStand(int min,int max){
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
		arretStand=true;
		distance -= arret;
		tauxUsure = 0;
		if (GameState.nombreAleaBorne(0, 100) < 50) {
			changePneu(0);
		} else {
			changePneu(1);
		}
	}
	
	public void calculEcart(Pilote premier)
	{
		String retard;
		retard=Integer.toString((premier.distance-this.distance)/1000)+".";
		int temp=premier.distance-this.distance-((premier.distance-this.distance)/1000)*1000;
		if(temp<10)
			retard+="00"+Integer.toString(temp);
		else if(temp<100)
			retard+="0"+Integer.toString(temp);
		else
			retard+=Integer.toString(temp);
		
		infoPilote+="Retard : "+retard;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}
	
	public String toString(){
		String retour="";
		retour=nom+"("+getTeam().getNom()+")";
		if(etat<3)
		{
			
		}
		else if(etat==3)
		{
			
			if(abandon)
				retour +=" a abandonné alors qu'il était "+dernierePosition;
			else
				retour+=" Temps --> "+GameState.temps(score[3])+" "+((pneu==0)?"option":"prime")+" "+performancePneu+" Usure : "+usureVoiture+" "+infoPilote;
		}
		else if(etat==4)
		{
			retour+=" Position au départ : "+this.positionDepart+" Meilleur Temps --> "+GameState.temps(meilleurTemps);
		}
		else
		{
			
		}
		return retour;
		
	}

	@Override
	public int compareTo(Object other) {
		// TODO Auto-generated method stub
		int nombre1=0;
		int nombre2=0;
		if(etat<5)
		{
			nombre1 = ((Pilote) other).getDistance(); 
		    nombre2 = this.getDistance();
		}
		else
		{
			nombre1 = ((Pilote) other).point[0]; 
		    nombre2 = this.point[0]; 
		}
	    if (nombre1 > nombre2)  return 1; 
	    else if(nombre1 == nombre2) return 0; 
	    else return -1; 
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public String getNewTeam() {
		return newTeam;
	}

	public void setNewTeam(String newTeam) {
		this.newTeam = newTeam;
	}

	public String getChampion() {
		return champion;
	}

	public void setChampion(String champion) {
		this.champion = champion;
	}
	
	public void addChampion(String annee){
		this.champion+="-"+annee;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
	
	public void addAge(){
		this.age++;
	}

	public int getVictoire() {
		return victoire;
	}

	public void setVictoire(int victoire) {
		this.victoire = victoire;
	}
	
	public void addVictoire()
	{
		this.victoire++;
	}

	
	
	
}
