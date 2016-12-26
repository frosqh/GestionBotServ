package state;

import core.GestionServer;
import display.MainWindow;

public class PreMain implements State{

	@Override
	public void next() {
		MainWindow window = GestionServer.getWindow();
		window.setContentPane(window.getAdminPanel());
		window.setSt(new InAdminPanel());
	}

	@Override
	public void prev() {
			
	}

}
