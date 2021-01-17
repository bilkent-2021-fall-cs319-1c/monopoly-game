package monopoly.gameplay.properties;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import monopoly.gameplay.GamePlayer;

/**
 * Represents "color" set for street properties, railroads or utilities.
 * Basically, groups some properties together and tracks who owns them
 * 
 * @author Ziya Mukhtarov
 * @version Jan 17, 2021
 */
@Getter
public class ColorSet {
	private final String name;
	private Set<Property> properties;

	public ColorSet(String name) {
		this.name = name;
		properties = new HashSet<>();
	}

	public void registerProperty(Property property) {
		properties.add(property);
	}

	public boolean isOwnedBy(GamePlayer player) {
		return properties.parallelStream().map(Property::getOwner).allMatch(player::equals);
	}

	public int countPropertiesOwnedBy(GamePlayer player) {
		return (int) properties.parallelStream().filter(property -> player.equals(property.getOwner())).count();
	}
}
