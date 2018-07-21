package inf101.v17.boulderdash.bdobjects;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

import inf101.v17.boulderdash.Direction;
import inf101.v17.boulderdash.IllegalMoveException;
import inf101.v17.boulderdash.Position;
import inf101.v17.boulderdash.maps.BDMap;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.ImagePattern;

/**
 * 
 * @author mathiasgronstad
 *
 *Very simple "AI" player which avoids rocks and bugs and seeks diamonds if these are immediately nearby
 *
 *
 */

public class AIBDPlayer extends BDPlayer {


	

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

	public AIBDPlayer(BDMap owner) {
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

	@Override
	public void keyPressed(KeyCode key) {
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
		//Finner en retning som ikke er heeelt idiotisk (i en del av tilfellene)
		Direction askedToGo = ArtificialStupidity(cur);
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
				}else if(owner.get(newPos) instanceof BDBug){
					this.kill();
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
	
	public Direction ArtificialStupidity(Position cur){
			
	//List of possible moves before checking neighbors
		ArrayList<Direction> moveList = new ArrayList<Direction>();
		moveList.add(Direction.EAST);
		moveList.add(Direction.WEST);
		moveList.add(Direction.NORTH);
		moveList.add(Direction.SOUTH);
		
	//Finding neighbors
		IBDObject objEast = owner.get(cur.moveDirection(Direction.EAST));
		IBDObject objWest = owner.get(cur.moveDirection(Direction.WEST));
		IBDObject objNorth = owner.get(cur.moveDirection(Direction.NORTH));
		IBDObject objSouth = owner.get(cur.moveDirection(Direction.SOUTH));
	
	//List of neighbors
		ArrayList<IBDObject> objList = new ArrayList<IBDObject>();
		objList.add(objEast);
		objList.add(objWest);
		objList.add(objNorth);
		objList.add(objSouth);
		
	//Assuming we're not next to a diamond
		Boolean diamondFound = false;
		
	//If the object above us is a rock, we don't go up or down
		if(objList.get(2) instanceof BDRock){
			moveList.remove(3);
			objList.remove(2);
		}
	//Looping over neighbors to neglect obviously stupid moves	
		for(int i = 0; i<moveList.size(); i++){
			//If a neighbor is a diamond, we go there
			if(objList.get(i) instanceof BDDiamond){
				askedToGo = moveList.get(i);
				diamondFound = true;
				break;
			}
	//If neighbor is bug or wall, we don't go there
			IBDObject curObj = objList.get(i);
			if(curObj instanceof BDBug || curObj instanceof BDWall){
				moveList.remove(i);
				objList.remove(i);
			}
		}	
	 //If neighbor is not a diamond we go to one of the other available direction that are not being neglected to far
		if(!diamondFound){
			int r = (int)(Math.random() * moveList.size());
			System.out.println(moveList.size());
			System.out.println(r);
			askedToGo = moveList.get(r);
		}
		return askedToGo;
	}
	@Override
	public boolean isKillable() {
		return true;
	}
}
