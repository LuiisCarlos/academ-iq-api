package dev.luiiscarlos.academ_iq_api.review.mapper;

import org.springframework.stereotype.Component;

import dev.luiiscarlos.academ_iq_api.review.dto.ReviewRequestDto;
import dev.luiiscarlos.academ_iq_api.review.dto.ReviewResponseDto;
import dev.luiiscarlos.academ_iq_api.review.model.Review;

@Component
public class ReviewMapper {

    public ReviewResponseDto toResponseDto(Review rating) {
        String userAvatarUrl = rating.getUser().getAvatar() != null
                ? rating.getUser().getAvatar().getUrl()
                : "";

        return ReviewResponseDto.builder()
                .username(rating.getUser().getUsername())
                .avatar(userAvatarUrl)
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
