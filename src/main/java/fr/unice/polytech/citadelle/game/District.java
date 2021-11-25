package fr.unice.polytech.citadelle.game;

/**
 * A District is a card that represent a plan for building a district.
 * @author BONNET Killian, IMAMI Ayoub, KARRAKCHOU Mourad, LE BIHAN Léo
 */
public class District {
    private final String name;
    private final int value;
    private String color;
    private String nameOfFamily;


    public District(String name, int value,String color,String nameOfFamily) {
        this.name = name;
        this.value = value;
        this.color=color;
        this.nameOfFamily=nameOfFamily;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public boolean equals(Object o) {
        if (o instanceof District) {
            return (((District) o).name.equals(name));
        }
        return false;
    }

    public String getColor() {
        return color;
    }

    public String getNameOfFamily() {
        return nameOfFamily;
    }

    @Override
    public String toString(){
        return("District "+name+" ("+value+")");
    }
}
