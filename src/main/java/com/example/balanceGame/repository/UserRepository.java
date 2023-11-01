package com.example.balanceGame.repository;

import com.example.balanceGame.entity.User;
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
    public User findByUserEmail(String name) {
        JPAQueryFactory qm = new JPAQueryFactory(em);
        return qm.selectFrom(user).where(user.userEmail.eq(name)).fetchOne();
    }

    public User findByUserId(long id) {
        return em.find(User.class, id);
    }

    public ResponseEntity join(User user) {
        try {
            em.persist(user);
            return new ResponseEntity("회원가입에 성공했습니다.", HttpStatus.OK);
        } catch (PersistenceException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
