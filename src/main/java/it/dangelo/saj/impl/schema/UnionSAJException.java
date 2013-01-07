package it.dangelo.saj.impl.schema;

import it.dangelo.saj.SAJException;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.List;

@SuppressWarnings("serial")
public class UnionSAJException extends SAJException {

	private List<Exception> exceptions ;
	
	public UnionSAJException(List<Exception> exceptions) {
		super("");
		this.exceptions = exceptions;
	}

	public void setExceptions(List<Exception> exceptions) {
		this.exceptions = exceptions;
	}
	
	@Override
	public String getMessage() {
		StringBuffer buffer = new StringBuffer(super.getMessage()).append('\n');
		ByteArrayOutputStream ostream = new ByteArrayOutputStream();
		PrintStream stream = new PrintStream(ostream);
		for (Exception exception : this.exceptions) {
			ostream.reset();
			exception.printStackTrace(stream);
			buffer.append(ostream.toString());
		}
		return buffer.toString();
	}

}
