package com.example.flashcardsapp

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import org.w3c.dom.Text

// Fix crash on next/prev button press

open class MainActivity : AppCompatActivity() {
    private lateinit var newSetButton: Button
    private lateinit var addSetButton: Button
    private lateinit var newCardButton: Button
    private lateinit var addCardButton: Button
    private lateinit var nextButton: Button
    private lateinit var prevButton: Button
    private lateinit var practiceButton: Button
    private lateinit var backButton: Button
    private lateinit var emptySetTextView: TextView
    private lateinit var emptyCardSetTextView: TextView
    private lateinit var flashCardTextView: TextView
    private lateinit var flashCardHintTextView: TextView
    private lateinit var inputNameEditText: EditText
    private lateinit var inputFrontEditText: EditText
    private lateinit var inputBackEditText: EditText
    private lateinit var cardSetListView: ListView
    private lateinit var cardListView: ListView
    private lateinit var mainWindow: LinearLayout
    private lateinit var addSetWindow: LinearLayout
    private lateinit var cardSetWindow: LinearLayout
    private lateinit var addCardWindow: LinearLayout
    private lateinit var flashCardWindow: LinearLayout

    private val flashCardSetViewModel: FlashCardSetViewModel by lazy {
        ViewModelProviders.of(this).get(FlashCardSetViewModel::class.java)
    }

    private var set_index = 0
    private var card_index = 0
    private var showing_front = true
    private var current_screen = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        mainWindow = findViewById(R.id.main_window)
        addSetWindow = findViewById(R.id.add_set_window)
        cardSetWindow = findViewById(R.id.card_set_window)
        addCardWindow = findViewById(R.id.add_card_window)
        flashCardWindow = findViewById(R.id.flash_card_window)

        newSetButton = findViewById(R.id.new_set_button)
        inputNameEditText = findViewById(R.id.input_name)
        addSetButton = findViewById(R.id.add_set_button)
        newCardButton = findViewById(R.id.new_card_button)
        inputFrontEditText = findViewById(R.id.input_front_card)
        inputBackEditText = findViewById(R.id.input_back_card)
        addCardButton = findViewById(R.id.add_card_button)
        emptySetTextView = findViewById(R.id.empty_set_string)
        emptyCardSetTextView = findViewById(R.id.empty_card_set_string)
        cardSetListView = findViewById(R.id.card_set_list)
        cardListView = findViewById(R.id.card_list)
        practiceButton = findViewById(R.id.practice_button)
        nextButton = findViewById(R.id.next_button)
        prevButton = findViewById(R.id.prev_button)
        flashCardTextView = findViewById(R.id.flashcard)
        flashCardHintTextView = findViewById(R.id.flashcard_hint)
        backButton = findViewById(R.id.back_button)

        mainWindow.visibility = View.VISIBLE
        cardSetWindow.visibility = View.INVISIBLE
        addCardWindow.visibility = View.INVISIBLE
        addSetWindow.visibility = View.INVISIBLE
        flashCardWindow.visibility = View.INVISIBLE

        newSetButton.setOnClickListener { view: View ->
            toAddSetWindow()
        }

        newCardButton.setOnClickListener { view: View ->
            toAddCardWindow()
        }

        addSetButton.setOnClickListener { view: View ->
            if (inputNameEditText.text.toString() == "") {
                Toast.makeText(this, "Please enter a name for your set", Toast.LENGTH_SHORT).show()
            }
            else {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.hideSoftInputFromWindow(view.windowToken, 0)
                addSet()
            }
        }

        addCardButton.setOnClickListener { view: View ->
            if (inputFrontEditText.text.toString() == "") {
                Toast.makeText(this, "Please enter the front of the flashcard", Toast.LENGTH_SHORT).show()
            }
            else if (inputBackEditText.text.toString() == "") {
                Toast.makeText(this, "Please enter the back of the flashcard", Toast.LENGTH_SHORT).show()
            }
            else {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.hideSoftInputFromWindow(view.windowToken, 0)
                addCard()
            }
        }

        practiceButton.setOnClickListener { view: View ->
            toFlashCardWindow()
        }

        nextButton.setOnClickListener { view: View ->
            nextCard()
        }

        prevButton.setOnClickListener { view: View ->
            prevCard()
        }

        backButton.setOnClickListener { view: View ->
            goBackOneWindow()
        }

        flashCardTextView.setOnClickListener { view ->
            flipCard()
        }

        cardSetListView.setOnItemClickListener { parent, view, position, id ->
            set_index = position
            toCardSetWindow()
        }

        // Can implement editing already made flashcards
//        cardListView.setOnItemClickListener { parent, view, position, id ->
//            card_index = position
//            toFlashCardWindow()
//        }
        toMainWindow()
    }

    fun toMainWindow() {
        mainWindow.visibility = View.VISIBLE
        cardSetWindow.visibility = View.INVISIBLE
        addCardWindow.visibility = View.INVISIBLE
        addSetWindow.visibility = View.INVISIBLE
        flashCardWindow.visibility = View.INVISIBLE
        backButton.visibility = View.INVISIBLE
        current_screen = 0
        updateMainWindow()
    }

    fun toAddSetWindow() {
        mainWindow.visibility = View.INVISIBLE
        cardSetWindow.visibility = View.INVISIBLE
        addCardWindow.visibility = View.INVISIBLE
        addSetWindow.visibility = View.VISIBLE
        flashCardWindow.visibility = View.INVISIBLE
        backButton.visibility = View.VISIBLE
        current_screen = 1
    }

    fun toCardSetWindow() {
        mainWindow.visibility = View.INVISIBLE
        cardSetWindow.visibility = View.VISIBLE
        addCardWindow.visibility = View.INVISIBLE
        addSetWindow.visibility = View.INVISIBLE
        flashCardWindow.visibility = View.INVISIBLE
        backButton.visibility = View.VISIBLE
        current_screen = 2
        updateSetWindow(set_index)
    }

    fun toAddCardWindow() {
        mainWindow.visibility = View.INVISIBLE
        cardSetWindow.visibility = View.INVISIBLE
        addCardWindow.visibility = View.VISIBLE
        addSetWindow.visibility = View.INVISIBLE
        flashCardWindow.visibility = View.INVISIBLE
        backButton.visibility = View.VISIBLE
        current_screen = 3
    }

    fun toFlashCardWindow() {
        mainWindow.visibility = View.INVISIBLE
        cardSetWindow.visibility = View.INVISIBLE
        addCardWindow.visibility = View.INVISIBLE
        addSetWindow.visibility = View.INVISIBLE
        flashCardWindow.visibility = View.VISIBLE
        backButton.visibility = View.VISIBLE
        showing_front = true
        current_screen = 4
        updateFlashCardWindow()
    }

    fun goBackOneWindow() {
        if (current_screen == 1) {
            toMainWindow()
        }
        else if (current_screen == 2) {
            toMainWindow()
        }
        else if (current_screen == 3) {
            toCardSetWindow()
        }
        else if (current_screen == 4) {
            toCardSetWindow()
        }
    }

    fun addSet() {
        val setName = inputNameEditText.text
        flashCardSetViewModel.addSet(setName.toString(), FlashCardSet())
        toMainWindow()
        inputNameEditText.setText("")
        updateMainWindow()
    }

    fun addCard() {
        val front = inputFrontEditText.text
        val back = inputBackEditText.text
        flashCardSetViewModel.addCardToSet(set_index, front.toString(), back.toString())
        toCardSetWindow()
        inputFrontEditText.setText("")
        inputBackEditText.setText("")
        updateSetWindow(set_index)
    }

    fun nextCard() {
        if (card_index == flashCardSetViewModel.sizeOfSet(set_index) - 1)
            card_index = 0
        else
            card_index += 1 % flashCardSetViewModel.sizeOfSet(set_index)
        updateFlashCardWindow()
    }

    fun prevCard() {
        if (card_index == 0)
            card_index = flashCardSetViewModel.sizeOfSet(set_index) - 1
        else
            card_index -= 1
        updateFlashCardWindow()
    }

    fun flipCard() {
        showing_front = !showing_front
        updateFlashCardWindow()
    }

    fun updateMainWindow() {
        if (flashCardSetViewModel.size() != 0)
            emptySetTextView.setText(R.string.card_set_hint_string);

        val listItems = flashCardSetViewModel.getNames()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems)

        cardSetListView.adapter = adapter
    }

    fun updateSetWindow(index: Int) {
        val listItems = flashCardSetViewModel.getFronts(index)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems)

        cardListView.adapter = adapter

        if (flashCardSetViewModel.sizeOfSet(set_index) == 0) {
            practiceButton.visibility = View.INVISIBLE
            emptySetTextView.setText(R.string.empty_card_set_string)
        }
        else{
            practiceButton.visibility = View.VISIBLE
            emptyCardSetTextView.setText(R.string.practice_hint_string)
        }
    }

    fun updateFlashCardWindow() {
        if (showing_front) {
            flashCardTextView.setText(flashCardSetViewModel.getFrontAt(set_index, card_index))
        }
        else {
            flashCardTextView.setText(flashCardSetViewModel.getBackAt(set_index, card_index))
        }
    }

//    override fun onSaveInstanceState(savedInstanceState: Bundle) {
//        super.onSaveInstanceState(savedInstanceState)
//        Log.i(TAG, "onSaveInstanceState")
//        savedInstanceState.putInt(KEY_INDEX,  quizViewModel.currentIndex)
//    }
}