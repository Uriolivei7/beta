package com.lagradost

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
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
        assertNotNull(apiImages.thumbnailRaw)
        val thumbnail = apiImages.thumbnailRaw as? List<*>
        assertEquals(2, thumbnail?.size)
        
        // Verify first list of images
        val firstList = thumbnail?.get(0) as? List<*>
        assertEquals(2, firstList?.size)
        val firstImage = (firstList?.get(0) as? Map<*, *>)
        assertEquals("thumbnail", firstImage?.get("type"))
        assertEquals("https://example.com/image1.jpg", firstImage?.get("source"))
        assertEquals(640, firstImage?.get("width"))
        assertEquals(360, firstImage?.get("height"))
        
        val secondImage = (firstList?.get(1) as? Map<*, *>)
        assertEquals("https://example.com/image2.jpg", secondImage?.get("source"))
        assertEquals(1280, secondImage?.get("width"))
        assertEquals(720, secondImage?.get("height"))
        
        // Verify second list of images
        val secondList = thumbnail?.get(1) as? List<*>
        assertEquals(1, secondList?.size)
        val thirdImage = (secondList?.get(0) as? Map<*, *>)
        assertEquals("https://example.com/image3.jpg", thirdImage?.get("source"))
        assertEquals(320, thirdImage?.get("width"))
        assertEquals(180, thirdImage?.get("height"))
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
        assertNotNull(apiImages.thumbnailRaw)
        val thumbnail = apiImages.thumbnailRaw as? List<*>
        assertEquals(0, thumbnail?.size)
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
        assertNull(apiImages.thumbnailRaw)
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
        assertNull(apiImages.thumbnailRaw)
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
        assertNotNull(apiImages.thumbnailRaw)
        
        assertEquals(1, apiImages.posterTall?.size)
        assertEquals(1, apiImages.posterWide?.size)
        val thumbnail = apiImages.thumbnailRaw as? List<*>
        assertEquals(1, thumbnail?.size)
        
        assertEquals("https://example.com/poster_tall.jpg", apiImages.posterTall?.get(0)?.get(0)?.source)
        assertEquals("https://example.com/poster_wide.jpg", apiImages.posterWide?.get(0)?.get(0)?.source)
        val thumbnailImage = ((thumbnail?.get(0) as? List<*>)?.get(0) as? Map<*, *>)
        assertEquals("https://example.com/thumbnail.jpg", thumbnailImage?.get("source"))
    }

    // Test case 1: ApiSeriesItem.getPosterUrl() extracts URL when posterTall is null and thumbnailRaw is a List<*>
    @Test
    fun `ApiSeriesItem getPosterUrl extracts URL when posterTall is null and thumbnailRaw is List`() {
        // Given
        val json = """
        {
            "id": "series123",
            "title": "Test Series",
            "images": {
                "poster_tall": null,
                "thumbnail": [
                    [
                        {
                            "type": "thumbnail",
                            "source": "https://example.com/thumbnail-fallback.jpg",
                            "width": 640,
                            "height": 360
                        }
                    ]
                ]
            }
        }
        """.trimIndent()

        // When
        val apiSeriesItem: ApiSeriesItem = objectMapper.readValue(json)
        val posterUrl = apiSeriesItem.getPosterUrl()

        // Then
        assertNotNull(posterUrl)
        assertEquals("https://example.com/thumbnail-fallback.jpg", posterUrl)
    }

    // Test case 2: ApiSeriesItem.getPosterUrl() returns null when both posterTall and thumbnailRaw are invalid
    @Test
    fun `ApiSeriesItem getPosterUrl returns null when both posterTall and thumbnailRaw are invalid`() {
        // Given - posterTall is null and thumbnailRaw is null
        val json1 = """
        {
            "id": "series123",
            "title": "Test Series",
            "images": {
                "poster_tall": null,
                "thumbnail": null
            }
        }
        """.trimIndent()

        // When
        val apiSeriesItem1: ApiSeriesItem = objectMapper.readValue(json1)
        val posterUrl1 = apiSeriesItem1.getPosterUrl()

        // Then
        assertNull(posterUrl1)

        // Given - posterTall is empty and thumbnailRaw is empty list
        val json2 = """
        {
            "id": "series456",
            "title": "Test Series 2",
            "images": {
                "poster_tall": [],
                "thumbnail": []
            }
        }
        """.trimIndent()

        // When
        val apiSeriesItem2: ApiSeriesItem = objectMapper.readValue(json2)
        val posterUrl2 = apiSeriesItem2.getPosterUrl()

        // Then
        assertNull(posterUrl2)

        // Given - posterTall is empty and thumbnailRaw contains empty nested list
        val json3 = """
        {
            "id": "series789",
            "title": "Test Series 3",
            "images": {
                "poster_tall": [],
                "thumbnail": [[]]
            }
        }
        """.trimIndent()

        // When
        val apiSeriesItem3: ApiSeriesItem = objectMapper.readValue(json3)
        val posterUrl3 = apiSeriesItem3.getPosterUrl()

        // Then
        assertNull(posterUrl3)
    }

    // Test case 3: ApiEpisodeItem.getThumbnailUrl() extracts thumbnail URL with nested list structure
    @Test
    fun `ApiEpisodeItem getThumbnailUrl extracts URL with nested list structure`() {
        // Given
        val json = """
        {
            "id": "episode123",
            "title": "Episode 1",
            "slug_title": "episode-1",
            "episode_number": 1,
            "images": {
                "thumbnail": [
                    [
                        {
                            "type": "thumbnail",
                            "source": "https://example.com/episode-thumbnail.jpg",
                            "width": 1920,
                            "height": 1080
                        },
                        {
                            "type": "thumbnail",
                            "source": "https://example.com/episode-thumbnail-alt.jpg",
                            "width": 640,
                            "height": 360
                        }
                    ],
                    [
                        {
                            "type": "thumbnail",
                            "source": "https://example.com/episode-thumbnail-small.jpg",
                            "width": 320,
                            "height": 180
                        }
                    ]
                ]
            }
        }
        """.trimIndent()

        // When
        val apiEpisodeItem: ApiEpisodeItem = objectMapper.readValue(json)
        val thumbnailUrl = apiEpisodeItem.getThumbnailUrl()

        // Then
        assertNotNull(thumbnailUrl)
        assertEquals("https://example.com/episode-thumbnail.jpg", thumbnailUrl)
    }

    @Test
    fun `ApiEpisodeItem getThumbnailUrl returns null when images are empty or missing`() {
        // Given - images is null
        val json1 = """
        {
            "id": "episode123",
            "title": "Episode 1",
            "slug_title": "episode-1",
            "episode_number": 1,
            "images": null
        }
        """.trimIndent()

        // When
        val apiEpisodeItem1: ApiEpisodeItem = objectMapper.readValue(json1)
        val thumbnailUrl1 = apiEpisodeItem1.getThumbnailUrl()

        // Then
        assertNull(thumbnailUrl1)

        // Given - thumbnail key is missing
        val json2 = """
        {
            "id": "episode456",
            "title": "Episode 2",
            "slug_title": "episode-2",
            "episode_number": 2,
            "images": {}
        }
        """.trimIndent()

        // When
        val apiEpisodeItem2: ApiEpisodeItem = objectMapper.readValue(json2)
        val thumbnailUrl2 = apiEpisodeItem2.getThumbnailUrl()

        // Then
        assertNull(thumbnailUrl2)

        // Given - thumbnail is empty list
        val json3 = """
        {
            "id": "episode789",
            "title": "Episode 3",
            "slug_title": "episode-3",
            "episode_number": 3,
            "images": {
                "thumbnail": []
            }
        }
        """.trimIndent()

        // When
        val apiEpisodeItem3: ApiEpisodeItem = objectMapper.readValue(json3)
        val thumbnailUrl3 = apiEpisodeItem3.getThumbnailUrl()

        // Then
        assertNull(thumbnailUrl3)
    }

    // Test case 4: Deserialization of ApiImages with thumbnailRaw: Any? field
    @Test
    fun `ApiImages deserialization handles thumbnailRaw as Any correctly`() {
        // Given - thumbnailRaw as List<List<ApiImage>>
        val json1 = """
        {
            "thumbnail": [
                [
                    {
                        "type": "thumbnail",
                        "source": "https://example.com/thumb1.jpg",
                        "width": 640,
                        "height": 360
                    }
                ]
            ]
        }
        """.trimIndent()

        // When
        val apiImages1: ApiImages = objectMapper.readValue(json1)

        // Then
        assertNotNull(apiImages1.thumbnailRaw)
        // Verify it's stored as Any and can be used properly
        val thumbnailData = apiImages1.thumbnailRaw
        assert(thumbnailData is List<*>)

        // Given - thumbnailRaw as null
        val json2 = """
        {
            "thumbnail": null
        }
        """.trimIndent()

        // When
        val apiImages2: ApiImages = objectMapper.readValue(json2)

        // Then
        assertNull(apiImages2.thumbnailRaw)

        // Given - thumbnailRaw as empty list
        val json3 = """
        {
            "thumbnail": []
        }
        """.trimIndent()

        // When
        val apiImages3: ApiImages = objectMapper.readValue(json3)

        // Then
        assertNotNull(apiImages3.thumbnailRaw)
        assert(apiImages3.thumbnailRaw is List<*>)
        assertEquals(0, (apiImages3.thumbnailRaw as List<*>).size)
    }

    // Test case 5: Deserialization of ApiEpisodeItem with images: Map<String, List<List<ApiImage>>>?
    @Test
    fun `ApiEpisodeItem deserialization handles images Map with nested lists correctly`() {
        // Given - Complete episode with images
        val json1 = """
        {
            "id": "episode123",
            "title": "Episode 1",
            "slug_title": "episode-1",
            "episode_number": 1,
            "images": {
                "thumbnail": [
                    [
                        {
                            "type": "thumbnail",
                            "source": "https://example.com/ep1-thumb.jpg",
                            "width": 1920,
                            "height": 1080
                        }
                    ]
                ],
                "poster": [
                    [
                        {
                            "type": "poster",
                            "source": "https://example.com/ep1-poster.jpg",
                            "width": 768,
                            "height": 1024
                        }
                    ]
                ]
            }
        }
        """.trimIndent()

        // When
        val apiEpisodeItem1: ApiEpisodeItem = objectMapper.readValue(json1)

        // Then
        assertNotNull(apiEpisodeItem1.images)
        assertEquals(2, apiEpisodeItem1.images?.size)
        assertNotNull(apiEpisodeItem1.images?.get("thumbnail"))
        assertNotNull(apiEpisodeItem1.images?.get("poster"))
        
        // Verify nested structure
        val thumbnailList = apiEpisodeItem1.images?.get("thumbnail")
        assertEquals(1, thumbnailList?.size)
        assertEquals(1, thumbnailList?.get(0)?.size)
        assertEquals("https://example.com/ep1-thumb.jpg", thumbnailList?.get(0)?.get(0)?.source)
        
        val posterList = apiEpisodeItem1.images?.get("poster")
        assertEquals(1, posterList?.size)
        assertEquals(1, posterList?.get(0)?.size)
        assertEquals("https://example.com/ep1-poster.jpg", posterList?.get(0)?.get(0)?.source)

        // Given - Episode with null images
        val json2 = """
        {
            "id": "episode456",
            "title": "Episode 2",
            "slug_title": "episode-2",
            "episode_number": 2,
            "images": null
        }
        """.trimIndent()

        // When
        val apiEpisodeItem2: ApiEpisodeItem = objectMapper.readValue(json2)

        // Then
        assertNull(apiEpisodeItem2.images)

        // Given - Episode with empty images map
        val json3 = """
        {
            "id": "episode789",
            "title": "Episode 3",
            "slug_title": "episode-3",
            "episode_number": 3,
            "images": {}
        }
        """.trimIndent()

        // When
        val apiEpisodeItem3: ApiEpisodeItem = objectMapper.readValue(json3)

        // Then
        assertNotNull(apiEpisodeItem3.images)
        assertEquals(0, apiEpisodeItem3.images?.size)
    }
}
