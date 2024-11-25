package ca.ulaval.glo2004.domain.teardrop;

public interface Command {
    public TearDropTrailer undo();
    public TearDropTrailer redo();
}