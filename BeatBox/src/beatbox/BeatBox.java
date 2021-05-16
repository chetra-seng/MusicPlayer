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
	Track track;
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
		
		// background will be the main content pane
		JPanel background = new JPanel(layout);
		background.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		// Create a box and adding button to it
		checkboxList = new ArrayList<JCheckBox>();
		Box buttonBox = new Box(BoxLayout.Y_AXIS);
		
		JButton start = new JButton("Start");
		buttonBox.add(start);
		start.addActionListener(new MyStartListener());
		
		JButton stop = new JButton("Stop");
		stop.addActionListener(new MyStopListener());
		buttonBox.add(stop);
		
		JButton upTempo = new JButton("Tempo UP");
		upTempo.addActionListener(new MyUpTempoListener());
		buttonBox.add(upTempo);
		
		JButton downTempo = new JButton("Tempo Down");
		downTempo.addActionListener(new MyDownTempoListener());
		buttonBox.add(downTempo);
		
		//Creating a box and add 16 label with customer font to match the checklist
		Font font = new Font("serif", Font.PLAIN, 16);
		Box nameBox = new Box(BoxLayout.Y_AXIS);
		for(int i=0; i<16; i++) {
			JLabel label = new JLabel(instrumentNames[i]);
			label.setFont(font);
			nameBox.add(label);
		}
		
		background.add(BorderLayout.EAST, buttonBox);
		background.add(BorderLayout.WEST, nameBox);
		
		// Create a grid layout 16 x 16 to hold the checkbox on the main panel
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
		
		setUpMidi();
		frame.getContentPane().add(background);
		frame.setBounds(50, 50, 350, 350);
		frame.pack();
		frame.setVisible(true);
	}
	
	public void setUpMidi() {
		try {
			sequencer = MidiSystem.getSequencer();
			sequencer.open();
			sequence = new Sequence(Sequence.PPQ, 4);
			track = sequence.createTrack();
			sequencer.setTempoInBPM(120);
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void buildTrackAndStart() {
		int[] trackList = null;
		
		// Clear and track and make a new one
		sequence.deleteTrack(track);
		track = sequence.createTrack();
		
		// Loop through the instrument name
		for(int i=0; i<16; i++) {
			trackList = new int[16];
			
			int key = instruments[i];
			
			// Loop through all the 16 check box in each instrument
			for(int j=0; j<16; j++) {
				JCheckBox jc = checkboxList.get(j + 16 * i);
				if(jc.isSelected()) {
					trackList[j] = key;
				}
				else {
					trackList[j] = 0;
				}
			}
			
			makeTrack(trackList);
			track.add(makeEvent(176, 1, 127, 0, 16)); // Make an event for each 16 beats
			
			try {
				sequencer.setSequence(sequence);
				sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
				sequencer.start();
				sequencer.setTempoInBPM(120);
			}
			catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public void makeTrack(int[] trackList) {
		for(int i=0; i<16; i++) {
			int key = trackList[i];
			
			if(key!=0) {
				track.add(makeEvent(144, 9, key, 100, i));	//Note on
				track.add(makeEvent(128, 9, key, 100, i+1)); //Note off
			}
			
		}
	}
	
	// Create a MidiEvent method
	public MidiEvent makeEvent(int cmd, int ch, int n, int h, int dur) {
		ShortMessage a = new ShortMessage();
		MidiEvent event = null;
		try {
			a.setMessage(cmd, ch, n, h);
			event = new MidiEvent(a, dur);
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return event;
	}
	
	// All button event handler 
	public class MyStartListener implements ActionListener{
		public void actionPerformed(ActionEvent a) {
			buildTrackAndStart();
		}
	}
	
	public class MyStopListener implements ActionListener{
		public void actionPerformed(ActionEvent a) {
			sequencer.stop();
		}
	}
	
	public class MyUpTempoListener implements ActionListener{
		public void actionPerformed(ActionEvent a) {
			// Get sequencer tempo and set new tempo
			float tempoFactor = sequencer.getTempoFactor();
			sequencer.setTempoFactor((float)(tempoFactor * 1.03));
		}
	}
	
	public class MyDownTempoListener implements ActionListener{
		public void actionPerformed(ActionEvent a) {
			float tempoFactor = sequencer.getTempoFactor();
			sequencer.setTempoFactor((float)(tempoFactor * 0.97));
		}
	}
}
