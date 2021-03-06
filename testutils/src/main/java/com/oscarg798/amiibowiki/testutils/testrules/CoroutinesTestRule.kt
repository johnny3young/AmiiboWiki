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

package com.oscarg798.amiibowiki.testutils.testrules

import com.oscarg798.amiibowiki.core.utils.CoroutineContextProvider
import kotlin.reflect.KClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class CoroutinesTestRule : TestRule {

    private val job = SupervisorJob()
    val testDispatcher = TestCoroutineDispatcher()
    val testCoroutineScope =
        TestCoroutineScope(testDispatcher + job)
    val coroutineContextProvider =
        CoroutineContextProvider(
            testDispatcher,
            testDispatcher
        )

    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            override fun evaluate() {
                Dispatchers.setMain(testDispatcher)
                base.evaluate()
                job.cancel()
                testDispatcher.cleanupTestCoroutines()
                testCoroutineScope.cleanupTestCoroutines()
                Dispatchers.resetMain()
            }
        }
    }

    inline infix fun <reified T : Exception> wasAnExceptionRaisedInScopeOfType(culito: KClass<T>) {
        Assert.assertEquals(1, testCoroutineScope.uncaughtExceptions.size)
        Assert.assertTrue(testCoroutineScope.uncaughtExceptions.first()::class == culito)
    }
}
