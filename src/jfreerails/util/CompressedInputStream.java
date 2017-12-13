package jfreerails.util;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;


/**
* @author Patrice Espi�
* Licensing: LGPL
*/
public class CompressedInputStream extends FilterInputStream {
    public CompressedInputStream(InputStream in) {
        super(in);
        buffer = new byte[0x7d000];
        compBuffer = new byte[(int)((double)buffer.length * 1.2D)];
        readIndex = 0;
        maxReadIndex = 0;
        inflater = new Inflater();
    }

    public boolean markSupported() {
        return false;
    }

    public int available() throws IOException {
        if (maxReadIndex - readIndex == 0 && super.in.available() > 0 &&
                !readNextBuffer()) {
            return -1;
        } else {
            return maxReadIndex - readIndex;
        }
    }

    public int read() throws IOException {
        if (maxReadIndex - readIndex == 0 && !readNextBuffer()) {
            return -1;
        }

        byte b = buffer[readIndex++];

        if (b < 0) {
            return 256 + b;
        } else {
            return b;
        }
    }

    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    public int read(byte[] b, int off, int len) throws IOException {
        if (maxReadIndex - readIndex == 0 && !readNextBuffer()) {
            return -1;
        }

        int read = 0;

        for (int i = 0; i < len && available() > 0;) {
            b[off + i] = (byte)read();
            i++;
            read++;
        }

        return read;
    }

    private boolean readNextBuffer() throws IOException {
        byte compressionFlag = -1;
        compressionFlag = (byte)super.in.read();

        if (compressionFlag == -1) {
            return false;
        }

        maxReadIndex = super.in.read() & 0xff;
        maxReadIndex = maxReadIndex << 8 | super.in.read() & 0xff;
        maxReadIndex = maxReadIndex << 8 | super.in.read() & 0xff;
        maxReadIndex = maxReadIndex << 8 | super.in.read() & 0xff;

        if (buffer.length < maxReadIndex) {
            buffer = new byte[maxReadIndex + 40960];
        }

        if (compressionFlag == 1) {
            int compSize = super.in.read() & 0xff;
            compSize = compSize << 8 | super.in.read() & 0xff;
            compSize = compSize << 8 | super.in.read() & 0xff;
            compSize = compSize << 8 | super.in.read() & 0xff;

            if (compBuffer.length < compSize) {
                compBuffer = new byte[compSize + 40960];
            }

            for (int read = 0; read < compSize;
                    read += super.in.read(compBuffer, read, compSize - read)) {
            }

            inflater.reset();
            inflater.setInput(compBuffer, 0, compSize);

            try {
                inflater.inflate(buffer);
            } catch (DataFormatException ex) {
                throw new IOException("Data format exception");
            }
        } else if (compressionFlag == 0) {
            for (int read = 0; read < maxReadIndex;
                    read += super.in.read(buffer, read, maxReadIndex - read)) {
            }
        }

        readIndex = 0;

        return true;
    }

    private byte[] buffer;
    private byte[] compBuffer;
    private int readIndex;
    private int maxReadIndex;
    private Inflater inflater;
}