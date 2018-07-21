package inf101.v17.boulderdash.bdobjects.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import inf101.v17.boulderdash.Direction;
import inf101.v17.boulderdash.Position;
import inf101.v17.boulderdash.bdobjects.AbstractBDFallingObject;
import inf101.v17.boulderdash.bdobjects.BDBug;
import inf101.v17.boulderdash.bdobjects.BDDiamond;
import inf101.v17.boulderdash.bdobjects.IBDObject;
import inf101.v17.boulderdash.maps.BDMap;
import inf101.v17.datastructures.IGrid;
import inf101.v17.datastructures.MyGrid;

public class BugTest {

	private BDMap map;

	@Before
	public void setup() {
	}

	@Test
	public void bugMoves() {
		IGrid<Character> grid = new MyGrid<>(4, 4, ' ');
		grid.set(2, 2, 'b');
		map = new BDMap(grid);

		// find the bug
		Position bugPos = new Position(2,2);
		IBDObject bug = map.get(bugPos);
		assertTrue(bug instanceof BDBug);
		
		for(int i = 0; i < 100; i++) {
			map.step();
			if(map.get(bugPos) != bug) { // bug has moved
				// reported position should be different
				assertNotEquals(bugPos, map.getPosition(bug));
				// bug moved –  we're done
				return;
			}	
		}
		fail("Bug should have moved in 100 steps!");
	}

	
	@Test
	public void bugWallSurround() {
		IGrid<Character> grid = new MyGrid<>(4, 4, '#');
		grid.set(2, 2, 'b');
		map = new BDMap(grid);

		// find the bug
		Position bugPos = new Position(2,2);
		IBDObject bug = map.get(bugPos);
		assertTrue(bug instanceof BDBug);
		
		for(int i = 0; i < 100; i++) {
			map.step();
			if(map.get(bugPos) == bug) { // bug has not moved
				// reported position should be same
				assertEquals(bugPos, map.getPosition(bug));
				// bug not moved –  we're done
				return;
			}
		}
		
		fail("Bug has moved!");
	}
	
	@Test
	public void bugSandSurround() {
		IGrid<Character> grid = new MyGrid<>(4, 4, '*');
		grid.set(2, 2, 'b');
		map = new BDMap(grid);

		// find the bug
		Position bugPos = new Position(2,2);
		IBDObject bug = map.get(bugPos);
		assertTrue(bug instanceof BDBug);
		
		for(int i = 0; i < 100; i++) {
			map.step();
			if(map.get(bugPos) == bug) { // bug has not moved
				// reported position should be same
				assertEquals(bugPos, map.getPosition(bug));
				// bug not moved –  we're done
				return;
			}
		}
		
		fail("Bug has moved!");
	}
	
	@Test
	public void bugCheckPositions() {
		IGrid<Character> grid = new MyGrid<>(4, 4, ' ');
		grid.set(2, 2, 'b');
		map = new BDMap(grid);

		// find the bug
		Position bugPos = new Position(2,2);
		IBDObject bug = map.get(bugPos);
		assertTrue(bug instanceof BDBug);
		
		//Making a list of possible bug positions to check against
		Position pos1 = new Position(2,2);
		Position pos2 = new Position(1,2);
		Position pos3 = new Position(1,3);
		Position pos4 = new Position(2,3);
		
		ArrayList<Position> positions = new ArrayList<Position>();
		positions.add(pos1);
		positions.add(pos2);
		positions.add(pos3);
		positions.add(pos4);
		
		//Counter for keeping count of bug moves
		int moves = 0;
		

		for(int i = 0; i < 100; i++) {
			map.step();
			Position newPos = map.getPosition(bug);
		 //If bug has moved:
			if(!newPos.equals(bugPos)) { 
				moves++;
				//Checking if the move made is the next possible move
				if(moves == 4) moves=0;
				assertEquals(newPos, positions.get(moves));
				bugPos=newPos;
			}	
			}
	}
	
	@Test
	public void bugKillsPlayer() {
		IGrid<Character> grid = new MyGrid<>(4, 4, ' ');
		grid.set(2, 2, 'b');
		grid.set(1, 1, 'p');
		map = new BDMap(grid);

		
		for(int i = 0; i < 100; i++) {
			map.step();
		}
		//Check if player is alive after 100 steps
		assert(map.getPlayer().isAlive());		
	}
	
	
	
}
