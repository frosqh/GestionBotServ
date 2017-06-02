package display;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;

import core.GestionServer;

@SuppressWarnings("serial")
public class PreMainWindow extends MainLabel{
	
	private ArrayList<JCheckBox> tabCheck;
	private JButton apply;

	public PreMainWindow(ImageIcon back){
		
		super(back);
		tabCheck = new ArrayList<JCheckBox>();
	
		apply = new JButton("Valider");
		apply.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				//if (getPeriph()!=null){ //For windows
					GestionServer.getWindow().setPeriph(getPeriph());
					GestionServer.getWindow().next();
				//} //For windows
			}
		});
		
		add(new JLabel("Quel périphérique audio souhaitez vous utilisez ?"));
		
		System.out.println(AudioSystem.getMixerInfo()[0].getDescription());
		
		for (Mixer.Info info : AudioSystem.getMixerInfo()){
			//if (info.getDescription().contains("Playback")){
				JCheckBox buttonTemp = new JCheckBox(info.getName());
				buttonTemp.setBackground(new Color(0,0,0,1));
				buttonTemp.setBorderPainted(false);
				buttonTemp.setContentAreaFilled(false);
				buttonTemp.setFocusPainted(false);
				buttonTemp.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e) {
						JCheckBox buttonConcerned = (JCheckBox) e.getSource();
						for (JCheckBox button : tabCheck){
							if (button != buttonConcerned)
							button.setSelected(false);
						}
					}
					
				});
				tabCheck.add(buttonTemp);
				add(buttonTemp);
			//}
		}
		
		add(apply);

		
	}

	
	public String getPeriph(){
		for (JCheckBox button : tabCheck){
			if (button.isSelected()){
				return button.getText();
			}
		}
		return null;
	}
}
