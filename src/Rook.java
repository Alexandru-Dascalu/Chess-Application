/**
 * Class models a rook chess piece object. A rook can only move linearly,
 * however many spaces, and can not jump over other pieces.
 * @author Alexandru Dascalu
 * @version 1.0
 */
public class Rook extends ChessPiece
{
    /**
     * Creates a new Bishop chess piece object, ready to be used in the beginning
     * of the game.
     * @param player The player (or color) of this chess piece.
     * @param row The starting row position of the chess piece.
     * @param column The starting column position of the chess piece.
     */
    public Rook(ChessPlayer player, int row, int column)
    {
        super(player, row, column);
        pieceType = PieceType.rook;
    }
    
    /**
     * Check if a move of this piece to the square at the given coordinates of 
     * the given chess board is valid or not, according to chess rules and 
     * depending on the type of the chess piece. This method makes sure a 
     * rook can only move linearly, however many spaces.
     * @param row The starting row of this piece.
     * @param column The starting column of this piece.
     * @param board The chess board this chess piece is on.
     * @return True if the move is valid, false if not.
     */
    @Override
    public boolean validMove (int row, int column, ChessBoard board)
    {
        /*if the move would not be valid for any chess pieces, it is not valid.
         * Else, go on with other checks.*/
        if(!super.validMove(row, column, board))
        {
            return false;
        }
        
        //rooks can move horizontally and cant jump over a piece
        if(this.row==row && board.clearLiniarPath(this, row, column))
        {
            return true;
        }
        //rooks can move vertically and cant jump over a piece
        else if(this.column==column && board.clearLiniarPath(this, row, column))
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
        return (super.toString() + "R");
    }
}
