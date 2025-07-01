package dev.luiiscarlos.academ_iq_api.features.learning.review.mapper;

import org.springframework.stereotype.Component;

import dev.luiiscarlos.academ_iq_api.features.identity.user.model.User;
import dev.luiiscarlos.academ_iq_api.features.learning.review.dto.ReviewRequest;
import dev.luiiscarlos.academ_iq_api.features.learning.review.dto.ReviewResponse;
import dev.luiiscarlos.academ_iq_api.features.learning.review.model.Review;

@Component
public class ReviewMapper {

    public Review toEntity(ReviewRequest dto) {
        return Review.builder()
                .rating(dto.getRating())
                .comment(dto.getComment())
                .build();
    }

    public ReviewResponse toDto(Review entity) {
        User user = entity.getUser();

        return ReviewResponse.builder()
                .username(user.getUsername())
                .avatar(user.getAvatar().getUrl())
                .rating(entity.getRating())
                .comment(entity.getComment())
                .ratedAt(entity.getRatedAt())
                .build();
    }

}
