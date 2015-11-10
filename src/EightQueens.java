// Adrian Melendez
// A1540936
// COP3503C-15Fall 0001
// Eight Queens

import java.applet.Applet;
import java.awt.Button;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Random;

public class EightQueens extends Applet implements MouseListener, MouseMotionListener, Runnable, ActionListener  
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Mine
	// Image for double buffering
	Image offScreen;
	
	Thread m_objThread;
	Image m_imgQueen;//icon of the queen chess piece
	MediaTracker tracker = new MediaTracker(this);//to track when the 'canvas' changes
	
	//parameters for the game board:
	static final int NUMROWS = 8;
	static final int NUMCOLS = 8;
	static final int SQUAREWIDTH = 50;
	static final int SQUAREHEIGHT = 50;
	static final int BOARDLEFT = 50;
	static final int BOARDTOP = 50;
	
	int m_nBoard[][] = new int[NUMROWS][NUMCOLS];//initializes the size of the game board
	
	Button m_objButton = new Button("Solve");
	boolean m_bSolving = false;
	boolean m_bClash;
	String m_strStatus = "";
	
	public void init()
	{
		// Maybe
		setSize( 1020, 700 );
		
        addMouseListener( this );
        add( m_objButton );
        m_objButton.addActionListener(this);        
        
        try
        {
        	m_imgQueen = getImage(getDocumentBase(),"queen.png");
        	tracker.addImage(m_imgQueen, 1);
        	tracker.waitForAll();        	
        }
        catch (Exception e)
        {}
        
        //Mine
        // Creating the Image for double buffering
        offScreen = createImage(1020, 700);
		
        m_objThread = new Thread(this);
        m_objThread.start();
    }

	public void paint (Graphics canvas)
	{
		m_bClash = false;
		DrawSquares( canvas );
		canvas.setColor(Color.RED);
		CheckColumns( canvas );
		CheckRows( canvas );
		CheckDiagonal1( canvas );
		CheckDiagonal2( canvas );
		canvas.setColor(Color.BLUE);
		canvas.drawString(m_strStatus, 
				BOARDLEFT, BOARDTOP + SQUAREHEIGHT * NUMROWS + 20);
	}
	
	void DrawSquares( Graphics canvas )
	{
		canvas.setColor(Color.BLACK);
		for( int nRow=0; nRow<NUMROWS; nRow++ )
		{
			for( int nCol=0; nCol<NUMCOLS; nCol++ )
			{
				canvas.drawRect( BOARDLEFT + nCol * SQUAREWIDTH,
					BOARDTOP + nRow * SQUAREHEIGHT, SQUAREWIDTH, SQUAREHEIGHT );
					
				if( m_nBoard[nRow][nCol] != 0 )
				{
					canvas.drawImage( m_imgQueen,
						BOARDLEFT + nCol * SQUAREWIDTH + NUMCOLS, BOARDTOP + nRow * SQUAREHEIGHT + 6, null );
				}
			}
		}
	}
		
	void CheckColumns( Graphics canvas )
	{
		// Check all columns
		for(  int nCol=0; nCol<NUMCOLS; nCol++ )
		{
			int nColCount = 0;
			for( int nRow=0; nRow<NUMROWS; nRow++ )
			{
				if( m_nBoard[nRow][nCol] != 0 )
				{
					nColCount++;
				}
			}
			if( nColCount > 1 )
			{
				canvas.drawLine( BOARDLEFT + nCol * SQUAREWIDTH + ( SQUAREWIDTH / 2 ),
					BOARDTOP + ( SQUAREHEIGHT / 2 ),	
					BOARDLEFT + nCol * SQUAREWIDTH + ( SQUAREWIDTH / 2 ),
					BOARDTOP + SQUAREHEIGHT * 7 + ( SQUAREHEIGHT / 2 ) );
					
				m_bClash = true;
			}
		}
	}

	void CheckRows( Graphics canvas )
	{
		for(  int nRow=0; nRow<NUMROWS; nRow++ )
		{
			int nRowCount = 0;
			for( int nCol=0; nCol<NUMCOLS; nCol++ )
			{
				if( m_nBoard[nRow][nCol] != 0 )
				{
					nRowCount++;
				}
			}
			if( nRowCount > 1 )
			{
				canvas.drawLine( BOARDLEFT + ( SQUAREWIDTH / 2 ),
					BOARDTOP + nRow * SQUAREHEIGHT + ( SQUAREHEIGHT / 2 ),	
					BOARDLEFT + 7 * SQUAREWIDTH + ( SQUAREWIDTH / 2 ),
					BOARDTOP + nRow * SQUAREHEIGHT + ( SQUAREHEIGHT / 2 ) );
					
				m_bClash = true;
			}
		}
	}
		
	void CheckDiagonal1( Graphics canvas )
	{
		// Check diagonal 1
		
		for( int nRow=NUMROWS-1; nRow>=0; nRow-- )
		{
			int nCol = 0;
				
			int nThisRow = nRow;
			int nThisCol = nCol;

			int nColCount = 0;
				
			while( nThisCol < NUMCOLS &&
				nThisRow < NUMROWS )
			{
				if( m_nBoard[nThisRow][nThisCol] != 0 )
				{
					nColCount++;
				}
				nThisCol++;
				nThisRow++;
			}
				
			if( nColCount > 1 )
			{
				canvas.drawLine( BOARDLEFT + nCol * SQUAREWIDTH + ( SQUAREWIDTH / 2 ),
						BOARDTOP + nRow * SQUAREHEIGHT + ( SQUAREHEIGHT / 2 ),	
						BOARDLEFT + ( nThisCol - 1 ) * SQUAREWIDTH + ( SQUAREWIDTH / 2 ),
						BOARDTOP + ( nThisRow - 1 ) * SQUAREHEIGHT + ( SQUAREHEIGHT / 2 ) );
					
				m_bClash = true;
			}
		}

		for( int nCol=1; nCol<NUMCOLS; nCol++)
		{
			int nRow = 0;
			
			int nThisRow = nRow;
			int nThisCol = nCol;

			int nColCount = 0;
				
			while( nThisCol < NUMCOLS &&
				nThisRow < NUMROWS )
			{
				if( m_nBoard[nThisRow][nThisCol] != 0 )
				{
					nColCount++;
				}
				nThisCol++;
				nThisRow++;
			}
				
			if( nColCount > 1 )
			{
				canvas.drawLine( BOARDLEFT + nCol * SQUAREWIDTH + ( SQUAREWIDTH / 2 ),
						BOARDTOP + nRow * SQUAREHEIGHT + ( SQUAREHEIGHT / 2 ),	
						BOARDLEFT + ( nThisCol - 1 ) * SQUAREWIDTH + ( SQUAREWIDTH / 2 ),
						BOARDTOP + ( nThisRow - 1 ) * SQUAREHEIGHT + ( SQUAREHEIGHT / 2 ) );
					
				m_bClash = true;
			}
		}
	}
		
	void CheckDiagonal2( Graphics canvas )
	{
		// Check diagonal 2
			
		for( int nRow=NUMROWS-1; nRow>=0; nRow-- )
		{
			int nCol = NUMCOLS - 1;
				
			int nThisRow = nRow;
			int nThisCol = nCol;

			int nColCount = 0;
				
			while( nThisCol >= 0 &&
				nThisRow < NUMROWS )
			{
				if( m_nBoard[nThisRow][nThisCol] != 0 )
				{
					nColCount++;
				}
				nThisCol--;
				nThisRow++;
			}
				
			if( nColCount > 1 )
			{
				canvas.drawLine( BOARDLEFT + nCol * SQUAREWIDTH + ( SQUAREWIDTH / 2 ),
						BOARDTOP + nRow * SQUAREHEIGHT + ( SQUAREHEIGHT / 2 ),	
						BOARDLEFT + ( nThisCol + 1 ) * SQUAREWIDTH + ( SQUAREWIDTH / 2 ),
						BOARDTOP + ( nThisRow - 1 ) * SQUAREHEIGHT + ( SQUAREHEIGHT / 2 ) );
					
				m_bClash = true;
			}
		}

		for( int nCol=NUMCOLS-1; nCol>=0; nCol--)
		{
			int nRow = 0;
			
			int nThisRow = nRow;
			int nThisCol = nCol;

			int nColCount = 0;
				
			while( nThisCol >= 0 &&
				nThisRow < NUMROWS )
			{
				if( m_nBoard[nThisRow][nThisCol] != 0 )
				{
					nColCount++;
				}
				nThisCol--;
				nThisRow++;
			}
				
			if( nColCount > 1 )
			{
				canvas.drawLine( BOARDLEFT + nCol * SQUAREWIDTH + ( SQUAREWIDTH / 2 ),
						BOARDTOP + nRow * SQUAREHEIGHT + ( SQUAREHEIGHT / 2 ),	
						BOARDLEFT + ( nThisCol + 1 ) * SQUAREWIDTH + ( SQUAREWIDTH / 2 ),
						BOARDTOP + ( nThisRow - 1 ) * SQUAREHEIGHT + ( SQUAREHEIGHT / 2 ) );
					
				m_bClash = true;
			}
				
		}
	}
	
	// Recursive function to solve the puzzle
	// Any start position for the leftmost column can be used to solve the puzzle.
	//   Using a random number between 0 and the number of rows results in a more
	//   interesting solution than having the puzzle be solved the same way every time.
	private boolean solve(int col) {
		// Base case
		if (col == NUMCOLS) {return true;}
		
		// Value for the start position of the loop when the method is first called.
		int yStart = 0;
		
		// Looping over every box in each column
		for (int y = yStart; y < NUMROWS; y++) {
			// Setting the box to 1 to indicate a queen has been placed.
			m_nBoard[y][col] = 1;
			// Repainting to update the screen and also set m_bClash
			repaint();
			// If there was a clash, set the box to 0
			if (m_bClash) {
				m_nBoard[y][col] = 0;
			// If no clash, move on to the next column recursively.
			} else {
				if (solve(col + 1)) {return true;}
				// If the recursive call didn't return true, the box needs to be cleared.
				m_nBoard[y][col] = 0;
			}
			delay(50);
		}
		
		return false;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// Loops to Zero out the array
		for (int y = 0; y < NUMROWS; y++) {
			for (int x = 0; x < NUMCOLS; x++) {
				m_nBoard[y][x] = 0;
			}
		}
		repaint();
		
		// Initial call to the recursive function
		solve(0);
		
	}

	@Override
	public void run() 
	{
		try
		{
			while( true )
			{
				
				
				Thread.sleep(1);
			}
		}
		catch(Exception ex)
		{
		}
	}
	
	// Mine
	// Method to slow down the program.
	
	private void delay(int t) {
		try {
			Thread.sleep(t);
		}
		catch(Exception ex) {}
	}
	

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent ms) 
	{
		int nCol = ( ms.getX() - BOARDLEFT ) / SQUAREWIDTH;
		int nRow = ( ms.getY() - BOARDTOP ) / SQUAREHEIGHT;
		if( nCol >= 0 &&
			nRow >= 0 &&
			nCol < NUMCOLS &&
			nRow < NUMROWS )
		{
			m_nBoard[nRow][nCol] ^= 1;
			repaint();
		}
	}
	
	// Mine
	// New repaint method to support double buffering 
	public void repaint() {
		Graphics g = offScreen.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 1020, 700);
		paint(g);
		getGraphics().drawImage(offScreen, 0, 0, null);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
