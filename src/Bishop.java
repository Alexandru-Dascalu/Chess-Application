/**
 * Class models a knight chess piece object. A bishop can only move diagonally,
 * however many spaces, and can not jump over other pieces.
 * @author Alexandru Dascalu
 * @version 1.0
 */
public class Bishop extends ChessPiece 
{
    /**
     * Creates a new Bishop chess piece object, ready to be used in the beginning
     * of the game.
     * @param player The player (or color) of this chess piece.
     * @param row The starting row position of the chess piece.
     * @param column The starting column position of the chess piece.
     */
    public Bishop(ChessPlayer player, int row, int column)
    {
        super(player, row, column);
        pieceType = PieceType.bishop;
    }
    
    /**
     * Check if a move of this piece to the square at the given coordinates of 
     * the given chess board is valid or not, according to chess rules and 
     * depending on the type of the chess piece. This method makes sure a 
     * bishop can only move , however many spaces.
     * @param row The starting row of this piece.
     * @param column The starting column of this piece.
     * @param board The chess board this chess piece is on.
     * @return True if the mov is valid, false if not.
     */
    @Override
    public boolean validMove(int row, int column, ChessBoard board)
    {
        if(!super.validMove(row, column, board))
        {
            return false;
        }
        
       //bishops can move diagonally on multiple squares, but cant jump over a piece
        if(Math.abs(this.column-column)==Math.abs(this.row-row) && 
                board.clearDiagonalPath(this, row, column))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    /**Gives a string abbreviation of this chess piece, showing its type and 
     * colour.
     * @return an abbreviation of this chess piece, showing its type and colour.*/
    @Override
    public String toString()
    {
        return (super.toString() + "B");
    }
}
