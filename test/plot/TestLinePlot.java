package plot;


import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.junit.Test;

import view.LinePlot;

public class TestLinePlot {

	@Test public void createAndDisplay() throws InterruptedException {		
		
		LinePlot plot = new LinePlot("Sample Line Plot", "This is the sample plot subtitle", createDataset());
		
		plot.display();
		
		TimeUnit.SECONDS.sleep(5);
		
	}
	
	@Test public void createAndExport() {	
		
		LinePlot plot = new LinePlot("Sample Line Plot", "This is the sample plot subtitle", createDataset());
		
		try {
			plot.save("./test/plot/svg/svg-sample.svg", 500, 300);
		} catch (IOException exc) {
			fail("Plot SVG export failure: " + exc.getMessage());
		}	
		
	}
	
	private XYDataset createDataset() {
		
		Random rnd = new Random();		
        
        final XYSeries series1 = new XYSeries("First");
        final XYSeries series2 = new XYSeries("Second");
        final XYSeries series3 = new XYSeries("Third");
        
        for (int input = 100; input <= 1000; input+=100) {
        	double value = getRandom(rnd, 100, 1000000);
        	series1.add(input, value);
        	series2.add(input, value * 10);
        	series3.add(input, value * 100);
        }

        final XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series1);
        dataset.addSeries(series2);
        dataset.addSeries(series3);
                
        return dataset;        
        
    }
	
	private double getRandom(Random rnd, double min, double max) {
		return min + (max - min) * rnd.nextDouble();
	}

}
