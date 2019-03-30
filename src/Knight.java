/**
 * Class models a knight chess piece object. A knight can only move in L shaped
 * jumps and can jump over other pieces.
 * @author Alexandru Dascalu
 * @version 1.0
 */
public class Knight extends ChessPiece
{
    /**
     * Creates a new Pawn chess piece object, ready to be used in the beginning
     * of the game.
     * @param player The player (or color) of this chess piece.
     * @param row The starting row position of the chess piece.
     * @param column The starting column position of the chess piece.
     */
    public Knight(ChessPlayer player, int row, int column)
    {
        super(player, row, column);
        pieceType = PieceType.knight;
    }
    
    /**
     * Check if a move of this piece to the square at the given coordinates of 
     * the given chess board is valid or not, according to chess rules and 
     * depending on the type of the chess piece. This method makes sure a 
     * knight can only an L move, and that it jumps over other chess pieces.
     * @param row The starting row of this piece.
     * @param column The starting column of this piece.
     * @param board The chess board this chess piece is on.
     * @return True if the mov is valid, false if not.
     */
    @Override
    public boolean validMove(int row, int column, ChessBoard board)
    {
        /*if the move would not be valid for any chess pieces, it is not valid.
         * Else, go on with other checks.*/
        if(!super.validMove(row, column, board))
        {
            return false;
        }
        
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
    
    /**Gives a string abbreviation of this chess piece, showing its type and 
     * colour.
     * @return an abbreviation of this chess piece, showing its type and colour.*/
    @Override
    public String toString()
    {
        return (super.toString() + "KN");
    }
}
