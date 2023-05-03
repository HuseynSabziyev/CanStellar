package com.mjhutchinson.cansatgroundstationtoolkit.homeWindow;

import com.mjhutchinson.cansatgroundstationtoolkit.NewModuleWindow.NewModuleWindow;
import com.mjhutchinson.cansatgroundstationtoolkit.modules.AbstractModule;
import com.mjhutchinson.cansatgroundstationtoolkit.modules.MPUProcessingModule;
import com.mjhutchinson.cansatgroundstationtoolkit.modules.charts.LineChartModule;
import com.mjhutchinson.cansatgroundstationtoolkit.modules.logging.DataLoggerModule;
import com.mjhutchinson.cansatgroundstationtoolkit.serial.*;
import com.google.common.eventbus.Subscribe;
import com.mjhutchinson.cansatgroundstationtoolkit.modules.ModuleContainer;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

/**
 * The main program class.
 *
 * Created by Michael Hutchinson on 28/10/2014 at 09:14 at 14:27.
 */

public class HomeWindow extends JFrame implements SerialDataHandler, SerialMessageHandler{

    final SerialCommunicator serialCommunicator;
    private ModuleContainer moduleContainer;

    JComboBox baudRateComboBox;
    private JTextField serialInputField;
    private JTextArea serialLogArea;
    private JCheckBox autoScrollCheckBox;
    private JTabbedPane tabbedPane1;
    private JTabbedPane tabbedPane2;
    private JTabbedPane tabbedPane3;
    private JTabbedPane tabbedPane4;


    //TODO: remove after testing
    AbstractModule module1;
    AbstractModule module2;
    AbstractModule module3;
    AbstractModule module4;


    /**
     * Default constructor for the program
     */
    public HomeWindow(){

        super("OSSO CANSAT Data Visualiser");

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        serialCommunicator = new SerialCommunicator();
        serialCommunicator.serialRegister(this);

        moduleContainer = new ModuleContainer();

        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        this.setMinimumSize(new Dimension(1000, 500));

        List<Image> icons = new LinkedList<>();
        icons.add(new ImageIcon(getClass().getResource("/icons/lowreslogo8.png")).getImage());
        icons.add(new ImageIcon(getClass().getResource("/icons/lowreslogo16.png")).getImage());
        icons.add(new ImageIcon(getClass().getResource("/icons/lowreslogo32.png")).getImage());
        icons.add(new ImageIcon(getClass().getResource("/icons/lowreslogo64.png")).getImage());
        icons.add(new ImageIcon(getClass().getResource("/icons/lowreslogo128.png")).getImage());
        this.setIconImages(icons);

        this.setJMenuBar(new HomeWindowMenu(this));

        this.getContentPane().setLayout(new BorderLayout());

        JPanel serialPanel = new JPanel();
        serialPanel.setLayout(new BorderLayout());

        GridBagConstraints gridBagConstraints = new GridBagConstraints();

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        serialInputField = new JTextField();

        gridBagConstraints.gridx = gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = gridBagConstraints.weighty = 1.0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.insets = new Insets(2,2,2,2);

        panel.add(serialInputField, gridBagConstraints);

        JButton button = new JButton("Send");
        button.addActionListener(e -> {
            serialCommunicator.writeSerial(serialInputField.getText());
            serialInputField.setText("");
        });

        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = GridBagConstraints.VERTICAL;
        gridBagConstraints.weightx = 0;

        panel.add(button, gridBagConstraints);

        serialPanel.add(panel, BorderLayout.PAGE_START);


        serialLogArea = new JTextArea();
        serialLogArea.setEditable(false);
        serialLogArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(serialLogArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().addAdjustmentListener(e -> {
            if(autoScrollCheckBox.isSelected()){
                e.getAdjustable().setValue(e.getAdjustable().getMaximum());
            }
        });

        serialPanel.add(scrollPane, BorderLayout.CENTER);


        panel = new JPanel(new GridBagLayout());

        autoScrollCheckBox = new JCheckBox("Autoscroll");
        gridBagConstraints.gridx = gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = gridBagConstraints.weighty = 0.0;
        gridBagConstraints.insets = new Insets(2,2,2,2);
        gridBagConstraints.fill = GridBagConstraints.VERTICAL;

        JCheckBox autoScrollCheckBox1 = autoScrollCheckBox;
        panel.add(autoScrollCheckBox1, gridBagConstraints);

        JPanel padding = new JPanel();

        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = gridBagConstraints.weighty = 1.0;

        panel.add(padding, gridBagConstraints);

        JTextField textField = new JTextField("Baudrate:");
        textField.setEditable(false);
        textField.setBorder(new EmptyBorder(0, 0, 0, 0));

        gridBagConstraints.gridx = 2;
        gridBagConstraints.fill = GridBagConstraints.VERTICAL;
        gridBagConstraints.weightx = gridBagConstraints.weighty = 0.0;

        panel.add(textField, gridBagConstraints);


        String[] baudRates = new String[]{
                "300",
                "1200",
                "2400",
                "4800",
                "9600",
                "14400",
                "19200",
                "28800",
                "38400",
                "57600",
                "115200",
                "230400"};
        baudRateComboBox = new JComboBox<>(baudRates);
        baudRateComboBox.setSelectedIndex(4);

        gridBagConstraints.gridx = 3;
        gridBagConstraints.fill = GridBagConstraints.VERTICAL;
        gridBagConstraints.weightx = gridBagConstraints.weighty = 0.0;

        panel.add(baudRateComboBox, gridBagConstraints);

        serialPanel.add(panel, BorderLayout.PAGE_END);


        this.getContentPane().add(serialPanel, BorderLayout.LINE_START);


        JPanel splitPanel = new JPanel(new BorderLayout());

        tabbedPane1 = new JTabbedPane();
        tabbedPane2 = new JTabbedPane();
        tabbedPane3 = new JTabbedPane();
        tabbedPane4 = new JTabbedPane();

        tabbedPane1.setBorder(new EmptyBorder(0,0,0,0));
        tabbedPane2.setBorder(new EmptyBorder(0,0,0,0));
        tabbedPane3.setBorder(new EmptyBorder(0,0,0,0));
        tabbedPane4.setBorder(new EmptyBorder(0,0,0,0));

        tabbedPane1.addMouseListener(new TabMouseAdapter(tabbedPane1, this));
        tabbedPane2.addMouseListener(new TabMouseAdapter(tabbedPane2, this));
        tabbedPane3.addMouseListener(new TabMouseAdapter(tabbedPane3, this));
        tabbedPane4.addMouseListener(new TabMouseAdapter(tabbedPane4, this));

        JSplitPane innerSplitPanel1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tabbedPane1, tabbedPane2);
        innerSplitPanel1.setResizeWeight(0.5d);
        innerSplitPanel1.setBorder(new EmptyBorder(0, 0, 0, 0));

        JSplitPane innerSplitPanel2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tabbedPane3, tabbedPane4);
        innerSplitPanel2.setResizeWeight(0.5d);
        innerSplitPanel2.setBorder(new EmptyBorder(0, 0, 0, 0));

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, innerSplitPanel1, innerSplitPanel2);
        splitPane.setResizeWeight(0.5d);
        splitPane.setBorder(new EmptyBorder(0, 0, 0, 0));

        splitPanel.add(splitPane, BorderLayout.CENTER);

        this.getContentPane().add(splitPanel, BorderLayout.CENTER);

        //TODO: remove after testing
//        module1 = new LineChartModule("Accelerations", tabbedPane1, new String[]{"AX","AY","AZ"}, new String[]{"Acceleration x","Acceleration y","Acceleration z"}, "Y axis");
//        registerModule(module1);
//        registerModule(new LineChartModule("Gyros", tabbedPane1, new String[]{"GX","GY","GZ"}, new String[]{"Gyro x","Gyro y","Gyro z"}, "Y axis"));
//        registerModule(new LineChartModule("Mags", tabbedPane1, new String[]{"MX","MY","MZ"}, new String[]{"Mag x","Mag y","Mag z"}, "Y axis"));
        registerModule(new LineChartModule("Pressure", tabbedPane1,  new String[]{"PR"}, new String[]{"Pressure"}, "Y axis"));
        registerModule(new LineChartModule("Air quality", tabbedPane2,  new String[]{"AR"}, new String[]{"Air Quality"}, "Y axis"));
//        module2 = new LineChartModule("Quaternions", tabbedPane2,  new String[]{"QA","QB","QC","QD"}, new String[]{"Quaternion A","Quaternion B","Quaternion C","Quaternion D"}, "Y axis");
//        registerModule(module2);
//        module3 = new LineChartModule("Eulers", tabbedPane3,  new String[]{"EA","EB","EG"}, new String[]{"Alpha", "Beta", "Gamma"}, "Y axis");
//        registerModule(module3);
        registerModule(new LineChartModule("Temperature", tabbedPane3,  new String[]{"TP"}, new String[]{"Temperature"}, "Y axis"));
        registerModule(new LineChartModule("Alt", tabbedPane4,  new String[]{"AP", "AT"}, new String[]{"Alt pressure", "Alt tempreature"}, "Y axis"));
//        registerModule(new LineChartModule("GPS", tabbedPane4,  new String[]{"GN","GE","GA","GB","GS"}, new String[]{"GPS lat", "GPS long","GPS alt", "GPS Bering", "GPS Speed"}, "Y axis"));
//        registerModule(new LineChartModule("p/a", tabbedPane4,  new String[]{"PR","AR"}, new String[]{"Pressure", "Air"}, "Y axis"));

        registerModule(new DataLoggerModule());
        registerModule(new MPUProcessingModule("MPU Processor", null));


        //this.new DataGenerator().start();

        //new ConfigurationWindow(this);

        this.pack();
        this.setVisible(true);
    }

    @Subscribe
    public void serialDataEventHandler(SerialDataEvent event){
        //serialLogArea.append(event.getKey() + ":" + event.getData() + "\n");
    }

    @Subscribe
    public void serialMessageEventHandler(SerialMessageEvent event){
        serialLogArea.append(event.getMessage() + "\n");
    }

    /**
     * Takes an instance of AbstractModule and registers it to the programs ModuleContainer
     * and SerialCommunicator.
     *
     * @param module the module to be added to the programs module list.
     *
     * @see com.mjhutchinson.cansatgroundstationtoolkit.modules.AbstractModule
     * @see com.mjhutchinson.cansatgroundstationtoolkit.modules.ModuleContainer
     * @see com.mjhutchinson.cansatgroundstationtoolkit.serial.SerialCommunicator
     */
    public void registerModule(AbstractModule module){
        serialCommunicator.serialRegister(module);
        moduleContainer.putValue(module);
    }

    /**
     * Deregisters a module from the programs ModuleContainer and SerialCommunicator.
     *
     * @param module the module to deregister.
     * @see com.mjhutchinson.cansatgroundstationtoolkit.modules.AbstractModule
     * @see com.mjhutchinson.cansatgroundstationtoolkit.modules.ModuleContainer
     * @see com.mjhutchinson.cansatgroundstationtoolkit.serial.SerialCommunicator
     */
    public void unregisterModule(AbstractModule module){
        serialCommunicator.serialUnregister(module);
        moduleContainer.removeValue(module);
        module.getTabbedPane().remove(module);
    }

    /**
     * Gets the full list of modules currently registered in the program.
     * Returns them in an Enumeration.
     *
     * @return the enumeration of registered modules.
     *
     * @see com.mjhutchinson.cansatgroundstationtoolkit.modules.AbstractModule
     * @see java.util.Enumeration
     */
    public Enumeration<AbstractModule> getModuleEnumerator(){
        return moduleContainer.getElements();
    }

    /**
     * Gets the numerical position of an instance of a JTabbedPane. Used for building
     * configuration and settings UIs
     *
     * @param tabbedPane the pane to check for.
     * @return the position of the JTabbedPane.
     *
     * @see javax.swing.JTabbedPane
     */
    public int getTabbedPaneInt(JTabbedPane tabbedPane){
        if(tabbedPane.equals(tabbedPane1)) return 1;
        else if(tabbedPane.equals(tabbedPane2)) return 2;
        else if(tabbedPane.equals(tabbedPane3)) return 3;
        else if(tabbedPane.equals(tabbedPane4)) return 4;
        else return -1;
    }

    /**
     * A data generator to test the graphs with.
     */
    class DataGenerator extends Timer implements ActionListener {

        private long timeStarted;

        public DataGenerator(){
            super(1000, null);
            timeStarted = System.currentTimeMillis();
            addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try{serialCommunicator.getSerialEventBus().post(new SerialDataEvent("AA", Math.random()*20, System.currentTimeMillis() - timeStarted));
                serialCommunicator.getSerialEventBus().post(new SerialDataEvent("BB", Math.random()*20, System.currentTimeMillis() - timeStarted));
                serialCommunicator.getSerialEventBus().post(new SerialDataEvent("CC", Math.random()*20, System.currentTimeMillis() - timeStarted));
                serialCommunicator.getSerialEventBus().post(new SerialDataEvent("DD", Math.random()*20, System.currentTimeMillis() - timeStarted));
                System.out.println("gen");
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

}
