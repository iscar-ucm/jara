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
package jeco.core.benchmarks.dtlz;

import java.util.ArrayList;

import jeco.core.problem.Solution;
import jeco.core.problem.Variable;

/**
 * DTLZ4 problem
 * 
 * This class represents the DTLZ4 problem. It is a subclass of the DTLZ class.
 * The DTLZ4 problem has the following properties:
 * - Number of variables: 12
 * - Number of objectives: 3
 * - Bounds for variables: [0, 1]
 * - Type of variables: real
 * - Pareto front: convex
 * - Pareto set: convex
 * 
 * The DTLZ4 problem is defined as follows:
 * - Minimize f1, f2, f3
 * - f1 = 1 + g
 * - f2 = 1 + g
 * - f3 = 1 + g
 * - g = sum_{i=numberOfVariables-k+1}^{numberOfVariables} (x_i - 0.5)^2
 * - k = numberOfVariables - numberOfObjectives + 1
 * - x_i in [0, 1]
 * - i = 1, 2, ..., numberOfVariables
 * 
 */
public class DTLZ4 extends DTLZ {

    /**
     * Constructor
     * @param numberOfVariables Number of variables
     */
    public DTLZ4(Integer numberOfVariables) {
        super(numberOfVariables);
        for (int i = 0; i < numberOfVariables; i++) {
            lowerBound[i] = 0.0;
            upperBound[i] = 1.0;
        }
    }

    /**
     * Constructor
     */
    public DTLZ4() {
        this(12);
    }

    @Override
    public void evaluate(Solution<Variable<Double>> solution) {
        ArrayList<Variable<Double>> variables = solution.getVariables();

        double[] x = new double[numberOfVariables];
        double[] f = new double[numberOfObjectives];
        double alpha = 100.0;
        int k = numberOfVariables - numberOfObjectives + 1;

        for (int i = 0; i < numberOfVariables; i++) {
            x[i] = variables.get(i).getValue();
        }

        double g = 0.0;
        for (int i = numberOfVariables - k; i < numberOfVariables; i++) {
            g += (x[i] - 0.5) * (x[i] - 0.5);
        }

        for (int i = 0; i < numberOfObjectives; i++) {
            f[i] = 1.0 + g;
        }

        for (int i = 0; i < numberOfObjectives; i++) {
            for (int j = 0; j < numberOfObjectives - (i + 1); j++) {
                f[i] *= java.lang.Math.cos(java.lang.Math.pow(x[j], alpha) * (java.lang.Math.PI / 2.0));
            }
            if (i != 0) {
                int aux = numberOfObjectives - (i + 1);
                f[i] *= java.lang.Math.sin(java.lang.Math.pow(x[aux], alpha) * (java.lang.Math.PI / 2.0));
            } //if
        } // for

        for (int i = 0; i < numberOfObjectives; i++) {
            solution.getObjectives().set(i, f[i]);
        }
    }
    
    @Override
    public DTLZ4 clone() {
    	DTLZ4 clone = new DTLZ4(this.numberOfVariables);
    	for(int i=0; i<numberOfVariables; ++i) {
    		clone.lowerBound[i] = lowerBound[i];
    		clone.upperBound[i] = upperBound[i];
    	}
    	return clone;
    }
}
