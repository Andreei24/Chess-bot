package Pieces;
import Proiect.Color;
import Proiect.Game;
import Proiect.Position;

import java.util.ArrayList;

// the class for the bishops
public class Bishop  extends Piece {

    public Bishop(Color c, int x, int y) {
        super(c,x,y);
    }

    // find all the possible moves for the bishop from its current position
    public void generateMoves(Game game) {

        Piece[][] table = game.getTable();

        // the current position
        int currentX = this.getPosition().getX();
        int currentY = this.getPosition().getY();

        // the list containing all the possible moves
        ArrayList<Position> moves = new ArrayList<Position>();


        // find all the possible moves to bottom right
        int i = currentX + 1;
        int j = currentY + 1;

        while(i <= 8 && j <= 8) {
            if(table[i][j] == null) {
                moves.add(new Position(i, j));
                i++;
                j++;
            }
            else if(table[i][j].getColor() != this.getColor()) {
                moves.add(new Position(i, j));
                break;
            }
            else {
                break;
            }
        }

        // find all the possible moves to bottom left
        i = currentX + 1;
        j = currentY - 1;

        while(i <= 8 && j >= 1) {
            if(table[i][j] == null) {
                moves.add(new Position(i, j));
                i++;
                j--;
            }
            else if(table[i][j].getColor() != this.getColor()) {
                moves.add(new Position(i, j));
                break;
            }
            else {
                break;
            }
        }

        // find all the possible moves to top left
        i = currentX - 1;
        j = currentY - 1;

        while(i >= 1 && j >= 1) {
            if(table[i][j] == null) {
                moves.add(new Position(i, j));
                i--;
                j--;
            }
            else if(table[i][j].getColor() != this.getColor()) {
                moves.add(new Position(i, j));
                break;
            }
            else {
                break;
            }
        }

        // find all the possible moves to top right
        i = currentX - 1;
        j = currentY + 1;

        while(i >= 1 && j <= 8) {
            if(table[i][j] == null) {
                moves.add(new Position(i, j));
                i--;
                j++;
            }
            else if(table[i][j].getColor() != this.getColor()) {
                moves.add(new Position(i, j));
                break;
            }
            else {
                break;
            }
        }

        // remove all the moves that would leave the king on check
        filterMovesSoNoCheckOnKing(this, moves, game);

        // set all the remaining moves that the bishop can make
        this.setLegalMoves(moves);
    }

    // show the piece's type
    @Override
    public String toString(){
        return "B";
    }
}
