package gui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import fachlogik.DataMining_Champs;

public class Loading extends JFrame {
	public class thread1 implements Runnable {
		@Override
		public void run()
		{
			
			
			
				
				
			//progressBar.setValue(i);
			//progressBar.repaint();
		}
	}


	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	private JPanel	contentPane;
	private JProgressBar progressBar;
	private JButton btnTest;

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
					Loading frame = new Loading();
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
	 * @throws IOException 
	 * @throws UnsupportedLookAndFeelException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 */
	public Loading() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException 
	{
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		
		DataMining_Champs dmc = new DataMining_Champs();
		dmc.updateDB();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 437, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JButton btnStart = new JButton("Start");
		btnStart.setBounds(332, 234, 89, 23);
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try
				{
					do_btnStart_actionPerformed(arg0);
				}
				catch (IOException e)
				{	
					e.printStackTrace();
				}
			}
		});
		contentPane.setLayout(null);
		contentPane.add(btnStart);
		
		progressBar = new JProgressBar();
		progressBar.setBounds(1, 234, 329, 23);
		contentPane.add(progressBar);
		
		btnTest = new JButton("Test");
		btnTest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				do_btnTest_actionPerformed(arg0);
			}
		});
		btnTest.setBounds(10, 203, 89, 23);
		contentPane.add(btnTest);
		
		
		
	}
	
	protected void do_btnStart_actionPerformed(ActionEvent arg0) throws IOException 
	{
		MainWindow frame = new MainWindow();
		frame.setVisible(true);
		dispose();
	}
	
	
	protected void do_btnTest_actionPerformed(ActionEvent arg0) 
	{
		new Thread(new thread1()).start();
	}
}
