package com.example.balanceGame.repository;

import com.example.balanceGame.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        return qm.selectFrom(user).where(user.userEmail.eq(email)).fetchOne();
    }

    // 아이디 엔티티 조회
    public Long findByUserId(String userId) {
        return qm.select(user.userKey).from(user).where(user.userId.eq(userId)).fetchOne();
    }

    // user key 엔티티 조회
    public User findByUserKey(Long userKey) {
        return qm.selectFrom(user).where(user.userKey.eq(userKey)).fetchOne();
    }

    // 회원가입 메서드
    public boolean join(User user) {
        try {
            // persist 작업 수행
            em.persist(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(User user) {
        try {
            em.remove(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
