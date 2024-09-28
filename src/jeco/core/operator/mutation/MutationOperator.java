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

/**
 * Mutation operator. It changes the value of a variable with a given probability.
 * 
 * @param <T> Variable type.
 */
public abstract class MutationOperator<T extends Variable<?>> {
	protected double probability;
	
	public MutationOperator(double probability) {
		this.probability = probability;
	}
	
	public void setProbability(double probability) {
		this.probability = probability;
	}

	abstract public Solution<T> execute(Solution<T> solution);
}
