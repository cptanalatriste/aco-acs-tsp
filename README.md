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

The results 
-----------
For the berlin52 problem instance, the optional solution has a total distance of 7542 units. Under the current configuration, the solutions produced by the algorithm are around 7941 after an execution time of 1.5 seconds.

How to use this code
--------------------
The code uploaded to this GitHub Repository corresponds to a Maven Java Project. As such, it is strongly recommended that you have Maven installed before working with it.

**This project depends on the Isula Framework**.  You need to download and install the Isula Framework Project on your local Maven repository. Follow the instructions available in https://github.com/cptanalatriste/isula

This project also depends on some components of our Ant System implementation. The `aco-tsp` project that contains them is available on this Github Repository: https://github.com/cptanalatriste/aco-tsp

Keep in mind that several file and folder locations were configured on the `AcoAcsTspWithIsula.java` file. You need to set values according to your environment in order to avoid a `FileNotFoundException`. Once this is ready, you can launch this project by executing `mvn exec:java -Dexec.mainClass="tsp.acs.isula.sample.AcoAcsTspWithIsula"` from the project root folder.

More about Isula
----------------
Visit the Isula Framework site: http://cptanalatriste.github.io/isula/

Review the Isula JavaDoc: http://cptanalatriste.github.io/isula/doc/

Questions, issues or support?
----------------------------
Feel free to contact me at carlos.gavidia@pucp.edu.pe.
