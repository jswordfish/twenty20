package com.twenty20.jsf.charts.barchart.model;

import java.util.ArrayList;
import java.util.List;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

import com.twenty20.jsf.charts.piechart.model.LabelValue;

public class BarChartBean {
	 private BarChartModel barModel = new BarChartModel();
	   // private HorizontalBarChartModel horizontalBarModel = new HorizontalBarChartModel();
	    
	    String title;
	    
	    String legendPosition;
	    
	    String xAxisLabel;
	    
	    String yAxisLabel;
	    
	    Boolean stacked = false;
	    
	    List<ChartSeriesData> chartSeriesDatas = new ArrayList<>();
	    
	    public BarChartBean() {
	    	
	    }
	    
	    public BarChartBean(Boolean stacked) {
	    	this.stacked = stacked;
	    }
	    
	    public BarChartBean(String title, String legendPosition, String xAxisLabel, String yAxisLabel) {
	    	this.title = title;
	    	this.legendPosition = legendPosition;
	    	this.xAxisLabel = xAxisLabel;
	    	this.yAxisLabel = yAxisLabel;
	    }
	    
	    public void init() {
	    	barModel.setTitle(title);
	    	barModel.setStacked(stacked);
	    	barModel.setLegendPosition(legendPosition);
	    	Axis xAxis = barModel.getAxis(AxisType.X);
	    	xAxis.setLabel(xAxisLabel);
	    	Axis yAxis = barModel.getAxis(AxisType.Y);
	    	yAxis.setLabel(yAxisLabel);
	    		for(ChartSeriesData data : chartSeriesDatas) {
	    			ChartSeries series = new ChartSeries();
	    			series.setLabel(data.getLabel());
	    				for(LabelValue labelValue : data.getLabelValues()) {
	    					series.set(labelValue.getLabel(), labelValue.getValue());
	    				}
	    			barModel.addSeries(series);
	    		}
	    	
	    }

		public BarChartModel getBarModel() {
			return barModel;
		}

		public void setBarModel(BarChartModel barModel) {
			this.barModel = barModel;
		}

		

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getLegendPosition() {
			return legendPosition;
		}

		public void setLegendPosition(String legendPosition) {
			this.legendPosition = legendPosition;
		}

		public String getxAxisLabel() {
			return xAxisLabel;
		}

		public void setxAxisLabel(String xAxisLabel) {
			this.xAxisLabel = xAxisLabel;
		}

		public String getyAxisLabel() {
			return yAxisLabel;
		}

		public void setyAxisLabel(String yAxisLabel) {
			this.yAxisLabel = yAxisLabel;
		}

		public List<ChartSeriesData> getChartSeriesDatas() {
			return chartSeriesDatas;
		}

		public void setChartSeriesDatas(List<ChartSeriesData> chartSeriesDatas) {
			this.chartSeriesDatas = chartSeriesDatas;
		}

		public Boolean getStacked() {
			return stacked;
		}

		public void setStacked(Boolean stacked) {
			this.stacked = stacked;
		}
	    
	    
}
