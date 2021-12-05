package fr.unice.polytech.citadelle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import fr.unice.polytech.citadelle.game.DeckDistrict;
import fr.unice.polytech.citadelle.game.District;
import fr.unice.polytech.citadelle.game.Player;
import fr.unice.polytech.citadelle.game_interactor.RushBot;

public class RushBotTest {
	Player player;
	RushBot rusher;
	DeckDistrict deckDistrict;

	@BeforeEach
	public void init() {
		deckDistrict = new DeckDistrict();
		deckDistrict.initialise();
		player = new Player("Player1");
		rusher = spy(new RushBot(player));
	}

	@Test
	public void normalBehaviourNoCardButGoldTest() {
		player = new Player("Player1");
		rusher = spy(new RushBot(player));

		player.getDistrictCards().clear();

		player.setGolds(15);

		rusher.normalBehaviour(deckDistrict);
		verify(rusher, times(1)).takeCard(any(), any());
	}

	@Test
	public void normalBehaviourNoCardTest() {
		ArrayList<District> districtsCards = player.getDistrictCards();
		districtsCards.clear();
		rusher.normalBehaviour(deckDistrict);
		verify(rusher, times(1)).takeCard(any(), any());
		verify(rusher, times(0)).takeGold();
	}

	@Test
	public void normalBehaviourTooExpansivesDistrictsTest() {
		int tooExpansiveValue = 4;
		ArrayList<District> districtsCards = player.getDistrictCards();
		districtsCards.clear();
		districtsCards.add(new District("testDistrict", tooExpansiveValue, "testColor", "testFamily"));
		rusher.normalBehaviour(deckDistrict);
		verify(rusher, times(1)).takeCard(any(), any());
		verify(rusher, times(0)).takeGold();
	}

	@Test
	public void normalBehaviourABuildableDistrictTest() {
		int cheapValue = 3;

		ArrayList<District> districtsCards = player.getDistrictCards();
		districtsCards.clear();
		player.getCity().getBuiltDistrict().clear();
		player.setGolds(0);

		District newCheapDistrict = new District("testDistrict", cheapValue, "testColor", "testFamily");
		districtsCards.add(newCheapDistrict);

		rusher.normalBehaviour(deckDistrict);
		verify(rusher, times(1)).takeGold();
	}

	// ---

	@Test
	public void endGameBehaviourNoCardTest() {
		ArrayList<District> districtsCards = player.getDistrictCards();
		districtsCards.clear();
		rusher.endGameBehaviour(deckDistrict);
		verify(rusher, times(1)).takeCard(any(), any());
		verify(rusher, times(0)).takeGold();
	}

	@Test
	public void endGameBehaviourCanBuildNewDistrictWithFuturGoldDistrictsTest() {
		int tooExpansiveValueForNow = 3;

		ArrayList<District> districtsCards = player.getDistrictCards();
		districtsCards.clear();
		player.getCity().getBuiltDistrict().clear();
		rusher.getPlayer().setGolds(1);

		districtsCards.add(new District("testDistrict", tooExpansiveValueForNow, "testColor", "testFamily"));

		rusher.endGameBehaviour(deckDistrict);
		verify(rusher, times(1)).takeGold();
	}
	
	@Test
	public void ifPossibleBuildACheapDistrictOneCheapTest() {
		District aCheaperBuildableDistrict = new District("aCheaperBuildableDistrict", 2, "colorTest", "familyTest");
		District aOtherBuildableDistrict = new District("aOtherBuildableDistrict", 5, "colorTest", "familyTest");

		player.getCity().getBuiltDistrict().clear();
		player.getDistrictCards().clear();
		
		player.getDistrictCards().add(aCheaperBuildableDistrict);
		player.getDistrictCards().add(aOtherBuildableDistrict);
		
		assertEquals(player.getCity().getSizeOfCity(), 0);
		rusher.ifPossibleBuildACheapDistrict();
		assertEquals(player.getCity().getSizeOfCity(), 1);
		assertEquals(player.getCity().getBuiltDistrict().get(0), aCheaperBuildableDistrict);
	}
	
	@Test
	public void ifPossibleBuildACheapDistrictTwoCheapTest() {
		District aCheaperBuildableDistrict = new District("aCheaperBuildableDistrict", 1, "colorTest", "familyTest");
		District aOtherCheaperBuildableDistrict = new District("aOtherBuildableDistrict", 2, "colorTest", "familyTest");

		player.getCity().getBuiltDistrict().clear();
		player.getDistrictCards().clear();
		
		player.getDistrictCards().add(aCheaperBuildableDistrict);
		player.getDistrictCards().add(aOtherCheaperBuildableDistrict);
		
		assertEquals(player.getCity().getSizeOfCity(), 0);
		rusher.ifPossibleBuildACheapDistrict();
		assertEquals(player.getCity().getSizeOfCity(), 1);
		assertEquals(player.getCity().getBuiltDistrict().get(0), aCheaperBuildableDistrict);
	}
	
	@Test
	public void ifPossibleBuildACheapDistrictZeroCheapTest() {
		District aNotCheaperBuildableDistrict = new District("aCheaperBuildableDistrict", 10, "colorTest", "familyTest");
		District aOtherNotCheaperBuildableDistrict = new District("aOtherBuildableDistrict", 5, "colorTest", "familyTest");

		player.getCity().getBuiltDistrict().clear();
		player.getDistrictCards().clear();
		
		player.getDistrictCards().add(aNotCheaperBuildableDistrict);
		player.getDistrictCards().add(aOtherNotCheaperBuildableDistrict);
		
		assertEquals(player.getCity().getSizeOfCity(), 0);
		rusher.ifPossibleBuildACheapDistrict();
		assertEquals(player.getCity().getSizeOfCity(), 0);
	}
	
	@Test
	public void ifPossibleBuildACheapDistrict2CheapEqualityTest() {
		District aCheaperBuildableDistrict = new District("aCheaperBuildableDistrict", 2, "colorTest", "familyTest");
		District aOtherCheaperBuildableDistrict = new District("aOtherBuildableDistrict", 2, "colorTest", "familyTest");

		player.getCity().getBuiltDistrict().clear();
		player.getDistrictCards().clear();
		
		player.getDistrictCards().add(aCheaperBuildableDistrict);
		player.getDistrictCards().add(aOtherCheaperBuildableDistrict);
		
		assertEquals(player.getCity().getSizeOfCity(), 0);
		rusher.ifPossibleBuildACheapDistrict();
		assertEquals(player.getCity().getSizeOfCity(), 1);
		assertEquals(player.getCity().getBuiltDistrict().get(0), aCheaperBuildableDistrict);
	}

}