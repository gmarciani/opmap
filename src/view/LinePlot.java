package view;

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
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYDataset;
import org.jfree.graphics2d.svg.SVGGraphics2D;
import org.jfree.graphics2d.svg.SVGUtils;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.RefineryUtilities;
import org.jfree.ui.VerticalAlignment;

public class LinePlot {

	private String title;
	private String subtitle;
	private XYDataset dataset;
	private JFreeChart plot;
	
	public LinePlot(String title, String subtitle, XYDataset dataset) {
		
		this.title = title;
		this.subtitle = subtitle;
		this.dataset = dataset;
        this.createPlot();
        this.applyTheme();
        
	}

	public LinePlot(String title, XYDataset dataset) {
		
		this.title = title;
		this.subtitle = null;
		this.dataset = dataset;
        this.createPlot();
        this.applyTheme();
        
	}
	
	private void createPlot() {			
		
		this.plot = ChartFactory.createXYLineChart(
            this.title,
            "Input",
            "Time (ms)",
            this.dataset,
            PlotOrientation.VERTICAL,
            true,
            false,
            false
        );  
        	        
	}
	
	private void applyTheme() {
		
		final Font xlFont = new Font("Latin Modern Roman", Font.BOLD, 12);
		final Font nlbFont = new Font("Latin Modern Roman", Font.BOLD, 11);
        final Font nlFont = new Font("Latin Modern Roman", Font.PLAIN, 11);
        final Font slFont = new Font("Latin Modern Roman", Font.PLAIN, 8);                
        
        // Title
        this.plot.getTitle().setFont(xlFont);
        
        // Subtitle
        if (this.subtitle != null) 		
			this.plot.addSubtitle(0, new TextTitle(this.subtitle, nlFont, Color.BLACK, RectangleEdge.TOP, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, RectangleInsets.ZERO_INSETS));
        
        // Legend
        this.plot.getLegend().setItemFont(nlFont); 
        this.plot.getLegend().setPosition(RectangleEdge.RIGHT);
        this.plot.getLegend().setFrame(BlockBorder.NONE);
        
        // Domain Axis        
        this.plot.getXYPlot().getDomainAxis().setLabelFont(nlbFont);
        this.plot.getXYPlot().getDomainAxis().setTickLabelFont(slFont);
        this.plot.getXYPlot().setDomainGridlinesVisible(false);
        this.plot.getXYPlot().setDomainGridlinePaint(Color.LIGHT_GRAY);
        
        // Range Axis
        this.plot.getXYPlot().getRangeAxis().setLabelFont(nlbFont);
        this.plot.getXYPlot().getRangeAxis().setTickLabelFont(slFont);
        this.plot.getXYPlot().setRangeGridlinesVisible(false);
        this.plot.getXYPlot().setRangeGridlinePaint(Color.LIGHT_GRAY);
        
        // Background
        this.plot.setBackgroundPaint(Color.WHITE);
        this.plot.getXYPlot().setBackgroundPaint(Color.WHITE);  
        
        
	}
	
	public void display() {			
		
		ApplicationFrame frame = new ApplicationFrame(this.title);
		ChartPanel plotPanel = new ChartPanel(this.plot);
		
        plotPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        
        frame.setContentPane(plotPanel);			
		frame.pack();
        RefineryUtilities.centerFrameOnScreen(frame);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);	  
        
	}
	
	public void save(String path, int width, int height) throws IOException {
		
		SVGGraphics2D svg = new SVGGraphics2D(width, height);
		Rectangle area = new Rectangle(0, 0, width, height);
		File file = new File(path);
		
		this.plot.draw(svg, area);			
		SVGUtils.writeToSVG(file,  svg.getSVGElement());
		
	}

}
