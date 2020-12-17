package project;
import javax.jms.IllegalStateException;
import java.util.*;

public class Simulation {

    private final LinkedHashSet<Animal> animalsOnMap = new LinkedHashSet<>();
    private HashSet<Vector2d> takenPositions;
    public final EvolutionMap map;
    private final int nutritionalEnergy;
    public final int minimumEnergy;
    public final Statistics stats;

    private int simulationDay = 1;
    private Animal observedAnimal;
    private boolean isAnimalObserved = false;
    private int observedChildrenCount;
    private int observedOffspringCount;
    private int observedAnimalDeathDay;
    private int observedAnimalSetDay;
    private HashSet<Animal> observedOffsprings;


    public Simulation(int width, int height, int nutritionalEnergy, int initialPopulation, int initialEnergy, int jungleRatio){

        this.nutritionalEnergy = nutritionalEnergy;
        minimumEnergy = initialEnergy/2;

        map = new EvolutionMap(width, height, jungleRatio);
        stats = new Statistics();

        //gen 0
        for (int i = 0; i < initialPopulation; i++) {
            Animal newAnimal = new Animal(map, initialEnergy);
            stats.animalBorn(newAnimal.getGenomeString());
            animalsOnMap.add(newAnimal);
            newAnimal.addStatisticsObserver(stats);
        }
    }

    public void run(){
        simulationDay++;
        decreaseEnergy();
        removeDead();
        moveAnimals();
        eatGrass();
        reproduction();
        map.updateJungle(takenPositions);
        addNewGrass(nutritionalEnergy);
        turnAnimals();
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
                stats.animalDead(animal);
                if (observedAnimal == animal){
                    observedAnimalDeathDay = simulationDay;
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

    private void eatGrass(){ //poprawić
        for (Vector2d position: takenPositions){
            Grass grass = map.grassAt(position);
            if (grass == null) continue;

            TreeSet<Animal> animalsOnPosition = map.animalsAt(position);
            if (animalsOnPosition.isEmpty()) continue;

            Iterator <Animal> iterator = animalsOnPosition.iterator();
            Animal maxEnergyAnimal = iterator.next();

            int maxEnergyCount = 1;

            while (iterator.hasNext() && iterator.next().getEnergy() == maxEnergyAnimal.getEnergy()){
                maxEnergyCount++;
            }

            iterator = animalsOnPosition.iterator();

            while (iterator.hasNext()) {
                Animal currentAnimal = iterator.next();
                if (currentAnimal.getEnergy() == maxEnergyAnimal.getEnergy()) {
                    currentAnimal.increaseEnergy(grass.energy / maxEnergyCount);
                }
            }
            map.removeGrass(grass);
            stats.grassEaten();
        }
    }

    private void reproduction(){
        HashSet <Vector2d> offspringPositions = new HashSet<>();

        for (Vector2d position : takenPositions) {
            TreeSet<Animal> animalsOnPosition = map.animalsAt(position);

            if (animalsOnPosition.size() < 2) continue;

            Iterator<Animal> iterator = animalsOnPosition.iterator();
            Animal parent1 = iterator.next();
            Animal parent2 = iterator.next();
            if (parent1.canBreed(minimumEnergy) && parent2.canBreed(minimumEnergy)) {
                Animal offspring = new Animal(map, parent1, parent2);
                offspring.addStatisticsObserver(stats);
                animalsOnMap.add(offspring);
                stats.animalBorn(offspring.getGenomeString());
                offspringPositions.add(offspring.getPosition());

                if (isAnimalObserved){
                    if (parent1 == observedAnimal || parent2 == observedAnimal){
                        observedOffsprings.add(offspring);
                        observedOffspringCount++;
                        observedChildrenCount++;
                    }
                    else if (observedOffsprings.contains(parent1) || observedOffsprings.contains(parent2)){
                        observedOffsprings.add(offspring);
                        observedOffspringCount++;
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
            stats.newGrass();
        }

        map.updateJungle(takenPositions);
        newGrassPosition = map.getFreeJunglePosition();

        if (newGrassPosition != null) {
            map.place(new Grass(newGrassPosition, nutritionalEnergy));
            stats.newGrass();
        }
    }

    public void setObservedAnimal (Animal animal, int days)  throws IllegalStateException {
        if (isAnimalObserved)
            throw new IllegalStateException("an animal is already being observed");

        isAnimalObserved = true;
        observedAnimal = animal;
        observedOffspringCount = 0;
        observedChildrenCount = 0;
        observedAnimalDeathDay = -1;
        observedAnimalSetDay = simulationDay;
        observedOffsprings = new HashSet<Animal>();
    }

    public String getObservedAnimalInfo(){
        String info = "children count: " + observedChildrenCount +
                "\ntotal offspring count: " + observedOffspringCount;

        if (observedAnimalDeathDay != -1){
            int deathDay = observedAnimalDeathDay - observedAnimalSetDay;
            info += "\nday of death: " + deathDay;
        }
        return info;
    }

    public void unsetObservedAnimal() {
        isAnimalObserved = false;
    }
}
