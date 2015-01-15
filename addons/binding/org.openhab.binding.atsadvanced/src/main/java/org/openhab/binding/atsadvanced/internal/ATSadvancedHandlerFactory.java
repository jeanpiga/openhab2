/**
 * Copyright (c) 2014 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.atsadvanced.internal;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.eclipse.smarthome.config.core.Configuration;
import org.eclipse.smarthome.config.discovery.DiscoveryService;
import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandlerFactory;
import org.eclipse.smarthome.core.thing.binding.ThingHandler;
import org.openhab.binding.atsadvanced.ATSadvancedBindingConstants;
import org.openhab.binding.atsadvanced.discovery.ATSadvancedDiscoveryService;
import org.openhab.binding.atsadvanced.handler.AreaHandler;
import org.openhab.binding.atsadvanced.handler.PanelHandler;
import org.openhab.binding.atsadvanced.handler.ZoneHandler;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.ComponentContext;

import com.google.common.collect.Lists;

/**
 * The {@link ATSadvancedHandlerFactory} is responsible for creating things and thing
 * handlers.
 *
 * @author Karel Goderis - Initial contribution
 */
public class ATSadvancedHandlerFactory extends BaseThingHandlerFactory {

    public final static Collection<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = Lists.newArrayList(
            ATSadvancedBindingConstants.THING_TYPE_PANEL, ATSadvancedBindingConstants.THING_TYPE_AREA,
            ATSadvancedBindingConstants.THING_TYPE_ZONE);

    private Map<ThingUID, ServiceRegistration<?>> discoveryServiceRegs = new HashMap<>();
    private String monoPath;
    private String atsPath;

    @Override
    protected void activate(ComponentContext componentContext) {
        super.activate(componentContext);
        Dictionary<String, Object> properties = componentContext.getProperties();
        monoPath = (String) properties.get("mono");
        atsPath = (String) properties.get("ats");
    };

    @Override
    public boolean supportsThingType(ThingTypeUID thingTypeUID) {
        return SUPPORTED_THING_TYPES_UIDS.contains(thingTypeUID);
    }

    @Override
    public Thing createThing(ThingTypeUID thingTypeUID, Configuration configuration, ThingUID thingUID,
            ThingUID bridgeUID) {
        if (ATSadvancedBindingConstants.THING_TYPE_PANEL.equals(thingTypeUID)) {
            ThingUID panelUID = getPanelUID(thingTypeUID, thingUID, configuration);
            return super.createThing(thingTypeUID, configuration, panelUID, null);
        }
        if (ATSadvancedBindingConstants.THING_TYPE_AREA.equals(thingTypeUID)) {
            ThingUID blasterUID = getAreaUID(thingTypeUID, thingUID, configuration, bridgeUID);
            return super.createThing(thingTypeUID, configuration, blasterUID, bridgeUID);
        }
        if (ATSadvancedBindingConstants.THING_TYPE_ZONE.equals(thingTypeUID)) {
            ThingUID blasterUID = getZoneUID(thingTypeUID, thingUID, configuration, bridgeUID);
            return super.createThing(thingTypeUID, configuration, blasterUID, bridgeUID);
        }
        throw new IllegalArgumentException(
                "The thing type " + thingTypeUID + " is not supported by the ATS Advanced binding.");
    }

    @Override
    protected ThingHandler createHandler(Thing thing) {
        if (thing.getThingTypeUID().equals(ATSadvancedBindingConstants.THING_TYPE_PANEL)) {
            PanelHandler panel = new PanelHandler((Bridge) thing, monoPath, atsPath);
            registerATSadvancedDiscoveryService(panel);
            return panel;
        } else if (thing.getThingTypeUID().equals(ATSadvancedBindingConstants.THING_TYPE_AREA)) {
            return new AreaHandler(thing);
        } else if (thing.getThingTypeUID().equals(ATSadvancedBindingConstants.THING_TYPE_ZONE)) {
            return new ZoneHandler(thing);
        } else {
            return null;
        }
    }

    private synchronized void registerATSadvancedDiscoveryService(PanelHandler bridgeHandler) {
        ATSadvancedDiscoveryService discoveryService = new ATSadvancedDiscoveryService(bridgeHandler);
        // discoveryService.activate();
        this.discoveryServiceRegs.put(bridgeHandler.getThing().getUID(), bundleContext
                .registerService(DiscoveryService.class.getName(), discoveryService, new Hashtable<String, Object>()));
    }

    @Override
    protected synchronized void removeHandler(ThingHandler thingHandler) {
        if (thingHandler instanceof PanelHandler) {
            ServiceRegistration<?> serviceReg = this.discoveryServiceRegs.get(thingHandler.getThing().getUID());
            if (serviceReg != null) {
                // remove discovery service, if bridge handler is removed
                ATSadvancedDiscoveryService service = (ATSadvancedDiscoveryService) bundleContext
                        .getService(serviceReg.getReference());
                // service.deactivate();
                serviceReg.unregister();
                discoveryServiceRegs.remove(thingHandler.getThing().getUID());
            }
        }
    }

    private ThingUID getPanelUID(ThingTypeUID thingTypeUID, ThingUID thingUID, Configuration configuration) {
        if (thingUID == null) {
            String ipAddress = (String) configuration.get(PanelHandler.IP_ADDRESS);
            thingUID = new ThingUID(thingTypeUID, ipAddress);
        }
        return thingUID;
    }

    private ThingUID getAreaUID(ThingTypeUID thingTypeUID, ThingUID thingUID, Configuration configuration,
            ThingUID bridgeUID) {
        BigDecimal areaID = (BigDecimal) configuration.get(AreaHandler.NUMBER);

        if (thingUID == null) {
            thingUID = new ThingUID(thingTypeUID, "Area" + areaID, bridgeUID.getId());
        }
        return thingUID;
    }

    private ThingUID getZoneUID(ThingTypeUID thingTypeUID, ThingUID thingUID, Configuration configuration,
            ThingUID bridgeUID) {
        BigDecimal zoneID = (BigDecimal) configuration.get(ZoneHandler.NUMBER);

        if (thingUID == null) {
            thingUID = new ThingUID(thingTypeUID, "Zone" + zoneID, bridgeUID.getId());
        }
        return thingUID;
    }

}
