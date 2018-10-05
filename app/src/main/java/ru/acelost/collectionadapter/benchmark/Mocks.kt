package ru.acelost.collectionadapter.benchmark

import android.support.annotation.DrawableRes
import ru.acelost.collectionadapter.R
import java.util.*

data class IconBackground(
        @DrawableRes val drawableRes: Int
) {
    companion object Factory {
        fun random(seed: Long): IconBackground {
            return oneOf(seed, iconBackgrounds)
        }
    }
}

data class Person(
        val firstName: String,
        val middleName: String? = null,
        val lastName: String? = null,
        val iconBackground: IconBackground
) {
    companion object Factory {
        fun random(seed: Long): Person {
            return Person(
                    oneOf(seed, firstNames),
                    oneOfOrNull(seed, middleNames, 0.3f),
                    oneOfOrNull(seed, lastNames, 0.5f),
                    IconBackground.random(seed)
            )
        }
    }
}

data class Rank(
        val person: Person,
        val rank: Int
) {
    companion object Factory {
        fun random(seed: Long): Rank {
            return Rank(
                    Person.random(seed),
                    Random(seed).nextInt(20)
            )
        }
    }
}

// Utils
private fun <T> oneOf(seed: Long, options: List<T>): T {
    val index = Random(seed).nextInt(options.size)
    return options[index]
}

private fun <T> oneOfOrNull(seed: Long, options: List<T>, nullProbability: Float): T? {
    if (Random().nextFloat() > nullProbability) {
        return oneOf(seed, options)
    }
    return null
}

// Data
private val iconBackgrounds = listOf(
        IconBackground(R.drawable.shape_circle_blue),
        IconBackground(R.drawable.shape_circle_gray),
        IconBackground(R.drawable.shape_circle_green),
        IconBackground(R.drawable.shape_circle_yellow),
        IconBackground(R.drawable.shape_circle_red)
)

private val firstNames = listOf(
        "Louis",
        "Rosan",
        "Lilli",
        "Caden",
        "Alees",
        "Barri",
        "Anton",
        "Elmar",
        "Jarro",
        "Linus"
)

private val middleNames = listOf(
        "Marle",
        "Meade",
        "Ida",
        "Calix",
        "Skyle",
        "Davey",
        "Merri",
        "Riple",
        "Willy",
        "Eckha"
)

private val lastNames = listOf(
        "Geary",
        "Ibbot",
        "Simms",
        "Beck",
        "Leona",
        "Dixon",
        "Harla",
        "Kidd",
        "Kings",
        "Leyto"
)