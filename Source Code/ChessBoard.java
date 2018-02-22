import java.util.ArrayList;
/*Class models a chess board. It has a constructor that sets the pieces the way they
 *  are in the beginning of a chess match, a method to move a piece, a method that 
 *  displays the chess board, a getter for individual pieces, a method for the 
 *  castling move and for promoting a pawn, and methods for determining whether a king 
 *  is in check or check mate.*/
public class ChessBoard 
{
	//the chess board is a 2D array of chess piece objects
	//a chess board is an 8x8 board
	private static final int SIZE_OF_BOARD=8;
	private ChessPiece [][] chessBoard=new ChessPiece[SIZE_OF_BOARD][SIZE_OF_BOARD];
	
	//for checking whether a king is in check or check mate, we need a list of each player's pieces
	private ArrayList<ChessPiece> whitePieces=new ArrayList<>();
	private ArrayList<ChessPiece> blackPieces=new ArrayList<>();
	
	//constructor for a chess board, adds the pieces for the beginning of the match
	public ChessBoard()
	{
		//add the non-pawn white pieces on the first row
		chessBoard[0][0]=new ChessPiece(PieceType.rook,ChessPlayer.white,0,0);
		chessBoard[0][7]=new ChessPiece(PieceType.rook,ChessPlayer.white,0,7);
		chessBoard[0][1]=new ChessPiece(PieceType.knight,ChessPlayer.white,0,1);
		chessBoard[0][6]=new ChessPiece(PieceType.knight,ChessPlayer.white,0,6);
		chessBoard[0][2]=new ChessPiece(PieceType.bishop,ChessPlayer.white,0,2);
		chessBoard[0][5]=new ChessPiece(PieceType.bishop,ChessPlayer.white,0,5);
		chessBoard[0][3]=new ChessPiece(PieceType.queen,ChessPlayer.white,0,3);
		chessBoard[0][4]=new ChessPiece(PieceType.king,ChessPlayer.white,0,4);
		
		/*this for loop adds the white pieces on the first row to the arrayList, creates
		 * and adds to the board the pawns on the second row, then adds the pawns to 
		 * the pieces list*/
		for(int i=0;i<8;i++)
		{
			whitePieces.add(chessBoard[0][i]);
			chessBoard[1][i]=new ChessPiece(PieceType.pawn,ChessPlayer.white,1,i);
			whitePieces.add(chessBoard[1][i]);
		}
		
		//add non-pawn black pieces on row 7
		chessBoard[7][0]= new ChessPiece(PieceType.rook,ChessPlayer.black,7,0);
		chessBoard[7][7]= new ChessPiece(PieceType.rook,ChessPlayer.black,7,7);
		chessBoard[7][1]=new ChessPiece(PieceType.knight,ChessPlayer.black,7,1);
		chessBoard[7][6]=new ChessPiece(PieceType.knight,ChessPlayer.black,7,6);
		chessBoard[7][2]=new ChessPiece(PieceType.bishop,ChessPlayer.black,7,2);
		chessBoard[7][5]=new ChessPiece(PieceType.bishop,ChessPlayer.black,7,5);
		chessBoard[7][3]=new ChessPiece(PieceType.queen,ChessPlayer.black,7,3);
		chessBoard[7][4]=new ChessPiece(PieceType.king,ChessPlayer.black,7,4);
		
		//add pawn black pieces to the list, create black pawns and add them to the list
		for(int i=0;i<8;i++)
		{
			blackPieces.add(chessBoard[7][i]);
			chessBoard[6][i]=new ChessPiece(PieceType.pawn,ChessPlayer.black,6,i);
			blackPieces.add(chessBoard[6][i]);
		}
	}
	
	//method returns the piece at the indicated coordinates
	public ChessPiece getPiece(int row, int column)
	{
		return chessBoard[row][column];
	}
	
	//method returns the size of the chess board
	public static int getSize()
	{
		return SIZE_OF_BOARD;
	}
	
	//method returns the list of all white pieces
	public ArrayList<ChessPiece> getWhitePieces()
	{
		return whitePieces;
	}
	
	//method returns the list of all black pieces
	public ArrayList<ChessPiece> getBlackPieces()
	{
		return blackPieces;
	}
	
	//method moves a piece on the chess board to the given coordinates if the 
	//specified move is valid
	public boolean movePiece(ChessPiece piece, int row, int column)
	{
		if(piece.validMove(row, column, chessBoard))
		{
			//kings are not allowed to be moved to a square where they would be in check
			//even though the move itself is valid otherwise
			if(piece.getType()==PieceType.king )
			{
				ChessPlayer player;
				if(piece.isWhite())
				{
					player=ChessPlayer.white;
				}
				else
				{
					player=ChessPlayer.black;
				}
				
				if(isInCheck(player,row,column))
				{
					return false;
				}
			}
			//if an enemy piece is captured, we remove the captured piece from the appropriate list
			if(chessBoard[row][column]!=null)
			{
				if(chessBoard[row][column].isWhite() && !piece.isWhite())
				{
					whitePieces.remove(chessBoard[row][column]);
				}
				else if(!chessBoard[row][column].isWhite() && piece.isWhite())
				{
					blackPieces.remove(chessBoard[row][column]);
				}
			}
			/*in the en passant movement, the piece that is captured is not on the square you move 
			 * your piece to. So, if the square you move to is empty, we check if you are doing an
			 * en passant move, we remove the captured pawn from the correct list and remove it 
			 * from the board*/
			else if(piece.getType()==PieceType.pawn && piece.getRow()+piece.getDirection()==row && Math.abs(column-piece.getColumn())==1 
					&& chessBoard[piece.getRow()][column]!=null && chessBoard[piece.getRow()][column].hasMoved2Spaces())
			{
				if(chessBoard[piece.getRow()][column].isWhite() && !piece.isWhite())
				{
					whitePieces.remove(chessBoard[piece.getRow()][column]);
				}
				else if(!chessBoard[piece.getRow()][column].isWhite() && piece.isWhite())
				{
					blackPieces.remove(chessBoard[piece.getRow()][column]);
				}
				
				chessBoard[piece.getRow()][column]=null;
				piece.setMoved2Spaces(true);
			}
			else if(piece.getType()==PieceType.pawn)
			{
				piece.setMoved2Spaces(false);
			}
			//set the original square of the piece to be empty
			chessBoard[piece.getRow()][piece.getColumn()]=null;
			
			//update the position variables of the piece and set hasMoved to true
			piece.setPosition(row,column);
			piece.setHasMoved();
			
			//set the piece to its new place on the board, if 
			//there was an enemy piece on that square, we capture it by replacing it
			chessBoard[row][column]=piece;
			return true;
		}
		else 
		{
			return false;
		}
	}
	
	/*method for moving a piece on the board in the castling move, only used in the 
	 * castling method so its private.Castling would not normally be valid, so we dont
	 * call the valid move method of the piece.*/
	public void movePieceNoValidation(ChessPiece piece, int row, int column)
	{
		//set the original square of the piece to be empty
		chessBoard[piece.getRow()][piece.getColumn()]=null;
		
		//update the position variables of the piece and set hasMoved to true
		piece.setPosition(row,column);
		piece.setHasMoved();
		
		//set the piece to its new place on the board
		chessBoard[row][column]=piece;
	}
	
	//method for promoting a pawn at a certain square to a new piece of another type
	//returns false if that is not possible and does nothing instead
	public boolean promotePawn(PieceType type, int row, int column)
	{
		//canBePromoted checks if the piece at those coordinates is a pawn on the row 
		//opposite its starting end of the board
		if(chessBoard[row][column]==null)
		{
			return false;
		}
		else if(chessBoard[row][column].canBePromoted())
		{
			//we find out the colour of the piece to assign the new piece the proper colour
			ChessPlayer player;
			if(chessBoard[row][column].isWhite())
			{
				player=ChessPlayer.white;
			}
			else
			{
				player=ChessPlayer.black;
			}
			//remove the old piece from the correct list of pieces
			getAppropiateList(player).remove(chessBoard[row][column]);
			
			//put the new piece in the specified square
			chessBoard[row][column]=new ChessPiece(type,player, row, column);
			
			//add the new piece to the proper list
			getAppropiateList(player).add(chessBoard[row][column]);
			
			return true;
		}
		//if the old piece cannot be promoted, this move cannot happen
		else
		{
			return false;
		}
	}
	
	/*Method for the castling move. It is a move that involves moving the king 2 squares towards a rook while
	 * simultaneously moving the rook to the square over which the king passed.Method returns false if such
	 * a move is not possible, or returns true after moving the pieces if the move is possbile. It takes in 
	 * a ChessPlayer enumerated object so that it knows which player wants to know, and the column of the rook
	 * it wants to move, since we need to know which of the two rooks the player wants to move. Method checks
	 * if the column given has a rook, so the method will return false if a column that does not have a rook is entered.*/
	public boolean castling(ChessPlayer player, int rookColumn)
	{
		//based on the player, we select the correct row on which the king and the rooks are at first
		int rookRow;
		if(player==ChessPlayer.white)
		{
			rookRow=0;
		}
		else
		{
			rookRow=SIZE_OF_BOARD-1;
		}
		
		//we need to have a rook at those coordinates
		if(chessBoard[rookRow][rookColumn]==null || chessBoard[rookRow][rookColumn].getType()!=PieceType.rook )
		{
			return false;
		}
		//the rook must not have moved
		else if(chessBoard[rookRow][rookColumn].hasMoved()) 
		{
			return false;
		}
		//we need to have a king on column 4 and the row of the rook. Column 4 is where kings are placed in the beginning of the match
		else if(chessBoard[rookRow][4]==null || chessBoard[rookRow][4].getType()!=PieceType.king)
		{
			return false;
		}
		//the king must not have moved
		else if(chessBoard[rookRow][4].hasMoved())
		{
			return false;
		}
		//there must be no pieces on the squares between the king and the rook
		else if(!chessBoard[rookRow][rookColumn].clearLiniarPath(rookRow,4,chessBoard))
		{
			return false;
		}
		//the king must not currently be in check
		else if(isInCheck(player,rookRow,4))
		{
			return false;
		}
		//the king must jump over or land over a square that is in check, these squares have
		//different columns depending on the row, so we have two else ifs
		else if(rookColumn==0 && (isInCheck(player,rookRow,3) || isInCheck(player, rookRow,2)))
		{
			return false;
		}			
		else if(rookColumn==7 && (isInCheck(player,rookRow,5) || isInCheck(player, rookRow,6)))
		{		
			return false;
		}
		//if it as not returned false by know, the move can be done
		else
		{
			/*Depending on the column of the rook being moved, the king and rook 
			 * land on different squares in each case. After the move is made, 
			 * we set the hasMoved boolean to true and the method returns true*/
			if(rookColumn==0)
			{
				/*move king 2 squares toward the rook and the rook on the 
				 *square the king jumped over*/
				movePieceNoValidation(chessBoard[rookRow][rookColumn],rookRow,3);
				chessBoard[rookRow][3].setHasMoved();
				movePieceNoValidation(chessBoard[rookRow][4],rookRow,2);
				chessBoard[rookRow][2].setHasMoved();
				return true;
			}
			/*at this point, we know the rook has not moved yet so the rookColumn
			 * must be either 0 or 7, so this else is equivalent to else if(rookColumn==7)*/
			else
			{
				movePieceNoValidation(chessBoard[rookRow][rookColumn],rookRow,5);
				chessBoard[rookRow][5].setHasMoved();
				movePieceNoValidation(chessBoard[rookRow][4],rookRow,6);
				chessBoard[rookRow][6].setHasMoved();
				return true;
			}
		}
	}
	
	/*Method finds the king of a certain player .Method used in the main method when checking 
	 * if the king is in check or for detecting a check mate.It finds the king 
	 * so that we can call the isInCheck method, which has 2 integers and a ChessPlayer as 
	 * parameters. As a parameters it has a ChessPlayer so that we know which king to find.*/
	public ChessPiece findKing(ChessPlayer player)
	{
		//go through the corresponding list of the pieces untill we find the king and return him
		for(ChessPiece elem: getAppropiateList(player))
		{
			if(elem.getType()==PieceType.king)
			{
				return elem;
			}
		}
		
		//the list will always have a king, this is just so that java does not complain
		return null;
	}
	
	/*Method finds out if ,had a king be in a certain position, the king would be in check.
	 * Parameters are a ChessPlayer, so that we know which whose player's piece would be in 
	 * check, and a row and a column integer, to know which square we see if it is threatened.*/
	public boolean isInCheck(ChessPlayer player,int row, int column)
	{
		//we go through all the pieces of the enemy and see if there is one 
		//which could move to the specified square, if we find one, it is in check and we stop
		if(player==ChessPlayer.white)
		{	
			for (ChessPiece elem: blackPieces)
			{
				if(elem.getType()!=PieceType.pawn && elem.validMove(row, column,chessBoard))
				{
					return true;
				}
				/*pawns capture pieces only on the diagonal. The validPawnMove method also checks that 
				 * the specified square is occupied.Since isInCheck also checks if empty squares would 
				 * be under check, we treat pawns separately.*/
				else if(elem.getType()==PieceType.pawn)
				{
					if(elem.getRow()+elem.getDirection()==row && Math.abs(elem.getColumn()-column)==1)
					{
						return true;
					}
				}
			}
		}
		else
		{
			for (ChessPiece elem: whitePieces)
			{
				if(elem.getType()!=PieceType.pawn && elem.validMove(row, column,chessBoard))
				{
					if(elem.getRow()+elem.getDirection()==row && Math.abs(elem.getColumn()-column)==1)
					{
					return true;
					}
				}
			}
		}
		//if no enemy piece can move to the square, it is not in check
		return false;
	}
	
	//method determines whether or not a king is in a check mate
	//parameter is a ChessPlayer so we know which king we need to check
	public boolean isCheckMate(ChessPlayer player)
	{
		//get the correct king
		ChessPiece king=findKing(player);
		//represents the squares the king can move to or his current square,
		//that are in check
		int checkedSquares=0;
		
		/*variable for the number of squares that the king could move to,
		 * meaning squares that are not occupied by pieces of the same team.
		 * Initialised as 1 for the square the king is currently on.*/
		int possibleSquares=1;
		
		//see if the square the king is on is in check
		if(isInCheck(player, king.getRow(), king.getColumn()))
		{
			checkedSquares++;
		}

		/*There are 8 squares the king can move to. For each, we use the validMove method
		 * to see if the king could move there. If the square we are checking does not 
		 * exist and is off the edges of the board, this method will return false. It will
		 * also return false if the square is populated by a piece of the same colour.
		 * If it returns true, we increment possibleSquares. Then, we check if that 
		 * square is in check, and if it is, we increment checkedSquares.*/
		
		//see if the square directly above the king is in check
		if(king.validMove(king.getRow()+1,king.getColumn(), chessBoard))
		{
			possibleSquares++;
			if(isInCheck(player, king.getRow()+1, king.getColumn()))
			{
				checkedSquares++;
			}
		}
		
		//see if the square directly below the king is in check
		if(king.validMove(king.getRow()-1,king.getColumn(), chessBoard))
		{
			possibleSquares++;
			if(isInCheck(player, king.getRow()-1, king.getColumn()))
			{
				checkedSquares++;
			}
		}
		
		//see if the square directly to the left of the king is in check
		if(king.validMove(king.getRow(),king.getColumn()-1, chessBoard))
		{
			possibleSquares++;
			if(isInCheck(player, king.getRow(), king.getColumn()-1))
			{
				checkedSquares++;
			}
		}
		
		//see if the square directly to the right of the the king is in check
		if(king.validMove(king.getRow(),king.getColumn()+1, chessBoard))
		{
			possibleSquares++;
			if(isInCheck(player, king.getRow(), king.getColumn()+1))
			{
				checkedSquares++;
			}
		}
		
		//see if the square directly to the bottom left of the king is in check
		if(king.validMove(king.getRow()-1,king.getColumn()-1, chessBoard))
		{
			possibleSquares++;
			if(isInCheck(player,king.getRow()-1,king.getColumn()-1))
			{
				checkedSquares++;
			}
		}
		
		//see if the square directly to the top left of the king is in check
		if(king.validMove(king.getRow()+1,king.getColumn()-1, chessBoard))
		{
			possibleSquares++;
			if(isInCheck(player,king.getRow()+1, king.getColumn()-1))
			{
				checkedSquares++;
			}
		}
		
		//see if the square directly to the top right of the king is in check
		if(king.validMove(king.getRow()+1,king.getColumn()+1, chessBoard))
		{
			possibleSquares++;
			if(isInCheck(player,king.getRow()+1,king.getColumn()+1))
			{
				checkedSquares++;
			}
		}
		
		//see if the square directly to the bottom right of the king is in check
		if(king.validMove(king.getRow()-1,king.getColumn()+1, chessBoard))
		{
			possibleSquares++;
			if(isInCheck(player,king.getRow()-1,king.getColumn()+1))
			{
				checkedSquares++;
			}
		}
	
		//if all squares the king can move to are in check, the player is checkmated.
		if(checkedSquares==possibleSquares)
		{
			return true;
		}
		else
		{
			return false;
		}
		
		//if all squares the king can move to are in check, the player is checkmated
		if(checkedSquares==possibleSquares)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/*method determines whether there is a stalemate,meaning the king is not in check, 
	 *but any possible move would place him in check. Same as the previous method, 
	 *but does not check the square the king is on .*/
	public boolean isStalemate(ChessPlayer player)
	{
		ChessPiece king=findKing(player);
		int checkedSquares=0;
		//only check the squares around the king, so we start at 0 and not 1
		int possibleSquares=0;
	
		// see if the square directly above the king is in check
		if (king.validMove(king.getRow() + 1, king.getColumn(), chessBoard))
		{
			possibleSquares++;
			if (isInCheck(player, king.getRow() + 1, king.getColumn()))
			{
				checkedSquares++;
			}
		}

		// see if the square directly below the king is in check
		if (king.validMove(king.getRow() - 1, king.getColumn(), chessBoard))
		{
			possibleSquares++;
			if (isInCheck(player, king.getRow() - 1, king.getColumn()))
			{
				checkedSquares++;
			}
		}

		// see if the square directly to the left of the king is in check
		if (king.validMove(king.getRow(), king.getColumn() - 1, chessBoard))
		{
			possibleSquares++;
			if (isInCheck(player, king.getRow(), king.getColumn() - 1))
			{
				checkedSquares++;
			}
		}

		// see if the square directly to the right of the the king is in check
		if (king.validMove(king.getRow(), king.getColumn() + 1, chessBoard))
		{
			possibleSquares++;
			if (isInCheck(player, king.getRow(), king.getColumn() + 1))
			{
				checkedSquares++;
			}
		}

		// see if the square directly to the bottom left of the king is in check
		if (king.validMove(king.getRow() - 1, king.getColumn() - 1, chessBoard))
		{
			possibleSquares++;
			if (isInCheck(player, king.getRow() - 1, king.getColumn() - 1))
			{
				checkedSquares++;
			}
		}

		// see if the square directly to the top left of the king is in check
		if (king.validMove(king.getRow() + 1, king.getColumn() - 1, chessBoard))
		{
			possibleSquares++;
			if (isInCheck(player, king.getRow() + 1, king.getColumn() - 1))
			{
				checkedSquares++;
			}
		}

		// see if the square directly to the top right of the king is in check
		if (king.validMove(king.getRow() + 1, king.getColumn() + 1, chessBoard))
		{
			possibleSquares++;
			if (isInCheck(player, king.getRow() + 1, king.getColumn() + 1))
			{
				checkedSquares++;
			}
		}

		// see if the square directly to the bottom right of the king is in check
		if (king.validMove(king.getRow() - 1, king.getColumn() + 1, chessBoard))
		{
			possibleSquares++;
			if (isInCheck(player, king.getRow() - 1, king.getColumn() + 1))
			{
				checkedSquares++;
			}
		}

		/* if all squares the king can move to are in check, the player is checkmated.If the king 
		 * is surrounded by pieces of the same colour, both possibleSquares and checkedSquares 
		 * would be 0, and the method wrongly say that the king is in a stalemate. So we make 
		 * sure possbielSquares is different than 0.*/
		if (possibleSquares != 0 && checkedSquares == possibleSquares)
		{
			return true;
		} 
		else
		{
			return false;
		}
		
	}
	
	// method return the corresponding list of pieces based on the indicated
	// ChessPlayer enumerated type
	private ArrayList<ChessPiece> getAppropiateList(ChessPlayer player) 
	{
		if (player == ChessPlayer.white) 
		{
			return whitePieces;
		} 
		else 
		{
			return blackPieces;
		}
	}

	//method for printing the entire board
	public void printChessBoard()
	{
		//white pieces start on rows 0 and 1, so the outer loop is decremented so
		//that they are displayed at the bottom of the board
		for (int i = SIZE_OF_BOARD-1; i >=0; i--)
		{
			System.out.print("\n");
			//print row numbers
			System.out.printf("%-5s",i+1);
			for (int j = 0; j < SIZE_OF_BOARD; j++)
			{
				if (chessBoard[i][j] != null)
				{
					System.out.printf("%-5s"," "+chessBoard[i][j].toString()+" ");
				} 
				else
				{
					System.out.printf("%-5s"," 0 ");
				}
			}
			//one empty row separates lines
			System.out.print("\n\n");
		}
		
		//print column letters
		System.out.print("     ");
		for(int i=0;i<8;i++)
		{
			System.out.printf("%-5s"," "+(char)(i+97)+" ");
		}
		
		System.out.print("\n\n");
	}
	
	//method takes a char and converts to an associated integer, a becomes 1, b be becomes 2 ,and so on
	//real chess boards note columns with letters so this method is necessary
	public int charToInt(char letter)
	{
		return (int)(letter)-96;
	}
}
