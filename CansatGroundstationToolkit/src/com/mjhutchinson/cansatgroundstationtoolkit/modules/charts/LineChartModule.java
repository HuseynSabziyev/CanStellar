package com.mjhutchinson.cansatgroundstationtoolkit.modules.charts;

import com.mjhutchinson.cansatgroundstationtoolkit.modules.AbstractModule;
import com.mjhutchinson.cansatgroundstationtoolkit.serial.SerialDataEvent;
import com.google.common.collect.HashBiMap;
import com.google.common.eventbus.Subscribe;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.general.SeriesException;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Created by Michael Hutchinson on 24/02/2015 at 21:39.
 */
public class LineChartModule extends AbstractModule {

    private final HashBiMap<String, XYSeries> seriesMap;
    private final String Y_AXIS_LABEL;
    private long timeStarted;

    /**
     * The default constructor for a LineChartModule.
     *
     * @param title the title of the LineChartModule (N.B. must be unique).
     * @param tabbedPane the JTabbedPane in which the LineChartModule is to appear.
     * @param serialKeys the String[] of keys which dictates which SerialMessageEvents the LineChartModule
     * picks up and adds to its series. The length of this array dictates the number of series on the LineChartModule.
     * @param seriesLabels the labels for the series dictated by the serialKeys parameter.
     * @param yAxisLabel the label to put on the y-axis of the chart.
     */
    public LineChartModule(String title, JTabbedPane tabbedPane, String[] serialKeys, String[] seriesLabels, String yAxisLabel) {
        super(title, tabbedPane);
        Y_AXIS_LABEL = yAxisLabel;

        //TODO:
        timeStarted = System.currentTimeMillis();

        seriesMap = HashBiMap.create(serialKeys.length);

        for(int i = 0; i < serialKeys.length; i++){
            seriesMap.put(serialKeys[i], new XYSeries(seriesLabels[i], false, true));
        }

        XYSeriesCollection collection = new XYSeriesCollection();

        seriesMap.values().forEach(collection::addSeries);

        JFreeChart chart = createChart(collection);
        ChartPanel chartPanel = new ChartPanel(chart);
        this.add(chartPanel);

    }

    /**
     * Creates a chart from the given dataset
     *
     * @param data the XYDataset from which to create the chart
     * @return the chart created from the dataset
     */
    private JFreeChart createChart(XYDataset data){
        JFreeChart chart = ChartFactory.createXYLineChart(TITLE, "Time(s)", Y_AXIS_LABEL, data);
        XYPlot plot = (XYPlot)chart.getPlot();
        plot.setRangePannable(true);
        plot.setDomainPannable(true);
        NumberAxis numberAxis = (NumberAxis)plot.getRangeAxis();
        numberAxis.setTickMarksVisible(false);
        numberAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        return chart;
    }

    @Override
    @Subscribe
    public void serialDataEventHandler(SerialDataEvent event) {
        if(seriesMap.containsKey(event.getKey()))
            try {
                seriesMap.get(event.getKey()).add(System.currentTimeMillis()-timeStarted, event.getData());
            }catch(NullPointerException|SeriesException e){
                e.printStackTrace();
            }
    }

    @Override
    public JPanel getSettingsPanel() {
        JPanel settingsPanel = new JPanel(new BorderLayout());

        JLabel title = new JLabel(TITLE);
        title.setFont(new Font(title.getFont().getFontName(), Font.BOLD, 32));
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setBorder(new CompoundBorder(new EmptyBorder(2,2,2,2),BorderFactory.createEtchedBorder()));

        settingsPanel.add(title, BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new CompoundBorder(new CompoundBorder(new EmptyBorder(0,2,2,2), BorderFactory.createEtchedBorder()), new EmptyBorder(10,10,10,10)));
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = constraints.gridy = 0;
        constraints.weightx = 1;
        constraints.weighty = 0;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        int count = 0;

        for (XYSeries element : seriesMap.values()) {
            JPanel elementPanel = new JPanel(new GridLayout(2, 1));
            JLabel label = new JLabel((String) element.getKey());
            label.setFont(new Font(label.getFont().getName(), Font.BOLD, 16));
            elementPanel.add(label, 0);
            label = new JLabel("Serial key: " + seriesMap.inverse().get(element));
            label.setFont(new Font(label.getFont().getName(), Font.PLAIN, 14));
            elementPanel.add(label, 1);

            constraints.gridx = Math.floorMod(count, 2);
            constraints.gridy = Math.floorDiv(count, 2);

            panel.add(elementPanel, constraints);

            count++;
        }

        constraints.gridx = Math.floorMod(count, 2);
        constraints.gridy = 0;
        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.VERTICAL;
        constraints.anchor = GridBagConstraints.SOUTH;

        panel.add(Box.createVerticalGlue(), constraints);

        constraints.gridy = 1;

        panel.add(Box.createVerticalGlue(), constraints);

        settingsPanel.add(panel, BorderLayout.CENTER);

        return settingsPanel;
    }
}
