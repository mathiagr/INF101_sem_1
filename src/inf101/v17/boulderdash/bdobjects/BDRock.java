

package inf101.v17.boulderdash.bdobjects;




import java.io.InputStream;

import inf101.v17.boulderdash.Direction;
import inf101.v17.boulderdash.IllegalMoveException;
import inf101.v17.boulderdash.Position;
import inf101.v17.boulderdash.maps.BDMap;

import javafx.scene.paint.ImagePattern;
import javafx.scene.image.Image;

//import javax.swing.text.Position;

/**
 * 
 * Implementation of a Rock
 * 
 * @author mathiasgronstad
 *Graphic source: https://pixabay.com/no/stein-gr%C3%A5-kvadrat-tekstur-575687/
 *
 */



public class BDRock extends AbstractBDFallingObject{

	private ImagePattern image;
	
	public BDRock(BDMap owner) {
		super(owner);

		InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("rock.png");
		image = new ImagePattern(new Image(resourceAsStream), 0, 0, 1.0,1.0, true);
		}
	
		@Override
		public ImagePattern getColor() {
			return image;
		}
/*		@Override
		public Color getColor() {
			return Color.DARKGRAY;
		}
*/	
	public boolean push(Direction dir) throws IllegalMoveException{
		//Exception message
		if(!dir.equals(Direction.WEST) && !dir.equals(Direction.EAST)){
			throw new IllegalMoveException("Direction not EAST or WEST");
		}
		Position cur = owner.getPosition(this);
		Position temp = cur.copy();
		if(owner.canGo(this, dir)){
			//Moving rock West if possible
			if(dir.equals(Direction.WEST)){
				temp=temp.moveDirection(dir);
				if (owner.get(temp) instanceof BDEmpty) {
					this.prepareMove(temp);
					return true;
				}
			}
			//Moving rock East if possible
			if(dir.equals(Direction.EAST)){
				temp=temp.moveDirection(dir);
				if (owner.get(temp) instanceof BDEmpty) {
					this.prepareMove(temp);
					return true;
				}
			}
		}
		return false;
	}
}