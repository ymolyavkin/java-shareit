package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NoneXSharerUserIdException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.OwnerMismatchException;
import ru.practicum.shareit.item.dto.IncomingItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public List<Item> getAll() {
        return itemRepository.findAll();
    }

    @Override
    public Item getItemById(Long id) {
        return itemRepository.getReferenceById(id);
    }

    @Override
    public List<ItemDto> getItemsByUser(Long userId) {
        List<Item> items = itemRepository.findAllByOwnerId(userId);
        return items
                .stream()
                .map(ItemMapper::mapToItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto addItem(IncomingItemDto incomingItemDto) {
        if (incomingItemDto.getOwnerId().equals(-1L)) {
            throw new NoneXSharerUserIdException("Не указан владелец вещи");
        }
        Long userId = incomingItemDto.getOwnerId();
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id %d не найден", userId)));
        Item item = itemRepository.save(ItemMapper.mapToItem(incomingItemDto, owner));

        return ItemMapper.mapToItemDto(item);
    }

    @Override
    public ItemDto updateItem(IncomingItemDto incomingItemDto, Long itemId, Long userId) {
        Item item = itemRepository.getReferenceById(itemId);
        if (!userId.equals(item.getOwnerId())) {
            throw new OwnerMismatchException("Указанный пользователь не является владельцем вещи");
        }
        boolean needsToBeChanged = false;
        if (incomingItemDto.getName() != null && !incomingItemDto.getName().equals(item.getName())) {
            item.setName(incomingItemDto.getName());
            needsToBeChanged = true;
        }
        if (incomingItemDto.getDescription() != null && !incomingItemDto.getDescription().equals(item.getDescription())) {
            item.setDescription(incomingItemDto.getDescription());
            needsToBeChanged = true;
        }
        if (incomingItemDto.getAvailable() != null && !incomingItemDto.getAvailable().equals(item.getAvailable())) {
            item.setAvailable(incomingItemDto.getAvailable());
            needsToBeChanged = true;
        }
        /*if (incomingItemDto.getOwnerId() != null && !incomingItemDto.getOwnerId().equals(item.getOwnerId())) {
            item.setOwnerId(incomingItemDto.getOwnerId());
            needsToBeChanged = true;
        }*/
        if (needsToBeChanged) {
            itemRepository.saveAndFlush(item);
        }
        return ItemMapper.mapToItemDto(item);
    }

    @Override
    public List<ItemDto> searchItemsByText(String searchText) {
        List<Item> items = itemRepository.findByNameIsContainingIgnoreCaseOrDescriptionIsContainingIgnoreCase(searchText, searchText);
        // return itemRepository.findByNameLikeOrDescriptionLike(searchText.toLowerCase(), searchText.toLowerCase());
        return items
                .stream()
                .map(ItemMapper::mapToItemDto)
                .collect(Collectors.toList());
    }
//    @Override
//    public List<Item> searchItemsByText(String searchText) {
/*        ExampleMatcher customExampleMatcher = ExampleMatcher.matchingAny()
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("description", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withIgnorePaths("id", "available", "owner","request", "numberOfTimesToRent");
        Item item = new Item();
        item.setName(searchText);
        item.setDescription(searchText);
        Example example = Example.of(item, customExampleMatcher);*/

    //  return itemRepository.findAll(example);
    // }
/*
ExampleMatcher customExampleMatcher = ExampleMatcher.matchingAny()
      .withMatcher("firstName", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
      .withMatcher("lastName", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

    Example example = Example.of(Passenger.from("e", "s", null), customExampleMatcher);

public Iterable<User> searchByText(String searchText) {

    String likeExpression = "%" + searchText + "%";
    Iterable<User> users = userRepository.searchByText(likeExpression);
    return users ;
}
    @Override
    public Optional<Item> getItemById(Long id) {
        return itemStorage.getItemById(id);
    }

    @Override
    public List<Item> searchItems(String keyword) {
        return itemStorage.searchItems(keyword);
    }*/
}
