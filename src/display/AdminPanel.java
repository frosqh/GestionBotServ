package display;

import java.awt.Color;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

import core.ServerThread;

@SuppressWarnings("serial")
public class AdminPanel extends MainLabel{
	
	private JLabel encours = new JLabel("");
	private JLabel listeAtt = new JLabel("");
	private JButton pausePlay;
	private JButton next;
	private JButton stop;
	//private JButton stopServ;
	
	

	public AdminPanel(ImageIcon back) throws IOException {
		super(back);
		pausePlay = new JButton(new  ImageIcon(ImageIO.read(getClass().getResource("/resources/pausePlay.png"))));
		next = new JButton(new ImageIcon(ImageIO.read(getClass().getResource("/resources/next.png"))));
		stop = new JButton(new ImageIcon(ImageIO.read(getClass().getResource("/resources/stop.png"))));
		makeTransparent(pausePlay);
		makeTransparent(next);
		makeTransparent(stop);
		pausePlay.setSize(50,50);
		add(encours);
		add(listeAtt);
		Thread t = new Thread(new Traitement());
		t.start();
		add(pausePlay);
		add(stop);
		add(next);
		//add(stopServ);
	}
	
	
	private void makeTransparent(JButton button) {
		button.setBackground(new Color(0,0,0,1));
		button.setBorderPainted(false);
		button.setContentAreaFilled(false);
		button.setFocusPainted(false);
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
		encours.setText(ServerThread.whichPlaying());
		listeAtt.setText(ServerThread.whichWaiting());
	}
	
}
