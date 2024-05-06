package com.example.foodie_guardv0.Activity.main_fragments

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodie_guard0.R
import com.example.foodie_guardv0.adapters.dish.DishAdapter
import com.example.foodie_guardv0.dataclass.Dish
import com.example.foodie_guardv0.retrofitt.ApiService
import com.example.foodie_guardv0.retrofitt.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * A simple [Fragment] subclass.
 * Use the [search_Fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class search_Fragment : Fragment() {
    private var isGlutenSelected=false
    private var isSoySelected=false
    private var isCrustaceanSelected=false
    private var isShellfishSelected=false
    private var isSulfitesSelected=false
    private var isFishSelected=false
    private var isMilkSelected=false
    private var isMustardSelected=false
    private var isCelerySelected=false
    private var isPeanutsSelected=false
    private var isEggSelected=false
    private var isNutsSelected=false
    private var isLupinsSelected=false
    private var isSesameSelected=false


    private val service = RetrofitClient.retrofit.create(ApiService::class.java)
    companion object {
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search_, container, false)
        setupImageButtons(view)
        val myButton = view.findViewById<Button>(R.id.myButton)
        val myTextView = view.findViewById<TextView>(R.id.myTextView)

        myButton.setOnClickListener {
            if (myTextView.visibility == View.GONE) {
                myTextView.visibility = View.VISIBLE
            } else {
                myTextView.visibility = View.GONE
            }
        }

        GlobalScope.launch(Dispatchers.Main) {
            try {
                initRecyclerRestaurant(dishes())
            } catch (e: Exception) {
                Log.e("Resultado", "Error" + e.message)
            }
        }

        return view
    }

    private fun initRecyclerRestaurant(dishes: List<Dish>) {
        val recyclerView = view?.findViewById<RecyclerView>(R.id.recyclerDishes)
        recyclerView?.layoutManager = LinearLayoutManager(activity)
        recyclerView?.adapter = DishAdapter(dishes)
    }

    fun generateConcatenatedString(): String {
        val selectedItems = listOf(
            isGlutenSelected, isSoySelected, isCrustaceanSelected, isShellfishSelected,
            isSulfitesSelected, isFishSelected, isMilkSelected, isMustardSelected,
            isCelerySelected, isPeanutsSelected, isEggSelected, isNutsSelected,
            isLupinsSelected, isSesameSelected
        )

        return selectedItems
            .map { if (it) "1" else "0" }
            .joinToString(separator = "")
    }

    private suspend fun dishes(): List<Dish> {
        val concatenatedResult = generateConcatenatedString()
        return suspendCoroutine { continuation ->
            var call = service.getDishesFiltered(concatenatedResult)

            call.enqueue(object : Callback<List<Dish>> {
                override fun onResponse(
                    call: Call<List<Dish>>,
                    response: Response<List<Dish>>
                ) {
                    if (response.isSuccessful) {
                        val respuesta = response.body()
                        continuation.resume(respuesta!!)
                    } else {
                        continuation.resumeWithException(Exception("Error de la API"))
                        Log.e("Resultado", "error Api")
                    }
                }

                override fun onFailure(call: Call<List<Dish>>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }

    private fun onImageButtonClick(view: View) {
        when (view.id) {
            R.id.glutenImageButton -> isGlutenSelected = !isGlutenSelected
            R.id.soyImageButton -> isSoySelected = !isSoySelected
            R.id.milkImageButton -> isMilkSelected = !isMilkSelected
            R.id.eggImageButton -> isEggSelected = !isEggSelected
            R.id.mustardImageButton -> isMustardSelected = !isMustardSelected
            R.id.nutsImageButton -> isNutsSelected = !isNutsSelected
            R.id.sulfitesImageButton -> isSulfitesSelected = !isSulfitesSelected
            R.id.celeryImageButton -> isCelerySelected = !isCelerySelected
            R.id.fishImageButton -> isFishSelected = !isFishSelected
            R.id.crustaceanImageButton -> isCrustaceanSelected = !isCrustaceanSelected
            R.id.shellfishImageButton -> isShellfishSelected = !isShellfishSelected
            R.id.peanutsImageButton -> isPeanutsSelected = !isPeanutsSelected
            R.id.sesameImageButton -> isSesameSelected = !isSesameSelected
            R.id.lupinsImageButton -> isLupinsSelected = !isLupinsSelected
        }

        updateUI(view)
        GlobalScope.launch(Dispatchers.Main) {
            try {
                initRecyclerRestaurant(dishes())
            } catch (e: Exception) {
                Log.e("Resultado", "Error" + e.message)
            }
        }
    }

    private fun updateUI(view: View) {
        updateButtonState(view.findViewById(R.id.glutenImageButton), isGlutenSelected)
        updateButtonState(view.findViewById(R.id.soyImageButton), isSoySelected)
        updateButtonState(view.findViewById(R.id.milkImageButton), isMilkSelected)
        updateButtonState(view.findViewById(R.id.eggImageButton), isEggSelected)
        updateButtonState(view.findViewById(R.id.mustardImageButton), isMustardSelected)
        updateButtonState(view.findViewById(R.id.nutsImageButton), isNutsSelected)
        updateButtonState(view.findViewById(R.id.sulfitesImageButton), isSulfitesSelected)
        updateButtonState(view.findViewById(R.id.celeryImageButton), isCelerySelected)
        updateButtonState(view.findViewById(R.id.fishImageButton), isFishSelected)
        updateButtonState(view.findViewById(R.id.crustaceanImageButton), isCrustaceanSelected)
        updateButtonState(view.findViewById(R.id.shellfishImageButton), isShellfishSelected)
        updateButtonState(view.findViewById(R.id.peanutsImageButton), isPeanutsSelected)
        updateButtonState(view.findViewById(R.id.sesameImageButton), isSesameSelected)
        updateButtonState(view.findViewById(R.id.lupinsImageButton), isLupinsSelected)
    }

    private fun updateButtonState(button: ImageButton?, isSelected: Boolean) {
        button?.let { btn ->
            val resourceName = requireContext().resources.getResourceEntryName(btn.id)
            val selectedResourceName = resourceName + "Selected"

            val packageContext = requireContext()
            val packageName = packageContext.packageName

            // Obtener el ID numérico del recurso "selectedResourceName"
            val selectedResourceId = packageContext.resources.getIdentifier(selectedResourceName, "id", packageName)

            if (selectedResourceId != 0) {
                // El ID numérico se encontró correctamente, ahora obtenemos la referencia al ImageView
                val selectedImageView = requireView().findViewById<ImageView>(selectedResourceId)

                // Modificar la visibilidad del ImageView según el estado isSelected
                if (isSelected) {
                    selectedImageView.isVisible = true
                } else {
                    selectedImageView.isGone = true
                }
            } else {
                // El ID numérico no se encontró, muestra un mensaje de error
                Log.e("updateButtonState", "ID numérico no encontrado para $selectedResourceName")
            }
        }
    }

    private fun setupImageButtons(view: View) {
        val glutenButton = view.findViewById<ImageButton>(R.id.glutenImageButton)
        val soyButton = view.findViewById<ImageButton>(R.id.soyImageButton)
        val milkButton = view.findViewById<ImageButton>(R.id.milkImageButton)
        val eggButton = view.findViewById<ImageButton>(R.id.eggImageButton)
        val mustardButton = view.findViewById<ImageButton>(R.id.mustardImageButton)
        val nutsButton = view.findViewById<ImageButton>(R.id.nutsImageButton)
        val sulfitesButton = view.findViewById<ImageButton>(R.id.sulfitesImageButton)
        val celeryButton = view.findViewById<ImageButton>(R.id.celeryImageButton)
        val fishButton = view.findViewById<ImageButton>(R.id.fishImageButton)
        val crustaceanButton = view.findViewById<ImageButton>(R.id.crustaceanImageButton)
        val shellfishButton = view.findViewById<ImageButton>(R.id.shellfishImageButton)
        val peanutsButton = view.findViewById<ImageButton>(R.id.peanutsImageButton)
        val sesameButton = view.findViewById<ImageButton>(R.id.sesameImageButton)
        val lupinsButton = view.findViewById<ImageButton>(R.id.lupinsImageButton)

        glutenButton.setOnClickListener { onImageButtonClick(it) }
        soyButton.setOnClickListener { onImageButtonClick(it) }
        milkButton.setOnClickListener { onImageButtonClick(it) }
        eggButton.setOnClickListener { onImageButtonClick(it) }
        mustardButton.setOnClickListener { onImageButtonClick(it) }
        nutsButton.setOnClickListener { onImageButtonClick(it) }
        sulfitesButton.setOnClickListener { onImageButtonClick(it) }
        celeryButton.setOnClickListener { onImageButtonClick(it) }
        fishButton.setOnClickListener { onImageButtonClick(it) }
        crustaceanButton.setOnClickListener { onImageButtonClick(it) }
        shellfishButton.setOnClickListener { onImageButtonClick(it) }
        peanutsButton.setOnClickListener { onImageButtonClick(it) }
        sesameButton.setOnClickListener { onImageButtonClick(it) }
        lupinsButton.setOnClickListener { onImageButtonClick(it) }
    }

    fun getResourceNameFromId(context: Context, id: Int): String? {
        try {
            return context.resources.getResourceName(id)
        } catch (e: Resources.NotFoundException) {
            // El recurso no fue encontrado
            e.printStackTrace()
        }
        return null
    }

}