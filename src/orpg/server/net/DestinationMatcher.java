package orpg.server.net;

import java.util.Collection;
import java.util.List;

import orpg.server.BaseServer;
import orpg.server.ServerSession;

public interface DestinationMatcher {

	public Collection<ServerSession> getReceivingSessions(BaseServer baseServer);

}
