package orpg.server.net.handlers;

import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;

import orpg.server.BaseServer;
import orpg.server.data.Account;
import orpg.server.data.ServerReceivedPacket;
import orpg.server.data.SessionType;
import orpg.server.data.store.DataStoreException;
import orpg.server.net.packets.EditorLoginOkPacket;
import orpg.server.net.packets.ErrorPacket;
import orpg.server.net.packets.LoginOkPacket;
import orpg.shared.ErrorMessage;
import orpg.shared.data.Validator;

public class LoginHandler implements ServerPacketHandler {

	@Override
	public void handle(ServerReceivedPacket packet, BaseServer baseServer) {
		// Standard login process
		boolean isEditorHandler = packet.getByteBuffer().getBoolean();
		String name = packet.getByteBuffer().getString();
		char[] password = packet.getByteBuffer().getCharArray();

		try {
			// First check that username is valid
			if (!Validator.isAccountNameValid(name)) {
				baseServer.sendPacket(new ErrorPacket(packet.getSession(),
						ErrorMessage.LOGIN_INVALID_CREDENTIALS));
				return;
			}

			// If username was valid, check if account exists, and if so load
			// it.
			Account account = null;
			account = baseServer.getAccountController().get(name);

			// Make sure the account was loaded and that there was no error
			if (account == null) {
				baseServer.sendPacket(new ErrorPacket(packet.getSession(),
						ErrorMessage.GENERIC_LOGIN_ERROR));
				return;
			}

			// If we've finally loaded the account, test credentials.
			if (!account.passwordMatches(password)) {
				baseServer.sendPacket(new ErrorPacket(packet.getSession(),
						ErrorMessage.LOGIN_INVALID_CREDENTIALS));
				return;
			}

			if (isEditorHandler) {
				// At this point, we would test for access rights.
			}

			// At this point, our login was successful, so update the session.
			packet.getSession().login(
					account,
					isEditorHandler ? SessionType.EDITOR
							: SessionType.LOGGED_IN);
		} catch (IllegalArgumentException e) {
			// Thrown when the account does not exist
			baseServer.sendPacket(new ErrorPacket(packet.getSession(),
					ErrorMessage.LOGIN_INVALID_CREDENTIALS));

		} catch (NoSuchAlgorithmException e) {
			// Could be thrown when testing password.
			baseServer
					.getConfigManager()
					.getErrorLogger()
					.log(Level.SEVERE,
							"Could not test password match. Reason: "
									+ e.getMessage());
			baseServer.sendPacket(new ErrorPacket(packet.getSession(),
					ErrorMessage.GENERIC_LOGIN_ERROR));
		} finally {
			// Clear out the password for safety
			for (int i = 0; i < password.length; i++) {
				password[i] = '\0';
			}
		}
	}
}
