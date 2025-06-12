package dev.luiiscarlos.academ_iq_api.payment;

import java.time.LocalDateTime;

import dev.luiiscarlos.academ_iq_api.course.model.Course;
import dev.luiiscarlos.academ_iq_api.user.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    @Column(name = "user_id")
    private User user;

    @ManyToOne
    @Column(name = "course_id")
    private Course course;

    @Column(name = "payment_intent_id")
    private String paymentIntentId;

    @Column(name = "customer_id")
    private String customerId;

    private Long amount;

    private String currency;

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
