package net.strangled.aldaris.catan.game;

import java.util.List;

import net.strangled.aldaris.catan.DevelopmentCard;
import net.strangled.aldaris.catan.game.CatanGame.GameState;
import net.strangled.aldaris.catan.game.command.EndTurn;
import net.strangled.aldaris.catan.game.command.PlayDevelopmentCard;

public class RegularPlayPostRoll extends GameState {
	public static final int ID = 3;
	private static final RegularPlayPostRoll rppr = new RegularPlayPostRoll();
	public static RegularPlayPostRoll getState() {return rppr;}
	
	@Override
	public int getId() {
		return ID;
	}
	@Override
	public boolean canApply(Command command, CatanGame catanGame) {
		return command.canApply(catanGame, this);
	}
	@Override
	public void apply(Command command, CatanGame catanGame) {
		command.apply(catanGame, this);
	}
	@Override
	public GameState getNextState(CatanGame cg) {
		List<Command> commandHistory = cg.getCommandsDoneThisTurn();
		if (commandHistory.isEmpty()) {return this;}
		//TODO : innacurate (more needed) end condition, add dev card transitions, fix 
		if (commandHistory.get(0).getId() == EndTurn.ID) {
			//end turn go back to start
			cg.setCurrentPlayerIndex(cg.getCurrentPlayerIndex() + 1 % cg.getPlayerOrder().size());
			return RegularPlayPreRoll.getState();
		} 
		else if (commandHistory.get(0).getId() == PlayDevelopmentCard.ID) {
			DevelopmentCard devCard = ((PlayDevelopmentCard)commandHistory.get(0)).getDevelopmentCardPlayed();
			switch (devCard) {
			case KNIGHT:
				return ThiefMove.getState();
			case VICTORY_POINT:
				return this;
			case ROAD_BUILDING:
				return RoadBuilding.getState();
			case MONOPOLY:
				return Monopoly.getState();
			case YEAR_OF_PLENTY:
				return YearOfPlenty.getState();
			default:
				throw new NullPointerException();
			}
				
		}
		else {
			return this;
		}
	}

}
