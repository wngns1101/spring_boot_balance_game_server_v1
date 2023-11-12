package com.example.balanceGame.repository;

import com.example.balanceGame.entity.User;
import com.example.balanceGame.exception.Message;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TransactionRequiredException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import static com.example.balanceGame.entity.QUser.user;
@Slf4j
@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final EntityManager em;
    private final JPAQueryFactory qm;

    // 이메일 엔티티 조회
    public User findByUserEmail(String email) {
        JPAQueryFactory qm = new JPAQueryFactory(em);
        return qm.selectFrom(user).where(user.userEmail.eq(email)).fetchOne();
    }

    // 아이디 엔티티 조회
    public Long findByUserId(String userId) {
        JPAQueryFactory qm = new JPAQueryFactory(em);
        return qm.select(user.userKey).from(user).where(user.userId.eq(userId)).fetchOne();
    }

    // user key 엔티티 조회
    public User findByUserKey(Long userKey) {
        JPAQueryFactory qm = new JPAQueryFactory(em);
        return qm.selectFrom(user).where(user.userKey.eq(userKey)).fetchOne();
    }

    // 회원가입 메서드
    public boolean join(User user) {
        try {
            // persist 작업 수행
            em.persist(user);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public boolean delete(User user) {
        try {
            em.remove(user);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
