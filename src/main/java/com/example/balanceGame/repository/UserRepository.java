package com.example.balanceGame.repository;

import com.example.balanceGame.entity.User;
import com.example.balanceGame.exception.Message;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
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
    // 이메일 엔티티 조회
    public User findByUserEmail(String email) {
        JPAQueryFactory qm = new JPAQueryFactory(em);
        return qm.selectFrom(user).where(user.userEmail.eq(email)).fetchOne();
    }

    // 아이디 엔티티 조회
    public User findByUserId(String userId) {
        JPAQueryFactory qm = new JPAQueryFactory(em);
        return qm.selectFrom(user).where(user.userId.eq(userId)).fetchOne();
    }

    // user key 엔티티 조회
    public User findByUserKey(long id) {
        return em.find(User.class, id);
    }

    // 회원가입 메서드
    public ResponseEntity join(User user) {
        try {
            em.persist(user);
            return new ResponseEntity(Message.CREATED_USER, HttpStatus.OK);
        } catch (PersistenceException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity delete(User user) {
        try {
            em.remove(user);
            return new ResponseEntity(Message.DELETE_USER, HttpStatus.OK);
        } catch (PersistenceException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
