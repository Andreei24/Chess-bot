package Pieces;

import Proiect.Color;
import Proiect.Game;
import Proiect.Position;

import java.util.ArrayList;

//the class for the rooks
public class Rook extends Piece {
    private boolean neverMoved;

    public Rook(Color c, int x, int y) {
        super(c, x, y);
        this.neverMoved = true;
    }

    @Override
    public String toString() {
        ;
        return "R";
    }

    public boolean isNeverMoved() {
        return neverMoved;
    }

    public void setNeverMoved(boolean neverMoved) {
        this.neverMoved = neverMoved;
    }

    public void generateMoves(Game game) {
        Piece[][] table = game.getTable();

        // the list containing all the possible moves
        ArrayList<Position> moves = new ArrayList<Position>();

        // find all the possible moves to the bottom
        for(int i = this.getPosition().getX() + 1; i <= 8; i++){
            if(table[i][this.getPosition().getY()] != null){
                if(table[i][this.getPosition().getY()].getColor() == this.getColor())
                    break;
                else {
                    moves.add(new Position(i, this.getPosition().getY()));
                    break;
                }
            }
            else
                moves.add(new Position(i,this.getPosition().getY()));
        }

        // find all the possible moves to the top
        for(int i = this.getPosition().getX() - 1; i >= 1; i--){
            if(table[i][this.getPosition().getY()] != null){
                if(table[i][this.getPosition().getY()].getColor() == this.getColor())
                    break;
                else {
                    moves.add(new Position(i, this.getPosition().getY()));
                    break;
                }
            }
            else
                moves.add(new Position(i, this.getPosition().getY()));
        }

        // find all the possible moves to the right
        for(int i = this.getPosition().getY() + 1; i <= 8; i++){
            if(table[this.getPosition().getX()][i] != null){
                if(table[this.getPosition().getX()][i].getColor() == this.getColor())
                    break;
                else {
                    moves.add(new Position(this.getPosition().getX(), i));
                    break;
                }
            }
            else
                moves.add(new Position(this.getPosition().getX(), i));
        }

        // find all the possible moves to the left
        for(int i = this.getPosition().getY() - 1; i >= 1; i--){
            if(table[this.getPosition().getX()][i] != null){
                if(table[this.getPosition().getX()][i].getColor() == this.getColor())
                    break;
                else {
                    moves.add(new Position(this.getPosition().getX(), i));
                    break;
                }
            }
            else
                moves.add(new Position(this.getPosition().getX(), i));
        }

        // remove all the moves that would leave the king on check
        filterMovesSoNoCheckOnKing(this, moves, game);

        // set all the remaining moves that the rook can make
        this.setLegalMoves(moves);
    }
}