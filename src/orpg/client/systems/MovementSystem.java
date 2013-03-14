package orpg.client.systems;

import orpg.client.BaseClient;
import orpg.client.ClientConstants;
import orpg.client.data.component.Animated;
import orpg.client.data.component.AnimatedPlayer;
import orpg.client.data.component.HandlesInput;
import orpg.client.ui.ViewBox;
import orpg.shared.data.Direction;
import orpg.shared.data.Map;
import orpg.shared.data.component.Collidable;
import orpg.shared.data.component.IsPlayer;
import orpg.shared.data.component.Moveable;
import orpg.shared.data.component.Position;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.managers.GroupManager;
import com.artemis.managers.PlayerManager;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.ImmutableBag;

/**
 * @author Dom
 * 
 */
public class MovementSystem extends EntityProcessingSystem {

	@Mapper
	ComponentMapper<Moveable> movementMapper;
	@Mapper
	ComponentMapper<Position> positionMapper;
	@Mapper
	ComponentMapper<AnimatedPlayer> animatedMapper;
	@Mapper
	ComponentMapper<IsPlayer> isPlayerMapper;
	@Mapper
	ComponentMapper<Collidable> collideableMapper;

	private BaseClient baseClient;
	private GroupManager groupManager;

	public MovementSystem(BaseClient baseClient) {
		super(Aspect.getAspectForAll(Position.class, Moveable.class));
		this.baseClient = baseClient;
		this.groupManager = this.baseClient.getWorld().getManager(
				GroupManager.class);
	}

	@Override
	protected void process(Entity e) {
		Position position = positionMapper.get(e);
		Moveable moveable = movementMapper.get(e);

		if (moveable.isMoving() && !moveable.isMoveProcessed()) {
			moveable.setMoveProcessed(true);

			// Apply the move
			int oldX = position.getX();
			int oldY = position.getY();
			switch (moveable.getDirection()) {
			case UP:
				position.setY(position.getY() - 1);
				break;
			case DOWN:
				position.setY(position.getY() + 1);
				break;
			case LEFT:
				position.setX(position.getX() - 1);
				break;
			case RIGHT:
				position.setX(position.getX() + 1);
				break;
			}

			updateEntitySegment(e, oldX, oldY);

			// Animate if necessary
			if (animatedMapper.getSafe(e) != null) {
				world.getSystem(AnimationSystem.class).animate(e,
						moveable.getDirection());
			}

		}
	}

	public boolean canMove(Entity entity, int x, int y) {
		return baseClient.getMap().isWalkable(x, y);
	}

	public void updateEntitySegment(Entity entity, int oldX, int oldY) {
		// Add groups based on components
		Position position = positionMapper.getSafe(entity);

		if (position != null) {
			updateEntitySegment(entity, oldX, oldY, position.getX(),
					position.getY());
		}
	}

	public void updateEntitySegment(Entity entity, int oldX, int oldY,
			int newX, int newY) {
		// Add groups based on components

		boolean requiresMapJoin = false;
		boolean requiresSegmentJoin = false;

		Map map = baseClient.getMap();

		if (groupManager.getGroups(entity) != null) {
			if (!groupManager.inInGroup(entity, ClientConstants.GROUP_MAP)) {
				requiresMapJoin = true;
				requiresSegmentJoin = true;
			} else if (map.getSegmentX(oldX) != map.getSegmentX(newX)
					|| map.getSegmentY(oldY) != map.getSegmentY(newY)) {
				requiresSegmentJoin = true;
				groupManager.remove(
						entity,
						String.format(ClientConstants.GROUP_MAP_SEGMENT,
								map.getSegmentX(oldX), map.getSegmentY(oldY)));
			}
		}

		// Register the map / segment groups
		if (requiresMapJoin) {
			groupManager.add(entity, ClientConstants.GROUP_MAP);
		}

		if (requiresSegmentJoin) {
			groupManager.add(
					entity,
					String.format(ClientConstants.GROUP_MAP_SEGMENT,
							map.getSegmentX(newX), map.getSegmentY(newY)));
		}
	}

	/**
	 * This checks whether there are any entities with the {@link Collidable}
	 * component which has the {@link Collidable#isPassable()} set to false at a
	 * given position.
	 * 
	 * @param x
	 * @param y
	 * @return true if there is an entity which is not passable at a given slot.
	 */
	public boolean hasBlockingEntities(int x, int y) {
		ImmutableBag<Entity> entities = groupManager
				.getEntities(ClientConstants.GROUP_COLLIDEABLE);
		Entity entity;
		Position position;

		for (int i = 0; i < entities.size(); i++) {
			entity = entities.get(i);
			position = positionMapper.get(entity);
			if (position.getX() == x && position.getY() == y) {
				if (!collideableMapper.get(entity).isPassable()) {
					return true;
				}
			}
		}

		return false;
	}
}
