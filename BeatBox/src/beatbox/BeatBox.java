package beatbox;
import javax.swing.*;
import java.awt.*;
import javax.sound.midi.*;
import java.awt.event.*;
import java.util.*;

public class BeatBox {
	JFrame frame;
	JPanel mainPanel;
	Sequencer sequencer;
	Sequence sequence;
	ArrayList<JCheckBox> checkboxList;
	
	String[] instrumentNames = {"Bass Drum", "Closed Hi-Hat", "Open Hi-Hat", "Acoustic Snare", 
			"Crash Cymbal", "Hand Clap", "High Tom", "Hi Bongo", "Maracas", "Whistle",
			"Low Conga", "Cowbell", "Vibraslap", "Low-mid Tom", "High Agogo", "Open Hi Conga"};
	int[] instruments = {35, 42, 46, 38, 49, 39, 50, 60, 70, 72, 64, 56, 58, 47, 67, 63};
	
	public static void main(String[] args) {
		new BeatBox().buildGui();
	}
	
	public void buildGui() {
		frame = new JFrame("Cyber BeatBox");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		BorderLayout layout = new BorderLayout();
		JPanel background = new JPanel(layout);
		background.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		checkboxList = new ArrayList<JCheckBox>();
		Box buttonBox = new Box(BoxLayout.Y_AXIS);
		
		JButton start = new JButton("Start");
		buttonBox.add(start);
		
		JButton stop = new JButton("Stop");
		buttonBox.add(stop);
		
		JButton upTempo = new JButton("Tempo UP");
		buttonBox.add(upTempo);
		
		JButton downTempo = new JButton("Tempo Down");
		buttonBox.add(downTempo);
		
		Box nameBox = new Box(BoxLayout.Y_AXIS);
		for(int i=0; i<16; i++) {
			nameBox.add(new JLabel(instrumentNames[i]));
		}
		
		background.add(BorderLayout.EAST, buttonBox);
		background.add(BorderLayout.WEST, nameBox);
		
		GridLayout grid = new GridLayout(16, 16);
		grid.setVgap(1);
		grid.setHgap(2);
		mainPanel = new JPanel(grid);
		background.add(BorderLayout.CENTER, mainPanel);
		
		for(int i=0; i<256; i++) {
			JCheckBox c = new JCheckBox();
			c.setSelected(false);
			mainPanel.add(c);
			checkboxList.add(c);
		}
		
		frame.getContentPane().add(background);
		frame.setBounds(50, 50, 300, 300);
		frame.pack();
		frame.setVisible(true);
	}
}
