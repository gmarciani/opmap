package commons;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.graphics2d.svg.SVGGraphics2D;
import org.jfree.graphics2d.svg.SVGUtils;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RefineryUtilities;

public final class Plotter {
	
	private static final int WIDTH = 500;
	private static final int HEIGHT = 300;
	
	private static final Font FONT_XL = new Font("Latin Modern Roman", Font.BOLD, 12);
	private static final Font FONT_NLB = new Font("Latin Modern Roman", Font.BOLD, 11);
	private static final Font FONT_NL = new Font("Latin Modern Roman", Font.PLAIN, 11);
	private static final Font FONT_SL = new Font("Latin Modern Roman", Font.PLAIN, 8);

	private Plotter() {}
	
	public static JFreeChart create(final String title, final String xAxis, final String yAxis, final XYDataset dataset) {
		JFreeChart plot = ChartFactory.createXYLineChart(
	            title,
	            xAxis,
	            yAxis,
	            dataset,
	            PlotOrientation.VERTICAL,
	            true,
	            false,
	            false
	        ); 
		
		applyTheme(plot);
		
		return plot;
	}
	
	public static void display(JFreeChart plot) {		
		ApplicationFrame frame = new ApplicationFrame(plot.getTitle().getText());
		ChartPanel plotPanel = new ChartPanel(plot);
		
        plotPanel.setPreferredSize(new java.awt.Dimension(WIDTH, HEIGHT));
        
        frame.setContentPane(plotPanel);			
		frame.pack();
        RefineryUtilities.centerFrameOnScreen(frame);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);	        
	}
	
	public static void save(JFreeChart plot, String path) throws IOException {		
		save(plot, path, WIDTH, HEIGHT);		
	}
	
	public static void save(JFreeChart plot, String path, int width, int height) throws IOException {		
		SVGGraphics2D svg = new SVGGraphics2D(width, height);
		Rectangle area = new Rectangle(0, 0, width, height);
		File file = new File(path);
		
		plot.draw(svg, area);			
		SVGUtils.writeToSVG(file,  svg.getSVGElement());		
	}
	
	private static void applyTheme(JFreeChart plot) {    
		/********************************************************************************
		 * Title		
		 ********************************************************************************/
        plot.getTitle().setFont(FONT_XL);
        
        /********************************************************************************
		 * Legend		
		 ********************************************************************************/
        plot.getLegend().setItemFont(FONT_NL); 
        plot.getLegend().setPosition(RectangleEdge.RIGHT);
        plot.getLegend().setFrame(BlockBorder.NONE);
        
        /********************************************************************************
		 * Domain Axis		
		 ********************************************************************************/       
        plot.getXYPlot().getDomainAxis().setLabelFont(FONT_NLB);
        plot.getXYPlot().getDomainAxis().setTickLabelFont(FONT_SL);
        plot.getXYPlot().setDomainGridlinesVisible(false);
        plot.getXYPlot().setDomainGridlinePaint(Color.LIGHT_GRAY);
        
        /********************************************************************************
		 * Range Axis		
		 ********************************************************************************/
        plot.getXYPlot().getRangeAxis().setLabelFont(FONT_NLB);
        plot.getXYPlot().getRangeAxis().setTickLabelFont(FONT_SL);
        plot.getXYPlot().setRangeGridlinesVisible(false);
        plot.getXYPlot().setRangeGridlinePaint(Color.LIGHT_GRAY);
        
        /********************************************************************************
		 * Background		
		 ********************************************************************************/
        plot.setBackgroundPaint(Color.WHITE);
        plot.getXYPlot().setBackgroundPaint(Color.WHITE); 
	}

}
