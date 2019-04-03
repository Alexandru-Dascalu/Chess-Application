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
		chessBoard[0][0]=new Rook(ChessPlayer.white,0,0);
		chessBoard[0][7]=new Rook(ChessPlayer.white,0,7);
		chessBoard[0][1]=new Knight(ChessPlayer.white,0,1);
		chessBoard[0][6]=new Knight(ChessPlayer.white,0,6);
		chessBoard[0][2]=new Bishop(ChessPlayer.white,0,2);
		chessBoard[0][5]=new Bishop(ChessPlayer.white,0,5);
		chessBoard[0][3]=new Queen(ChessPlayer.white,0,3);
		chessBoard[0][4]=new King(ChessPlayer.white,0,4);
		
		/*this for loop adds the white pieces on the first row to the arrayList, creates
		 * and adds to the board the pawns on the second row, then adds the pawns to 
		 * the pieces list*/
		for(int i=0;i<8;i++)
		{
			whitePieces.add(chessBoard[0][i]);
			chessBoard[1][i]=new Pawn(ChessPlayer.white,1,i);
			whitePieces.add(chessBoard[1][i]);
		}
		
		//add non-pawn black pieces on row 7
		chessBoard[7][0]= new Rook(ChessPlayer.black,7,0);
		chessBoard[7][7]= new Rook(ChessPlayer.black,7,7);
		chessBoard[7][1]=new Knight(ChessPlayer.black,7,1);
		chessBoard[7][6]=new Knight(ChessPlayer.black,7,6);
		chessBoard[7][2]=new Bishop(ChessPlayer.black,7,2);
		chessBoard[7][5]=new Bishop(ChessPlayer.black,7,5);
		chessBoard[7][3]=new Queen(ChessPlayer.black,7,3);
		chessBoard[7][4]=new King(ChessPlayer.black,7,4);
		
		//add pawn black pieces to the list, create black pawns and add them to the list
		for(int i=0;i<8;i++)
		{
			blackPieces.add(chessBoard[7][i]);
			chessBoard[6][i]=new Pawn(ChessPlayer.black,6,i);
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
	
	/**
     * Moves a piece on the board to the given coordinates. It ensures the move
     * is valid, that the appropriate enemy pieces are removed, and that this 
     * piece is moved and any piece attributes are changed to reflect the move.
     * @param piece The piece to be moved
     * @param endRow The row of the square where the piece will be moved.
     * @param endColumn The column of the square where the piece will be moved.
     */
	public boolean movePiece(ChessPiece piece, int row, int column)
	{
		if(piece.validMove(row, column, this))
		{
			//kings are not allowed to be moved to a square where they would be in check
			//even though the move itself is valid otherwise
			if(piece.getType()==PieceType.king )
			{
				/*Temporarily take the king off the board so that isInCheck will detect whether
				 *  a square would be in check after you would move the king to that square. Else,
				 *  the king might block the reach of an enemy piece to that square, but after 
				 *  you moved the king, the king would be in the reach of an enemy piece and in check.*/
				if(isInCheck(piece.getPlayer(),row,column))
				{
					return false;
				}
			}
			
			//if an enemy piece is captured, we remove the captured piece from the appropriate list
			if(chessBoard[row][column]!=null)
			{
				getAppropiateList(chessBoard[row][column].getPlayer()).remove(chessBoard[row][column]);
			}
			/*in the en passant movement, the piece that is captured is not on 
			 * the square you move your piece to. So, we remove the captured 
			 * pawn at the correct square from the correct list and remove it 
			 * from the board*/
			else if(isEnPassantMove(piece, row, column))
			{
			    /*Get the pawn that needs to be removed after an en passant
			     * move and remove it.*/
			    ChessPiece removedPawn = chessBoard[piece.getRow()][column];
				getAppropiateList(removedPawn.getPlayer()).remove(removedPawn);
				
				chessBoard[piece.getRow()][column]=null;
				((Pawn)piece).setMoved2Spaces(true);
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
	
	/**
	 * Moves a piece on the board in the castling move, only used in the 
     * castling method so its private. Castling would not normally be valid, so we don't
     * call the valid move method of the piece.
	 * @param piece The piece to be moved
     * @param endRow The row of the square where the piece will be moved.
     * @param endColumn The column of the square where the piece will be moved.
	 */
	private void movePieceNoValidation(ChessPiece piece, int row, int column)
	{
		//set the original square of the piece to be empty
		chessBoard[piece.getRow()][piece.getColumn()]=null;
		
		//update the position variables of the piece and set hasMoved to true
		piece.setPosition(row,column);
		piece.setHasMoved();
		
		//set the piece to its new place on the board
		chessBoard[row][column]=piece;
	}
	
	/**
	 * Checks if moving the given piece to the given coordinates would be an 
	 * en passant move.
	 * @param piece The piece to be moved
	 * @param endRow The row of the square where the piece will be moved.
	 * @param endColumn The column of the square where the piece will be moved.
	 * @return True if the move is an en passant move, false if not.
	 */
	private boolean isEnPassantMove(ChessPiece piece, int endRow, int endColumn)
	{
	    /*It can be an en passant move if the chess piece being moved is a pawn.*/
	    if(piece.getType() != PieceType.pawn)
	    {
	        return false;
	    }
	    
	    //we know it is a pawn, so we cast it to use pawn specific methods
	    Pawn movedPawn = (Pawn)piece;
	    
	    /*says if the pawn is being moved forward diagonally*/
	    boolean diagonalMove = movedPawn.getRow() + movedPawn.getDirection() == endRow 
	            && Math.abs(endColumn-piece.getColumn())==1;
	    
	    /*It can be an en passant move if it moves forward diagonally and if 
	     * next to the current position of the pawn there is another piece.*/
	    if(diagonalMove && chessBoard[piece.getRow()][endColumn]!=null)
	    {
	        //the piece currently next to moved piece, that may be an enemy pawn.
	        ChessPiece otherPiece = chessBoard[movedPawn.getRow()][endColumn];
	        
	        /*says if the piece next to the moved piece is an enemy pawn*/
	        boolean isEnemyPawn = otherPiece.getType() == PieceType.pawn 
	                && otherPiece.getPlayer() != movedPawn.getPlayer();
	        
	        /*The move can be an en passant move if the piece next the moved 
	         * piece is an enemy pawn.*/
	        if(isEnemyPawn)
	        {
	            Pawn enemyPawn = (Pawn)otherPiece;
	            
	            /*Finally, it is an en passant move if the enemy pawn has 
	             * moved 2 spaces during its last move.*/
	            if(enemyPawn.hasMoved2Spaces())
	            {
	                return true;
	            }
	        }
	    }
	    
	    /*If any condition has failed so far, it is not an en passant move.*/
	    return false;
	}
	
	/**
	 * Promotes a pawn at the given coordinates on the board to the given
	 *  piece type.
	 * @param type The type of the new chess piece you want.
	 * @param row The row of the pawn you want to promote.
	 * @param column The column of the pawn you want to promote.
	 * @return True if the pawn was promoted, false if the piece at the given
	 * coordinates can not be promoted (nothing happens in this case).
	 */
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
			ChessPlayer player=chessBoard[row][column].getPlayer();
			//remove the old piece from the correct list of pieces
			getAppropiateList(player).remove(chessBoard[row][column]);
			
			/*Make a new chess piece of the right type and put it on the board.*/
			if(type == PieceType.bishop)
			{
			    chessBoard[row][column] = new Bishop(player, row, column);
			}
			else if(type == PieceType.knight)
			{
			    chessBoard[row][column] = new Knight(player, row, column);
			}
			else if(type == PieceType.rook)
            {
                chessBoard[row][column] = new Rook(player, row, column);
            }
			else if(type == PieceType.queen)
            {
                chessBoard[row][column] = new Queen(player, row, column);
            }
			
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
	
	//method finds out if there are pieces between the position of this piece and another square 
    //on the board, both situated diagonally.Private because it is not used in the ChessBoard class
    public boolean clearDiagonalPath(ChessPiece piece, int finalRow, int finalColumn)
    {
        /*We have to figure out in what direction does each axis position change,
         * if it increases or decreases.*/
        int xDirection;
        int yDirection;
        
        //the row and column where the piece we want to mve is currently at
        int row = piece.getRow();
        int column = piece.getColumn();
        
        //set vertical direction
        if(row<finalRow)
        {
            xDirection=1;
        }
        else
        {
            xDirection=-1;
        }
        
        //set horizontal direction
        if(column<finalColumn)
        {
            yDirection=1;
        }
        else
        {
            yDirection=-1;
        }
        
        //initialise the coordinates used in the loop, we begin from the next square after the start position
        int currentRow = row + xDirection;
        int currentColumn = column + yDirection;
        
        //we dont check the final position for a piece
        while(currentRow!=finalRow && currentColumn!=finalColumn)
        {
            if(chessBoard[currentRow][currentColumn]!=null)
            {
                return false;
            }
            
            currentRow+=xDirection;
            currentColumn+=yDirection;
        }
        
        //if it has not returned false by now, the path is clear
        return true;
    }
    
    /*method finds out whether there are any pieces between 2 positions that are either on
     * the same row or column needed for queen and bishop moves.Public because it is also
     * used in the method for the castling move in the ChessBoard class.*/
    public boolean clearLiniarPath(ChessPiece piece, int finalRow, int finalColumn)
    {
        //the row and column indexes of the current location of the piece
        int pieceRow = piece.getRow();
        int pieceColumn = piece.getColumn();
        
        /* we find out which position has smaller coordinates*/
        int startPoint;
        int endPoint;
        
        //we find out if the line is vertical or horizontal
        if(pieceRow == finalRow && pieceColumn != finalColumn)
        {
            if(pieceColumn > finalColumn)
            {
                startPoint = finalColumn;
                endPoint= pieceColumn;
            }
            else
            {
                startPoint = pieceColumn;
                endPoint = finalColumn;
            }
            
            /*we start from the square after the initial position, and dont check the final square,
             * since enemy pieces are captured by moving on their location*/
            for(int i=startPoint+1;i<endPoint;i++)
            {
                //if we find just one piece on the way, the path is not clear
                if(chessBoard[finalRow][i]!=null)
                {
                    return false;
                }
            }
        }
        else if(pieceRow != finalRow && pieceColumn==finalColumn)
        {
            if(pieceRow>finalRow)
            {
                startPoint = finalRow;
                endPoint = pieceRow;
            }
            else
            {
                startPoint = pieceRow;
                endPoint = finalRow;
            }
            
            //since its a horizontal line, the for loop loops through different row, not columns
            for(int i=startPoint+1;i<endPoint;i++)
            {
                if(chessBoard[i][finalColumn]!=null)
                {
                    return false;
                }
            }
        }
        
        return true;
    }
    
	/*Method for the castling move. It is a move that involves moving the king 2 squares towards a rook while
	 * simultaneously moving the rook to the square over which the king passed.Method returns false if such
	 * a move is not possible, or returns true after moving the pieces if the move is possbile. It takes in 
	 * a ChessPlayer enumerated object so that it knows which player wants to make the move, and the column of the rook
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
		else if(!clearLiniarPath(getPiece(rookRow, rookColumn), rookRow, 4))
		{
			return false;
		}
		//the king must not currently be in check
		else if(isInCheck(player,rookRow,4))
		{
			return false;
		}
		
		//the king must not jump over or land over a square that is in check, these squares have
		//different columns depending on the row, so we have two else ifs
		if(rookColumn==0 && (isInCheck(player,rookRow,3) || isInCheck(player, rookRow,2)))
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
	
	/*Method finds the king of a certain player .Method used for detecting a check mate.It finds the king 
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
		/*if there is a piece that would be moved if the square would not be in check, we need to
		 * take it off the board temporarily, so that this method detects if the square at the given
		 * coordinates would in check or not after the piece moved. Now, the square might be 
		 * protected from enemy reach by the piece, but after we move the piece, the piece would
		 * be in check, because there is nothing between the enemy piece and the square you moved 
		 * the piece to.*/
		ChessPiece king=findKing(player);
		chessBoard[king.getRow()][king.getColumn()]=null;
		
		ArrayList<ChessPiece> pieceList;
		if(player == ChessPlayer.white)
		{
		    pieceList = blackPieces;
		}
		else
		{
		    pieceList = whitePieces;
		}
		
        /*
         * we go through all the pieces of the enemy and see if there is one
         * which could move to the specified square, if we find one, it is in
         * check and we return true. Before returning, we put the king back on
         * the chessboard.
         */
        for (ChessPiece piece : pieceList) 
        {
            if (piece.getType() != PieceType.pawn &&
                piece.validMove(row, column, this)) 
            {
                chessBoard[king.getRow()][king.getColumn()] = king;
                return true;
            }
            /*
             * pawns capture pieces only on the diagonal. The validPawnMove
             * method also checks that the specified square is occupied. Since
             * isInCheck also checks if empty squares would be under check, we
             * treat pawns separately, and we check if the pawn could move on
             * the diagonal forward to the square we are checking.
             */
            else if (piece.getType() == PieceType.pawn) 
            {
                Pawn pawn = (Pawn) piece;

                if (piece.getRow() + pawn.getDirection() == row &&
                    Math.abs(piece.getColumn() - column) == 1) 
                {
                    chessBoard[king.getRow()][king.getColumn()] = king;
                    return true;
                }
            }
        }
		
		//if no enemy piece can move to the square, it is not in check
		chessBoard[king.getRow()][king.getColumn()]=king;
		return false;
	}
	
	//method determines whether or not a king is in a check mate
	//parameter is a ChessPlayer so we know which king we need to check
	public boolean isCheckMate(ChessPlayer player)
	{
		//get the correct king
		ChessPiece king=findKing(player);
		chessBoard[king.getRow()][king.getColumn()]=null;
		
		//represents the squares the king can move to (or his current square),
		//that are in check
		int checkedSquares=0;
		
		/*variable for the number of squares that the king could move to,
		 * meaning squares that are not occupied by pieces of the same team.
		 * Initialised as 1 for the square the king is currently on.*/
		int possibleSquares=1;
		
		//see if the square the king is on is in check
		if(isInCheck(king.getPlayer(), king.getRow(), king.getColumn()))
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
		if(king.validMove(king.getRow()+1,king.getColumn(), this))
		{
			possibleSquares++;
			if(isInCheck(king.getPlayer(), king.getRow()+1, king.getColumn()))
			{
				checkedSquares++;
			}
		}
		
		//see if the square directly below the king is in check
		if(king.validMove(king.getRow()-1,king.getColumn(), this))
		{
			possibleSquares++;
			if(isInCheck(king.getPlayer(), king.getRow()-1, king.getColumn()))
			{
				checkedSquares++;
			}
		}
		
		//see if the square directly to the left of the king is in check
		if(king.validMove(king.getRow(),king.getColumn()-1, this))
		{
			possibleSquares++;
			if(isInCheck(king.getPlayer(), king.getRow(), king.getColumn()-1))
			{
				checkedSquares++;
			}
		}
		
		//see if the square directly to the right of the the king is in check
		if(king.validMove(king.getRow(),king.getColumn()+1, this))
		{
			possibleSquares++;
			if(isInCheck(king.getPlayer(), king.getRow(), king.getColumn()+1))
			{
				checkedSquares++;
			}
		}
		
		//see if the square directly to the bottom left of the king is in check
		if(king.validMove(king.getRow()-1,king.getColumn()-1, this))
		{
			possibleSquares++;
			if(isInCheck(king.getPlayer(),king.getRow()-1,king.getColumn()-1))
			{
				checkedSquares++;
			}
		}
		
		//see if the square directly to the top left of the king is in check
		if(king.validMove(king.getRow()+1,king.getColumn()-1, this))
		{
			possibleSquares++;
			if(isInCheck(king.getPlayer(),king.getRow()+1, king.getColumn()-1))
			{
				checkedSquares++;
			}
		}
		
		//see if the square directly to the top right of the king is in check
		if(king.validMove(king.getRow()+1,king.getColumn()+1, this))
		{
			possibleSquares++;
			if(isInCheck(king.getPlayer(),king.getRow()+1,king.getColumn()+1))
			{
				checkedSquares++;
			}
		}
		
		//see if the square directly to the bottom right of the king is in check
		if(king.validMove(king.getRow()-1,king.getColumn()+1, this))
		{
			possibleSquares++;
			if(isInCheck(king.getPlayer(),king.getRow()-1,king.getColumn()+1))
			{
				checkedSquares++;
			}
		}
	
		chessBoard[king.getRow()][king.getColumn()]=king;
		//if all squares the king can move to are in check, the player is checkmated.
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
		if (king.validMove(king.getRow() + 1, king.getColumn(), this))
		{
			possibleSquares++;
			if (isInCheck(king.getPlayer(), king.getRow() + 1, king.getColumn()))
			{
				checkedSquares++;
			}
		}

		// see if the square directly below the king is in check
		if (king.validMove(king.getRow() - 1, king.getColumn(), this))
		{
			possibleSquares++;
			if (isInCheck(king.getPlayer(), king.getRow() - 1, king.getColumn()))
			{
				checkedSquares++;
			}
		}

		// see if the square directly to the left of the king is in check
		if (king.validMove(king.getRow(), king.getColumn() - 1, this))
		{
			possibleSquares++;
			if (isInCheck(king.getPlayer(), king.getRow(), king.getColumn() - 1))
			{
				checkedSquares++;
			}
		}

		// see if the square directly to the right of the the king is in check
		if (king.validMove(king.getRow(), king.getColumn() + 1, this))
		{
			possibleSquares++;
			if (isInCheck(king.getPlayer(), king.getRow(), king.getColumn() + 1))
			{
				checkedSquares++;
			}
		}

		// see if the square directly to the bottom left of the king is in check
		if (king.validMove(king.getRow() - 1, king.getColumn() - 1, this))
		{
			possibleSquares++;
			if (isInCheck(king.getPlayer(), king.getRow() - 1, king.getColumn() - 1))
			{
				checkedSquares++;
			}
		}

		// see if the square directly to the top left of the king is in check
		if (king.validMove(king.getRow() + 1, king.getColumn() - 1, this))
		{
			possibleSquares++;
			if (isInCheck(king.getPlayer(), king.getRow() + 1, king.getColumn() - 1))
			{
				checkedSquares++;
			}
		}

		// see if the square directly to the top right of the king is in check
		if (king.validMove(king.getRow() + 1, king.getColumn() + 1, this))
		{
			possibleSquares++;
			if (isInCheck(king.getPlayer(), king.getRow() + 1, king.getColumn() + 1))
			{
				checkedSquares++;
			}
		}

		// see if the square directly to the bottom right of the king is in check
		if (king.validMove(king.getRow() - 1, king.getColumn() + 1, this))
		{
			possibleSquares++;
			if (isInCheck(king.getPlayer(), king.getRow() - 1, king.getColumn() + 1))
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
	
}
