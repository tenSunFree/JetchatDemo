@file:Suppress("DEPRECATION")

package com.example.jetchatdemo.home

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.example.jetchatdemo.*
import com.example.jetchatdemo.R
import com.example.jetchatdemo.common.util.SymbolAnnotationType
import com.example.jetchatdemo.common.util.messageFormatter
import dev.chrisbanes.accompanist.insets.navigationBarsWithImePadding

@Composable
fun HomeContent(
    uiState: HomeState
) {
    val scrollState = rememberScrollState()
    Surface() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFA0B5D9))
        ) {
            Column(Modifier.fillMaxSize()) {
                Messages(
                    messages = uiState.messages,
                    modifier = Modifier.weight(1f),
                    scrollState = scrollState
                )
                UserInput(
                    onMessageSent = { content ->
                        uiState.addMessage(Message("me", content))
                    },
                    scrollState = scrollState,
                    modifier = Modifier.navigationBarsWithImePadding(),
                )
            }
        }
    }
}

@Composable
fun Messages(
    messages: List<Message>,
    scrollState: ScrollState,
    modifier: Modifier = Modifier
) {

    val scope = rememberCoroutineScope()
    Box(modifier = modifier) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState, reverseScrolling = true)
        ) {
            val authorMe = stringResource(id = R.string.author_me)
            messages.forEachIndexed { index, content ->
                val prevAuthor = messages.getOrNull(index - 1)?.author
                val nextAuthor = messages.getOrNull(index + 1)?.author
                val isFirstMessageByAuthor = prevAuthor != content.author
                val isLastMessageByAuthor = nextAuthor != content.author
                val isContentAuthor = "me" == content.author
                Message(
                    msg = content,
                    isUserMe = content.author == authorMe,
                    isFirstMessageByAuthor = isFirstMessageByAuthor,
                    isLastMessageByAuthor = isLastMessageByAuthor,
                    isContentAuthor = isContentAuthor
                )
            }
        }
    }
}

@Composable
fun Message(
    msg: Message,
    isUserMe: Boolean,
    isFirstMessageByAuthor: Boolean,
    isLastMessageByAuthor: Boolean,
    isContentAuthor: Boolean,
) {
    val painter = if (isUserMe) {
        painterResource(id = R.drawable.ali)
    } else {
        painterResource(id = R.drawable.someone_else)
    }
    val borderColor = if (isUserMe) {
        MaterialTheme.colors.primary
    } else {
        MaterialTheme.colors.secondary
    }
    val spaceBetweenAuthors = if (isFirstMessageByAuthor) {
        Modifier.padding(top = 8.dp)
    } else {
        Modifier
    }
    Row(modifier = spaceBetweenAuthors) {
        if (isFirstMessageByAuthor && !isContentAuthor) {
            Image(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .size(42.dp)
                    .border(1.5.dp, borderColor, CircleShape)
                    .border(3.dp, MaterialTheme.colors.surface, CircleShape)
                    .clip(CircleShape)
                    .align(Alignment.Top),
                painter = painter,
                contentScale = ContentScale.Crop,
                contentDescription = null,
            )
        } else {
            Spacer(modifier = Modifier.width(74.dp))
        }
        AuthorAndTextMessage(
            msg = msg,
            isFirstMessageByAuthor = isFirstMessageByAuthor,
            isLastMessageByAuthor = isLastMessageByAuthor,
            isContentAuthor = isContentAuthor
        )
    }
}

@Composable
fun AuthorAndTextMessage(
    msg: Message,
    isFirstMessageByAuthor: Boolean,
    isLastMessageByAuthor: Boolean,
    isContentAuthor: Boolean
) {
    Column(modifier = Modifier.padding(end = 16.dp)) {
        if (isFirstMessageByAuthor && !isContentAuthor) AuthorNameTimestamp(msg)
        ChatItemBubble(msg, isContentAuthor)
        if (isLastMessageByAuthor) {
            Spacer(modifier = Modifier.height(12.dp))
        } else {
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun AuthorNameTimestamp(msg: Message) {
    Row(modifier = Modifier.semantics(mergeDescendants = true) {}) {
        Text(
            text = msg.author,
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier
                .alignBy(LastBaseline)
                .paddingFrom(LastBaseline, after = 8.dp) // Space to 1st bubble
        )
    }
}

private val ChatBubbleShape = RoundedCornerShape(0.dp, 16.dp, 16.dp, 16.dp)
private val LastChatBubbleShape = RoundedCornerShape(16.dp, 0.dp, 16.dp, 16.dp)

@Composable
fun ChatItemBubble(
    message: Message,
    isContentAuthor: Boolean
) {
    val backgroundBubbleColor =
        if (isContentAuthor) {
            Color(0xFF9DE693)
        } else {
            Color(0xFFFFFFFF)
        }
    val bubbleShape = if (isContentAuthor) {
        LastChatBubbleShape
    } else {
        ChatBubbleShape
    }
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = if (isContentAuthor) {
            Alignment.End
        } else {
            Alignment.Start
        }
    ) {
        Surface(color = backgroundBubbleColor, shape = bubbleShape) {
            ClickableMessage(message = message)
        }
    }
}

@Composable
fun ClickableMessage(message: Message) {
    val uriHandler = LocalUriHandler.current
    val styledMessage = messageFormatter(text = message.content)
    ClickableText(
        text = styledMessage,
        style = MaterialTheme.typography.body1.copy(color = LocalContentColor.current),
        modifier = Modifier.padding(8.dp),
        onClick = {
            styledMessage
                .getStringAnnotations(start = it, end = it)
                .firstOrNull()
                ?.let { annotation ->
                    when (annotation.tag) {
                        SymbolAnnotationType.LINK.name -> uriHandler.openUri(annotation.item)
                        else -> Unit
                    }
                }
        }
    )
}