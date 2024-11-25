package ca.ulaval.glo2004.domain.teardrop;

import ca.ulaval.glo2004.domain.controller.UserException;
import java.io.Serializable;
/*
The solution is based on the implementation of Greg Cope that can be found
on https://www.algosome.com/articles/implementing-undo-redo-java.html
*/
public class UndoRedoManager implements Serializable {
    private Node currentIndex = null;
    private Node parentNode = new Node();

    public UndoRedoManager() {
	currentIndex = parentNode;
    }
    
    public UndoRedoManager(Command command) {
        parentNode = new Node(command);
	currentIndex = parentNode;
    }
    
    public UndoRedoManager(UndoRedoManager undoRedoManager ){
	this();
	currentIndex = undoRedoManager.currentIndex;
    }

    public void setParentNode(Command command) {
        Node newParentNode = new Node(command);
        this.parentNode = newParentNode;
    }
    
    public Node getCurrentIndex() {
        return this.currentIndex;
    }
    
    public void clear(){
	currentIndex = parentNode;
    }
    
    public void addCommand(Command command){
        Node node = new Node(command);
        currentIndex.right = node;
        node.left = currentIndex;
        currentIndex = node;
    }
    
    private void moveLeft(){
        if ( currentIndex.left == null ){
            throw new UserException("L'index précédent est nul.");
        }
        currentIndex = currentIndex.left;
    }
    
    private void moveRight(){
        if ( currentIndex.right == null ){
            throw new UserException("L'index suivant est nul.");
        }
        currentIndex = currentIndex.right;
    }
    
    public TearDropTrailer undo(){
        if (currentIndex == parentNode){
            this.addCommand(currentIndex.command);
            return (TearDropTrailer) currentIndex.command;
            //throw new UserException("Aucune modification à annuler.");
        }
        moveLeft();
        return currentIndex.command.undo();
    }
    
    public TearDropTrailer redo(){
        if (currentIndex.right == null){
            throw new UserException("Aucune modification à rétablir.");
        }
        moveRight();
        return currentIndex.command.redo();
    }
    
    private class Node implements Serializable {
        private Node left = null;
        private Node right = null;
        private final Command command;
        public Node(Command c){
                command = c;
        }

        public Node(){
                command = null;
        }
    }
}
