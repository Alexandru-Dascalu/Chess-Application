/**
 * Class models a king chess piece object. A king can move in any direction, but
 * only one square at a time, however many spaces, and can not jump over other pieces.
 * @author Alexandru Dascalu
 * @version 1.0
 */
public class King extends ChessPiece
{
    /**
     * Creates a new King chess piece object, ready to be used in the beginning
     * of the game.
     * @param player The player (or color) of this chess piece.
     * @param row The starting row position of the chess piece.
     * @param column The starting column position of the chess piece.
     */
    public King(ChessPlayer player, int row, int column)
    {
        super(player, row, column);
        pieceType = PieceType.king;
    }
    
    /**
     * Check if a move of this piece to the square at the given coordinates of 
     * the given chess board is valid or not, according to chess rules and 
     * depending on the type of the chess piece. This method makes sure a 
     * king can only move one space, in any direction.
     * @param row The starting row of this piece.
     * @param column The starting column of this piece.
     * @param board The chess board this chess piece is on.
     * @return True if the move is valid, false if not.
     */
    public boolean validMove(int row, int column, ChessBoard board)
    {
        /*if the move would not be valid for any chess pieces, it is not valid.
         * Else, go on with other checks.*/
        if(!super.validMove(row, column, board))
        {
            return false;
        }
        
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
    
    /**Gives a string abbreviation of this chess piece, showing its type and 
     * colour.
     * @return an abbreviation of this chess piece, showing its type and colour.*/
    @Override
    public String toString()
    {
        return (super.toString() + "K");
    }
}
