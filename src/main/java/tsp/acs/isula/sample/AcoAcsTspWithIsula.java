package tsp.acs.isula.sample;

import isula.aco.*;
import isula.aco.algorithms.acs.PseudoRandomNodeSelection;
import isula.aco.algorithms.antsystem.OfflinePheromoneUpdate;
import isula.aco.algorithms.antsystem.OnlinePheromoneUpdate;
import isula.aco.algorithms.antsystem.StartPheromoneMatrix;
import isula.aco.exception.InvalidInputException;
import tsp.isula.sample.AcoTspWithIsula;
import isula.aco.tsp.TspEnvironment;

import javax.naming.ConfigurationException;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * This class solves the Berlin52 instance of the TSPLIB repository using an Ant Colony System algorithm,
 * trying to emulate the procedure present in Section 6.4 of the Clever Algorithms book by Jason Brownlee.
 */
public class AcoAcsTspWithIsula {

    private static Logger logger = Logger.getLogger(AcoAcsTspWithIsula.class.getName());

    public static void main(String... args) throws IOException, InvalidInputException, ConfigurationException {
        logger.info("ANT COLONY SYSTEM FOR THE TRAVELING SALESMAN PROBLEM");

        String fileName = "C:\\Users\\Carlos G. Gavidia\\git\\aco-tsp\\src\\main\\resources\\berlin52.tsp";
        logger.info("fileName : " + fileName);

        double[][] problemRepresentation = AcoTspWithIsula.getRepresentationFromFile(fileName);

        AcsTspProblemConfiguration configurationProvider = new AcsTspProblemConfiguration(problemRepresentation);
        AntColony<Integer, TspEnvironment> colony = AcoTspWithIsula.getAntColony(configurationProvider);
        TspEnvironment environment = new TspEnvironment(problemRepresentation);

        AcoProblemSolver<Integer, TspEnvironment> solver = new AcoProblemSolver<>();
        solver.initialize(environment, colony, configurationProvider);
        solver.addDaemonActions(new StartPheromoneMatrix<Integer, TspEnvironment>());
        solver.addDaemonActions(getGlobalPheromoneUpdatePolicy());

        solver.getAntColony().addAntPolicies(getLocalPheromoneUpdatePolicy());
        solver.getAntColony().addAntPolicies(new PseudoRandomNodeSelection<Integer, TspEnvironment>());
        solver.solveProblem();
    }

    /**
     * Return the local procedure for pheromone update, executed after an Ant has build a feasible solution.
     *
     * @return Ant Policy for local pheromone update.
     */
    private static AntPolicy<Integer, TspEnvironment> getLocalPheromoneUpdatePolicy() {
        return new OnlinePheromoneUpdate<Integer, TspEnvironment>() {

            @Override
            protected double getNewPheromoneValue(Integer solutionComponent,
                                                  Integer positionInSolution,
                                                  TspEnvironment environment,
                                                  ConfigurationProvider configurationProvider) {

                AcsTspProblemConfiguration configuration = (AcsTspProblemConfiguration) configurationProvider;
                Double afterEvaporation = (1 - configuration.getLocalPheromoneCoefficient()) * getAnt().getPheromoneTrailValue(
                        solutionComponent, positionInSolution, environment);
                Double contribution = configuration.getLocalPheromoneCoefficient() * configurationProvider.getInitialPheromoneValue();

                return afterEvaporation + contribution;
            }
        };
    }

    /**
     * Returns the global pheromone update procedure, executed after all ants have finished their solution construction.
     *
     * @return Daemon Action for global pheromone update.
     */
    private static DaemonAction<Integer, TspEnvironment> getGlobalPheromoneUpdatePolicy() {
        return new OfflinePheromoneUpdate<Integer, TspEnvironment>() {
            @Override
            protected double getNewPheromoneValue(Ant<Integer, TspEnvironment> ant, Integer positionInSolution,
                                                  Integer solutionComponent,
                                                  TspEnvironment environment,
                                                  ConfigurationProvider configurationProvider) {
                Double afterEvaporation = ant.getPheromoneTrailValue(solutionComponent, positionInSolution, environment) *
                        configurationProvider.getEvaporationRatio();
                Double pheromoneDecay = 1 - configurationProvider.getEvaporationRatio();
                Double contribution = pheromoneDecay / ant.getSolutionCost(environment);
                return afterEvaporation + contribution;
            }
        };
    }
}
