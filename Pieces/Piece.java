package Pieces;

import Proiect.Game;
import Proiect.Position;
import Proiect.Color;

import java.util.ArrayList;
import java.util.Random;

// the class for chess pieces
public abstract class Piece {
	private Color color;
	private Position position; // the piece's position
	private ArrayList<Position> legalMoves; // the piece's legal moves

	public Piece() {
		this.position = new Position();
	}

	public Piece(Color c, int x, int y) {
		this.position = new Position(x, y);
		this.color = c;
	}

	public Color getColor() {
		return color;
	}

	public Position getPosition(){
		return this.position;
	}

	public ArrayList<Position> getLegalMoves() {
		return legalMoves;
	}

	public void setLegalMoves(ArrayList<Position> legalMoves) {
		this.legalMoves = legalMoves;
	}

	public void setPosition(int x, int y) {
		this.position.setX(x);
		this.position.setY(y);
	}

	@Override
	public String toString() {
		return color + " " + position.getX() + " " + position.getY();
	}

	public abstract void generateMoves(Game game);


	public void movePiece(Game game) {
		Piece[][] table = game.getTable(); // get the current board
		Random r = new Random();

		// check if the bot is on (if the bot has to move the pawn)
		if (game.getBotStatus()) {
			ArrayList<Position> pozitii = this.legalMoves;

			// if the piece cannot make any moves, resign
			if (pozitii.size() == 0) {
				System.out.println("resign");
			}

			// choose a random move from the available ones
			int p = r.nextInt(pozitii.size());

			// create the string representing the chosen move
			String move = Character.toString(this.getPosition().getY() + 96) + Character.toString(this.getPosition().getX() + '0');
			move += Character.toString(pozitii.get(p).getY() + 96) + Character.toString(pozitii.get(p).getX() + '0');

			// if the piece is a pawn / king / rook set its "neverMoved" parameter to "false"
			if(this instanceof Pawn) {
				((Pawn) this).setNeverMoved(false);
			}

			if(this instanceof King){
				((King)this).setNeverMoved(false);
			}

			if(this instanceof Rook){
				((Rook)this).setNeverMoved(false);
			}

			// modify the board using the chosen move
			game.interpretBotMove(move);

			// send to xboard the chosen move
			System.out.println("move " + move);
		}
	}

	// filter out the moves of the given piece from the available arraylist of positions, so that
	// the king is not left on check after it moves
	public void filterMovesSoNoCheckOnKing(Piece ourPiece, ArrayList<Position> moves, Game game) {
		Piece[][] board = game.getTable();
		int currentX = ourPiece.getPosition().getX();
		int currentY = ourPiece.getPosition().getY();
		Color ourColor = ourPiece.getColor();
		Piece ourKing = (ourColor == Color.WHITE) ? game.getWhiteKing() : game.getBlackKing();
		int kingX = ourKing.getPosition().getX();
		int kingY = ourKing.getPosition().getY();

		// check if our piece and the king are on the same row, column or diagonal
		int direction = isCollinear(kingX, kingY, currentX, currentY);
		// if it's not then there is no threat of getting check after the piece moves
		if(direction == -1) {
			return;
		}

		// check if there is one of our pieces between the king and the piece we want to move.
		// if there is, then there is no threat of check
		if(checkBetween(kingX, kingY, currentX, currentY, direction, ourColor, board)) {
			return;
		}

		int i = 0;

		// on the given direction, check if the king is left on check; if it is, remove the move; otherwise, leave it
		while(i < moves.size()) {
			int next = 0;
			int finishX = moves.get(i).getX();
			int finishY = moves.get(i).getY();

			Piece aux = board[finishX][finishY];
			board[finishX][finishY] = ourPiece;
			board[currentX][currentY] = null;

			if(isCheckAfterMove(kingX, kingY, ourColor, direction, board)) {
				moves.remove(moves.get(i));
				next = 1;
			}

			board[currentX][currentY] = ourPiece;
			board[finishX][finishY] = aux;

			if(next == 0) {
				i++;
			}
		}
	}

	// check if our king and an opponent piece are collinear;
	// return an int which codifies the collinearity
	public int isCollinear(int kingX, int kingY, int pieceX, int pieceY) {
		// on the same row
		if(kingX == pieceX) {
			// on the right
			if(kingY < pieceY) {
				return 2;
			}
			// on the left
			else {
				return 6;
			}
		}
		// on the same column
		if(kingY == pieceY) {
			// up
			if(kingX > pieceX) {
				return 0;
			}
			// down
			else {
				return 4;
			}
		}
		int diffX = pieceX - kingX;
		int diffY = pieceY - kingY;
		// top right
		if(diffX < 0 && diffY > 0 && -diffX == diffY) {
			return 1;
		}
		// bottom right
		if(diffX > 0 && diffY > 0 && diffX == diffY) {
			return 3;
		}
		// bottom left
		if(diffX > 0 && diffY < 0 && diffX == -diffY) {
			return 5;
		}
		// top left
		if(diffX < 0 && diffY < 0 && diffX == diffY) {
			return 7;
		}
		// the king and the piece are not collinear
		return -1;
	}

	// check if the king has an allied piece between itself and the opponent piece
	private boolean checkBetween(int kingX, int kingY, int pieceX, int pieceY, int direction, Color ourColor, Piece[][] board) {
		switch (direction) {
			case 0:
				for(int i = 1; (kingX - i) > pieceX; i++) {
					if(board[kingX - i][kingY] != null && board[kingX - i][kingY].getColor() == ourColor) {
						return true;
					}
				}
				break;
			case 1:
				for(int i = 1; (kingX - i) > pieceX && (kingY + i) < pieceY; i++) {
					if(board[kingX - i][kingY + i] != null && board[kingX - i][kingY + i].getColor() == ourColor) {
						return true;
					}
				}
				break;
			case 2:
				for(int i = 1; (kingY + i) < pieceY; i++) {
					if(board[kingX][kingY + i] != null && board[kingX][kingY + i].getColor() == ourColor) {
						return true;
					}
				}
				break;
			case 3:
				for(int i = 1; (kingX + i) < pieceX && (kingY + i) < pieceY; i++) {
					if(board[kingX + i][kingY + i] != null && board[kingX + i][kingY + i].getColor() == ourColor) {
						return true;
					}
				}
				break;
			case 4:
				for(int i = 1; (kingX + i) < pieceX; i++) {
					if(board[kingX + i][kingY] != null && board[kingX + i][kingY].getColor() == ourColor) {
						return true;
					}
				}
				break;
			case 5:
				for(int i = 1; (kingX + i) < pieceX && (kingY - i) > pieceY; i++) {
					if(board[kingX + i][kingY - i] != null && board[kingX + i][kingY - i].getColor() == ourColor) {
						return true;
					}
				}
				break;
			case 6:
				for(int i = 1; (kingY - i) > pieceY; i++) {
					if(board[kingX][kingY - i] != null && board[kingX][kingY - i].getColor() == ourColor) {
						return true;
					}
				}
				break;
			case 7:
				for(int i = 1; (kingX - i) > pieceX && (kingY - i) > pieceY; i++) {
					if(board[kingX - i][kingY - i] != null && board[kingX - i][kingY - i].getColor() == ourColor) {
						return true;
					}
				}
				break;
		}
		// no piece between
		return false;
	}

	private boolean isCheckAfterMove(int kingX, int kingY, Color ourColor, int direction, Piece[][] board) {
		switch (direction) {
			case 0:
				for(int i = 1; (kingX - i) >= 1; i++) {
					if(board[kingX - i][kingY] != null) {
						if(board[kingX - i][kingY].getColor() == ourColor) {
							return false;
						}
						else if(board[kingX - i][kingY] instanceof Queen || board[kingX - i][kingY] instanceof Rook) {
							return true;
						}
					}
				}
				break;
			case 1:
				for(int i = 1; (kingX - i) >= 1 && (kingY + i) <= 8; i++) {
					if(board[kingX - i][kingY + i] != null) {
						if(board[kingX - i][kingY + i].getColor() == ourColor) {
							return false;
						}
						else if(board[kingX - i][kingY + i] instanceof Queen || board[kingX - i][kingY + i] instanceof Bishop) {
							return true;
						}
					}
				}
				break;
			case 2:
				for(int i = 1; (kingY + i) <= 8; i++) {
					if(board[kingX][kingY + i] != null) {
						if(board[kingX][kingY + i].getColor() == ourColor) {
							return false;
						}
						else if(board[kingX][kingY + i] instanceof Queen || board[kingX][kingY + i] instanceof Rook) {
							return true;
						}
					}
				}
				break;
			case 3:
				for(int i = 1; (kingX + i) <= 8 && (kingY + i) <= 8; i++) {
					if(board[kingX + i][kingY + i] != null) {
						if(board[kingX + i][kingY + i].getColor() == ourColor) {
							return false;
						}
						else if(board[kingX + i][kingY + i] instanceof Queen || board[kingX + i][kingY + i] instanceof Bishop) {
							return true;
						}
					}
				}
				break;
			case 4:
				for(int i = 1; (kingX + i) <= 8; i++) {
					if(board[kingX + i][kingY] != null) {
						if(board[kingX + i][kingY].getColor() == ourColor) {
							return false;
						}
						else if(board[kingX + i][kingY] instanceof Queen || board[kingX + i][kingY] instanceof Rook) {
							return true;
						}
					}
				}
				break;
			case 5:
				for(int i = 1; (kingX + i) <= 8 && (kingY - i) >= 1; i++) {
					if(board[kingX + i][kingY - i] != null) {
						if(board[kingX + i][kingY - i].getColor() == ourColor) {
							return false;
						}
						else if(board[kingX + i][kingY - i] instanceof Queen || board[kingX + i][kingY - i] instanceof Bishop) {
							return true;
						}
					}
				}
				break;
			case 6:
				for(int i = 1; (kingY - i) >= 1; i++) {
					if(board[kingX][kingY - i] != null) {
						if(board[kingX][kingY - i].getColor() == ourColor) {
							return false;
						}
						else if(board[kingX][kingY - i] instanceof Queen || board[kingX][kingY - i] instanceof Rook) {
							return true;
						}
					}
				}
				break;
			case 7:
				for(int i = 1; (kingX - i) >= 1 && (kingY - i) >= 1; i++) {
					if(board[kingX - i][kingY - i] != null) {
						if(board[kingX - i][kingY - i].getColor() == ourColor) {
							return false;
						}
						else if(board[kingX - i][kingY - i] instanceof Queen || board[kingX - i][kingY - i] instanceof Bishop) {
							return true;
						}
					}
				}
				break;
		}
		// no check
		return false;
	}

	// get the arraylist of pieces that can block the check made on our king
	public ArrayList<Piece> getBlockingPieces(int kingX, int kingY, int pieceX, int pieceY, int direction, ArrayList<Piece> ourPieces, Piece[][] board) {
		ArrayList<Piece> blockingPieces = new ArrayList<Piece>();
		ArrayList<Piece> aux = new ArrayList<Piece>(ourPieces);

		Random r = new Random();

		// extract from the available pieces the ones that have blocking moves and update their legal moves to contain
		// them only
		while (aux.size() > 0) {
			int randomi = r.nextInt(aux.size());
			Piece p = aux.remove(randomi);

			ArrayList<Position> theTrueMoves =  new ArrayList<Position>();

			for(Position poz : p.getLegalMoves()) {
				switch (direction) {
					case 0:
						for(int i = 1; (kingX - i) > pieceX; i++) {
							if(kingX - i == poz.getX() && kingY == poz.getY()) {
								theTrueMoves.add(poz);
							}
						}
						break;
					case 1:
						for(int i = 1; (kingX - i) > pieceX && (kingY + i) < pieceY; i++) {
							if(kingX - i == poz.getX() && kingY + i == poz.getY()) {
								theTrueMoves.add(poz);
							}
						}
						break;
					case 2:
						for(int i = 1; (kingY + i) < pieceY; i++) {
							if(kingX == poz.getX() && kingY + i == poz.getY()) {
								theTrueMoves.add(poz);
							}
						}
						break;
					case 3:
						for(int i = 1; (kingX + i) < pieceX && (kingY + i) < pieceY; i++) {
							if(kingX + i == poz.getX() && kingY + i == poz.getY()) {
								theTrueMoves.add(poz);
							}
						}
						break;
					case 4:
						for(int i = 1; (kingX + i) < pieceX; i++) {
							if(kingX + i == poz.getX() && kingY == poz.getY()) {
								theTrueMoves.add(poz);
							}
						}
						break;
					case 5:
						for(int i = 1; (kingX + i) < pieceX && (kingY - i) > pieceY; i++) {
							if(kingX + i == poz.getX() && kingY - i == poz.getY()) {
								theTrueMoves.add(poz);
							}
						}
						break;
					case 6:
						for(int i = 1; (kingY - i) > pieceY; i++) {
							if(kingX == poz.getX() && kingY - i == poz.getY()) {
								theTrueMoves.add(poz);
							}
						}
						break;
					case 7:
						for(int i = 1; (kingX - i) > pieceX && (kingY - i) > pieceY; i++) {
							if(kingX - i == poz.getX() && kingY - i == poz.getY()) {
								theTrueMoves.add(poz);
							}
						}
						break;
				}
			}

			// if the current piece has at least 1 blocking move, add it to the blocking pieces list
			p.setLegalMoves(theTrueMoves);

			if(p.getLegalMoves().size() > 0) {
				blockingPieces.add(p);
			}
		}

		// return the arraylist of blocking pieces
		return blockingPieces;
	}
}
