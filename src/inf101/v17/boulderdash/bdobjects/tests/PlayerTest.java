package inf101.v17.boulderdash.bdobjects.tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import inf101.v17.boulderdash.Direction;
import inf101.v17.boulderdash.Position;
import inf101.v17.boulderdash.bdobjects.BDPlayer;
import inf101.v17.boulderdash.bdobjects.BDRock;
import inf101.v17.boulderdash.maps.BDMap;
import inf101.v17.datastructures.IGrid;
import inf101.v17.datastructures.MyGrid;
import javafx.scene.input.KeyCode;

public class PlayerTest {
	
	private BDMap map;

	@Before
	public void setup() {
	}

	@Test
	/**
	 * Testing if moving in circle gets us back to original position
	 */
	public void playerMoveCircle() {
		IGrid<Character> grid = new MyGrid<>(4, 4, ' ');
		grid.set(2, 2, 'p');
		map = new BDMap(grid);
		// find the player
		Position playerPos = new Position(2,2);
		BDPlayer player = (BDPlayer) map.get(playerPos);
	
		player.keyPressed(KeyCode.LEFT);
		map.step();
		playerPos=playerPos.moveDirection(Direction.WEST);
		assertEquals(map.getPosition(player), playerPos);		
		
		player.keyPressed(KeyCode.UP);
		map.step();
		playerPos=playerPos.moveDirection(Direction.NORTH);
		assertEquals(map.getPosition(player), playerPos);
		
		player.keyPressed(KeyCode.RIGHT);
		map.step();
		playerPos=playerPos.moveDirection(Direction.EAST);
		assertEquals(map.getPosition(player), playerPos);
		
		player.keyPressed(KeyCode.DOWN);
		map.step();
		playerPos=playerPos.moveDirection(Direction.SOUTH);
		assertEquals(map.getPosition(player), playerPos);
		
	}
	
	@Test
	/**
	 * Testing if moving into bug kills player
	 */
	public void playerMeetsBug() {
		IGrid<Character> grid = new MyGrid<>(4, 4, ' ');
		grid.set(2, 2, 'p');
		grid.set(1, 3, 'b');
		map = new BDMap(grid);

		// find the player
		Position playerPos = new Position(2,2);
		BDPlayer player = (BDPlayer) map.get(playerPos);
				
		player.keyPressed(KeyCode.LEFT);
		map.step();
		
		player.keyPressed(KeyCode.UP);
		map.step();		
	
		
		// check if player is dead
		assert(!map.getPlayer().isAlive());		
	}
	
	@Test
	/**
	 * Testing if player collects diamonds
	 */
	public void playerCollectDiamonds() {
		IGrid<Character> grid = new MyGrid<>(4, 4, ' ');
		grid.set(2, 0, 'p');
		grid.set(3, 0, 'd');
		grid.set(1, 0, 'd');
		map = new BDMap(grid);
		// find the player
		Position playerPos = new Position(2,0);
		BDPlayer player = (BDPlayer) map.get(playerPos);
		//assertEquals(player.numberOfDiamonds(), 0);
		player.keyPressed(KeyCode.RIGHT);
		map.step();
		player.keyPressed(KeyCode.LEFT);
		map.step();
		player.keyPressed(KeyCode.LEFT);
		map.step();
		// Check if player collected diamonds
		assertEquals(player.numberOfDiamonds(), 2);
	}
	
	@Test
	/**
	 * Test if rock kills player
	 */
	public void playerKilledByRock() {
		IGrid<Character> grid = new MyGrid<>(4, 4, ' ');
		grid.set(1, 0, 'p');
		grid.set(1, 3, 'r');
		map = new BDMap(grid);
		// find the player
		Position playerPos = new Position(1,0);
		BDPlayer player = (BDPlayer) map.get(playerPos);
		Position rockPos = new Position(1,3);
		BDRock rock = (BDRock) map.get(rockPos);
		map.step();
		map.step();
		map.step();
		map.step();
		map.step();
		map.step();
		// check if player is dead
		assertFalse(map.getPlayer().isAlive());		
	}	
}