package model.policy;

import model.data.MovableItem;
import model.data.Position;

/**
 * General Policy Interface.
 * @author ����
 *
 */
public interface Policy {
	public boolean canMove(MovableItem mi, Position p);
}
