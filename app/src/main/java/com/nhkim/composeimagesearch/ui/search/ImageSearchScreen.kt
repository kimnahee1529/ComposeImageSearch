package com.nhkim.composeimagesearch.ui.search

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.nhkim.composeimagesearch.R
import com.nhkim.composeimagesearch.ui.model.ResultItem
import com.nhkim.composeimagesearch.ui.model.UiState
import com.nhkim.composeimagesearch.ui.theme.PrimaryColor
import com.nhkim.composeimagesearch.ui.theme.PrimaryMainColor

@Composable
fun ImageSearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    onNavigateToSearch: () -> Unit,
    onNavigateToFavorites: () -> Unit
) {
    val results by viewModel.searchResult.collectAsState()
    Scaffold(
        bottomBar = {
        },
        topBar = {
            SearchText(
//                onClick = { viewModel.searchImage(it) }
                onClick = viewModel::searchImage
            )
        }
    ) { paddingValues ->
        when (results) {
            is UiState.Success -> ImageList(
                (results as UiState.Success<List<ResultItem>>).data,
                modifier = Modifier.padding(paddingValues),
                onHeartClick = { viewModel.clickButton(it) }
            )

            is UiState.Loading -> {
                /*TODO : 프로그래스바 출력*/
            }

            is UiState.Error -> {
                /*TODO : 에러 */
            }
        }
    }
}


@Composable
fun SearchText(
    onClick: (String) -> Unit
) {
    val (text, setText) = remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    Row(
        modifier = Modifier.padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = text,
            onValueChange = setText,
            modifier = Modifier
                .weight(1f)
                .background(Color.White)
//                .border(border = BorderStroke(1.dp, Color.Gray), shape = RoundedCornerShape(12.dp))
                .padding(horizontal = 8.dp),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.White,
                focusedIndicatorColor = PrimaryColor,
                unfocusedLabelColor = Color.Gray
            ),
            placeholder = {
                Text(text = "검색어를 입력해주세요")
            },
            keyboardOptions = KeyboardOptions.Default.copy( //키보드 옵션
                imeAction = ImeAction.Done
            )
        )
        Button(
            onClick = {
                onClick(text)
                keyboardController?.hide()
            },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = PrimaryMainColor,
                contentColor = Color.White
            ),
        ) {
            Text(
                text = "검색",
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun ImageList(
    results: List<ResultItem>,
    modifier: Modifier,
    onHeartClick: (ResultItem) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Log.d("ComposeImageSearch", "results: $results")
        items(results, key = { item -> item.url }) { item ->
            ImageItem(item, onHeartClick = onHeartClick)
        }
    }
}

@Composable
fun ImageItem(
    item: ResultItem,
    onHeartClick: (ResultItem) -> Unit
) {
//    var isLike by remember { mutableStateOf(item.isLike) } //UI용
    //var isLike = remember { mutableStateOf(item.isLike) }
    Column(
        modifier = Modifier
            .padding(8.dp)
    ) {
        Box {
            AsyncImage(
                model = item.url,
                contentDescription = "item",
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(12.dp))
                    .border(
                        border = BorderStroke(3.dp, PrimaryColor),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentScale = ContentScale.Crop
            )
            Row(
                modifier = Modifier
                    .padding(top = 8.dp, start = 8.dp, end = 8.dp)
            ) {
                Image(
                    painter = painterResource(
                        id = R.drawable.ic_img
                    ),
                    contentDescription = "img",
                    modifier = Modifier
                        .size(20.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                Image(
                    modifier = Modifier
                        .size(20.dp)
                        .clickable(
                            interactionSource = remember {
                                MutableInteractionSource()
                            },
                            indication = null,
                            onClick = { onHeartClick(item) }
                        ),
                    painter = if (item.isLike) painterResource(R.drawable.ic_full_heart) else painterResource(
                        R.drawable.ic_empty_heart
                    ),
                    contentDescription = "like"
                )
            }
        }
        Text(
            text = item.title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(top = 4.dp),
        )
        Text(
            text = item.dateTime,
            color = Color.Gray,
            modifier = Modifier
                .padding(top = 4.dp),
        )
    }
}

@Composable
fun NavigationButton(
    onNavigateToSearch: () -> Unit,
    onNavigateToFavorites: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Button(
                onClick = { onNavigateToSearch() },
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)
                    .height(40.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = PrimaryMainColor,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("이미지 검색")
            }
            Button(
                onClick = { onNavigateToFavorites() },
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)
                    .height(40.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = PrimaryMainColor,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("내 보관함")
            }
        }
    }
}

@Preview
@Composable
fun SearchTextPreview() {
    SearchText({})
}

@Preview
@Composable
fun NavigationButtonPreview() {
    NavigationButton({}, {})
}

@Preview(showSystemUi = true)
@Composable
fun ImageItemPreview() {
    ImageItem(ResultItem("Image", "123", "12", "https://picsum.photos/200/300", false), {})
}

//@Preview(showSystemUi = true)
//@Composable
//fun ImageSearchScreenPreview() {
//    ComposeImageSearchTheme {
//        ImageSearchScreen()
//    }
//}