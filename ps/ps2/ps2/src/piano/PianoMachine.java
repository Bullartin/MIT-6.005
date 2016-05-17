package piano;

import java.util.HashMap;
import java.util.Map;

import javax.sound.midi.MidiUnavailableException;

import midi.Instrument;
import midi.Midi;
import music.Pitch;

public class PianoMachine {
	
	private Midi midi;
	private Instrument instrument;
//	private Map<Integer, Boolean> notes;  // record note is sounding or not
	private int shift;
    
	/**
	 * constructor for PianoMachine.
	 * 
	 * initialize midi device and any other state that we're storing.
	 */
    public PianoMachine() {
    	try {
            midi = Midi.getInstance();
        } catch (MidiUnavailableException e1) {
            System.err.println("Could not initialize midi device");
            e1.printStackTrace();
            return;
        }
//    	notes = new HashMap<Integer, Boolean>();
    	instrument = Instrument.PIANO;
    	shift = 0;
    }
        
    /** 
     * Begin note if this note has not been sounding yet 
     * @param rawPitch 
     */
    public void beginNote(Pitch rawPitch) {
        int note = rawPitch.transpose(shift).toMidiFrequency();
//        if(notes.containsKey(note) && notes.get(note)){
//            return;
//        }
//        notes.put(note, true);
        midi.beginNote(note, instrument);

    }
    
    /** 
     * End note if this not is currently sounding
     * @param rawPitch
     */
    public void endNote(Pitch rawPitch) {
        int note = rawPitch.transpose(shift).toMidiFrequency();
//        if (!notes.containsKey(note) || !notes.get(note)) {
//            return;
//        }
//        notes.put(note, false);
        midi.endNote(note, instrument);
    }
    
    /**
     * Change instrument to next instrument
     */
    public void changeInstrument() {
       	instrument = instrument.next();
    }
    
    /**
     * Shift the note up by one octave (12 semitones)
     * Max shift two octaves
     */
    public void shiftUp() {
    	//TODO: implement for question 3
        if (shift < Pitch.OCTAVE * 2) {
            shift += Pitch.OCTAVE;
        }
    }
    
    /**
     * Shift the note up down one octave (12 semitones)
     * Max shift two octaves
     */
    public void shiftDown() {
    	if (shift > -Pitch.OCTAVE * 2) {
    	    shift -= Pitch.OCTAVE;
    	}
    }
    
    //TODO write method spec
    public boolean toggleRecording() {
    	return false;
    	//TODO: implement for question 4
    }
    
    //TODO write method spec
    protected void playback() {    	
        //TODO: implement for question 4
    }

}
