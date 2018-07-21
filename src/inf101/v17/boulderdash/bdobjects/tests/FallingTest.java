package inf101.v17.boulderdash.bdobjects.tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import inf101.v17.boulderdash.Direction;
import inf101.v17.boulderdash.IllegalMoveException;
import inf101.v17.boulderdash.Position;
import inf101.v17.boulderdash.bdobjects.AbstractBDFallingObject;
import inf101.v17.boulderdash.bdobjects.BDDiamond;
import inf101.v17.boulderdash.bdobjects.BDPlayer;
import inf101.v17.boulderdash.bdobjects.BDRock;
import inf101.v17.boulderdash.bdobjects.IBDObject;
import inf101.v17.boulderdash.maps.BDMap;
import inf101.v17.datastructures.IGrid;
import inf101.v17.datastructures.MyGrid;

public class FallingTest {

	private BDMap map;

	@Before
	public void setup() {
		IGrid<Character> grid = new MyGrid<>(2, 5, ' ');
		grid.set(0, 4, 'd');
		grid.set(0, 0, '*');
		map = new BDMap(grid);
	}

	
	@Test
	public void fallingKills1() {
		// diamond two tiles above kills player
		IGrid<Character> grid = new MyGrid<>(2, 5, ' ');
		grid.set(0, 4, 'd');
		grid.set(0, 2, 'p');
		grid.set(0, 0, '*');
		map = new BDMap(grid);
		
		checkFall(new Position(0, 4));
		checkFall(new Position(0, 3));
		checkFall(new Position(0, 2));
		assertFalse(map.getPlayer().isAlive());
	}

	@Test
	public void restingDoesntKill1() {
		// diamond on top of player doesn't kill player
		IGrid<Character> grid = new MyGrid<>(2, 5, ' ');
		grid.set(0, 3, 'd');
		grid.set(0, 2, 'p');
		grid.set(0, 0, '*');
		map = new BDMap(grid);
		
		map.step();
		map.step();
		
		assertTrue(map.getPlayer().isAlive());
	}

	@Test
	public void fallingTest1() {
		IBDObject obj = map.get(0, 4);
		assertTrue(obj instanceof BDDiamond);

		//System.out.println(map.getPosition(obj).toString());
		// 2 steps later, we've fallen down one step
		map.step();
		map.step();
		//System.out.println(map.getPosition(obj).toString());
		assertEquals(obj, map.get(0, 3));

	
		map.step();
		map.step();
		//System.out.println(map.getPosition(obj).toString());
		assertEquals(obj, map.get(0, 2));
		
		
		map.step();
		map.step();
		
		//System.out.println(map.getPosition(obj).toString());
		assertEquals(obj, map.get(0, 1));

		// wall reached, no more falling
		for (int i = 0; i < 10; i++)
			map.step();
		assertEquals(obj, map.get(0, 1));
	}

	
	
	
	//SAME TESTS FOR ROCK
	

	
	@Test
	public void fallingKills1Rock() {
		// Rock two tiles above kills player
		IGrid<Character> grid = new MyGrid<>(2, 5, ' ');
		grid.set(0, 4, 'r');
		grid.set(0, 2, 'p');
		grid.set(0, 0, '*');
		map = new BDMap(grid);
		
		checkFall(new Position(0, 4));
		checkFall(new Position(0, 3));
		checkFall(new Position(0, 2));
		assertFalse(map.getPlayer().isAlive());
	}

	@Test
	public void restingDoesntKill1Rock() {
		// diamond on top of player doesn't kill player
		IGrid<Character> grid = new MyGrid<>(2, 5, ' ');
		grid.set(0, 3, 'r');
		grid.set(0, 2, 'p');
		grid.set(0, 0, '*');
		map = new BDMap(grid);
		
		map.step();
		map.step();
		
		assertTrue(map.getPlayer().isAlive());
	}

	@Test
	public void fallingTest1Rock() {
		IGrid<Character> grid = new MyGrid<>(2, 5, ' ');
		grid.set(0, 4, 'r');
		map = new BDMap(grid);
		
		IBDObject obj = map.get(0, 4);
		
		assertTrue(obj instanceof BDRock);

		//System.out.println(map.getPosition(obj).toString());
		// 2 steps later, we've fallen down one step
		map.step();
		map.step();
		//System.out.println(map.getPosition(obj).toString());
		assertEquals(obj, map.get(0, 3));
		map.step();
		map.step();
		//System.out.println(map.getPosition(obj).toString());
		assertEquals(obj, map.get(0, 2));
		map.step();
		map.step();
		//System.out.println(map.getPosition(obj).toString());
		assertEquals(obj, map.get(0, 1));
		map.step();
		map.step();
		assertEquals(obj, map.get(0, 0));
	}
	@Test
	public void pushRock(){
		IGrid<Character> grid = new MyGrid<>(6, 1, ' ');
		grid.set(4, 0, 'r');
		grid.set(2, 0, 'r');
		map = new BDMap(grid);
		BDRock r = (BDRock) map.get(2, 0);
		BDRock r2 = (BDRock) map.get(4, 0);
		assertTrue(map.get(2,0) instanceof BDRock);
		try {
			r.push(Direction.WEST);
		} catch (IllegalMoveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			r2.push(Direction.EAST);
		} catch (IllegalMoveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		map.step();
		map.step();
		assertTrue(map.get(1,0) instanceof BDRock);
		assertTrue(map.get(5,0) instanceof BDRock);
	}
	protected Position checkFall(Position pos) {
		IBDObject obj = map.get(pos);
		if (obj instanceof AbstractBDFallingObject) {
			Position next = pos.moveDirection(Direction.SOUTH);
			if (map.canGo(next)) {
				IBDObject target = map.get(next);
				if (target.isEmpty() || target.isKillable()) {
				} else {
					next = pos;
				}
			} else {
				next = pos;
			}
			//map.step(); System.out.println(map.getPosition(object));
			map.step();
			map.step();
			//Trenger bare 2 steg for aa flytte objekt, ikke 4 slik det staar her
			//map.step();
			//map.step();
			assertEquals(obj, map.get(next));
			return next;
		} else
			return pos;
	}
}
