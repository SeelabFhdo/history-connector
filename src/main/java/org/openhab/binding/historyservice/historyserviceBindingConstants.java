/**
 * Copyright (c) 2014-2015 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.historyservice;

import org.eclipse.smarthome.core.thing.ThingTypeUID;

/**
 * The {@link historyserviceBinding} class defines common constants, which are
 * used across the whole binding.
 *
 * @author Philip Wizenty - Initial contribution
 */
public class historyserviceBindingConstants {

    public static final String BINDING_ID = "historyservice";

    // List of all Thing Type UIDs
    public final static ThingTypeUID THING_TYPE_SAMPLE = new ThingTypeUID(BINDING_ID, "HistoryService");

    // List of all Channel ids
    public final static String CHANNEL_1 = "channel1";

    // List of bridge parameters
    public final static String BROKERURL = "brokerurl";
    public final static String INSTANCE = "instance";
    public final static String CERTPATH = "certpath";
    public final static String CERTPASSWORD = "certpassword";
    public final static String USERNAME = "username";
    public final static String PASSWORD = "password";

    // Value of bridge parameters
    public static String VALUE_BROKER_URL;
    public static String VALUE_INSTANCE;
    public static String VALUE_CERTPATH;
    public static String VALUE_CERTPASSWORD;
    public static String VALUE_USERNAME;
    public static String VALUE_PASSWORD;

}
