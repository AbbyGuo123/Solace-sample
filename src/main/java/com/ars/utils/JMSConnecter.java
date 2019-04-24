package com.ars.utils;

import com.solacesystems.jcsmp.JCSMPException;
import com.solacesystems.jcsmp.JCSMPFactory;
import com.solacesystems.jcsmp.JCSMPProperties;
import com.solacesystems.jcsmp.JCSMPSession;

/**
 * @Author: Abby
 * @Description:
 * @Date: Create in 8:28 AM 4/24/2019
 * @Modified By:
 */
public class JMSConnecter {

    public static JCSMPSession getJcsmpSession() throws JCSMPException {
        final JCSMPProperties properties = new JCSMPProperties();
        properties.setProperty(JCSMPProperties.HOST, "192.168.99.100:55555");
        properties.setProperty(JCSMPProperties.USERNAME,"client-username");
        properties.setProperty(JCSMPProperties.VPN_NAME,"default");
        properties.setProperty(JCSMPProperties.PASSWORD,"client-password");
        final JCSMPSession session = JCSMPFactory.onlyInstance().createSession(properties);
        session.connect();
        return session;
    }
}
