package Proiect;

import Pieces.*;

import java.util.ArrayList;
import java.util.Random;

// the class for the current game
public class Game {
	private Piece [][]table; // the game's board
	private Color crt_color; // the current player's color
	private Color botColor; // the bot's color
	private boolean botIsOn; // check if the bot is on
	private Piece whiteKing; // the white king
	private Piece blackKing; // the black king
	private ArrayList<Piece> remWhitePieces; // remaining white pieces on the board
	private ArrayList<Piece> remBlackPieces; // remaining black pieces on the board
	private Piece lastPiece; // the last piece that has been moved
	private Piece sndLastPiece; // the penultimate piece that has been moved


	public void setTable(Piece[][] table) {
		this.table = table;
	}

	public Piece getLastPiece() {
		return lastPiece;
	}

	public void setLastPiece(Piece lastPiece) {
		this.lastPiece = lastPiece;
	}

	public Piece getSndLastPiece() {
		return sndLastPiece;
	}

	public void setSndLastPiece(Piece sndLastPiece) {
		this.sndLastPiece = sndLastPiece;
	}


	// get the piece that the bot will move, together with its updated list of legal moves
	public Piece getCrtPiece(){

		// get the list of available pieces
		ArrayList<Piece> aux = botColor == Color.BLACK ? new ArrayList<Piece>(remBlackPieces) : new ArrayList<Piece>(remWhitePieces);
		Piece currentKing = this.botColor == Color.BLACK ? this.blackKing : this.whiteKing;


		// check if an opponent's piece put our king in check
		Piece opponentPiece = ((King) currentKing).isCheck(table, currentKing.getPosition());

		// if the king is not in check
		if (opponentPiece == null) {

			// if the king can make any castling move, return the king as the current piece

			// check if the king can make a kingside castling move
			if(getKingsideCastling((King) currentKing)) {
				return currentKing;
			}

			// check if the king can make a queenside castling move
			if(getQueensideCastling((King) currentKing)) {
				return currentKing;
			}

			// if possible, make an "en passant" move using the last piece (if it is a pawn)
			Random r = new Random();

			if(sndLastPiece instanceof Pawn && lastPiece instanceof Pawn) {

				int currentX = sndLastPiece.getPosition().getX();
				int currentY = sndLastPiece.getPosition().getY();

				ArrayList<Position> moves = new ArrayList<Position>();

				int offset = sndLastPiece.getColor() == Color.BLACK ? -1 : 1;

				if (table[currentX][currentY - 1] == lastPiece) {
					if (lastPiece.getColor() != sndLastPiece.getColor()) {
						if (currentX == 4 || currentX == 5) {
							if (((Pawn) table[currentX][currentY - 1]).isAntNeverMoved()) {
								moves.add(new Position(currentX + offset, currentY - 1));
							}
						}
					}
				}

				if (table[currentX][currentY + 1] == lastPiece) {
					if (lastPiece.getColor() != sndLastPiece.getColor()) {
						if (currentX == 4 || currentX == 5) {
							if (((Pawn) table[currentX][currentY + 1]).isAntNeverMoved()) {
								moves.add(new Position(currentX + offset, currentY + 1));
							}
						}
					}
				}

				sndLastPiece.filterMovesSoNoCheckOnKing(sndLastPiece, moves, this);

				// if there is at least one available "en passant" moves, return the pawn
				if (moves.size() > 0) {
					sndLastPiece.setLegalMoves(moves);
					return sndLastPiece;
				}
			}

			// extract pieces from the available list one by one and return the first one found with legal moves
			while (aux.size() > 0) {
				int i = r.nextInt(aux.size());
				Piece p = aux.remove(i);
				p.generateMoves(this);

				if (p.getLegalMoves().size() > 0)
					return p;
			}
		}
		else {
			// the king is being attacked

			// if the king has been on check at least 3 times, resign
			if(((King) currentKing).getCheckNumber() >= 3){
				return null;
			}

			Random r = new Random();

			// check if the attacking piece can be eaten; return one of the pieces that can eat it if possible
			while (aux.size() > 0) {
				int i = r.nextInt(aux.size());
				Piece p = aux.remove(i);
				p.generateMoves(this);

				for(Position poz : p.getLegalMoves()) {
					if(poz.getX() == opponentPiece.getPosition().getX() && poz.getY() == opponentPiece.getPosition().getY()) {
						ArrayList<Position> theTrueMove =  new ArrayList<Position>();
						theTrueMove.add(poz);
						p.setLegalMoves(theTrueMove);
						return p;
					}
				}
			}

			// if the attacking piece cannot be eaten, try to move the king away from check
			currentKing.generateMoves(this);
			if(currentKing.getLegalMoves().size() > 0) {
				return currentKing;
			}

			// if a knight can't be eaten and the king can't escape from it,
			// we have received check mate
			if(opponentPiece instanceof Knight) {
				return null;
			}

			// if a pawn can't be eaten and the king can't escape from it,
			// we have received check mate
			if(opponentPiece instanceof Pawn) {
				return null;
			}

			// try to block the attacking piece with an allied piece

			int kingX = currentKing.getPosition().getX();
			int kingY = currentKing.getPosition().getY();
			int opponentX = opponentPiece.getPosition().getX();
			int opponentY = opponentPiece.getPosition().getY();

			// find the direction on which the piece attacks the king
			int direction = currentKing.isCollinear(kingX, kingY, opponentX, opponentY);

			// find all the allied pieces that could block the attacking piece
			ArrayList<Piece> aux2 = botColor == Color.BLACK? new ArrayList<>(remBlackPieces) : new ArrayList<Piece>(remWhitePieces);
			ArrayList<Piece> blockingPieces = currentKing.getBlockingPieces(kingX, kingY, opponentX, opponentY, direction, aux2, table);

			if(blockingPieces.size() == 0) {
				return null;
			}

			// return a random piece from the list of blocking pieces
			return blockingPieces.get(r.nextInt(blockingPieces.size()));
		}

		return null;
	}

	// check if the current king can make a kingside castling move
	private boolean getKingsideCastling(King currentKing) {

		// check if the king has been moved before
		if(!(currentKing.isNeverMoved())) {
			return false;
		}

		int line = currentKing.getColor() == Color.BLACK ? 8 : 1;

		// check if the rook has been moved before
		if(!(table[line][8] instanceof Rook)) {
			return false;
		}

		if(!(((Rook)table[line][8]).isNeverMoved())) {
			return false;
		}

		table[currentKing.getPosition().getX()][currentKing.getPosition().getY()] = null;

		// check if any position between the king and the rook is occupied or the king would go through check
		for(int i = 6; i <= 7; i++) {
			if(table[line][i] != null){
				table[currentKing.getPosition().getX()][currentKing.getPosition().getY()] = currentKing;
				return false;
			}

			Piece violentPiece = currentKing.isCheck(table, new Position(line, i));
			if(violentPiece != null) {
				table[currentKing.getPosition().getX()][currentKing.getPosition().getY()] = currentKing;
				return false;
			}
		}

		table[currentKing.getPosition().getX()][currentKing.getPosition().getY()] = currentKing;

		// if possible, add the move to the list
		ArrayList<Position> moves = new ArrayList<Position>();
		moves.add(new Position(line, 7));
		currentKing.setLegalMoves(moves);

		return true;
	}

	// check if the current king can make a queenside castling move
	private boolean getQueensideCastling(King currentKing) {

		// check if the king has been moved before
		if(!(currentKing.isNeverMoved())) {
			return false;
		}

		int line = currentKing.getColor() == Color.BLACK ? 8 : 1;

		// check if the rook has been moved before
		if(!(table[line][1] instanceof Rook)) {

			return false;
		}

		if(!(((Rook)table[line][1]).isNeverMoved())) {
			return false;
		}

		// check if any position between the king and the rook is occupied
		for(int i = 2; i <= 4; i++) {
			if(table[line][i] != null) {
				return false;
			}
		}

		table[currentKing.getPosition().getX()][currentKing.getPosition().getY()] = null;

		// check if the king would go through check
		for(int i = 3; i <= 4; i++) {
			Piece violentPiece = currentKing.isCheck(table, new Position(line, i));
			if(violentPiece != null) {
				table[currentKing.getPosition().getX()][currentKing.getPosition().getY()] = currentKing;
				return false;
			}
		}

		table[currentKing.getPosition().getX()][currentKing.getPosition().getY()] = currentKing;

		// if possible, add the move to the list
		ArrayList<Position> moves = new ArrayList<Position>();
		moves.add(new Position(line, 3));
		currentKing.setLegalMoves(moves);

		return true;
	}

	public boolean getBotStatus(){
		return botIsOn;
	}

	public void setBotStatus(boolean bol){
		this.botIsOn = bol;
	}

	public void setCrtColor(Color c){
		this.crt_color = c;
	}

	public Color getCrtColor(){
		return this.crt_color;
	}

	public Color getBotColor() {
		return botColor;
	}

	public Piece getWhiteKing() {
		return whiteKing;
	}

	public Piece getBlackKing() {
		return blackKing;
	}

	public ArrayList<Piece> getRemWhitePieces() {
		return remWhitePieces;
	}

	public void setRemWhitePieces(ArrayList<Piece> remWhitePieces) {
		this.remWhitePieces = remWhitePieces;
	}

	public ArrayList<Piece> getRemBlackPieces() {
		return remBlackPieces;
	}

	public void setRemBlackPieces(ArrayList<Piece> remBlackPieces) {
		this.remBlackPieces = remBlackPieces;
	}

	public Game (){
		table = new Piece[10][10];
	}

	// set the bot's color to be white
	public void setColorWhite() {
		this.botColor = Color.WHITE;
	}

	// set the bot's color to be black
	public void setColorBlack() {
		this.botColor = Color.BLACK;
	}


	// initialize the game
	public void initializeGame(){
		this.sndLastPiece = null;
		this.lastPiece = null;

		table = new Piece[10][10];

		this.crt_color = Color.WHITE;
		this.botColor = Color.BLACK;
		this.botIsOn = true;
		this.remBlackPieces = new ArrayList<Piece>();
		this.remWhitePieces = new ArrayList<Piece>();

		for(int i = 1; i<= 8; i++){
			table[2][i] = new Pawn(Color.WHITE,2,i);
			table[7][i] = new Pawn(Color.BLACK,7,i);
		}

		table[1][1] = new Rook(Color.WHITE,1,1);
		table[1][8] = new Rook(Color.WHITE,1,8);
		table[8][1] = new Rook(Color.BLACK,8,1);
		table[8][8] = new Rook(Color.BLACK,8,8);

		table[1][2] = new Knight(Color.WHITE,1,2);
		table[1][7] = new Knight(Color.WHITE,1,7);
		table[8][2] = new Knight(Color.BLACK,8,2);
		table[8][7] = new Knight(Color.BLACK,8,7);

		table[1][3] = new Bishop(Color.WHITE,1,3);
		table[1][6] = new Bishop(Color.WHITE,1,6);
		table[8][3] = new Bishop(Color.BLACK,8,3);
		table[8][6] = new Bishop(Color.BLACK,8,6);

		table[1][4] = new Queen(Color.WHITE,1,4);
		table[8][4] = new Queen(Color.BLACK,8,4);

		table[1][5] = new King(Color.WHITE,1,5);
		this.whiteKing = table[1][5];
		table[8][5] = new King(Color.BLACK,8,5);
		this.blackKing = table[8][5];

		for(int i = 1; i <= 2; i++){
			for(int j = 1; j <= 8; j++){
				this.remWhitePieces.add(table[i][j]);
				this.remBlackPieces.add(table[i+6][j]);
			}
		}
	}

	// show the board at a given time (used for debugging)
	public void afis(){
		for(int i=8;i>=1;i--){
			for(int j=1;j<=8;j++){
				if(table[i][j] == null){
					System.out.print("0 ");
				}
				else {
					if(table[i][j].getColor().equals(Color.BLACK))
						System.out.print("\u001B[34m");
					else
						System.out.print("\u001B[33m");

					System.out.print(table[i][j].toString() + "\u001B[0m ");
				}
			}
			System.out.println();
		}
	}

	public Piece[][] getTable() {
		return table;
	}

	public void interpretNotBotMove(String word){
		int startX = word.charAt(1) - '0';
		int startY = word.charAt(0) - 96;
		int finishX = word.charAt(3) - '0';
		int finishY = word.charAt(2) - 96;

		// if the finish position contains a piece, remove it from the list of available pieces according to its color
		if(this.table[finishX][finishY] != null)
		{
			if(this.table[finishX][finishY].getColor() == Color.BLACK)
				remBlackPieces.remove(this.table[finishX][finishY]);
			else
				remWhitePieces.remove(this.table[finishX][finishY]);
		}
		else {
			// if the finish position does not contain a piece, check if it is an "en passant" move or a castling move
			if(this.table[startX][startY] instanceof Pawn){
				if(((startX - finishX == 1) || (finishX - startX == 1)) && ((startY - finishY == 1) || (finishY - startY == 1))){
					if(this.table[finishX][finishY] == null) {
						if(this.table[startX][finishY].getColor() == Color.WHITE){
							this.getRemWhitePieces().remove(this.table[startX][finishY]);
						}
						else this.getRemBlackPieces().remove(this.table[startX][finishY]);
						this.table[startX][finishY] = null;
					}
				}
			}
			else if(this.table[startX][startY] instanceof King) {
				int line = this.table[startX][startY].getColor() == Color.BLACK ? 8 : 1;

				if(finishY - startY == 2) {
					// kingside castling

					((Rook) this.table[line][8]).setNeverMoved(false);
					this.table[line][8].setPosition(line, 6);
					this.table[line][6] = this.table[line][8];
					this.table[line][8] = null;
				}
				else if(startY - finishY == 2) {
					// queenside castling

					((Rook) this.table[line][1]).setNeverMoved(false);
					this.table[line][1].setPosition(line, 4);
					this.table[line][4] = this.table[line][1];
					this.table[line][1] = null;
				}
			}
		}

		// move the piece on the board
		this.table[startX][startY].setPosition(finishX, finishY);
		this.table[finishX][finishY] = this.table[startX][startY];
		this.table[startX][startY] = null;

		// update the last moved piece and the penultimate moved piece
		sndLastPiece = lastPiece;
		lastPiece = table[finishX][finishY];

		// check if the piece is a pawn that needs to be promoted
		if(this.table[finishX][finishY] instanceof  Pawn && finishX == 1){
			this.remBlackPieces.remove(this.table[finishX][finishY]);
			if(word.charAt(4) == 'q'){
				this.table[finishX][finishY] = new Queen(Color.BLACK, finishX, finishY);
				this.remBlackPieces.add(this.table[finishX][finishY]);
			}
			else if(word.charAt(4) == 'r'){
				this.table[finishX][finishY] = new Rook(Color.BLACK, finishX, finishY);
				this.remBlackPieces.add(this.table[finishX][finishY]);
			}
			else if(word.charAt(4) == 'b'){
				this.table[finishX][finishY] = new Bishop(Color.BLACK, finishX, finishY);
				this.remBlackPieces.add(this.table[finishX][finishY]);
			}
			else if(word.charAt(4) == 'n'){
				this.table[finishX][finishY] = new Knight(Color.BLACK, finishX, finishY);
				this.remBlackPieces.add(this.table[finishX][finishY]);
			}
		}

		if(this.table[finishX][finishY] instanceof  Pawn && finishX == 8){
			this.remWhitePieces.remove(this.table[finishX][finishY]);
			if(word.charAt(4) == 'q'){
				this.table[finishX][finishY] = new Queen(Color.WHITE, finishX, finishY);
				this.remWhitePieces.add(this.table[finishX][finishY]);
			}
			else if(word.charAt(4) == 'r'){
				this.table[finishX][finishY] = new Rook(Color.WHITE, finishX, finishY);
				this.remWhitePieces.add(this.table[finishX][finishY]);
			}
			else if(word.charAt(4) == 'b'){
				this.table[finishX][finishY] = new Bishop(Color.WHITE, finishX, finishY);
				this.remWhitePieces.add(this.table[finishX][finishY]);
			}
			else if(word.charAt(4) == 'n'){
				this.table[finishX][finishY] = new Knight(Color.WHITE, finishX, finishY);
				this.remWhitePieces.add(this.table[finishX][finishY]);
			}
		}

		// update the number of checks of both the kings
		if(((King) whiteKing).isCheck(table, whiteKing.getPosition()) != null) {
			((King) whiteKing).setCheckNumber(((King) whiteKing).getCheckNumber() + 1);
		}

		if(((King) blackKing).isCheck(table, blackKing.getPosition()) != null) {
			((King) blackKing).setCheckNumber(((King) blackKing).getCheckNumber() + 1);
		}

		// if the piece is a pawn/ king/ rook, update its "neverMoved" parameter
		if(this.table[finishX][finishY] instanceof Pawn) {
			((Pawn) this.table[finishX][finishY]).setAntNeverMoved(((Pawn) this.table[finishX][finishY]).isNeverMoved());
			((Pawn) this.table[finishX][finishY]).setNeverMoved(false);
		}

		if(this.table[finishX][finishY] instanceof King) {
			((King) this.table[finishX][finishY]).setNeverMoved(false);
		}

		if(this.table[finishX][finishY] instanceof Rook) {
			((Rook) this.table[finishX][finishY]).setNeverMoved(false);
		}
	}

	// interpret the given move
	public void interpretBotMove(String word) {
		int startX = word.charAt(1) - '0';
		int startY = word.charAt(0) - 96;
		int finishX = word.charAt(3) - '0';
		int finishY = word.charAt(2) - 96;

		// if the finish position contains a piece, remove it from the list of available pieces according to its color
		if(this.table[finishX][finishY] != null)
		{
			if(this.table[finishX][finishY].getColor() == Color.BLACK)
				remBlackPieces.remove(this.table[finishX][finishY]);
			else
				remWhitePieces.remove(this.table[finishX][finishY]);
		}
		else {
			// if the finish position does not contain a piece, check if it is an "en passant" move or a castling move
			if(this.table[startX][startY] instanceof Pawn){
				if(((startX - finishX == 1) || (finishX - startX == 1)) && (startY - finishY == 1|| finishY - startY == 1)){
					if(this.table[finishX][finishY] == null) {
						if(this.table[startX][finishY].getColor() == Color.WHITE){
							this.getRemWhitePieces().remove(this.table[startX][finishY]);
						}
						else this.getRemBlackPieces().remove(this.table[startX][finishY]);
						this.table[startX][finishY] = null;
					}
				}
			}
			else if(this.table[startX][startY] instanceof King) {
				int line = this.table[startX][startY].getColor() == Color.BLACK ? 8 : 1;

				if(finishY - startY == 2) {
					//kingside castling

					((Rook) this.table[line][8]).setNeverMoved(false);
					this.table[line][8].setPosition(line, 6);
					this.table[line][6] = this.table[line][8];
					this.table[line][8] = null;
				}
				else if(startY - finishY == 2) {
					// queenside castling

					((Rook) this.table[line][1]).setNeverMoved(false);
					this.table[line][1].setPosition(line, 4);
					this.table[line][4] = this.table[line][1];
					this.table[line][1] = null;
				}
			}
		}

		// move the piece on the board
		this.table[startX][startY].setPosition(finishX, finishY);
		this.table[finishX][finishY] = this.table[startX][startY];
		this.table[startX][startY] = null;

		// update the last moved piece and the penultimate moved piece
		sndLastPiece = lastPiece;
		lastPiece = table[finishX][finishY];

		// update the number of checks of both the kings
		if(((King) whiteKing).isCheck(table, whiteKing.getPosition()) != null) {
			((King) whiteKing).setCheckNumber(((King) whiteKing).getCheckNumber() + 1);
		}

		if(((King) blackKing).isCheck(table, blackKing.getPosition()) != null) {
			((King) blackKing).setCheckNumber(((King) blackKing).getCheckNumber() + 1);
		}
	}

	public void showTable(){
		for(int i = 1; i <= 8; i++){
			for(int j = 1; j <= 8 ; j++)
			{
				if (table[i][j] != null){
					if(table[i][j].getColor() == Color.WHITE)
						System.out.print("\u001B[33m");
					else
						System.out.print("\u001B[34m");
					System.out.print(table[i][j].toString() + " " + "\u001B[0m");
				}
				else System.out.print("0 ");
			}
			System.out.println();
		}
	}
}
