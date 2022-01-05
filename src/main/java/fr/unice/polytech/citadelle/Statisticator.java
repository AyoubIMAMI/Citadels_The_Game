package fr.unice.polytech.citadelle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import fr.unice.polytech.citadelle.game.Player;
import fr.unice.polytech.citadelle.game_engine.Controller;
import fr.unice.polytech.citadelle.output.PrintCitadels;

public class Statisticator {
	private final ArrayList<StatsBot> listOfBotStats;
	
	public Statisticator() {
		listOfBotStats = new ArrayList<>();
	}

	/**
	 * Run a Thousand game.
	 * Save the stats of the 1000 game on the listOfBotStats.
	 */
	public void runAThousandGames() throws Exception {
		// Only read the logs with a Warning level
		PrintCitadels.activateLevelWarning();

		for(int i = 0; i < 1000; i++){
			// Run one game
			Controller controller = new Controller();
			controller.initGame();
			ArrayList<Player> leaderboard = controller.runGame();

			// Update all bots stats with the game leaderboards
			updateBotStats(leaderboard);

			//Writing in the CSV file
			CsvManager csv = new CsvManager(leaderboard);
			csv.saveFile();
		}
		PrintCitadels.activateLevelInfo();
		PrintCitadels.start1000games();

		Collections.sort(listOfBotStats);
		for (StatsBot statsBot : listOfBotStats)
			PrintCitadels.printStat(statsBot);
	}

	/**
	 * For a given bot name, return - if exist - the associated StatsBot object.
	 * @param name The name of the bot
	 * @return If exists, the associated statistic object.
	 */
	public Optional<StatsBot> getBotStats(String name){
		for (StatsBot statsBot : listOfBotStats)
			if (statsBot.getName().equals(name))
				return Optional.of(statsBot);
		return Optional.empty();
	}

	/**
	 * Update the list of BotStats according to the leaderboard results.
	 * @param leaderboard The leaderboard of a game.
	 */
	public void updateBotStats(ArrayList<Player> leaderboard){
		for(int i=0; i<leaderboard.size(); i++){
			String currentBotName = leaderboard.get(i).getName();
			StatsBot currentBotStats;

			// Checking if the bot is already in the listOfBotStats
			if (getBotStats(currentBotName).isPresent())
				currentBotStats = getBotStats(currentBotName).get();
			else {
				currentBotStats = new StatsBot(currentBotName);
				listOfBotStats.add(currentBotStats);
			}

			currentBotStats.increaseNumberOfGamePlay();
			if (i == 0)
				currentBotStats.increaseNumberOfGameWin();
		}
	}
}
