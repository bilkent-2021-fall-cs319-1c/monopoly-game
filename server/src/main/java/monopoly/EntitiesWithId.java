package monopoly;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Entities that are identifiable
 * 
 * @author Ziya Mukhtarov
 * @version Dec 12, 2020
 */
public class EntitiesWithId<T extends Identifiable> {
	private Map<Integer, T> entities;

	public EntitiesWithId() {
		entities = Collections.synchronizedMap(new HashMap<Integer, T>());
	}

	/**
	 * Associates the specified entity with a key value in the map. Entity should
	 * not be null
	 * 
	 * @param entity the object to be associated
	 */
	public void add(T entity) {
		entities.put(entity.getId(), entity);
	}

	/**
	 * Removes the specified entity from the map if it exists, does nothing if not.
	 * Entity should not be null
	 * 
	 * @param entity the object to be removed
	 */
	public void remove(T entity) {
		entities.remove(entity.getId());
	}

	/**
	 * Gives the object with a specified id
	 * 
	 * @param id the id to be searched with
	 * 
	 * @return T object if found, null if not
	 */
	public T getByID(int id) {
		return entities.get(id);
	}
	
	/**
	 * Gives the size of the map
	 * 
	 * @return the number of key-value mappings
	 */
	public int size() {
		return entities.size();
	}
}
