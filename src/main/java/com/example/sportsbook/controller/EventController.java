package com.example.sportsbook.controller;

import com.example.sportsbook.model.Event;
import com.example.sportsbook.repository.EventRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventRepository eventRepository;

    public EventController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    // GET /api/events
    @GetMapping
    public List<Event> getAll() {
        // NOTE: repository method is `all()`, not `findAll()`
        return eventRepository.all();
    }

    // GET /api/events/{id}
    @GetMapping("/{id}")
    public Event getOne(@PathVariable long id) {
        return eventRepository.getById(id);
    }

    // POST /api/events
    @PostMapping
    public ResponseEntity<Void> create(@RequestBody Event event) {
        int rows = eventRepository.create(event);
        if (rows == 1) {
            // You can adjust the URI format depending on how IDs are generated
            return ResponseEntity.created(URI.create("/api/events")).build();
        }
        return ResponseEntity.badRequest().build();
    }

    // PATCH /api/events/{id}/result
    @PatchMapping("/{id}/result")
    public ResponseEntity<Void> updateResult(@PathVariable long id, @RequestBody String result) {
        int rows = eventRepository.updateResult(id, result);
        return rows == 1 ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // DELETE /api/events/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        int rows = eventRepository.delete(id);
        return rows == 1 ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
