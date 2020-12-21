package project.engine;

import java.util.HashSet;

public class AnimalObservation {

    private int childrenCount;
    private int deathDay = -1;
    private final int observationStartDay;
    private final HashSet<Animal> observedOffsprings = new HashSet<>();
    private final Animal observedAnimal;

    public AnimalObservation(Animal animal, int simulationDay) {
        observedAnimal = animal;
        observationStartDay = simulationDay;
    }

    public void died(int simulationDay) {
        deathDay = simulationDay - observationStartDay;
    }

    public void newChild(Animal child) {
        observedOffsprings.add(child);
        childrenCount++;
    }

    public void newOffspring(Animal offspring) {
        observedOffsprings.add(offspring);
    }

    public boolean isParentObserved(Animal parent1, Animal parent2) {
        return parent1 == observedAnimal || parent2 == observedAnimal;
    }

    public boolean isParentObservedOffspring(Animal parent1, Animal parent2) {
        return observedOffsprings.contains(parent1) || observedOffsprings.contains(parent2);
    }

    public String getObservedAnimalInfo() {
        String info = "children count: " + childrenCount +
                "\ntotal offspring count: " + observedOffsprings.size();

        if (deathDay != -1) {
            info += "\nday of death: " + deathDay;
        }
        return info;
    }
}
