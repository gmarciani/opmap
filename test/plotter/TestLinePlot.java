package plotter;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import commons.Plotter;
import commons.Randomizer;

public class TestLinePlot {
	
	@Rule public TestName name = new TestName();
	
	@Before
	public void testInfo() {
		System.out.println("\n/********************************************************************************");
		System.out.println(" * TEST: " + this.getClass().getSimpleName() + " " + name.getMethodName());
		System.out.println(" ********************************************************************************/\n");
	}

	@Test 
	public void randomDisplay() throws InterruptedException {		
		JFreeChart plot = Plotter.create("Sample Random Line Plot", "Domain", "Range", sampleDataset());
		
		Plotter.display(plot);
		
		TimeUnit.SECONDS.sleep(5);		
	}
	
	@Test 
	public void randomSave() throws IOException {		
		JFreeChart plot = Plotter.create("Sample Random Line Plot", "Domain", "Range", sampleDataset());
		
		Plotter.save(plot, "./test/plotter/svg/sample-random.svg");		
	}
	
	private static XYDataset sampleDataset() {		
		Random rnd = new Random();		
        
        XYSeries series1 = new XYSeries("First");
        XYSeries series2 = new XYSeries("Second");
        XYSeries series3 = new XYSeries("Third");
        
        for (int input = 100; input <= 1000; input+=100) {
        	double value = Randomizer.rndDouble(rnd, 100, 1000000);
        	series1.add(input, value);
        	series2.add(input, value * 10);
        	series3.add(input, value * 100);
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series1);
        dataset.addSeries(series2);
        dataset.addSeries(series3);
                
        return dataset;        
    }

}