package com.example.ticketbookingapp.Activities.TicketDetail

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.ticketbookingapp.Activities.Dashboard.DashboardActivity
import com.example.ticketbookingapp.Activities.SeatSelect.TicketDetailHeader
import com.example.ticketbookingapp.Activities.Splash.GradientButton
import com.example.ticketbookingapp.Domain.BookingModel
import com.example.ticketbookingapp.Domain.FlightModel
import com.example.ticketbookingapp.Domain.UserModel
import com.example.ticketbookingapp.R
import com.example.ticketbookingapp.Repository.MainRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TicketDetailScreen(
    flight: FlightModel,
    selectedSeats: String,
    totalPrice: Double,
    user: UserModel,
    onBackClick: () -> Unit,
    onDownloadTicketClick: () -> Unit,
    isHistoryView: Boolean = false
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val repository = MainRepository()
    val currentTime = SimpleDateFormat("HH:mm - dd/MM/yyyy", Locale.getDefault()).format(Date())

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.lightGreyWhite)) // Nền xám trắng
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .background(colorResource(R.color.lightGreyWhite)) // Nền xám trắng
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorResource(R.color.lightGreyWhite)) // Nền xám trắng
            ) {
                val (topSection, ticketDetail) = createRefs()

                TicketDetailHeader(
                    onBackClick = onBackClick,
                    modifier = Modifier.constrainAs(topSection) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                )

                TicketDetailContent(
                    flight = flight,
                    selectedSeats = selectedSeats,
                    totalPrice = totalPrice,
                    bookingTime = if (isHistoryView) flight.bookingTime else currentTime,
                    modifier = Modifier.constrainAs(ticketDetail) {
                        top.linkTo(parent.top, margin = 110.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                )
            }

            // Chỉ hiển thị nút Booking Tickets nếu không phải chế độ lịch sử
            if (!isHistoryView) {
                GradientButton(
                    onClick = {
                        coroutineScope.launch {
                            try {
                                val flightId = flight.FlightId
                                if (flightId.isEmpty()) {
                                    Toast.makeText(context, "Lỗi: Mã chuyến bay không hợp lệ", Toast.LENGTH_LONG).show()
                                    return@launch
                                }

                                val bookingExists = repository.checkBookingExists(
                                    username = user.username,
                                    flightId = flightId,
                                    seats = selectedSeats
                                )
                                if (bookingExists) {
                                    Toast.makeText(context, "Vé này đã được đặt trước đó!", Toast.LENGTH_LONG).show()
                                    return@launch
                                }

                                val booking = BookingModel(
                                    flightId = flightId,
                                    date = flight.Date,
                                    from = flight.From,
                                    to = flight.To,
                                    typeClass = flight.TypeClass,
                                    seats = selectedSeats,
                                    price = totalPrice,
                                    bookingDate = currentTime,
                                    airlineName = flight.AirlineName,
                                    airlineLogo = flight.AirlineLogo,
                                    arriveTime = flight.ArriveTime,
                                    fromShort = flight.FromShort,
                                    toShort = flight.ToShort,
                                    time = flight.Time,
                                    classSeat = flight.ClassSeat
                                )

                                val saveResult = repository.saveBooking(user.username, booking)
                                if (saveResult.isFailure) {
                                    Toast.makeText(context, "Lỗi khi lưu vé: ${saveResult.exceptionOrNull()?.message}", Toast.LENGTH_LONG).show()
                                    return@launch
                                }

                                val updateSeatsResult = repository.updateFlightReservedSeats(flightId, selectedSeats)
                                if (updateSeatsResult.isFailure) {
                                    Toast.makeText(context, "Lỗi khi cập nhật ghế: ${updateSeatsResult.exceptionOrNull()?.message}", Toast.LENGTH_LONG).show()
                                    return@launch
                                }

                                Toast.makeText(context, "Đặt vé thành công!", Toast.LENGTH_LONG).show()
                                context.startActivity(Intent(context, DashboardActivity::class.java).apply {
                                    putExtra("user", user)
                                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                })
                                onDownloadTicketClick()
                            } catch (e: Exception) {
                                Toast.makeText(context, "Lỗi khi đặt vé: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                        }
                    },
                    text = "Đặt Vé",
                    gradientColors = listOf(
                        colorResource(R.color.lightBlue),
                        colorResource(R.color.mediumBlue)
                    )
                )
            }
        }
    }
}