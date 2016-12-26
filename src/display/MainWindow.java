package display;

import java.awt.Container;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

import state.PreMain;
import state.State;

@SuppressWarnings("serial")
public class MainWindow extends JFrame{
	
	private State st;
	private PreMainWindow preWindow;
	private AdminPanel adminPanel;
	private String periph;
	
	
	public MainWindow() throws IOException{
		
		setSt(new PreMain());
		
		setResizable(true);
		setSize(900,600);
		setTitle("Bot Paikea");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		ImageIcon back = new  ImageIcon(ImageIO.read(getClass().getResource("/resources/patate.jpeg")));
		preWindow = new PreMainWindow(back);
		adminPanel = new AdminPanel(back);
		setContentPane(preWindow);
		
		setIconImage(ImageIO.read(getClass().getResource("/resources/musique.jpeg")));
		setVisible(true);
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
}
