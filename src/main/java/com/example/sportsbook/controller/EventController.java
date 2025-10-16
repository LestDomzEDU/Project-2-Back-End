package com.example.sportsbook.controller;

import com.example.sportsbook.model.Event;
import com.example.sportsbook.repository.EventRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventRepository eventRepository;

    public EventController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @GetMapping
    public List<Event> all() {
        return eventRepository.all();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(eventRepository.getById(id));
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Event> create(@RequestBody Event e) {
        Event saved = eventRepository.create(e);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PatchMapping("/{id}/result")
    public ResponseEntity<Void> updateResult(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String result = body.getOrDefault("result", "");
        int updated = eventRepository.updateResult(id, result);
        return (updated > 0) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        int deleted = eventRepository.delete(id);
        return (deleted > 0) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
