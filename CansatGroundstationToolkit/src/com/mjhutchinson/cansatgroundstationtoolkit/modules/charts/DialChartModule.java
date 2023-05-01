package com.mjhutchinson.cansatgroundstationtoolkit.modules.charts;

import com.mjhutchinson.cansatgroundstationtoolkit.modules.AbstractModule;
import com.mjhutchinson.cansatgroundstationtoolkit.serial.SerialDataEvent;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.dial.*;
import org.jfree.data.general.DefaultValueDataset;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Michael Hutchinson on 09/03/2015 at 15:32.
 */
public class DialChartModule extends AbstractModule {

    private DefaultValueDataset dataset;

    /**
     * Default constructor for the module.
     *
     * @param title      the title of the module (N.B. must be unique).
     * @param tabbedPane the JTabbedPane in which the module is to appear.
     */
    public DialChartModule(String title, JTabbedPane tabbedPane, String serialKey, Number maxValue, Number minValue) {
        super(title, tabbedPane);
        dataset = new DefaultValueDataset();
        dataset.setValue(10);

        this.add(new ChartPanel(createChart(dataset, title, minValue, maxValue)));
    }

    public JFreeChart createChart(DefaultValueDataset dataset, String title, Number minVal, Number maxVal){
        DialPlot plot = new DialPlot(dataset);
        plot.setView(0.0d, 0.0d, 1.0d, 1.0d);
        plot.setDataset(dataset);
        plot.setDialFrame(new StandardDialFrame());
        StandardDialScale scale = new StandardDialScale(minVal.doubleValue(), maxVal.doubleValue(), 0, -300, 10, 4);
        plot.addScale(0, scale);
        plot.mapDatasetToScale(0,0);
        StandardDialRange range = new StandardDialRange(minVal.doubleValue(), maxVal.doubleValue(), Color.RED);
        plot.addLayer(range);
        DialPointer.Pointer pointer = new DialPointer.Pointer(0);
        plot.addPointer(pointer);

        return new JFreeChart(title, plot);
    }

    @Override
    public void serialDataEventHandler(SerialDataEvent event) {

    }
}
