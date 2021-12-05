package fr.unice.polytech.citadelle.game_interactor;

import java.nio.file.DirectoryStream;
import java.util.*;
import java.util.stream.Collectors;

import fr.unice.polytech.citadelle.game.DeckDistrict;
import fr.unice.polytech.citadelle.game.District;
import fr.unice.polytech.citadelle.game.Board;
import fr.unice.polytech.citadelle.game.Player;
import fr.unice.polytech.citadelle.game_engine.PhaseManager;
import fr.unice.polytech.citadelle.output.PrintCitadels;
import fr.unice.polytech.citadelle.game.Character;

/**
 * A Behaviour realize all the action of a player.
 * 
 * @author BONNET Killian, IMAMI Ayoub, KARRAKCHOU Mourad, LE BIHAN Léo
 */
public class Behaviour {
	// The player controlled by the Behaviour.
	protected final Player player;
	protected final PrintCitadels printC = new PrintCitadels();
	protected Boolean botIsKing = false;
	protected int numberOfCharacter = 8;

	// ---
	CityManagement cityMan;
	Executor executor;
	private static final int ONE_CARD = 1;
	private static final int TWO_CARD = 2;

	public Behaviour(Player player) {
		this.player = player;
		cityMan = new CityManagement(player);
		executor = new Executor(player);
	}

	/**
	 * @param pickedDistricts The two picked cards.
	 * @return The district having the higher value.
	 */
	public District selectTheHigherDistrict(DeckDistrict deckDistrict, ArrayList<District> pickedDistricts) {
		int cardOneValue = pickedDistricts.get(0).getValue();
		int cardTwoValue = pickedDistricts.get(1).getValue();

		if (cardOneValue >= cardTwoValue) {
			deckDistrict.addDistrict(pickedDistricts.get(1));
			return pickedDistricts.get(0);
		}
		deckDistrict.addDistrict(pickedDistricts.get(0));
		return pickedDistricts.get(1);
	}

	
	public boolean play(DeckDistrict deckDistrict, String currentPhase,
			LinkedHashMap<Character, Optional<Behaviour>> hashOfCharacters) {
		printC.dropALine();
		if (player.getCharacter().isCharacterIsAlive()) {
			this.getPlayer().getCharacter().spellOfTurn(this, hashOfCharacters, printC);
			if (currentPhase == PhaseManager.END_GAME_PHASE && player.getCity().getSizeOfCity() < 6)
				endGameBehaviour(deckDistrict);
			if (currentPhase == PhaseManager.LAST_TURN_PHASE)
				lastTurnBehaviour(deckDistrict);
			else
				normalBehaviour(deckDistrict);
		} else {
			printC.botIsDead(player);
		}
		return (player.getCity().isComplete());

	}

	public void normalBehaviour(DeckDistrict deckDistrict) {
	};

	public void endGameBehaviour(DeckDistrict deckDistrict) {
	};

	public void lastTurnBehaviour(DeckDistrict deckDistrict) {
	};

	

	
	/**
	 * 
	 * Je comprend pas l'interêt, si on veut voler la carte de l'assasin pk on retourne pas le character assasin direct ???
	 */
	public Character selectCharacterForSpell(LinkedHashMap<Character, Optional<Behaviour>> hashOfCharacters) {
		int i = randomInt(numberOfCharacter - 1);
		Character character = (Character) hashOfCharacters.keySet().toArray()[i];
		List<Character> list = hashOfCharacters.keySet().stream().toList();
		if (this.player.getCharacter().getName().equals("Thief")) {
			while (list.get(i).getName().equals(this.player.getCharacter().getName())
					|| list.get(i).getName().equals("Assassin") || character.isCharacterIsAlive() == false) {
				i = randomInt(numberOfCharacter - 1);
				character = (Character) hashOfCharacters.keySet().toArray()[i];
			}
		}

		while (hashOfCharacters.keySet().stream().toList().get(i).getName()
				.equals(this.player.getCharacter().getName())) {
			i = randomInt(numberOfCharacter - 1);
			character = (Character) hashOfCharacters.keySet().toArray()[i];
		}

		return (character);
	}
	 
	public void ifPossibleBuildADistrict() {
		ArrayList<District> districtWeCanBuild = cityMan.listOfDistrictBuildable();
		if (!districtWeCanBuild.isEmpty()) {
			Collections.sort(districtWeCanBuild);
			Collections.reverse(districtWeCanBuild);
			District district = districtWeCanBuild.get(0);
			executor.buildDistrict(district);
		}
	}
	
	public Optional<District> pick2CardsIntoTheDeck(DeckDistrict deckDistrict){
		ArrayList<District> pickedCards = executor.pickCards(deckDistrict);
		ArrayList<District> possibleCards = chooseToKeepOrNotPickedCards(pickedCards, deckDistrict);
		Optional<District> choosenDistrictCard = Optional.empty();
		if(possibleCards.size() == ONE_CARD)
			choosenDistrictCard = Optional.of(possibleCards.get(0));
		else if(possibleCards.size() == TWO_CARD)
			choosenDistrictCard = Optional.of(chooseBetweenTwoCards(possibleCards.get(0), possibleCards.get(1), deckDistrict));
		return choosenDistrictCard;
	}
	
	public District chooseBetweenTwoCards(District firstDistrict, District secondDistrict, DeckDistrict deckDistrict) {
		ArrayList<District> pickedCards = new ArrayList<>();
		pickedCards.add(firstDistrict);
		pickedCards.add(secondDistrict);
		return selectTheHigherDistrict(deckDistrict, pickedCards);
	}
 
	/*
	 * For the two cards chosen look if they are present in the city or the hand, if yes we discard the card
	 * */
	public ArrayList<District> chooseToKeepOrNotPickedCards(ArrayList<District> pickedDistrictCards, DeckDistrict deckDistrict) {
		ArrayList<District> removeDistrictCards = new ArrayList<District>();
		for(int i = 0 ; i < 2 ; i++) {
			District currentDistrictCard = pickedDistrictCards.get(i);
			if (cityMan.isAlreadyBuilt(currentDistrictCard.getName()) || player.hasDistrict(currentDistrictCard)) {
				executor.putCardBackInDeck(deckDistrict, currentDistrictCard);
				removeDistrictCards.add(pickedDistrictCards.get(i));
			}
		}
		pickedDistrictCards.removeAll(removeDistrictCards);
		return pickedDistrictCards;
	}
	
	public void takeCard(District districtCard, DeckDistrict deckDistrict) {
		executor.takeCard(districtCard, deckDistrict);
	}


	public void takeGold() {
		executor.takeGold();		 
	}
	
	public void buildDistrict(District district) {
		executor.buildDistrict(district);
	}

	

	public int randomInt(int scope) {
		Random random = new Random();
		return (random.nextInt(scope));
	}

	public void setBehaviourIsKing(Boolean BehaviourIsKing) {
		this.botIsKing = BehaviourIsKing;
	}

	public void setCharacterIsAlive(Boolean characterIsAlive) {
		player.getCharacter().setCharacterIsAlive(characterIsAlive);
	}
	
	public Player getPlayer() {
		return player;
	}

	public Boolean getBehaviourIsKing() {
		return botIsKing;
	}

	public CityManagement getCityManager() {
		return cityMan;
	}
	
	public Executor getExecutor() {
		return executor;
	}

	

}