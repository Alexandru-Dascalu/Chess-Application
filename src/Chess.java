import java.util.Scanner;
public class Chess 
{
	public static void main(String[] args)
	{
		ChessBoard board= new ChessBoard();
		Scanner kb=new Scanner(System.in);
		boolean cont=true;
		boolean nextPlayer=true;
		ChessPlayer currentPlayer=ChessPlayer.black;
		
		board.printChessBoard();
		
		//System.out.println(board.isCheckMate(ChessPlayer.white));
		while (cont)
		{
			if(nextPlayer)
			{
				if(currentPlayer==ChessPlayer.white)
				{
					currentPlayer=ChessPlayer.black;
				}
				else
				{
					currentPlayer=ChessPlayer.white;
				}
			}
			
			if(currentPlayer==ChessPlayer.white)
			{
				System.out.println("It is the turn of the white player.");
			}
			else
			{
				System.out.println("It is the turn of the black player.");
			}
	
			System.out.println("Select an option from below:\n"+
					"1-Move piece.\n"+"2-Castling move.\n"+
					"3-Promote pawn.\n"+"4-Display Legend.\n"+
					"5-Quit.\n");
			
			int answer;
			
			do
			{
				System.out.println("Type in a number between 1 and 5, which corresponds with an option.");
				while(!kb.hasNextInt())
				{
					System.out.println("You must type in an integer.");
					kb.next();
				}
				answer=kb.nextInt();			
			}
			while(answer<1 || answer>5);
			
			kb.nextLine();
			switch(answer)
			{
				case 1:
					boolean repeat=true;
					do
					{
						System.out.println("Type in the coordinates of the piece you want to move.");
						int row=readRow();
						char column=readColumn();
						
						ChessPiece chosenPiece=board.getPiece(row-1,charToInt(column)-1);
						if(chosenPiece==null || chosenPiece.getPlayer()!=currentPlayer)
						{
							System.out.println("There is no piece of yours at the given coordinates!");
							repeat=true;
						}
						else
						{
							System.out.println("Type in where you want to move the piece.");
							int finalRow=readRow();
							char finalColumn=readColumn();
							
							
							if(board.movePiece(board.getPiece(row-1,charToInt(column)-1),finalRow-1,charToInt(finalColumn)-1))
							{
								System.out.println("Move successful!");
								repeat=false;
							}
							else
							{
								System.out.println("Invalid move.");
								repeat=true;
							}
						}
					}
					while(repeat);
					nextPlayer=true;
					break;
				case 2:
					System.out.println("Type in the column of the rook you want to use.");
					char rookColumn=readColumn();
					
					if(board.castling(currentPlayer,charToInt(rookColumn)-1))
					{
						System.out.println("Move successful!");
						nextPlayer=true;
					}
					else
					{
						System.out.println("Invalid move.");
						nextPlayer=false;
					}
					break;
				case 3:
					String piece;
					do
					{
						System.out.println("Type in what type of piece you want to upgrade to. First letter must be uppercase.");
						piece=kb.nextLine();
					}
					while(!piece.equals("Queen") && !piece.equals("Bishop") &&
							!piece.equals("Knight") && !piece.equals("Rook"));
					PieceType pieceType=PieceType.pawn;
					
					switch (piece)
					{
						case "Queen":
							pieceType=PieceType.queen;
							break;
						case "Rook":
							pieceType=PieceType.rook;
							break;
						case "Bishop":
							pieceType=PieceType.bishop;
							break;
						case "Knight":
							pieceType=PieceType.knight;
							break;
					}
					System.out.println("Type in the coordinates of the pawn you want to promote.");
					int row=readRow();
					char column=readColumn();
					
					if(board.promotePawn(pieceType, row-1, charToInt(column)-1))
					{
						System.out.println("Promotion was successful.");
						nextPlayer=true;
					}
					else
					{
						System.out.println("You can not promote the piece at those coordinates! Invalid move");
						nextPlayer=false;
					}
					break;
				case 4:
					printLegend();
					nextPlayer=false;
					break;
				case 5:
					System.out.println("Are you sure? You will lose the macth if you quit.");
					String yesNo;
					
					kb.nextLine();
					do
					{
						System.out.println("Yes or no.");
						yesNo=kb.nextLine();
					}
					while(!yesNo.equalsIgnoreCase("yes") && !yesNo.equalsIgnoreCase("no"));
					
					if(yesNo.equalsIgnoreCase("yes"))
					{
						cont=false;
						String winner=(currentPlayer==ChessPlayer.white) ? "black": "white";
						System.out.println("The winner is the "+winner+" player!");
					}
					else
					{
						nextPlayer=false;
					}
					break;
			}
			
			board.printChessBoard();
			if (board.isStalemate(ChessPlayer.white))
			{
				System.out.println("The white player is in a stalemate. The game is a draw.");
				cont = false;
			} 
			else if (board.isStalemate(ChessPlayer.black))
			{
				System.out.println("The black player is in a stalemate. The game is a draw.");
				cont = false;
			} 
			else if (board.isCheckMate(ChessPlayer.white))
			{
				System.out.println("White player is checkmated. Black player wins.");
				cont = false;
			} 
			else if (board.isCheckMate(ChessPlayer.black))
			{
				System.out.println("Black player is checkmated. White player wins.");
				cont = false;
			}
		}
		kb.close();
	}
	
	public static int readRow()
	{
		Scanner kb=new Scanner(System.in);
		System.out.println("Type in row:");
		int row;
		do
		{
			System.out.println("Type in a number from 1 to 8.");
			while(!kb.hasNextInt())
			{
				System.out.println("You must type in an integer.");
				kb.next();
			}
			row=kb.nextInt();
		}
		while(row<1 || row>8);
		return row;
	}
	
	public static char readColumn()
	{
		Scanner kb=new Scanner(System.in);
		System.out.println("Type in Column");
		String column;
		do
		{
			System.out.println("Type in a letter from a to h.");
			column=kb.nextLine();
		}
		while(column.length()!=1 || column.charAt(0)<97 || column.charAt(0)>104);
		return column.charAt(0);
	}
	
	//method takes a char and converts to an associated integer, a becomes 1, b be becomes 2 ,and so on
	//real chess boards note columns with letters so this method is necessary
	public static int charToInt(char letter)
	{
		return (int)(letter)-96;
	}
	
	public static void printLegend()
	{
		System.out.println("WP-White pawn.");
		System.out.println("WR-White rook.");
		System.out.println("WKN-White knight.");
		System.out.println("WB-White bishop.");
		System.out.println("WK-White king.");
		System.out.println("WQ-White queen.\n");
		
		System.out.println("BP-Black pawn.");
		System.out.println("BR-Black rook.");
		System.out.println("BKN-Black knight.");
		System.out.println("BB-Black bishop.");
		System.out.println("BK-Black king.");
		System.out.println("BQ-Black queen.\n");
	}
}
