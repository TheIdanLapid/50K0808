package controller.commands;
/**
 * A general interface for commands, only method is execute.
 * @author Eon
 *
 */
public interface Command {
	public void execute();
	public boolean isDone();
}
