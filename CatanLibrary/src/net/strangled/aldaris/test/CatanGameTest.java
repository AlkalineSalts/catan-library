package net.strangled.aldaris.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.strangled.aldaris.catan.DevelopmentCard;
import net.strangled.aldaris.catan.board.CatanBoard;
import net.strangled.aldaris.catan.board.StandardCatanBoard;
import net.strangled.aldaris.catan.game.CatanGame;
import net.strangled.aldaris.catan.game.CatanGame.GameState;
import net.strangled.aldaris.catan.game.GameStart;
import net.strangled.aldaris.catan.game.Player;
import net.strangled.aldaris.catan.game.RegularPlayPreRoll;
import net.strangled.aldaris.catan.game.command.EndTurn;
import net.strangled.aldaris.catan.game.command.PlaceRoad;
import net.strangled.aldaris.catan.game.command.PlaceSettlement;
import net.strangled.aldaris.catan.math.Line;
import net.strangled.aldaris.catan.math.Point;

class CatanGameTest {
	
	static ArrayList<DevelopmentCard> cardList;
	CatanBoard establishedBoard;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		cardList = new ArrayList<DevelopmentCard>(25);
		
		for (int i = 0; i < 14; i++) {
			cardList.add(DevelopmentCard.KNIGHT);
			if (i < 2) {
				cardList.add(DevelopmentCard.ROAD_BUILDING);
				cardList.add(DevelopmentCard.YEAR_OF_PLENTY);
				cardList.add(DevelopmentCard.MONOPOLY);
			}
			if (i < 5) {cardList.add(DevelopmentCard.VICTORY_POINT);}
		}
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		
		
	}

	@AfterEach
	void tearDown() throws Exception {
		
	}
	
	private GameState getGameState(CatanGame cg) {
		try {
			var f = cg.getClass().getDeclaredField("gameState");
			f.setAccessible(true);
			GameState gstate = (GameState)f.get(cg);
			return gstate;
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			throw new IllegalStateException();
		}   
		
		
	}

	@Test
	void testRunThroughStart() {
		StandardCatanBoard board = new StandardCatanBoard();
		var g = new CatanGame(board, cardList, new Player(1), new Player(2));
		Assert.assertTrue(getGameState(g).getId() == GameStart.ID);
		PlaceSettlement p1s1 = new PlaceSettlement(1, new Point(0, 0));
		PlaceSettlement p1s2 = new PlaceSettlement(1, new Point(3, 2));
		PlaceSettlement p2s1 = new PlaceSettlement(2, new Point(-1, -3));
		PlaceSettlement p2s2 = new PlaceSettlement(2, new Point(-2, -3));
		Assert.assertTrue(g.canProcessCommand(p1s1));
		g.processCommand(p1s1);
		Assert.assertFalse(g.canProcessCommand(p1s2));
		Assert.assertFalse(g.canProcessCommand(p2s2));
		
		
		
		
		//road tests
		PlaceRoad p1r1 = new PlaceRoad(1, new Line(new Point(0,0), new Point(1,1)));
		Assert.assertTrue(g.canProcessCommand(p1r1));
		g.processCommand(p1r1);
		Assert.assertFalse(g.canProcessCommand(p1s2));
		Assert.assertFalse(g.canProcessCommand(p2s1)); //trying to place player 2 settlement
		
		//end turn necessary
		EndTurn end1 = new EndTurn(1);
		EndTurn end2 = new EndTurn(2);
		
		Assert.assertFalse(g.canProcessCommand(end2));
		Assert.assertTrue(g.canProcessCommand(end1));
		
		g.processCommand(end1);
		
		//player 2 first placement
		
		Assert.assertTrue(g.canProcessCommand(p2s1));
		g.processCommand(p2s1);
		
		PlaceRoad p2r1 = new PlaceRoad(2, new Line(new Point(-1, -3), new Point(1, 4)));
		Assert.assertTrue(g.canProcessCommand(p2r1));
		g.processCommand(p2r1);
		
		Assert.assertTrue(g.canProcessCommand(end2));
		g.processCommand(end2);
		
		//player 2 second placement
		
		Assert.assertTrue(g.canProcessCommand(p2s2));
		g.processCommand(p2s2);
		
		PlaceRoad p2r2 = new PlaceRoad(2, new Line(new Point(-2, -3), new Point(3, 4)));
		Assert.assertTrue(g.canProcessCommand(p2r2));
		g.processCommand(p2r2);
		
		Assert.assertTrue(g.canProcessCommand(end2));
		g.processCommand(end2);
		
		//player 1 second placement
		
		Assert.assertTrue(g.canProcessCommand(p1s2));
		g.processCommand(p1s2);
		
		PlaceRoad p1r2 = new PlaceRoad(1, new Line(new Point(3, 2), new Point(-3, -1)));
		Assert.assertTrue(g.canProcessCommand(p1r2));
		g.processCommand(p1r2);
		
		Assert.assertTrue(g.canProcessCommand(end1));
		g.processCommand(end1);
		
		
		
		Assert.assertTrue(getGameState(g).getId() == RegularPlayPreRoll.ID);
		
	}
	
	
	

}
