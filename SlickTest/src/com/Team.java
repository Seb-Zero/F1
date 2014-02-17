package com;

public class Team implements Comparable{
	private int id;
	private String nom;
	private int nombrePilote;
	private int minPerformance;
	private int ancienMinPerformance;
	private int maxPerformance;
	private int ancienMaxPerformance;
	private int usurePneu;
	private int [] point;
	private static int nombre = 0;
	
	
	public Team(String nom,int budget,int minPerformance,int maxPerformance)
	{
		setId(nombre++);
		this.nom = nom;
		this.minPerformance = minPerformance;
		this.maxPerformance = maxPerformance;
		point=new int[21];
		for(int i=0;i<point.length;i++)
			point[i] = 0;
		nombrePilote = 0;
		
		
	}
	
	public void reset(int base)
	{
		point=new int[21];
		for(int i=0;i<point.length;i++)
			point[i] = 0;
		nombrePilote = 0;
		this.minPerformance = base-300+GameState.nombreAleaBorne(1, 200);
		this.maxPerformance = base + GameState.nombreAleaBorne(1, 200);
		if(this.minPerformance>= this.maxPerformance)
			this.minPerformance -= GameState.nombreAleaBorne(1, 200);
	}
	
	public void ajoutPoint(int point,int numGp)
	{
		this.point[0]+=point;
		this.point[numGp]+=point;
	}

	
	public int aleaPerformance()
	{
		return GameState.nombreAleaBorne(minPerformance, maxPerformance);
		
	}
	
	public void evolution(int numGP)
	{
		ancienMinPerformance = minPerformance;
		ancienMaxPerformance = maxPerformance;
		
		this.minPerformance += GameState.nombreAleaBorne(0, 40)+point[numGP-1]/2;
		this.maxPerformance += GameState.nombreAleaBorne(0, 40)+point[numGP-1]/2;
		if(minPerformance>=maxPerformance)
			minPerformance = maxPerformance-1;
	}
	
	@Override
	public int compareTo(Object other) {
		int nombre1=0;
		int nombre2=0;
		nombre1 = ((Team) other).point[0]; 
		nombre2 = this.point[0]; 
	    if (nombre1 > nombre2)  return 1; 
	    else if(nombre1 == nombre2) return 0; 
	    else return -1; 
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	
	public int getMinPerformance() {
		return minPerformance;
	}

	public void setMinPerformance(int minPerformance) {
		this.minPerformance = minPerformance;
	}

	public int getMaxPerformance() {
		return maxPerformance;
	}

	public void setMaxPerformance(int maxPerformance) {
		this.maxPerformance = maxPerformance;
	}

	public int getUsurePneu() {
		return usurePneu;
	}

	public void setUsurePneu(int usurePneu) {
		this.usurePneu = usurePneu;
	}

	public int [] getPoint() {
		return point;
	}

	public void setPoint(int [] point) {
		this.point = point;
	}



	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}
	
	public String toString(){
		String retour=nom+" : "+point[0]+" points";
		return retour;
		
	}

	public int getAncienMinPerformance() {
		return ancienMinPerformance;
	}

	public void setAncienMinPerformance(int ancienMinPerformance) {
		this.ancienMinPerformance = ancienMinPerformance;
	}

	public int getAncienMaxPerformance() {
		return ancienMaxPerformance;
	}

	public void setAncienMaxPerformance(int ancienMaxPerformance) {
		this.ancienMaxPerformance = ancienMaxPerformance;
	}

	public int getNombrePilote() {
		return nombrePilote;
	}

	public void setNombrePilote(int nombrePilote) {
		this.nombrePilote = nombrePilote;
	}

}
