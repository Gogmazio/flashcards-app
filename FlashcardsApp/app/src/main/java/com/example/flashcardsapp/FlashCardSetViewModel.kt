package com.example.flashcardsapp
import android.util.Log
import androidx.lifecycle.ViewModel

private const val TAG = "FlashCardSetViewModel"

class FlashCardSetViewModel : ViewModel() {
    private val flashCardSetNames = mutableListOf<String>()
    private val flashCardSets = mutableListOf<FlashCardSet>()

    fun addSet(flashCardSetName: String, flashCardSet: FlashCardSet) {
        flashCardSetNames.add(flashCardSetName)
        flashCardSets.add(flashCardSet)
    }

    fun removeSet(index: Int) {
        flashCardSets.removeAt(index);
    }

    fun addCardToSet(index: Int, front: String, back: String) {
        flashCardSets[index].addCard(front, back);
    }

    fun removeCardToSet(setIndex: Int, index: Int) {
        flashCardSets[setIndex].removeCard(index);
    }

//    fun checkAnswer(answer: String): Boolean {
//        return flashCardSets[currentSet].checkAnswer(answer);
//    }

    fun size(): Int {
        return flashCardSets.size;
    }

    fun sizeOfSet(index: Int): Int {
        return flashCardSets[index].size()
    }

//    fun getName(): String {
//        return flashCardSetNames[currentSet]
//    }

    fun getNames(): List<String> {
        return flashCardSetNames
    }

    fun getFronts(index: Int): List<String> {
        return flashCardSets[index].getFronts()
    }

    fun getFrontAt(setIndex: Int, cardIndex: Int): String {
        return flashCardSets[setIndex].front(cardIndex)
    }

    fun getBackAt(setIndex: Int, cardIndex: Int): String {
        return flashCardSets[setIndex].back(cardIndex)
    }
}
