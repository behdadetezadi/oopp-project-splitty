package client.utils.undoable;

import java.util.Stack;

public class UndoManager {
    private Stack<UndoableCommand> executedCommands;

    /**
     * Constructs a new UndoManager
     * Initializes the stack that will hold the executed commands
     */
    public UndoManager() {
        executedCommands = new Stack<>();
    }

    /**
     * Executes a command and records it for potential undo
     * @param command The command to be executed; cannot be null
     */
    public void executeCommand(UndoableCommand command) {
        command.execute();
        executedCommands.push(command);
    }

    /**
     * Returns the stack of executed commands
     * @return The stack of executed commands
     */
    public Stack<UndoableCommand> getExecutedCommands() {
        return executedCommands;
    }

    /**
     * Undoes the last executed command, if any
     */
    public void undoLastCommand() {
        UndoableCommand undoneCommand;
        if (!executedCommands.isEmpty()) {
            undoneCommand = executedCommands.pop();
            undoneCommand.undo();
        }
    }

}

