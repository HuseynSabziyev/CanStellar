package com.mjhutchinson.cansatgroundstationtoolkit.homeWindow;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/**
 * Created by Michael Hutchinson on 29/10/2014 at 18:11 at 14:20 at 14:20 at 14:20 at 14:20.
 *
 * The menu system for the HomeWindow.
 */

public class HomeWindowMenu extends JMenuBar{

    private JMenu submenu;
    private JMenuItem menuItem;

    private static JMenuItem openPort, closePort;
    private static JTextField selectedPortField;
    private static JPanel connectionStatusPanel;

    private final Color connectedColor = Color.GREEN;
    private final Color disconnectedColor = Color.RED;
    private final Color selectedBackgroundColor = new Color(163,184,204);

    private static String selectedPort = "";

    /**
     * Default constructor for the menu.
     *
     * @param window the window to build the menu for
     */
    public HomeWindowMenu(HomeWindow window){

        JMenu menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_F);

        menuItem = new JMenuItem("Save");
        menuItem.getAccessibleContext().setAccessibleDescription("Save a .png of a graph");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
        menuItem.addActionListener(e -> {
            
        });
        menu.add(menuItem);

        menuItem = new JMenuItem("Open");
        menuItem.getAccessibleContext().setAccessibleDescription("Open a .png of a graph to view");
        menuItem.setMnemonic(KeyEvent.VK_O);
        menuItem.addActionListener(e -> System.out.println("Opened"));
        menu.add(menuItem);

        menu.addSeparator();

        menuItem = new JMenuItem("Export");
        menuItem.getAccessibleContext().setAccessibleDescription("Export the data from a graph in CSV format");
        menuItem.setMnemonic(KeyEvent.VK_E);
        menuItem.addActionListener(e -> System.out.println("Exported"));
        menu.add(menuItem);

        menuItem = new JMenuItem("Import");
        menuItem.getAccessibleContext().setAccessibleDescription("Import a set of CSV data to view as a graph");
        menuItem.setMnemonic(KeyEvent.VK_I);
        menuItem.addActionListener(e -> System.out.println("Imported"));
        menu.add(menuItem);

        this.add(menu);


        menu = new JMenu("Edit");
        menu.setMnemonic(KeyEvent.VK_E);

        openPort = new JMenuItem("Open serial port");
        openPort.getAccessibleContext().setAccessibleDescription("Open currently selected serial port");
        openPort.setMnemonic(KeyEvent.VK_O);
        openPort.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!selectedPort.equals("")){
                    if(window.serialCommunicator.openPort(selectedPort, Integer.parseInt((String)window.baudRateComboBox.getSelectedItem()))){
                        openPort.setEnabled(false);
                        closePort.setEnabled(true);
                        connectionStatusPanel.setBackground(connectedColor);
                    }
                }
            }
        });
        menu.add(openPort);

        closePort = new JMenuItem("Close serial port");
        closePort.getAccessibleContext().setAccessibleDescription("Close currently open serial port");
        closePort.setMnemonic(KeyEvent.VK_C);
        closePort.setEnabled(false);
        closePort.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                window.serialCommunicator.closePort();
                openPort.setEnabled(true);
                closePort.setEnabled(false);
                connectionStatusPanel.setBackground(disconnectedColor);
            }
        });
        menu.add(closePort);

        submenu = new JMenu("Port");
        submenu.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                submenu.removeAll();
                String[] ports = window.serialCommunicator.getPorts();
                if(ports != null){
                    for(String name: ports){
                        menuItem = new JMenuItem(name);
                        menuItem.addActionListener(e1 -> {
                            if(e1.getActionCommand().equals(selectedPort)){
                                selectedPort = "";
                                selectedPortField.setText("Port: NONE  ");
                            }else{
                                selectedPort = e1.getActionCommand();
                                selectedPortField.setText("Port: " + selectedPort);
                            }
                        });
                        if(name.equals(selectedPort)){
                            menuItem.setBackground(selectedBackgroundColor);
                        }
                        submenu.add(menuItem);
                    }
                }
                if(ports.length == 0){
                    menuItem = new JMenuItem("No ports found");
                    menuItem.setEnabled(false);
                    submenu.add(menuItem);
                }
            }

            @Override
            public void menuDeselected(MenuEvent e) {

            }

            @Override
            public void menuCanceled(MenuEvent e) {

            }
        });

        menu.add(submenu);

        this.add(menu);


        menu = new JMenu("View");
        menu.setMnemonic(KeyEvent.VK_V);

        menuItem = new JMenuItem("Configuration");

        this.add(menu);


        this.add(Box.createHorizontalGlue());


        selectedPortField = new JTextField("Port: NONE  ");
        selectedPortField.setMaximumSize(selectedPortField.getPreferredSize());
        selectedPortField.setEditable(false);
        selectedPortField.setBorder(new EmptyBorder(0,0,0,0));
        selectedPortField.setEnabled(false);
        selectedPortField.setDisabledTextColor(Color.BLACK);

        this.add(selectedPortField);


        connectionStatusPanel  = new JPanel();
        connectionStatusPanel.setBackground(disconnectedColor);
        connectionStatusPanel.setMinimumSize(new Dimension(4,0));
        connectionStatusPanel.setMaximumSize(new Dimension(4 ,100));

        this.add(connectionStatusPanel);

    }


}
