package project;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class EvolutionMap implements IWorldMap{
    public final int width;
    public final int height;
    private final Vector2d lowerLeft = new Vector2d(0,0);
    private final Vector2d upperRight;
    public final Vector2d jungleLowerLeft;
    public final Vector2d jungleUpperRight;
    public final Map<Vector2d, Grass> placedGrass = new LinkedHashMap<>();
    public final HashSet<Vector2d> jungleAvailablePositions = new HashSet<>();
    public final Map<Vector2d, TreeSet<Animal> > placedAnimals = new LinkedHashMap<>();
    private final MapVisualizer visualizer;
    private int animalsCount = 0;
    private int grassCount = 0;

    public EvolutionMap(int width, int height, int jungleRatio){
        visualizer = new MapVisualizer(this);
        this.width = width;
        this.height = height;
        upperRight = new Vector2d(width-1, height-1);
        int jungleWidth = (int)(Math.sqrt((double)jungleRatio/(100+jungleRatio))*width);
        int jungleHeight = (int)(Math.sqrt((double)jungleRatio/(100+jungleRatio))*height);
        jungleLowerLeft = new Vector2d((width-jungleWidth)/2, (height - jungleHeight)/2);
        jungleUpperRight = new Vector2d((width+jungleWidth)/2, (height + jungleHeight)/2);

        for (int x = jungleLowerLeft.x;  x <= jungleUpperRight.x; x++)
            for (int y = jungleLowerLeft.y; y <= jungleUpperRight.y; y++)
                jungleAvailablePositions.add(new Vector2d(x, y));
    }

    public boolean place(Animal animal){
        Vector2d position = animal.getPosition();
        if (!(position.follows(lowerLeft) && position.precedes(upperRight)))
            throw new IllegalArgumentException(position + " is not a correct place position");

        if (!placedAnimals.containsKey(position))
            placedAnimals.put(position, new TreeSet<>(Comparator.comparing(Animal::getEnergy)));

        placedAnimals.get(position).add(animal);
        jungleAvailablePositions.remove(position);

        animalsCount++;
        return true;
    }

    public void place(Grass grass){
        Vector2d position = grass.getPosition();
        if (isOccupied(position)){
            throw new IllegalStateException(position.toString()+" is occupied, grass cannot be placed");
        }
        placedGrass.put(position, grass);
        jungleAvailablePositions.remove(position);
        grassCount++;
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return true;
    }

    public Object objectAt(Vector2d position){
        if (placedAnimals.containsKey(position) && placedAnimals.get(position).size() > 0) {
            return placedAnimals.get(position);
        }
        return placedGrass.get(position);
    }

    public TreeSet<Animal> animalsAt(Vector2d position){
        return placedAnimals.get(position);
    }

    public Grass grassAt(Vector2d position){
        return placedGrass.get(position);
    }

    public boolean isOccupied(Vector2d position){
        return objectAt(position) != null;
    }

    public Vector2d wrapPosition(Vector2d position){
        if (position.follows(lowerLeft) && position.precedes(upperRight)) return position;
        return new Vector2d((position.x + width)%width, (position.y + height)%height);
    }

    public Vector2d getFreeJunglePosition(){
        if (jungleAvailablePositions.isEmpty()) return null;

        Iterator <Vector2d> iterator = jungleAvailablePositions.iterator();
        int randNum = ThreadLocalRandom.current().nextInt(0, jungleAvailablePositions.size());
        for (int i = 0; i < randNum; i++)
            iterator.next();
        return iterator.next();
    }

    public Vector2d getFreePosition(){
        if (animalsCount + grassCount == width*height){
            return null;
        }

        while (true){
            int x = ThreadLocalRandom.current().nextInt(0, width);
            int y = ThreadLocalRandom.current().nextInt(0, height);
            Vector2d position = new Vector2d(x, y);
            if (!isInJungle(position) && objectAt(new Vector2d(x, y)) == null)
                return new Vector2d(x, y);
        }
    }

    public String toString() {
        return visualizer.draw(lowerLeft, upperRight);
    }

    public Vector2d getOffspringPosition(Vector2d position) {
        int x = position.x;
        int y = position.y;
        int[] possibleX = {x - 1, x - 1, x - 1, x, x, x + 1, x + 1, x + 1};
        int[] possibleY = {y - 1, y, y + 1, y - 1, y + 1, y - 1, y, y + 1};

        Vector2d newPosition = position;

        for (int i = 0; i <= 8; i++) {
            int randIndex = ThreadLocalRandom.current().nextInt(i, 8);

            int temp = possibleX[i];
            possibleX[i] = possibleX[randIndex];
            possibleX[randIndex] = temp;

            temp = possibleY[i];
            possibleY[i] = possibleY[randIndex];
            possibleY[randIndex] = temp;

            newPosition = wrapPosition(new Vector2d(possibleX[i], possibleY[i]));

            if (!isOccupied(newPosition) || objectAt(newPosition) instanceof Grass)
                return newPosition;
        }
        return newPosition;
    }

    public void removeGrass(Grass grass){
        placedGrass.remove(grass.getPosition());
        grassCount--;
    }

    public void removeAnimal(Animal animal){
        Vector2d position = animal.getPosition();
        placedAnimals.get(position).remove(animal);
        animalsCount--;
    }

    public void updateJungle(HashSet<Vector2d> takenPositions){
        jungleAvailablePositions.clear();
        for (int x = jungleLowerLeft.x;  x <= jungleUpperRight.x; x++){
            for (int y = jungleLowerLeft.y; y <= jungleUpperRight.y; y++){
                Vector2d position = new Vector2d(x, y);
                if (!takenPositions.contains(position) && !placedGrass.containsKey(position)){
                    jungleAvailablePositions.add(new Vector2d(x, y));
                }
            }
        }
    }

    public boolean isInJungle(Vector2d position){
        return position.follows(jungleLowerLeft) && position.precedes(jungleUpperRight);
    }

    public Animal getPressedAnimal(Vector2d position){
        TreeSet<Animal> animalsOnPosition = placedAnimals.get(position);
        if (animalsOnPosition == null || animalsOnPosition.size() == 0) return null;
        return animalsOnPosition.first();
    }


    public void positionChanged(Vector2d oldPosition, Animal element){
        placedAnimals.get(oldPosition).remove(element);
        Vector2d newPosition = element.getPosition();
        if (!placedAnimals.containsKey(newPosition)){
            placedAnimals.put(newPosition, new TreeSet<>(Comparator.comparing(Animal::getEnergy)));
        }
        placedAnimals.get(newPosition).add(element);
    }
}
