package com.forasoft.datastoreexample

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.migrations.SharedPreferencesMigration
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.preferencesOf
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.forasoft.datastoreexample.Game.*
import com.forasoft.datastoreexample.GamesDataStoreManager.Companion.GAME_KEY
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch

val Context.gamesDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "games_data_store",
    corruptionHandler = ReplaceFileCorruptionHandler {
        preferencesOf(GAME_KEY to TENNIS.text)
    }
)

class MainActivity : AppCompatActivity() {
    private lateinit var gameDataStoreManager: GamesDataStoreManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        gameDataStoreManager = GamesDataStoreManager(applicationContext)
        initViews()
        observeCurrentGame()
    }

    private fun initViews() {
        cricketButton.setOnClickListener { onButtonClick(CRICKET) }
        tennisButton.setOnClickListener { onButtonClick(TENNIS) }
        rugbyButton.setOnClickListener { onButtonClick(RUGBY) }
        volleyballButton.setOnClickListener { onButtonClick(VOLLEYBALL) }
    }

    private fun onButtonClick(game: Game) {
        lifecycleScope.launch {
            gameDataStoreManager.saveValue(game.text)
        }
    }

    private fun observeCurrentGame() {
        gameDataStoreManager.valueFlow.asLiveData().observe(this) { value ->
            val iconSource = getGameIconSource(value)
            iconView.setImageResource(iconSource)
        }
    }

    private fun getGameIconSource(gameName: String): Int {
        return when(gameName) {
            CRICKET.text -> R.drawable.cricket_icon
            TENNIS.text -> R.drawable.tennis_icon
            RUGBY.text -> R.drawable.rugby_icon
            VOLLEYBALL.text -> R.drawable.volleyball_icon
            else -> R.drawable.default_game_icon
        }
    }
}