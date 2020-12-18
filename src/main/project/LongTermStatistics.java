package project;
import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class LongTermStatistics {

    private final HashMap<String, Integer> genomeDominantCount = new HashMap<>();
    private double averageAnimalCount = 0;
    private double averageGrassCount = 0;
    private double averageEnergy = 0;
    private double averageOffspringCount = 0;
    private double averageLifespan = 0;

    private final Statistics stats;
    private int generationsCounted = 0;
    private final int numGenerations;
    private final String file;
    private Simulation simulation;

    public LongTermStatistics (Statistics stats, int numGenerations, String fileName){
        this.stats = stats;
        this.file = fileName;
        this.numGenerations = numGenerations;
    }

    public void update(){
        if (generationsCounted == numGenerations){
            saveStatistics(file);
            simulation.unsetLongTermStatistics();
        }
        averageAnimalCount = ((averageAnimalCount * generationsCounted) + stats.countAnimals())/(generationsCounted+1);
        averageGrassCount = ((averageGrassCount * generationsCounted) + stats.countGrass())/(generationsCounted+1);
        averageEnergy = ((averageEnergy * generationsCounted) + stats.getAverageEnergy())/(generationsCounted+1);
        averageOffspringCount = ((averageOffspringCount * generationsCounted) + stats.getAverageOffspringCount())/(generationsCounted+1);
        averageLifespan = ((averageLifespan  * generationsCounted) + stats.getAverageLifespan())/(generationsCounted+1);

        String dominantGenome = stats.getDominantGenome();

        if (!genomeDominantCount.containsKey(dominantGenome)){
            genomeDominantCount.put(dominantGenome, 1);
        } else {
            genomeDominantCount.replace(dominantGenome, genomeDominantCount.get(dominantGenome) + 1);
        }

        generationsCounted++;
    }

    public void saveStatistics(String file){
        String dominantGenome = Collections.max(genomeDominantCount.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();
        try {
            FileWriter myWriter = new FileWriter(file);
            myWriter.write("animal count: " + averageAnimalCount +
                            "\ngrass count: " + averageGrassCount +
                            "\nenergy: " + averageEnergy +
                            "\nchildren count: " + averageOffspringCount +
                            "\nlifespan: " + averageLifespan
            );
            myWriter.close();
            JOptionPane.showMessageDialog(null, "statistics are saved to the file");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "couldn't write to file");
            e.printStackTrace();
        }
    }

    public void setSimulation(Simulation simulation){
        this.simulation = simulation;
    }
}
