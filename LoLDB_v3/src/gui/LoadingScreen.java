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
		private String	c_name;
		private int	i;
		
		private int max = champDBlist.size();
		private Lock lock;

		public DownloadThread(String c_name, int i, Lock lock) 
		{
			this.c_name = c_name;
			this.i = i;
			this.lock = lock;
		}

		@Override
		public void run()
		{
			//Der Thread meldete sich in die Lock Methode
			//Nachdem das Bild heruntergeladen wurde, meldet sich der Thread wieder ab
			lock.addRunningThread();
			try{im.download(urllist.get(i), c_name);}
			catch (IOException e1){e1.printStackTrace();}
			lock.removeRunningThread();
		
			//Das LoadingScreen Fenster schließt sich automatisch nachdem alle Champbilder
			//gespeichert wurden
			if(count < max-1)
			{
				progressBar.setValue(progressBar.getValue() + 1);
				progressBar.repaint();
				System.out.println("BAR: " + progressBar.getValue() + " count: " + count);
				count++;
			}
			else
			{
				System.out.println("YO");
		        try
				{
		        	dmc.updateDB();
					mw = new MainWindow(lock);
					dispose();
					mw.setVisible(true);
					
				}
				catch (IOException | InterruptedException e){e.printStackTrace();}
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
	private Lock lock = new Lock();
	
	private int count = 0;

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
	
	
	public LoadingScreen() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException, IOException, InterruptedException
	{
		if (cf.getAllChamp_DB().isEmpty())
			dmc.fillDB("Champ");
		
		urllist = im.getImageUrl();
		champDBlist = cf.getChampList_DB();
		int igor = im.fileList(im.getFileImageOrdner()).length;
		if (!(champDBlist.size() == im.fileList(im.getFileImageOrdner()).length))
		{
			init();
			doc = jm.setSite();
			lock = new Lock();
			int i = 0;
			progressBar.setMaximum(champDBlist.size());
			for ( String c_name : champDBlist )
			{
				if (doc != null)
					new Thread(new DownloadThread(c_name, i, lock)).start();
				else
					System.out.println("");
				i++;
			}
			
		}
		else
		{
			dmc.updateDB();
			mw = new MainWindow(null);
			mw.setVisible(true);
		}
	}

	/**
	 * Create the frame.
	 * @return 
	 * @throws UnsupportedLookAndFeelException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public void init() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException, IOException, InterruptedException 
	{
		//Falls die Champion Datenbank leer ist, so wird eine neue erstellt
		
		
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
		
		//progressBar.setIndeterminate(true);
	}
}
