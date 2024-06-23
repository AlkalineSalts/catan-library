package net.strangled.aldaris.catan.game;
import net.strangled.aldaris.catan.game.CatanGame.GameState;
public class DiscardPhase extends GameState {
	private static DiscardPhase d = new DiscardPhase();
	public static final int ID = -423342;
	
	private DiscardPhase() {
		
	}
	public static DiscardPhase getState() {return d;}
	@Override
	public int getId() {
		return ID;
	}

	@Override
	public GameState getNextState(CatanGame cg) {
		if (cg.getPlayerData().values().stream().filter( p -> p.getAmountOfResources() > 7).findAny().isPresent()) {
			return this;
		} else {
			return GameStateFactory.get().getGameState(ThiefMove.ID);
		}
	}

	@Override
	public boolean canApply(Command command, CatanGame catanGame) {
		return command.canApply(catanGame, this);
	}

	@Override
	public void apply(Command command, CatanGame catanGame) {
		command.apply(catanGame, this);
	}

}
