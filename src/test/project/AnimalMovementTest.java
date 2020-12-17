package project;
import org.junit.Assert;
import org.junit.Test;


public class AnimalMovementTest {

    private final Animal animal = new Animal(new EvolutionMap(5,5, 50), 0);

    @Test
    public void testMovement(){

        for (int i = 0; i < 10; i++){
            animal.move();
            System.out.println(animal.getPosition());
            Assert.assertTrue(animal.getPosition().follows(new Vector2d(0,0))
                        && animal.getPosition().precedes(new Vector2d(4,4)));
        }
    }

    @Test
    public void testAnimalsSameField(){
        EvolutionMap map = new EvolutionMap(2, 2, 50);
        Animal animal1 = new Animal(map, 10);
        Animal animal2 = new Animal(map, 10);
        System.out.println(map.placedAnimals);
        animal1.move();
        animal2.move();
        System.out.println(map.placedAnimals);
        animal1.move();
        animal2.move();
        System.out.println(map.placedAnimals);
        animal1.move();
        animal2.move();
        System.out.println(map.placedAnimals);
    }

}
