package com.example.pinokkio.api.customer;

import com.example.pinokkio.common.type.Gender;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {

    // 성별과 나이 범위로 고객을 조회하는 메서드
    List<Customer> findByGenderAndAgeBetween(Gender gender, int minAge, int maxAge);

    List<Customer> findByPosIdAndGenderAndAgeBetween(UUID posId, Gender gender, int minAge, int maxAge);
    List<Customer> findAllByPosId(UUID posId);
}
