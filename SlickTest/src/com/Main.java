package com;
import javax.swing.Icon;
import javax.swing.plaf.IconUIResource;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;


public class Main extends StateBasedGame
{
    
    private AppGameContainer container; // le conteneur du jeu
   public Main() {
	   super("Mon premier jeu");
   } // le constructeur de la classe
    @Override
   public void initStatesList(GameContainer container) throws SlickException 
    {
        if (container instanceof AppGameContainer) {
        this.container = (AppGameContainer) container;// on stocke le conteneur du jeu !
        }
        Variable.menu = new Menu();
        Variable.jeu = new GameState();//le jeu en lui m�me !!
        Variable.annee = "2012";
        container.setShowFPS(false);//on ne veut pas voir le FPS ?? mettre alors "false" !
        //addState(menu);
        addState(Variable.menu);    //on ajoute le GameState au conteneur !s
        addState(Variable.jeu);
        
        
    }
    public static void main(String[] args) 
   {
        try
       {
             AppGameContainer container = new AppGameContainer(new Main());
             container.setDisplayMode(1280, 1000, false);// fen�tre de 1280*768 fullscreen =false !!
             container.setTargetFrameRate(60);// on r�gle le FrameRate
             container.start();//on d�marre le container
        }                       
        catch (SlickException e) {e.printStackTrace();}  // l'exception de base de slick !!
    }
}  // fin de classe