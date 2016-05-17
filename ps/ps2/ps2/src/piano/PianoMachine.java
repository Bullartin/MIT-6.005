package piano;

import java.util.ArrayList;
import java.util.List;

//import java.util.HashMap;
//import java.util.Map;

import javax.sound.midi.MidiUnavailableException;

import midi.Instrument;
import midi.Midi;
import music.Pitch;
import music.NoteEvent;
import music.NoteEvent.Kind;

/**
 * @author Administrator
 *
 */
public class PianoMachine {

    private Midi midi;
    private Instrument instrument;
    // private Map<Integer, Boolean> notes; // record note is sounding or not
    private int shift;
    private boolean isRecording;
    private List<NoteEvent> records;

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
        // notes = new HashMap<Integer, Boolean>();
        instrument = Instrument.PIANO;
        shift = 0;
        isRecording = false;
        records = new ArrayList<NoteEvent>();
    }

    /**
     * Begin note if this note has not been sounding yet
     * 
     * @param rawPitch
     */
    public void beginNote(Pitch rawPitch) {
        Pitch newPitch = rawPitch.transpose(shift);
        int note = newPitch.toMidiFrequency();
        // if(notes.containsKey(note) && notes.get(note)){
        // return;
        // }
        // notes.put(note, true);
        if (isRecording) {
            records.add(new NoteEvent(newPitch, System.currentTimeMillis(), instrument, Kind.start));
        }
        midi.beginNote(note, instrument);
    }

    /**
     * End note if this not is currently sounding
     * 
     * @param rawPitch
     */
    public void endNote(Pitch rawPitch) {
        Pitch newPitch = rawPitch.transpose(shift);
        int note = newPitch.toMidiFrequency();
        // if (!notes.containsKey(note) || !notes.get(note)) {
        // return;
        // }
        // notes.put(note, false);
        if (isRecording) {
            records.add(new NoteEvent(newPitch, System.currentTimeMillis(), instrument, Kind.stop));
        }
        midi.endNote(note, instrument);
    }

    /**
     * Change instrument to next instrument
     */
    public void changeInstrument() {
        instrument = instrument.next();
    }

    /**
     * Shift the note up by one octave (12 semitones) Max shift two octaves
     */
    public void shiftUp() {
        if (shift < Pitch.OCTAVE * 2) {
            shift += Pitch.OCTAVE;
        }
    }

    /**
     * Shift the note up down one octave (12 semitones) Max shift two octaves
     */
    public void shiftDown() {
        if (shift > -Pitch.OCTAVE * 2) {
            shift -= Pitch.OCTAVE;
        }
    }

    /**
     * Toggle Recording on and off
     * 
     * @return true if recording is on after toggle else false
     */
    public boolean toggleRecording() {
        if (!isRecording) {
            records = new ArrayList<NoteEvent>();
        }
        isRecording = !isRecording;
        return isRecording;
    }

    /**
     * Play back the note since recording
     */
    protected void playback() {
        for (int i = 0; i < records.size(); i++) {
            NoteEvent event = records.get(i);
            Kind kind = event.getKind();
            if (kind == Kind.start) {
                midi.beginNote(event.getPitch().toMidiFrequency(), event.getInstr());
            } else {
                midi.endNote(event.getPitch().toMidiFrequency(), event.getInstr());
            }
            if (i < records.size() - 1) {
                long duration = records.get(i + 1).getTime() - event.getTime();
                Midi.wait((int) duration);
            }
        }
    }
}