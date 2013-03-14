package orpg.server.event;

import orpg.server.BaseServer;
import orpg.server.systems.MovementSystem;
import orpg.shared.data.Direction;

import com.artemis.Entity;
import com.artemis.World;

public interface MovementEvent {

	public void process(BaseServer baseServer,
			MovementSystem movementSystem);

}
