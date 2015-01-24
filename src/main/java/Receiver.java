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

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collection;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.hbm.devices.scan.AnnounceReceiver;
import com.hbm.devices.scan.DeviceMonitor;
import com.hbm.devices.scan.FakeMessageReceiver;
import com.hbm.devices.scan.MessageReceiver;
import com.hbm.devices.scan.ScanConstants;
import com.hbm.devices.scan.events.LostDeviceEvent;
import com.hbm.devices.scan.events.NewDeviceEvent;
import com.hbm.devices.scan.events.UpdateDeviceEvent;
import com.hbm.devices.scan.filter.FamilytypeMatch;
import com.hbm.devices.scan.filter.Filter;
import com.hbm.devices.scan.messages.Announce;
import com.hbm.devices.scan.messages.CommunicationPath;
import com.hbm.devices.scan.messages.IPv6Entry;
import com.hbm.devices.scan.messages.MessageParser;
import com.hbm.devices.scan.messages.MissingDataException;
import com.hbm.devices.scan.messages.ServiceEntry;
import com.hbm.devices.scan.util.ConnectionFinder;
import com.hbm.devices.scan.util.ScanInterfaces;

public class Receiver implements Observer {

    private Collection<NetworkInterface> scanInterfaces;
    private ConnectionFinder connectionFinder;

    private static final Logger LOGGER = Logger.getLogger(ScanConstants.LOGGER_NAME);

    public Receiver() throws SocketException {
        scanInterfaces = new ScanInterfaces().getInterfaces();
        connectionFinder = new ConnectionFinder(scanInterfaces, false);
    }

    public static void main(String[] args) {
        try {
            MessageParser jf = new MessageParser();
            MessageReceiver mr;
            if ((args.length > 0) && (args[0].compareTo("fake") == 0)) {
                FakeMessageReceiver fmr = new FakeMessageReceiver();
                fmr.addObserver(jf);
                mr = fmr;
            } else {
                AnnounceReceiver ar = new AnnounceReceiver();
                ar.addObserver(jf);
                mr = ar;
            }

            String[] families = { "QuantumX" };
            Filter ftFilter = new Filter(new FamilytypeMatch(families));
            jf.addObserver(ftFilter);

            DeviceMonitor af = new DeviceMonitor();
            ftFilter.addObserver(af);

            Receiver r = new Receiver();
            af.addObserver(r);
            mr.start();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error instantiating announce receiver chain!", e);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        try {
            CommunicationPath ap;
            if (arg instanceof NewDeviceEvent) {
                ap = ((NewDeviceEvent) arg).getAnnouncePath();
                Announce a = ap.getAnnounce();
                InetAddress connectAddress = connectionFinder.getConnectableAddress(a);
                LOGGER.info("New Device:\n");
                if (connectAddress != null) {
                    LOGGER.info("Connectable: " + connectAddress + "\n");
                }
            } else if (arg instanceof LostDeviceEvent) {
                ap = ((LostDeviceEvent) arg).getAnnouncePath();
                LOGGER.info("Lost Device:\n");
            } else if (arg instanceof UpdateDeviceEvent) {
                UpdateDeviceEvent event = (UpdateDeviceEvent) arg;
                ap = event.getNewCommunicationPath();
                LOGGER.info("Update Device:\n");
            } else {
                LOGGER.info("unknown\n");
                return;
            }

            Announce a = ap.getAnnounce();
            LOGGER.info(a.getParams().getDevice().toString());

            LOGGER.info("\tIP-Addresses:\n");
            LOGGER.info("\t interfaceName: "
                    + a.getParams().getNetSettings().getInterface().getName() + "\n");
            LOGGER.info("\t method:"
                    + a.getParams().getNetSettings().getInterface().getConfigurationMethod() + "\n");
            Iterable<?> ipv4 = (Iterable<?>) a.getParams().getNetSettings().getInterface().getIPv4();
            Iterable<IPv6Entry> ipv6 = a.getParams().getNetSettings().getInterface().getIPv6();
            if (ipv4 != null) {
                for (Object entry : ipv4) {
                    LOGGER.info("\t " + entry + "\n");
                }
            }
            if (ipv6 != null) {
                for (IPv6Entry e : ipv6) {
                    LOGGER.info("\t " + e + "\n");
                }
            }

            LOGGER.info("\tServices:\n");
            Iterable<ServiceEntry> services = a.getParams().getServices();
            for (ServiceEntry entry : services) {
                LOGGER.info("\t " + entry + "\n");
            }
            LOGGER.info("\n");
        } catch (MissingDataException e) {
            LOGGER.info("Some data missing in Announce: " + e);
        }
    }
}
