package piwords;

import java.util.Arrays;

import static org.junit.Assert.*;

import org.junit.Test;

public class PiGeneratorTest {
    @Test
    public void basicPowerModTest() {
        // 5^7 mod 23 = 17
        assertEquals(17, PiGenerator.powerMod(5, 7, 23));
    }

    // TODO: Write more tests (Problem 1.a, 1.c)
    @Test
    public void NegativePowerModTest() {
    	assertEquals(-1, PiGenerator.powerMod(-1, 3, 4));
    	assertEquals(-1, PiGenerator.powerMod(3, -1, -1));
    }
    
    @Test
    public void pi2nddigitTest()
    {
    	int[] a = new int[0x0]; //precision <= 0x0
		int[] b = new int[] {0x2,0x4}; //precision 0x2
		int[] c = new int[] {0x2,0x4,0x3,0xf,0x6,0xa,0x8,0x8,0x8,0x5,0xa,0x3}; //precision 0xc
		assertArrayEquals(a, PiGenerator.computePiInHex(0x0));
		assertArrayEquals(b, PiGenerator.computePiInHex(0x2));
		assertArrayEquals(c, PiGenerator.computePiInHex(0xc));
    }
}
