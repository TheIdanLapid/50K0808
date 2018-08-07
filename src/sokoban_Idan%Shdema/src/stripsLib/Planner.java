package stripsLib;

import java.util.List;
/**
 * An interface for plan solvers
 * @author שדמה
 *
 */
public interface Planner {

	List<Action> plan(Plannable plannable);
}
