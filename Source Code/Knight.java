
public class Knight extends ChessPiece
{
    /*Constructor has parameters that specify the piece's type, team, and position*/
    public Knight(PieceType type, ChessPlayer player, int row, int column)
    {
        super(type, player, row, column);
    }
    
    /*method determines whether a move is valid for a knight, no ChessPiece[][] parameter
     * because the knight jumps over pieces so we dont check if the path to its final 
     * position is clear*/
    @Override
    public boolean validMove(int row, int column,ChessPiece[][] board)
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
    
    /**Gives a string abreviation of this chess piece, showing its type and 
     * colour.
     * @return an abreviation of this chess piece, showing its type and colour.*/
    @Override
    public String toString()
    {
        return (super.toString() + "KN");
    }
}
