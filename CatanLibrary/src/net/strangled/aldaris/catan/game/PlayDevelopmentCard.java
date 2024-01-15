package net.strangled.aldaris.catan.game;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import net.strangled.aldaris.catan.DevelopmentCard;

public class PlayDevelopmentCard extends Command {
	public static final int ID = 3;
	private final DevelopmentCard developmentCard;
	public PlayDevelopmentCard(int playerId, DevelopmentCard d) {
		super(playerId);
		if (d == null) {throw new IllegalArgumentException("development card cannot be null");}
		this.developmentCard = d;
	}

	public PlayDevelopmentCard(JsonObject jObj) {
		super(jObj);
		developmentCard = DevelopmentCard.idToDevelopmentCard(jObj.getInt("developmentCard"));
	}

	public DevelopmentCard getDevelopmentCardPlayed() {
		return developmentCard;
	}
	
	@Override
	public int getId() {
		return ID;
	}
	
	@Override
	public boolean canApply(CatanGame cg) {
		if (!super.canApply(cg)) {return false;}
		//checks to make sure no other development cards have been played this turn
		if (cg.getCommandsDoneThisTurn().stream().anyMatch(command -> command.getId() == PlayDevelopmentCard.ID))
		{return false;}
		
		//checks to make sure card wasn't built this turn
		int builtThisTurn = 0;
		for (Command command : cg.getCommandsDoneThisTurn()) {
			if (command.getId() == BuildDevelopmentCard.ID) {
				BuildDevelopmentCard card = (BuildDevelopmentCard)command;
				if (card.getBuiltDevelopmentCard() == developmentCard) {builtThisTurn++;}
			}
		}
		return builtThisTurn < cg.getPlayerData().get(this.getPlayerTakingAction()).getDevelopmentCards().get(developmentCard);
		
	}
	
	@Override
	public void apply(CatanGame cg) {
		cg.getPlayerData().get(this.getPlayerTakingAction()).playDevelopmentCard(developmentCard);
		//rest of the effect changes state, so is handled in regular play changeState()
	}

	@Override
	public void _toJson(JsonObjectBuilder builder) {
		builder.add("developmentCard", developmentCard.getId());

	}

}
