package benzenestudios.installer;

import java.io.IOException;
import java.io.Writer;

/**
 * From my Swing Game private repository
 * @author Valoeghese
 */
public class NoneOutputStream extends Writer {
	@Override
	public void write(char[] cbuf, int off, int len) throws IOException {
	}

	@Override
	public void flush() throws IOException {
	}

	@Override
	public void close() throws IOException {
	}
}
