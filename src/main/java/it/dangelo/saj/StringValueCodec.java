package it.dangelo.saj;

/**
 * This class supply utility method to codec json string. It resolves unicode, escape characters,
 * etc...
 *
 */
public class StringValueCodec {
	
	/**
	 * Resolve a unicode string in char
	 * 
	 * @param unicode An unicode sting in format u[xxxx] (ex: u00c0) 
	 * @return Character resolved by unicode string
	 * @throws Exception in an error accurs
	 */
	public static char resolveUnicode(String unicode) throws Exception {
		return (char) Integer.parseInt(unicode.substring(1), 16);
	}

	/**
	 * Extract a json string using json specification. Alert, this method don't 
	 * remove double quote in the begin and end.
	 * 
	 * @param value String to decode
	 * @return String decoded
	 * @throws Exception in an error occurs
	 */
	public static String extractString(String value) throws Exception {
		StringBuffer sb = new StringBuffer(value);
		int startPoint = 1;
		for (;;) {
			int slashIndex = sb.indexOf("\\", startPoint); // search for a
															// single backslash
			if (slashIndex == -1)
				break;
			char escapeType = sb.charAt(slashIndex + 1);
			switch (escapeType) {
			case 'u':
				// Unicode escape.
				String unicodeString = sb.substring(slashIndex + 1,
						slashIndex + 6);
				char unicode = resolveUnicode(unicodeString);
				sb.replace(slashIndex, slashIndex + 6, String.valueOf(unicode)); // backspace
				break;
			case 'b':
				sb.replace(slashIndex, slashIndex + 2, "\b"); // backspace
				break;
			case 't':
				sb.replace(slashIndex, slashIndex + 2, "\t"); // tab
				break;
			case 'n':
				sb.replace(slashIndex, slashIndex + 2, "\n"); // newline
				break;
			case 'f':
				sb.replace(slashIndex, slashIndex + 2, "\f"); // form feed
				break;
			case 'r':
				sb.replace(slashIndex, slashIndex + 2, "\r"); // return
				break;
			// case '\'':
			// sb.replace(slashIndex, slashIndex + 2, "\'"); // single quote
			// break;
			case '\"':
				sb.replace(slashIndex, slashIndex + 2, "\""); // double quote
				break;
			case '\\':
				sb.replace(slashIndex, slashIndex + 2, "\\"); // backslash
				break;
			case '/':
				sb.replace(slashIndex, slashIndex + 2, "/"); // backslash
				break;
			}
			startPoint = slashIndex + 1;
		}
		return sb.toString();
	}
	
	
}
