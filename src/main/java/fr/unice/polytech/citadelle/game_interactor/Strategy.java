package fr.unice.polytech.citadelle.game_interactor;

import fr.unice.polytech.citadelle.game.Board;
import fr.unice.polytech.citadelle.game.Character;
import fr.unice.polytech.citadelle.game.District;
import fr.unice.polytech.citadelle.game.Player;
import fr.unice.polytech.citadelle.game.purple_districts.HauntedCity;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Math.abs;

public class Strategy {
    Player player;
    Board board;
    Predict predict;
    int numberOfCharacter;
    public Strategy(int numberOfCharacter,Board board,Player player){
        predict=new Predict();
        this.numberOfCharacter=numberOfCharacter;
        this.board=board;
        this.player=player;
    }

    public int randomInt(int scope) {
        Random random = new Random();
        return (random.nextInt(scope));
    }


    public Character chooseCharacterForThiefRandom(LinkedHashMap<Character, Optional<Behaviour>> hashOfCharacters) {
        List<Character> list = hashOfCharacters.keySet().stream().toList();
        Character randomCharacter=list.get(randomInt(8));
        while (randomCharacter.getName().equals("Assassin") || randomCharacter.getName().equals("Thief") || randomCharacter.isCharacterIsAlive() == false) {
            randomCharacter=list.get(randomInt(numberOfCharacter));
        }
        return randomCharacter;
    }


    public Character chooseCharacterForAssassinRandom(LinkedHashMap<Character, Optional<Behaviour>> hashOfCharacters) {
        List<Character> list = hashOfCharacters.keySet().stream().toList();
        Character randomCharacter=list.get(randomInt(8));
        while (randomCharacter.getName().equals("Assassin")) {
            randomCharacter=list.get(randomInt(numberOfCharacter));
        }
        return(randomCharacter);
    }

    public Character chooseCharacterForMagicianRandom(LinkedHashMap<Character, Optional<Behaviour>> hashOfCharacters){
        List<Character> list = hashOfCharacters.keySet().stream().toList();
        Character randomCharacter=list.get(randomInt(numberOfCharacter));
        while (randomCharacter.getName()=="Magician")
            randomCharacter=list.get(randomInt(numberOfCharacter));
        return randomCharacter;
    }

    Character chooseCharacterForAssassinAdvanced(LinkedHashMap<Character, Optional<Behaviour>> hashOfCharacters){
        int predictedScore=playerPredictScore(player);
        ArrayList<Player> listOfPlayer=board.getListOfPlayer();
        ArrayList<String> listOfCharacterToNotKill=new ArrayList<>();
        listOfCharacterToNotKill.add("Assassin");
        //We want to find the player with the PredictedScore the closest to the score of our Player
        int scoreDiffenreceWithClosestScore=abs(playerPredictScore(listOfPlayer.get(0))-predictedScore);
        Player playerWithClosestScore=listOfPlayer.get(0);

        for (int k=0;k<listOfPlayer.size();k++){
            int scoreDifference=abs(playerPredictScore(listOfPlayer.get(k))-predictedScore);
            if (scoreDiffenreceWithClosestScore>scoreDifference)
            {
                playerWithClosestScore=listOfPlayer.get(k);
                scoreDiffenreceWithClosestScore=scoreDifference;
            }
        }
        return (predict.predictWhoIsPlayer(playerWithClosestScore,listOfCharacterToNotKill));
    }

    /**
     * @param player The player to process.
     * @return The score formed by summing built district score.
     */
    public int cityDistrictScore(Player player){
        int sum = 0;
        ArrayList<District> playerCity = player.getCity().getBuiltDistrict();

        for (District district : playerCity)
            sum += district.getValue();

        return sum;
    }

    public int playerPredictScore(Player player) {
        // City Score processing.
        int scoreToPredict = cityDistrictScore(player);

        // Add 3 bonus points if the player has 5 built cites of different colors.
        if (hasFiveDistrictColors(player)) scoreToPredict += 3;

        // Predict an approximation of the score of the Player.
        return(scoreToPredict);
    }

    /**
     * @param player The player to process.
     * @return True/False, the player has 5 district colors in its city.
     */
    public boolean hasFiveDistrictColors(Player player){
        ArrayList<District> playerCity = player.getCity().getBuiltDistrict();
        boolean hasBlue = false;
        boolean hasRed = false;
        boolean hasGreen = false;
        boolean hasYellow = false;
        boolean hasPurple = false;

        for (District district : playerCity){
            switch (district.getColor()) {
                case "Blue" -> hasBlue = true;
                case "Red" -> hasRed = true;
                case "Green" -> hasGreen = true;
                case "Yellow" -> hasYellow = true;
                case "Purple" -> hasPurple = true;
            }
        }

        return (hasBlue && hasRed && hasGreen && hasYellow && hasPurple);
    }


}