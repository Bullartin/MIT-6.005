// Board.java
package tetris;

import java.util.Arrays;

/**
 CS108 Tetris Board.
 Represents a Tetris board -- essentially a 2-d grid
 of booleans. Supports tetris pieces and row clearing.
 Has an "undo" feature that allows clients to add and remove pieces efficiently.
 Does not do any drawing or have any idea of pixels. Instead,
 just represents the abstract 2-d board.
*/
public class Board	{
	// Some ivars are stubbed out for you:
	private int width;
	private int height;
	private boolean[][] grid;
	private int[] widths;
	private int[] heights;
	private int maxHeight;
	private boolean DEBUG = true;
	// below are state for undo
	private boolean[][] xGrid;
	private int[] xWidths;
	private int[] xHeights;
	private int xMaxHeight;
	boolean committed;
	
	
	// Here a few trivial methods are provided:
	
	/**
	 Creates an empty board of the given width and height
	 measured in blocks.
	*/
	public Board(int width, int height) {
		this.width = width;
		this.height = height;
		grid = new boolean[width][height];
		heights = new int[width];  // store index of the open spot which is just above the top filled spot
        widths = new int[height];  // widths store how many filled spots there are in each row  
        maxHeight = 0;
        // below are state for undo
		committed = true;
		xGrid = new boolean[width][height];
        xHeights = new int[width];
        xWidths = new int[height];
        xMaxHeight = 0;
	}
	
	
	/**
	 Returns the width of the board in blocks.
	*/
	public int getWidth() {
		return width;
	}
	
	
	/**
	 Returns the height of the board in blocks.
	*/
	public int getHeight() {
		return height;
	}
	
	
	/**
	 Returns the max column height present in the board.
	 For an empty board this is 0.
	*/
	public int getMaxHeight() {	 
		return maxHeight;
	}
	
	
	/**
	 Checks the board for internal consistency -- used
	 for debugging.
	*/
	public void sanityCheck() {
		if (DEBUG) {
		    int[] tempHeight = new int[width];
		    int[] tempWidth = new int[height];
		    int tempMaxHeight = 0;
 		    for (int i = 0; i < width; i++) {
		        for (int j = 0; j < height; j++) {
		            if (grid[i][j]) {
		                tempHeight[i] = Math.max(tempHeight[i], j+1);
		                tempWidth[j] += 1;
		                tempMaxHeight = Math.max(tempMaxHeight, j+1);
		            }
		        }
		    }
// 		    for (int height: heights) {
// 		        System.out.println(height);
// 		    }
// 		    for (int height: tempHeight) {
//               System.out.println(height);
//            }
 		    if (!Arrays.equals(heights, tempHeight)) {
 		        throw new RuntimeException("heights not match");
 		    }
 		    if (!Arrays.equals(widths, tempWidth)) {
               throw new RuntimeException("widths not match");
 		    }
 		    if (maxHeight != tempMaxHeight) {
 		        throw new RuntimeException("max height not match");
 		    }
		}
	}
	
	/**
	 Given a piece and an x, returns the y
	 value where the piece would come to rest
	 if it were dropped straight down at that x.
	 
	 <p>
	 Implementation: use the skirt and the col heights
	 to compute this fast -- O(skirt length).
	*/
	public int dropHeight(Piece piece, int x) {
		// assume x + piece.witdh <= borad.width
	    if (x + piece.getWidth() > width) {
	        throw new RuntimeException("piece out of bound");
	    }
	    int y = 0;
	    int[] skirt = piece.getSkirt();
		for (int i = 0; i < skirt.length; i++) {
		    y = Math.max(y, heights[x+i]-skirt[i]);
		}
		return y;  // y may >= board.height;
	}
	
	
	/**
	 Returns the height of the given column --
	 i.e. the y value of the highest block + 1.
	 The height is 0 if the column contains no blocks.
	*/
	public int getColumnHeight(int x) {
		return heights[x];
	}
	
	
	/**
	 Returns the number of filled blocks in
	 the given row.
	*/
	public int getRowWidth(int y) {
		 return widths[y];
	}
	
	
	/**
	 Returns true if the given block is filled in the board.
	 Blocks outside of the valid width/height area
	 always return true.
	*/
	public boolean getGrid(int x, int y) {
		if (x >= width || y >= height) {
		    return true;
		}
		return grid[x][y];
	}
	
	
	public static final int PLACE_OK = 0;
	public static final int PLACE_ROW_FILLED = 1;
	public static final int PLACE_OUT_BOUNDS = 2;
	public static final int PLACE_BAD = 3;
	
	/**
	 Attempts to add the body of a piece to the board.
	 Copies the piece blocks into the board grid.
	 Returns PLACE_OK for a regular placement, or PLACE_ROW_FILLED
	 for a regular placement that causes at least one row to be filled.
	 
	 <p>Error cases:
	 A placement may fail in two ways. First, if part of the piece may falls out
	 of bounds of the board, PLACE_OUT_BOUNDS is returned.
	 Or the placement may collide with existing blocks in the grid
	 in which case PLACE_BAD is returned.
	 In both error cases, the board may be left in an invalid
	 state. The client can use undo(), to recover the valid, pre-place state.
	*/
	public int place(Piece piece, int x, int y) {
		if (!committed) throw new RuntimeException("place commit problem");
			
		int result = PLACE_OK;
		for (TPoint point: piece.getBody()) {
		    int i = x + point.x;
		    int j = y + point.y;		    
		    if (i >= width || j >= height || i < 0 || j < 0) {
		        result = PLACE_OUT_BOUNDS;
		        break;
		    } else if (grid[i][j]) {
		        result = PLACE_BAD;
		        break;
		    } else {
		        grid[i][j] = true;
		        heights[i] = Math.max(heights[i], j+1);
		        maxHeight = Math.max(maxHeight, j+1);
		        widths[j] += 1;
		        if (widths[j] == width) {
		            result = PLACE_ROW_FILLED;
		        }
		    }
		}
		
		committed = false;
		if (result == PLACE_OK || result == PLACE_ROW_FILLED) {
		    sanityCheck();
		}
		return result;
	}
	
	
	/**
	 Deletes rows that are filled all the way across, moving
	 things above down. Returns the number of rows cleared.
	*/
	public int clearRows() {
		int rowsCleared = 0;
		int from = 0, to = 0;
		int end = maxHeight;
		for (int i = 0; i < width; i++) {
            heights[i] = 0;
        }
		while (from < end) {
		    if (getRowWidth(from) == width) {
		        from++; rowsCleared++;
		    } else {
		        copyrow(from, to);
		        from++; to++;
		    }
		}
		maxHeight = to;
		clear(to, end);
		
		committed = false;
		sanityCheck();
		return rowsCleared;
	}

	private void copyrow(int from, int to) {
	    for (int i = 0; i < width; i++) {
	        grid[i][to] = grid[i][from];
	        if (grid[i][to]) {
	            heights[i] = to+1;
	        }
	    }
	    widths[to] = widths[from];
	}
	
	private void clear(int to, int end) {
	    for (int i = 0; i < width; i++) {
	        for (int j = maxHeight; j < end; j++) {
	            grid[i][j] = false;
	        }
	    }
	    for (int j = maxHeight; j < end; j++) {
	        widths[j] = 0;
	    }
	}

	/**
	 Reverts the board to its state before up to one place
	 and one clearRows();
	 If the conditions for undo() are not met, such as
	 calling undo() twice in a row, then the second undo() does nothing.
	 See the overview docs.
	*/
	public void undo() {
	    if (committed) {
	        return;
	    }
	    recover();
	    committed = true;
	}
	
	
	/**
	 Puts the board in the committed state.
	*/
	public void commit() {
	    if (committed) {
	        return;
	    }
	    backup();
		committed = true;
	}
	
	// backup current state
	private void backup() {
	    for (int i = 0; i < width; i++) {
	        System.arraycopy(grid[i], 0, xGrid[i], 0, height);
	    }
	    System.arraycopy(heights, 0, xHeights, 0, width);
	    System.arraycopy(widths, 0, xWidths, 0, height);
	    xMaxHeight = maxHeight;
	}
	
	// recover from last commit state
	private void recover() {
	    for (int i = 0; i < width; i++) {
            System.arraycopy(xGrid[i], 0, grid[i], 0, height);
        }
        System.arraycopy(xHeights, 0, heights, 0, width);
        System.arraycopy(xWidths, 0, widths, 0, height);
        maxHeight = xMaxHeight;
	}
	
	/*
	 Renders the board state as a big String, suitable for printing.
	 This is the sort of print-obj-state utility that can help see complex
	 state change over time.
	 (provided debugging utility) 
	 */
	public String toString() {
		StringBuilder buff = new StringBuilder();
		for (int y = height-1; y>=0; y--) {
			buff.append('|');
			for (int x=0; x<width; x++) {
				if (getGrid(x,y)) buff.append('+');
				else buff.append(' ');
			}
			buff.append("|\n");
		}
		for (int x=0; x<width+2; x++) buff.append('-');
		return(buff.toString());
	}
}

