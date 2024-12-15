package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.category.CategoryDto;
import org.example.dto.category.NewCategoryDto;
import org.example.dto.compilation.CompilationDto;
import org.example.dto.compilation.NewCompilationDto;
import org.example.dto.compilation.UpdateCompilationRequest;
import org.example.dto.event.EventFullDto;
import org.example.dto.event.UpdateEventAdminRequest;
import org.example.dto.user.NewUserRequest;
import org.example.dto.user.UserDto;
import org.example.service.dao.CategoryService;
import org.example.service.dao.CompilationService;
import org.example.service.dao.EventService;
import org.example.service.dao.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/admin")
@RequiredArgsConstructor
@Validated
public class AdminController {
    private final UserService userService;
    private final EventService eventService;
    private final CategoryService categoryService;
    private final CompilationService compilationService;

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@Valid @RequestBody NewUserRequest newUser) {
        return userService.create(newUser);
    }

    @DeleteMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable int userId) {
        userService.deleteUser(userId);
    }

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getUsersByParam(@RequestParam(required = false) List<Integer> ids,
                                         @RequestParam(required = false, defaultValue = "0") Integer from,
                                         @RequestParam(required = false, defaultValue = "10") Integer size) {
        return userService.getUsersByParam(ids, from, size);
    }

    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(@Valid @RequestBody NewCategoryDto newCategory) {
        return categoryService.createCategory(newCategory);
    }

    @DeleteMapping("/categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable int catId) {
        categoryService.deleteCategory(catId);
    }

    @PatchMapping("/categories/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto updateCategory(@PathVariable int catId,
                                      @RequestBody CategoryDto updateCategory) {
        return categoryService.updateCategory(catId, updateCategory);
    }

    @GetMapping("/events")
    @ResponseStatus(HttpStatus.OK)
    public List<EventFullDto> getEventsByParam(@RequestParam(required = false) List<Integer> users,
                                               @RequestParam(required = false) List<String> states,
                                               @RequestParam(required = false) List<Integer> categories,
                                               @RequestParam(required = false) String rangeStart,
                                               @RequestParam(required = false) String rangeEnd,
                                               @RequestParam(required = false, defaultValue = "0") Integer from,
                                               @RequestParam(required = false, defaultValue = "10") Integer size) {
        return eventService.getEventsByParam(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto updateEvent(@PathVariable Integer eventId,
                                    @RequestBody UpdateEventAdminRequest updateEvent) {
        return eventService.updateEvent(eventId, updateEvent);
    }

    @PostMapping("/compilations")
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto createCompilation(@Valid @RequestBody NewCompilationDto newCompilation) {
        return compilationService.createCompilation(newCompilation);
    }

    @PatchMapping("/compilations/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto updateCompilation(@PathVariable Integer compId,
                                            @RequestBody UpdateCompilationRequest updateCompilation) {
        return compilationService.updateCompilation(compId, updateCompilation);
    }

    @DeleteMapping("/compilations/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Integer compId) {
        compilationService.deleteCompilation(compId);
    }

}
