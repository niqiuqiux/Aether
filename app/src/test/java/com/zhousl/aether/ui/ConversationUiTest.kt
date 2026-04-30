package com.zhousl.aether.ui

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ConversationUiTest {
    @Test
    fun pendingIndicatorShowsThinkingAfterBodyTextResetsForToolCall() {
        val previousBlocks = listOf(
            AssistantResponseBlock.Text(
                id = "text-1",
                text = "I will inspect the file first.",
            ),
            AssistantResponseBlock.ToolGroup(
                id = "tools-1",
                toolInvocations = listOf(
                    ChatToolInvocation(
                        id = "call-1",
                        toolName = "read",
                        argumentsJson = """{"path":"README.md"}""",
                        isRunning = true,
                    )
                ),
            ),
        )

        assertTrue(previousBlocks.any { it is AssistantResponseBlock.Text && it.text.isNotBlank() })
        assertEquals(
            PendingGenerationIndicator.Thinking,
            pendingGenerationIndicator(
                isSending = true,
                pendingAssistantText = "",
                pendingStatusText = "",
            ),
        )
    }

    @Test
    fun pendingIndicatorHidesThinkingWhileBodyTextIsActive() {
        assertEquals(
            PendingGenerationIndicator.None,
            pendingGenerationIndicator(
                isSending = true,
                pendingAssistantText = "Streaming body text",
                pendingStatusText = "",
            ),
        )
    }

    @Test
    fun pendingIndicatorShowsStatusWhenStatusTextExists() {
        assertEquals(
            PendingGenerationIndicator.Status,
            pendingGenerationIndicator(
                isSending = true,
                pendingAssistantText = "Streaming body text",
                pendingStatusText = "Reconnecting...",
            ),
        )
    }

    @Test
    fun pendingIndicatorHidesAfterTurnEnds() {
        assertEquals(
            PendingGenerationIndicator.None,
            pendingGenerationIndicator(
                isSending = false,
                pendingAssistantText = "",
                pendingStatusText = "",
            ),
        )
    }
}
