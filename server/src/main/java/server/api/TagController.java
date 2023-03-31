package server.api;

import org.springframework.web.bind.annotation.*;

import server.services.TagService;

@RestController
@RequestMapping("/api/tag")
public class TagController {
    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }


}
