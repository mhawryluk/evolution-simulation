package project.engine;

import java.util.concurrent.ThreadLocalRandom;

public class Animal extends MapElement {
    private MapDirection orientation;
    private final EvolutionMap map;
    private final Genome genome;
    private int lifespan;
    private int childrenCount;

    public Animal(EvolutionMap map, Vector2d position, int energy){
        this(map, position, energy, new Genome());
    }

    public Animal(EvolutionMap map, Vector2d position, int energy, Genome genome) {
        this.map = map;
        this.position = position;
        this.energy = energy;
        this.orientation = MapDirection.NORTH.turn(ThreadLocalRandom.current().nextInt(0, 8));
        this.genome = genome;
        map.place(this);
    }

    public void move() {
        Vector2d newPosition = position.add(orientation.toVector());
        newPosition = map.wrapPosition(newPosition);
        Vector2d oldPosition = position;
        position = newPosition;
        positionChanged(oldPosition);
        lifespan++;
    }

    public void turn(int turnValue) {
        orientation = orientation.turn(turnValue);
    }

    public int getRandomTurnValue() {
        return genome.getRandomGene();
    }

    private void positionChanged(Vector2d oldPosition) {
        map.positionChanged(oldPosition, this);
    }

    public void decreaseEnergy(int energy) {
        this.energy -= energy;
    }

    public void energyLost() {
        energy *= 0.75;
    }

    public void increaseEnergy(int nutrition) {
        energy += nutrition;
    }

    public void newChild() {
        childrenCount++;
    }

    public boolean isDead() {
        return energy <= 0;
    }

    public int getEnergy() {
        return energy;
    }

    public int getLifespan() {
        return lifespan;
    }


    public int getChildrenCount() {
        return childrenCount;
    }

    public MapDirection getOrientation() {
        return orientation;
    }

    public byte[] getGenome() {
        return genome.getGenome();
    }


    public String getGenomeString() {
        return genome.toString();
    }
}
