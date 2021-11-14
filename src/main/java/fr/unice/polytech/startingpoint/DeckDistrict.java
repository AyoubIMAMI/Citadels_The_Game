package fr.unice.polytech.startingpoint;

import java.util.ArrayList;
import java.util.Random;

public class DeckDistrict {
    ArrayList<District> deckDistrict;
    int size;
    public DeckDistrict(){
        size=12;
        deckDistrict=new ArrayList<>();
        this.initialise();
    }
    private void initialise(){
        for (int k=0;k<6;k++){
            deckDistrict.add(new District("house",1));
        }
        for (int k=0;k<6;k++){
            deckDistrict.add(new District("house",2));
        }
    }
    public ArrayList<District> getDeckCharacter() {
        return deckDistrict;
    }
    public int getSize() {
        return size;
    }
    void chooseDistrict(Player player){
        Random random=new Random();
        player.addDistrict(deckDistrict.remove(random.nextInt(deckDistrict.size())));

    }
}