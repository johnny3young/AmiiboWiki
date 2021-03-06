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

package com.oscarg798.amiibowiki.core.models

sealed class AgeRatingCategory {

    object ESRB : AgeRatingCategory()
    object PEGI : AgeRatingCategory()

    companion object {
        fun getCategory(categoryId: Int) = when (categoryId) {
            1 -> ESRB
            else -> PEGI
        }
    }
}

sealed class Rating(val id: Int) {

    object PEGI3 : Rating(1)
    object PEGI7 : Rating(2)
    object PEGI12 : Rating(3)
    object PEGI16 : Rating(4)
    object PEGI18 : Rating(5)
    object ESRBRatingPending : Rating(6)
    object ESRBEC : Rating(7)
    object ESRBEveryone : Rating(8)
    object ESRB10 : Rating(9)
    object ESRBTeen : Rating(10)
    object ESRBMature : Rating(11)
    object ESRBAdultsOnly : Rating(12)

    companion object {

        fun getRating(rating: Int) = when (rating) {
            1 -> PEGI3
            2 -> PEGI7
            3 -> PEGI12
            4 -> PEGI16
            5 -> PEGI18
            6 -> ESRBRatingPending
            7 -> ESRBEC
            8 -> ESRBEveryone
            9 -> ESRB10
            10 -> ESRBTeen
            11 -> ESRBMature
            else -> ESRBAdultsOnly
        }
    }
}

data class AgeRating(
    val category: AgeRatingCategory,
    val rating: Rating
)
