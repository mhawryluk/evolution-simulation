package project;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Animal extends MapElement {
    private MapDirection orientation;
    private final EvolutionMap map;
    private final Genome genes;
    private Statistics stats;
    private int lifespan = 0;
    private final ArrayList <Animal> offsprings = new ArrayList();

    public Animal(EvolutionMap map, int energy){ //dla początkowych osobników
        this.map = map;
        this.energy = energy;

        position = map.getFreeJunglePosition();
        if (position == null) position = map.getFreePosition();

        orientation = MapDirection.NORTH.turn(ThreadLocalRandom.current().nextInt(0, 8));
        genes = new Genome();

        map.place(this);
    }

    public Animal(EvolutionMap map, Animal parent1, Animal parent2){ //dla potomków
        this.map = map;
        genes = new Genome(parent1, parent2);
        position = map.getOffspringPosition(parent1.getPosition());
        orientation = MapDirection.NORTH.turn(ThreadLocalRandom.current().nextInt(0, 8));
        energy = parent1.energy/4 + parent2.energy/4;

        parent1.energy *= 0.75;
        parent2.energy *= 0.75;

        parent1.addOffspring(this);
        parent2.addOffspring(this);
        map.place(this);
    }

    public void addStatisticsObserver(Statistics statistics){
        stats = statistics;
    }

    public String toString() {
        return orientation.toString();
    }

    public void move(){
        Vector2d newPosition;
        newPosition = position.add(orientation.toVector());
        newPosition = map.wrapPosition(newPosition);
        Vector2d oldPosition = position;
        position = newPosition;
        positionChanged(oldPosition);
        lifespan++;
    }

    public void turn(){
        int turnValue = genes.getRandomGene();
        orientation = orientation.turn(turnValue);
    }

    private void positionChanged(Vector2d oldPosition){
        map.positionChanged(oldPosition, this);
    }

    public byte[] getGenes() {
        return genes.getGenome();
    }

    public boolean isDead(){
        return energy == 0;
    }

    public void decreaseEnergy(){
        energy--;
    }

    public boolean canBreed(int minimumEnergy){
        return energy >= minimumEnergy;
    }

    public void increaseEnergy(int nutrition){
        energy += nutrition;
    }

    public int getEnergy(){
        return energy;
    }

    public int getLifespan(){
        return lifespan;
    }

    public void addOffspring(Animal animal){
        offsprings.add(animal);
    }

    public int getAllOffspringCount(){
        int offspringCount = 0;
        for (Animal offSpring: offsprings){
            offspringCount ++;
            offspringCount += offSpring.getAllOffspringCount();
        }
        return offspringCount;
    }

    public int getOffspringCount(){
        return offsprings.size();
    }

    public MapDirection getOrientation(){
        return orientation;
    }

    public String getGenomeString(){
        return genes.toString();
    }
}
