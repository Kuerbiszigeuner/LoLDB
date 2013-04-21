package gui;

import java.awt.EventQueue;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import org.jsoup.nodes.Document;

import fachlogik.*;

public class LoadingScreen extends JFrame {
	public class DownloadThread implements Runnable
	{
		@Override
		public void run()
		{
			doc = jm.setSite();
			int i = 0;
			int max = champDBlist.size();
			
			for ( String c_name : champDBlist )
			{
				if (doc != null)
				{
					//System.out.println("Champ: "+c_name);
					
					try
					{
						im.download(urllist.get(i), c_name);
					}
					catch (IOException e1)
					{
						e1.printStackTrace();
					}
					
					//Progressbar wird aktualisiert, muss zuvor auf einen Maximalwert gesetzt werden 
					//abhängig davon wieviele Champs es gibt
					progressBar.setValue(i);
					progressBar.repaint();
				
					
					//Das LoadingScreen Fenster schließt sich automatisch nachdem alle Champbilder
					//gespeichert wurden
					if(i < max-1)
						i++;
					else
					{
						im.addImageToChamp();
						try
						{
							mw = new MainWindow();
						}
						catch (IOException e)
						{
							e.printStackTrace();
						}
						dispose();
						mw.setVisible(true);
					}
				}
				else
				{
					System.out.println("");
				}
			}
		}
	}
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	private JPanel	contentPane;
	private JProgressBar progressBar;
	
	private MainWindow mw;
	private Document doc;
	private ArrayList<String> urllist = new ArrayList<String>();
	private ArrayList<String> champDBlist = new ArrayList<String>();
	
	private DataMining_Champs dmc = new DataMining_Champs();
	private Champ_func cf = new Champ_func();
	private JSoupManagment jm = new JSoupManagment();
	private ImageManagment im = new ImageManagment();
	private JPanel imageLoLDB;
	
	private BufferedImage img;
	
	private int posX = 0;
	private int posY = 0;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable() {
			public void run()
			{
				try
				{
					LoadingScreen frame = new LoadingScreen();
					frame.setUndecorated(true);
					frame.setVisible(true);
					 
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @throws UnsupportedLookAndFeelException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 * @throws IOException 
	 */
	public LoadingScreen() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException, IOException 
	{
		//Falls die Champion Datenbank leer ist, so wird eine neue erstellt
		if (cf.getAllChamp_DB().isEmpty())
			dmc.fillDB("Champ");
		
		//Das Fenster lässt sich, egal wohin man klickt, verschieben
		addMouseListener(new MouseAdapter() 
		{
			@Override
			//Die Positionen des Mauszeigers werden erfasst
			public void mousePressed(MouseEvent e)
			{
				posX = e.getX();
				posY = e.getY();
			}
			
		});
		addMouseMotionListener(new MouseMotionAdapter() 
		{
			@Override
			//Die erfassten X und Y Achsen setzen den Bereich für das Fenster fest
			public void mouseDragged(MouseEvent evt) 
			{
				setLocation(evt.getXOnScreen()-posX, evt.getYOnScreen()-posY);
			}
		});
		
		setType(Type.UTILITY);
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		@SuppressWarnings("unused")
		EntityManager em = Persistence.createEntityManagerFactory("LoL").createEntityManager();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(600, 600, 600, 230);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		progressBar = new JProgressBar();
		progressBar.setBounds(0, 210, 600, 20);
		
		contentPane.add(progressBar);
		
		
		
		progressBar.setMaximum(cf.getAllChamp_DB().size());
		
		imageLoLDB = new JPanel();
		imageLoLDB.setBounds(0, -20, 600, 230);
		contentPane.add(imageLoLDB);
		
		

		JLabel lIcon = new JLabel("");
		imageLoLDB.add(lIcon);
		
		img = ImageIO.read(new File("src/LoadingScreen.png"));
		lIcon.setIcon(new ImageIcon(img));
		imageLoLDB.add(lIcon);
		
		System.out.println(progressBar.getMaximum());
		new Thread(new DownloadThread()).start();
		urllist = im.getImageUrl();
		champDBlist = cf.getChampList_DB();
	}
	
	
	
}
