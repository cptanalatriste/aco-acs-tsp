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
