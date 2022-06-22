package tsp.acs.isula.sample;

import isula.aco.algorithms.acs.AcsConfigurationProvider;
import isula.aco.tsp.AntForTsp;
import isula.aco.tsp.TspEnvironment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class contains the algorithm configuration of the Ant Colony System algorithm described in
 * Section 6.4 of the Clever Algorithms book by Jason Brownlee.
 */
public class AcsTspProblemConfiguration implements AcsConfigurationProvider {

    private final double initialPheromoneValue;

    /**
     * In the algorithm described in the book, the initial pheromone value was a function of the quality of a
     * random solution. That logic is included in this constructor.
     *
     * @param environment TSP environment with coordinate information.
     */
    public AcsTspProblemConfiguration(TspEnvironment environment) {
        List<Integer> randomSolution = new ArrayList<>();
        int numberOfCities = environment.getProblemRepresentation().length;

        for (int cityIndex = 0; cityIndex < numberOfCities; cityIndex += 1) {
            randomSolution.add(cityIndex);
        }

        Collections.shuffle(randomSolution);

        double randomQuality = AntForTsp.getTotalDistance(randomSolution, environment);
        this.initialPheromoneValue = 1.0 / (numberOfCities * randomQuality);
    }

    /**
     * Returns the pheromone coefficient for the local pheromone update procedure.
     *
     * @return Local pheromone coefficient.
     */
    public double getLocalPheromoneCoefficient() {
        return 0.1;
    }

    @Override
    public int getNumberOfAnts() {
        return 30;
    }

    @Override
    public double getEvaporationRatio() {
        return 1 - 0.1;
    }

    @Override
    public double getPheromoneDecayCoefficient() {
        return 1 - 0.1;
    }

    @Override
    public int getNumberOfIterations() {
        return 100;
    }

    @Override
    public double getInitialPheromoneValue() {
        return initialPheromoneValue;
    }

    @Override
    public double getHeuristicImportance() {
        return 2.5;
    }

    @Override
    public double getPheromoneImportance() {
        return 1.0;
    }

    @Override
    public double getBestChoiceProbability() {
        return 0.9;
    }


}
