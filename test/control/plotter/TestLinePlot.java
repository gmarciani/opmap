package control.plotter;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.junit.Test;

import control.plotter.Plotter;

public class TestLinePlot {

	@Test 
	public void createAndDisplay() throws InterruptedException {		
		JFreeChart plot = Plotter.create("Sample Line Plot", "Domain", "Range", sampleDataset());
		
		Plotter.display(plot);
		
		TimeUnit.SECONDS.sleep(5);		
	}
	
	@Test 
	public void createAndExport() throws IOException {		
		JFreeChart plot = Plotter.create("Sample Line Plot", "Domain", "Range", sampleDataset());
		
		Plotter.save(plot, "./test/plot/svg/svg-sample.svg");		
	}
	
	private XYDataset sampleDataset() {		
		Random rnd = new Random();		
        
        XYSeries series1 = new XYSeries("First");
        XYSeries series2 = new XYSeries("Second");
        XYSeries series3 = new XYSeries("Third");
        
        for (int input = 100; input <= 1000; input+=100) {
        	double value = getRandom(rnd, 100, 1000000);
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
	
	private double getRandom(Random rnd, double min, double max) {
		return min + (max - min) * rnd.nextDouble();
	}

}
