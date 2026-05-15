package dev.danya.museum.feature.artworks.domain.entity

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class DepartmentTest {

    @Test
    fun fromId_returnsCorrectDepartment() {
        assertEquals(Department.EUROPEAN_PAINTINGS, Department.fromId(11))
        assertEquals(Department.ASIAN_ART, Department.fromId(6))
        assertEquals(Department.PHOTOGRAPHS, Department.fromId(19))
    }

    @Test
    fun fromId_unknownIdReturnsNull() {
        assertNull(Department.fromId(0))
        assertNull(Department.fromId(999))
        assertNull(Department.fromId(-1))
    }

    @Test
    fun fromId_coversGap_id2DoesNotExist() {
        assertNull(Department.fromId(2))
    }

    @Test
    fun allEntriesHaveUniqueIds() {
        val ids = Department.entries.map { it.id }
        assertEquals(ids.size, ids.toSet().size)
    }

    @Test
    fun allEntriesHaveNonBlankResourceKeys() {
        Department.entries.forEach { dept ->
            assertTrue(dept.resourceKey.isNotBlank(), "${dept.name} has blank resourceKey")
        }
    }

    @Test
    fun entriesCount() {
        assertEquals(18, Department.entries.size)
    }

    @Test
    fun fromId_roundTripsForAllEntries() {
        Department.entries.forEach { dept ->
            assertEquals(dept, Department.fromId(dept.id))
        }
    }
}
