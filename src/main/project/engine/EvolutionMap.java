package project.engine;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class EvolutionMap {

    public final MapDimensions dimensions;

    public final Map<Vector2d, Grass> placedGrass = new LinkedHashMap<>();
    private final HashSet<Vector2d> jungleAvailablePositions = new HashSet<>();
    private final Map<Vector2d, ArrayList<Animal>> placedAnimals = new LinkedHashMap<>();
    private int animalsCount = 0;
    private int grassCount = 0;

    public EvolutionMap(MapDimensions dimensions){
        this.dimensions = dimensions;

        for (int x = dimensions.jungleLowerLeft.x;  x <= dimensions.jungleUpperRight.x; x++)
            for (int y = dimensions.jungleLowerLeft.y; y <= dimensions.jungleUpperRight.y; y++)
                jungleAvailablePositions.add(new Vector2d(x, y));
    }

    public boolean place(Animal animal){
        Vector2d position = animal.getPosition();

        if (!dimensions.isWithinMap(position))
            throw new IllegalArgumentException(position + " is not a correct place position");

        if (!placedAnimals.containsKey(position))
            placedAnimals.put(position, new ArrayList<>());

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

    public Object objectAt(Vector2d position){
        if (placedAnimals.containsKey(position) && placedAnimals.get(position).size() > 0) {
            return placedAnimals.get(position);
        }
        return placedGrass.get(position);
    }

    public ArrayList<Animal> animalsAt(Vector2d position){
        return placedAnimals.get(position);
    }

    public ArrayList<Animal> animalsAtSortedByEnergy(Vector2d position){
        ArrayList<Animal> animalsAtPosition = placedAnimals.get(position);
        animalsAtPosition.sort(Comparator.comparing(Animal::getEnergy));
        Collections.reverse(animalsAtPosition);
        return animalsAtPosition;
    }

    public Grass grassAt(Vector2d position){
        return placedGrass.get(position);
    }

    public boolean isOccupied(Vector2d position){
        return objectAt(position) != null;
    }

    public Vector2d wrapPosition(Vector2d position){
        if (dimensions.isWithinMap(position)) return position;
        return new Vector2d((position.x + dimensions.width) % dimensions.width,
                            (position.y + dimensions.height) % dimensions.height);
    }

    public Vector2d getFreeJunglePosition(){
        if (jungleAvailablePositions.isEmpty()) return null;

        Iterator <Vector2d> iterator = jungleAvailablePositions.iterator();
        int randNum = ThreadLocalRandom.current().nextInt(0, jungleAvailablePositions.size());
        for (int i = 0; i < randNum; i++) iterator.next();
        return iterator.next();
    }

    public Vector2d getFreePosition(){
        if (animalsCount + grassCount == dimensions.allPositionsCount())
            return null;

        if (dimensions.jungleUpperRight.equals(dimensions.upperRight))
            return null;

        if (dimensions.jungleRatio > 70 || animalsCount + grassCount > 0.7 * dimensions.allPositionsCount()){
            ArrayList<Vector2d> freePositions = new ArrayList<>();
            for (int i = 0; i < dimensions.jungleUpperRight.x; i++){
                for (int j = 0; j < dimensions.jungleUpperRight.y; j++){
                    Vector2d position = new Vector2d(i, j);
                    if (!dimensions.isWithinJungle(position) && objectAt(position) == null)
                        freePositions.add(position);
                }
            }
            return freePositions.get(ThreadLocalRandom.current().nextInt(0, freePositions.size()));
        }

        else {
            while (true){
                int x = ThreadLocalRandom.current().nextInt(0, dimensions.width);
                int y = ThreadLocalRandom.current().nextInt(0, dimensions.height);
                Vector2d position = new Vector2d(x, y);
                if (!dimensions.isWithinJungle(position) && objectAt(position) == null)
                   return position;
            }
        }
    }

    public Vector2d getOffspringPosition(Vector2d position) {

        int x = position.x;
        int y = position.y;
        int[] possibleX = {x - 1, x - 1, x - 1, x, x, x + 1, x + 1, x + 1};
        int[] possibleY = {y - 1, y, y + 1, y - 1, y + 1, y - 1, y, y + 1};

        Vector2d newPosition = position;

        for (int i = 0; i < 8; i++) {
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
        placedAnimals.get(animal.getPosition()).remove(animal);
        animalsCount--;
    }

    public void updateJungle(){
        jungleAvailablePositions.clear();

        for (int x = dimensions.jungleLowerLeft.x;  x <= dimensions.jungleUpperRight.x; x++){
            for (int y = dimensions.jungleLowerLeft.y; y <= dimensions.jungleUpperRight.y; y++){
                Vector2d position = new Vector2d(x, y);
                if ((!placedAnimals.containsKey(position) || placedAnimals.get(position).size() == 0)
                        && !placedGrass.containsKey(position)){
                    jungleAvailablePositions.add(new Vector2d(x, y));
                }
            }
        }

    }

    public Animal getClickedAnimal(Vector2d position){
        ArrayList<Animal> animalsOnPosition = placedAnimals.get(position);
        if (animalsOnPosition == null || animalsOnPosition.size() == 0) return null;
        return animalsOnPosition.get(0);
    }


    public void positionChanged(Vector2d oldPosition, Animal element){
        placedAnimals.get(oldPosition).remove(element);
        Vector2d newPosition = element.getPosition();

        if (!placedAnimals.containsKey(newPosition))
            placedAnimals.put(newPosition, new ArrayList<>());

        placedAnimals.get(newPosition).add(element);
    }

}
