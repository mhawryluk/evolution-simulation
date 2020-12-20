package project.engine;

import org.junit.Assert;
import org.junit.Test;

public class AnimalTest {

    @Test
    public void animalEnergyTest(){

        Animal animal = new Animal(new EvolutionMap(new MapDimensions(5,5,5)), new Vector2d(0,0), 5);

        Assert.assertEquals(animal.getEnergy(), 5);

        animal.decreaseEnergy();
        Assert.assertEquals(animal.getEnergy(), 4);

        animal.energyLost();
        Assert.assertEquals(animal.getEnergy(), 3);

        animal.increaseEnergy(3);
        Assert.assertEquals(animal.getEnergy(), 6);

        animal.energyLost();
        Assert.assertEquals(animal.getEnergy(), 4);

    }

    @Test
    public void movementTest(){

        Animal animal = new Animal(new EvolutionMap(new MapDimensions(5,5, 50)), new Vector2d(0,0), 10);

        while (animal.getOrientation() != MapDirection.NORTH){
            animal.turn(1);
        }

        animal.move();
        Assert.assertEquals(animal.getPosition(), new Vector2d(0,4));

        animal.turn(6);
        animal.move();
        Assert.assertEquals(animal.getPosition(), new Vector2d(4,4));

        animal.turn(5);
        animal.move();
        Assert.assertEquals(animal.getPosition(), new Vector2d(0,0));

        animal.move();
        Assert.assertEquals(animal.getPosition(), new Vector2d(1,1));

        animal.turn(3);
        animal.move();
        Assert.assertEquals(animal.getPosition(), new Vector2d(0,1));
    }

    @Test
    public void turnTest(){
        Animal animal = new Animal(new EvolutionMap(new MapDimensions(2,2, 5)), new Vector2d(0,0), 10);

        while (animal.getOrientation() != MapDirection.SOUTH){
            animal.turn(1);
        }

        Assert.assertEquals(animal.getOrientation(), MapDirection.SOUTH);
        animal.turn(2);
        Assert.assertEquals(animal.getOrientation(), MapDirection.WEST);

        animal.turn(5);
        Assert.assertEquals(animal.getOrientation(), MapDirection.SOUTH_EAST);

        animal.turn(2);
        Assert.assertEquals(animal.getOrientation(), MapDirection.SOUTH_WEST);

    }
}
