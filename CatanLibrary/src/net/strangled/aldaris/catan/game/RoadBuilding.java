package net.strangled.aldaris.catan.game;

import net.strangled.aldaris.catan.game.CatanGame.GameState;

public class RoadBuilding extends GameState {
	public static final int ID = 777;
	public RoadBuilding() {
		// TODO Auto-generated constructor stub
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
	public boolean canDoCommand(CatanGame cg, Command command) {
		//only roads can be built
		return !haveTwoRoadsBeenBuilt(cg) && command.getId() == RoadBuilding.ID;
	}

	@Override
	public GameState getNextState(CatanGame cg) {
		if (haveTwoRoadsBeenBuilt(cg)) {
			if (RegularPlay.haveDiceRolledSinceLastEndTurn(cg.getCommandHistory())) {
				return GameStateFactory.get().getGameState(RegularPlayPostRoll.ID);
			}
			else {
				return GameStateFactory.get().getGameState(RegularPlayPreRoll.ID);
			}
		}
		else {
			return this;
		}
	}

}
