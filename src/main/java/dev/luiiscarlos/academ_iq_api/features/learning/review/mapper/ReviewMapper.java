package dev.luiiscarlos.academ_iq_api.features.learning.review.mapper;

import org.springframework.stereotype.Component;

import dev.luiiscarlos.academ_iq_api.features.learning.review.dto.ReviewRequest;
import dev.luiiscarlos.academ_iq_api.features.learning.review.dto.ReviewResponse;
import dev.luiiscarlos.academ_iq_api.features.learning.review.model.Review;

@Component
public class ReviewMapper {

    public ReviewResponse toResponseDto(Review rating) {
        String userAvatarUrl = rating.getUser().getAvatar() != null
                ? rating.getUser().getAvatar().getUrl()
                : "";

        return ReviewResponse.builder()
                .username(rating.getUser().getUsername())
                .avatar(userAvatarUrl)
                .rating(rating.getRating())
                .comment(rating.getComment())
                .ratedAt(rating.getRatedAt())
                .build();
    }

    public Review toModel(ReviewRequest requestDto) {
        return Review.builder()
                .rating(requestDto.getRating())
                .comment(requestDto.getComment())
                .build();
    }
}
