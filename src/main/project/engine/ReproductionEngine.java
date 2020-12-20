package project.engine;

import java.util.*;

public class ReproductionEngine{

    private final int requiredEnergy;
    private final EvolutionMap map;
    private AnimalObservation animalObservation;

    public ReproductionEngine(EvolutionMap map, int initialEnergy){
        requiredEnergy = initialEnergy/2;
        this.map = map;
    }

    public LinkedList<Animal> reproduction(HashSet<Vector2d> takenPositions){
        LinkedList<Animal> offspringsBorn = new LinkedList<>();
        ArrayList<Animal> newParents1 = new ArrayList<>();
        ArrayList<Animal> newParents2 = new ArrayList<>();

        for (Vector2d position: takenPositions) {
            ArrayList<Animal> animalsAtPosition = map.animalsAtSortedByEnergy(position);

            if (animalsAtPosition.size() < 2) continue;

            Animal parent1 = animalsAtPosition.get(0);
            Animal parent2 = animalsAtPosition.get(1);

            if (canReproduce(parent2)) {
                newParents1.add(parent1);
                newParents2.add(parent2);
            }
        }

        for (int i = 0; i < newParents1.size(); i++){
            offspringsBorn.add(newOffspringBorn(newParents1.get(i), newParents2.get(i)));
        }

        return offspringsBorn;
    }

    private Animal newOffspringBorn(Animal parent1, Animal parent2){
        Genome genes = new Genome(parent1, parent2);
        Vector2d position = map.getOffspringPosition(parent1.getPosition());

        int energy = parent1.getEnergy()/4 + parent2.getEnergy()/4;
        parent1.energyLost();
        parent2.energyLost();
        parent1.newChild();
        parent2.newChild();

        Animal offspring = new Animal(map, position, energy, genes);

        if (animalObservation != null){
            if (animalObservation.isParentObserved(parent1, parent2))
                animalObservation.newChild(offspring);
            else if (animalObservation.isParentObservedOffspring(parent1, parent2))
                animalObservation.newOffspring(offspring);
        }

        return offspring;
    }

    public void setAnimalObservation(AnimalObservation animalObservation){
        this.animalObservation = animalObservation;
    }

    public void unsetAnimalObservation(){
        animalObservation = null;
    }

    private boolean canReproduce(Animal animal){
        return animal.getEnergy() >= requiredEnergy;
    }
}
