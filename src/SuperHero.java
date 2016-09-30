import java.util.ArrayList;

/**
 * Created by Zach on 9/26/16.
 */
public class SuperHero {
    int index;
    String secretIdentity;
    String heroName;
    int age;
    ArrayList<String> knownHeroPowers = new ArrayList<>();
    String primaryCostumeColor;
    String secondaryCostumeColor;
    String userName;
    String owner;

    public SuperHero(){}

    public SuperHero(int index, String ownerName, String secretIdentity, String heroName, int age, ArrayList<String> knownHeroPowers,
                     String primaryCostumeColor, String secondaryCostumeColor) {
        this.index = index;
        this.userName = ownerName;
        this.secretIdentity = secretIdentity;
        this.heroName = heroName;
        this.age = age;
        this.knownHeroPowers = knownHeroPowers;
        this.primaryCostumeColor = primaryCostumeColor;
        this.secondaryCostumeColor = secondaryCostumeColor;
    }

    @Override
    public String toString() {
        return String.format("%s, %s, "+age+", "+knownHeroPowers+", %s, %s", secretIdentity,heroName,
                primaryCostumeColor,secondaryCostumeColor);
    }
}
