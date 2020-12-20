package project.engine;

import java.util.ArrayList;
import java.util.HashSet;

public class GrassConsumptionEngine {

    private final int nutritionalEnergy;
    private final EvolutionMap map;

    public GrassConsumptionEngine(EvolutionMap map, int nutritionalEnergy){
        this.map = map;
        this.nutritionalEnergy= nutritionalEnergy;
    }

    public int consumption(HashSet<Vector2d> takenPositions){
        int grassEatenCount = 0;

        for (Vector2d position: takenPositions){
            Grass grass = map.grassAt(position);
            if (grass == null) continue;

            ArrayList<Animal> animalsAtPosition = map.animalsAt(position);
            if (animalsAtPosition.isEmpty()) continue;

            int maxEnergy = animalsAtPosition.get(0).getEnergy();
            int maxEnergyAnimalCount = 1;

            while (maxEnergyAnimalCount < animalsAtPosition.size()
                    && animalsAtPosition.get(maxEnergyAnimalCount).getEnergy() == maxEnergy){
                maxEnergyAnimalCount++;
            }

            for (int i = 0; i < maxEnergyAnimalCount; i++){
                animalsAtPosition.get(i).increaseEnergy(nutritionalEnergy/maxEnergyAnimalCount);
            }

            grassEatenCount++;
            map.removeGrass(grass);
        }
        return grassEatenCount;
    }
}
