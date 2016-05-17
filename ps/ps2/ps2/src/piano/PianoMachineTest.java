package piano;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.sound.midi.MidiUnavailableException;

import midi.Midi;
import music.Pitch;

import org.junit.Test;

public class PianoMachineTest {
	
	PianoMachine pm = new PianoMachine();
	
    @Test
    public void singleNoteTest() throws MidiUnavailableException {
        String expected0 = "on(61,PIANO) wait(100) off(61,PIANO)";
        
    	Midi midi = Midi.getInstance();

    	midi.clearHistory();
    	
        pm.beginNote(new Pitch(1));
		Midi.wait(100);
		pm.endNote(new Pitch(1));

        System.out.println(midi.history());
        assertEquals(expected0,midi.history());
    }
    
    @Test
    public void changeInstrumentTest() throws MidiUnavailableException {
        String expected0 = "on(61,BRIGHT_PIANO) wait(100) off(61,BRIGHT_PIANO)";
        
        pm.changeInstrument();
        
        Midi midi = Midi.getInstance();
        
        midi.clearHistory();
        
        
        pm.beginNote(new Pitch(1));
        Midi.wait(100);
        pm.endNote(new Pitch(1));

        System.out.println(midi.history());
        assertEquals(expected0,midi.history());
    }
    
    @Test
    public void shiftTest() throws MidiUnavailableException {
        String expected0 = "on(73,PIANO) wait(100) off(73,PIANO)";
        
        pm.shiftUp();
        
        Midi midi = Midi.getInstance();

        midi.clearHistory();
        
        pm.beginNote(new Pitch(1));
        Midi.wait(100);
        pm.endNote(new Pitch(1));

        System.out.println(midi.history());
        assertEquals(expected0,midi.history());
    }
    
    
    
    
    
}
