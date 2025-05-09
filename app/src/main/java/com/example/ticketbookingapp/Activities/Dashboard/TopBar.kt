package com.example.ticketbookingapp.Activities.Dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.ticketbookingapp.Domain.UserModel
import com.example.ticketbookingapp.R
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun TopBar(
    user: UserModel,
    title: String = stringResource(R.string.dashboard_title)
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth() // Full chiều rộng
            .wrapContentHeight()
            .background(
                color = colorResource(R.color.lightBlue), // Nền lightBlue
                shape = RoundedCornerShape(16.dp) // Bo tròn 4 góc
            )
            .padding(horizontal = 32.dp, vertical = 16.dp) // Padding cân đối
    ) {
        val (world, name, profile, notification, titleText) = createRefs()
        Image(
            painter = painterResource(R.drawable.world),
            contentDescription = "Biểu tượng thế giới",
            modifier = Modifier
                .clickable { }
                .constrainAs(world) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                }
        )

        Image(
            painter = painterResource(R.drawable.profile),
            contentDescription = "Biểu tượng hồ sơ",
            modifier = Modifier
                .constrainAs(profile) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                }
        )

        Image(
            painter = painterResource(R.drawable.bell_icon),
            contentDescription = "Biểu tượng thông báo",
            modifier = Modifier
                .constrainAs(notification) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                }
        )

        Text(
            text = user.fullName,
            color = colorResource(R.color.darkBlue),
            fontWeight = FontWeight.Bold,
            fontSize = 35.sp,
            modifier = Modifier.constrainAs(name) {
                top.linkTo(parent.top)
                start.linkTo(profile.end, margin = 8.dp)
                bottom.linkTo(parent.bottom)
            }
        )

        Text(
            text = title,
            color = colorResource(R.color.darkBlue),
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp,
            modifier = Modifier
                .constrainAs(titleText) {
                    top.linkTo(profile.bottom, margin = 8.dp)
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom)
                }
        )
    }
}

@Preview
@Composable
fun TopBarPreview() {
    val dummyUser = UserModel(
        username = "dante_123",
        password = "UserTBA@123_",
        role = "user",
        email = "dante_123@gmail.com",
        fullName = "Dante",
        dateOfBirth = "19/09/2005",
        gender = "Nam",
        phoneNumber = "0987654321"
    )
    TopBar(user = dummyUser, title = "Trang chính")
}