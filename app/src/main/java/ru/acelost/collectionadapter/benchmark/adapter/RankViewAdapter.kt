package ru.acelost.collectionadapter.benchmark.adapter

import android.view.View
import android.view.ViewGroup
import ru.acelost.collectionadapter.benchmark.Rank
import ru.acelost.collectionadapter.benchmark.binding.AvatarViewBinding
import ru.acelost.collectionadapter.benchmark.binding.NameViewBinding
import ru.acelost.collectionadapter.benchmark.binding.StartViewBinding
import ru.acelost.collectionadapter.benchmark.binding.ViewBinding
import java.lang.IllegalStateException

private const val STARS = 10

class RankViewAdapter {

    companion object {
        const val VIEW_TYPE_AVATAR = 1
        const val VIEW_TYPE_NAME = 2
        const val VIEW_TYPE_STAR = 3
    }

    private var model: Rank = Rank.random(0)

    fun setModel(rank: Rank) {
        model = rank
    }

    fun getItemCount(): Int {
        return getPositionOfStar(STARS - 1) + 1
    }

    fun getViewType(position: Int): Int {
        return when {
            position == getPositionOfAvatar() -> VIEW_TYPE_AVATAR
            position <= getPositionOfLastName() -> VIEW_TYPE_NAME
            else -> VIEW_TYPE_STAR
        }
    }

    fun onCreateView(parent: ViewGroup, viewType: Int): ViewBinding<View> {
        return when (viewType) {
            VIEW_TYPE_AVATAR -> AvatarViewBinding(parent)
            VIEW_TYPE_NAME -> NameViewBinding(parent)
            VIEW_TYPE_STAR -> StartViewBinding(parent)
            else -> throw IllegalStateException("Unknown viewType $viewType.")
        }
    }

    fun onBindView(binding: ViewBinding<View>, position: Int) {
        when {
            binding is AvatarViewBinding -> {
                binding.setAvatar(model.person.firstName.substring(0, 1), model.person.iconBackground)
            }
            binding is NameViewBinding -> {
                when (position) {
                    getPositionOfFirstName() -> binding.setName(model.person.firstName)
                    getPositionOfMiddleName() -> binding.setName(model.person.middleName)
                    getPositionOfLastName() -> binding.setName(model.person.lastName)
                }
            }
            binding is StartViewBinding -> {
                val firstStar = getPositionOfStar(0)
                val delta = position - firstStar + 1
                val state = Math.max(model.rank - (delta * 2), 0)
                binding.setStarState(state)
            }
        }
    }

    private fun getPositionOfAvatar(): Int {
        return 0
    }

    private fun getPositionOfFirstName(): Int {
        return 1
    }

    private fun getPositionOfMiddleName(): Int {
        val firstNamePosition = getPositionOfFirstName()
        return if (model.person.middleName != null)
            firstNamePosition + 1
        else
            firstNamePosition
    }

    private fun getPositionOfLastName(): Int {
        val middleNamePosition = getPositionOfMiddleName()
        return if (model.person.lastName != null)
            middleNamePosition + 1
        else
            middleNamePosition
    }

    private fun getPositionOfStar(index: Int): Int {
        return getPositionOfLastName() + 1 + index
    }

}