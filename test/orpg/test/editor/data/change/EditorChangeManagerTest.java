package orpg.test.editor.data.change;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import orpg.editor.data.change.EditorChange;
import orpg.editor.data.change.EditorChangeManager;

public class EditorChangeManagerTest {

	private int testingValue;
	private EditorChangeManager manager;

	@Before
	public void setUp() throws Exception {
		this.manager = new EditorChangeManager();
		testingValue = 0;
		System.out.println(":D");
	}

	@Test
	public void testNewManagerCannotRedo() {
		assertFalse(manager.canRedo());
	}

	@Test
	public void testNewManagerCannotUndo() {
		assertFalse(manager.canUndo());
	}

	@Test
	public void testAddChangeAppliesTheChange() {
		assertEquals(0, testingValue);
		manager.addChange(new TestChange(1));
		assertEquals(1, testingValue);
		manager.addChange(new TestChange(2));
		assertEquals(2, testingValue);
		manager.addChange(new TestChange(3));
		assertEquals(3, testingValue);
	}

	@Test
	public void testCanUndoWhenChangesArePresent() {
		assertEquals(0, testingValue);
		manager.addChange(new TestChange(1));
		manager.addChange(new TestChange(2));
		manager.addChange(new TestChange(3));
		assertTrue(manager.canUndo());
	}

	@Test
	public void testUndoUndoesTheChange() {
		assertEquals(0, testingValue);
		manager.addChange(new TestChange(1));
		manager.addChange(new TestChange(2));
		manager.addChange(new TestChange(3));
		assertEquals(3, testingValue);
		manager.undo();
		assertEquals(2, testingValue);
		manager.undo();
		assertEquals(1, testingValue);
		manager.undo();
		assertEquals(0, testingValue);
	}

	@Test
	public void testCantUndoPastFirstChange() {
		assertEquals(0, testingValue);
		manager.addChange(new TestChange(1));
		assertTrue(manager.canUndo());
		manager.undo();
		assertFalse(manager.canUndo());
	}

	@Test
	public void testManagerCannotRedoWithoutUndo() {
		manager.addChange(new TestChange(1));
		manager.addChange(new TestChange(2));
		manager.addChange(new TestChange(3));
		assertFalse(manager.canRedo());
		manager.undo();
		assertTrue(manager.canRedo());
	}

	@Test
	public void testRedoingAnUndoneChangeWorks() {
		manager.addChange(new TestChange(1));
		manager.addChange(new TestChange(2));
		manager.addChange(new TestChange(3));
		assertEquals(3, testingValue);
		manager.undo();
		assertEquals(2, testingValue);
		manager.redo();
		assertEquals(3, testingValue);
	}

	@Test
	public void testAddingAChangeWhenNotAtEndOfChangesCrushesFurtherChanges() {
		manager.addChange(new TestChange(1));
		manager.addChange(new TestChange(2));
		manager.addChange(new TestChange(3));
		manager.undo();
		manager.undo();
		manager.addChange(new TestChange(4));
		assertEquals(4, testingValue);
		assertFalse(manager.canRedo());
		manager.undo();
		assertEquals(1, testingValue);
		manager.redo();
		assertEquals(4, testingValue);
		assertFalse(manager.canRedo());
	}

	@Test
	public void testResetErasesHistoryButNotChange() {
		manager.addChange(new TestChange(1));
		manager.addChange(new TestChange(2));
		manager.addChange(new TestChange(3));
		manager.reset();
		assertEquals(3, testingValue);
		assertFalse(manager.canRedo());
		assertFalse(manager.canUndo());
	}

	@Test
	public void testRedoingWhenNotPossibleThrowsIllegalStateException() {
		try {
			manager.redo();
			fail();
		} catch (IllegalStateException e) {
			assertNotNull(e);
		}

		manager.addChange(new TestChange(1));

		try {
			manager.redo();
			fail();
		} catch (IllegalStateException e) {
			assertNotNull(e);
		}

		manager.reset();
		try {
			manager.redo();
			fail();
		} catch (IllegalStateException e) {
			assertNotNull(e);
		}
	}

	@Test
	public void testUndoingWhenNotPossibleThrowsIllegalStateException() {
		try {
			manager.undo();
			fail();
		} catch (IllegalStateException e) {
			assertNotNull(e);
		}

		manager.reset();

		try {
			manager.undo();
			fail();
		} catch (IllegalStateException e) {
			assertNotNull(e);
		}
	}

	@Test
	public void testAddingAnUndoableChangeMakesTheManagerNotBeAbleToUndo() {
		manager.addChange(new TestChange(1));
		manager.addChange(new TestChange(2));
		assertTrue(manager.canUndo());

		manager.addChange(new UndoableTestChange(3));
		assertEquals(3, testingValue);
		assertFalse(manager.canUndo());
	}

	public class TestChange implements EditorChange {

		private int oldValue;
		private int newValue;

		public TestChange(int value) {
			this.newValue = value;
			this.oldValue = testingValue;
		}

		@Override
		public void undo() {
			testingValue = this.oldValue;
		}

		@Override
		public void apply() {
			testingValue = this.newValue;
		}

		@Override
		public boolean canUndo() {
			return true;
		}
	};

	public class UndoableTestChange extends TestChange {
		public UndoableTestChange(int value) {
			super(value);
		}

		@Override
		public boolean canUndo() {
			return false;
		}
	}

}
