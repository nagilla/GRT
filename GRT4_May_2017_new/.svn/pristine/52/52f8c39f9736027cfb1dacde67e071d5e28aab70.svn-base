package com.avaya.grt.web.util;

//=================
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.AttributedString;
import java.text.DecimalFormat;
import java.util.List;

import javax.imageio.ImageIO;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlotState;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.RingPlot;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.text.TextUtilities;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
/* This class will return the chart to the output stream */
/* This class will help the servlet to load chart data */
import org.jfree.ui.TextAnchor;
import org.jfree.ui.VerticalAlignment;
import org.jfree.xml.util.Base64;

import com.grt.dto.RegistrationCount;
/* This class will hold the chart object */

public class GRTGraphUtil {
	
	private static final String ibCompleted  = "IB Completed";
	private static final String tobCompleted = "TOB Completed";
	private static final String eqrCompleted = "EQR Completed";
	private static final String ibSaved  = "IB Saved";
	private static final String tobSaved = "TOB Saved";
	private static final String eqrSaved = "EQR Saved";
	private static final String tobResubmit = "TOB To Re-Submit";
	private static final String tobInitiateStepB = "TOB To Initiate Step B";
	
	private static final String notInitiated  = "Not Initiated";
	private static final String inProcessIn  = "In Process";
	private static final String awaitingInfo = "Awaiting Info";
	private static final String saved  = "Saved";
	
	public static String createBarStackedGraphForCompletedRegistrations(List<RegistrationCount> regCountList,String title)
	{
		final CategoryDataset dataset = createDatasetForCompleteRegistrations(regCountList);
		return createBase64EncodedImageString(dataset, title);
	}
	
	public static String createBarStackedGraphForSavedRegistrations(List<RegistrationCount> regCountList,String title)
	{
		final CategoryDataset dataset = createDatasetForSavedRegistrations(regCountList);
		return createBase64EncodedImageString(dataset, title);
	}

	public static String createBarStackedGraphForTOBRegistrations(List<RegistrationCount> registrationsNotCompletedList,String title)
	{
		final CategoryDataset dataset = createDatasetForTOBRegistrations(registrationsNotCompletedList);
		return createBase64EncodedImageString(dataset, title);
	}
	
	//GRT 4.0 Chnages
	public static String createPlotRingStackedGraphForTOBRegistrations(List<RegistrationCount> registrationsNotCompletedList,String title)
	{
		
		final  DefaultPieDataset defaultpiedataset = createPlotRingDatasetForTOBRegistrations(registrationsNotCompletedList);
		return createBase64PlotRingEncodedImageString(defaultpiedataset, title);
	}
	

	private static CategoryDataset createDatasetForCompleteRegistrations(List<RegistrationCount> regCountList) {
		DefaultCategoryDataset result = new DefaultCategoryDataset();

		for(RegistrationCount regCount : regCountList) {	    	
			result.addValue(Integer.parseInt(regCount.getIbCompleted()) ,ibCompleted , regCount.getCreatedDate());
			result.addValue(Integer.parseInt(regCount.getTobCompleted()), tobCompleted , regCount.getCreatedDate());
			result.addValue(Integer.parseInt(regCount.getEqrCompleted()), eqrCompleted , regCount.getCreatedDate());
		}
		return result;
	}
	
	private static CategoryDataset createDatasetForSavedRegistrations(List<RegistrationCount> regCountList) {
		DefaultCategoryDataset result = new DefaultCategoryDataset();

		for(RegistrationCount regCount : regCountList) {	    	
			result.addValue(Integer.parseInt(regCount.getIbSaved()) ,ibSaved , regCount.getCreatedDate());
			result.addValue(Integer.parseInt(regCount.getTobSaved()), tobSaved , regCount.getCreatedDate());
			result.addValue(Integer.parseInt(regCount.getEqrSaved()), eqrSaved , regCount.getCreatedDate());
		}
		return result;
	}

	private static CategoryDataset createDatasetForTOBRegistrations(List<RegistrationCount> regCountList) {
		DefaultCategoryDataset result = new DefaultCategoryDataset();
		
		
		for(RegistrationCount regCount : regCountList) {	    	
			result.addValue(Integer.parseInt(regCount.getTobReSubmit()) ,tobResubmit , regCount.getCreatedDate());
			result.addValue(Integer.parseInt(regCount.getTobInitiate()),tobInitiateStepB , regCount.getCreatedDate());
		}
		return result;
	}
	//GRT 4.0 Chnages
	private static DefaultPieDataset createPlotRingDatasetForTOBRegistrations(List<RegistrationCount> regCountList) {
		DefaultPieDataset result = new DefaultPieDataset();   
      
		for(RegistrationCount regCount : regCountList) {	    	
			
			result.setValue(notInitiated,new Integer(regCount.getNotInitiated()));
			result.setValue(inProcessIn, new Integer(regCount.getInProcess()));
			result.setValue(awaitingInfo, new Integer(regCount.getAwaitingInfo()));
			result.setValue(saved, new Integer(regCount.getSaved()));
			System.out.println(regCount.getTobInitiate()+" : "+regCount.getTobReSubmit()+" : "+regCount.getTobCompleted()+" : "+regCount.getTobSaved());
		}
		
		System.out.println("Final Result:--------"+result);
		
		return result;
	}

	@SuppressWarnings("deprecation")
	private static JFreeChart createChart(final CategoryDataset dataset,String title) {

		
		final JFreeChart chart = ChartFactory.createStackedBarChart(
				title,  					 // chart title
				"Months",   				 // domain axis label
				"Number of registrations",   // range axis label
				dataset,                     // data
				PlotOrientation.VERTICAL,    // the plot orientation
				true,                        // legend
				true,                        // tooltips
				false                        // urls
				);

		CategoryPlot plot = (CategoryPlot) chart.getPlot();
		StackedBarRenderer renderer = (StackedBarRenderer) plot.getRenderer();
	    DecimalFormat decimalformat1 = new DecimalFormat("##");
	    renderer.setItemLabelGenerator(new StandardCategoryItemLabelGenerator("{2}", decimalformat1));
	    renderer.setItemLabelsVisible(true);
	    renderer.setSeriesVisible(true);
		
		return chart;
	}
	
	//GRT 4.0 Chnages
	@SuppressWarnings("deprecation")
	private static JFreeChart createPlotRingChart(final DefaultPieDataset dataset,String title) {

		 CustomRingPlot ringplot = new CustomRingPlot(dataset);
		final JFreeChart ringChartObject = new JFreeChart("", JFreeChart.DEFAULT_TITLE_FONT, ringplot, true);
		
		ringChartObject.getLegend().setFrame(BlockBorder.NONE);
        ringChartObject.getLegend().setPosition(RectangleEdge.RIGHT); 
        ringChartObject.getLegend().setVerticalAlignment(VerticalAlignment.CENTER);
        ringChartObject.getLegend().setItemFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));        
        
        
        /*
        ringChartObject.setBackgroundPaint(java.awt.Color.white);	        
        ringChartObject.setBorderVisible(false);
        ringChartObject.setBorderPaint(java.awt.Color.white);
        */	        
        ringChartObject.setPadding(new RectangleInsets(4, 8, 2, 2));
        ringChartObject.setBackgroundPaint(null);	        
        ringChartObject.setBorderVisible(false);
        
        ringplot.setBackgroundPaint(null);
        ringplot.setOutlineVisible(false);
        ringplot.setNoDataMessage("No data available");   
        //ringplot.setLabelGenerator(new CustomLabelGenerator(dataset));
        ringplot.setLabelGenerator(null);
        ringplot.setLegendLabelGenerator(new CustomLegendLabelGenerator(dataset));
		
		return ringChartObject;
	}
	
	private static String createBase64EncodedImageString(CategoryDataset dataset, String title) {
		final JFreeChart chart = createChart(dataset,title);
		
		BufferedImage objImg = chart.createBufferedImage(400, 300);
		ByteArrayOutputStream boutStream = new ByteArrayOutputStream();

		//Convert it into bytes array
		try {
			ImageIO.write(objImg, "PNG", boutStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] bytes = boutStream.toByteArray();

		//Encode the bytes array
		String base64bytes = new String( Base64.encode(bytes) );
		String img = "data:image/png;base64," + base64bytes;

		return img;
	}
	
	//GRT 4.0 Chnages
	private static String createBase64PlotRingEncodedImageString(DefaultPieDataset dataset, String title) {
		final JFreeChart chart = createPlotRingChart(dataset,title);
		
		BufferedImage objImg = chart.createBufferedImage(400, 300);
		ByteArrayOutputStream boutStream = new ByteArrayOutputStream();

		//Convert it into bytes array
		try {
			ImageIO.write(objImg, "PNG", boutStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] bytes = boutStream.toByteArray();

		//Encode the bytes array
		String base64bytes = new String( Base64.encode(bytes) );
		String img = "data:image/png;base64," + base64bytes;

		return img;
	}
	
	//GRT 4.0 Chnages
	static class CustomLabelGenerator implements PieSectionLabelGenerator {
		private final PieDataset dataset;

		  public CustomLabelGenerator(PieDataset dataset) {
			  this.dataset = dataset;
		  }

		  public String generateSectionLabel(PieDataset inDataset, Comparable key) {
			  inDataset = this.dataset;
			  return ((Number) inDataset.getValue(key)).toString();
		  }
		  
		  public AttributedString generateAttributedSectionLabel(
	                PieDataset dataset, Comparable key) {
	        return null;
	      }

    }
	//GRT 4.0 Chnages
	static class CustomLegendLabelGenerator implements PieSectionLabelGenerator {
		private final PieDataset dataset;

		  public CustomLegendLabelGenerator(PieDataset dataset) {
			  this.dataset = dataset;
		  }

		  public String generateSectionLabel(PieDataset inDataset, Comparable key) {
			  inDataset = this.dataset;
			  return key + " = " + inDataset.getValue(key);
		  }
		  
		  public AttributedString generateAttributedSectionLabel(
	                PieDataset dataset, Comparable key) {
	        return null;
	      }

    }
	
	
	static class CustomRingPlot extends RingPlot {
		/** The font. */
        private Font centerTextFont; 
        
        /** The text color. */
        private Color centerTextColor;
        
		public CustomRingPlot(PieDataset dataset) {
            super(dataset);
            this.centerTextFont = new Font(Font.SERIF, Font.BOLD, 18);
            this.centerTextColor = Color.GRAY;
        }
		
		@Override
        protected void drawItem(Graphics2D g2, int section, 
                Rectangle2D dataArea, PiePlotState state, int currentPass) {
            super.drawItem(g2, section, dataArea, state, currentPass);
            
            int number = 0;
            
            for (int c = 0; c < this.getDataset().getItemCount(); c++) {
            	int temp = ((Number) this.getDataset().getValue(c)).intValue();
            	number += temp;
            }
            
            g2.setFont(this.centerTextFont);
            g2.setPaint(this.centerTextColor);
            TextUtilities.drawAlignedString(Integer.toString(number), g2, 
                    (float) dataArea.getCenterX(), 
                    (float) dataArea.getCenterY(),  
                    TextAnchor.CENTER);
            
        }
		
		
	}

}
