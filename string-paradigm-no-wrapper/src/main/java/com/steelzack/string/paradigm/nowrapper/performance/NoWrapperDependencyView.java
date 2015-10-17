package com.steelzack.string.paradigm.nowrapper.performance;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

public class NoWrapperDependencyView extends JPanel {

    /**
	 * 
	 */
	private static final long serialVersionUID = -8522811069947393404L;

	public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                }
                catch (Exception e) {
                    throw new RuntimeException(e);
                }
                
                JFrame frame = new JFrame("Code Combinations View");
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.getContentPane().add(new NoWrapperDependencyView());
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }

    private NoWrapperDependencyTableModel tableModel;
    private JTable table;
    
    private NoWrapperDependencyTreeModel treeModel;
    private JTree tree;
    
    private List<String> dependencies = Collections.emptyList();
    
    public NoWrapperDependencyView() {
        JToolBar toolBar = new JToolBar();
        toolBar.add(new AbstractAction("Load dependencies") {
            /**
			 * 
			 */
			private static final long serialVersionUID = -1459726190734592549L;

			@Override
            public void actionPerformed(ActionEvent e) {
                loadCombinations();
            }
        });
        
        tableModel = new NoWrapperDependencyTableModel();
        table = new JTable(tableModel);
        
        treeModel = new NoWrapperDependencyTreeModel();
        tree = new JTree(treeModel) {
            /**
			 * 
			 */
			private static final long serialVersionUID = 3676921264574247353L;

			@Override
            public Dimension getPreferredScrollableViewportSize() {
                return new Dimension(300, 400);
            }
        };
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                showCombinations(tree.getSelectionPaths());
            }
        });
        
        JSplitPane pane = new JSplitPane();
        pane.setLeftComponent(new JScrollPane(tree));
        pane.setRightComponent(new JScrollPane(table));
        
        setLayout(new BorderLayout());
        add(toolBar, BorderLayout.PAGE_START);
        add(pane, BorderLayout.CENTER);
    }

    private void showCombinations(TreePath[] paths) {
        List<String> filtered = new ArrayList<String>();

        if(paths != null) {
            for(TreePath path : paths) {
                List<String> subDependencyElement = treeModel.getDependencies(path);
                for(String data : dependencies) {
                    NoWrapperDependencyImpl dependency = new NoWrapperDependencyImpl(data);
                    if(dependency.startsWith(subDependencyElement)) {
                        filtered.add(data);
                    }
                }
            }
        }

        loadTableData(filtered);
    }

    private void loadCombinations() {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("."));
        
        int result = chooser.showOpenDialog(this);
        if(result == JFileChooser.APPROVE_OPTION) {
            try {
                loadCombinations(chooser.getSelectedFile());
            }
            catch (IOException e1) {
                JOptionPane.showMessageDialog(this,
                        e1.getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void loadCombinations(File file) throws IOException {
        this.dependencies = readLines(file);
        loadTableData(dependencies);
        loadTreeData(dependencies);
        this.dependencies.clear();


    }

    private void loadTableData(List<String> dependencies) {
        tableModel.clear();
        int lastrow= 0;
        for(String data : dependencies) {
            NoWrapperDependencyImpl dependency = new NoWrapperDependencyImpl(data);
            lastrow= tableModel.addCombination(dependency);
        }
        tableModel.fireTableStructureChanged();
        tableModel.fireTableRowsInserted(0,lastrow);
        
        
    }

    private void loadTreeData(List<String> dependencies) {
        treeModel.clear();
        Object root = treeModel.getRoot();
        tree.expandPath(new TreePath(root));
        for(String data : dependencies) {
            NoWrapperDependencyImpl dependency = new NoWrapperDependencyImpl(data);
            DefaultMutableTreeNode child = treeModel.addCombination(dependency);
            tree.expandPath(new TreePath(new Object[] { root, child }));
        }
        tree.setSelectionPath(new TreePath(root));
    }
    
    private List<String> readLines(File file) throws IOException {
        List<String> lines = new ArrayList<String>();
        FileReader fileReader = new FileReader(file);
        BufferedReader reader = new BufferedReader(fileReader);
        try {
            String line;
            while((line = reader.readLine()) != null) {
                if(!line.isEmpty()) {
                    lines.add(line);
                }
            }
            
            
            
            return lines;
        }
        finally {
        	fileReader.close();
            reader.close();
        }
    }
}
