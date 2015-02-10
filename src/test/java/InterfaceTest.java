/*
 * Java Scan, a library for scanning and configuring HBM devices.
 *
 * The MIT License (MIT)
 *
 * Copyright (C) Stephan Gatzka
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS
 * BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import java.util.Observable;
import java.util.Observer;

import com.hbm.devices.scan.FakeMessageReceiver;
import com.hbm.devices.scan.messages.Announce;
import com.hbm.devices.scan.messages.CommunicationPath;
import com.hbm.devices.scan.messages.MessageParser;
import com.hbm.devices.scan.messages.MissingDataException;

public class InterfaceTest {

    private CommunicationPath cp;
    private FakeMessageReceiver fsmmr;

    @Before
    public void setUp() {
        fsmmr = new FakeMessageReceiver();
        MessageParser jf = new MessageParser();
        fsmmr.addObserver(jf);
        jf.addObserver(new Observer() {
            public void update(Observable o, Object arg) {
                if (arg instanceof CommunicationPath) {
                    cp = (CommunicationPath) arg;
                }
            }
        });
    }

    @Test
    public void parseDhcpMessage() {
        fsmmr.emitSingleCorrectMessage();
        assertNotNull("No CommunictionPath object after correct message", cp);
        try {
            String configMethod = cp.getAnnounce().getParams().getNetSettings().getInterface().getConfigurationMethod();
            assertEquals(configMethod, "dhcp");
        } catch (MissingDataException e) {
            fail("MissingDataException thrown" + e);
        }
    }

    @Test
    public void parseManualMessage() {
        fsmmr.emitCorrectMessageManual();
        assertNotNull("No CommunictionPath object after correct message", cp);
        try {
            String configMethod = cp.getAnnounce().getParams().getNetSettings().getInterface().getConfigurationMethod();
            assertEquals(configMethod, "manual");
        } catch (MissingDataException e) {
            fail("MissingDataException thrown" + e);
        }
    }

    @Test
    public void parseRouterSolicitMessage() {
        fsmmr.emitCorrectMessageRouterSolicit();
        assertNotNull("No CommunictionPath object after correct message", cp);
        try {
            String configMethod = cp.getAnnounce().getParams().getNetSettings().getInterface().getConfigurationMethod();
            assertEquals(configMethod, "routerSolicitation");
        } catch (MissingDataException e) {
            fail("MissingDataException thrown" + e);
        }
    }

    @Test
    public void parseMissingConfigurationMethod() {
        fsmmr.emitMissingConfigurationMethod();
        assertNotNull("No CommunictionPath object after correct message", cp);
        try {
            String configMethod = cp.getAnnounce().getParams().getNetSettings().getInterface().getConfigurationMethod();
            fail("MissingDataException not thrown");
        } catch (MissingDataException e) {
            assertNotNull("MissingDataException not thrown", e);
        }
    }
}
