package fa.training.eventbox.controller;

import fa.training.eventbox.constant.AppConstant;
import fa.training.eventbox.model.dto.EventFormDto;
import fa.training.eventbox.model.entity.Event;
import fa.training.eventbox.service.EventService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Controller
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }


    @GetMapping("/events") // GET /events
    public String showList(Model model,
                           @RequestParam(required = false, defaultValue = AppConstant.DEFAULT_PAGE_STR) Integer page,
                           @RequestParam(required = false, defaultValue = AppConstant.DEFAULT_PAGE_SIZE_STR) Integer size,
                           @RequestParam(required = false, name = "sort",
                   defaultValue = AppConstant.DEFAULT_SORT_FIELD) List<String> sorts,
                           @RequestParam(required = false, name = "q") Optional<String> keywordOpt) {

        List<Sort.Order> orders = new ArrayList<>();
        for (String sortField : sorts) {
            boolean isDesc = sortField.startsWith("-");
            orders.add(isDesc ? Sort.Order.desc(sortField.substring(1))
                               : Sort.Order.asc(sortField));
        }
        // WHERE deleted = 0
        Specification<Event> spec = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("deleted"), false);
        if (keywordOpt.isPresent()) {
            // WHERE (name like '%Fville1%' OR place like '%Fville1%')
            Specification<Event> specByKeyword = (root, query, criteriaBuilder) ->
                    criteriaBuilder.or(
                            criteriaBuilder.like(root.get("name"), "%" + keywordOpt.get() + "%"),
                            criteriaBuilder.like(root.get("place"), "%" + keywordOpt.get() + "%")
                    );

            // WHERE deleted = 0 AND (name like '%Fville1%' OR place like '%Fville1%')
            spec = spec.and(specByKeyword);
        }

        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(orders));
        Page<Event> eventPage = eventService.findAllPaging(spec, pageRequest);
        model.addAttribute("eventPage", eventPage);
        return "events/list"; // forward to view
    }

    @GetMapping("/events/create")
    public String showCreate(Model model) {
        model.addAttribute("eventFormDto", new EventFormDto());
        return "events/form";
    }

    @PostMapping("/events/create")
    public String create(@Valid EventFormDto eventFormDto,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "events/form";
        }
        if (eventService.existEventName(eventFormDto.getName())) {
            bindingResult.rejectValue("name", "event.name.duplicate");
        }
        if (bindingResult.hasErrors()) {
            return "events/form";
        }
        // Convert eventFormDto -> event entity
        Event event = new Event();
        BeanUtils.copyProperties(eventFormDto, event);
        eventService.create(event);

        redirectAttributes.addFlashAttribute("successMessage",
                "event.create.success");

        return "redirect:/events";
    }

//    @GetMapping("/events/update")
//    public String showUpdate(Model model,
//                             @RequestParam Long id) {
//        System.out.println(id);
//
//        return "events/form";
//    }

    @GetMapping({"/events/update/{id}"})
    public String showUpdate(Model model,
                             @PathVariable(name = "id") Long eventId) {
        System.out.println(eventId);

        return "events/form";
    }

}
