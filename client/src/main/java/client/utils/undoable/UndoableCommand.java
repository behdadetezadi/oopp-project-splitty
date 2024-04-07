package client.utils.undoable;

public interface UndoableCommand
{
    /**
     * Execute the command.
     */
    void execute();

    /**
     * Undo the command, reverting the effects of the execute method.
     */
    void undo();
}
