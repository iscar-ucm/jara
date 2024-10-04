/*
 * Copyright (C) 2010 José Luis Risco Martín <jlrisco@ucm.es>
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
package jeco.core.operator.selection;

import java.util.Collections;
import java.util.Comparator;

import jeco.core.operator.comparator.SolutionDominance;
import jeco.core.problem.Solution;
import jeco.core.problem.Solutions;
import jeco.core.problem.Variable;
import jeco.core.util.random.RandomGenerator;

/**
 * TournamentSelect is a selection operator that selects the best individual
 * from a set of individuals. To do so, first randomly selects a set of individuals
 * and then selects the best individual from this set.
 * 
 * @param <T> Variable type
 */
public class TournamentSelect<T extends Variable<?>> extends SelectionOperator<T> {

    public static final int DEFAULT_TOURNAMENT_SIZE = 2;
    protected Comparator<Solution<T>> comparator;
    protected int tournamentSize;

    public TournamentSelect(int tournamentSize, Comparator<Solution<T>> comparator) {
        this.tournamentSize = tournamentSize;
        this.comparator = comparator;
    } // TournamentSelect

    public TournamentSelect(Comparator<Solution<T>> comparator) {
        this(TournamentSelect.DEFAULT_TOURNAMENT_SIZE, comparator);
    } // TournamentSelect

    public TournamentSelect() {
        this(TournamentSelect.DEFAULT_TOURNAMENT_SIZE, new SolutionDominance<T>());
    } // TournamentSelect

    public Solutions<T> execute(Solutions<T> solutions) {
        Solutions<T> result = new Solutions<T>();
        Solutions<T> tournamentSet = new Solutions<T>();
        for (int i = 0; i < tournamentSize; ++i) {
            tournamentSet.add(solutions.get(RandomGenerator.nextInteger(solutions.size())));
        }
        Collections.sort(tournamentSet, comparator);
        result.add(tournamentSet.get(0));
        return result;
    } // execute
} // TournamentSelect

