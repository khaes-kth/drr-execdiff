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
package org.apache.commons.math;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.math.exception.MathThrowable;
import org.apache.commons.math.exception.util.Localizable;
import org.apache.commons.math.exception.util.LocalizedFormats;


/**
* Base class for commons-math checked exceptions.
* <p>
* Supports nesting, emulating JDK 1.4 behavior if necessary.</p>
* <p>
* Adapted from <a href="http://commons.apache.org/collections/api-release/org/apache/commons/collections/FunctorException.html"/>.</p>
*
* @version $Revision$ $Date$
*/
public class MathException extends Exception implements MathThrowable {

    /** Serializable version identifier. */
    private static final long serialVersionUID = 7428019509644517071L;

    /** Deprecation message. */
    private static final String DEPRECATION_MESSAGE = "This class is deprecated; calling this method is a bug.";

    /**
     * Pattern used to build the message.
     */
    private final Localizable pattern;

    /**
     * Arguments used to build the message.
     */
    private final Object[] arguments;

    /**
     * Constructs a new <code>MathException</code> with no
     * detail message.
     */
    public MathException() {
        this.pattern   = LocalizedFormats.SIMPLE_MESSAGE;
        this.arguments = new Object[] { "" };
    }

    /**
     * Constructs a new <code>MathException</code> with specified
     * formatted detail message.
     * Message formatting is delegated to {@link java.text.MessageFormat}.
     * @param pattern format specifier
     * @param arguments format arguments
     * @since 2.2
     */
    public MathException(Localizable pattern, Object ... arguments) {
      this.pattern   = pattern;
      this.arguments = (arguments == null) ? new Object[0] : arguments.clone();
    }

    /**
     * Constructs a new <code>MathException</code> with specified
     * nested <code>Throwable</code> root cause.
     *
     * @param rootCause  the exception or error that caused this exception
     *                   to be thrown.
     */
    public MathException(Throwable rootCause) {
        super(rootCause);
        this.pattern   = LocalizedFormats.SIMPLE_MESSAGE;
        this.arguments = new Object[] { (rootCause == null) ? "" : rootCause.getMessage() };
    }

    /**
     * Constructs a new <code>MathException</code> with specified
     * formatted detail message and nested <code>Throwable</code> root cause.
     * Message formatting is delegated to {@link java.text.MessageFormat}.
     * @param rootCause the exception or error that caused this exception
     * to be thrown.
     * @param pattern format specifier
     * @param arguments format arguments
     * @since 2.2
     */
    public MathException(Throwable rootCause, Localizable pattern, Object ... arguments) {
      super(rootCause);
      this.pattern   = pattern;
      this.arguments = (arguments == null) ? new Object[0] : arguments.clone();
    }

    /** {@inheritDoc} */
    public void addMessage(Localizable pat,
                           Object ... args) {
        throw new UnsupportedOperationException(DEPRECATION_MESSAGE);
    }

    /** {@inheritDoc} */
    public void setContext(String key, Object value) {
        throw new UnsupportedOperationException(DEPRECATION_MESSAGE);
    }

    /** {@inheritDoc} */
    public Object getContext(String key) {
        throw new UnsupportedOperationException(DEPRECATION_MESSAGE);
    }

    /** {@inheritDoc} */
    public Set<String> getContextKeys() {
        throw new UnsupportedOperationException(DEPRECATION_MESSAGE);
    }

    /** Gets the message in a specified locale.
     *
     * @param locale Locale in which the message should be translated
     *
     * @return localized message
     * @since 1.2
     */
    public String getMessage(final Locale locale) {
        if (pattern != null) {
            return new MessageFormat(pattern.getLocalizedString(locale), locale).format(arguments);
        }
        return "";
    }

    /** {@inheritDoc} */
    @Override
    public String getMessage() {
        return getMessage(Locale.US);
    }

    /** {@inheritDoc} */
    @Override
    public String getLocalizedMessage() {
        return getMessage(Locale.getDefault());
    }

    /**
     * Prints the stack trace of this exception to the standard error stream.
     */
    @Override
    public void printStackTrace() {
        printStackTrace(System.err);
    }

    /**
     * Prints the stack trace of this exception to the specified stream.
     *
     * @param out  the <code>PrintStream</code> to use for output
     */
    @Override
    public void printStackTrace(PrintStream out) {
        synchronized (out) {
            PrintWriter pw = new PrintWriter(out, false);
            printStackTrace(pw);
            // Flush the PrintWriter before it's GC'ed.
            pw.flush();
        }
    }

}
