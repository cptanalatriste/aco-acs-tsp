# aco-acs-tsp
A Java Program that solves the Travelling Salesman Problem using the Ant Colony System algorithm. The algorithm details and configuration where taken from in Section 6.4 of the Clever Algorithms book by Jason Brownlee.

In the same fashion as the book, we use the berlin52 instance from TSPLIB as a testbed for the program.

The Ant-Colony Algorithm
------------------------
This program uses the Ant Colony System algorithm for solving the Travelling Salesman Problem. The specifics of the algorithm described in the book are also ported to this Java program. To implement this algorithm, we use the Isula Framework.

```java
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
```
The implemented process has the following characteristics:
* The initial pheromone value is a function of the number of cities and the cost of a randomly-generated solution. That procedure is implemented in the `AcsTspProblemConfiguration` class.
* The pheromone update happens at two levels in the proposed algorithm: The local pheromone update is triggered after an Ant has finished constructing its solution and the global pheromone update starts when the whole colony have completed their solutions.
* The global and local pheromone updates depend on different evaporation ratios. Check this values on the `AcsTspProblemConfiguration` class.
* Being an Ant Colony System algorithm, the policy for incorporing components to a solution is the Pseudo-Random Proportional Selection Rule.
