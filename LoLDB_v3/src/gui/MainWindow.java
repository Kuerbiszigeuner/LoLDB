package gui;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.html.HTMLEditorKit;

import entities.*;
import fachlogik.*;

public class MainWindow extends JFrame 
{
	private static final long	serialVersionUID	= 1L;
	
	private JPanel contentPane;
	
	private DefaultListModel<Champs> dlm_champs = new DefaultListModel<Champs>();
	private DefaultListModel<Typ> dlm_tags = new DefaultListModel<Typ>();
	private DefaultListModel<Items> dlm_items = new DefaultListModel<Items>();
	
	private Champ_func cf;
	private Typ_func tf;
	private Tagging_func tgf;
	private DataMining_Champs dmc = new DataMining_Champs();
	private DataMining_Items dmi = new DataMining_Items();
	
	private JList<Champs> champ_list;
	private JList<Typ> tag_list;
	private JLabel statusLabel;
	private JPanel pIcon;
	private JLabel lIcon;
	private JList<?> item_list;
	private JLabel lIcon_item;
	private JEditorPane jPE;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow frame = new MainWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	
	public MainWindow() throws IOException 
	{
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage("src\\League_Of_Legends_by_DKman.png"));
		setTitle("Championselect");
		cf = new Champ_func();
		tf = new Typ_func();
		
		new Tagging_func();
		
		
		if ( cf.getFirstChamp().isEmpty() )
			dmc.fillDB("Champ");
		if ( tf.getFirstTyp().isEmpty() )
			dmc.fillDB("Typ");
		
		
		
		dmc.fillDB("Tag");
		dmc.updateDB();
		
		addListe();
		initFrame();
		
		
	
		
	}
	
	public void initFrame(){
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e1)
		{
			e1.printStackTrace();
		}
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 878, 578);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnDatei = new JMenu("Datei");
		menuBar.add(mnDatei);
		
		JMenuItem mntmBeenden = new JMenuItem("Beenden");
		mntmBeenden.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		JMenuItem mntmUpdate = new JMenuItem("Update");
		mntmUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try
				{
					UpdateChamps();
				}
				catch (IOException e1)
				{
					e1.printStackTrace();
				}
			}
		});
		mnDatei.add(mntmUpdate);
		mnDatei.add(mntmBeenden);
		
		JMenu mnAnsicht = new JMenu("Ansicht");
		menuBar.add(mnAnsicht);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(14, 19, 171, 303);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(14, 328, 120, 134);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(15, 472, 832, 2);
		
		statusLabel = new JLabel("");
		statusLabel.setBounds(131, 487, 15, 27);
		
		pIcon = new JPanel();
		pIcon.setBounds(152, 333, 132, 121);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(664, 19, 183, 279);
		
		JPanel item_icon_label = new JPanel();
		item_icon_label.setBounds(577, 345, 132, 121);
		
		JScrollPane scrollPane_3 = new JScrollPane();
		scrollPane_3.setBounds(715, 332, 132, 134);
		
		JScrollPane scrollPane_4 = new JScrollPane();
		scrollPane_4.setBounds(229, 44, 380, 278);
		
		JButton btnNewButton = new JButton("PatchNotes");
		btnNewButton.setBounds(372, 17, 87, 23);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				getPatchNotes();
			}
		});
		
		jPE = new JEditorPane(new HTMLEditorKit().getContentType(),"");
		jPE.setEditable(false);
		scrollPane_4.setViewportView(jPE);
		
		JList<Items> stats_list = new JList<Items>(dlm_items);
		scrollPane_3.setViewportView(stats_list);
		
		lIcon_item = new JLabel("");
		item_icon_label.add(lIcon_item);
		
		item_list = new JList<Object>();
		scrollPane_2.setViewportView(item_list);
		
		lIcon = new JLabel("");
		pIcon.add(lIcon);
		
		tag_list = new JList<Typ>(dlm_tags);
		scrollPane_1.setViewportView(tag_list);
		champ_list = new JList<Champs>(dlm_champs);
		
		
		champ_list.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent me) 
			{
				// 3 = Rechtsklick
				if ( me.getButton() == 3)
				{
					int index = champ_list.locationToIndex(me.getPoint());
					if(index != -1 && champ_list.getSelectedIndex() != index)
			           champ_list.setSelectedIndex(index);
				}	
				if (me.isPopupTrigger())
					contextMenu(me);
			}
			 
		});
		champ_list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				try
				{
					addTagList();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		});
		contentPane.setLayout(null);
		scrollPane.setViewportView(champ_list);
		contentPane.add(scrollPane);
		contentPane.add(btnNewButton);
		contentPane.add(scrollPane_4);
		contentPane.add(scrollPane_1);
		contentPane.add(pIcon);
		contentPane.add(item_icon_label);
		contentPane.add(scrollPane_3);
		contentPane.add(scrollPane_2);
		contentPane.add(statusLabel);
		contentPane.add(separator);
		
		JButton btnChampLschen = new JButton("Champ L\u00F6schen");
		btnChampLschen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try
				{
					do_btnChampLschen_actionPerformed(arg0);
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		});
		btnChampLschen.setBounds(231, 17, 114, 23);
		contentPane.add(btnChampLschen);
	}



	//Die Seite von Leaguepedia wird angezapft um die Patchnotes aufzulisten
	protected void getPatchNotes()
	{
		DataMining_PatchNotes dmpn = new DataMining_PatchNotes();
		
		jPE.setContentType("text/html");
		jPE.setText(dmpn.getPatchNotes(champ_list.getSelectedValue().toString()));
	}


	
	//Bei einem Rechtsklick auf die Liste, erscheint ein Kontextmenü
	//mit zusätzlichen Optionen
	protected void contextMenu(MouseEvent me)
	{
		if(champ_list.getSelectedValue() != null)
		{
			JPopupMenu popmen = new JPopupMenu();
			JMenuItem menu1 = new JMenuItem( "Eintrag 1");
			popmen.add( menu1 );
			popmen.add( new JMenuItem( "Eintrag 2") );
			popmen.show(champ_list, me.getX(), me.getY());
		}
	}


	//Die Datenbank wird, abhängig davon welcher Champ und Tags fehlen, aktualisiert
	protected void UpdateChamps() throws IOException
	{
		dmc.updateDB();
		addListe();
		addTagList();
		statusLabel.setText("Datenbank aktualisiert!");
	}
	
	
	//Listet alle Champions auf
	public void addListe()
	{
		dlm_champs.clear();
		for ( Champs ca : cf.getAllChamp_DB())
			dlm_champs.addElement(ca);
	}
	
	
	
	//Listet die Tags der Champions auf, sowie die Bilder
	//abhängig davon welchen Champion man angeklickt hat
	public void addTagList() throws IOException
	{
		dlm_tags.clear();
		for ( Typ tg : tf.getTagForChamp(champ_list.getSelectedValue()) )
			dlm_tags.addElement(tg);
		
		
		//Das Championbild wird im pIcon Panel angezeigt
		if (champ_list.getSelectedValue() != null)
		{
			for ( Champs c : cf.getChamp_DB(champ_list.getSelectedValue().toString()))
			{
				String path = c.getPath();
				if (path != null)
				{
					BufferedImage img = null;
					img = ImageIO.read(new File(path));

					lIcon.setIcon(new ImageIcon(img));
					pIcon.add(lIcon);
				}
			}
		}
	}
	
	
	//Löscht einen Champ mit seinen Tags
	protected void do_btnChampLschen_actionPerformed(ActionEvent arg0) throws IOException 
	{
		ArrayList<Object> abc = new ArrayList<Object>();
		tgf = new Tagging_func();
		
		for ( Tagging tg : tgf.getTaggingFromChamp(champ_list.getSelectedValue()) )
			abc.add(tg);
		
		cf.deleteChamp(champ_list.getSelectedValue(), abc);
		addListe();
	}
}
