package ru.practicum.shareitserver.request.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareitserver.request.model.ItemRequest;


public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    Page<ItemRequest> findAllByRequesterIdOrderByCreatedDesc(Long requesterId, Pageable pageable);

    Page<ItemRequest> findAllByRequesterIdNotOrderByCreatedDesc(Long requesterId, Pageable pageable);
}
