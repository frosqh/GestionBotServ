package help;

import java.io.FileNotFoundException;

import core.GestionServer;
import core.newThread;
import javazoom.jl.decoder.JavaLayerException;

@SuppressWarnings("unused")
public class Lire extends Thread{
	public String song;
	public jlp mp;

	public Lire(String chanson){
		super();
		this.mp = null;
		this.song = chanson;
		this.start();
	}
	
	public void run(){
		this.mp=new jlp();
	    mp.init(song);
	    newThread.setPlaying(true);
		try {
			mp.play();
			newThread.setPlaying(false);
			newThread.lireNext();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			newThread.setMessageError("PasLaChanson");
			newThread.setOk(true);
			newThread.setPlaying(false);
			e.printStackTrace();
		} catch (Exception e) {
			newThread.setPlaying(false);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public jlp getMp() {
		return mp;
	}

	public void setMp(jlp mp) {
		this.mp = mp;
	}

	public String getSong() {
		return song;
	}

	public void setSong(String song) {
		this.song = song;
	}
}
