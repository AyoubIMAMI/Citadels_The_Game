package fr.unice.polytech.citadelle.bot;

import java.util.ArrayList;
import java.util.Random;

import fr.unice.polytech.citadelle.game.DeckDistrict;
import fr.unice.polytech.citadelle.game.District;
import fr.unice.polytech.citadelle.game.Game;
import fr.unice.polytech.citadelle.game.Player;
import fr.unice.polytech.citadelle.game_engine.PhaseManager;
import fr.unice.polytech.citadelle.output.PrintCitadels;
import fr.unice.polytech.citadelle.game.Character;

/**
 * A Bot realize all the action of a player.
 * @author BONNET Killian, IMAMI Ayoub, KARRAKCHOU Mourad, LE BIHAN Léo
 */
public class Bot {
	// The player controlled by the bot.
	private final Player player;
	private final PrintCitadels printC = new PrintCitadels();
	private Boolean botIsAlive=true;
	int numberOfCharacter=4;

	public Bot(Player player) {
		this.player = player;
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

	/**
	 * @param deckDistrict    The global game deck of districts
	 * @param pickedDistricts The two picked cards.
	 * @return Whether a card has been picked and the other burned or not.
	 *
	 *         Criteria : 1 - The card must not already be in the player's city or
	 *         in the player's hand 2 - If there are still 2 cards after applying
	 *         the criteria (1), the card with the higher value is selected.
	 */
	public boolean districtCardIsSelected(DeckDistrict deckDistrict, ArrayList<District> pickedDistricts) {

		// Player will not pick cards it already have on its city or its hand.
		for (int i = 0; i < pickedDistricts.size(); i++) {
			District currentDistrictCard = pickedDistricts.get(i);
			if (player.cityHasDistrict(currentDistrictCard) || player.hasDistrict(currentDistrictCard)) {
				putCardBackInDeck(pickedDistricts, currentDistrictCard);
				;
			}
		}

		switch (pickedDistricts.size()) {
		// One district card have been selected, player will take this card.
		case 1 -> {
			printC.printTakeDistrictCard(player);// pas d'accord, le controller demande au printer de print les infos du
													// bot
			player.addDistrict(pickedDistricts.get(0));
			return true;
		}

		// Two district cards have been selected, player will take the higher one.
		case 2 -> {
			printC.printTakeDistrictCard(player);
			District higherDistrict = selectTheHigherDistrict(deckDistrict, pickedDistricts);
			player.addDistrict(higherDistrict);
			return true;
		}

		default -> {
			return false;
		}
		}
	}



	public boolean play(DeckDistrict deckDistrict, String currentPhase, Game game) {
		printC.dropALine();
		if (botIsAlive){
			this.getPlayer().getCharacter().spellOfTurn(this,game,printC);
			if (currentPhase == PhaseManager.END_GAME_PHASE && player.getCity().getSizeOfCity() < 6)
				endGameBehaviour(deckDistrict);
			if (currentPhase == PhaseManager.LAST_TURN_PHASE)
				lastTurnBehaviour(deckDistrict);
			else
				normalBehaviour(deckDistrict);

			ifPossibleBuildADistrict();
			return (player.getCity().isComplete());}
		else{
			printC.botIsDead(player);
			botIsAlive=true;
			return(false);
		}
	}

	private void normalBehaviour(DeckDistrict deckDistrict) {
		if (player.getDistrictCardsSize() == 0 || player.districtWeCanBuild(player.getDistrictCards()).size() == 0)
			takeCard(deckDistrict);
		else {
			printC.printTakeGold(player);
			player.addGold();
		}
	}

	private void endGameBehaviour(DeckDistrict deckDistrict) {
		System.out.println("***"+player.getName() + " is in EndGame mode***");
		takeCard(deckDistrict);
	}

	private void lastTurnBehaviour(DeckDistrict deckDistrict) {
		System.out.println("***"+player.getName() + " is in LAST TURN mode***");
		takeCard(deckDistrict);
	}

	public void ifPossibleBuildADistrict() {
		ArrayList<District> districtWeCanBuild = player.listOfDistrictBuildable();
		if (!districtWeCanBuild.isEmpty())
			{player.buildDistrict(districtWeCanBuild.get(0));
			printC.printBuildDistrict(player,districtWeCanBuild.get(0));
			printC.printBoardOfPlayer(player);}
	}


	private void takeCard(DeckDistrict deckDistrict) {
		// Pick two district cards and add it to an ArrayList.
		ArrayList<District> pickedDistricts = new ArrayList<District>();
		pickedDistricts.add(deckDistrict.chooseDistrict());
		pickedDistricts.add(deckDistrict.chooseDistrict());

		// Allow player to take or not one of the 2 picked district cards
		boolean playerTakeADistrictCard = districtCardIsSelected(deckDistrict, pickedDistricts);

		// If player don't take any district cards
		if (!playerTakeADistrictCard) {
			printC.printTakeGold(player);
			player.addGold();
		}
	}

	private void putCardBackInDeck(ArrayList<District> pickedDistricts, District districtCard) {
		player.addDistrict(districtCard);
		pickedDistricts.remove(districtCard);
	}

	public Player getPlayer() {
		return player;
	}

	public Character selectCharacterForAssassin(ArrayList<Character> listOfCharactersInGame){
		Random random=new Random();
		Character character=listOfCharactersInGame.get(random.nextInt(numberOfCharacter-1));
		while (character.getName().equals("Assassin"))
		character=listOfCharactersInGame.get(random.nextInt(numberOfCharacter-1));
		return (character);
	}

	public void setBotIsAlive(Boolean botIsAlive) {
		this.botIsAlive = botIsAlive;
	}


}
