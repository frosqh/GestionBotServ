package display;

import java.awt.AWTException;
import java.awt.CheckboxMenuItem;
import java.awt.Container;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

import core.ServerThread;
import state.PreMain;
import state.State;

@SuppressWarnings("serial")
public class MainWindow extends JFrame{
	
	private State st;
	private MainLabel preWindow;
	private MainLabel adminPanel;
	private String periph;
	private PopupMenu popup;
	private MenuItem aboutItem;
	private SystemTray tray;
	
	
	public MainWindow() throws IOException{
		setSystemTray();

		setSt(new PreMain());
		
		setResizable(true);
		setSize(900,600);
		setTitle("Bot Paikea");
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e){
				setVisible(false);
			}
		});
		ImageIcon back = new  ImageIcon(ImageIO.read(getClass().getResource("/resources/patate.jpeg")));
		preWindow = new PreMainWindow(back);
		adminPanel = new AdminPanel(back);
		setContentPane(preWindow);
		
		setIconImage(ImageIO.read(getClass().getResource("/resources/musique.jpeg")));
		Traitement t = new Traitement();
		Thread t2 = new Thread(t);
		t2.start();
		setVisible(true);
	}


	private void setSystemTray() throws IOException {
		if (!SystemTray.isSupported()){
			System.out.println("System Tray is not supported");
		}
		else{
		popup = new PopupMenu();
		BufferedImage a = ImageIO.read(getClass().getResource("/resources/musique.jpg"));
		Image b = (Image) a;
		TrayIcon trayIcon = new TrayIcon(b);
		tray = SystemTray.getSystemTray();
		
		// Create a pop-up menu components
		aboutItem = new MenuItem(ServerThread.whichPlaying());
        MenuItem pausePlayItem = new MenuItem("Pause/Play");
        MenuItem nextItem = new MenuItem("Next");
        MenuItem exitItem = new MenuItem("Exit");
        CheckboxMenuItem cb = new CheckboxMenuItem("Afficher notifications");
       
        //Add components to pop-up menu
        popup.add(aboutItem);
        popup.addSeparator();
        popup.add(cb);
        popup.addSeparator();
        popup.add(pausePlayItem);
        popup.add(nextItem);
        popup.add(exitItem);
       
        trayIcon.setPopupMenu(popup);
		
        aboutItem.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(true);
			}
        	
        });
        
        pausePlayItem.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if (ServerThread.isPlaying()){
					ServerThread.pause();
				}
				else{
					ServerThread.lireNext();
					ServerThread.setPlaying(true);
				}
			}
        	
        });
        
        nextItem.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if (ServerThread.isPlaying()){
					ServerThread.lireNext();
				}
				
			}
        	
        });
        
        exitItem.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
	        	System.exit(0);
			}

        });
		
		 try {
	            tray.add(trayIcon);
		 } catch (AWTException e) {
	            System.out.println("TrayIcon could not be added.");
		 }
		}
		
	}


	public State getSt() {
		return st;
	}


	public void setSt(State st) {
		this.st = st;
	}


	public void next() {
		st.next();
	}
	
	public void prev() {
		st.prev();
	}


	public String getPeriph() {
		return periph;
	}


	public void setPeriph(String periph) {
		this.periph = periph;
	}


	public Container getAdminPanel() {
		return adminPanel;
	}
	
	private class Traitement implements Runnable{

		@Override
		public void run() {
			while(true){
				try {
					Thread.sleep(1000);
					update();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

	public void update() {
			aboutItem.setLabel(ServerThread.whichPlaying());
	}
	
	public void displayMessage(String msg){
			tray.getTrayIcons()[0].displayMessage("Musique jouée", msg, TrayIcon.MessageType.INFO);
	}
	
}
