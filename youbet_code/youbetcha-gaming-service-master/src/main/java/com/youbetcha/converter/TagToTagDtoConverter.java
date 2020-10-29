package com.youbetcha.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.youbetcha.model.entity.Tag;
import com.youbetcha.model.games.TagDto;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class TagToTagDtoConverter implements Converter<Tag, TagDto> {

    Gson gson = new Gson();

    public TagToTagDtoConverter() {
    }

    @Override
    public TagDto convert(Tag source) {
    	TagDto tagDto = new TagDto();
    	tagDto.setId(source.getId());
    	tagDto.setKeyWord(source.getKeyWord());
    	tagDto.setName(source.getName());
    	tagDto.setOrderNumber(source.getOrderNumber());

        return tagDto;
    }
}
