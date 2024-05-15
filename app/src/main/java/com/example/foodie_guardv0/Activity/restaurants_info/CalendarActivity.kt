package com.example.foodie_guardv0.Activity.restaurants_info

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Bundle
import android.text.TextPaint
import android.text.style.CharacterStyle
import android.text.style.LineBackgroundSpan
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.foodie_guard0.R
import com.example.foodie_guardv0.dataclass.Reservation
import com.example.foodie_guardv0.retrofitt.ApiService
import com.example.foodie_guardv0.retrofitt.RetrofitClient
import com.example.foodie_guardv0.sharedPreferences.UserSharedPreferences
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar
import java.util.Date
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class CalendarActivity : AppCompatActivity() {
    private val service = RetrofitClient.retrofit.create(ApiService::class.java)
    private lateinit var selectedDate: Date
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)
        val photo = intent.getStringExtra("photo")
        val id = intent.getIntExtra("id", 0)
        val ImageRestaurant = findViewById<ImageView>(R.id.imageRestaurant)
        Glide.with(ImageRestaurant.context).load(photo).into(ImageRestaurant)
        val BackButton = findViewById<ImageButton>(R.id.buttonReturn)
        val reservationButton = findViewById<Button>(R.id.dateselected)
        val userSharedPreferences = UserSharedPreferences(this)
        BackButton.setOnClickListener() {
            finish()
        }
        reservationButton.setOnClickListener {
            if (::selectedDate.isInitialized) {
                Log.e("fecha seleccionada",selectedDate.toString())
                Toast.makeText(this, "Fecha seleccionada para reserva: $selectedDate", Toast.LENGTH_SHORT).show()
                sendConfirmationEmail()
            } else {
                Toast.makeText(this, "Por favor, selecciona una fecha primero.", Toast.LENGTH_SHORT).show()
            }
        }


        lifecycleScope.launch {
            val reservations = getReservations(id)
            val reservationDates = getReservationDates(reservations)
            updateCalendar(reservationDates)
            setupDateClickListener(reservationDates)
            Log.e("reservas", reservationDates.toString())
            Log.e("reservas", reservations.toString())
        }
        val calendarView = findViewById<MaterialCalendarView>(R.id.calendar)
        calendarView.setHeaderTextAppearance(R.style.CalendarWidgetHeader);
        calendarView.setWeekDayTextAppearance(R.style.CalendarWidgetHeader)
        calendarView.state().edit()
            .setFirstDayOfWeek(Calendar.MONDAY)
            .commit()
    }

    private suspend fun getReservations(id: Int): List<Reservation> {
        return suspendCoroutine { continuation ->
            var call = service.getReservationsByIdRes(id)

            call.enqueue(object : Callback<List<Reservation>> {
                override fun onResponse(
                    call: Call<List<Reservation>>,
                    response: Response<List<Reservation>>
                ) {
                    if (response.isSuccessful) {
                        Log.e("Respuesta existosa", "todo fresco")
                        val respuesta = response.body()
                        Log.e("respuesta", response.body().toString())
                        continuation.resume(respuesta!!)
                    } else {
                        continuation.resumeWithException(Exception("Error de la API"))
                        Log.e("Resultado", "error Api")
                    }
                }

                override fun onFailure(call: Call<List<Reservation>>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }
    private fun getReservationDates(reservations: List<Reservation>): List<Date> {
        return reservations.map { it.date }
    }

    private fun setupDateClickListener(dates: List<Date>) {
        val calendarView = findViewById<MaterialCalendarView>(R.id.calendar)
        val reservedDays = dates.map { CalendarDay.from(it) }.toSet()

        calendarView.setOnDateChangedListener { widget, date, selected ->
            if (date in reservedDays) {
                selectedDate = date.date
                //onDateSelected(date.date)
            } else {
                Toast.makeText(this, "Esta fecha no está disponible para reserva.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onDateSelected(date: Date) {
        Toast.makeText(this, "Fecha reservada seleccionada: $date", Toast.LENGTH_SHORT).show()
    }

    private fun updateCalendar(dates: List<Date>) {
        val calendarView = findViewById<MaterialCalendarView>(R.id.calendar)
        val calendarDays = dates.map { CalendarDay.from(it) }
        val radius = 50f
        val decorator = ReservationDecorator(calendarDays, this, radius)
        calendarView.addDecorator(decorator)
        val clickableDatesDecorator = ClicableDayDecorator(calendarDays, this)
        calendarView.addDecorator(clickableDatesDecorator)
        val disabledDaysDecorator = DisableDaysDecorator(calendarDays, this)
        calendarView.addDecorator(disabledDaysDecorator)
    }



    class ReservationDecorator(private val dates: List<CalendarDay>, private val context: Context, private val radius: Float) : DayViewDecorator {
        override fun shouldDecorate(day: CalendarDay): Boolean {
            return dates.contains(day)
        }

        override fun decorate(view: DayViewFacade) {
            view.addSpan(CustomSpan(ContextCompat.getColor(context, R.color.mostaza), Color.WHITE, radius))
        }
    }



    class CustomSpan(private val backgroundColor: Int, private val textColor: Int, private val radius: Float) : LineBackgroundSpan {
        override fun drawBackground(
            canvas: Canvas,
            paint: Paint,
            left: Int,
            right: Int,
            top: Int,
            baseline: Int,
            bottom: Int,
            text: CharSequence,
            start: Int,
            end: Int,
            lineNumber: Int
        ) {
            val oldColor = paint.color
            if (backgroundColor != 0) {
                paint.color = backgroundColor
                canvas.drawCircle((right - left) / 2.toFloat(), (bottom - top) / 2.toFloat(), radius, paint)
            }
            paint.color = oldColor
        }
    }

    class DisableDaysDecorator(private val dates: List<CalendarDay>, private val context: Context) : DayViewDecorator {
        override fun shouldDecorate(day: CalendarDay): Boolean {
            return day !in dates
        }

        override fun decorate(view: DayViewFacade) {
            view.setDaysDisabled(true)
        }
    }

    class ClicableDayDecorator(private val dates: List<CalendarDay>, private val context: Context) : DayViewDecorator {

        override fun shouldDecorate(day: CalendarDay): Boolean {
            return day in dates
        }

        override fun decorate(view: DayViewFacade) {
            view.addSpan(CustomStyleSpan(context))
        }
    }

    class CustomStyleSpan(private val context: Context) : CharacterStyle() {

        override fun updateDrawState(tp: TextPaint) {
            tp.typeface = Typeface.create("sans-serif", Typeface.BOLD)
            tp.color = ContextCompat.getColor(context, R.color.black)
        }
    }

   private fun sendConfirmationEmail() {
       val userSharedPreferences = UserSharedPreferences(this)
       val actualUser = userSharedPreferences.getUser()!!.user
       val name = actualUser.name
        val message = "El usuario $name ha solicitado una reserva para el día $selectedDate"
        val call = RetrofitClient.apiService.sendConfirmationEmailToRestaurant(message)
        call.enqueue(object : retrofit2.Callback<Void> {
            override fun onResponse(call: Call<Void>, response: retrofit2.Response<Void>) {
                if (response.isSuccessful) {
                    println("Solicitud POST exitosa")
                    successToast()
                    finish()
                } else{
                    println("Me ise popo")
                }
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                // Se produjo un error de red u otro tipo de error
                println("Error en la solicitud POST: ${t.message}")
                failureToast()
            }
        })

    }
    private fun successToast() {
        val text = "Se ha reservado con éxito"
        val duration = Toast.LENGTH_SHORT
        val context = (this)
        val toast = Toast.makeText(context, text, duration)
        toast.show()
    }

    private fun failureToast() {
        val text = "No se ha podido reservar"
        val duration = Toast.LENGTH_SHORT
        val context = (this)
        val toast = Toast.makeText(context, text, duration)
        toast.show()
    }

}