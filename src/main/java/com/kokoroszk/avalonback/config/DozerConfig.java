package com.kokoroszk.avalonback.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.dozermapper.core.loader.api.BeanMappingBuilder;
import com.kokoroszk.avalonback.dto.Game;
import com.kokoroszk.avalonback.response.GameResponse;

@Configuration
public class DozerConfig {
    @Bean
    public BeanMappingBuilder a() {
        return new BeanMappingBuilder() {
            @Override
            protected void configure() {
                mapping(type(Game.Player.class), type(GameResponse.Player.class))
                    .fields("selected", "isSelected");
            }
        };
    }
}
