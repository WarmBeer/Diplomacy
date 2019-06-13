package controllers;

import models.GameModel;
import observers.GameObserver;

public class GameController  {

    private GameModel gamemodel = new GameModel();

    public void requestLoadGame(){
        try{
            gamemodel.loadGame();
        }
        catch(Exception E){
            System.out.println("Exception while request to load a game");
            E.printStackTrace();
        }
    }

    public void registerViewObserver(GameObserver gameobserver){
        gamemodel.registerObserver(gameobserver);
    }

    public void addOrderIsClicked(String action, String prov1, String prov2){
        gamemodel.addOrder(action, prov1, prov2);
    }
}