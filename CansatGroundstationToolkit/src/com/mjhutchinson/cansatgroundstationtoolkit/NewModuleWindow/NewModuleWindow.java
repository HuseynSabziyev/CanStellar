package com.mjhutchinson.cansatgroundstationtoolkit.NewModuleWindow;

import com.mjhutchinson.cansatgroundstationtoolkit.homeWindow.HomeWindow;
import com.mjhutchinson.cansatgroundstationtoolkit.modules.charts.LineChartModule;

import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * Created by Michael Hutchinson on 12/03/2015 at 12:09.
 */
public class NewModuleWindow extends JFrame {

    public NewModuleWindow(HomeWindow window, JTabbedPane tabbedPane){

        this.setTitle("Create new module");
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        java.util.List<Image> icons = new LinkedList<>();
        icons.add(new ImageIcon(getClass().getResource("/icons/lowreslogo8.png")).getImage());
        icons.add(new ImageIcon(getClass().getResource("/icons/lowreslogo16.png")).getImage());
        icons.add(new ImageIcon(getClass().getResource("/icons/lowreslogo32.png")).getImage());
        icons.add(new ImageIcon(getClass().getResource("/icons/lowreslogo64.png")).getImage());
        icons.add(new ImageIcon(getClass().getResource("/icons/lowreslogo128.png")).getImage());
        this.setIconImages(icons);

        JPanel panel = new JPanel(new GridLayout(5, 2, 3, 3));
        JTextField nameField = new JTextField("Name");
        nameField.setPreferredSize(new Dimension(300, 10));
        JTextField keysField = new JTextField("Keys");
        keysField.setPreferredSize(new Dimension(300, 10));
        JTextField seriesNamesField = new JTextField("Series Names");
        seriesNamesField.setPreferredSize(new Dimension(300, 10));
        JTextField yLabelField = new JTextField("Y-Label");
        yLabelField.setPreferredSize(new Dimension(300, 10));
        JButton button = new JButton("Create");
        button.addActionListener(e -> {
            String[] keys = keysField.getText().split(":");
            String[] seriesNames = seriesNamesField.getText().split(":");
            if(keys.length != seriesNames.length){
                return;
            }
            window.registerModule(new LineChartModule(nameField.getText(), tabbedPane, keys, seriesNames, yLabelField.getText()));
            this.dispose();
        });

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = constraints.gridy = 0;
        panel.add(nameField,constraints);
        constraints.gridy = 1;
        panel.add(keysField, constraints);
        constraints.gridy = 2;
        panel.add(seriesNamesField, constraints);
        constraints.gridy = 3;
        panel.add(yLabelField, constraints);
        constraints.gridy = 4;
        constraints.gridx = 1;
        panel.add(button, constraints);

        this.getContentPane().add(panel);
        this.pack();
        this.setVisible(true);
    }

}
