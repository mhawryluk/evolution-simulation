package project.engine;

import java.util.*;

public class Simulation {

    public final EvolutionMap map;
    public final Statistics statistics;
    private LongTermStatistics longTermStatistics;
    private AnimalObservation animalObservation;
    private final ReproductionEngine reproductionEngine;
    private final GrassConsumptionEngine grassConsumptionEngine;

    private final LinkedHashSet<Animal> animalsOnMap = new LinkedHashSet<>();
    private HashSet<Vector2d> takenPositions;
    private Animal animalObserved = null;

    public final int minimumEnergy;
    private int simulationDay = 0;

    public Simulation(MapDimensions dimensions, int nutritionalEnergy, int initialPopulation, int initialEnergy){
        map = new EvolutionMap(dimensions);
        statistics = new Statistics();
        minimumEnergy = initialEnergy/2;
        reproductionEngine = new ReproductionEngine(map, initialEnergy);
        grassConsumptionEngine = new GrassConsumptionEngine(map, nutritionalEnergy);

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
        move();
        eat();
        reproduce();
        statistics.updateAverageEnergy(animalsOnMap);
        addNewGrass();
        turnAnimals();

        if (longTermStatistics != null){
            longTermStatistics.update();
        }
    }

    private void decreaseEnergy(){
        for (Animal animal: animalsOnMap)
            animal.decreaseEnergy();

    }

    private void turnAnimals(){
        for (Animal animal: animalsOnMap){
            int turnValue = animal.getRandomTurnValue();
            animal.turn(turnValue);
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

    private void move(){
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

    private void eat(){
        int grassEatenCount = grassConsumptionEngine.consumption(takenPositions);
        statistics.grassEaten(grassEatenCount);
    }

    private void reproduce(){
        LinkedList<Animal> newOffsprings = reproductionEngine.reproduction(takenPositions);
        for (Animal offspring: newOffsprings){
            takenPositions.add(offspring.getPosition());
            statistics.animalBorn(offspring);
            animalsOnMap.add(offspring);
        }
    }

    private void addNewGrass(){
        Vector2d newGrassPosition = map.getFreePosition();

        if (newGrassPosition != null) {
            new Grass(newGrassPosition, map);
            statistics.newGrass();
        }

        map.updateJungle();
        newGrassPosition = map.getFreeJunglePosition();

        if (newGrassPosition != null) {
            new Grass(newGrassPosition, map);
            statistics.newGrass();
        }
    }

    public void setObservedAnimal (Animal animal)  throws IllegalStateException {
        if (animalObserved != null)
            throw new IllegalStateException("an animal is already being observed");

        animalObservation = new AnimalObservation(animal, simulationDay);
        animalObserved = animal;
        reproductionEngine.setAnimalObservation(animalObservation);
    }

    public String getObservedAnimalInfo(){
        return animalObservation.getObservedAnimalInfo();
    }

    public void unsetObservedAnimal() {
        animalObserved = null;
        animalObservation = null;
        reproductionEngine.unsetAnimalObservation();
    }

    public void setLongTermStatistics(LongTermStatistics longTermStatistics){
        this.longTermStatistics = longTermStatistics;
        longTermStatistics.setSimulation(this);
    }

    public void unsetLongTermStatistics(){
        longTermStatistics = null;
    }
}
