
/*Class models a chess piece object. It has an enumerated type representing its type, a boolean representing black
 * or white player, ints for the row and column, booleans for wether the piece has moved(used for pawns
 * and the castling move) and whether it has just moved 2 spaces(used for pawns), and an integer that is 1 
 * or -1 representing the direction of moving ahead for each team.
 * 
 * It has a constructor, a setter for the position, getters for all the variables, and method for each piece
 * type that determine whether a move is valid. It also has private methods that finds out whether a path to
 *  a new position is not blocked by other pieces.*/
public class ChessPiece 
{
	//ChessPlayer is an enumerated type that shows the colour of the piece, white or black
	private final ChessPlayer player;
	//piecetype is an enumerated type to only allow possible values
	private final PieceType pieceType;
	private int row, column;

	//true if the piece has moved at least once
	private boolean hasMoved;
	
	//true if the piece is a pawn and has moved 2 spaces in the previous move
	private boolean moved2Spaces;
	
	/*On a chessboard, for the white player, moving ahead increases the number
	 *of the row. For the black player, moving ahead decreases the number of 
	 *the row, since black pieces start from row 8 or 7. Since pawns can only 
	 *move ahead, we need to know what ahead means.Hence, if its black, the 
	 *direction is -1 so moving ahead decreases the row. If its white, direction is 1.*/
	private final int direction;
	
	/*Constructor has parameters that specify the piece's type, team, and position*/
	public ChessPiece(PieceType type, ChessPlayer player, int row, int column)
	{
		//set team related variables
		this.player=player;
		
		if(player==ChessPlayer.white)
		{
			direction=1;
		}
		else
		{
			direction=-1;
		}
		
		//set position
		this.row=row;
		this.column=column;
		
		pieceType=type;
		//the piece has not moved at all
		hasMoved=false;
		moved2Spaces=false;
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
	
	//method sets moved2Spaces to a specified boolean value
	public void setMoved2Spaces(boolean move)
	{
		moved2Spaces=move;
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
	
	public int getDirection()
	{
		return direction;
	}
	
	public boolean hasMoved()
	{
		return hasMoved;
	}
	
	public boolean hasMoved2Spaces()
	{
		return moved2Spaces;
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
		else if(board[row][column]!=null)
		{
			//for all piece types, you cannot capture your own pieces
			if(player==board[row][column].getPlayer())
			{
				return false;
			}
		}
		//if it has not returned false by now, we check specific conditions for each piece type
		switch (pieceType)
		{
			case pawn:
				return validPawnMove(row,column,board);
			case knight:
				return validKnightMove(row,column,board);
			case rook:
				return validRookMove(row,column,board);
			case bishop:
				return validBishopMove(row,column,board);
			case queen:
				return validQueenMove(row,column,board);
			case king:
				return validKingMove(row,column,board);
				//default value so Java does not complain
			default:
				return false;
		}
	}
	
	//method to determine whether a pawn move is valid
	private boolean validPawnMove(int row, int column, ChessPiece[][] board)
	{
		//simple move forward, cant capture a piece this way
		if(this.row+direction==row && column==this.column && board[row][column]==null)
		{
			moved2Spaces=false;
			return true;
		}
		//move forward 2 spaces if its the first move
		//the final postion and the square the pawn jumps over must be empty
		else if (this.row+2*direction==row && column==this.column && !hasMoved && 
				board[row][column]==null && board[row-1][column]==null)
		{
			moved2Spaces=true;
			return true;
		}
		//capture a piece one space on the diagonal
		else if (this.row+direction==row && Math.abs(column-this.column)==1 && board[row][column]!=null )
		{
			moved2Spaces=false;
			return true;
		}
		//en Passant movement, the space one square on the diagonal needs to be empty and next to your 
		//piece there needs to be a pawn that has just moved 2 spaces, and that is of the opposite team
		else if (this.row+direction==row && Math.abs(column-this.column)==1 && board[row][column]==null 
				&& board[row-direction][column]!=null && board[row-direction][column].hasMoved2Spaces())
		{
			if(player!=board[row-direction][column].getPlayer())
			{
				moved2Spaces=false;
				return true;
			}
			//if next to your pawn there is a pawn that has just moved 2 spaces of 
			//the same colour, the move is invalid
			else
			{
				return false;
			}
		}
		//otherwise, its an invalid move
		else
		{
			return false;
		}
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
	
	/*method determines whether a move is valid for a knight, no ChessPiece[][] parameter
	 * because the knight jumps over pieces so we dont check if the path to its final 
	 * position is clear*/
	private boolean validKnightMove(int row, int column, ChessPiece[][] board)
	{
		/*Knights move in an L shaped pattern, the piece moves 2 squares on one axis and one square
		 * on the other, resulting in 8 possible positions. If either the piece moves 2 squares 
		 * vertically and one horizontally or 2 squares horizontally and one vertically, its a valid move.*/
		if(Math.abs(this.column-column)==2 && Math.abs(this.row-row)==1)
		{
			return true;
		}
		else if(Math.abs(this.column-column)==1 && Math.abs(this.row-row)==2)
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
	
	//method determines whether the piece can be promoted to a more powerful one
	public boolean canBePromoted()
	{
		//only pawns can be promoted
		if(pieceType!=PieceType.pawn)
		{
			return false;
		}
		/*A pawn can only be promoted if it has reached the other end of the board, opposite 
		 * from where they started. White pawns need to get to row 7, and black pawns need to get to row 0.*/
		else if(player==ChessPlayer.white && this.row==7)
		{
			return true;
		}
		else if(player==ChessPlayer.black && this.row==0)
		{
			return true;
		}
		//else, it cant be promoted
		else
		{
			return false;
		}
	}
	
	//outputs an abbreviation of a piece based on its type and colour
	@Override
	public String toString()
	{
		String abreviation;
		
		/*Format of the string will be ColourPiece, so WK means White King, BR means black rook, etc. 
		 * We assign the colour letter first*/
		if(player==ChessPlayer.white)
		{
			abreviation="W";
		}
		else
		{
			abreviation="B";
		}
		
		//assign the piece letter
		switch (pieceType)
		{
			case pawn:
				abreviation+="P";
				break;
			case rook:
				abreviation+="R";
				break;
			case knight:
				abreviation+="KN";
				break;
			case bishop:
				abreviation+="B";
				break;
			case queen:
				abreviation+="Q";
				break;
			case king:
				abreviation+="K";
				break;
		}
		
		return abreviation;
	}
}
