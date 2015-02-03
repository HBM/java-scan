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

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

import com.hbm.devices.scan.messages.ConfigurationRequest;
import com.hbm.devices.scan.messages.ConfigureDevice;
import com.hbm.devices.scan.messages.ConfigureInterface;
import com.hbm.devices.scan.messages.ConfigureNetSettings;
import com.hbm.devices.scan.messages.ConfigureParams;
import com.hbm.devices.scan.messages.Interface.Method;

public class ConfigMessagesTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();
 
    @Test
    public void createConfigureNullParams() {
        exception.expect(IllegalArgumentException.class);
        ConfigurationRequest conf = new ConfigurationRequest(null, "1234");
    }

    @Test
    public void parseNullUUID() {
        ConfigureDevice device = new ConfigureDevice("0009E5001571");
        ConfigureNetSettings settings = new ConfigureNetSettings(new ConfigureInterface("eth0", Method.DHCP, null));
        ConfigureParams configParams = new ConfigureParams(device, settings);
        exception.expect(IllegalArgumentException.class);
        ConfigurationRequest conf = new ConfigurationRequest(configParams, null);
    }

    @Test
    public void parseNullDevice() {
        ConfigureNetSettings settings = new ConfigureNetSettings(new ConfigureInterface("eth0", Method.DHCP, null));
        exception.expect(IllegalArgumentException.class);
        ConfigureParams configParams = new ConfigureParams(null, settings);
    }

    @Test
    public void parseEmptyDevice() {
        exception.expect(IllegalArgumentException.class);
        ConfigureDevice device = new ConfigureDevice("");
    }

    @Test
    public void parseNullNetSettings() {
        ConfigureDevice device = new ConfigureDevice("0009E5001571");
        exception.expect(IllegalArgumentException.class);
        ConfigureParams configParams = new ConfigureParams(device, null);
    }

    @Test
    public void parseNullInterface() {
        exception.expect(IllegalArgumentException.class);
        ConfigureNetSettings settings = new ConfigureNetSettings(null);
    }

    @Test
    public void parseNullInterfaceName() {
        exception.expect(IllegalArgumentException.class);
        ConfigureInterface iface = new ConfigureInterface(null, Method.DHCP, null);
    }

    @Test
    public void parseNoInterfaceName() {
        exception.expect(IllegalArgumentException.class);
        ConfigureInterface iface = new ConfigureInterface("", Method.DHCP, null);
    }

    @Test
    public void parseManualAndNoIp() {
        exception.expect(IllegalArgumentException.class);
        ConfigureInterface iface = new ConfigureInterface("eth0", Method.MANUAL, null);
    }
}
