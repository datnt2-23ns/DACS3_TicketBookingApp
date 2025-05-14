package com.example.ticketbookingapp.Activities.TopDestinations

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ticketbookingapp.Activities.Dashboard.MyBottomBar
import com.example.ticketbookingapp.Activities.Dashboard.TopBar
import com.example.ticketbookingapp.Domain.LocationModel
import com.example.ticketbookingapp.Domain.UserModel
import com.example.ticketbookingapp.R
import com.example.ticketbookingapp.ViewModel.MainViewModel

@Composable
fun TopDestinationsScreen(user: UserModel) {
    val locations = remember { mutableStateListOf<LocationModel>() }
    val viewModel = MainViewModel()
    var showLocationLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val lifecycleOwner = LocalLifecycleOwner.current
    val TAG = "TopDestinationsScreen"

    // Map mô tả và hình ảnh fix cứng
    val destinationDetails = mapOf(
        "Tân Sơn Nhất" to Pair(
            "Sân bay lớn nhất Việt Nam, nằm tại TP. Hồ Chí Minh, trung tâm kinh tế sôi động với các điểm tham quan như Dinh Độc Lập, chợ Bến Thành.",
            R.drawable.tansonnhat
        ),
        "Nội Bài" to Pair(
            "Sân bay quốc tế chính của miền Bắc, gần Hà Nội - thủ đô văn hóa với Hồ Gươm, Văn Miếu và ẩm thực phở nổi tiếng.",
            R.drawable.hoguom
        ),
        "Đà Nẵng" to Pair(
            "Thành phố biển miền Trung với cầu Rồng, Bà Nà Hills và bãi biển Mỹ Khê tuyệt đẹp, lý tưởng cho du lịch nghỉ dưỡng.",
            R.drawable.caurong
        ),
        "Phú Quốc" to Pair(
            "Đảo ngọc với những bãi biển trong xanh, khu nghỉ dưỡng sang trọng và chợ đêm sầm uất, thiên đường du lịch biển.",
            R.drawable.daongocphuquoc
        ),
        "Cát Bi" to Pair(
            "Sân bay tại Hải Phòng, cửa ngõ đến vịnh Hạ Long - di sản thế giới, và ẩm thực đặc sắc như bánh đa cua.",
            R.drawable.vinhhalong
        ),
        "Cà Mau" to Pair(
            "Điểm cực Nam của Việt Nam, nổi tiếng với rừng ngập mặn U Minh Hạ và văn hóa sông nước miền Tây.",
            R.drawable.runguminh
        ),
        "Vân Đồn" to Pair(
            "Sân bay mới tại Quảng Ninh, gần vịnh Hạ Long và các đảo Cô Tô, Quan Lạn, lý tưởng cho du lịch khám phá.",
            R.drawable.vinhhalong
        ),
        "Đồng Hới" to Pair(
            "Cửa ngõ đến Quảng Bình, nơi có động Phong Nha - Kẻ Bàng, di sản thiên nhiên thế giới với hệ thống hang động kỳ vĩ.",
            R.drawable.phongnhakebang
        ),
        "Thọ Xuân" to Pair(
            "Sân bay tại Thanh Hóa, gần thành nhà Hồ và bãi biển Sầm Sơn, điểm đến lý tưởng cho lịch sử và nghỉ dưỡng.",
            R.drawable.biensamson
        ),
        "Rạch Giá" to Pair(
            "Thành phố biển ở Kiên Giang, nổi tiếng với hải sản tươi ngon và là điểm khởi hành đến các đảo như Nam Du.",
            R.drawable.namdu
        ),
        "Điện Biên Phủ" to Pair(
            "Điểm đến lịch sử với chiến thắng Điện Biên Phủ, khám phá văn hóa dân tộc Thái và cảnh sắc núi rừng Tây Bắc.",
            R.drawable.dienbienphu
        )
    )

    LaunchedEffect(Unit) {
        try {
            viewModel.loadLocations().observe(lifecycleOwner) { result ->
                if (result != null) {
                    locations.clear()
                    locations.addAll(result)
                    showLocationLoading = false
                } else {
                    errorMessage = "Không tải được danh sách điểm đến"
                    showLocationLoading = false
                }
            }
        } catch (e: Exception) {
            errorMessage = "Lỗi tải danh sách điểm đến: ${e.message}"
            showLocationLoading = false
        }
    }

    Scaffold(
        bottomBar = { MyBottomBar(user = user, currentScreen = "TopTravelDestinations") },
        modifier = Modifier.background(colorResource(R.color.lightGreyWhite))
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(R.color.lightGreyWhite))
                .padding(paddingValues)
        ) {
            item {
                TopBar(user = user, title = "Điểm đến du lịch hàng đầu")
            }
            item {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 32.dp, vertical = 16.dp)
                        .background(Color.White, RoundedCornerShape(20.dp))
                        .fillMaxWidth()
                        .padding(vertical = 16.dp, horizontal = 24.dp)
                ) {
                    if (showLocationLoading) {
                        Text(
                            text = "Đang tải...",
                            color = Color.DarkGray,
                            fontSize = 16.sp
                        )
                    } else if (errorMessage != null) {
                        Text(
                            text = errorMessage!!,
                            color = Color.Red,
                            fontSize = 14.sp
                        )
                    } else if (locations.isEmpty()) {
                        Text(
                            text = "Không có điểm đến nào để hiển thị",
                            color = Color.DarkGray,
                            fontSize = 16.sp
                        )
                    } else {
                        locations.forEach { location ->
                            DestinationItem(location = location, destinationDetails = destinationDetails)
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DestinationItem(location: LocationModel, destinationDetails: Map<String, Pair<String, Int>>) {
    val (description, imageResId) = destinationDetails[location.Name] ?: Pair(
        "Không có mô tả",
        R.drawable.world
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp) // Chỉ padding, không background xám
    ) {
        // Hình ảnh
        Image(
            painter = painterResource(imageResId),
            contentDescription = "Hình ảnh ${location.Name}",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clip(RoundedCornerShape(10.dp))
        )
        Spacer(modifier = Modifier.height(8.dp))
        // Tiêu đề
        Text(
            text = location.Name,
            fontWeight = FontWeight.SemiBold,
            color = colorResource(R.color.darkBlue),
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        // Mô tả
        Text(
            text = description,
            color = Color.DarkGray, // Đổi sang DarkGray để dễ đọc trên nền trắng
            fontSize = 14.sp,
            maxLines = 3,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

//@Preview(showBackground = true)
//@Composable
//fun TopDestinationsScreenPreview() {
//    // Dữ liệu giả lập cho preview
//    val mockUser = UserModel(
//        username = "testuser",
//        email = "test@example.com"
//        // Thêm các thuộc tính khác của UserModel nếu cần
//    )
//    val mockLocations = listOf(
//        LocationModel(Id = 0, Name = "Tân Sơn Nhất"),
//        LocationModel(Id = 1, Name = "Nội Bài"),
//        LocationModel(Id = 2, Name = "Đà Nẵng")
//    )
//    val destinationDetails = mapOf(
//        "Tân Sơn Nhất" to Pair(
//            "Sân bay lớn nhất Việt Nam, nằm tại TP. Hồ Chí Minh, trung tâm kinh tế sôi động.",
//            R.drawable.tansonnhat
//        ),
//        "Nội Bài" to Pair(
//            "Sân bay quốc tế chính của miền Bắc, gần Hà Nội - thủ đô văn hóa.",
//            R.drawable.hoguom
//        ),
//        "Đà Nẵng" to Pair(
//            "Thành phố biển miền Trung với cầu Rồng, Bà Nà Hills và bãi biển Mỹ Khê.",
//            R.drawable.caurong
//        )
//    )
//
//    Scaffold(
//        bottomBar = { MyBottomBar(user = mockUser, currentScreen = "TopTravelDestinations") },
//        modifier = Modifier.background(colorResource(R.color.lightGreyWhite))
//    ) { paddingValues ->
//        LazyColumn(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(colorResource(R.color.lightGreyWhite))
//                .padding(paddingValues)
//        ) {
//            item {
//                TopBar(user = mockUser, title = "Những điểm đến du lịch hàng đầu")
//            }
//            item {
//                Column(
//                    modifier = Modifier
//                        .padding(horizontal = 32.dp, vertical = 16.dp)
//                        .background(Color.White, RoundedCornerShape(20.dp))
//                        .fillMaxWidth()
//                        .padding(vertical = 16.dp, horizontal = 24.dp)
//                ) {
//                    mockLocations.forEach { location ->
//                        DestinationItem(location = location, destinationDetails = destinationDetails)
//                        Spacer(modifier = Modifier.height(8.dp))
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun DestinationItemPreview() {
//    val mockLocation = LocationModel(Id = 0, Name = "Tân Sơn Nhất")
//    val mockDestinationDetails = mapOf(
//        "Tân Sơn Nhất" to Pair(
//            "Sân bay lớn nhất Việt Nam, nằm tại TP. Hồ Chí Minh, trung tâm kinh tế sôi động.",
//            R.drawable.tansonnhat
//        )
//    )
//
//    DestinationItem(location = mockLocation, destinationDetails = mockDestinationDetails)
//}