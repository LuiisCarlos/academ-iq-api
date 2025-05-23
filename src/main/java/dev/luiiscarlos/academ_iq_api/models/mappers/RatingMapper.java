package dev.luiiscarlos.academ_iq_api.models.mappers;

import org.springframework.stereotype.Component;

import dev.luiiscarlos.academ_iq_api.models.Rating;
import dev.luiiscarlos.academ_iq_api.models.dtos.rating.RatingRequestDto;
import dev.luiiscarlos.academ_iq_api.models.dtos.rating.RatingResponseDto;

@Component
public class RatingMapper {

    @SuppressWarnings("null")
    public RatingResponseDto toResponseDto(Rating rating) {
        String userAvatarUrl = rating.getUser().getAvatar() != null
                ? rating.getUser().getAvatar().getUrl()
                : "";

        return RatingResponseDto.builder()
                .user(rating.getUser().getUsername())
                .userAvatarUrl(userAvatarUrl)
                .rating(rating.getRating())
                .comment(rating.getComment())
                .ratedAt(rating.getRatedAt())
                .build();
    }

    public Rating toModel(RatingRequestDto requestDto) {
        return Rating.builder()
                .rating(requestDto.getRating())
                .comment(requestDto.getComment())
                .build();
    }
}
