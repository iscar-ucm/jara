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
package jeco.core.operator.mutation;

import jeco.core.problem.Solution;
import jeco.core.problem.Variable;
import jeco.core.util.random.RandomGenerator;

/**
 * Boolean mutation operator. It changes the value of a boolean variable with a given probability.
 * 
 * @param <T> Variable type.
 */
public class BooleanMutation<T extends Variable<Boolean>> extends MutationOperator<T> {
	/**
	 * Constructor
	 * Creates a new IntegerFlipMutation mutation operator instance
	 * 
	 * @param probability The probability of mutation
	 */
	public BooleanMutation(double probability) {
		super(probability);
	} // IntegerFlipMutation

	@Override
	public Solution<T> execute(Solution<T> solution) {
		for (int i = 0; i < solution.getVariables().size(); i++) {
			if (RandomGenerator.nextDouble() < probability) {
				solution.getVariables().get(i).setValue(!solution.getVariables().get(i).getValue());
			}
		}
		return solution;
	} // execute
}

