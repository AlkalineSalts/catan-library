package net.strangled.aldaris.catan;
import java.util.HashMap;
public enum DevelopmentCard {		
		KNIGHT(0), VICTORY_POINT(1), ROAD_BUILDING(2), MONOPOLY(3), YEAR_OF_PLENTY(4);
		private int id;
		private static HashMap<Integer, DevelopmentCard> idToDevelopmentCard = new HashMap<>();
		static {
			for (DevelopmentCard r : DevelopmentCard.values()) {
				idToDevelopmentCard.put(r.getId(), r);
			}
		}
		private DevelopmentCard(int id) {
			this.id = id;
		}
		public int getId() {
			return id;
		}
		public static DevelopmentCard idToDevelopmentCard(int potentialId) {
			DevelopmentCard r = idToDevelopmentCard.get(potentialId);
			if (r == null) {
				throw new IllegalArgumentException(potentialId + " is not a valid resource.");
			}
			return r;
		}
	

}
