package it.dangelo.saj.impl.parser;

public enum JsonToken {
	WS(1),
	START_ARRAY(2),
	START_OBJECT(3),
	END_ARRAY(4),
	END_OBJECT(5),
	PAIR_SEPARATOR(6),
	MEMBER_SEPARATOR(7),
	EOF(-1),
	STRING(8),
	BOOLEAN_FALSE(9),
	BOOLEAN_TRUE(10),
	NULL_VALUE(11),
	NUMBER(12),
	COMMA(13);
	
	private int value;
	private JsonToken(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
}
