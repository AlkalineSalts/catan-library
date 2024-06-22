package net.strangled.aldaris.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.strangled.aldaris.catan.DevelopmentCard;
import net.strangled.aldaris.catan.Resource;
import net.strangled.aldaris.catan.board.CatanBoard;
import net.strangled.aldaris.catan.board.StandardCatanBoard;
import net.strangled.aldaris.catan.game.CatanGame;
import net.strangled.aldaris.catan.game.CatanGame.GameState;
import net.strangled.aldaris.catan.game.CommandFactory;
import net.strangled.aldaris.catan.game.GameStart;
import net.strangled.aldaris.catan.game.GameStateFactory;
import net.strangled.aldaris.catan.game.Player;
import net.strangled.aldaris.catan.game.RegularPlayPostRoll;
import net.strangled.aldaris.catan.game.RegularPlayPreRoll;
import net.strangled.aldaris.catan.game.ThiefMove;
import net.strangled.aldaris.catan.game.command.BuildDevelopmentCard;
import net.strangled.aldaris.catan.game.command.EndTurn;
import net.strangled.aldaris.catan.game.command.MoveThiefTo;
import net.strangled.aldaris.catan.game.command.PlaceRoad;
import net.strangled.aldaris.catan.game.command.PlaceSettlement;
import net.strangled.aldaris.catan.game.command.PlayDevelopmentCard;
import net.strangled.aldaris.catan.game.command.RollDice;
import net.strangled.aldaris.catan.math.Line;
import net.strangled.aldaris.catan.math.Point;

class CatanGameTest {
	
	static ArrayList<DevelopmentCard> cardList;
	CatanBoard establishedBoard;
	Random r;
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
		r = new Random(100);
		
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
	
	private CatanGame setUpCatanGameToRegular(List<DevelopmentCard> cardList) {
		StandardCatanBoard board = new StandardCatanBoard(r);
		var g = new CatanGame(board, cardList, new Player(1), new Player(2));
		PlaceSettlement p1s1 = new PlaceSettlement(1, new Point(0, 0));
		PlaceSettlement p1s2 = new PlaceSettlement(1, new Point(3, 2));
		PlaceSettlement p2s1 = new PlaceSettlement(2, new Point(-1, -3));
		PlaceSettlement p2s2 = new PlaceSettlement(2, new Point(-2, -3));
		g.processCommand(p1s1);
		
		//road tests
		PlaceRoad p1r1 = new PlaceRoad(1, new Line(new Point(0,0), new Point(1,1)));
		g.processCommand(p1r1);
		
		//end turn necessary
		EndTurn end1 = new EndTurn(1);
		EndTurn end2 = new EndTurn(2);		
		g.processCommand(end1);
		
		//player 2 first placement
		g.processCommand(p2s1);
		
		PlaceRoad p2r1 = new PlaceRoad(2, new Line(new Point(-1, -3), new Point(1, 4)));
		g.processCommand(p2r1);
		
		g.processCommand(end2);
		
		//player 2 second placement
		g.processCommand(p2s2);
		
		PlaceRoad p2r2 = new PlaceRoad(2, new Line(new Point(-2, -3), new Point(3, 4)));
		g.processCommand(p2r2);
		
		g.processCommand(end2);
		
		//player 1 second placement
		g.processCommand(p1s2);
		
		PlaceRoad p1r2 = new PlaceRoad(1, new Line(new Point(3, 2), new Point(-3, -1)));
		g.processCommand(p1r2);
		
		g.processCommand(end1);
		return g;
	}

	@Test
	void testRunThroughStart() {
		StandardCatanBoard board = new StandardCatanBoard(r);
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
	
	private RollDice getRollDice(int playerId, int num) {
		var jsonStr = String.format("{\"id\": %d, \"playerTakingAction\":%d, \"rolledNumber\": %d}", RollDice.ID, playerId, num);
		JsonReader jsonReader = Json.createReader(new StringReader(jsonStr));
		JsonObject object = jsonReader.readObject();
		jsonReader.close();
		return (RollDice)CommandFactory.get().getCommand(object);
	}
	
	private void addDevelopmentCardResourcesToPlayer(Player player) {
		try {
			Map<Resource, Integer> devCardRecipe = new EnumMap<Resource, Integer>(Resource.class);
			devCardRecipe.put(Resource.SHEEP, 1);
			devCardRecipe.put(Resource.ORE, 1);
			devCardRecipe.put(Resource.WHEAT, 1);
			var addMethod = player.getClass().getDeclaredMethod("giveTheseResources", Map.class);
			addMethod.setAccessible(true);
			addMethod.invoke(player, devCardRecipe);
		} catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
			e.printStackTrace();
			throw new IllegalStateException();
		}
	}
	
	
	@Test
	public void testDevelopmentCards() {
		List<DevelopmentCard> exampleCards = new ArrayList<DevelopmentCard>(5);
		exampleCards.add(DevelopmentCard.KNIGHT);
		exampleCards.add(DevelopmentCard.MONOPOLY);
		exampleCards.add(DevelopmentCard.ROAD_BUILDING);
		exampleCards.add(DevelopmentCard.VICTORY_POINT);
		exampleCards.add(DevelopmentCard.YEAR_OF_PLENTY);
		CatanGame game = setUpCatanGameToRegular(exampleCards);
		var playerList = game.getPlayerData();
		var rollDice1 = getRollDice(1, 4);
		var rollDice2 = getRollDice(2, 4);
		var end1 = new EndTurn(1);
		var end2 = new EndTurn(2);
		Assert.assertEquals(this.getGameState(game), GameStateFactory.get().getGameState(RegularPlayPreRoll.ID));
		BuildDevelopmentCard card1 = new BuildDevelopmentCard(1);
		BuildDevelopmentCard card2 = new BuildDevelopmentCard(1);
		BuildDevelopmentCard card3 = new BuildDevelopmentCard(1);
		BuildDevelopmentCard card4 = new BuildDevelopmentCard(1);
		BuildDevelopmentCard card5 = new BuildDevelopmentCard(1);
		Assert.assertFalse(game.canProcessCommand(card1));
		BuildDevelopmentCard[] buildCards = {card1, card2, card3, card4, card5};
		Player player1 = game.getPlayerData().get(1);
		for (int i = 0; i < 5; i++) {
			addDevelopmentCardResourcesToPlayer(player1);
			Assert.assertTrue(game.canProcessCommand(buildCards[i]));
			game.processCommand(buildCards[i]);
			Assert.assertEquals(exampleCards.get(i), buildCards[i].getBuiltDevelopmentCard());
		}
		PlayDevelopmentCard pcard1 = new PlayDevelopmentCard(1, DevelopmentCard.KNIGHT);
		PlayDevelopmentCard pcard2 = new PlayDevelopmentCard(1, DevelopmentCard.MONOPOLY);
		PlayDevelopmentCard pcard3 = new PlayDevelopmentCard(1, DevelopmentCard.ROAD_BUILDING);
		PlayDevelopmentCard pcard4 = new PlayDevelopmentCard(1, DevelopmentCard.VICTORY_POINT);
		PlayDevelopmentCard pcard5 = new PlayDevelopmentCard(1, DevelopmentCard.YEAR_OF_PLENTY);
		PlayDevelopmentCard[] playCards = {pcard1, pcard2, pcard3, pcard4, pcard5};
		Assert.assertFalse(game.canProcessCommand(pcard1));
		
		game.processCommand(rollDice1);
		game.processCommand(end1);
		
		
		
		
	}
	@Test
	public void thiefTest() {
		CatanGame game = setUpCatanGameToRegular(cardList);
		var rollDice1 = getRollDice(1, 7);
		var end1 = new EndTurn(1);
		var end2 = new EndTurn(2);
		game.processCommand(rollDice1);
		var testingHexagon = new Point(2, 3);
		var moveThief = new MoveThiefTo(1, testingHexagon);
		Assert.assertTrue(game.getCurrentPlayer().intValue() == 1);
		Assert.assertTrue(getGameState(game).getId() == ThiefMove.ID);
		Assert.assertTrue(game.canProcessCommand(moveThief));
		game.processCommand(moveThief);
		int resourceCollectNumber = game.getCatanBoard().getCoordinateToDataHexagon().get(testingHexagon).getCollectResourceNumber();
		Resource collectResource = game.getCatanBoard().getCoordinateToDataHexagon().get(testingHexagon).getResourceType().get();
		Assert.assertTrue(getGameState(game).getId() == RegularPlayPostRoll.ID);
		Assert.assertEquals(game.getPlayerData().get(2).getAmountOfResources(), 0);
	}
	
	

}
