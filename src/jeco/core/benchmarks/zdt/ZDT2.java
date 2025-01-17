/*
 * Copyright (C) 2010-2016 José Luis Risco Martín <jlrisco@ucm.es>
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
 *
 * Contributors:
 *  - José Luis Risco Martín
 */
package jeco.core.benchmarks.zdt;

import java.util.ArrayList;

import jeco.core.operator.comparator.SolutionDominance;
import jeco.core.problem.Solution;
import jeco.core.problem.Solutions;
import jeco.core.problem.Variable;

/**
 * ZDT2 benchmark problem
 * 
 * ZDT2 is a multi-objective optimization problem
 * 
 * f1(x) = x1
 * f2(x) = g(x) * h(f1(x), g(x))
 * g(x) = 1 + 9 * (sum(xj) / (n-1))
 * h(f1, g) = 1 - (f1 / g)^2
 * 
 * xj in [0, 1]
 * 
 * Pareto optimal front: convex
 */
public class ZDT2 extends ZDT {

    /**
     * Constructor
     * @param numberOfVariables Number of variables
     */
    public ZDT2(Integer numberOfVariables) {
        super(numberOfVariables);
        for (int i = 0; i < numberOfVariables; i++) {
            lowerBound[i] = 0.0;
            upperBound[i] = 1.0;
        }
    } // ZDT1

    /**
     * Constructor
     */
    public ZDT2() {
        this(30);
    }

    @Override
    public void evaluate(Solution<Variable<Double>> solution) {
        ArrayList<Variable<Double>> variables = solution.getVariables();
        double f1 = variables.get(0).getValue();
        double g = 0;
        for (int j = 1; j < numberOfVariables; ++j) {
            g += variables.get(j).getValue();
        }
        g *= 9.0;
        g /= numberOfVariables - 1;
        g += 1.0;
        double h = 1 - (f1 / g) * (f1 / g);
        solution.getObjectives().set(0, f1);
        solution.getObjectives().set(1, g * h);
    }

    /**
     * Compute the Pareto optimal front
     * @param n Number of points
     * @return Pareto optimal front
     */
    public Solutions<Variable<Double>> computeParetoOptimalFront(int n) {
        Solutions<Variable<Double>> result = new Solutions<Variable<Double>>();

        double temp;
        for (int i = 0; i < n; ++i) {
            Solution<Variable<Double>> sol = new Solution<Variable<Double>>(numberOfObjectives);
            temp = 0.0 + (1.0 * i) / (n - 1);
            sol.getVariables().add(new Variable<Double>(temp));
            for (int j = 1; j < numberOfVariables; ++j) {
                sol.getVariables().add(new Variable<Double>(0.0));
            }

            evaluate(sol);
            result.add(sol);
        }

        result.reduceToNonDominated(new SolutionDominance<Variable<Double>>());
        return result;
    }

    @Override
    public ZDT2 clone() {
    	ZDT2 clone = new ZDT2(this.numberOfVariables);
    	for(int i=0; i<numberOfVariables; ++i) {
    		clone.lowerBound[i] = lowerBound[i];
    		clone.upperBound[i] = upperBound[i];
    	}
    	return clone;
    }
}
