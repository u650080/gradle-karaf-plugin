/**
 * Copyright 2016, Luca Burgazzoli and contributors as indicated by the @author tags
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.lburgazzoli.gradle.plugin.karaf.features.model

import org.gradle.util.ConfigureUtil

/**
 * DSL extension allowing instruction on how to produce a {@code <bundle/>} entry
 * in a Karaf features repository file
 *
 * @author Steve Ebersole
 * @author Luca Burgazzoli
 */
class BundleInstructionDescriptor {
	private DependencyMatcher matcher
    private DependencyDescriptor remap
    private WrapInstructions wrapInstructions;

	boolean include;
	String startLevel;
	boolean dependency

    protected BundleInstructionDescriptor(DependencyMatcher matcher) {
        this.matcher = matcher
        this.remap = null
        this.wrapInstructions = null
        this.include = true
        this.startLevel = null
        this.dependency = false
    }

	DependencyMatcher getMatcher() {
		return matcher
	}

    boolean matches(DependencyDescriptor dependency) {
        return matcher ? matcher.matches(dependency) : false
    }


    // *************************************************************************
    // Remap
    // *************************************************************************

	def remap(Closure closure) {
		ConfigureUtil.configure( closure, remap = new DependencyDescriptor())
	}

    def remap(Map properties) {
		ConfigureUtil.configureByMap( properties, remap = new DependencyDescriptor())
	}

    boolean hasRemap() {
        return this.remap != null;
    }

    DependencyDescriptor getRemap() {
		return remap
	}


    // *************************************************************************
    // Wrap
    // *************************************************************************

	def wrap(Closure closure) {
		ConfigureUtil.configure(
            closure,
            wrapInstructions = new WrapInstructions()
        )
	}

    def wrap(Map<String, String> properties) {
        wrapInstructions = new WrapInstructions()
        wrapInstructions.instructions(properties)
    }

	def boolean hasWrapInstructions() {
		return wrapInstructions != null;
	}

    WrapInstructions getWrapInstructions() {
		return wrapInstructions
	}

    public static class WrapInstructions {
        def Map<String, String> instructions

        public void instruction(String key, String value) {
            if ( instructions == null ) {
                instructions = new HashMap<String,String>()
            }

            instructions.put( key, value )
        }

        public void instructions(Map<String,String> instructions) {
            if ( instructions == null ) {
                instructions = new HashMap<String,String>()
            }

            instructions.putAll(instructions)
        }
    }
}
