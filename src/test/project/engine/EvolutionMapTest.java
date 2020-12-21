package project.engine;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class EvolutionMapTest {


    @Test
    public void animalsOnMapTest() {
        EvolutionMap map = new EvolutionMap(new MapDimensions(5, 5, 50));
        Animal animal1 = new Animal(map, new Vector2d(1, 1), 10);
        Animal animal2 = new Animal(map, new Vector2d(1, 1), 5);
        Animal animal3 = new Animal(map, new Vector2d(1, 1), 8);

        ArrayList<Animal> animalsAtPosition = map.animalsAtSortedByEnergy(new Vector2d(1, 1));

        Assert.assertEquals(animalsAtPosition.size(), 3);
        Assert.assertEquals(animalsAtPosition.get(0), animal1);
        Assert.assertEquals(animalsAtPosition.get(1), animal3);
        Assert.assertEquals(animalsAtPosition.get(2), animal2);

        map.removeAnimal(animal2);
        animalsAtPosition = map.animalsAtSortedByEnergy(new Vector2d(1, 1));

        Assert.assertEquals(animalsAtPosition.get(0), animal1);
        Assert.assertEquals(animalsAtPosition.get(1), animal3);

        animal3.move();
        animal1.move();

        animalsAtPosition = map.animalsAt(new Vector2d(1, 1));
        Assert.assertEquals(0, animalsAtPosition.size());

        Assert.assertNull(map.animalsAt(new Vector2d(4, 4)));
    }

    @Test
    public void grassAtTest() {
        EvolutionMap map = new EvolutionMap(new MapDimensions(3, 3, 50));
        Grass grass = new Grass(new Vector2d(0, 1), map);
        Assert.assertEquals(map.grassAt(new Vector2d(0, 1)), grass);
        Assert.assertNull(map.grassAt(new Vector2d(1, 1)));

        map.removeGrass(grass);
        Assert.assertNull(map.grassAt(new Vector2d(0, 1)));
    }

    @Test
    public void objectAtTest() {
        EvolutionMap map = new EvolutionMap(new MapDimensions(3, 3, 50));
        Grass grass = new Grass(new Vector2d(1, 0), map);

        Assert.assertTrue(map.isOccupied(new Vector2d(1, 0)));
        Assert.assertFalse(map.isOccupied(new Vector2d(0, 1)));

        Assert.assertEquals(map.objectAt(new Vector2d(1, 0)), grass);

        Animal animal = new Animal(map, new Vector2d(1, 0), 10);
        Object objectAtPosition = map.objectAt(new Vector2d(1, 0));
        Assert.assertTrue(objectAtPosition instanceof ArrayList);
        Assert.assertEquals(((ArrayList<Animal>) objectAtPosition).get(0), animal);
    }

    @Test
    public void wrapPositionTest() {
        EvolutionMap map = new EvolutionMap(new MapDimensions(5, 5, 10));
        Assert.assertEquals(new Vector2d(1, 0), map.wrapPosition(new Vector2d(6, 5)));
        Assert.assertEquals(new Vector2d(4, 4), map.wrapPosition(new Vector2d(-1, -1)));
        Assert.assertEquals(new Vector2d(0, 0), map.wrapPosition(new Vector2d(0, 0)));
        Assert.assertEquals(new Vector2d(1, 1), map.wrapPosition(new Vector2d(1, 1)));
        Assert.assertEquals(new Vector2d(1, 1), map.wrapPosition(new Vector2d(6, 6)));
    }

    @Test
    public void getFreePositionTest() {
        EvolutionMap map = new EvolutionMap(new MapDimensions(100, 10, 50));
        Vector2d freePosition = map.getFreePosition();
        Assert.assertTrue(freePosition.follows(new Vector2d(0, 0))
                && freePosition.precedes(new Vector2d(99, 9))
                && !map.dimensions.isWithinJungle(freePosition));

        Assert.assertNull(map.objectAt(freePosition));

        map = new EvolutionMap(new MapDimensions(3, 3, 10));
        new Animal(map, new Vector2d(0, 0), 7);

        freePosition = map.getFreePosition();
        Assert.assertTrue(freePosition.follows(new Vector2d(0, 0))
                && freePosition.precedes(new Vector2d(4, 4))
                && !map.dimensions.isWithinJungle(freePosition)
                && !freePosition.equals(new Vector2d(0, 0)));

        Assert.assertNull(map.objectAt(freePosition));

        map = new EvolutionMap(new MapDimensions(1000, 1000, 90));
        freePosition = map.getFreePosition();
        Assert.assertTrue(freePosition.follows(new Vector2d(0, 0))
                && freePosition.precedes(new Vector2d(999, 999))
                && !map.dimensions.isWithinJungle(freePosition));

        Assert.assertNull(map.objectAt(freePosition));
    }
}