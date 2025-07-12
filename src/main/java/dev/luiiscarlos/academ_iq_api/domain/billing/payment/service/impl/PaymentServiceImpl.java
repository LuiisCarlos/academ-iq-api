package dev.luiiscarlos.academ_iq_api.domain.billing.payment.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

import dev.luiiscarlos.academ_iq_api.domain.billing.payment.dto.PaymentRequest;
import dev.luiiscarlos.academ_iq_api.domain.billing.payment.dto.PaymentResponse;
import dev.luiiscarlos.academ_iq_api.domain.billing.payment.mapper.PaymentMapper;
import dev.luiiscarlos.academ_iq_api.domain.billing.payment.model.Payment;
import dev.luiiscarlos.academ_iq_api.domain.billing.payment.model.PaymentStatus;
import dev.luiiscarlos.academ_iq_api.domain.billing.payment.model.PaymentType;
import dev.luiiscarlos.academ_iq_api.domain.billing.payment.repository.PaymentRepository;
import dev.luiiscarlos.academ_iq_api.domain.billing.payment.service.PaymentService;
import dev.luiiscarlos.academ_iq_api.features.identity.user.model.User;
import dev.luiiscarlos.academ_iq_api.features.learning.course.model.Course;
import dev.luiiscarlos.academ_iq_api.features.learning.course.service.CourseCrudService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    private final PaymentMapper paymentMapper;

    private final CourseCrudService courseQueryService;

    @Override
    public PaymentResponse create(long userId, PaymentRequest request) {
        Course course = courseQueryService.findById(request.getCourseId());
        PaymentIntent paymentIntent = new PaymentIntent();

        try {
            PaymentIntentCreateParams paymentIntentParams = PaymentIntentCreateParams.builder()
                    .setAmount(course.getPrice().longValue() * 100)
                    .setCurrency("eur")
                    .setDescription("Payment for course: " + course.getTitle())
                    .putMetadata("userId", String.valueOf(userId))
                    .putMetadata("courseId", String.valueOf(course.getId()))
                    .build();

            paymentIntent = PaymentIntent.create(paymentIntentParams);
        } catch (StripeException e) {
            throw new RuntimeException("BAD BOY!"); // TODO Change exception
        }

        User user = new User();
        user.setId(userId);

        Payment payment = Payment.builder()
                .user(user)
                .course(course)
                .stripePaymentIntentId(paymentIntent.getId())
                .amount(course.getPrice().longValue() * 100)
                .status(PaymentStatus.fromValue(paymentIntent.getStatus()))
                .type(PaymentType.INDIVIDUAL)
                .build();

        return paymentMapper.toDto(paymentRepository.save(payment), paymentIntent);
    }

    @Override
    public Page<PaymentResponse> getAll(Pageable pageable) {
        Page<Payment> payments = paymentRepository.findAll(pageable);

        return payments.map(paymentMapper::toDto);
    }

    @Override
    public PaymentResponse get(long userId) {
        Payment payment = paymentRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found for user ID: " + userId));

        return paymentMapper.toDto(payment);
    }

    @Override
    public PaymentResponse get(String intentId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'get'");
    }

}
