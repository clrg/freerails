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

import freerails.controller.ClientControlInterface.ClientProperty;
import freerails.controller.MessageToClient;
import freerails.util.ImStringList;

import java.io.IOException;

/**
 * Tests FreerailsClient with a network server.
 */
public class FreerailsClientTest extends AbstractFreerailsServerTestCase {

    /**
     *
     */
    public void testLogon() {
        try {
            /* Test 1 : connecting a client. */
            assertEquals("No client connected yet.", 0, server
                    .countOpenConnections());

            FreerailsClient client = new FreerailsClient();
            LogOnResponse response = client.connect(getIpAddress(), getPort(),
                    "name", "password");
            assertTrue(response.isSuccessful());
            assertEquals(1, server.countOpenConnections());

            assertMapsAndSaveGamesReceived(client);
            assertConnectClientsEquals(client, new ImStringList("name"));

            /* Test 2 : a client that has already logged on. */
            FreerailsClient client2 = new FreerailsClient();
            response = client2.connect(getIpAddress(), getPort(), "name",
                    "password");
            assertFalse("The player is already logged on.", response
                    .isSuccessful());
            assertEquals(1, server.countOpenConnections());

            /* Test 3 : connecting a client. */
            FreerailsClient client3 = new FreerailsClient();
            response = client3.connect(getIpAddress(), getPort(), "name3",
                    "password");
            assertTrue(response.isSuccessful());
            assertEquals(2, server.countOpenConnections());

            /* read list of connected clients. */
            assertConnectClientsEquals(client,
                    new ImStringList("name", "name3"));
            assertMapsAndSaveGamesReceived(client3);
            assertConnectClientsEquals(client3, new ImStringList("name",
                    "name3"));

            /* Test 4 : disconnect the client from test 1. */
            client.disconnect();
            assertEquals(1, server.countOpenConnections());
        } catch (Exception e) {
            fail();
        }
    }

    private void assertConnectClientsEquals(FreerailsClient client,
                                            ImStringList expectedPlayerNames) throws IOException,
            InterruptedException {
        MessageToClient messageToClient = (MessageToClient) client.read();
        messageToClient.execute(client);

        ImStringList actualPlayerNames = (ImStringList) client
                .getProperty(ClientProperty.CONNECTED_CLIENTS);
        assertNotNull(actualPlayerNames);
        assertEquals(expectedPlayerNames, actualPlayerNames);
    }

    private void assertMapsAndSaveGamesReceived(FreerailsClient client)
            throws IOException, InterruptedException {
        // 2 commands to read.
        MessageToClient messageToClient = (MessageToClient) client.read();
        messageToClient.execute(client);
        messageToClient = (MessageToClient) client.read();
        messageToClient.execute(client);

        Object maps = client.getProperty(ClientProperty.MAPS_AVAILABLE);
        assertNotNull(maps);

        Object savedGames = client.getProperty(ClientProperty.SAVED_GAMES);
        assertNotNull(savedGames);
    }
}