package project;
import org.junit.Assert;
import org.junit.Test;
import project.engine.Animal;
import project.engine.EvolutionMap;
import project.engine.MapDimensions;
import project.engine.Vector2d;


public class AnimalMovementTest {

    private final Animal animal = new Animal(new EvolutionMap(new MapDimensions(5,5, 50)), new Vector2d(0,0), 0);

    @Test
    public void testMovement(){

        for (int i = 0; i < 10; i++){
            animal.move();
            System.out.println(animal.getPosition());
            Assert.assertTrue(animal.getPosition().follows(new Vector2d(0,0))
                        && animal.getPosition().precedes(new Vector2d(4,4)));
        }
    }
}
