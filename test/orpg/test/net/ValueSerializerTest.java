package orpg.test.net;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import orpg.shared.net.serialize.InputByteBuffer;
import orpg.shared.net.serialize.OutputByteBuffer;
import orpg.shared.net.serialize.SerializeableValue;
import orpg.shared.net.serialize.ValueSerializer;

public class ValueSerializerTest {

	@Test
	public void testPutAndGetForSerializableValueInvokeSerializerMethods() {
		OutputByteBuffer out = new OutputByteBuffer();
		out.putValue(new ValueContainer(5));
		InputByteBuffer in = new InputByteBuffer(out.getBytes());
		assertEquals(10,
				in.getValue(ValueContainerSerializer.getInstance())
						.getValue());
	}

	@Test
	public void testPutAndGetForNonSerializableValueInvokeSerializerMethods() {
		OutputByteBuffer out = new OutputByteBuffer();
		out.putValue("Hello, world!", TestStringSerializer.getInstance());
		InputByteBuffer in = new InputByteBuffer(out.getBytes());
		assertEquals("Hello, world!",
				in.getValue(TestStringSerializer.getInstance()));
	}

	private static class ValueContainer implements
			SerializeableValue<ValueContainer> {

		private int value;

		public ValueContainer(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		@Override
		public ValueSerializer<ValueContainer> getSerializer() {
			return ValueContainerSerializer.getInstance();
		}

	}

	private static class ValueContainerSerializer implements
			ValueSerializer<ValueContainer> {

		private static ValueContainerSerializer instance = new ValueContainerSerializer();

		private ValueContainerSerializer() {
		}

		public static ValueContainerSerializer getInstance() {
			return instance;
		}

		@Override
		public void put(OutputByteBuffer out, ValueContainer obj) {
			out.putInt(obj.getValue() * 2);
		}

		@Override
		public ValueContainer get(InputByteBuffer in) {
			return new ValueContainer(in.getInt());
		}

	}

	private static class TestStringSerializer implements
			ValueSerializer<String> {

		private static TestStringSerializer instance = new TestStringSerializer();

		private TestStringSerializer() {
		}

		public static TestStringSerializer getInstance() {
			return instance;
		}

		@Override
		public void put(OutputByteBuffer out, String obj) {
			out.putString(obj);
		}

		@Override
		public String get(InputByteBuffer in) {
			return in.getString();
		}

	}
}
