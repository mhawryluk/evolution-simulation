package project;
import org.junit.Assert;
import org.junit.Test;

public class ReproductionTest {

    @Test
    public void offspringGenesTest(){
        EvolutionMap map = new EvolutionMap(10, 10, 50);
        Animal animal1 = new Animal(map, 5);
        Animal animal2 = new Animal(map, 5);

//        System.out.println(Arrays.toString(animal1.getGenes()));
//        System.out.println(Arrays.toString(animal2.getGenes()));

        Animal offspring = new Animal(map, animal1, animal2);
//        System.out.println(Arrays.toString(offspring.getGenes()));

        byte[] offspringGenes = offspring.getGenes();
        Assert.assertEquals(offspringGenes[0], 0);

        for (int i = 0; i < offspring.getGenes().length -1 ; i++){
            Assert.assertTrue(offspringGenes[i+1]-offspringGenes[i] < 2);
        }
    }

    @Test
    public void reproductionTest(){
        EvolutionMap map = new EvolutionMap(10, 10, 50);
        Animal animal1 = new Animal(map, 5);
        Animal animal2 = new Animal(map, 5);
        //(...)
    }

}
