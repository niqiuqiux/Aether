package com.zhousl.aether.data

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class LlmApiClientTest {
    @Test
    fun vertexOfficialEndpointIncludesPreviewModels() = runBlocking {
        val result = LlmApiClient.fetchModels(
            LlmProviderConfig(
                providerId = "vertex",
                name = "Vertex",
                providerType = LlmProvider.VertexExpress,
                apiKey = "test-key",
                baseUrl = "https://aiplatform.googleapis.com/v1/",
                modelId = "gemini-2.5-flash",
            )
        )

        assertEquals(null, result.error)
        assertTrue(result.models.contains("gemini-3.1-pro-preview"))
        assertTrue(result.models.contains("gemini-3-flash-preview"))
        assertTrue(result.models.contains("gemini-3.1-flash-lite-preview"))
        assertTrue(result.models.indexOf("gemini-3.1-pro-preview") < result.models.indexOf("gemini-2.5-pro"))
    }

    @Test
    fun vertexRegionalOfficialEndpointIncludesPreviewModels() = runBlocking {
        val result = LlmApiClient.fetchModels(
            LlmProviderConfig(
                providerId = "vertex",
                name = "Vertex",
                providerType = LlmProvider.VertexExpress,
                apiKey = "test-key",
                baseUrl = "https://us-central1-aiplatform.googleapis.com/v1",
                modelId = "gemini-2.5-flash",
            )
        )

        assertTrue(result.models.contains("gemini-3.1-flash-lite-preview"))
    }

    @Test
    fun vertexCustomEndpointDoesNotIncludeOfficialPreviewPatch() = runBlocking {
        val result = LlmApiClient.fetchModels(
            LlmProviderConfig(
                providerId = "vertex",
                name = "Vertex",
                providerType = LlmProvider.VertexExpress,
                apiKey = "test-key",
                baseUrl = "https://vertex-proxy.example.com/v1",
                modelId = "gemini-2.5-flash",
            )
        )

        assertFalse(result.models.contains("gemini-3.1-pro-preview"))
        assertFalse(result.models.contains("gemini-3-flash-preview"))
        assertFalse(result.models.contains("gemini-3.1-flash-lite-preview"))
    }
}
