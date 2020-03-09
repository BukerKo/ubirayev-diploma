package com.ubirayev;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class PlotFrame extends JFrame {
    
    private String title;
    private String xAxisLabel;
    private String yAxisLabel;
    
    public PlotFrame(Map<String, List<XYDataItem>> series, String title, String xAxisLabel, String yAxisLabel) {
        this.title = title;
        this.xAxisLabel = xAxisLabel;
        this.yAxisLabel = yAxisLabel;
        initUI(series);
    }
    
    private void initUI(Map<String, List<XYDataItem>> series) {
        XYDataset dataset = createDataset(series);
        JFreeChart chart = createChart(dataset);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        chartPanel.setBackground(Color.white);
    
        add(chartPanel);
    
        pack();
        setSize(1000, 1000);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }
    
    private XYDataset createDataset(Map<String, List<XYDataItem>> series) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        series.entrySet().stream()
                .map(entry ->  {
                    XYSeries xySeries = new XYSeries(entry.getKey());
                    entry.getValue().forEach(xySeries::add);
                    return xySeries;
                })
                .forEach(dataset::addSeries);
    
        return dataset;
    }
    
    private JFreeChart createChart(XYDataset dataset) {
        JFreeChart chart = ChartFactory.createXYLineChart(
                title,
                xAxisLabel,
                yAxisLabel,
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
    
        XYPlot plot = chart.getXYPlot();
    
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
    
        int seriesCount = dataset.getSeriesCount();
        Random rand = new Random();
        for (int i = 0; i < seriesCount; i++) {
            renderer.setSeriesPaint(i, new Color(rand.nextInt()));
        }
    
        plot.setRenderer(renderer);
        plot.setBackgroundPaint(Color.white);
        plot.setRangeGridlinesVisible(false);
        plot.setDomainGridlinesVisible(false);
    
        chart.getLegend().setFrame(BlockBorder.NONE);
    
        chart.setTitle(new TextTitle(title,
                        new Font("Serif", Font.BOLD, 18)
                )
        );
    
        return chart;
    }
}
