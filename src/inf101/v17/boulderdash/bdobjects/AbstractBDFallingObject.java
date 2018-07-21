package inf101.v17.boulderdash.bdobjects;

import java.util.Random;

import inf101.v17.boulderdash.Direction;
import inf101.v17.boulderdash.IllegalMoveException;
import inf101.v17.boulderdash.Position;
import inf101.v17.boulderdash.maps.BDMap;

/**
 * Contains most of the logic associated with objects that fall such as rocks
 * and diamonds.
 *
 * @author larsjaffke
 *
 */
public abstract class AbstractBDFallingObject extends AbstractBDKillingObject {

	/**
	 * A timeout between the moment when an object can fall (e.g. the tile
	 * underneath it becomes empty) and the moment it does. This is necessary to
	 * make sure that the player doesn't get killed immediately when walking
	 * under a rock.
	 */
	protected static final int WAIT = 3;

	protected boolean falling = false;
	protected Random r;
	
	
	/**
	 * A counter to keep track when the falling should be executed next, see the
	 * WAIT constant.
	 */
	protected int fallingTimeWaited = 0;

	public AbstractBDFallingObject(BDMap owner) {
		super(owner);
		r = new Random(); //random for choosing which side to fall
	}

	/**
	 * This method implements the logic of the object falling. It checks whether
	 * it can fall, depending on the object in the tile underneath it and if so,
	 * tries to prepare the move.
	 */
	public void fall() {
		// Wait until its time to fall
		if (falling && fallingTimeWaited < WAIT) {
			fallingTimeWaited++;
			return;
		}
		// The timeout is over, try and prepare the move
		fallingTimeWaited = 0;

		Position pos = owner.getPosition(this);
	
		// The object cannot fall if it is on the lowest row.
		if (pos.getY() > 0) {
			try {
				// Get the object in the tile below.
				Position below = pos.moveDirection(Direction.SOUTH);
				IBDObject under = owner.get(below);

				if (falling) {
					// fall one step if tile below is empty or killable
					if (under instanceof BDEmpty || under instanceof IBDKillable) {
						prepareMoveTo(Direction.SOUTH);
					} else {
						falling = false;
					}
				} else {
					// start falling if tile below is empty
					falling = under instanceof BDEmpty;
					fallingTimeWaited = 1;
				}
			} catch (IllegalMoveException e) {
				// This should never happen.
				System.out.println(e);
				System.exit(1);
			}
		}
	}

	@Override
	public void step() {
		// (Try to) fall if possible
		
		fall();
		super.step();
		checkNeighbours(); //We're checking if the Fancy Rock can fall to a side 
		fall();
		super.step();
		
	}

	public void checkNeighbours(){
		Position cur = owner.getPosition(this);
		
		Position down = cur.moveDirection(Direction.SOUTH);
		
		Position left = cur.moveDirection(Direction.WEST);
		Position leftDown = left.moveDirection(Direction.SOUTH);
		
		Position right = cur.moveDirection(Direction.EAST);
		Position rightDown = right.moveDirection(Direction.SOUTH);
		
	//First check if this is a rock. Then rock needs to be falling, and the objects under musd be either, diamond, rock or wall
		if(this instanceof BDRock && this.falling && 
				(owner.get(down) instanceof BDRock || owner.get(down) instanceof BDDiamond || owner.get(down) instanceof BDWall)){
	//If rock can fall both left or right, we make a random choice
			if((owner.get(left) instanceof BDEmpty && owner.get(leftDown) instanceof BDEmpty) &&
				(owner.get(right) instanceof BDEmpty && owner.get(rightDown) instanceof BDEmpty)){
				Boolean fallEast = r.nextBoolean();
				Position newPos = this.getPosition();
				if(fallEast){
					newPos = this.getPosition().moveDirection(Direction.EAST);
				} if(!fallEast) {
					newPos = this.getPosition().moveDirection(Direction.WEST);
				}
				try {
					this.prepareMove(newPos);
				} catch (IllegalMoveException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					}
				}
					
		//If rock can only fall right
			else if(owner.get(right) instanceof BDEmpty && owner.get(rightDown) instanceof BDEmpty){
				Position newPos = this.getPosition().moveDirection(Direction.EAST);
				try {
					this.prepareMove(newPos);
				} catch (IllegalMoveException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					}
		//If rock can only fall left
				} else if(owner.get(left) instanceof BDEmpty && owner.get(leftDown) instanceof BDEmpty){
					Position newPos = this.getPosition().moveDirection(Direction.WEST);
					try {
						this.prepareMove(newPos);
					} catch (IllegalMoveException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						}
					}
		}
		}
		

	@Override
	public boolean isEmpty() {
		return false;
	}
}
