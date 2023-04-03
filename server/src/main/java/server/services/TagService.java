package server.services;

import commons.Tag;
import org.springframework.stereotype.Service;
import server.database.TagRepository;

@Service
public class TagService {

    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    /**
     * Saves {@code tag}  to {@link TagRepository}.
     *
     * @param tag   Tag to be saved
     * @return      Saved tag
     * @author      Kirill Zhankov
     */
    public Tag save(Tag tag){
        return tagRepository.save(tag);
    }

    /**
     * A method to get the tag by its id
     * @param tagId the id of the tag to be returned
     * @return
     */
    public Tag getTag(long tagId){
        return tagRepository.findById(tagId).get();
    }

    /**
     * A method to remove the chosen tag
     * @param tagId the id of the tag to be removed
     */
    public void removeTag(long tagId){
        tagRepository.deleteById(tagId);
    }

}
