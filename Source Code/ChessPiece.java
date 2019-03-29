/**Class models a chess piece object. It has an enumerated type representing its type, a boolean representing black
 * or white player, ints for the row and column, booleans for wether the piece has moved(used for pawns
 * and the castling move) and whether it has just moved 2 spaces(used for pawns), and an integer that is 1 
 * or -1 representing the direction of moving ahead for each team.
 * 
 * It has a constructor, a setter for the position, getters for all the variables, and method for each piece
 * type that determine whether a move is valid. It also has private methods that finds out whether a path to
 *  a new position is not blocked by other pieces.
 *  
 *  @author Alexandru Dascalu
 *  @version 1.2
 */
public abstract class ChessPiece 
{
	/**ChessPlayer is an enumerated type that shows the colour of the piece, 
	 * white or black*/
	protected final ChessPlayer player;
	
	/**piecetype is an enumerated type to only allow possible values*/
	private final PieceType pieceType;
	
	/**The row position of the piece.*/
	protected int row;
	
	/**The column position of the piece.*/
	protected int column;

	/**A flag used to ensure validity of chess piece moves. True if the piece
	 * has moved at least once.*/
	protected boolean hasMoved;
	
	/*Constructor has parameters that specify the piece's type, team, and position*/
	public ChessPiece(PieceType type, ChessPlayer player, int row, int column)
	{
		//set team related variables
		this.player=player;
	
		//set position
		this.row=row;
		this.column=column;
		
		pieceType=type;
		//the piece has not moved at all
		hasMoved=false;
	}
	
	//method sets the position of a piece to a new one
	public void setPosition(int row, int column)
	{
		if(row>=0 && row<ChessBoard.getSize() && column>=0 && column<ChessBoard.getSize())
		{
			this.row=row;
			this.column=column;
		}
	}
	
	//method sets hasMoved to true, needed in the castling method in the ChessBoard class 
	public void setHasMoved()
	{
		hasMoved=true;
	}
	
	//geters for all the variables
	public int getRow()
	{
		return row;
	}
	
	public int getColumn()
	{
		return column;
	}
	
	public ChessPlayer getPlayer()
	{
		return player;
	}
	
	public PieceType getType()
	{
		return pieceType;
	}
	
	public boolean hasMoved()
	{
		return hasMoved;
	}
	
	//method to determine whether a move for a piece is valid, depending on the type 
	//of the piece that is to be moved, it calls the appropiate method for that piece
	public boolean validMove(int row, int column,ChessPiece[][] board)
	{
		//for all piece types, we cannot move it outside the chessBoard
		if(row<0 || row>=ChessBoard.getSize() || column<0 || column>=ChessBoard.getSize())
		{
			return false;
		}
		//players are not allowed to skip a move
		else if(row==this.row && column==this.column)
		{
			return false;
		}
		//for all piece types, you cannot capture your own pieces
		else if(board[row][column]!=null)
		{
			if(player==board[row][column].getPlayer())
			{
				return false;
			}
		}
		
		/*otherwise, we can proceed to further checks to see if it is a valid
		 * move, based on the overriding method*/
		return true;
	}
	
	//method determines whether a move is valid for a king
	private boolean validKingMove(int row, int column, ChessPiece[][] board)
	{
		//a king can move one square diagonally, vertically or horizontally, which means at least
		//one coordinate needs to change by one
		if(Math.abs(this.row-row)<=1 && Math.abs(this.column-column)<=1)
		{
			return true;
		}
		//otherwise, its an invalid move
		else
		{
			return false;
		}
	}
	
	///method determines whether a move is valid for a queen
	private boolean validQueenMove (int row, int column, ChessPiece[][] board)
	{
		//a queen can move horizontally however many spaces, but it cant jump over a piece
		if(this.row==row && clearLiniarPath(row,column,board))
		{
			return true;
		}
		// a queen can move vertically  however many spaces, but it cant jump over a piece
		else if(this.column==column && clearLiniarPath(row,column,board))
		{
			return true;
		}
		// a queen can move diagonally however many spaces, but cant jump over another piece
		else if(Math.abs(this.row-row)==Math.abs(this.column-column) && 
				clearDiagonalPath(row,column,board))
		{
			return true;
		}
		else 
		{
			return false;
		}
	}
	
	//method determines whether a move is valid for a rook
	private boolean validRookMove (int row, int column, ChessPiece[][] board)
	{
		//rooks can move horizontally and cant jump over a piece
		if(this.row==row && clearLiniarPath(row,column,board))
		{
			return true;
		}
		//rooks can move vertically and cant jump over a piece
		else if(this.column==column && clearLiniarPath(row,column,board))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	//method determines whether a move is valid for a bishop
	private boolean validBishopMove(int row, int column, ChessPiece[][] board)
	{
		//bishops can move diagonally on multiple squares, but cant jump over a piece
		if(Math.abs(this.column-column)==Math.abs(this.row-row) && 
				clearDiagonalPath(row,column,board))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/*method finds out whether there are any pieces between 2 positions that are either on
	 *  the same row or column needed for queen and bishop moves.Public because it is also
	 *   used in the method for the castling move in the ChessBoard class.*/
	public boolean clearLiniarPath(int finalRow, int finalColumn,ChessPiece[][] board)
	{
		//so that we dont repeat a for loop, we find out which position has smaller coordinates
		int startPoint, endPoint;
		
		//we find out if the line is vertical or horizontal
		if(this.row==finalRow && this.column!=finalColumn)
		{
			if(this.column>finalColumn)
			{
				startPoint=finalColumn;
				endPoint=this.column;
			}
			else
			{
				startPoint=this.column;
				endPoint=finalColumn;
			}
			
			/*we start from the square after the initial position, and dont check the final square,
			 * since enemy pieces are captured by moving on their location*/
			for(int i=startPoint+1;i<endPoint;i++)
			{
				//if we find just one piece on the way, the path is not clear
				if(board[finalRow][i]!=null)
				{
					return false;
				}
			}
		}
		else if(this.row!=finalRow && this.column==finalColumn)
		{
			if(this.row>finalRow)
			{
				startPoint=finalRow;
				endPoint=this.row;
			}
			else
			{
				startPoint=this.row;
				endPoint=finalRow;
			}
			
			//since its a horizontal line, the for loop loops through different row, not columns
			for(int i=startPoint+1;i<endPoint;i++)
			{
				if(board[i][finalColumn]!=null)
				{
					return false;
				}
			}
		}
		
		return true;
	}
	
	//method finds out if there are pieces between the position of this piece and another square 
	//on the board, both situated diagonally.Private because it is not used in the ChessBoard class
	private boolean clearDiagonalPath(int finalRow, int finalColumn,
			ChessPiece[][] board)
	{
		/*We have to figure out in what direction does each axis position change,
		 * if it increases or decreases.*/
		int xDirection, yDirection;
		
		//set vertical direction
		if(this.row<finalRow)
		{
			xDirection=1;
		}
		else
		{
			xDirection=-1;
		}
		
		//set horizontal direction
		if(this.column<finalColumn)
		{
			yDirection=1;
		}
		else
		{
			yDirection=-1;
		}
		
		//initialise the coordinates used in the loop, we begin from the next square after the start position
		int currentRow=this.row+xDirection;
		int currentColumn=this.column+yDirection;
		
		//we dont check the final position for a piece
		while(currentRow!=finalRow && currentColumn!=finalColumn)
		{
			if(board[currentRow][currentColumn]!=null)
			{
				return false;
			}
			
			currentRow+=xDirection;
			currentColumn+=yDirection;
		}
		
		//if it has not returned false by now, the path is clear
		return true;
	}
	
	/**Checks if this chess piece can be promoted to a more powerful one.
	 * @return true if the chess piece can be promoted, false if not. Returns 
	 * false unless overriden, since only pawns can be promoted.*/
	public boolean canBePromoted()
	{
		return false;
	}
	
	/**Gives a string abreviation of this chess piece, showing its type and 
	 * colour.
	 * @return an abreviation of this chess piece, showing its type and colour.*/
	@Override
	public String toString()
	{
		/*Format of the string will be ColourPiece, so WK means White King, BR means black rook, etc. 
		 * We assign the colour letter first*/
		if(player==ChessPlayer.white)
		{
			return "W";
		}
		else
		{
			return "B";
		}
	}
}
