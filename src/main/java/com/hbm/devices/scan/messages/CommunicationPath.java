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

package com.hbm.devices.scan.messages;

/**
 * This class carries parsed announce messages.
 * <p>
 * If it would be possible to retrieve the information which {@link
 * java.net.NetworkInterface} received an announce messages, this will
 * also handled here to provide all information necessary to decide if
 * an IP communication is possible to the announced device.
 * <p>
 * Please note that the {@link #hashCode()} method is overridden. The
 * hash code of this announce object is unique for the communication
 * path the {@link Announce} message travelled.
 *
 * @since 1.0
 */
public final class CommunicationPath {

    private final Announce announce;
    private final int hash;
    private final String communicationPath;
    private Object cookie;
    private static final int INITIAL_HASHCODE_BUFFER_SIZE = 100;

    CommunicationPath(Announce announce) throws MissingDataException {
        this.announce = announce;

        final AnnounceParams params = announce.getParams();
        final String deviceUuid = params.getDevice().getUuid();
        final StringBuilder hashBuilder = new StringBuilder(INITIAL_HASHCODE_BUFFER_SIZE);
        hashBuilder.append(deviceUuid);

        final Router router = params.getRouter();
        if (router != null) {
            hashBuilder.append(router.getUuid());
        }

        final String deviceInterfaceName = params.getNetSettings().getInterface().getName();
        hashBuilder.append(deviceInterfaceName);
        communicationPath = hashBuilder.toString();
        hash = communicationPath.hashCode();
    }

    public void setCookie(Object cookie) {
        this.cookie = cookie;
    }

    public Object getCookie() {
        return cookie;
    }

    /**
     * Calculates a unique hash for a communication path.
     * <p>
     * Currently the device uuid, the router uuid and the interface name
     * of the sending device are take into the hash calculation.
     */
    @Override
    public int hashCode() {
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CommunicationPath)) {
            return false;
        }
        return communicationPath.equals(((CommunicationPath)obj).communicationPath);
    }
    
    public Announce getAnnounce() {
        return announce;
    }
    
}
