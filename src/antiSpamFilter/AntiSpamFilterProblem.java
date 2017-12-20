package antiSpamFilter;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.problem.impl.AbstractDoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;

public class AntiSpamFilterProblem extends AbstractDoubleProblem {

	private GUI_Worker g_Worker;
	
	public AntiSpamFilterProblem(GUI_Worker g_Worker) {
		// 10 variables (anti-spam filter rules) by default
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

	public void evaluate(DoubleSolution solution) {
		double aux, xi, xj;
		double[] fx = new double[getNumberOfObjectives()];
		double[] x = new double[getNumberOfVariables()];
		for (int i = 0; i < solution.getNumberOfVariables(); i++) {
			x[i] = solution.getVariableValue(i);
		}
		g_Worker.updateMap(x);
		// FN
		fx[0] = g_Worker.calculateFN();

		// FP
		fx[1] = g_Worker.calculateFP();

		solution.setObjective(0, fx[0]);
		solution.setObjective(1, fx[1]);
	}
}
