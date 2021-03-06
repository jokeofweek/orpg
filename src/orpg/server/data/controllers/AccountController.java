package orpg.server.data.controllers;

import java.util.HashMap;
import java.util.logging.Level;

import orpg.server.BaseServer;
import orpg.server.data.Account;
import orpg.server.data.store.DataStore;
import orpg.shared.data.store.DataStoreException;
import orpg.shared.data.AccountCharacter;
import orpg.shared.data.Direction;

public class AccountController implements Controller<Account, String> {

	private BaseServer baseServer;
	private DataStore dataStore;
	private HashMap<String, Account> accounts;

	public AccountController(BaseServer baseServer, DataStore dataStore) {
		this.baseServer = baseServer;
		this.dataStore = dataStore;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see orpg.server.data.controllers.Controller#setup()
	 */
	@Override
	public boolean setup() {
		this.accounts = new HashMap<String, Account>();
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see orpg.server.data.controllers.Controller#get(java.lang.Object)
	 */
	@Override
	public synchronized Account get(String name)
			throws IllegalArgumentException {
		if (!accounts.containsKey(name)) {
			// Load the account
			try {
				Account account = dataStore.loadAccount(name);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see orpg.server.data.controllers.Controller#update(java.lang.Object)
	 */
	@Override
	public synchronized void update(Account obj) {
		accounts.put(obj.getName(), obj);
	}

	/**
	 * This saves an account to the data store.
	 * 
	 * @param name
	 *            the name of the account to save
	 */
	public synchronized void save(String name) {
		if (accounts.containsKey(name)) {
			try {
				dataStore.saveAccount(accounts.get(name));
			} catch (DataStoreException e) {
				baseServer
						.getConfigManager()
						.getErrorLogger()
						.log(Level.SEVERE,
								"Could not save account " + name
										+ ". Reason : " + e.getMessage());
			}
		}
	}

	/**
	 * This notifies the controller that an account is no longer needed. Note
	 * that the account should have been saved via
	 * {@link AccountController#save(String)} beforehand.
	 * 
	 * @param name
	 *            the name of the account to release
	 */
	public synchronized void release(String name) {
		accounts.remove(name);
	}

	public boolean accountExists(String name) {
		return dataStore.accountExists(name);
	}

	/**
	 * This attempts to create an account.
	 * 
	 * @param account
	 *            the account to create
	 * @return true if the creation was successful, else false.
	 */
	public boolean createAccount(Account account) {
		try {
			dataStore.createAccount(account);
		} catch (IllegalArgumentException e) {
			return false;
		} catch (DataStoreException e) {
			baseServer
					.getConfigManager()
					.getErrorLogger()
					.log(Level.SEVERE,
							"Could not create account "
									+ account.getName()
									+ ". Error when creating: "
									+ e.getMessage());
			return false;
		}
		update(account);

		baseServer
				.getConfigManager()
				.getSessionLogger()
				.log(Level.INFO,
						"Account " + account.getName() + "("
								+ account.getEmail() + ") was created.");
		return true;
	}

}
