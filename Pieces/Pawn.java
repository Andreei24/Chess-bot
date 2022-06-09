package Pieces;

import Proiect.Color;
import Proiect.Game;
import Proiect.Position;
import java.util.ArrayList;
import java.util.Random;

// the class for the pawns
public class Pawn extends Piece {

    private boolean neverMoved; // check if the pawn has never been moved
    private boolean antNeverMoved; // check if the pawn has been moved once at most

    public Pawn() {
        neverMoved = true;
        antNeverMoved = true;
    }

    public boolean isNeverMoved() {
        return neverMoved;
    }

    public boolean isAntNeverMoved() {
        return antNeverMoved;
    }

    public void setAntNeverMoved(boolean antNeverMoved) {
        this.antNeverMoved = antNeverMoved;
    }

    public Pawn(Color c, int x, int y) {
        super(c, x, y);
    }

    @Override
    public String toString() {
        return "P";
    }

    public void setNeverMoved(boolean neverMoved) {
        this.neverMoved = neverMoved;
    }

    // move the current pawn on the current game's board
    @Override
    public void movePiece(Game game) {
        Piece[][] table = game.getTable(); // get the current board
        Random r = new Random();

        // check if the bot is on (if the bot has to move the pawn)
        if (game.getBotStatus()) {
            this.antNeverMoved = this.neverMoved;
            this.neverMoved = false;

            // generate the pawn's possible moves
            ArrayList<Position> pozitii = this.getLegalMoves();

            // choose a random move from the available ones
            int p = r.nextInt(pozitii.size());

            // create the string representing the chosen move
            String move = Character.toString(this.getPosition().getY() + 96) + Character.toString(this.getPosition().getX() + '0');
            move += Character.toString(pozitii.get(p).getY() + 96) + Character.toString(pozitii.get(p).getX() + '0');

            // modify the board using the chosen move
            game.interpretBotMove(move);

            // send to xboard the chosen move
            String out = "move " + move;

            // if the pawn has reached the board's limit, promote it randomly to a new type of piece
            // extract the pawn from the remaining pieces and add the new piece to the list
            ArrayList<String> promotions = new ArrayList<String>();
            promotions.add("r");
            promotions.add("q");
            promotions.add("b");
            promotions.add("n");

            int i = r.nextInt(promotions.size());
            if (game.getBotColor() == Color.BLACK && this.getPosition().getX() == 1){
                game.getRemBlackPieces().remove(this);
                switch (i){
                    case 0 : {
                        table[this.getPosition().getX()][this.getPosition().getY()] = new Rook(Color.BLACK,
                                this.getPosition().getX(), this.getPosition().getY());
                        break;
                    }

                    case 1 : {
                        table[this.getPosition().getX()][this.getPosition().getY()] = new Queen(Color.BLACK,
                                this.getPosition().getX(), this.getPosition().getY());
                        break;
                    }

                    case 2 : {
                        table[this.getPosition().getX()][this.getPosition().getY()] = new Bishop(Color.BLACK,
                                this.getPosition().getX(), this.getPosition().getY());
                        break;
                    }

                    case 3 : {
                        table[this.getPosition().getX()][this.getPosition().getY()] = new Knight(Color.BLACK,
                                this.getPosition().getX(), this.getPosition().getY());
                        break;
                    }

                    default : break;
                }
                game.getRemBlackPieces().add(table[this.getPosition().getX()][this.getPosition().getY()]);
                out += promotions.get(i);
            }
            else if (game.getBotColor() == Color.WHITE && this.getPosition().getX() == 8) {
                game.getRemWhitePieces().remove(this);
                switch (i){
                    case 0 : {
                        table[this.getPosition().getX()][this.getPosition().getY()] = new Rook(Color.WHITE,
                                this.getPosition().getX(), this.getPosition().getY());
                        break;
                    }

                    case 1 : {
                        table[this.getPosition().getX()][this.getPosition().getY()] = new Queen(Color.WHITE,
                                this.getPosition().getX(), this.getPosition().getY());
                        break;
                    }

                    case 2 : {
                        table[this.getPosition().getX()][this.getPosition().getY()] = new Bishop(Color.WHITE,
                                this.getPosition().getX(), this.getPosition().getY());
                        break;
                    }

                    case 3 : {
                        table[this.getPosition().getX()][this.getPosition().getY()] = new Knight(Color.WHITE,
                                this.getPosition().getX(), this.getPosition().getY());
                        break;
                    }

                    default : break;
                }
                game.getRemWhitePieces().add(table[this.getPosition().getX()][this.getPosition().getY()]);
                out += promotions.get(i);
            }
            System.out.println(out);
        }
    }

    // find all the possible moves for the pawn from its current position
    public void generateMoves(Game game) {
        Piece[][] table = game.getTable();
        int offset = this.getColor() == Color.BLACK ? -1 : 1;
        int currentX = this.getPosition().getX();
        int currentY = this.getPosition().getY();

        // the list containing all the possible moves
        ArrayList<Position> moves = new ArrayList<Position>(4);

        if (this.neverMoved) {

            // check if the pawn can move 1 tile forward
            if (table[currentX + offset][currentY] == null) {
                moves.add(new Position(currentX + offset, currentY));

                // check if the pawn can move 2 tiles forward
                if (table[currentX + 2 * offset][currentY] == null)
                    moves.add(new Position(currentX + 2 * offset, currentY));
            }

            // check if the pawn can move diagonally to the left
            if ((currentY - 1) >= 1 && table[currentX + offset][currentY - 1] != null) {
                if(this.getColor()!= table[currentX + offset][currentY - 1].getColor())
                    moves.add(new Position(currentX + offset, currentY - 1));
            }
            // check if the pawn can move diagonally to the right
            if ((currentY + 1) <= 8 && table[currentX + offset][currentY + 1] != null) {
                if(this.getColor()!= table[currentX + offset][currentY + 1].getColor())
                    moves.add(new Position(currentX + offset, currentY + 1));
            }
        } else {
            // check if the pawn can move 1 tile forward
            if ((currentX + offset) >= 1 && (currentX + offset) <= 8 && table[currentX + offset][currentY] == null)
                moves.add(new Position(currentX + offset, currentY));

            // check if the pawn can move diagonally to the left
            if ((currentX + offset) >= 1 && (currentX + offset) <= 8
                    && (currentY - 1) >= 1
                    && table[currentX + offset][currentY - 1] != null)
                if(table[currentX + offset][currentY - 1].getColor()!= this.getColor())
                    moves.add(new Position(currentX + offset, currentY - 1));

            // check if the pawn can move diagonally to the right
            if ((currentX + offset) >= 1 && (currentX + offset) <= 8
                    && (currentY + 1) <= 8
                    && table[currentX + offset][currentY + 1] != null)
                if(table[currentX + offset][currentY + 1].getColor()!= this.getColor())
                    moves.add(new Position(currentX + offset, currentY + 1));
        }

        // if the move leaves the king in check, then it's an invalid move
        filterMovesSoNoCheckOnKing(this, moves, game);

        // return the list of possible moves
        this.setLegalMoves(moves);
    }
}
