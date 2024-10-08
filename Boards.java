package threeInARowGame;
import java.util.Arrays;

public class Boards {
	//the first 8 bytes of each code say what square each piece is in, with the first 4 being for x and the next for being for o. slot 8 says who's turn it is, with 0 being x and 1 being o. slot 9 says what the result of the position is, with 0 being undetermined (or a draw), 1 being x wins, 2 being o wins, and 3 being impossible position. If the position is winning for either x or o, slot 10 says how many more moves have to be made for the side that is winning to get three in a row assuming both sides play optimally (each sides move is counted as one move, so if it is X to move and they have to make a move, then O makes a move, then X makes a move and wins, this slot should say 3)
	public byte[][] boards900900 = new byte[11][900900];
	private byte[][] boards1820 = new byte[4][1820];
	private byte[][] boards495 = new byte[4][495];
	private short[] search1820 = new short[28561];
	private short[] search495 = new short[6561];
	private int boardsChanged;
	boolean x3 = false;
	boolean y3 = false;
	public Boards() {
		create495Boards();
		create1820Boards();
		create900900Boards();
		whosTurn();
		firstRound();
		nextRoundsToRun();
		howManyMovesToWinToRun();
	}
	private int[] findPuzzles(int lengthToFind) {
		int totalPuzzleCount = 0;
		for (int i=0;i<900900;i++) {
			if (boards900900[10][i]==lengthToFind) {
				totalPuzzleCount++;
			}
		}
		int currentPuzzleAddingToArray = 0;
		int[] puzzles = new int[totalPuzzleCount];
		for (int i=0;i<900900;i++) {
			if (boards900900[10][i]==lengthToFind) {
				puzzles[currentPuzzleAddingToArray]=i;
				currentPuzzleAddingToArray++;
			}
		}
		return puzzles;
	}
	public void printPuzzles(int lengthToFind) {
		for (int puzzle : findPuzzles(lengthToFind)) {
			System.out.println(puzzle);
		}
	}
	private void howManyMovesToWinToRun() {
		while (true) {
			boolean anyUnsolvedProblems = false;
			for (int i=0;i<900900;i++) {
				if (boards900900[10][i]>=100||(boards900900[9][i]!=0&&boards900900[10][i]<-1)) {
					anyUnsolvedProblems=true;
				}
			}
			if (anyUnsolvedProblems==true) {
				howManyMovesToWin();
			}
			else {
				break;
			}
		}
		for(int i=0;i<900900;i++) {
			if (boards900900[9][i]==0) {
				boards900900[10][i]=0;	
			}
		}
	}
	private void howManyMovesToWin() {
		int[] boards1d = new int[8];
		for (int i=0;i<900900;i++) {
			anyThreeInRows(i);
			if (x3==false&&y3==false) {
				boards1d[0]=boards900900[0][i];
				boards1d[1]=boards900900[1][i];
				boards1d[2]=boards900900[2][i];
				boards1d[3]=boards900900[3][i];
				boards1d[4]=boards900900[4][i];
				boards1d[5]=boards900900[5][i];
				boards1d[6]=boards900900[6][i];
				boards1d[7]=boards900900[7][i];
				int[] possibleBoards = new int[14];
				byte possibleBoardNumber=0;
				boolean[] filled = new boolean[16];
				for (int j=0;j<8;j++) {
					filled[boards1d[j]-1]=true;
				}
				if (boards900900[8][i]==0) {
					for (int j=0;j<4;j++) {
						
						if (boards1d[j]>4) {
							boards1d[j]-=4;
							if (filled[boards1d[j]-1]==false) {
								possibleBoards[possibleBoardNumber]=peiceSpotsToBoardNumber(boards1d[0],boards1d[1],boards1d[2],boards1d[3],boards1d[4],boards1d[5],boards1d[6],boards1d[7]);
								possibleBoardNumber++;
							}
							boards1d[j]+=4;
						}
						if (boards1d[j]!=1&&boards1d[j]!=5&&boards1d[j]!=9&&boards1d[j]!=13) {
							boards1d[j]-=1;
							if (filled[boards1d[j]-1]==false) {
								possibleBoards[possibleBoardNumber]=peiceSpotsToBoardNumber(boards1d[0],boards1d[1],boards1d[2],boards1d[3],boards1d[4],boards1d[5],boards1d[6],boards1d[7]);
								possibleBoardNumber++;
							}
							boards1d[j]+=1;
						}
						if (boards1d[j]<13) {
							boards1d[j]+=4;
							if (filled[boards1d[j]-1]==false) {
								possibleBoards[possibleBoardNumber]=peiceSpotsToBoardNumber(boards1d[0],boards1d[1],boards1d[2],boards1d[3],boards1d[4],boards1d[5],boards1d[6],boards1d[7]);
								possibleBoardNumber++;
							}
							boards1d[j]-=4;
						}
						if (boards1d[j]!=16&&boards1d[j]!=12&&boards1d[j]!=8&&boards1d[j]!=4) {
							boards1d[j]+=1;
							if (filled[boards1d[j]-1]==false) {
								possibleBoards[possibleBoardNumber]=peiceSpotsToBoardNumber(boards1d[0],boards1d[1],boards1d[2],boards1d[3],boards1d[4],boards1d[5],boards1d[6],boards1d[7]);
								possibleBoardNumber++;
							}
							boards1d[j]-=1;
						}
					}
				}
				else {
					for (int j=4;j<8;j++) {
						
						if (boards1d[j]>4) {
							boards1d[j]-=4;
							if (filled[boards1d[j]-1]==false) {
								possibleBoards[possibleBoardNumber]=peiceSpotsToBoardNumber(boards1d[0],boards1d[1],boards1d[2],boards1d[3],boards1d[4],boards1d[5],boards1d[6],boards1d[7]);
								possibleBoardNumber++;
							}
							boards1d[j]+=4;
						}
						if (boards1d[j]!=1&&boards1d[j]!=5&&boards1d[j]!=9&&boards1d[j]!=13) {
							boards1d[j]-=1;
							if (filled[boards1d[j]-1]==false) {
								possibleBoards[possibleBoardNumber]=peiceSpotsToBoardNumber(boards1d[0],boards1d[1],boards1d[2],boards1d[3],boards1d[4],boards1d[5],boards1d[6],boards1d[7]);
								possibleBoardNumber++;
							}
							boards1d[j]+=1;
						}
						if (boards1d[j]<13) {
							boards1d[j]+=4;
							if (filled[boards1d[j]-1]==false) {
								possibleBoards[possibleBoardNumber]=peiceSpotsToBoardNumber(boards1d[0],boards1d[1],boards1d[2],boards1d[3],boards1d[4],boards1d[5],boards1d[6],boards1d[7]);
								possibleBoardNumber++;
							}
							boards1d[j]-=4;
						}
						if (boards1d[j]!=16&&boards1d[j]!=12&&boards1d[j]!=8&&boards1d[j]!=4) {
							boards1d[j]+=1;
							if (filled[boards1d[j]-1]==false) {
								possibleBoards[possibleBoardNumber]=peiceSpotsToBoardNumber(boards1d[0],boards1d[1],boards1d[2],boards1d[3],boards1d[4],boards1d[5],boards1d[6],boards1d[7]);
								possibleBoardNumber++;
							}
							boards1d[j]-=1;
						}
					}
				}

				if (boards900900[8][i]==0&&boards900900[9][i]==2) {
					byte fastestWin = 0;
					for (int j=0;j<possibleBoardNumber;j++) {
						if (boards900900[10][possibleBoards[j]]>fastestWin&&boards900900[10][possibleBoards[j]]>=0) {
							fastestWin=boards900900[10][possibleBoards[j]];
						}
					}
					fastestWin++;
					boards900900[10][i]=fastestWin;
				}
				if (boards900900[8][i]==1&&boards900900[9][i]==1) {
					byte fastestWin = 0;
					for (int j=0;j<possibleBoardNumber;j++) {
						if (boards900900[10][possibleBoards[j]]>fastestWin&&boards900900[10][possibleBoards[j]]>=0) {
							fastestWin=boards900900[10][possibleBoards[j]];
						}
					}
					fastestWin++;
					boards900900[10][i]=fastestWin;
				}
				if (boards900900[8][i]==0&&boards900900[9][i]==1) {
					byte fastestWin = 100;
					for (int j=0;j<possibleBoardNumber;j++) {
						if (boards900900[9][possibleBoards[j]]==1&&boards900900[10][possibleBoards[j]]<fastestWin&&boards900900[10][possibleBoards[j]]>=0) {
							fastestWin=boards900900[10][possibleBoards[j]];
						}
					}
					fastestWin++;
					boards900900[10][i]=fastestWin;
				}
				if (boards900900[8][i]==1&&boards900900[9][i]==2) {
					byte fastestWin = 100;
					for (int j=0;j<possibleBoardNumber;j++) {
						if (boards900900[9][possibleBoards[j]]==2&&boards900900[10][possibleBoards[j]]<fastestWin&&boards900900[10][possibleBoards[j]]>=0) {
							fastestWin=boards900900[10][possibleBoards[j]];
						}
					}
					fastestWin++;
					boards900900[10][i]=fastestWin;
				}
			}
		}
	}
	public void howManyUnsolvedBoards() {
		int a=0;
		for (int i=0;i<900900;i++) {
			if (boards900900[9][i]==0) {
				a++;
			}
		}
		System.out.println("the amount of boards without a solution is "+a);
	}
	
	private int[] findPossibleMoves(int boardNumber) {
		int[] boards1d = new int[8];
		boards1d[0]=boards900900[0][boardNumber];
		boards1d[1]=boards900900[1][boardNumber];
		boards1d[2]=boards900900[2][boardNumber];
		boards1d[3]=boards900900[3][boardNumber];
		boards1d[4]=boards900900[4][boardNumber];
		boards1d[5]=boards900900[5][boardNumber];
		boards1d[6]=boards900900[6][boardNumber];
		boards1d[7]=boards900900[7][boardNumber];
		int[] possibleBoards = new int[14];
		byte possibleBoardNumber=0;
		boolean[] filled = new boolean[16];
		for (int j=0;j<8;j++) {
			filled[boards1d[j]-1]=true;
		}
		if (boards900900[8][boardNumber]==0) {
			for (int j=0;j<4;j++) {
				
				if (boards1d[j]>4) {
					boards1d[j]-=4;
					if (filled[boards1d[j]-1]==false) {
						possibleBoards[possibleBoardNumber]=peiceSpotsToBoardNumber(boards1d[0],boards1d[1],boards1d[2],boards1d[3],boards1d[4],boards1d[5],boards1d[6],boards1d[7]);
						possibleBoardNumber++;
					}
					boards1d[j]+=4;
				}
				if (boards1d[j]!=1&&boards1d[j]!=5&&boards1d[j]!=9&&boards1d[j]!=13) {
					boards1d[j]-=1;
					if (filled[boards1d[j]-1]==false) {
						possibleBoards[possibleBoardNumber]=peiceSpotsToBoardNumber(boards1d[0],boards1d[1],boards1d[2],boards1d[3],boards1d[4],boards1d[5],boards1d[6],boards1d[7]);
						possibleBoardNumber++;
					}
					boards1d[j]+=1;
				}
				if (boards1d[j]<13) {
					boards1d[j]+=4;
					if (filled[boards1d[j]-1]==false) {
						possibleBoards[possibleBoardNumber]=peiceSpotsToBoardNumber(boards1d[0],boards1d[1],boards1d[2],boards1d[3],boards1d[4],boards1d[5],boards1d[6],boards1d[7]);
						possibleBoardNumber++;
					}
					boards1d[j]-=4;
				}
				if (boards1d[j]!=16&&boards1d[j]!=12&&boards1d[j]!=8&&boards1d[j]!=4) {
					boards1d[j]+=1;
					if (filled[boards1d[j]-1]==false) {
						possibleBoards[possibleBoardNumber]=peiceSpotsToBoardNumber(boards1d[0],boards1d[1],boards1d[2],boards1d[3],boards1d[4],boards1d[5],boards1d[6],boards1d[7]);
						possibleBoardNumber++;
					}
					boards1d[j]-=1;
				}
			}
		}
		else {
			for (int j=4;j<8;j++) {
				
				if (boards1d[j]>4) {
					boards1d[j]-=4;
					if (filled[boards1d[j]-1]==false) {
						possibleBoards[possibleBoardNumber]=peiceSpotsToBoardNumber(boards1d[0],boards1d[1],boards1d[2],boards1d[3],boards1d[4],boards1d[5],boards1d[6],boards1d[7]);
						possibleBoardNumber++;
					}
					boards1d[j]+=4;
				}
				if (boards1d[j]!=1&&boards1d[j]!=5&&boards1d[j]!=9&&boards1d[j]!=13) {
					boards1d[j]-=1;
					if (filled[boards1d[j]-1]==false) {
						possibleBoards[possibleBoardNumber]=peiceSpotsToBoardNumber(boards1d[0],boards1d[1],boards1d[2],boards1d[3],boards1d[4],boards1d[5],boards1d[6],boards1d[7]);
						possibleBoardNumber++;
					}
					boards1d[j]+=1;
				}
				if (boards1d[j]<13) {
					boards1d[j]+=4;
					if (filled[boards1d[j]-1]==false) {
						possibleBoards[possibleBoardNumber]=peiceSpotsToBoardNumber(boards1d[0],boards1d[1],boards1d[2],boards1d[3],boards1d[4],boards1d[5],boards1d[6],boards1d[7]);
						possibleBoardNumber++;
					}
					boards1d[j]-=4;
				}
				if (boards1d[j]!=16&&boards1d[j]!=12&&boards1d[j]!=8&&boards1d[j]!=4) {
					boards1d[j]+=1;
					if (filled[boards1d[j]-1]==false) {
						possibleBoards[possibleBoardNumber]=peiceSpotsToBoardNumber(boards1d[0],boards1d[1],boards1d[2],boards1d[3],boards1d[4],boards1d[5],boards1d[6],boards1d[7]);
						possibleBoardNumber++;
					}
					boards1d[j]-=1;
				}
			}
		}
		int[] possibleBoardsOutput = new int[possibleBoardNumber];
		for (int j=0;j<possibleBoardNumber;j++) {
			possibleBoardsOutput[j]=possibleBoards[j];
		}
		return possibleBoardsOutput;
	}
	
	public void printPossibleMoves(int boardNumber) {
		int[] possibleBoards = findPossibleMoves(boardNumber);
			System.out.println("The following boards are winning:");
			for (int j=0;j<possibleBoards.length;j++) {
				if (isCurrentPlayerWinning(possibleBoards[j])==-1) {
					print900900Boards(possibleBoards[j]);
				}
			}
			System.out.println("The following boards are a draw:");
			for (int j=0;j<possibleBoards.length;j++) {
				if (isCurrentPlayerWinning(possibleBoards[j])==0) {
					print900900Boards(possibleBoards[j]);
				}
			}
			System.out.println("The following boards are losing:");
			for (int j=0;j<possibleBoards.length;j++) {
				if (isCurrentPlayerWinning(possibleBoards[j])==1) {
					print900900Boards(possibleBoards[j]);
				}
			}
	}

	public void printBestMove(int boardNumber) {
		int[] possibleBoards = findPossibleMoves(boardNumber);
		boolean canWin=false;
		boolean canDraw=false;
		byte fastestWin=127;
		byte slowestLoss=0;
		for(int i=0;i<possibleBoards.length;i++) {
			if (isCurrentPlayerWinning(possibleBoards[i])==-1) {
				canWin=true;
				if (boards900900[10][possibleBoards[i]]<fastestWin) {
					fastestWin=boards900900[10][possibleBoards[i]];
				}
			}
			else if (isCurrentPlayerWinning(possibleBoards[i])==0) {
				canDraw=true;
			}
			else {
				if (boards900900[10][possibleBoards[i]]>slowestLoss) {
					slowestLoss=boards900900[10][possibleBoards[i]];
				}
			}
		}
		System.out.println("The best move(s):");
		for (int i=0;i<possibleBoards.length;i++) {
			if (canWin==true&&boards900900[10][possibleBoards[i]]==fastestWin&&isCurrentPlayerWinning(possibleBoards[i])==-1) {
				print900900Boards(possibleBoards[i]);
			}
			else if (canWin==false&&canDraw==true&&isCurrentPlayerWinning(possibleBoards[i])==0) {
				print900900Boards(possibleBoards[i]);
			}
			else if (canWin==false&&canDraw==false&&boards900900[10][possibleBoards[i]]==slowestLoss&&isCurrentPlayerWinning(possibleBoards[i])==1) {
				print900900Boards(possibleBoards[i]);
			}
		}
	}

	private int isCurrentPlayerWinning(int boardNumber) {
		int winningStatus = 0; //Initializing this just to avoid an error, also -1 if losing, 0 if draw, 1 if winning
		if ((boards900900[8][boardNumber]==0&&boards900900[9][boardNumber]==1)||(boards900900[8][boardNumber]==1&&boards900900[9][boardNumber]==2)) {
			winningStatus = 1;
		}
		else if (boards900900[9][boardNumber]==0) {
			winningStatus = 0;
		}
		else {
			winningStatus = -1;
		}
		return winningStatus;
	}

	private void nextRoundsToRun() {
		do {
			boardsChanged=0;
			nextRounds();
		}while (boardsChanged>0);
	}
	private void nextRounds() {
		int[] boards1d = new int[8];
		for (int i=0;i<900900;i++) {
			if (boards900900[9][i]==0) {
				boards1d[0]=boards900900[0][i];
				boards1d[1]=boards900900[1][i];
				boards1d[2]=boards900900[2][i];
				boards1d[3]=boards900900[3][i];
				boards1d[4]=boards900900[4][i];
				boards1d[5]=boards900900[5][i];
				boards1d[6]=boards900900[6][i];
				boards1d[7]=boards900900[7][i];
				int[] possibleBoards = new int[14];
				byte possibleBoardNumber=0;
				boolean[] filled = new boolean[16];
				for (int j=0;j<8;j++) {
					filled[boards1d[j]-1]=true;
				}
				if (boards900900[8][i]==0) {
					for (int j=0;j<4;j++) {
						
						if (boards1d[j]>4) {
							boards1d[j]-=4;
							if (filled[boards1d[j]-1]==false) {
								possibleBoards[possibleBoardNumber]=peiceSpotsToBoardNumber(boards1d[0],boards1d[1],boards1d[2],boards1d[3],boards1d[4],boards1d[5],boards1d[6],boards1d[7]);
								possibleBoardNumber++;
							}
							boards1d[j]+=4;
						}
						if (boards1d[j]!=1&&boards1d[j]!=5&&boards1d[j]!=9&&boards1d[j]!=13) {
							boards1d[j]-=1;
							if (filled[boards1d[j]-1]==false) {
								possibleBoards[possibleBoardNumber]=peiceSpotsToBoardNumber(boards1d[0],boards1d[1],boards1d[2],boards1d[3],boards1d[4],boards1d[5],boards1d[6],boards1d[7]);
								possibleBoardNumber++;
							}
							boards1d[j]+=1;
						}
						if (boards1d[j]<13) {
							boards1d[j]+=4;
							if (filled[boards1d[j]-1]==false) {
								possibleBoards[possibleBoardNumber]=peiceSpotsToBoardNumber(boards1d[0],boards1d[1],boards1d[2],boards1d[3],boards1d[4],boards1d[5],boards1d[6],boards1d[7]);
								possibleBoardNumber++;
							}
							boards1d[j]-=4;
						}
						if (boards1d[j]!=16&&boards1d[j]!=12&&boards1d[j]!=8&&boards1d[j]!=4) {
							boards1d[j]+=1;
							if (filled[boards1d[j]-1]==false) {
								possibleBoards[possibleBoardNumber]=peiceSpotsToBoardNumber(boards1d[0],boards1d[1],boards1d[2],boards1d[3],boards1d[4],boards1d[5],boards1d[6],boards1d[7]);
								possibleBoardNumber++;
							}
							boards1d[j]-=1;
						}
					}
				}
				else {
					for (int j=4;j<8;j++) {
						
						if (boards1d[j]>4) {
							boards1d[j]-=4;
							if (filled[boards1d[j]-1]==false) {
								possibleBoards[possibleBoardNumber]=peiceSpotsToBoardNumber(boards1d[0],boards1d[1],boards1d[2],boards1d[3],boards1d[4],boards1d[5],boards1d[6],boards1d[7]);
								possibleBoardNumber++;
							}
							boards1d[j]+=4;
						}
						if (boards1d[j]!=1&&boards1d[j]!=5&&boards1d[j]!=9&&boards1d[j]!=13) {
							boards1d[j]-=1;
							if (filled[boards1d[j]-1]==false) {
								possibleBoards[possibleBoardNumber]=peiceSpotsToBoardNumber(boards1d[0],boards1d[1],boards1d[2],boards1d[3],boards1d[4],boards1d[5],boards1d[6],boards1d[7]);
								possibleBoardNumber++;
							}
							boards1d[j]+=1;
						}
						if (boards1d[j]<13) {
							boards1d[j]+=4;
							if (filled[boards1d[j]-1]==false) {
								possibleBoards[possibleBoardNumber]=peiceSpotsToBoardNumber(boards1d[0],boards1d[1],boards1d[2],boards1d[3],boards1d[4],boards1d[5],boards1d[6],boards1d[7]);
								possibleBoardNumber++;
							}
							boards1d[j]-=4;
						}
						if (boards1d[j]!=16&&boards1d[j]!=12&&boards1d[j]!=8&&boards1d[j]!=4) {
							boards1d[j]+=1;
							if (filled[boards1d[j]-1]==false) {
								possibleBoards[possibleBoardNumber]=peiceSpotsToBoardNumber(boards1d[0],boards1d[1],boards1d[2],boards1d[3],boards1d[4],boards1d[5],boards1d[6],boards1d[7]);
								possibleBoardNumber++;
							}
							boards1d[j]-=1;
						}
					}
				}
				int[] possibleBoardsResults = new int[possibleBoardNumber];
				for (int j=0;j<possibleBoardNumber;j++) {
					possibleBoardsResults[j]=boards900900[9][possibleBoards[j]];
				}
				boolean is1=true;
				boolean is2=true;
				boolean maybe1=false;
				boolean maybe2=false;
				for (int j=0;j<possibleBoardNumber;j++) {
					if (possibleBoardsResults[j]!=1) {
						is1=false;
					}
					if (possibleBoardsResults[j]!=2) {
						is2=false;
					}
					if (possibleBoardsResults[j]==1) {
						maybe1=true;
					}
					if (possibleBoardsResults[j]==2) {
						maybe2=true;
					}
				}
				if (boards900900[8][i]==0&&is2==true) {
					boards900900[9][i]=2;	
					boardsChanged++;
				}
				if (boards900900[8][i]==1&&is1==true) {
					boards900900[9][i]=1;
					boardsChanged++;
				}
				if (boards900900[8][i]==0&&maybe1==true) {
					boards900900[9][i]=1;
					boardsChanged++;
				}
				if (boards900900[8][i]==1&&maybe2==true) {
					boards900900[9][i]=2;
					boardsChanged++;
				}
				
			}
		}
	}
	public int peiceSpotsToBoardNumber(int[] input) {
		return peiceSpotsToBoardNumber(input[0],input[1],input[2],input[3],input[4],input[5],input[6],input[7]);
	}
	public int peiceSpotsToBoardNumber(int a, int b, int c, int d, int e, int f,int g, int h) {
		int[] y = {a,b,c,d};
		int[] z = {e,f,g,h};
		Arrays.sort(y);
		Arrays.sort(z);
		a=y[0];
		b=y[1];
		c=y[2];
		d=y[3];
		e=z[0];
		f=z[1];
		g=z[2];
		h=z[3];
		int boardNumber=(search1820[(d-4)+13*((c-3)+13*((b-2)+13*(a-1)))]*495);
		int e2=0;
		int f2=0;
		int g2=0;
		int h2=0;
		if (e>a) 
			e2++;
		if (e>b)
			e2++;
		if (e>c)
			e2++;
		if (e>d)
			e2++;
		if (f>a) 
			f2++;
		if (f>b)
			f2++;
		if (f>c)
			f2++;
		if (f>d)
			f2++;
		if (g>a) 
			g2++;
		if (g>b)
			g2++;
		if (g>c)
			g2++;
		if (g>d)
			g2++;
		if (h>a) 
			h2++;
		if (h>b)
			h2++;
		if (h>c)
			h2++;
		if (h>d)
			h2++;
		e=e-e2;
		f=f-f2;
		g=g-g2;
		h=h-h2;
		boardNumber+=search495[(h-4)+9*((g-3)+9*((f-2)+9*(e-1)))];
		return boardNumber;
	}
	private void create1820Boards() {
		short boardNumber = 0;
		for (byte i=1;i<=13;i++) {
			for (byte j=(byte)(i+1);j<=14;j++) {
				for (byte k=(byte)(j+1);k<=15;k++) {
					for (byte l=(byte)(k+1);l<=16;l++) {
						boards1820[0][boardNumber]=i;
						boards1820[1][boardNumber]=j;
						boards1820[2][boardNumber]=k;
						boards1820[3][boardNumber]=l;
						search1820[(l-4)+13*((k-3)+13*((j-2)+13*(i-1)))]=boardNumber;
						boardNumber++;
					}
				}
			}
		}
	}
	private void create495Boards() {
		short boardNumber = 0;
		for (byte i=1;i<=9;i++) {
			for (byte j=(byte)(i+1);j<=10;j++) {
				for (byte k=(byte)(j+1);k<=11;k++) {
					for (byte l=(byte)(k+1);l<=12;l++) {
						boards495[0][boardNumber]=i;
						boards495[1][boardNumber]=j;
						boards495[2][boardNumber]=k;
						boards495[3][boardNumber]=l;
						search495[(l-4)+9*((k-3)+9*((j-2)+9*(i-1)))]=boardNumber;
						boardNumber++;
					}
				}
			}
		}
	}
	private void create900900Boards() {
		for (short i=0;i<1820;i++) {
			for (short j=0;j<495;j++) {
				for (byte y=4;y<8;y++) {
					boards900900[y][i*495+j]=boards495[y-4][j];
				}
				for (byte z=0;z<4;z++) {
					boards900900[z][i*495+j]=boards1820[z][i];
					for (byte x=4;x<8;x++) {
						if (boards900900[x][i*495+j]>=boards900900[z][i*495+j]) {
							boards900900[x][i*495+j]++;
						}
					}
				}
			}
		}
		for(int i=0;i<900900;i++) {
			boards900900[10][i]=-128;
		}
	}
	private void whosTurn() {
		for (int i=0;i<900900;i++) {
			if(((boards900900[0][i]-1)/4+(boards900900[0][i]-1)%4+(boards900900[1][i]-1)/4+(boards900900[1][i]-1)%4+(boards900900[2][i]-1)/4+(boards900900[2][i]-1)%4+(boards900900[3][i]-1)/4+(boards900900[3][i]-1)%4+(boards900900[4][i]-1)/4+(boards900900[4][i]-1)%4+(boards900900[5][i]-1)/4+(boards900900[5][i]-1)%4+(boards900900[6][i]-1)/4+(boards900900[6][i]-1)%4+(boards900900[7][i]-1)/4+(boards900900[7][i]-1)%4)%2==0) {
				boards900900[8][i]=0;
			}
			else {
				boards900900[8][i]=1;
			}
		}
	}
	private void anyThreeInRows(int z) {
		x3 = false;
		y3 = false;
		byte a=boards900900[0][z];
		byte b=boards900900[1][z];
		byte c=boards900900[2][z];
		byte d=boards900900[3][z];
		boolean[] filled = new boolean[16];
		for (int i=1;i<=16;i++) {
		if (a==i || b==i || c==i || d==i) {
		filled[i-1]=true;
		}
		}
		if (a!=1&&a!=4&&a!=13&&a!=16) {
		if (a==6||a==7||a==10||a==11) {
		if (filled[a-6]==true&&filled[a+4]==true) {
			x3=true;
		}
		if (filled[a-4]==true&&filled[a+2]==true) {
			x3=true;
		}
		}
		if (a>4&&a<13&&filled[a-5]==true&&filled[a+3]==true) {
			x3=true;
		}
		if (a!=5&&a!=8&&a!=9&&a!=12&&filled[a-2]==true&&filled[a]==true) {
			x3=true;
		}
		}
		if (b!=1&&b!=4&&b!=13&&b!=16) {
		if (b==6||b==7||b==10||b==11) {
		if (filled[b-6]==true&&filled[b+4]==true) {
			x3=true;
		}
		if (filled[b-4]==true&&filled[b+2]==true) {
			x3=true;
		}
		}
		if (b>4&&b<13&&filled[b-5]==true&&filled[b+3]==true) {
			x3=true;
		}
		if (b!=5&&b!=8&&b!=9&&b!=12&&filled[b-2]==true&&filled[b]==true) {
			x3=true;
		}
		}
		if (c!=1&&c!=4&&c!=13&&c!=16) {
		if (c==6||c==7||c==10||c==11) {
		if (filled[c-6]==true&&filled[c+4]==true) {
			x3=true;
		}
		if (filled[c-4]==true&&filled[c+2]==true) {
			x3=true;
		}
		}
		if (c>4&&c<13&&filled[c-5]==true&&filled[c+3]==true) {
			x3=true;
		}
		if (c!=5&&c!=8&&c!=9&&c!=12&&filled[c-2]==true&&filled[c]==true) {
			x3=true;
		}
		}
		if (d!=1&&d!=4&&d!=13&&d!=16) {
		if (d==6||d==7||d==10||d==11) {
		if (filled[d-6]==true&&filled[d+4]==true) {
			x3=true;
		}
		if (filled[d-4]==true&&filled[d+2]==true) {
			x3=true;
		}
		}
		if (d>4&&d<13&&filled[d-5]==true&&filled[d+3]==true) {
			x3=true;
		}
		if (d!=5&&d!=8&&d!=9&&d!=12&&filled[d-2]==true&&filled[d]==true) {
			x3=true;
		}
		}
		byte a2=boards900900[4][z];
		byte b2=boards900900[5][z];
		byte c2=boards900900[6][z];
		byte d2=boards900900[7][z];
		boolean[] filled2 = new boolean[16];
		for (int i=1;i<=16;i++) {
		if (a2==i || b2==i || c2==i || d2==i) {
		filled2[i-1]=true;
		}
		}
		if (a2!=1&&a2!=4&&a2!=13&&a2!=16) {
		if (a2==6||a2==7||a2==10||a2==11) {
		if (filled2[a2-6]==true&&filled2[a2+4]==true) {
			y3=true;
		}
		if (filled2[a2-4]==true&&filled2[a2+2]==true) {
			y3=true;
		}
		}
		if (a2>4&&a2<13&&filled2[a2-5]==true&&filled2[a2+3]==true) {
			y3=true;
		}
		if (a2!=5&&a2!=8&&a2!=9&&a2!=12&&filled2[a2-2]==true&&filled2[a2]==true) {
			y3=true;
		}
		}
		if (b2!=1&&b2!=4&&b2!=13&&b2!=16) {
		if (b2==6||b2==7||b2==10||b2==11) {
		if (filled2[b2-6]==true&&filled2[b2+4]==true) {
			y3=true;
		}
		if (filled2[b2-4]==true&&filled2[b2+2]==true) {
			y3=true;
		}
		}
		if (b2>4&&b2<13&&filled2[b2-5]==true&&filled2[b2+3]==true) {
			y3=true;
		}
		if (b2!=5&&b2!=8&&b2!=9&&b2!=12&&filled2[b2-2]==true&&filled2[b2]==true) {
			y3=true;
		}
		}
		if (c2!=1&&c2!=4&&c2!=13&&c2!=16) {
		if (c2==6||c2==7||c2==10||c2==11) {
		if (filled2[c2-6]==true&&filled2[c2+4]==true) {
			y3=true;
		}
		if (filled2[c2-4]==true&&filled2[c2+2]==true) {
			y3=true;
		}
		}
		if (c2>4&&c2<13&&filled2[c2-5]==true&&filled2[c2+3]==true) {
			y3=true;
		}
		if (c2!=5&&c2!=8&&c2!=9&&c2!=12&&filled2[c2-2]==true&&filled2[c2]==true) {
			y3=true;
		}
		}
		if (d2!=1&&d2!=4&&d2!=13&&d2!=16) {
		if (d2==6||d2==7||d2==10||d2==11) {
		if (filled2[d2-6]==true&&filled2[d2+4]==true) {
			y3=true;
		}
		if (filled2[d2-4]==true&&filled2[d2+2]==true) {
			y3=true;
		}
		}
		if (d2>4&&d2<13&&filled2[d2-5]==true&&filled2[d2+3]==true) {
			y3=true;
		}
		if (d2!=5&&d2!=8&&d2!=9&&d2!=12&&filled2[d2-2]==true&&filled2[d2]==true) {
			y3=true;
		}
		}
	}
	private void firstRound() {
		for (int z=0;z<900900;z++) {
			anyThreeInRows(z);
			if (x3==true&&y3==true) {
				boards900900[9][z]=3;
				boards900900[10][z]=0;
			}
			else if (x3==true) {
				boards900900[9][z]=1;
				boards900900[10][z]=0;
			}
			else if (y3==true) {
				boards900900[9][z]=2;
				boards900900[10][z]=0;
			}
		}
	}
	public void print495Boards(int boardNumber) {
		for (byte z=0;z<4;z++) {
			System.out.print(boards495[z][boardNumber]+" ");
		}
		System.out.println();
	}
	public void print1820Boards(int boardNumber) {
		for (byte z=0;z<4;z++) {
			System.out.print(boards1820[z][boardNumber]+" ");
		}
		System.out.println();
	}
	public void print900900Boards(int boardNumber) {
		for (byte z=0;z<11;z++) {
			System.out.print(boards900900[z][boardNumber]+" ");
		}
		System.out.println();
	}
}
