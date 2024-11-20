/*
* File: SimulatedAnnealing.java
* Author: José Luis Risco Martín <jlrisco@ucm.es>
* Author: José Manuel Colmenar Verdugo
* Created: 2010/05/27 (YYYY/MM/DD)
*
* Copyright (C) 2010
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package jeco.core.algorithms;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import jeco.core.benchmarks.Rastringin;
import jeco.core.operator.comparator.SimpleDominance;
import jeco.core.problem.Problem;
import jeco.core.problem.Solution;
import jeco.core.problem.Solutions;
import jeco.core.problem.Variable;
import jeco.core.util.logger.JecoLogger;
import jeco.core.util.random.RandomGenerator;

/**
 * Class implementing the simulated annealing technique for problem solving.
 *
 * Works only for one objective.
 *
 * It does not require temperature to be given because it automatically adapts the
 * parameters: Natural Optimization [de Vicente et al., 2000]
 */
public class SimulatedAnnealing<T extends Variable<?>> extends Algorithm<T> {

    private static final Logger LOGGER = Logger.getLogger(SimulatedAnnealing.class.getName());

    ///////////////////////////////////////////////////////////////////////////
    /**
     * Maximum number of iterations
     */
    protected Integer maxIterations = 10000;
    /**
     * Current iteration
     */
    protected Integer currentIteration = 0;

    /* Cost-related attributes */
    /**
     * Current minimum cost
     */
    private Double currentMinimumCost = Double.MAX_VALUE;
    /**
     * Initial cost
     */
    private Double initialCost = Double.MAX_VALUE;
    /**
     * Weight of the temperature
     */
    private Double k = 1.0;
    ///////////////////////////////////////////////////////////////////////////
    /**
     * Target objective. If the algorithm reaches this obj, the optimization
     * stops
     */
    protected Double targetObj = null;
    /**
     * Dominance comparator
     */
    protected SimpleDominance<T> dominance = new SimpleDominance<>();
    /**
     * Current solution
     */
    private Solution<T> currentSolution;
    /**
     * Best solution found
     */
    protected Solution<T> bestSolution;

    /**
     * Parameterized constructor
     *
     * @param problem Problem to be solved
     * @param maxIterations number of iterations where the search will stop.
     * @param k is the weight of the temperature (Default = 1.0)
     * @param targetObj If the algorithm reaches this obj, the optimization
     * stops (Default = Double.NEGATIVE_INFINITY)
     */
    public SimulatedAnnealing(Problem<T> problem, Integer maxIterations, Double k, Double targetObj) {
        super(problem);
        this.maxIterations = maxIterations;
        this.k = k;
        this.targetObj = targetObj;
    }

    /**
     * This constructor allows to establish the maximum number of iterations.
     *
     * @param problem Problem to be solved
     * @param maxIterations number of iterations where the search will stop.
     */
    public SimulatedAnnealing(Problem<T> problem, Integer maxIterations) {
        this(problem, maxIterations, 1.0, Double.NEGATIVE_INFINITY);
    }

    @Override
    public void initialize(Solutions<T> initialSolutions) {
        if (initialSolutions == null) {
            currentSolution = this.problem.newRandomSetOfSolutions(1).get(0);
        } else {
            currentSolution = initialSolutions.get(0);
        }
        problem.evaluate(currentSolution);
        bestSolution = currentSolution.clone();
        initialCost = currentSolution.getObjective(0);
        currentIteration = 0;
    }

    @Override
    public void step() {
        currentIteration++;
        currentMinimumCost = currentSolution.getObjective(0);
        Solution<T> newSolution = newSolution();
        problem.evaluate(newSolution);
        if (dominance.compare(newSolution, bestSolution) < 0) {
            bestSolution = newSolution.clone();
        }
        if (dominance.compare(newSolution, currentSolution) < 0 || changeState(newSolution)) {
            currentSolution = newSolution;
        }
    }

    @Override
    public Solutions<T> execute() {
        int nextPercentageReport = 10;
        while (currentIteration < maxIterations) {
            step();
            int percentage = Math.round((currentIteration * 100) / maxIterations);
            Double bestObj = bestSolution.getObjectives().get(0);
            if (percentage == nextPercentageReport) {
                LOGGER.info(percentage + "% performed ..." + " -- Best fitness: " + bestObj);
                nextPercentageReport += 10;
            }
            if (bestObj <= targetObj) {
                LOGGER.info("Optimal solution found in " + currentIteration + " iterations.");
                break;
            }
        }
        Solutions<T> solutions = problem.newRandomSetOfSolutions(1);
        solutions.set(0, bestSolution);
        return solutions;
    }

    /**
     * Computes probability of changing to new solution. It considers ONLY one
     * objective for energy.
     *
     * @param newSolution possible next state
     * @return true if probability gives chance to change state, false otherwise
     */
    private boolean changeState(Solution<T> newSolution) {
        // Higher cost means new energy to be higher than old energy
        double energyDiff;
        energyDiff = newSolution.getObjective(0) - currentSolution.getObjective(0);

        // Compute probability. Must be between 0 and 1.
        double temp = k * Math.abs((currentMinimumCost - initialCost) / currentIteration);
        double prob = Math.exp(-energyDiff / temp);

        // nextDouble returns the next pseudorandom, uniformly distributed double value between 0.0 and 1.0
        return RandomGenerator.nextDouble() <= prob;
    }

    /**
     * Returns a new solution when just of the variables has changed
     *
     * @return The new solution.
     */
    private Solution<T> newSolution() {
        // Generate a brand new solution
        ArrayList<T> variables = problem.newRandomSetOfSolutions(1).get(0).getVariables();
        // Randomly choose one variable
        int i = RandomGenerator.nextInt(variables.size());
        // Clone current solution and introduce change.
        Solution newSolution = currentSolution.clone();
        newSolution.getVariable(i).setValue(variables.get(i).getValue());
        return newSolution;
    }

    public static void main(String[] args) {
        JecoLogger.setup(Level.INFO);
        // First create the problem
        Rastringin problem = new Rastringin(4);
        // Second create the algorithm
        SimulatedAnnealing<Variable<Double>> algorithm = new SimulatedAnnealing<>(problem, 100000);
        algorithm.initialize();
        Solutions<Variable<Double>> solutions = algorithm.execute();
        for (Solution<Variable<Double>> solution : solutions) {
            LOGGER.log(Level.INFO, "Fitness = " + solution.getObjectives().get(0));
        }
    }


}
