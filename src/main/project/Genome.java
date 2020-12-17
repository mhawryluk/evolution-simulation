package project;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class Genome {
    private final int genomeLength = 32;
    private final int geneTypes = 8;
    private final byte[] genes = new byte[genomeLength];

    public Genome(){ //randomowy
        for (byte i = 0; i < geneTypes; i++)
            genes[i] = i;

        for (byte i = geneTypes; i < genomeLength; i++)
            genes[i] = (byte)ThreadLocalRandom.current().nextInt(0, geneTypes);

        Arrays.sort(genes);
    }

    public Genome(Animal fatherGenome, Animal motherGenome){ //genom dziecka
        byte[][] parentGenes = {fatherGenome.getGenes(), motherGenome.getGenes()};

        int splitPoint1 = ThreadLocalRandom.current().nextInt(1, genomeLength - 1);
        int splitPoint2 = ThreadLocalRandom.current().nextInt(splitPoint1, genomeLength);

        int[] index1 = {0,0,1,1,1,0};
        int[] index2 = {0,1,0,1,0,1};
        int[] index3 = {1,0,0,0,1,1};

        int randomCombination = ThreadLocalRandom.current().nextInt(0, 6);
        System.arraycopy(parentGenes[index1[randomCombination]], 0, genes, 0, splitPoint1);
        System.arraycopy(parentGenes[index2[randomCombination]], splitPoint1, genes, splitPoint1, splitPoint2 - splitPoint1);
        System.arraycopy(parentGenes[index3[randomCombination]], splitPoint2, genes, splitPoint2, genomeLength - splitPoint2);

        Arrays.sort(genes);

        int[] geneTypeCount = new int[geneTypes];

        for (byte geneType: genes){
            geneTypeCount[geneType]++;
        }

        // zamienianie genów, aby wszystkie występowały, okropne -> poprawić!
        for (byte i = 0; i < geneTypes; i++) {
            if (geneTypeCount[i] == 0) {
                while (true) {
                    int randomGeneType = ThreadLocalRandom.current().nextInt(0, geneTypes);
                    if (randomGeneType == i) continue;
                    if (geneTypeCount[randomGeneType] < 2) continue;

                    for (int j = 0; j < genomeLength; j++){
                        if (genes[j] == randomGeneType){
                            genes[j] = i;
                            geneTypeCount[i]++;
                            geneTypeCount[randomGeneType]--;
                        }
                    }
                    break;
                }
            }
        }

        Arrays.sort(genes);
    }

    public byte[] getGenome(){
        return genes.clone();
    }

    public byte getRandomGene(){
        return genes[ThreadLocalRandom.current().nextInt(0, genomeLength)];
    }

    @Override
    public String toString(){
        StringBuilder genomeString = new StringBuilder();
        for (byte gene: genes){
            genomeString.append(gene);
        }
        return genomeString.toString();
    }
}
