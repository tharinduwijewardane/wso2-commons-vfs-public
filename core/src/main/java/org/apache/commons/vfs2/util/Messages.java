/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.vfs2.util;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Formats messages.
 *
 * @author <a href="http://commons.apache.org/vfs/team-list.html">Commons VFS team</a>
 * @version $Revision: 1035190 $ $Date: 2010-11-15 14:28:37 +0530 (Mon, 15 Nov 2010) $
 */
public final class Messages
{
    /**
     * Map from message code to MessageFormat object for the message.
     */
    private static ConcurrentMap<String, MessageFormat> messages =
        new ConcurrentHashMap<String, MessageFormat>();
    private static ResourceBundle resources;

    private Messages()
    {
    }

    /**
     * Formats a message.
     *
     * @param code The message code.
     * @return The formatted message.
     */
    public static String getString(final String code)
    {
        return getString(code, new Object[0]);
    }

    /**
     * Formats a message.
     *
     * @param code  The message code.
     * @param param The message parameter.
     * @return The formatted message.
     */
    public static String getString(final String code, final Object param)
    {
        return getString(code, new Object[]{param});
    }

    /**
     * Formats a message.
     *
     * @param code   The message code.
     * @param params The message parameters.
     * @return The formatted message.
     */
    public static String getString(final String code, final Object[] params)
    {
        try
        {
            if (code == null)
            {
                return null;
            }

            final MessageFormat msg = findMessage(code);
            return msg.format(params);
        }
        catch (final MissingResourceException mre)
        {
            return "Unknown message with code \"" + code + "\".";
        }
    }

    /**
     * Locates a message by its code.
     */
    private static MessageFormat findMessage(final String code)
        throws MissingResourceException
    {
        // Check if the message is cached
        MessageFormat msg = messages.get(code);
        if (msg != null)
        {
            return msg;
        }

        // Locate the message
        if (resources == null)
        {
            resources = new CombinedResources("org.apache.commons.vfs2.Resources");
        }
        final String msgText = resources.getString(code);
        msg = new MessageFormat(msgText);
        messages.putIfAbsent(code, msg);
        return messages.get(code);
    }
}
