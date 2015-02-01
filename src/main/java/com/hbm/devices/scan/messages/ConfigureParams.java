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
 * This class holt all information to configure a device via the HBM
 * Network Discovery and Configuration Protocol for Embedded Devices.
 */
public final class ConfigureParams {

    private ConfigureDevice device;
    private ConfigureNetSettings netSettings;
    private int ttl;

    /**
     * Construct an object holding all information to configure a
     * device.
     * 
     * @param device the device to be configured.
     * @param netSettings the network settings which shal be configured.
     * @param ttl Time-To-Live. Limits how long multicast traffic will
     * expand across routers.
     *
     * @throws IllegalArgumentException if {@code ttl} is smaller then 1 or
     * {@code device} is {@code null} or {@code netSettings} is {@code
     * null}.
     */
    public ConfigureParams(ConfigureDevice device, ConfigureNetSettings netSettings, int ttl) {
        if (device == null) {
            throw new IllegalArgumentException("device must not be null");
        }
        if (netSettings == null) {
            throw new IllegalArgumentException("netSettings must not be null");
        }
        if (ttl < 1) {
            throw new IllegalArgumentException("ttl must be greater than zero");
        }
        this.device = device;
        this.netSettings = netSettings;
        this.ttl = ttl;
    }

    /**
     * Construct an object holding all information to configure a
     * device with a ttl of 1.
     * 
     * @param device the device to be configured.
     * @param netSettings the network settings which shal be configured.
     *
     */
    public ConfigureParams(ConfigureDevice device, ConfigureNetSettings netSettings) {
        this(device, netSettings, 1);
    }

    public ConfigureDevice getDevice() {
        return device;
    }

    public ConfigureNetSettings getNetSettings() {
        return netSettings;
    }

    public int getTtl() {
        return ttl;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        if (device != null) {
            builder.append(device);
        }
        if (netSettings != null) {
            builder.append(netSettings);
        }
        builder.append("ttl: ").append(ttl).append("\n\n");

        return builder.toString();
    }
}
