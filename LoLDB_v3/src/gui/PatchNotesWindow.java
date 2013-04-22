package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import entities.Champs;
import entities.Patch;
import fachlogik.*;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class PatchNotesWindow extends JDialog 
{

	private static final long	serialVersionUID	= 1L;
	private final JPanel	contentPanel	= new JPanel();
	private JLabel lTest;	
	private JList<Patch> pList;
	private JButton deletePatch;
	
	private DefaultListModel<Patch> dlm_patch = new DefaultListModel<Patch>();
	
	private Champs c;
	private PatchNotesManagment dmp = new PatchNotesManagment();
	private PatchManagment pm = new PatchManagment();
	private Patch_func pf = new Patch_func();
	private JList<Patch> notesList;
	private JButton btnAddPatchnotes;
	private PatchNotesManagment pnm;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		try
		{
			PatchNotesWindow dialog = new PatchNotesWindow(/*null*/);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public PatchNotesWindow(/*Champs c*/) 
	{
		//this.c = c;
		init();
	}
	
	public void init()
	{
		setModal(true);
		setBounds(100, 100, 624, 411);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		lTest = new JLabel("New label");
		lTest.setBounds(476, 13, 46, 14);
		contentPanel.add(lTest);
		
		//lTest.setText(c.getName());
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 175, 327);
		contentPanel.add(scrollPane);
		
		pList = new JList<Patch>(dlm_patch);
		scrollPane.setViewportView(pList);
		
		deletePatch = new JButton("Delete Patch");
		deletePatch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_btnGetpatchversion_actionPerformed(arg0);
			}
		});
		deletePatch.setBounds(476, 27, 122, 23);
		contentPanel.add(deletePatch);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(195, 61, 403, 277);
		contentPanel.add(scrollPane_1);
		
		notesList = new JList<Patch>();
		scrollPane_1.setViewportView(notesList);
		
		btnAddPatchnotes = new JButton("Add Patchnotes");
		btnAddPatchnotes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				do_btnAddPatchnotes_actionPerformed(e);
			}
		});
		btnAddPatchnotes.setBounds(195, 9, 133, 23);
		contentPanel.add(btnAddPatchnotes);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnTest = new JMenu("Test");
		menuBar.add(mnTest);
		
		JMenuItem mntmDbNeu = new JMenuItem("DB NEU!");
		mntmDbNeu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_mntmDbNeu_actionPerformed(arg0);
			}
		});
		
		JMenuItem mntmUpdate = new JMenuItem("UPDATE!");
		mntmUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				do_mntmUpdate_actionPerformed(e);
			}
		});
		mnTest.add(mntmUpdate);
		mnTest.add(mntmDbNeu);
		
		//TODO Updatefunktion zu Loadingscreen Klasse verschieben
		pm.updatePatchDB();
		addList();
	}
	protected void do_btnGetpatchversion_actionPerformed(ActionEvent arg0) 
	{
		if (pList.getSelectedValue() != null)
			pm.deletePatch(pList.getSelectedValue());
		addList();
	}
	
	
	//Patches werden in die Liste gestopft
	public void addList()
	{
		dlm_patch.clear();
		if (pf.getAllPatch_DB() != null)
		{
			for ( Patch p : pf.getAllPatch_DB() )
				dlm_patch.addElement(p);
		}
	}
	
	//------------TEST--------------
	//Erstellt die Patchversionen neu
	protected void do_mntmDbNeu_actionPerformed(ActionEvent arg0) 
	{
		pm.getPatches();
		addList();
	}
	
	
	protected void do_mntmUpdate_actionPerformed(ActionEvent e) 
	{
		pm.updatePatchDB();
		addList();
	}
	protected void do_btnAddPatchnotes_actionPerformed(ActionEvent e) 
	{
		pnm = new PatchNotesManagment();
		
	}
}
