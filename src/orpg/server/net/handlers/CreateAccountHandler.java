package orpg.server.net.handlers;

import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;

import orpg.server.BaseServer;
import orpg.server.data.Account;
import orpg.server.data.ServerReceivedPacket;
import orpg.server.data.SessionType;
import orpg.server.net.packets.ErrorPacket;
import orpg.shared.ErrorMessage;
import orpg.shared.data.Validator;
import orpg.shared.net.InputByteBuffer;

public class CreateAccountHandler implements ServerPacketHandler {

	@Override
	public void handle(ServerReceivedPacket packet, BaseServer baseServer) {
		InputByteBuffer in = packet.getByteBuffer();
		String name = in.getString();
		String email = in.getString();
		char[] password = in.getCharArray();

		// Validate the info first
		try {
			if (!Validator.isAccountNameValid(name)) {
				baseServer.sendPacket(new ErrorPacket(packet.getSession(),
						ErrorMessage.ACCOUNT_NAME_HAS_INVALID_CHARACTERS));
			} else {
				synchronized (this) {
					if (baseServer.getAccountController().accountExists(name)) {
						baseServer.sendPacket(new ErrorPacket(packet
								.getSession(),
								ErrorMessage.ACCOUNT_ALREADY_EXISTS));
					} else {
						try {
							// Create the account, filling in the details
							Account account = new Account();
							account.setName(name);
							account.setEmail(email);
							account.updatePassword(password);
							if (!baseServer.getAccountController().createAccount(account)) {
								baseServer
										.sendPacket(new ErrorPacket(
												packet.getSession(),
												ErrorMessage.GENERIC_ACCOUNT_CREATION_ERROR));
							}
							
							packet.getSession().login(account,
									SessionType.LOGGED_IN);

						} catch (NoSuchAlgorithmException e) {
							baseServer
									.getConfigManager()
									.getErrorLogger()
									.log(Level.SEVERE,
											"Could not create account "
													+ name
													+ ". Error when hashing password: "
													+ e.getMessage());
							baseServer
									.sendPacket(new ErrorPacket(
											packet.getSession(),
											ErrorMessage.GENERIC_ACCOUNT_CREATION_ERROR));
						}

					}
				}
			}
		} finally {
			// Clear password
			for (int i = 0; i < password.length; i++) {
				password[i] = '\0';
			}
		}
	}
}
