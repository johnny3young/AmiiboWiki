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

package com.oscarg798.amiibowiki.amiibodetail

import com.oscarg798.amiibowiki.amiibodetail.models.ViewAmiiboDetails
import com.oscarg798.amiibowiki.amiibodetail.mvi.AmiiboDetailResult
import com.oscarg798.amiibowiki.amiibodetail.mvi.AmiiboDetailViewState
import com.oscarg798.amiibowiki.core.models.Amiibo
import com.oscarg798.amiibowiki.core.models.AmiiboReleaseDate
import com.oscarg798.amiibowiki.core.models.GameSearchResult
import com.oscarg798.amiibowiki.core.mvi.ViewState
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class AmiiboDetailViewStateTest {

    private lateinit var state: AmiiboDetailViewState

    @Before
    fun setup() {
        state = AmiiboDetailViewState.init()
    }

    @Test
    fun `when amiibo detail fetch is success then state should reflect the change`() {
        val newState =
            state.reduce(
                AmiiboDetailResult.DetailFetched(
                    VIEW_AMIIBO_DETAIL,
                    false
                )
            ) as AmiiboDetailViewState

        Assert.assertNull(newState.error)
        assert(newState.loading is ViewState.LoadingState.None)
        assert(newState.status is AmiiboDetailViewState.Status.ShowingAmiiboDetails)
        Assert.assertEquals(
            VIEW_AMIIBO_DETAIL,
            (newState.status as AmiiboDetailViewState.Status.ShowingAmiiboDetails).amiiboDetails
        )
        Assert.assertEquals(
            false,
            (newState.status as AmiiboDetailViewState.Status.ShowingAmiiboDetails).isRelatedGamesSectionEnabled
        )
    }

    @Test
    fun `when amiibo detail fetch is success and related games are enable then state should reflect the change`() {
        val newState =
            state.reduce(
                AmiiboDetailResult.DetailFetched(
                    VIEW_AMIIBO_DETAIL,
                    true
                )
            ) as AmiiboDetailViewState

        Assert.assertNull(newState.error)
        assert(newState.loading is ViewState.LoadingState.None)
        assert(newState.status is AmiiboDetailViewState.Status.ShowingAmiiboDetails)
        Assert.assertEquals(
            VIEW_AMIIBO_DETAIL,
            (newState.status as AmiiboDetailViewState.Status.ShowingAmiiboDetails).amiiboDetails
        )
        Assert.assertEquals(
            true,
            (newState.status as AmiiboDetailViewState.Status.ShowingAmiiboDetails).isRelatedGamesSectionEnabled
        )
    }

    @Test
    fun `when result is loading then state should reflect this status`() {
        val newState =
            state.reduce(AmiiboDetailResult.Loading) as AmiiboDetailViewState

        Assert.assertNull(newState.error)
        assert(newState.status is AmiiboDetailViewState.Status.None)
        assert(newState.loading is ViewState.LoadingState.Loading)
    }

    @Test
    fun `when result is show game details then state should reflect this status`() {
        val newState =
            state.reduce(
                AmiiboDetailResult.ShowGameDetails(
                    GAME_ID,
                    VIEW_AMIIBO_DETAIL.gameSeries
                )
            ) as AmiiboDetailViewState

        Assert.assertNull(newState.error)
        assert(newState.status is AmiiboDetailViewState.Status.ShowingGameDetails)
        Assert.assertEquals(
            AmiiboDetailViewState.Status.ShowingGameDetails(
                GAME_ID,
                VIEW_AMIIBO_DETAIL.gameSeries
            ),
            newState.status
        )
        assert(newState.loading is ViewState.LoadingState.None)
    }

    @Test
    fun `when result is none then state should be same as the init one`() {
        val newState = state.reduce(AmiiboDetailResult.None) as AmiiboDetailViewState

        newState shouldBeEqualTo AmiiboDetailViewState.init()
    }
}

private const val GAME_ID = 1
private val GAME_SEARCH_RESULTS = listOf(GameSearchResult(1, "2", "3", 4))
private val VIEW_AMIIBO_DETAIL = ViewAmiiboDetails(
    Amiibo(
        "1",
        "2",
        "3",
        "4",
        "5",
        "6",
        AmiiboReleaseDate("7", "8", "9", "10"),
        "11", "12"
    ),
    GAME_SEARCH_RESULTS
)
