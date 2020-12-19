package project.engine;

import javax.jms.IllegalStateException;
import java.util.*;

public class Simulation {

    public final EvolutionMap map;
    public final Statistics statistics;
    private LongTermStatistics longTermStatistics = null;
    private AnimalObservation animalObservation = null;

    private final LinkedHashSet<Animal> animalsOnMap = new LinkedHashSet<>();
    private HashSet<Vector2d> takenPositions;
    private Animal animalObserved = null;

    private final int nutritionalEnergy;
    public final int minimumEnergy;
    private int simulationDay = 0;

    public Simulation(MapDimensions dimensions, int nutritionalEnergy, int initialPopulation, int initialEnergy){
        map = new EvolutionMap(dimensions);
        statistics = new Statistics();

        this.nutritionalEnergy = nutritionalEnergy;
        minimumEnergy = initialEnergy/2;

        //gen 0
        for (int i = 0; i < initialPopulation; i++) {

            Vector2d position = map.getFreeJunglePosition();
            if (position == null) position = map.getFreePosition();

            Animal newAnimal = new Animal(map, position, initialEnergy);
            statistics.firstGenAnimalPlaced(newAnimal);
            animalsOnMap.add(newAnimal);
        }
    }

    public void run(){
        simulationDay++;
        decreaseEnergy();
        removeDead();
        moveAnimals();
        eatGrass();
        reproduction();

        map.updateJungle();
        statistics.updateAverageEnergy(animalsOnMap);

        addNewGrass(nutritionalEnergy);
        turnAnimals();

        if (longTermStatistics != null){
            longTermStatistics.update();
        }
    }

    private void decreaseEnergy(){
        for (Animal animal: animalsOnMap){
            animal.decreaseEnergy();
        }
    }

    private void turnAnimals(){
        for (Animal animal: animalsOnMap){
            animal.turn();
        }
    }

    private void removeDead(){
        Iterator <Animal> iterator = animalsOnMap.iterator();
        while (iterator.hasNext()){
            Animal animal = iterator.next();
            if (animal.isDead()){
                map.removeAnimal(animal);
                statistics.animalDead(animal);
                if (animal == animalObserved){
                    animalObservation.died(simulationDay);
                }
                iterator.remove();
            }
        }
    }

    private void moveAnimals(){
        for (Animal animal: animalsOnMap){
            animal.move();
        }
        updateTakenPositions();
    }

    private void updateTakenPositions(){
        takenPositions = new HashSet<>();
        for (Animal animal: animalsOnMap){
            takenPositions.add(animal.getPosition());
        }
    }

    private void eatGrass(){
        for (Vector2d position: takenPositions){
            Grass grass = map.grassAt(position);
            if (grass == null) continue;

            TreeSet<Animal> animalsOnPosition = map.animalsAt(position);
            if (animalsOnPosition.isEmpty()) continue;

            Iterator <Animal> iterator = animalsOnPosition.descendingIterator();
            Animal maxEnergyAnimal = iterator.next();

            int maxEnergyAnimalCount = 1;

            while (iterator.hasNext() && iterator.next().getEnergy() == maxEnergyAnimal.getEnergy()){
                maxEnergyAnimalCount++;
            }

            iterator = animalsOnPosition.descendingIterator();

            while (iterator.hasNext()) {
                Animal currentAnimal = iterator.next();
                if (currentAnimal.getEnergy() == maxEnergyAnimal.getEnergy()) {
                    currentAnimal.increaseEnergy(grass.energy / maxEnergyAnimalCount);
                } else {
                    break;
                }
            }
            map.removeGrass(grass);
            statistics.grassEaten();
        }
    }

    private void reproduction(){
        HashSet <Vector2d> offspringPositions = new HashSet<>();

        for (Vector2d position: takenPositions) {
            TreeSet<Animal> animalsOnPosition = map.animalsAt(position);

            if (animalsOnPosition.size() < 2) continue;

            Iterator<Animal> iterator = animalsOnPosition.descendingIterator();
            Animal parent1 = iterator.next();
            Animal parent2 = iterator.next();

            if (parent2.canBreed(minimumEnergy)) {
                Animal offspring = new Animal(map, parent1, parent2);

                animalsOnMap.add(offspring);
                offspringPositions.add(offspring.getPosition());

                statistics.animalBorn(offspring);

                if (animalObserved != null){
                    if (animalObservation.isParentObserved(parent1, parent2)){
                        animalObservation.newChild(offspring);
                    } else if (animalObservation.isParentObservedOffspring(parent1, parent2)){
                        animalObservation.newOffspring(offspring);
                    }
                }
            }
        }
        takenPositions.addAll(offspringPositions);
    }

    private void addNewGrass(int nutritionalEnergy){
        Vector2d newGrassPosition = map.getFreePosition();

        if (newGrassPosition != null) {
            map.place(new Grass(newGrassPosition, nutritionalEnergy));
            statistics.newGrass();
        }

        map.updateJungle();
        newGrassPosition = map.getFreeJunglePosition();

        if (newGrassPosition != null) {
            map.place(new Grass(newGrassPosition, nutritionalEnergy));
            statistics.newGrass();
        }
    }

    public void setObservedAnimal (Animal animal)  throws IllegalStateException {
        if (animalObserved != null)
            throw new IllegalStateException("an animal is already being observed");

        animalObservation = new AnimalObservation(animal, simulationDay);
        animalObserved = animal;
    }

    public String getObservedAnimalInfo(){
        return animalObservation.getObservedAnimalInfo();
    }

    public void unsetObservedAnimal() {
        animalObserved = null;
        animalObservation = null;
    }

    public void setLongTermStatistics(LongTermStatistics longTermStatistics){
        this.longTermStatistics = longTermStatistics;
        longTermStatistics.setSimulation(this);
    }

    public void unsetLongTermStatistics(){
        longTermStatistics = null;
    }
}
