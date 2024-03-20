package net.strangled.aldaris.catan.game;

import net.strangled.aldaris.catan.game.CatanGame.GameState;

public class RoadBuilding extends GameState {
	public static final int ID = 777;
	private static final RoadBuilding rb = new RoadBuilding();
	public static RoadBuilding getState() {return rb;}
	private RoadBuilding() {
	}

	@Override
	public int getId() {
		return ID;
	}
	
	private boolean haveTwoRoadsBeenBuilt(CatanGame cg) {
		var history = cg.getCommandHistory();
		return history.size() > 2 && history.get(0).getId() == RoadBuilding.ID && history.get(1).getId() == RoadBuilding.ID;
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
		if (haveTwoRoadsBeenBuilt(cg)) {
			return super.getRegularGameState(cg);
		}
		else {
			return this;
		}
	}

}
