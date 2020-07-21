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

package com.oscarg798.amiibowiki.nfcreader.mvi

import com.oscarg798.amiibowiki.core.AmiiboIdentifier
import com.oscarg798.amiibowiki.core.mvi.ViewState
import com.oscarg798.amiibowiki.nfcreader.errors.NFCReaderFailure

data class NFCReaderViewState(
    val loading: ViewState.LoadingState,
    val status: Status,
    val adapterStatus: AdapterStatus,
    val error: NFCReaderFailure? = null
) : ViewState<NFCReaderResult> {

    sealed class Status {
        object None : Status()
        data class ReadSuccessful(val amiiboIdentifier: AmiiboIdentifier) : Status()
    }

    sealed class AdapterStatus {
        object Idle : AdapterStatus()
        object AdapterAvailable : AdapterStatus()
        object AdapterReadyToBeStoped : AdapterStatus()
    }

    override fun reduce(result: NFCReaderResult): ViewState<NFCReaderResult> {
        return when (result) {
            is NFCReaderResult.Reading -> copy(
                loading = ViewState.LoadingState.Loading,
                adapterStatus = AdapterStatus.Idle,
                status = Status.None,
                error = null
            )
            is NFCReaderResult.ReadSuccessful -> copy(
                loading = ViewState.LoadingState.None,
                status = Status.ReadSuccessful(result.amiiboIdentifier),
                adapterStatus = AdapterStatus.Idle,
                error = null
            )
            is NFCReaderResult.AdapterDisabled -> copy(
                loading = ViewState.LoadingState.None,
                status = Status.None,
                adapterStatus = AdapterStatus.Idle,
                error = NFCReaderFailure.AdapterDisabled
            )
            is NFCReaderResult.AdapterReady -> copy(
                loading = ViewState.LoadingState.None,
                status = Status.None,
                adapterStatus = AdapterStatus.AdapterAvailable,
                error = null
            )
            is NFCReaderResult.Error -> copy(
                loading = ViewState.LoadingState.None,
                status = Status.None,
                adapterStatus = AdapterStatus.Idle,
                error = result.error
            )
            is NFCReaderResult.AdapterStoped -> copy(
                loading = ViewState.LoadingState.None,
                status = Status.None,
                adapterStatus = AdapterStatus.AdapterReadyToBeStoped,
                error = null
            )
        }
    }

    companion object {
        fun init() = NFCReaderViewState(
            ViewState.LoadingState.None,
            Status.None,
            AdapterStatus.Idle,
            null
        )
    }
}
