/*
 * FreeRails
 * Copyright (C) 2000-2018 The FreeRails Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package freerails.network;

import java.io.IOException;
import java.io.Serializable;

/**
 * Defines the methods a client can use to send messages to the server.
 */
public interface ConnectionToServer {
    /**
     * Returns true if this connection is open.
     *
     * @return
     */
    boolean isOpen();

    /**
     * Returns an array containing all the objects read from the server since
     * the last time this method or waitForObjectFromServer() was called, if no
     * objects have been received, it returns an empty array rather than
     * blocking.
     *
     * @return
     * @throws java.io.IOException
     */
    Serializable[] readFromServer() throws IOException;

    /**
     * Returns the next object read from the server, blocking if non is
     * available.
     *
     * @return
     * @throws java.io.IOException
     * @throws java.lang.InterruptedException
     */
    Serializable waitForObjectFromServer() throws IOException,
            InterruptedException;

    /**
     * Sends the specified object to the server.
     *
     * @param object
     * @throws java.io.IOException
     */
    void writeToServer(Serializable object) throws IOException;

    /**
     * Disconnect from the server. When this method returns, calling isOpen() on
     * this object returns false <b>and</b> calling isOpen() on the
     * corresponding ConnectionToClient held by the server also returns false.
     *
     * @throws IOException
     */
    void disconnect() throws IOException;

    /**
     * Flush the underlying stream.
     *
     * @throws java.io.IOException
     */
    void flush() throws IOException;

    /**
     * @return
     */
    String getServerDetails();
}