package com.mjhutchinson.cansatgroundstationtoolkit.modules;

import com.mjhutchinson.cansatgroundstationtoolkit.serial.SerialDataHandler;
import com.sun.istack.internal.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by Michael Hutchinson on 01/11/2014 at 19:55.
 *
 * The Abstracted definition for all modules which will be displayed in the JTabbedPanes
 * in the HomeWindow.
 */
public abstract class AbstractModule extends JPanel implements SerialDataHandler{


    protected final String TITLE;

    protected JTabbedPane tabbedPane;
    protected JFrame frame;

    /**
     * Default constructor for the module.
     *
     * @param title the title of the module (N.B. must be unique).
     * @param tabbedPane the JTabbedPane in which the module is to appear.
     */
    public AbstractModule(String title,@Nullable JTabbedPane tabbedPane){
        super(new GridLayout(1,1));

        this.TITLE = title;
        this.tabbedPane = tabbedPane;

        if(tabbedPane != null){
            tabbedPane.add(this, title);
        }
    }

    /**
     * Removes the AbstractModule from its JTabbedPane and puts it into its
     * own JFrame.
     */
    public void popOut(){
        tabbedPane.remove(this);
        frame = new JFrame(TITLE);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.add(this);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                popIn();
            }
        });
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Takes an AbstractModule in a JFrame and puts it back into its
     * specified JTabbedPane.
     */
    private void popIn(){
        tabbedPane.add(TITLE, this);
    }

    /**
     * Generates the JPanel containing all of the modules settings and info.
     *
     * @return the JPanel containing settings information.
     */
    public JPanel getSettingsPanel(){
        return new JPanel();
    }

    /**
     * Gets the JTabbedPane in which the AbstractModule appears.
     *
     * @return the modules specified JTabbedPane.
     */
    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }

    /**
     * Gets the String title of the AbstractModule.
     *
     * @return the TITLE of the module.
     */
    public String getTITLE() {
        return TITLE;
    }

    @Override
    public String toString(){
        return TITLE;
    }
}
