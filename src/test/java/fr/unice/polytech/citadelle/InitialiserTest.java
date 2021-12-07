package fr.unice.polytech.citadelle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import fr.unice.polytech.citadelle.game.Board;
import fr.unice.polytech.citadelle.game.Character;
import fr.unice.polytech.citadelle.game.DeckCharacter;
import fr.unice.polytech.citadelle.game.Player;
import fr.unice.polytech.citadelle.game_engine.Initialiser;
import fr.unice.polytech.citadelle.game_interactor.Behaviour;

public class InitialiserTest {
	Initialiser init;
	LinkedHashMap<Character, Optional<Behaviour>> hashOfCharacters;
	ArrayList<Character> listOfAllCharacters;
	ArrayList<Behaviour> listOfBehaviour; 
	ArrayList<Player> listOfPlayer;

	
	@BeforeEach
	public void init() {
		init = new Initialiser();
		hashOfCharacters= new LinkedHashMap<Character, Optional<Behaviour>>();
		listOfAllCharacters= new ArrayList<Character>();
		listOfBehaviour = new ArrayList<Behaviour>();
		listOfPlayer = new ArrayList<Player>();


	}
	
	@Test
	public void initHashOfCharacterTest() {
		listOfAllCharacters.add(new Character("test0", 0));
		listOfAllCharacters.add(new Character("test1", 1));
		listOfAllCharacters.add(new Character("test2", 2));
		listOfAllCharacters.add(new Character("test3", 3));
		listOfAllCharacters.add(new Character("test4", 4));
		listOfAllCharacters.add(new Character("test5", 5));
		listOfAllCharacters.add(new Character("test6", 6));
		listOfAllCharacters.add(new Character("test7", 7));

		
		LinkedHashMap<Character, Optional<Behaviour>> hashOfCharactersSpy = spy(hashOfCharacters);
		init.resetHashOfCharacter(hashOfCharactersSpy, listOfAllCharacters);
		verify(hashOfCharactersSpy, times(8)).put(Mockito.any(Character.class), Mockito.any(Optional.class));
	}
	
	@Test
	public void initListOfAllCharacterTest() {
		ArrayList<Character> listOfAllCharactersSpy = spy(listOfAllCharacters);
		listOfAllCharactersSpy = init.createListOfAllCharacter();
		assertEquals(listOfAllCharactersSpy.size(), 8);
	}
	
	@Test
	public void initListOfBehaviourTest() {
    	Board board = new Board();
		ArrayList<Behaviour> listOfBehaviourSpy = spy(listOfBehaviour);
		ArrayList<Player> listOfPlayerSpy = spy(listOfPlayer); 
		
		listOfBehaviourSpy = init.createListOfBehaviour(board);
		assertEquals(listOfBehaviourSpy.size(), Initialiser.NUMBER_OF_PLAYER);

	}
	
	@Test
	public void fillHashOfCharacterTest() {
    	Board board = new Board();
		LinkedHashMap<Character, Optional<Behaviour>> hashOfCharactersSpy = spy(hashOfCharacters);
		init.fillHashOfCharacter(hashOfCharactersSpy, new Character("testCharacter", 0), new Behaviour(new Player("testPlayer"), board));
		verify(hashOfCharactersSpy, times(1)).put(Mockito.any(Character.class), Mockito.any(Optional.class));
	}
}
