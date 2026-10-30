package com.example.quicall.screens.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.quicall.R
import com.example.quicall.ui.theme.QuiCallTheme

@Composable
fun ContactCard(
    modifier: Modifier = Modifier,
    name: String,
    lastName: String,
    phoneNumber: String,
    imageResourceId: Int?,
    onRemoveContact: () -> Unit = {},
    onCallAttempt: (String) -> Unit = {},
    phoneNumberToCall: (String) -> Unit = {}
) {
    ElevatedCard(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium
    ) {
        ListItem(
            modifier = Modifier
                .fillMaxSize()
                .clip(MaterialTheme.shapes.medium),
            colors = ListItemDefaults.colors(containerColor = Color(0xFF00FF88)),
            leadingContent = {
                Image(
                    modifier =
                        if (imageResourceId != null) Modifier
                            .size(dimensionResource(R.dimen.profile_image_size))
                            .clip(shape = CircleShape)
                            else Modifier.size(dimensionResource(R.dimen.profile_image_size)),
                    painter = painterResource(imageResourceId ?: R.drawable.ic_person),
                    contentScale = ContentScale.Crop,
                    contentDescription = null
                )
            },
            headlineContent = {
                Column(
//                    modifier = Modifier.verticalScroll(rememberScrollState())
                    verticalArrangement = Arrangement.Center
                ){
                    Text(
                        text = name,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = lastName,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                }
            },
            supportingContent = {
                Text(
                    text = phoneNumber,
                    maxLines = 1
                )
            },
            trailingContent = {
                Column(
                    modifier = Modifier,
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.btn_dial_call_padding))
                ) {
                    IconButton(
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .size(dimensionResource(R.dimen.btn_size)),
                        onClick = {
                            phoneNumberToCall(phoneNumber)
                            onCallAttempt(phoneNumber)
                        }
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(dimensionResource(R.dimen.btn_icon_size)),
                            painter = painterResource(R.drawable.ic_call),
                            contentDescription = null
                        )
                    }

                    IconButton(
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .size(dimensionResource(R.dimen.btn_size)),
                        onClick = {
                            onRemoveContact()
                        }
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(dimensionResource(R.dimen.btn_icon_size)),
                            painter = painterResource(R.drawable.ic_close),
                            contentDescription = null
                        )
                    }
                }
            }
        )
    }
}

@Preview
@Composable
fun ContactCardPreview() {
    QuiCallTheme {
        ContactCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            name = "John",
            lastName = "Doe",
            phoneNumber = "123-456-7890",
            imageResourceId = R.drawable.test_img,
        )
    }
}