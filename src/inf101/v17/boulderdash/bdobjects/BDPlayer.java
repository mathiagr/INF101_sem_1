package inf101.v17.boulderdash.bdobjects;

import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;

import java.io.InputStream;

import inf101.v17.boulderdash.Direction;
import inf101.v17.boulderdash.IllegalMoveException;
import inf101.v17.boulderdash.Position;
import inf101.v17.boulderdash.maps.BDMap;

/**
 * An implementation of the player.
 *
 * @author larsjaffke
 * 
 *Graphic source: http://s782.photobucket.com/user/MileHighSaint/media/Miner.png.html
 *
 */
public class BDPlayer extends AbstractBDMovingObject implements IBDKillable {

	/**
	 * Is the player still alive?
	 */
	protected boolean alive = true;

	/**
	 * The direction indicated by keypresses.
	 */
	protected Direction askedToGo;

	/**
	 * Number of diamonds collected so far.
	 */
	protected int diamondCnt = 0;
	
	private ImagePattern image;

	public BDPlayer(BDMap owner) {
		super(owner);
		InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("miner.png");
		image = new ImagePattern(new Image(resourceAsStream), 0, 0, 1.0,1.0, true);
		}
	
	@Override
	public ImagePattern getColor() {
		return image;
		}


	/**
	 * @return true if the player is alive
	 */
	public boolean isAlive() {
		return alive;
	}

	public void keyPressed(KeyCode key) {
	    if (key == KeyCode.LEFT) {
	        askedToGo = Direction.WEST;
	    } else if (key == KeyCode.RIGHT) {
	        askedToGo = Direction.EAST;
	    } else if (key == KeyCode.DOWN) {
	        askedToGo = Direction.SOUTH;
	    } else if (key == KeyCode.UP) {
	        askedToGo = Direction.NORTH;
	    }
	}

	@Override
	public void kill() {
		this.alive = false;
	}

	/**
	 * Returns the number of diamonds collected so far.
	 *
	 * @return
	 */
	public int numberOfDiamonds() {
		return diamondCnt;
	}

	@Override
	public void step() {
		Position cur = owner.getPosition(this);
		
		if(askedToGo != null){
			if(owner.canGo(this, askedToGo)){
				
				Position newPos = cur.moveDirection(askedToGo);
				
			//If player encounters a diamond
				if(owner.get(newPos) instanceof BDDiamond){
					try {
						diamondCnt++;
						this.prepareMove(newPos);
					} catch (IllegalMoveException e) {
						e.printStackTrace();
					}
			//If player encounters a rock		
				}else if(owner.get(newPos) instanceof BDRock){
					BDRock rock = (BDRock) owner.get(newPos);
					try {
						if(rock.push(askedToGo)){
							rock.push(askedToGo);
						}
					} catch (IllegalMoveException e) {
						e.printStackTrace();
					}
			//Player encounters Bug
				}else if(owner.get(newPos) instanceof BDBug){
					this.kill();
			//Player encounters Empty
				}else if(owner.get(newPos) instanceof BDEmpty || owner.get(newPos) instanceof BDSand){
					try {
						this.prepareMove(newPos);
					} catch (IllegalMoveException e) {
						e.printStackTrace();
					}
				}
			}
		}
		//Remember to set askedToGo to null or else it will remain at the last assigned value
		askedToGo=null;
		super.step();
	}
	
	
	@Override
	public boolean isKillable() {
		return true;
	}
}
