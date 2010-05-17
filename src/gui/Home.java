package gui;

import java.awt.GridBagLayout;
import javax.swing.JTree;
import java.awt.GridBagConstraints;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import sniffer.HttpBag;

public class Home extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTree jTree = null;
	private JScrollPane treeView = null;
	/**
	 * This is the default constructor
	 */
	public Home() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.fill = GridBagConstraints.BOTH;
		gridBagConstraints1.gridy = 0;
		gridBagConstraints1.weightx = 1.0;
		gridBagConstraints1.weighty = 1.0;
		gridBagConstraints1.gridx = 0;
		this.setSize(500, 200);
		this.setLayout(new GridBagLayout());
		this.add(getTreeView(), gridBagConstraints1);
	}

	/**
	 * This method initializes jTree	
	 * 	
	 * @return javax.swing.JTree	
	 */
	private JTree getJTree() {
		if (jTree == null) {
			DefaultMutableTreeNode node = new DefaultMutableTreeNode("root");
			jTree = new JTree(node);
			
		}
		return jTree;
	}

	/**
	 * This method initializes treeView	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getTreeView() {
		if (treeView == null) {
			treeView = new JScrollPane();
			treeView.setViewportView(getJTree());
		}
		return treeView;
	}

	
	public void addTreeNode(HttpBag bag)
	{
		JTree tree = getJTree();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode)tree.getModel().getRoot();
		root.add(new DefaultMutableTreeNode(bag));
		tree.expandPath(new TreePath(root));
	}

}
