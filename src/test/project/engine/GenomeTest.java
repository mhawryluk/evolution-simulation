package project.engine;

import org.junit.Assert;
import org.junit.Test;

public class GenomeTest {

    @Test
    public void genomeTest() {
        byte[] genome = new Genome().getGenome();
        Assert.assertEquals(genome[0], 0);
        Assert.assertEquals(genome.length, 32);
        Assert.assertEquals(genome[31], 7);

        for (int i = 0; i < genome.length - 1; i++) {
            Assert.assertTrue(genome[i + 1] - genome[i] < 2 && genome[i + 1] - genome[i] >= 0);
        }
    }

    @Test
    public void offspringGenesTest() {

        EvolutionMap map = new EvolutionMap(new MapDimensions(10, 10, 50));
        Animal animal1 = new Animal(map, new Vector2d(0, 0), 5);
        Animal animal2 = new Animal(map, new Vector2d(0, 0), 5);

        byte[] offspringGenes = new Genome(animal1, animal2).getGenome();
        Assert.assertEquals(offspringGenes[0], 0);
        Assert.assertEquals(offspringGenes.length, 32);
        Assert.assertEquals(offspringGenes[31], 7);

        for (int i = 0; i < offspringGenes.length - 1; i++) {
            Assert.assertTrue(offspringGenes[i + 1] - offspringGenes[i] < 2 && offspringGenes[i + 1] - offspringGenes[i] >= 0);
        }
    }
}
