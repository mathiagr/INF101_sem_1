package inf101.v17.boulderdash.bdobjects;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;

import java.io.InputStream;

import inf101.v17.boulderdash.maps.BDMap;

/**
 * A diamond object. All its logic is implemented in the abstract superclass.
 *
 * @author larsjaffke
 *
 *Graphic source: http://fashland.wikia.com/wiki/FAQ
 *
 */
public class BDDiamond extends AbstractBDFallingObject {

	private ImagePattern image;
	
	public BDDiamond(BDMap owner) {
		super(owner);
		InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("diamond.png");
		image = new ImagePattern(new Image(resourceAsStream), 0, 0, 1.0,1.0, true);
		}

	@Override
	public ImagePattern getColor() {
		return image;
		}
	

}
