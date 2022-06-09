package Pieces;

import Proiect.Color;
import Proiect.Game;
import Proiect.Position;

import java.util.ArrayList;

// the class for the queens
public class Queen extends Piece {

    public Queen(Color c, int x, int y) {
        super(c,x,y);
    }

    public void generateMoves(Game game) {
        Piece[][] table = game.getTable();

        int currentX = this.getPosition().getX();
        int currentY = this.getPosition().getY();

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

        // find all the possible moves to the bottom right
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

        // find all the possible moves to the bottom left
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

        // find all the possible moves to the top left
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

        // find all the possible moves to the top right
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

        // set all the remaining moves that the queen can make
        this.setLegalMoves(moves);
    }

    @Override
    public String toString(){
        return "Q";
    }
}
