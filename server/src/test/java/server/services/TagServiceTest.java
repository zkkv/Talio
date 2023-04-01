package server.services;

import commons.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import server.database.TagRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class TagServiceTest {
    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private TagService tagService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSave() {
        Tag tagToSave = new Tag();

        Tag expectedSavedTag = new Tag();
        expectedSavedTag.setId(1L);

        when(tagRepository.save(tagToSave)).thenReturn(expectedSavedTag);

        Tag actualSavedTag = tagService.save(tagToSave);

        assertEquals(expectedSavedTag, actualSavedTag);
    }
}
