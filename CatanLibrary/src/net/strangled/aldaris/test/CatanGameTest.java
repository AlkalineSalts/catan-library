package net.strangled.aldaris.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.strangled.aldaris.catan.board.StandardCatanBoard;
import net.strangled.aldaris.catan.game.CatanGame;
import net.strangled.aldaris.catan.game.Player;

class CatanGameTest {
	
	StandardCatanBoard board;
	CatanGame g;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		board = new StandardCatanBoard();
		g = new CatanGame(board, CatanGame.getDefaultCards(), new Player(1), new Player(2));
	}

	@AfterEach
	void tearDown() throws Exception {
		
	}

	@Test
	void test() {
		
		
	}
	
	
	

}
