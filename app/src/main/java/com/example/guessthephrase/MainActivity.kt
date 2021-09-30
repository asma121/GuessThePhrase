package com.example.guessthephrase

import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    lateinit var textView: TextView
    lateinit var textView2: TextView
    lateinit var textView3: TextView
    lateinit var editText: EditText
    lateinit var button: Button
    lateinit var conly:ConstraintLayout
    lateinit var myRv:RecyclerView
    val phrase="done is better than perfect"
    val guess=ArrayList<String>()
    var guessphrase:String=""
    var count=10
    var count1=10
    var answer=""
    var score=0
    var highscore=0
    private lateinit var sharedPreferences: SharedPreferences
    private val myAnswerDictionary = mutableMapOf<Int, Char>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = this.getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        highscore = sharedPreferences.getInt("HighScore", 0)

        for(i in phrase.indices){
            if (phrase[i] == ' '){
                guessphrase +=" "
                myAnswerDictionary[i]=' '
            }else{
                guessphrase +="*"
                myAnswerDictionary[i]='*'
            }
        }

        textView=findViewById(R.id.textView)
        textView2=findViewById(R.id.textView3)
        textView3=findViewById(R.id.textView2)
        editText=findViewById(R.id.editText)
        button=findViewById(R.id.button)
        conly=findViewById(R.id.conlay)


         textView.text="guess the phrase:$guessphrase"
        textView3.text="high score: after $highscore guesses"


        myRv=findViewById<RecyclerView>(R.id.rvMain)
        myRv.adapter=RecyclerViewAdapter(guess)
        myRv.layoutManager=LinearLayoutManager(this)

        button.setOnClickListener{
           checkphrase()
        }

        }
    fun checkphrase(){
        var input = editText.text.toString()
        editText.hint="guess phrase"
        if (input.isNotEmpty()){
            if ( count>0 ){
                if (input==phrase){
                    disable()
                    updatescore(count)
                    alertdialog("you got it \n would you play again?")
                }else if (input!=phrase){
                    count--
                    guess.add("wrong guess :$input")
                    guess.add("$count guessing remaining")
                }

            }else {
                editText.hint="guess letter"
                if(count1>0){
                    checkletter(input)
                    count1--
                }
                else if (count1==0){
                    disable()
                    alertdialog("you lose \n play again?")
                }
            }
            editText.text.clear()
            editText.clearFocus()
            myRv.adapter?.notifyDataSetChanged()
        }else{
            Snackbar.make(conly,"guess the phrase",Snackbar.LENGTH_LONG).show()
        }
    }

    fun checkletter(input:String){
        var found=0
        var input=editText.text.toString()
        if(input!=phrase){
                if (input.length == 1){
                    for (i in phrase.indices){
                        if (input[0]==phrase[i]){
                            myAnswerDictionary[i]=input[0]
                            found++
                        }
                    }
                    answer="${myAnswerDictionary.values}"
                    textView.text="guess the phrase: $answer"
                    textView2.text=" guessed letter : $input"
                    guess.add("found $found $input ")
                    guess.add("guessing remaining $count1")
                }else{
                    Snackbar.make(conly,"plase Enter one letter",Snackbar.LENGTH_LONG).show()
                }
        }else{
            updatescore(count1)
            alertdialog("you got it \n play again?")
        }

    }

    fun updatescore(c:Int){
      score=10-c
        if (score > highscore){
            highscore=score
            with(sharedPreferences.edit()) {
                putInt("HighScore", highscore)
                apply()
            }
        }
    }

    fun disable(){
        button.isEnabled=false
        button.isClickable=false
        editText.isEnabled=false
        editText.isClickable=false
    }

    fun alertdialog(title:String){
        val builder =AlertDialog.Builder(this)
        builder.setMessage(title)
        builder.setCancelable(false)
        builder.setPositiveButton("yes", DialogInterface.OnClickListener {
            dialog,id-> this.recreate()
        })
        builder.setNegativeButton("no",DialogInterface.OnClickListener{
            dialog,id-> dialog.cancel()
        })
        val alert =builder.create()
        alert.setTitle("game over")
        alert.show()

    }

}

