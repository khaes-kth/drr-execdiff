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
package org.apache.commons.lang.text;

import java.text.DateFormat;
import java.util.Locale;

/**
 * Stock "date" MetaFormat.
 * 
 * @see {@link ExtendedMessageFormat}
 * @author Matt Benson
 * @since 2.4
 * @version $Id$
 */
public class DateMetaFormat extends DateMetaFormatSupport {
    private static final long serialVersionUID = -4732179430347600208L;

    /**
     * Create a new DateMetaFormat.
     */
    public DateMetaFormat() {
        super();
    }

    /**
     * Create a new DateMetaFormat.
     * 
     * @param locale
     */
    public DateMetaFormat(Locale locale) {
        super(locale);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.commons.lang.text.AbstractDateMetaFormat#createSubformatInstance(int)
     */
    protected DateFormat createSubformatInstance(int style) {
        return DateFormat.getDateInstance(style, getLocale());
    }
}
