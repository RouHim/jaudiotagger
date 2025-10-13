/*
 * Jaudiotagger Copyright (C)2004,2005
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public  License as published by the Free Software Foundation; either version 2.1 of the License,
 * or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this library; if not,
 * you can getFields a copy from http://www.opensource.org/licenses/lgpl-license.php or write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 */
package org.jaudiotagger;

import org.jaudiotagger.logging.ErrorMessage;
import org.jaudiotagger.tag.TagOptionSingleton;
import org.junit.jupiter.api.BeforeEach;

import java.util.EnumMap;
import java.util.regex.Pattern;

/**
 *
 */
public abstract class AbstractTestCase extends AbstractBaseTestCase {

    /**
     * Stores a {@link Pattern} for each {@link ErrorMessage}.<br>
     * Place holders like &quot;{&lt;number&gt;}&quot; will be replaced with
     * &quot;.*&quot;.<br>
     */
    private static final EnumMap<ErrorMessage, Pattern> ERROR_PATTERNS;

    static {
        ERROR_PATTERNS = new EnumMap<>(ErrorMessage.class);
        for (ErrorMessage curr : ErrorMessage.values()) {
            final String regex = curr.getMsg().replaceAll("\\{\\d+\\}", ".*");
            ERROR_PATTERNS.put(
                    curr,
                    Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.DOTALL)
            );
        }
    }

    @BeforeEach
    public void setUp() {
        TagOptionSingleton.getInstance().setToDefault();
    }
}
