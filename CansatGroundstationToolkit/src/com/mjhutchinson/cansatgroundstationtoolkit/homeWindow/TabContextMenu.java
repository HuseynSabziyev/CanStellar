package com.mjhutchinson.cansatgroundstationtoolkit.homeWindow;

import com.mjhutchinson.cansatgroundstationtoolkit.NewModuleWindow.NewModuleWindow;
import com.mjhutchinson.cansatgroundstationtoolkit.modules.AbstractModule;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Michael Hutchinson on 25/02/2015 at 15:20.
 *
 * The context menu for right clicks on tabs
 */
public class TabContextMenu extends JPopupMenu{

    public TabContextMenu(AbstractModule module, HomeWindow window){
        super(module.getTITLE());
        JMenuItem item = new JMenuItem("Remove module");
        item.addActionListener(e -> {if(module.getTabbedPane().getTabCount() != 1) window.unregisterModule(module);});
        this.add(item);

        item = new JMenuItem("Add module");
        item.addActionListener(e -> new NewModuleWindow(window, module.getTabbedPane()));
        this.add(item);
    }

}
