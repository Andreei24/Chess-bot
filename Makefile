FLAGS = -g
JC = javac
JVM = java
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	Pieces/Bishop.java \
	Pieces/King.java \
	Pieces/Knight.java \
	Pieces/Pawn.java \
	Pieces/Piece.java \
	Pieces/Queen.java \
	Pieces/Rook.java \
	Proiect/Color.java \
	Proiect/Game.java \
	Proiect/Position.java \
	Proiect/Main.java

build: classes

classes: $(CLASSES:.java=.class)

run: classes
	$(JVM) Proiect.Main

clean:
	$(RM) Proiect/*.class
	$(RM) Pieces/*.class
