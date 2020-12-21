package project.engine;

import java.util.*;

public class Statistics {

    private int animalsCount;
    private int grassCount;
    private int animalsDead;
    private double averageLifespan;
    private double averageChildrenCount;
    private double averageEnergy;
    private final HashMap<String, Integer> genomeCount = new HashMap<>();

    public void firstGenAnimalPlaced(Animal animal) {
        animalsCount++;
        includeGenome(animal.getGenomeString());
    }

    public void animalBorn(Animal animal) {
        averageChildrenCount = ((averageChildrenCount * animalsCount) + 2) / (animalsCount + 1);
        animalsCount++;
        includeGenome(animal.getGenomeString());
    }

    private void includeGenome(String genome) {
        if (!genomeCount.containsKey(genome))
            genomeCount.put(genome, 1);
        else
            genomeCount.replace(genome, genomeCount.get(genome) + 1);
    }

    public void animalDead(Animal animal) {
        averageLifespan = (averageLifespan * animalsDead + animal.getLifespan()) / (animalsDead + 1);
        averageChildrenCount = (((averageChildrenCount) * animalsCount) - animal.getChildrenCount()) / (animalsCount - 1);
        animalsCount--;
        animalsDead++;
        String genome = animal.getGenomeString();
        genomeCount.replace(genome, genomeCount.get(genome) - 1);
    }

    public String getDominantGenome() {
        if (genomeCount.size() == 0) return " ";
        return Collections.max(genomeCount.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();
    }

    public void newGrass() {
        grassCount++;
    }

    public void grassEaten(int grassEatenCount) {
        grassCount -= grassEatenCount;
    }

    public int countGrass() {
        return grassCount;
    }

    public int countAnimals() {
        return animalsCount;
    }

    public double getAverageLifespan() {
        return averageLifespan;
    }

    public double getAverageChildrenCount() {
        return averageChildrenCount;
    }

    public double getAverageEnergy() {
        return averageEnergy;
    }

    public void updateAverageEnergy(LinkedHashSet<Animal> animalsOnMap) {
        averageEnergy = animalsOnMap
                .stream()
                .mapToInt(Animal::getEnergy)
                .average()
                .orElse(0.0);
    }
}
