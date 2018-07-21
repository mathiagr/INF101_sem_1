package inf101.v17.boulderdash.bdobjects;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;

import java.io.InputStream;

import inf101.v17.boulderdash.maps.BDMap;

/**
 * Implementation of a piece of a wall.
 *
 * @author larsjaffke
 *
 *Graphic source: http://opengameart.org/node/8015
 *
 */
public class BDWall extends AbstractBDObject {

	private ImagePattern image;
	
	public BDWall(BDMap owner) {
		super(owner);
		InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("wall.png");
		image = new ImagePattern(new Image(resourceAsStream), 0, 0, 1.0,1.0, true);
		}
	
	@Override
	public ImagePattern getColor() {
		return image;
		}


	@Override
	public void step() {
		// DO NOTHING, IT'S A WALL
	}
}
