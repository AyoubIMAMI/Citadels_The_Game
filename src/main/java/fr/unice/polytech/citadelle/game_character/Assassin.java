package fr.unice.polytech.citadelle.game_character;

import fr.unice.polytech.citadelle.game_interactor.game_behaviour.Behaviour;
import fr.unice.polytech.citadelle.output.PrintCitadels;

import java.util.LinkedHashMap;
import java.util.Optional;

/**
 * Assassin can kill one character per round. The player having the character being killed will be not
 * able to play the round.
 *
 * @author BONNET Killian, IMAMI Ayoub, KARRAKCHOU Mourad, LE BIHAN Léo
 */
public class Assassin extends Character{
    public Assassin(){
        super("Assassin", 1);
    }

    /**
     * Execute the spell
     * @param bot
     * @param hashOfCharacters
     */
    @Override
    public void spellOfTurn(Behaviour bot, LinkedHashMap<Character, Optional<Behaviour>> hashOfCharacters){
        Character characterToDie= bot.selectCharacterForSpell(hashOfCharacters);
        if (hashOfCharacters.get(characterToDie).isPresent())
            hashOfCharacters.get(characterToDie).get().setCharacterIsAlive(false);
        PrintCitadels.killCharacter(characterToDie);
    }
}
