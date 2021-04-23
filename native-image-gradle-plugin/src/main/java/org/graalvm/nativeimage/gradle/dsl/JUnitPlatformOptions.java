/*
 * Copyright (c) 2021, 2021 Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * The Universal Permissive License (UPL), Version 1.0
 *
 * Subject to the condition set forth below, permission is hereby granted to any
 * person obtaining a copy of this software, associated documentation and/or
 * data (collectively the "Software"), free of charge and under any and all
 * copyright rights in the Software, and any and all patent rights owned or
 * freely licensable by each licensor hereunder covering either (i) the
 * unmodified Software as contributed to or provided by such licensor, or (ii)
 * the Larger Works (as defined below), to deal in both
 *
 * (a) the Software, and
 *
 * (b) any piece of software and/or hardware listed in the lrgrwrks.txt file if
 * one is included with the Software each a "Larger Work" to which the Software
 * is contributed by such licensors),
 *
 * without restriction, including without limitation the rights to copy, create
 * derivative works of, display, perform, and distribute the Software and make,
 * use, sell, offer for sale, import, export, have made, and have sold the
 * Software and the Larger Work(s), and to sublicense the foregoing rights on
 * either these or other terms.
 *
 * This license is subject to the following condition:
 *
 * The above copyright notice and either this complete permission notice or at a
 * minimum a reference to the UPL must be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.graalvm.nativeimage.gradle.dsl;

import org.graalvm.nativeimage.gradle.Utils;
import org.gradle.api.Project;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.SourceSet;

import javax.annotation.Nullable;

public class JUnitPlatformOptions extends NativeImageOptions {
    public static final String EXTENSION_NAME = "nativeTest";

    private final Property<TestMode> mode;

    @SuppressWarnings("UnstableApiUsage")
    public JUnitPlatformOptions(ObjectFactory objectFactory) {
        super(objectFactory);
        this.mode = objectFactory.property(TestMode.class).convention(TestMode.TEST_LISTENER);
        super.setMain("org.graalvm.junit.platform.NativeImageJUnitLauncher");
        super.setImageName(Utils.NATIVE_TESTS_EXE);
    }

    public Property<TestMode> getMode() {
        return this.mode;
    }

    @Override
    public NativeImageOptions setMain(@Nullable String main) {
        throw new IllegalStateException("Main class for test task cannot be changed");
    }

    @Override
    public NativeImageOptions setImageName(@Nullable String image) {
        throw new IllegalStateException("Image name for test task cannot be changed");
    }

    @Override
    public void configure(Project project) {
        if (System.getProperty("testDiscovery") != null
                || getMode().get().equals(TestMode.DISCOVERY)) {
            systemProperty("testDiscovery", "1");
        }
        args("--features=org.graalvm.junit.platform.JUnitPlatformFeature");
        super.configure(project, SourceSet.TEST_SOURCE_SET_NAME);
    }
}
