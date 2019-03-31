/**Class models a chess piece object. It has a chess piece type, a type white 
 * or black player, indexes of the starting row and column.
 * 
 * It offers methods to check if a move on the chess board to a certain position 
 * is valid for the given chess piece, according to all recognised rules of chess.
 * @author Alexandru Dascalu
 * @version 1.2
 */
public abstract class ChessPiece 
{
	/**ChessPlayer is an enumerated type that shows the colour of the piece, 
	 * white or black*/
	protected final ChessPlayer player;
	
	/**piecetype is an enumerated type to only allow possible values*/
	protected PieceType pieceType;
	
	/**The row position of the piece.*/
	protected int row;
	
	/**The column position of the piece.*/
	protected int column;

	/**A flag used to ensure validity of chess piece moves. True if the piece
	 * has moved at least once.*/
	protected boolean hasMoved;
	
	/*Constructor has parameters that specify the piece's type, team, and position*/
	public ChessPiece(ChessPlayer player, int row, int column)
	{
		//set team related variables
		this.player=player;
	
		//set position
		this.row=row;
		this.column=column;
		
		pieceType = null;
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
	
	/**
	 * Check if a move of this piece to the square at the given coordinates of 
	 * the given chess board is valid or not, according to chess rules and 
	 * depending on the type of the chess piece. Unless overridden, this method 
	 * just checks if the move would be valid for any type of piece (checking it 
	 * is moved inside the chess board, that it is moved , and that you do not 
	 * move a piece over another piece of yours), and should be called in the 
	 * beginning of any override.
	 * @param row The starting row of this piece.
	 * @param column The starting column of this piece.
	 * @param board The chess board this chess piece is on.
	 * @return True if the mov is valid, false if not.
	 */
	public boolean validMove(int row, int column, ChessBoard board)
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
		//check if the move would capture another piece.
		else if(board.getPiece(row, column)!=null)
		{
			if(player == board.getPiece(row, column).getPlayer())
			{
				return false;
			}
		}
		
		/*otherwise, we can proceed to further checks to see if it is a valid
		 * move, based on the overriding method*/
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
