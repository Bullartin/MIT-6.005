package sudoku;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import sudoku.Sudoku.ParseException;


public class SudokuTest {
    Sudoku s = new Sudoku(2);

    // make sure assertions are turned on!  
    // we don't want to run test cases without assertions too.
    // see the handout to find out how to turn them on.
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false;
    }

    @Test
    public void testConstructor() {
        assertEquals("....\n....\n....\n....\n", s.toString());
        Sudoku s2 = new Sudoku(2, new int[][] { 
          new int[] { 0, 1, 0, 4 }, 
          new int[] { 0, 0, 0, 0 }, 
          new int[] { 2, 0, 3, 0 }, 
          new int[] { 0, 0, 0, 0 }, 
        });
        assertEquals(".1.4\n....\n2.3.\n....\n", s2.toString());
    }
    
    @Test 
    public void testReadFromFile() throws IOException, ParseException{
        Sudoku s = Sudoku.fromFile(3, "samples/sudoku_easy.txt");
        assertEquals("2..1.5..3\n"
                +    ".54...71.\n"
                +    ".1.2.3.8.\n"
                +    "6.28.73.4\n"
                +    ".........\n"
                +    "1.53.98.6\n"
                +    ".2.7.1.6.\n"
                +    ".81...24.\n"
                +    "7..4.2..1\n", s.toString());
    }
        
}