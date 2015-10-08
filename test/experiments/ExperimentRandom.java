package experiments;

import static org.junit.Assert.*;

import java.io.IOException;

import org.jfree.chart.JFreeChart;
import org.jfree.data.function.Function2D;
import org.jfree.data.function.NormalDistributionFunction2D;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import control.plotter.Plotter;

public class ExperimentRandom {
	
	@Rule 
	public TestName name = new TestName();
	
	@Before
	public void testInfo() {
		System.out.println("\n/********************************************************************************");
		System.out.println(" * TEST: " + this.getClass().getSimpleName() + " " + name.getMethodName());
		System.out.println(" ********************************************************************************/\n");
	}
	
	@Test
	public void gaussian() {
		double min = Experiments.RandomGeneration.WRT_Variance.MIN;
		double max = Experiments.RandomGeneration.WRT_Variance.MAX;
		double vars[] = Experiments.RandomGeneration.WRT_Variance.VARS;

		XYSeriesCollection dataset = new XYSeriesCollection();
		
		for (double var : vars) {
			Function2D gaussian = new NormalDistributionFunction2D((max + min) / 2, var);
			XYSeries xy = DatasetUtilities.sampleFunction2DToSeries(gaussian, min, max, 1000, "Var:" + var);
			dataset.addSeries(xy);
		}
		
		String title = String.format("Gaussian Generation");
		String subtitle = String.format("[%.2f,%.2f]", min, max);
		JFreeChart plot = Plotter.createLine(title, null, "Values", "Density", dataset);		
		try {
			Plotter.save(plot, String.format("%s/%s - %s.svg", Experiments.RESULTS_DIR, title, subtitle));
		} catch (IOException exc) {
			fail("Plot SVG export failure: " + exc.getMessage());
		}		
	}
}
