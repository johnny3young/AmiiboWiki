/*
 * Copyright 2020 Oscar David Gallon Rosero
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 *
 */

package com.oscarg798.amiibowiki.core.featureflaghandler

import com.oscarg798.flagly.featureflag.FeatureFlag
import com.oscarg798.flagly.featureflag.FeatureFlagHandler
import com.oscarg798.flagly.featureflag.FeatureFlagProvider
import com.oscarg798.flagy.exceptions.FeatureFlagNotPresentInHandlerException

class AmiiboWikiFeatureFlagHandler(
    private val localFeatureFlagHandler: FeatureFlagHandler? = null,
    private val remoteConfigFeatureFlagHandler: FeatureFlagHandler
) : FeatureFlagHandler, FeatureFlagProvider {

    override fun isFeatureEnabled(featureFlag: FeatureFlag): Boolean {
        return if (localFeatureFlagHandler != null) {
            getLocalValueOrFallbackToRemote(featureFlag)
        } else {
            remoteConfigFeatureFlagHandler.isFeatureEnabled(featureFlag)
        }
    }

    private fun getLocalValueOrFallbackToRemote(featureFlag: FeatureFlag): Boolean {
        require(localFeatureFlagHandler != null)
        return try {
            localFeatureFlagHandler.isFeatureEnabled(featureFlag)
        } catch (e: FeatureFlagNotPresentInHandlerException) {
            remoteConfigFeatureFlagHandler.isFeatureEnabled(featureFlag)
        }
    }

    override fun provideAppSupportedFeatureflags(): Collection<FeatureFlag> {
        return AmiiboWikiFeatureFlag.getValues()
    }
}
