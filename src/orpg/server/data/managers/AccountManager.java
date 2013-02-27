package orpg.server.data.managers;

import java.util.HashMap;
import java.util.logging.Level;

import javax.naming.OperationNotSupportedException;

import orpg.server.BaseServer;
import orpg.server.data.Account;
import orpg.server.data.store.DataStoreException;

public class AccountManager implements Manager<Account, String> {

	private BaseServer baseServer;
	private HashMap<String, Account> accounts;

	public AccountManager(BaseServer baseServer) {
		this.baseServer = baseServer;
	}

	@Override
	public boolean setup() {
		this.accounts = new HashMap<String, Account>();
		return true;
	}

	@Override
	public synchronized Account get(String name)
			throws IllegalArgumentException {
		if (!accounts.containsKey(name)) {
			// Load the account
			try {
				Account account = baseServer.getDataStore().loadAccount(name);
				accounts.put(name, account);
			} catch (IllegalArgumentException e) {
				throw e;
			} catch (DataStoreException e) {
				baseServer
						.getConfigManager()
						.getErrorLogger()
						.log(Level.SEVERE,
								"Could not load account for " + name
										+ ". Reason: " + e.getMessage());
				return null;

			}
		}

		return accounts.get(name);
	}

	@Override
	public synchronized void update(Account obj) {
		accounts.put(obj.getName(), obj);
	}

}
