package Pieces;

import Proiect.Color;
import Proiect.Game;
import Proiect.Position;

import java.util.ArrayList;

// the class for the kings
public class King extends Piece{
    // the current number of checks the king has
    private int CheckNumber;
    private boolean neverMoved; // if the king has made any move

    public int getCheckNumber() {
        return CheckNumber;
    }

    public boolean isNeverMoved() {
        return neverMoved;
    }

    public King(Color c, int x, int y) {
        super(c,x,y);
        this.CheckNumber = 0;
        this.neverMoved = true;
    }

    public void setCheckNumber(int checkNumber) {
        CheckNumber = checkNumber;
    }

    public void setNeverMoved(boolean neverMoved) {
        this.neverMoved = neverMoved;
    }

    @Override
    public String toString(){
        return "K";
    }

    // check if the king is on check and the return the piece that attacks it
    public Piece isCheck(Piece[][] table, Position position){

        int currentX = position.getX();
        int currentY = position.getY();

        //check if a bishop or queen attacks the king on the diagonal from bottom right
        int i = currentX + 1;
        int j = currentY + 1;

        while (i <= 8 && j <= 8){
            if(table[i][j] == null){
                i++;
                j++;
                continue;
            }
            if(table[i][j].getColor() == this.getColor()){
                break;
            }
            else{
                if(table[i][j] instanceof Bishop || table[i][j] instanceof Queen){
                    return table[i][j];
                }
                else{
                    break;
                }
            }
        }

        //check if a bishop or queen attacks the king on the diagonal from top left
        i = currentX + 1;
        j = currentY - 1;

        while (i <= 8 && j >= 1){
            if(table[i][j] == null){
                i++;
                j--;
                continue;
            }
            if(table[i][j].getColor() == this.getColor()){
                break;
            }
            else{
                if(table[i][j] instanceof Bishop || table[i][j] instanceof Queen){
                    return table[i][j];
                }
                else{
                    break;
                }
            }
        }

        //check if a bishop or queen attacks the king on the diagonal from bottom left
        i = currentX - 1;
        j = currentY - 1;

        while (i >= 1  && j >= 1){
            if(table[i][j] == null){
                i--;
                j--;
                continue;
            }
            if(table[i][j].getColor() == this.getColor()){
                break;
            }
            else{
                if(table[i][j] instanceof Bishop || table[i][j] instanceof Queen){
                    return table[i][j];
                }
                else{
                    break;
                }
            }
        }

        //check if a bishop or queen attacks the king on the diagonal from top right
        i = currentX - 1;
        j = currentY + 1;

        while (i >= 1 && j <= 8){
            if(table[i][j] == null){
                i--;
                j++;
                continue;
            }
            if(table[i][j].getColor() == this.getColor()){
                break;
            }
            else{
                if(table[i][j] instanceof Bishop || table[i][j] instanceof Queen){
                    return table[i][j];
                }
                else{
                    break;
                }
            }
        }

        // check if a rook or a queen attacks the king from the bottom
        for(i = currentX + 1; i <= 8; i++){
            if(table[i][currentY] == null){
                continue;
            }

            if(table[i][currentY].getColor() == this.getColor()){
                break;
            }
            else{
                if(table[i][currentY] instanceof Rook || table[i][currentY] instanceof Queen){
                    return table[i][currentY];
                }
                else{
                    break;
                }
            }
        }

        // check if a rook or a queen attacks the king from the top
        for(i = currentX - 1; i >= 1; i--){
            if(table[i][currentY] == null){
                continue;
            }

            if(table[i][currentY].getColor() == this.getColor()){
                break;
            }
            else{
                if(table[i][currentY] instanceof Rook || table[i][currentY] instanceof Queen){
                    return table[i][currentY];
                }
                else{
                    break;
                }
            }
        }

        // check if a rook or a queen attacks the king from the right
        for(i = currentY + 1; i <= 8; i++){
            if(table[currentX][i] == null){
                continue;
            }

            if(table[currentX][i].getColor() == this.getColor()){
                break;
            }
            else{
                if(table[currentX][i] instanceof Rook || table[currentX][i] instanceof Queen){
                    return table[currentX][i];
                }
                else{
                    break;
                }
            }
        }

        // check if a rook or a queen attacks the king from the left
        for(i = currentY - 1; i >= 1; i--){
            if(table[currentX][i] == null){
                continue;
            }

            if(table[currentX][i].getColor() == this.getColor()){
                break;
            }
            else{
                if(table[currentX][i] instanceof Rook || table[currentX][i] instanceof Queen){
                    return table[currentX][i];
                }
                else{
                    break;
                }
            }
        }


        // check if a knight attacks the king
        if(currentX - 1 >= 1 && currentY + 2 <= 8)
            if(table[currentX - 1][currentY + 2] instanceof Knight && table[currentX - 1][currentY + 2].getColor()!= this.getColor())
                return table[currentX - 1][currentY + 2];
        if(currentX - 2 >= 1 && currentY + 1 <= 8)
            if(table[currentX - 2][currentY + 1] instanceof Knight && table[currentX - 2][currentY + 1].getColor()!= this.getColor())
                return table[currentX - 2][currentY + 1];
        if(currentX + 1 <= 8 && currentY + 2 <= 8)
            if(table[currentX + 1][currentY + 2] instanceof Knight && table[currentX + 1][currentY + 2].getColor()!= this.getColor())
                return table[currentX + 1][currentY + 2];
        if(currentX + 2 <= 8 && currentY + 1 <= 8)
            if(table[currentX + 2][currentY + 1] instanceof Knight && table[currentX + 2][currentY + 1].getColor()!= this.getColor())
                return table[currentX + 2][currentY + 1];
        if(currentX + 1 <= 8 && currentY - 2 >= 1)
            if(table[currentX + 1][currentY - 2] instanceof Knight && table[currentX + 1][currentY - 2].getColor()!= this.getColor())
                return table[currentX + 1][currentY - 2];
        if(currentX + 2 <= 8 && currentY - 1 >= 1)
            if(table[currentX + 2][currentY - 1] instanceof Knight && table[currentX + 2][currentY - 1].getColor()!= this.getColor())
                return table[currentX + 2][currentY - 1];
        if(currentX - 1 >= 1 && currentY - 2 >= 1)
            if(table[currentX - 1][currentY - 2] instanceof Knight && table[currentX - 1][currentY - 2].getColor()!= this.getColor())
                return table[currentX - 1][currentY - 2];
        if(currentX - 2 >= 1 && currentY - 1 >= 1)
            if(table[currentX - 2][currentY - 1] instanceof Knight && table[currentX - 2][currentY - 1].getColor()!= this.getColor())
                return table[currentX - 2][currentY - 1];


        // check if a pawn attacks the king
        int offset = this.getColor()== Color.BLACK ? -1:1;

        if(table[currentX + offset][currentY + 1] instanceof Pawn && table[currentX + offset][currentY + 1].getColor() != this.getColor()) {
            return table[currentX + offset][currentY + 1];
        }
        if(table[currentX + offset][currentY - 1] instanceof Pawn && table[currentX + offset][currentY - 1].getColor() != this.getColor()) {
            return table[currentX + offset][currentY - 1];
        }

        // check if another king attacks our king
        if(table[currentX - 1][currentY -1] instanceof King) {
            return table[currentX - 1][currentY -1];
        }
        if(table[currentX - 1][currentY] instanceof King) {
            return table[currentX - 1][currentY];
        }
        if(table[currentX - 1][currentY + 1] instanceof King) {
            return table[currentX - 1][currentY + 1];
        }
        if(table[currentX][currentY -1] instanceof King) {
            return table[currentX][currentY -1];
        }
        if(table[currentX][currentY + 1] instanceof King) {
            return table[currentX][currentY + 1];
        }
        if(table[currentX + 1][currentY -1] instanceof King) {
            return table[currentX + 1][currentY -1];
        }
        if(table[currentX + 1][currentY] instanceof King) {
            return table[currentX + 1][currentY];
        }
        if(table[currentX + 1][currentY + 1] instanceof King) {
            return table[currentX + 1][currentY + 1];
        }

        return null;
    }

    // generate all the moves for the current king
    @Override
    public void generateMoves(Game game){
        Piece[][] table = game.getTable();
        ArrayList <Position> moves = new ArrayList<Position>();
        int currentX = this.getPosition().getX();
        int currentY = this.getPosition().getY();

        // set the current position of the king to be null, so that the isCheck() function works properly
        table[currentX][currentY] = null;

        // for all the 8 normal moves that the king can make, check if the new position is free or occupied
        // by one of the enemy's pieces
        if(currentX - 1 >= 1 && currentY - 1 >= 1)
            if((table[currentX - 1][currentY - 1] == null ||
                    table[currentX - 1][currentY - 1].getColor() != this.getColor()) &&
                    isCheck(table,new Position(currentX -1, currentY - 1)) == null)
                moves.add(new Position(currentX - 1, currentY - 1));

        if(currentX - 1 >= 1)
            if((table[currentX - 1][currentY] == null ||
                    table[currentX - 1][currentY].getColor() != this.getColor()) &&
                    isCheck(table,new Position(currentX -1, currentY)) == null)
                moves.add(new Position(currentX - 1, currentY));

        if(currentX - 1 >= 1 && currentY + 1 <= 8)
            if((table[currentX - 1][currentY + 1] == null ||
                    table[currentX - 1][currentY + 1].getColor() != this.getColor()) &&
                    isCheck(table,new Position(currentX -1, currentY + 1)) == null)
                moves.add(new Position(currentX - 1, currentY + 1));

        if(currentY - 1 >= 1)
            if((table[currentX][currentY - 1] == null ||
                    table[currentX][currentY - 1].getColor() != this.getColor())&&
                    isCheck(table,new Position(currentX, currentY - 1)) == null)
                moves.add(new Position(currentX, currentY - 1));

        if(currentY + 1 <= 8)
            if((table[currentX][currentY + 1] == null ||
                    table[currentX][currentY + 1].getColor() != this.getColor()) &&
                    isCheck(table,new Position(currentX, currentY + 1)) == null)
                moves.add(new Position(currentX, currentY + 1));

        if(currentX + 1 <= 8 && currentY - 1 >= 1)
            if((table[currentX + 1][currentY - 1] == null ||
                    table[currentX + 1][currentY - 1].getColor() != this.getColor()) &&
                    isCheck(table,new Position(currentX + 1, currentY - 1)) == null)
                moves.add(new Position(currentX + 1, currentY - 1));

        if(currentX + 1 <= 8)
            if((table[currentX + 1][currentY] == null ||
                    table[currentX + 1][currentY].getColor() != this.getColor()) &&
                    isCheck(table,new Position(currentX + 1, currentY)) == null)
                moves.add(new Position(currentX + 1, currentY));

        if(currentX + 1 <= 8 && currentY + 1 <= 8)
            if((table[currentX + 1][currentY + 1] == null ||
                    table[currentX + 1][currentY + 1].getColor() != this.getColor()) &&
                    isCheck(table,new Position(currentX + 1, currentY + 1)) == null)
                moves.add(new Position(currentX + 1, currentY + 1));

        // restore the king on the board after the positions have been found
        table[currentX][currentY] = this;

        this.setLegalMoves(moves);
    }
}
