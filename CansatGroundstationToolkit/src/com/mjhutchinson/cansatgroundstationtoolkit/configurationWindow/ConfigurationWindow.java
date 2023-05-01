package com.mjhutchinson.cansatgroundstationtoolkit.configurationWindow;

import com.mjhutchinson.cansatgroundstationtoolkit.modules.AbstractModule;
import com.mjhutchinson.cansatgroundstationtoolkit.modules.charts.LineChartModule;
import com.mjhutchinson.cansatgroundstationtoolkit.homeWindow.HomeWindow;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by Michael Hutchinson on 25/02/2015 at 19:45.
 */
public class ConfigurationWindow extends JFrame implements TreeSelectionListener{

    JPanel settingsPanel;
    JScrollPane scrollPane;
    JTree tree;
    DefaultMutableTreeNode tabbedPane1;
    DefaultMutableTreeNode tabbedPane2;
    DefaultMutableTreeNode tabbedPane3;
    DefaultMutableTreeNode tabbedPane4;
    DefaultMutableTreeNode panes;

    GridBagConstraints gridBagConstraints;

    private HomeWindow window;

    /**
     * Creates a new instance of the configuration window for a specified
     * instance of the program
     *
     * @param window the window which the window is to configure
     */
    public ConfigurationWindow(HomeWindow window){
        super("Configuration");

        this.window = window;


        setPreferredSize(new Dimension(800, 600));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setAlwaysOnTop(true);

        List<Image> icons = new LinkedList<>();
        icons.add(new ImageIcon(getClass().getResource("/icons/lowreslogo8.png")).getImage());
        icons.add(new ImageIcon(getClass().getResource("/icons/lowreslogo16.png")).getImage());
        icons.add(new ImageIcon(getClass().getResource("/icons/lowreslogo32.png")).getImage());
        icons.add(new ImageIcon(getClass().getResource("/icons/lowreslogo64.png")).getImage());
        icons.add(new ImageIcon(getClass().getResource("/icons/lowreslogo128.png")).getImage());
        this.setIconImages(icons);

        getContentPane().setLayout(new BorderLayout());

        gridBagConstraints = new GridBagConstraints();

        tabbedPane1 = new DefaultMutableTreeNode("Pane 1");
        tabbedPane2 = new DefaultMutableTreeNode("Pane 2");
        tabbedPane3 = new DefaultMutableTreeNode("Pane 3");
        tabbedPane4 = new DefaultMutableTreeNode("Pane 4");

        Enumeration<AbstractModule> e = window.getModuleEnumerator();
        while(e.hasMoreElements()){
            AbstractModule module = e.nextElement();
            switch(window.getTabbedPaneInt(module.getTabbedPane())){
                case 1:
                    tabbedPane1.add(new DefaultMutableTreeNode(module, false));
                    break;
                case 2:
                    tabbedPane2.add(new DefaultMutableTreeNode(module, false));
                    break;
                case 3:
                    tabbedPane3.add(new DefaultMutableTreeNode(module, false));
                    break;
                case 4:
                    tabbedPane4.add(new DefaultMutableTreeNode(module, false));
                    break;
                case -1:
                default:
                    System.out.println("Unassigned Module");
                    break;
            }
        }

        panes = new DefaultMutableTreeNode("Panes");
        panes.add(tabbedPane1);
        panes.add(tabbedPane2);
        panes.add(tabbedPane3);
        panes.add(tabbedPane4);

        tree = new JTree(panes, true);
        tree.setCellRenderer(new ModuleTreeCellRenderer());
        tree.setPreferredSize(new Dimension(200,600));
        tree.addTreeSelectionListener(this);

        scrollPane = new JScrollPane(tree, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        getContentPane().add(scrollPane, BorderLayout.WEST);

        settingsPanel = new JPanel(new BorderLayout());

        getContentPane().add(settingsPanel, BorderLayout.CENTER);

        pack();
        setVisible(true);

    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
        settingsPanel.removeAll();
        if(node != null){
            if(node.getUserObject() instanceof AbstractModule){
                AbstractModule module = (AbstractModule)node.getUserObject();

                settingsPanel.add(module.getSettingsPanel(), BorderLayout.CENTER);
            }
        }
        settingsPanel.revalidate();
        settingsPanel.repaint();
    }

    /**
     * A modified DefaultTreeCallRenderer to specify own icons and end node types.
     *
     * @see javax.swing.tree.DefaultTreeCellRenderer
     */
    private class ModuleTreeCellRenderer extends DefaultTreeCellRenderer {

        ImageIcon folderIcon = new ImageIcon(getClass().getResource("/folder.png"));
        ImageIcon chartIcon = new ImageIcon(getClass().getResource("/chart_line.png"));
        ImageIcon exceptionIcon = new ImageIcon(getClass().getResource("/exclamation-white.png"));

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

            if(value instanceof DefaultMutableTreeNode) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
                if(node.getUserObject() instanceof LineChartModule){
                    setIcon(chartIcon);
                }else{
                    setIcon(folderIcon);
                }
            }else{
                setIcon(exceptionIcon);
            }

            return this;
        }

    }

}
