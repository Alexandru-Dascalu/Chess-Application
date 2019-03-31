/**
 * Class models a king chess piece object. A queen can move in any direction,
 * however many spaces, and can not jump over other pieces.
 * @author Alexandru Dascalu
 * @version 1.0
 */
public class Queen extends ChessPiece
{
    /**
     * Creates a new Queen chess piece object, ready to be used in the beginning
     * of the game.
     * @param player The player (or color) of this chess piece.
     * @param row The starting row position of the chess piece.
     * @param column The starting column position of the chess piece.
     */
    public Queen(ChessPlayer player, int row, int column)
    {
        super(player, row, column);
        pieceType = PieceType.queen;
    }
    
    /**
     * Check if a move of this piece to the square at the given coordinates of 
     * the given chess board is valid or not, according to chess rules and 
     * depending on the type of the chess piece. This method makes sure a 
     * queen can only move linearly or diagonally, however many spaces.
     * @param row The starting row of this piece.
     * @param column The starting column of this piece.
     * @param board The chess board this chess piece is on.
     * @return True if the move is valid, false if not.
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
        
        //a queen can move horizontally however many spaces, but it cant jump over a piece
        if(this.row==row && board.clearLiniarPath(this, row, column))
        {
            return true;
        }
        // a queen can move vertically  however many spaces, but it cant jump over a piece
        else if(this.column==column && board.clearLiniarPath(this, row, column))
        {
            return true;
        }
        // a queen can move diagonally however many spaces, but cant jump over another piece
        else if(Math.abs(this.row-row)==Math.abs(this.column-column) && 
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
        return (super.toString() + "Q");
    }
}
