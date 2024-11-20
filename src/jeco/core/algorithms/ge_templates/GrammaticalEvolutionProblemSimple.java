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
package jeco.core.algorithms.ge_templates;

import java.util.logging.Logger;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import jeco.core.algorithms.GrammaticalEvolutionSimple;
import jeco.core.operator.crossover.SinglePointCrossover;
import jeco.core.problem.GrammaticalEvolutionAbstractProblem;
import jeco.core.problem.GrammaticalEvolutionPhenotype;
import jeco.core.problem.Solution;
import jeco.core.problem.Solutions;
import jeco.core.problem.Variable;
import jeco.core.util.logger.JecoLogger;

import java.io.File;

/**
 * Example Please note that using the Script Engine is too slow. We recommend
 * using an external evaluation library like JEval (sourceforge).
 *
 * @author José Luis Risco Martín
 *
 */
public class GrammaticalEvolutionProblemSimple extends GrammaticalEvolutionAbstractProblem {

  /**
   * Logger
   */
  private static final Logger logger = Logger.getLogger(GrammaticalEvolutionProblemSimple.class.getName());
  /**
   * Evaluator
   */
  protected ScriptEngine evaluator = null;
  /**
   * Function to be approximated
   */
  protected double[] func = {0, 4, 30, 120, 340, 780, 1554}; //x^4+x^3+x^2+x

  /**
   * Constructor
   *
   * @param pathToBnf Path to the BNF file
   */
  public GrammaticalEvolutionProblemSimple(String pathToBnf) {
    super(pathToBnf, 1);
    ScriptEngineManager mgr = new ScriptEngineManager();
    evaluator = mgr.getEngineByName("JavaScript");
  }

  @Override
  public void evaluate(Solution<Variable<Integer>> solution, GrammaticalEvolutionPhenotype phenotype) {
    String originalFunction = phenotype.toString();
    double error, totError = 0;
    for (int i = 0; i < func.length; ++i) {
      String currentFunction = originalFunction.replaceAll("X", String.valueOf(i));
      double funcI;
      try {
        String aux = evaluator.eval(currentFunction).toString();
        if (aux.equals("NaN")) {
          funcI = Double.POSITIVE_INFINITY;
        } else {
          funcI = Double.valueOf(aux);
        }
      } catch (NumberFormatException e) {
        logger.severe(e.getLocalizedMessage());
        funcI = Double.POSITIVE_INFINITY;
      } catch (ScriptException e) {
        logger.severe(e.getLocalizedMessage());
        funcI = Double.POSITIVE_INFINITY;
      }
      error = Math.pow(funcI - func[i], 2);
      totError += error;
    }
    solution.getObjectives().set(0, totError);
  }

  @Override
  public GrammaticalEvolutionProblemSimple clone() {
    GrammaticalEvolutionProblemSimple clone = new GrammaticalEvolutionProblemSimple(super.pathToBnf);
    return clone;
  }

    public static void main(String[] args) {
        JecoLogger.setup();
        // First create the problem
        GrammaticalEvolutionProblemSimple problem = new GrammaticalEvolutionProblemSimple("lib" + File.separator + "grammar_example.bnf");
        // Second create the algorithm
        GrammaticalEvolutionSimple algorithm = new GrammaticalEvolutionSimple(problem,100,1000,1.0 / problem.getNumberOfVariables(),SinglePointCrossover.DEFAULT_PROBABILITY);
        // Run
        algorithm.initialize();
        Solutions<Variable<Integer>> solutions = algorithm.execute();
        for (Solution<Variable<Integer>> solution : solutions) {
            logger.info("Fitness = (" + solution.getObjectives().get(0) + ")");
            logger.info("Phenotype = (" + problem.generatePhenotype(solution).toString() + ")");
        }
  }
}
