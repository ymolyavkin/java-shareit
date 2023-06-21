package ru.practicum.shareit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@SpringBootTest
class ShareItTests {

	@Test
	void contextLoads() {

	}
	@BeforeEach
	void setUp() {
		User requestor = new User("Name Requestor", "email@yandex.ru");
		ItemRequest request= new ItemRequest("description", requestor, LocalDateTime.now());
		Item item = new Item.Builder()
				.id(0L)
				.name("Name")
				.description("descr")
				.isAvailable(true)
				.owner("owner")
				.request(request)
				.build();
		User booker = new User("Name", "email@mail.ru");

		Booking booking = new Booking.Builder()
				.id(0L)
				.start(LocalDateTime.now())
				.end(LocalDateTime.now())
				.item(item)
				.booker(booker)
				.status(Status.WAITING)
				.build();
		Long itemRequestId = item.getRequest().getId();
		ItemDto itemDto = new ItemDto.Builder()
				.id(0L)
				.name("Name")
				.description("descr")
				.isAvailable(true)
				.owner("owner")
				.request(itemRequestId)
				.build();

		System.out.println();
	}

	@AfterEach
	void tearDown() {
	}

}
