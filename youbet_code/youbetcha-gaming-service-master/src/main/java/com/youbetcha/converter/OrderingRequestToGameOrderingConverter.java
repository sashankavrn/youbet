package com.youbetcha.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.youbetcha.model.entity.Game;
import com.youbetcha.model.entity.GameOrdering;
import com.youbetcha.model.entity.Tag;
import com.youbetcha.model.games.OrderingRequest;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class OrderingRequestToGameOrderingConverter implements Converter<OrderingRequest, GameOrdering> {

    Gson gson = new Gson();

    public OrderingRequestToGameOrderingConverter() {
    }

    @Override
    public GameOrdering convert(OrderingRequest source) {
    	GameOrdering go = new GameOrdering();
    	go.setCountryCode(source.getCountryCode());
    	go.setOrderNumber(source.getOrderNumber());
    	Game g = new Game();
    	g.setId(source.getGameId());
    	go.setGame(g);
    	Tag t = new Tag();
    	t.setId(source.getTagId());
    	go.setTag(t);
    	
        return go;
    }
}
