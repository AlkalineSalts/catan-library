package net.strangled.aldaris.test;
import net.strangled.aldaris.catan.*;
import net.strangled.aldaris.catan.board.StandardCatanBoard;
public class MainTest {

	public static void main(String[] args) {
		var board = new StandardCatanBoard();
		System.out.println(board.toJson().build().toString());
		

	}

}
