package help;

import java.io.FileNotFoundException;

import core.GestionServer;
import core.ServerThread;
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
		try {
			mp.play();
			ServerThread.setPlaying(false);
			ServerThread.lireNext();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			ServerThread.setMessageError("PasLaChanson");
			ServerThread.setOk(true);
			ServerThread.setPlaying(false);
			e.printStackTrace();
		} catch (Exception e) {
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
