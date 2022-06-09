package Pieces;

import Proiect.Color;
import Proiect.Game;
import Proiect.Position;

import java.util.ArrayList;

// the class for the knights
public class Knight extends  Piece{

    public Knight(Color c, int x, int y) {
        super(c,x,y);
    }

    @Override
    public String toString(){ ;
        return "N";
    }

    @Override
    public void generateMoves(Game game) {
        Piece[][] table = game.getTable();
        int currentX = this.getPosition().getX();
        int currentY = this.getPosition().getY();

        // the list containing all the possible moves
        ArrayList<Position> moves = new ArrayList<Position>();


        // generate all the possible moves for the current knight; if the new position is free or it is occupied
        // by an enemy piece, it is available; otherwise, it is not
        if(currentX-1 >= 1){
            if(currentY - 2 >=1 ){
                if(table[currentX - 1][currentY-2] == null || table[currentX-1][currentY-2].getColor()!= this.getColor())
                    moves.add(new Position(currentX - 1, currentY - 2));
            }
            if(currentY + 2 <= 8){
                if(table[currentX - 1][currentY + 2] == null || table[currentX-1][currentY+2].getColor()!= this.getColor())
                    moves.add(new Position(currentX - 1, currentY + 2));
            }
            if(currentX - 2 >= 1){
                if(currentY - 1 >= 1){
                    if(table[currentX - 2][currentY-1] == null || table[currentX-2][currentY-1].getColor()!= this.getColor())
                        moves.add(new Position(currentX - 2, currentY - 1));
                }
                if(currentY + 1 <= 8){
                    if(table[currentX - 2][currentY+1] == null || table[currentX-2][currentY+1].getColor()!= this.getColor())
                        moves.add(new Position(currentX - 2, currentY + 1));
                }

            }
        }

        if(currentX + 1 <=8){
            if(currentY - 2 >=1 ){
                if(table[currentX + 1][currentY-2] == null || table[currentX + 1][currentY-2].getColor()!= this.getColor())
                    moves.add(new Position(currentX + 1, currentY - 2));
            }
            if(currentY + 2 <= 8){
                if(table[currentX + 1][currentY + 2] == null || table[currentX + 1][currentY + 2].getColor()!= this.getColor())
                    moves.add(new Position(currentX + 1, currentY + 2));
            }

            if(currentX + 2 <= 8){
                if(currentY - 1 >= 1){
                    if(table[currentX + 2][currentY - 1] == null || table[currentX + 2][currentY - 1].getColor()!= this.getColor())
                        moves.add(new Position(currentX + 2, currentY - 1));
                }
                if(currentY + 1 <= 8){
                    if(table[currentX + 2][currentY + 1] == null || table[currentX + 2][currentY + 1].getColor()!= this.getColor())
                        moves.add(new Position(currentX + 2, currentY + 1));
                }
            }
        }

        // remove all the moves that would leave the king on check
        filterMovesSoNoCheckOnKing(this, moves, game);

        // set all the remaining moves that the knight can make
        this.setLegalMoves(moves);
    }

}
