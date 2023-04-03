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

    public Tag getTag(long tagId){
        return tagRepository.findById(tagId).get();
    }

    public void removeTag(long tagId){
        tagRepository.deleteById(tagId);
    }

}
