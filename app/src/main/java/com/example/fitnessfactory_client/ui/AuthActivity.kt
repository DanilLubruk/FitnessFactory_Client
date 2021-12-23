package com.example.fitnessfactory_client.ui

import android.graphics.drawable.shapes.Shape
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.lifecycle.ViewModelProvider
import com.example.fitnessfactory_client.R

class AuthActivity : AppCompatActivity() {

    private lateinit var viewModel: AuthActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, AuthActivityViewModelFactory()).get(AuthActivityViewModel::class.java)

        setContent {

        }
    }

    /*@ExperimentalMaterialApi
    @Composable
    fun SignInButton(
        text: String,
        loadingTest: String = "Signing in...",
        icon: Painter,
        isLoading: Boolean = false,
        shape: Shape = Shapes.medium,
        orderColor: Color = Color.LightGray,

        )*/
}