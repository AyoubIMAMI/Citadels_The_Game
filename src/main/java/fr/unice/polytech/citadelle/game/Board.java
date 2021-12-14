package fr.unice.polytech.citadelle.game;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import fr.unice.polytech.citadelle.game_engine.RoundManager;
import fr.unice.polytech.citadelle.game_interactor.Behaviour;


/**
 * A Game represent a instance of the game Citadelle with all the cards and players that go with.
 * @author BONNET Killian, IMAMI Ayoub, KARRAKCHOU Mourad, LE BIHAN Léo
 */
public class Board {
    private ArrayList<Player> listOfPlayer;
    private DeckDistrict deckDistrict;
    private DeckCharacter deckCharacter;
    private int roundNumber = 0;
    private ArrayList<Character> listOfCharacter;

    public Board( ArrayList<Player> listOfPlayer,ArrayList<Character>listOfCharacter, DeckDistrict deckDistrict, DeckCharacter deckCharacter) {
        this.listOfPlayer = listOfPlayer;
        this.deckDistrict = deckDistrict;
        this.deckCharacter = deckCharacter;
        this.listOfCharacter=listOfCharacter;
    }

    public Board() {
		// TODO Auto-generated constructor stub
	}

	public ArrayList<Player> getListOfPlayer(){
        return listOfPlayer;
    }


	public DeckDistrict getDeckDistrict() {
		return deckDistrict;
	}

	public DeckCharacter getDeckCharacter() {
		return deckCharacter;
	}

    public void setListOfPlayer(ArrayList<Player> listOfPlayer) {
		this.listOfPlayer = listOfPlayer;
	}
	
    public void incrementRoundNumber() {
        roundNumber++;
    }

    public int getRoundNumber() { return roundNumber;}

    public ArrayList<Character> getListOfCharacter(){return listOfCharacter;}
}

