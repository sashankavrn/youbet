package com.youbetcha.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.youbetcha.model.entity.Tag;
import com.youbetcha.model.games.tags.TagRequest;
import com.youbetcha.repository.TagRepository;

@Service
@Transactional
public class TagService {

	@Autowired
    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

//    public void createMapTagToGame(Long id, TagRequest tagRequest){
//        Tag tag = new Tag();
//        tag.setName(tagRequest.getName());
//        if(Objects.nonNull(tagRequest.getEnabled())){
//            tag.setEnabled(tagRequest.getEnabled());
//        }else{
//            tag.setEnabled(Boolean.valueOf(false));
//        }
//        Optional<Game> game = gameRepository.findById(id);
//        if (game.isPresent()){
//            Set<Game> games = new HashSet<>();
//            games.add(game.get());
//            tag.setGames(games);
//            tagRepository.save(tag);
//        }
//    }

    public Tag createTag(TagRequest tagRequest){
        Tag tag = new Tag();
        tag.setName(tagRequest.getName());
        if(Objects.nonNull(tagRequest.getEnabled())){
            tag.setEnabled(tagRequest.getEnabled());
        }else{
            tag.setEnabled(Boolean.valueOf(false));
        }
        tagRepository.save(tag);
        return tag;
    }

    public List<Tag> retrieveAllTags() {
    	return tagRepository.findAll();
    }
    
    public Optional<Tag> retrieveTag(Long id) {
        Optional<Tag> tag = tagRepository.findById(id);
        return tag;
    }

    public Optional<Tag> updateTag(Long id, TagRequest tagRequest){
        Optional<Tag> tag = tagRepository.findById(id);
        if(tag.isPresent()){
            tag.get().setName(tagRequest.getName());
            if(Objects.nonNull(tagRequest.getEnabled())){
                tag.get().setEnabled(tagRequest.getEnabled());
            }else{
                tag.get().setEnabled(Boolean.valueOf(false));
            }
            tagRepository.saveAndFlush(tag.get());
        }

        return tag;
    }

    public void deleteTag(Long id){
        tagRepository.deleteById(id);
    }
}
