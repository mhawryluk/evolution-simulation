package project;
import org.junit.Assert;
import org.junit.Test;
import project.engine.Animal;
import project.engine.EvolutionMap;
import project.engine.MapDimensions;
import project.engine.Vector2d;

public class ReproductionTest {

    @Test
    public void offspringGenesTest(){

        EvolutionMap map = new EvolutionMap(new MapDimensions(10, 10, 50));
        Animal animal1 = new Animal(map, new Vector2d(0,0), 5);
        Animal animal2 = new Animal(map, new Vector2d(0,0), 5);

        Animal offspring = new Animal(map, animal1, animal2);

        byte[] offspringGenes = offspring.getGenes();
        Assert.assertEquals(offspringGenes[0], 0);

        for (int i = 0; i < offspring.getGenes().length -1 ; i++){
            Assert.assertTrue(offspringGenes[i+1]-offspringGenes[i] < 2 && offspringGenes[i+1]-offspringGenes[i] >= 0);
        }
    }

    @Test
    public void offSpringPositionTest(){
        EvolutionMap map = new EvolutionMap(new MapDimensions(10, 10, 50));
        Animal animal1 = new Animal(map, new Vector2d(5,5), 5);
        Animal animal2 = new Animal(map, new Vector2d(5,5), 5);

        Animal animal3 = new Animal(map, animal1, animal2);
        Vector2d position = animal3.getPosition();
        Assert.assertTrue(position.follows(new Vector2d(4,4))
                && position.precedes(new Vector2d(6,6))
                && !position.equals(new Vector2d(5,5)));

        animal1 = new Animal(map, new Vector2d(0,0), 5);
        animal2 = new Animal(map, new Vector2d(0,0), 5);

        animal3 = new Animal(map, animal1, animal2);
        position = animal3.getPosition();

        Assert.assertTrue(!position.equals(new Vector2d(0,0))
                        && ((position.follows(new Vector2d(0,0))
                        && position.precedes(new Vector2d(1,1)))
                        || position.equals(new Vector2d(9,9))
                        || position.equals(new Vector2d(9,0))
                || position.equals(new Vector2d(9,1))
                || position.equals(new Vector2d(0,9))
                || position.equals(new Vector2d(1,9))));
    }
}
