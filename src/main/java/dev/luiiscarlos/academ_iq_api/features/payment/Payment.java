package dev.luiiscarlos.academ_iq_api.features.payment;

import java.time.LocalDateTime;

import dev.luiiscarlos.academ_iq_api.features.course.model.Course;
import dev.luiiscarlos.academ_iq_api.features.user.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(name = "payment_intent_id")
    private String paymentIntentId;

    @Column(name = "customer_id")
    private String customerId;

    private Long amount;

    private String currency;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Builder.Default
    @Column(name = "create_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum Status {

        succeeded,

        pending,

        failed

    }

}
