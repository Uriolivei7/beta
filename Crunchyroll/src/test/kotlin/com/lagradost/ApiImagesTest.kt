package com.lagradost

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class ApiImagesTest {

    private val objectMapper = jacksonObjectMapper()

    @Test
    fun `ApiImages correctly deserializes thumbnail as List of List of ApiImage`() {
        // Given
        val json = """
        {
            "thumbnail": [
                [
                    {
                        "type": "thumbnail",
                        "source": "https://example.com/image1.jpg",
                        "width": 640,
                        "height": 360
                    },
                    {
                        "type": "thumbnail",
                        "source": "https://example.com/image2.jpg",
                        "width": 1280,
                        "height": 720
                    }
                ],
                [
                    {
                        "type": "thumbnail",
                        "source": "https://example.com/image3.jpg",
                        "width": 320,
                        "height": 180
                    }
                ]
            ]
        }
        """.trimIndent()

        // When
        val apiImages: ApiImages = objectMapper.readValue(json)

        // Then
        assertNotNull(apiImages.thumbnail)
        assertEquals(2, apiImages.thumbnail?.size)
        
        // Verify first list of images
        assertEquals(2, apiImages.thumbnail?.get(0)?.size)
        assertEquals("thumbnail", apiImages.thumbnail?.get(0)?.get(0)?.type)
        assertEquals("https://example.com/image1.jpg", apiImages.thumbnail?.get(0)?.get(0)?.source)
        assertEquals(640, apiImages.thumbnail?.get(0)?.get(0)?.width)
        assertEquals(360, apiImages.thumbnail?.get(0)?.get(0)?.height)
        
        assertEquals("https://example.com/image2.jpg", apiImages.thumbnail?.get(0)?.get(1)?.source)
        assertEquals(1280, apiImages.thumbnail?.get(0)?.get(1)?.width)
        assertEquals(720, apiImages.thumbnail?.get(0)?.get(1)?.height)
        
        // Verify second list of images
        assertEquals(1, apiImages.thumbnail?.get(1)?.size)
        assertEquals("https://example.com/image3.jpg", apiImages.thumbnail?.get(1)?.get(0)?.source)
        assertEquals(320, apiImages.thumbnail?.get(1)?.get(0)?.width)
        assertEquals(180, apiImages.thumbnail?.get(1)?.get(0)?.height)
    }

    @Test
    fun `ApiImages correctly deserializes empty thumbnail lists`() {
        // Given
        val json = """
        {
            "thumbnail": []
        }
        """.trimIndent()

        // When
        val apiImages: ApiImages = objectMapper.readValue(json)

        // Then
        assertNotNull(apiImages.thumbnail)
        assertEquals(0, apiImages.thumbnail?.size)
    }

    @Test
    fun `ApiImages correctly deserializes null thumbnail`() {
        // Given
        val json = """
        {
            "thumbnail": null
        }
        """.trimIndent()

        // When
        val apiImages: ApiImages = objectMapper.readValue(json)

        // Then
        assertEquals(null, apiImages.thumbnail)
    }

    @Test
    fun `ApiImages correctly deserializes missing thumbnail field`() {
        // Given
        val json = """
        {
            "poster_tall": []
        }
        """.trimIndent()

        // When
        val apiImages: ApiImages = objectMapper.readValue(json)

        // Then
        assertEquals(null, apiImages.thumbnail)
    }

    @Test
    fun `ApiImages correctly deserializes all fields including thumbnail`() {
        // Given
        val json = """
        {
            "poster_tall": [
                [
                    {
                        "type": "poster_tall",
                        "source": "https://example.com/poster_tall.jpg",
                        "width": 768,
                        "height": 1024
                    }
                ]
            ],
            "poster_wide": [
                [
                    {
                        "type": "poster_wide",
                        "source": "https://example.com/poster_wide.jpg",
                        "width": 1920,
                        "height": 1080
                    }
                ]
            ],
            "thumbnail": [
                [
                    {
                        "type": "thumbnail",
                        "source": "https://example.com/thumbnail.jpg",
                        "width": 640,
                        "height": 360
                    }
                ]
            ]
        }
        """.trimIndent()

        // When
        val apiImages: ApiImages = objectMapper.readValue(json)

        // Then
        assertNotNull(apiImages.posterTall)
        assertNotNull(apiImages.posterWide)
        assertNotNull(apiImages.thumbnail)
        
        assertEquals(1, apiImages.posterTall?.size)
        assertEquals(1, apiImages.posterWide?.size)
        assertEquals(1, apiImages.thumbnail?.size)
        
        assertEquals("https://example.com/poster_tall.jpg", apiImages.posterTall?.get(0)?.get(0)?.source)
        assertEquals("https://example.com/poster_wide.jpg", apiImages.posterWide?.get(0)?.get(0)?.source)
        assertEquals("https://example.com/thumbnail.jpg", apiImages.thumbnail?.get(0)?.get(0)?.source)
    }
}
