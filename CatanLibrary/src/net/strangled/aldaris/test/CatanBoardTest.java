package net.strangled.aldaris.test;

import net.strangled.aldaris.catan.board.*;
import net.strangled.aldaris.catan.math.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CatanBoardTest {
	CatanBoard board;
	CatanBoard regularGameBoard; //a game that has started
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		
		

	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		 board = new StandardCatanBoard();
		 
		 
		 regularGameBoard = new StandardCatanBoard();
			Point settlement1p1 = new Point(1, 1);
			Point settlement1p2 = new Point(2, 1);
			Point settlement2p1 = new Point(1,3);
			Point settlement2p2 = new Point(3,3);
			regularGameBoard.placeSettlement(settlement1p1, 1);
			regularGameBoard.placeSettlement(settlement1p2, 2);
			regularGameBoard.placeSettlement(settlement2p1, 1);
			regularGameBoard.placeSettlement(settlement2p2, 2);
			
			Line l1p1 = new Line(settlement1p1, new Point(-1, 0));
			Line l1p2 = new Line(settlement1p2, new Point(-2, 0));
			Line l2p1 = new Line(settlement2p1, new Point(-1, -2));
			Line l2p2 = new Line(settlement2p2, new Point(-2, -2));
			
			regularGameBoard.placeRoad(l1p1, 1);
			regularGameBoard.placeRoad(l1p2, 2);
			regularGameBoard.placeRoad(l2p1, 1);
			regularGameBoard.placeRoad(l2p2, 2);
			regularGameBoard.startGame();
	}

	@AfterEach
	void tearDown() throws Exception {
	}
	
	@Test
	void testJson() {
		var jsonObject = regularGameBoard.toJson().build();
		CatanBoard rebuilt = new CatanBoard(jsonObject);
		
		Assert.assertEquals(regularGameBoard, rebuilt);
		
		
		
	}
	
	@Test
	void testPlaceSettlementStartGame() {
		var point = new Point(2, 1);
		Assert.assertTrue(board.canPlaceSettlement(point, 1));
		board.placeSettlement(point, 1);
		Assert.assertNotNull(board.getPointToDataPoint().get(point).getOwner());
		Assert.assertNull(board.getPointToDataPoint().get(new Point(1, 1)).getOwner());
		
		//test point that is off the board
		var invalidPoint = new Point(100000, 1000000);
		Assert.assertFalse(board.canPlaceSettlement(invalidPoint, 1));
		Assert.assertNull(board.getPointToDataPoint().get(invalidPoint));
		
		//test point that is directly adjacent
		var invalidPoint2 = new Point(-2, 0);
		Assert.assertFalse(board.canPlaceSettlement(invalidPoint2, 2));
		board.placeSettlement(invalidPoint2, 2);
		Assert.assertNull(board.getPointToDataPoint().get(invalidPoint2).getOwner());
		
		//test placing two away
		var validPoint2 = new Point(3, 2);
		Assert.assertTrue(board.canPlaceSettlement(validPoint2, 2));
		board.placeSettlement(validPoint2, 2);
		Assert.assertEquals(board.getPointToDataPoint().get(validPoint2).getOwner().intValue(), 2);
	}
	
	@Test 
	void testPlaceRoadStartGame() {
		board.placeSettlement(new Point(2,2), 1);
		
		var validRoadPlacement = new Line(new Point(2, 2), new Point(-1, -1));
		Assert.assertTrue(board.canPlaceRoad(validRoadPlacement, 1));
		board.placeRoad(validRoadPlacement, 1);
		Assert.assertNotNull(board.getLineToDataLine().get(validRoadPlacement).getOwner());
		
		//placing a road connection that doesn't exist
		var invalidRoadPlacement = new Line(new Point(2, 1), new Point(-2, -1));
		Assert.assertFalse(board.canPlaceRoad(invalidRoadPlacement, 1));
		Assert.assertNull(board.getLineToDataLine().get(invalidRoadPlacement));
		
		//placing a road connection around a settlement that already has one
		Line invalidRoadPlacement2 = new Line(new Point(2, 2), new Point(-2, -1));
		Assert.assertFalse(board.canPlaceRoad(invalidRoadPlacement2, 1));
		Assert.assertNull(board.getLineToDataLine().get(invalidRoadPlacement2).getOwner());
		
		
		
	}
	
	@Test
	void testRoadPlacementRegularGame() {
		//tests that exactly four lines are roads
		var catanLineList = regularGameBoard.getLineToDataLine().entrySet().stream().filter(set -> set.getValue().hasOwner()).toList();
		Assert.assertEquals(catanLineList.size(), 4);

		
		
		
		//all roads are of length 1, so both players will have same road size
		Assert.assertEquals(regularGameBoard.getIdsOfLongestRoadOwners().size(), 2);
		Assert.assertEquals(regularGameBoard.getLongestRoadLength(), 1);
		
		
		//test if can place a road over an existing one ()should be false
		var invalidline = new Line(new Point(-2, -2), new Point(3, 3));
		Assert.assertFalse(regularGameBoard.canPlaceRoad(invalidline , 2));
		
		//test if player can place a road with no connecting roads or settlements
		var invalidline2 = new Line(new Point(-3, -3), new Point(3, 4));
		Assert.assertFalse(regularGameBoard.canPlaceRoad(invalidline2, 2));
		
		//test if  can place a road starting from the end of another road
		var validline = new Line(new Point(-2, -2), new Point(3, 4));
		Assert.assertTrue(regularGameBoard.canPlaceRoad(validline, 2));
		regularGameBoard.placeRoad(validline, 2);
		
		//var l = regularGameBoard.getLineToDataLine().entrySet().stream().filter(set -> set.getValue().hasOwner()).toList();
		
		
		//since there is now a road of length 2, player two will have the longest road
		Assert.assertEquals(2, regularGameBoard.getLongestRoadLength());
		Assert.assertEquals(1, regularGameBoard.getIdsOfLongestRoadOwners().size());
		Assert.assertEquals(2, regularGameBoard.getIdsOfLongestRoadOwners().get(0).intValue());
		
		
		//test if we can start a road from a settlement
		var settleRoad = new Line(new Point(1, 1), new Point(0, 0));
		Assert.assertTrue(regularGameBoard.canPlaceRoad(settleRoad, 1));
		//test that the other player cannot extend the first player's roads
		Assert.assertFalse(regularGameBoard.canPlaceRoad(settleRoad, 2));
		
		//
		regularGameBoard.placeRoad(settleRoad, 1);
		Assert.assertEquals(regularGameBoard.getIdsOfLongestRoadOwners().size(), 2);
		
	}
	
	@Test
	public void testSettlementPlacementRegular() {
		Point settlement1p1 = new Point(1, 1);
		//test that cannot place settlement at the same spot as a current settlement
		Assert.assertFalse(regularGameBoard.canPlaceSettlement(settlement1p1, 1));
		regularGameBoard.placeSettlement(settlement1p1, 2);
		Assert.assertEquals(1, regularGameBoard.getPointToDataPoint().get(settlement1p1).getOwner().intValue());
		Point oneAway = new Point(0, 0);
		regularGameBoard.placeRoad(new Line(settlement1p1, oneAway), 1);
		Assert.assertFalse (regularGameBoard.canPlaceSettlement(oneAway, 1));
		Point twoAway = new Point(1, 2);
		//checks that settlement cant be built without road linkage
		Assert.assertFalse(regularGameBoard.canPlaceSettlement(twoAway, 1));
		Line road2 = new Line(oneAway, twoAway);
		regularGameBoard.placeRoad(road2, 1);
		//check that can place settlement, and that p2 can't
		Assert.assertFalse(regularGameBoard.canPlaceSettlement(twoAway, 2));
		regularGameBoard.placeSettlement(twoAway, 2);
		Assert.assertNull(regularGameBoard.getPointToDataPoint().get(twoAway).getOwner());
		Assert.assertTrue(regularGameBoard.canPlaceSettlement(twoAway, 1));
		regularGameBoard.placeSettlement(twoAway, 1);
		Assert.assertEquals(1, regularGameBoard.getPointToDataPoint().get(twoAway).getOwner().intValue());
	}
	
	@Test
	public void makeCityTest() {
		Point settlement1p1 = new Point(1, 1);
		Point notASettlement = new Point(0, 0);
		Point notAPoint = new Point(-1000, 1000);
		Assert.assertFalse(regularGameBoard.canUpgradeToCity(notASettlement, 1));
		Assert.assertFalse(regularGameBoard.canUpgradeToCity(notAPoint, 1));
		Assert.assertFalse(regularGameBoard.canUpgradeToCity(settlement1p1, 2));
		Assert.assertTrue(regularGameBoard.canUpgradeToCity(settlement1p1, 1));
		regularGameBoard.upgradeToCity(settlement1p1, 2);
		Assert.assertFalse(regularGameBoard.getPointToDataPoint().get(settlement1p1).isCity());
		regularGameBoard.upgradeToCity(settlement1p1, 1);
		Assert.assertTrue(regularGameBoard.getPointToDataPoint().get(settlement1p1).isCity());
	}
	
	
	

}
