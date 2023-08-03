package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IncomingItemRequestDtoTest {

    @Test
    void testEquals() {
        String descriptionOne = "Description";
        String descriptionTwo = "Description";

        assertThat(descriptionOne, equalTo(descriptionTwo));
    }

    @Test
    void testHashCode() {
        String descriptionOne = "Description";
        String descriptionTwo = "Description";
        assertTrue(descriptionTwo.equals(descriptionOne) && descriptionOne.equals(descriptionTwo));
        assertTrue(descriptionOne.hashCode() == descriptionTwo.hashCode());
    }

    @Test
    void getDescription() {
        String description = "Description";

        IncomingItemRequestDto incomingItemRequestDto = new IncomingItemRequestDto();
        incomingItemRequestDto.setDescription(description);

        assertEquals(description, incomingItemRequestDto.getDescription());
    }
}