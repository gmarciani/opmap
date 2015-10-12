package experiments;

import static org.junit.Assert.*;

import java.io.IOException;

import org.apache.commons.math3.random.RandomDataGenerator;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import commons.GMath;
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
	public void freq() {
		double min = Experiments.RandomGeneration.WRT_Variance.MIN;
		double max = Experiments.RandomGeneration.WRT_Variance.MAX;
		double vars[] = Experiments.RandomGeneration.WRT_Variance.VARS;
		double intervals[] = new double[10];
		int frequencies[] = new int[10];
		double values[] = new double[1000];
		
		for (int i = 0; i < intervals.length; i++)
			intervals[i] = min + (max - min) * ((i + 1.0) / intervals.length);
		
		XYSeriesCollection dataset = new XYSeriesCollection();
		
		for (double var : vars) {
			RandomDataGenerator rnd = new RandomDataGenerator();
			XYSeries series = new XYSeries(String.format("VAR%.2f", var));
			
			for (int v = 0; v < values.length; v++)
				values[v] = GMath.randomNormal(rnd, min, max, var);
			
			for (int v = 0; v < values.length; v++) {
				for (int i = 0; i < intervals.length; i++) {
					double lb = (i==0)?min:intervals[i - 1];
					double ub = intervals[i];
					if (values[v] > lb  && values[v] <= ub) {
						frequencies[i] += 1;
						break;
					}
				}						
			}
			
			for (int i = 0; i < intervals.length; i++)			
				series.add(intervals[i], frequencies[i]);
			
			dataset.addSeries(series);
		}		
		
		String title = String.format("Frequency Generation");
		String subtitle = String.format("[%.2f,%.2f]", min, max);
		JFreeChart plot = Plotter.createLine(null, null, "Values", "Frequency", dataset);		
		try {
			Plotter.save(plot, String.format("%s/%s - %s.svg", Experiments.RESULTS_DIR, title, subtitle));
		} catch (IOException exc) {
			fail("Plot SVG export failure: " + exc.getMessage());
		}
		
	}
}
