package model.data;

/**
 * Superclass for movable items (now: box,char).
 * @author שדמה
 *
 */
public class MovableItem extends Item implements Movable {

	public MovableItem() {
		//empty
	}

	public MovableItem(int x, int y, String name1) {
		super(x, y, name1);
	}

	@Override
	public void move(String dir) { //change the position of the runtime movable item according to 'dir'

			
		if(dir.equals("left")) {
			this.setPosition(new Position(this.getPosition().getX(),this.getPosition().getY()-1));
		}

		else if(dir.equals("right")) {
			this.setPosition(new Position(this.getPosition().getX(),this.getPosition().getY()+1));
			
		}

		else if(dir.equals("down")) {
			this.setPosition(new Position(this.getPosition().getX()+1,this.getPosition().getY()));
			
		}

		else if(dir.equals("up")) {
			this.setPosition(new Position(this.getPosition().getX()-1,this.getPosition().getY()));
			
		}
		else
		{
			System.out.println("No such direction!");
		}
	}

}
