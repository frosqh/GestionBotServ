package display;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class MainLabel extends JLabel{
	
	public MainLabel(ImageIcon back){
		super(back);
		setSize(900, 600);
		setVisible(true);
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
	}
}
