/*
 * Java Scan, a library for scanning and configuring HBM devices.
 *
 * The MIT License (MIT)
 *
 * Copyright (C) Hottinger Baldwin Messtechnik GmbH
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

package com.hbm.devices.scan.configure;

import java.lang.reflect.Type;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import com.hbm.devices.scan.JsonRpc;
import com.hbm.devices.scan.ScanConstants;

/**
 * This class gets <a href="http://www.jsonrpc.org/specification">JSON-RPC 2.0</a>
 * response messages, parses them and notifies {@link Response} objects.
 * <p>
 * The whole class is designed as a best effort service. So invalid JSON messages, or messages that
 * do not conform to the HBM network discovery and configuration protocol are simply ignored. Users
 * of this class will <em>not</em> get any error messages or exceptions.
 *
 * @since 1.0
 */
public final class ResponseDeserializer extends Observable implements Observer {

    private final Gson gson;
    private static final Logger LOGGER = Logger.getLogger(ScanConstants.LOGGER_NAME);

    /**
     * Constructs a {@link ResponseDeserializer} object.
     */
    public ResponseDeserializer() {
        super();

        final GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(JsonRpc.class, new JsonRpcDeserializer());
        gson = builder.create();
    }

    @Override
    public void update(Observable observable, Object arg) {
        final String message = (String)arg;
        try {
            final JsonRpc json = gson.fromJson(message, JsonRpc.class);
            if (json != null) {
                setChanged();
                notifyObservers(json);
            }
        } catch (JsonSyntaxException e) {
            /*
             * There is no error handling necessary in this case. If somebody sends us invalid JSON,
             * we just ignore the packet and go ahead.
             */
            LOGGER.log(Level.SEVERE, "Can't parse JSON!", e);
        }
    }

    private static final class JsonRpcDeserializer implements JsonDeserializer<JsonRpc> {

        JsonRpcDeserializer() {
            // This constructor is only use by the outer class.
        }
    
        @Override
        public JsonRpc deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
    
            JsonRpc rpcObject = null;
            final JsonObject jsonObject = json.getAsJsonObject();
            
            final JsonElement identifier = jsonObject.get("id");
            if (identifier != null) {
                final String value = identifier.toString();
                if ((!"\"\"".equals(value)) && 
                    (jsonObject.has("result") ^ jsonObject.has("error"))) {
                    
                    rpcObject = context.deserialize(json, Response.class);
                    rpcObject.setJSONString(jsonObject.toString());
                }
            }
            return rpcObject;
        }
    }
}

