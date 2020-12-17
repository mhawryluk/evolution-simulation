package project;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class Statistics {

    private int animalsCount = 0;
    private int grassCount = 0;
    private int animalsDead = 0;
    private double averageLifespan = 0;
    private double averageOffspringCount = 0;
    private final HashMap<String, Integer> genomeCount = new HashMap<>();

    public int countAnimals(){
        return animalsCount;
    }

    public void animalBorn(String genome){
        averageOffspringCount = ((averageOffspringCount*animalsCount)+2)/(animalsCount+1);
        animalsCount++;
        if (!genomeCount.containsKey(genome))
            genomeCount.put(genome, 1);
        else
            genomeCount.replace(genome, genomeCount.get(genome)+1);
    }

    public void animalDead(Animal animal){
        averageLifespan = (averageLifespan*animalsDead + animal.getLifespan())/(animalsDead+1);
        averageOffspringCount = (((averageOffspringCount)*animalsCount) - animal.getOffspringCount())/(animalsCount-1);
        animalsCount--;
        animalsDead++;
        String genome = animal.getGenomeString();
        if (!genomeCount.containsKey(genome)){
            System.out.println(genome);
            System.out.println(animal.getPosition());
        }
        genomeCount.replace(genome, genomeCount.get(genome)-1);
    }

    public String getDominantGenome(){
        if (genomeCount.size() == 0) return " ";
        return Collections.max(genomeCount.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();
    }

    public int countGrass(){
        return grassCount;
    }

    public void newGrass(){
        grassCount++;
    }

    public void grassEaten(){
        grassCount--;
    }

    public double getAverageLifespan(){
        return averageLifespan;
    }

    public double getAverageOffspringCount(){
        return averageOffspringCount;
    }
}
