package client.utils.undoable;

import java.util.Stack;

public class UndoManager {
    private Stack<UndoableCommand> executedCommands;

    public UndoManager() {
        executedCommands = new Stack<>();
    }

    public void executeCommand(UndoableCommand command) {
        command.execute();
        executedCommands.push(command);
    }

    public Stack<UndoableCommand> getExecutedCommands() {
        return executedCommands;
    }

    public UndoableCommand undoLastCommand() {
        UndoableCommand undoneCommand = null;
        if (!executedCommands.isEmpty()) {
            undoneCommand = executedCommands.pop();
            undoneCommand.undo();
        }
        return undoneCommand;
    }

}

