package com.mjhutchinson.cansatgroundstationtoolkit.homeWindow;

import com.mjhutchinson.cansatgroundstationtoolkit.modules.AbstractModule;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by Michael Hutchinson on 25/02/2015 at 15:38.
 *
 * A MouseAdapter to listen for MouseEvents on tabs
 */
public class TabMouseAdapter implements MouseListener {

    private JTabbedPane tabbedPane;
    private HomeWindow window;

    public TabMouseAdapter(JTabbedPane tabbedPane, HomeWindow window) {
        this.tabbedPane = tabbedPane;
        this.window = window;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getButton() == 1){
            if(e.getClickCount() == 2){
                try{
                    AbstractModule module = (AbstractModule)tabbedPane.getSelectedComponent();
                    module.popOut();
                }catch(Exception ex){}
            }
        }
        if(e.getButton() == 3){
            try{
                AbstractModule module = (AbstractModule)tabbedPane.getSelectedComponent();
                new TabContextMenu(module, window).show(e.getComponent(), e.getX(), e.getY());
            }catch(Exception ex){}
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
