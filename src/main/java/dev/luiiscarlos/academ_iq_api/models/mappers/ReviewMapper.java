package dev.luiiscarlos.academ_iq_api.models.mappers;

import org.springframework.stereotype.Component;

import dev.luiiscarlos.academ_iq_api.models.Review;
import dev.luiiscarlos.academ_iq_api.models.dtos.rating.ReviewRequestDto;
import dev.luiiscarlos.academ_iq_api.models.dtos.rating.ReviewResponseDto;

@Component
public class ReviewMapper {

    public ReviewResponseDto toResponseDto(Review rating) {
        String userAvatarUrl = rating.getUser().getAvatar() != null
                ? rating.getUser().getAvatar().getUrl()
                : "";

        return ReviewResponseDto.builder()
                .user(rating.getUser().getUsername())
                .userAvatarUrl(userAvatarUrl)
                .rating(rating.getRating())
                .comment(rating.getComment())
                .ratedAt(rating.getRatedAt())
                .build();
    }

    public Review toModel(ReviewRequestDto requestDto) {
        return Review.builder()
                .rating(requestDto.getRating())
                .comment(requestDto.getComment())
                .build();
    }
}
