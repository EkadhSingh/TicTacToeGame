package threeInARowGame;

public class Main {

	public static void main(String[] args) {
		Boards boards = new Boards();
		boards.create495Boards();
		boards.create1820Boards();
		boards.create900900Boards();
		boards.whosTurn();
		boards.firstRound();
		boards.nextRoundsToRun();
		//in the next line, the first 4 numbers to enter are the positions of the x pieces, with 1 being the top left, 4 being the top right, 13 being the bottom left, and 16 being the bottom right. The last 4 numbers to enter are the positions of the O pieces.
		int[] boardToCheck = {10,3,13,11,9,7,15,4};
		//For the output the first 4 numbers should be the position of the x pieces, the next 4 the position of the o pieces, the 9th number should be who's turn it is to move, with 0 being x and 1 being o, and the 10th number in the output is who's winning this position, with 0 meaning it's a draw, 1 meaning x has a guaranteed win, 2 meaning o has a guaranteed win, and 3 meaning the position is impossible as both pieces have 3 in a row.
		boards.print900900Boards(boards.peiceSpotsToBoardNumber(boardToCheck[0],boardToCheck[1],boardToCheck[2],boardToCheck[3],boardToCheck[4],boardToCheck[5],boardToCheck[6],boardToCheck[7]));
		//If one side is winning and it is there move, this will print all of the possible moves (or technically, all of the possible boards they can go) to win.
		boards.bestMoves(boards.peiceSpotsToBoardNumber(boardToCheck[0],boardToCheck[1],boardToCheck[2],boardToCheck[3],boardToCheck[4],boardToCheck[5],boardToCheck[6],boardToCheck[7]));
		boards.print900900Boards(425040);
	}
}