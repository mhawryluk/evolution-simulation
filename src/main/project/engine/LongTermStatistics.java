package project.engine;

import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class LongTermStatistics {

    private final HashMap<String, Integer> genomeDominantCount = new HashMap<>();
    private double averageAnimalCount;
    private double averageGrassCount;
    private double averageEnergy;
    private double averageOffspringCount;
    private double averageLifespan;

    private final Statistics stats;
    private int generationsCounted;
    private final int generationsToCount;
    private final String file;
    private Simulation simulation;

    public LongTermStatistics(Statistics stats, int generationsToCount, String fileName) {
        this.stats = stats;
        this.file = fileName;
        this.generationsToCount = generationsToCount;
    }

    public void update() {

        averageAnimalCount = ((averageAnimalCount * generationsCounted) + stats.countAnimals()) / (generationsCounted + 1);
        averageGrassCount = ((averageGrassCount * generationsCounted) + stats.countGrass()) / (generationsCounted + 1);
        averageEnergy = ((averageEnergy * generationsCounted) + stats.getAverageEnergy()) / (generationsCounted + 1);
        averageOffspringCount = ((averageOffspringCount * generationsCounted) + stats.getAverageChildrenCount()) / (generationsCounted + 1);
        averageLifespan = ((averageLifespan * generationsCounted) + stats.getAverageLifespan()) / (generationsCounted + 1);

        String dominantGenome = stats.getDominantGenome();

        if (!genomeDominantCount.containsKey(dominantGenome)) {
            genomeDominantCount.put(dominantGenome, 1);
        } else {
            genomeDominantCount.replace(dominantGenome, genomeDominantCount.get(dominantGenome) + 1);
        }

        generationsCounted++;

        if (generationsCounted == generationsToCount) {
            saveStatistics(file);
            simulation.unsetLongTermStatistics();
        }
    }

    public void saveStatistics(String file) {

        String dominantGenome = Collections.max(genomeDominantCount.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();
        try {
            FileWriter myWriter = new FileWriter(file);
            myWriter.write("animal count: " + averageAnimalCount +
                    "\ngrass count: " + averageGrassCount +
                    "\nenergy: " + averageEnergy +
                    "\nchildren count: " + averageOffspringCount +
                    "\nlifespan: " + averageLifespan +
                    "\ndominant genome: " + dominantGenome
            );
            myWriter.close();
            JOptionPane.showMessageDialog(null, "statistics are saved to the file");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "couldn't write to file");
        }
    }

    public void setSimulation(Simulation simulation) {
        this.simulation = simulation;
    }
}
