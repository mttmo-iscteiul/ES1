package antiSpamFilter;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.problem.impl.AbstractDoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;

@SuppressWarnings("serial")
public class AntiSpamFilterProblem extends AbstractDoubleProblem {

	private GUI_Worker g_Worker;

	public AntiSpamFilterProblem(GUI_Worker g_Worker) {
		this(g_Worker.getRulesSize(), g_Worker);
		this.g_Worker = g_Worker;
	}

	public AntiSpamFilterProblem(Integer numberOfVariables, GUI_Worker g_Worker) {
		setNumberOfVariables(numberOfVariables);
		setNumberOfObjectives(2);
		setName("AntiSpamFilterProblem");

		List<Double> lowerLimit = new ArrayList<>(getNumberOfVariables());
		List<Double> upperLimit = new ArrayList<>(getNumberOfVariables());

		for (int i = 0; i < getNumberOfVariables(); i++) {
			lowerLimit.add(-5.0);
			upperLimit.add(5.0);
		}

		setLowerLimit(lowerLimit);
		setUpperLimit(upperLimit);
	}

	/**
	 * Method parameterized to evaluate the FN and FP in the current Solution.
	 */
	public void evaluate(DoubleSolution solution) {
		double[] fx = new double[getNumberOfObjectives()];
		double[] x = new double[getNumberOfVariables()];
		for (int i = 0; i < solution.getNumberOfVariables(); i++) {
			x[i] = solution.getVariableValue(i);
		}
		g_Worker.updateMapByVector(x, 0);
		// FN
		fx[0] = g_Worker.calculateFN(0);

		// FP
		fx[1] = g_Worker.calculateFP(0);

		solution.setObjective(0, fx[0]);
		solution.setObjective(1, fx[1]);
	}
}
