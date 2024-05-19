package com.nhkim.composeimagesearch.ui.favorites

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.nhkim.composeimagesearch.R
import com.nhkim.composeimagesearch.ui.model.ResultItem
import com.nhkim.composeimagesearch.ui.theme.PrimaryMainColor

@Composable
fun FavoritesImageScreen(
    viewModel: FavoritesViewModel = hiltViewModel(),
    onNavigateToSearch:  () -> Unit,
    onNavigateToFavorites:  () -> Unit
) {
    val result by viewModel.bookmark.collectAsState()
    Scaffold { paddingValues ->
        FavoritesImageList(result, modifier = Modifier.padding(paddingValues), viewModel::removeItem)
    }
}

@Composable
fun FavoritesImageList(
    results: List<ResultItem>,
    modifier: Modifier,
    onItemClick: (ResultItem) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(results, key = { item -> item.url }) { item ->
            FavoriteImageItem(item, onItemClick = onItemClick)
        }
    }
}

@Composable
fun FavoriteImageItem(
    item: ResultItem,
    onItemClick: (ResultItem) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .clickable(
                interactionSource = remember {
                    MutableInteractionSource()
                },
                indication = null,
                onClick = {
                    onItemClick(item)
                }
            ),
    ) {
        Box{
            AsyncImage(
                model = item.url,
                contentDescription = "item",
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Row(
                modifier = Modifier
                    .padding(top = 8.dp, start = 4.dp, end = 4.dp)
            ) {
                Text(
                    text = item.type,
                    modifier = Modifier
                        .background(
                            color = PrimaryMainColor,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .padding(vertical = 4.dp, horizontal = 10.dp),
                    color = Color.White,
                )
                Spacer(modifier = Modifier.weight(1f))
                Image(
                    modifier = Modifier
                        .size(20.dp),
                    // 왜 아래 코드를 실행하면 항상 빈 하트가 나오지?
//                    painter = if (item.isLike) painterResource(R.drawable.ic_full_heart) else painterResource(
//                        R.drawable.ic_empty_heart
//                    ),
                    painter = painterResource(R.drawable.ic_full_heart),
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

@Preview
@Composable
fun FavoriteImageItemPreview() {
    FavoriteImageItem(ResultItem("Image", "123", "12", "https://picsum.photos/200/300", false), {})
}
