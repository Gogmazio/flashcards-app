package com.example.flashcardsapp

class FlashCardSet {
    private val currentIndex = 0;
    private val flashCardSet = mutableListOf<FlashCard>();

    fun addCard(front: String, back: String) {
        flashCardSet.add(FlashCard(front, back));
    }

    fun removeCard(index: Int) {
        flashCardSet.removeAt(index);
    }

    fun size(): Int {
        return flashCardSet.size
    }

    fun getFronts(): List<String> {
        val fronts = mutableListOf<String>()
        for (i in 0 until flashCardSet.size) {
            fronts.add(flashCardSet[i].front)
        }
        return fronts
    }

    fun front(index: Int): String {
        return flashCardSet[index].front
    }

    fun back(index: Int): String {
        return flashCardSet[index].back
    }

    fun checkAnswer(answer: String): Boolean {
        if (flashCardSet[currentIndex].back == answer) {
            return true;
        }
        return false;
    }
}
