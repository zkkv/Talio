package server.services;

import commons.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import server.database.TagRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

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

    @Test
    public void testGetter(){
        Tag tagToGet = new Tag();
        tagToGet.setId(1L);

        when(tagRepository.findById(1L)).thenReturn(Optional.of(tagToGet));

        Tag actualTag = tagService.getTag(1L);

        assertEquals(tagToGet, actualTag);
    }

    @Test
    public void testRemove(){
        Tag tagToRemove = new Tag();
        tagToRemove.setId(1L);

        doNothing().when(tagRepository).deleteById(1L);

        tagService.removeTag(1L);

        verify(tagRepository,times(1)).deleteById(1L);
    }

}
