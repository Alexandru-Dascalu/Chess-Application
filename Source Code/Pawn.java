
public class Pawn extends ChessPiece
{
    /**A flag used to ensure validity of chess piece moves. True if the pawn
     * has moved 2 spaces in the previous move.*/
    private boolean moved2Spaces;
    
    /**On a chessboard, for the white player, moving ahead increases the number
     *of the row. For the black player, moving ahead decreases the number of 
     *the row, since black pieces start from row 8 or 7. Since pawns can only 
     *move ahead, we need to know what ahead means. Hence, if its black, the 
     *direction is -1 so moving ahead decreases the row. If its white, direction is 1.*/
    private final int direction;
    
    /*Constructor has parameters that specify the piece's type, team, and position*/
    public Pawn(PieceType type, ChessPlayer player, int row, int column)
    {
        super(type, player, row, column);
        if(player==ChessPlayer.white)
        {
            direction=1;
        }
        else
        {
            direction=-1;
        }
        
        moved2Spaces=false;
    }
    
    /**
     * Gets an integer representing the direction this pawn has to move along 
     * relative to the chess board.
     * @return 1 if the pawn is white and moves ahead (thus into larger rows),
     * -1 if it is black and thus moves ahead by going to smaller rows.*/
    public int getDirection()
    {
        return direction;
    }
    
    /**Sets the has moved flag to a new value.
     * @param move the new value of wether or not this pawn has moved 2 spaces 
     * in the previoues move.*/
    public void setMoved2Spaces(boolean move)
    {
        moved2Spaces=move;
    }
    
    /**Gets the value of the flag telling you if the pawn has just moved by 2 
     * spaces.
     * @return true if this pawn has moved by 2 spaces in the last move, false 
     * if not.*/
    public boolean hasMoved2Spaces()
    {
        return moved2Spaces;
    }
    
    @Override
    public boolean validMove(int row, int column, ChessPiece[][] board)
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
        /*en Passant movement, the space one square on the diagonal needs to be
         * empty and next to your piece there needs to be a pawn that has just 
         * moved 2 spaces, and that is of the opposite team.*/
        else if (this.row+direction==row && Math.abs(column-this.column)==1 
                && board[row][column]==null && board[row-direction][column]!=null)
        {
            /*Check if the piece next to this pawn is also a pawn. If not, the
             * move is not valid.*/
            if(board[row-direction][column].getClass() != Pawn.class)
            {
                return false;
            }
            /*Else, procced with final checks to see if the move is valid.*/
            else
            {
                Pawn enemyPawn = (Pawn)board[row-direction][column];
                
                /*The pawn next to this pawn needs to be an enemy pawn and to 
                 * have just moved 2 spaces.*/
                if(enemyPawn.hasMoved2Spaces() && player!=enemyPawn.getPlayer())
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
        }
        //otherwise, its an invalid move
        else
        {
            return false;
        }
    }
    
    /**Checks if this chess piece can be promoted to a more powerful one.
     * @return true if the chess piece can be promoted, false if not.*/
    @Override
    public boolean canBePromoted()
    {
       /*A pawn can only be promoted if it has reached the other end of the
        * board, opposite from where they started. White pawns need to get to
        * row 7, and black pawns need to get to row 0.*/
       if(player==ChessPlayer.white && this.row==7)
       {
           return true;
       }
       else if(player==ChessPlayer.black && this.row==0)
       {
           return true;
       }
       else
       {
           return false;
       }
    }
    
    /**Gives a string abreviation of this chess piece, showing its type and 
     * colour.
     * @return an abreviation of this chess piece, showing its type and colour.*/
    @Override
    public String toString()
    {
        return (super.toString() + "P");
    }
}
