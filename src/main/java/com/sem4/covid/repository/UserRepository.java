package com.sem4.covid.repository;

import com.sem4.covid.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    @Query("SELECT u from User u where u.deletedAt IS NULL and u.id = :id")
    User findIdActive(@Param("id") int id);

    @Query("SELECT u from User u where u.deletedAt IS NULL")
    List<User> getAllUserActive();

    @Query("SELECT u from User u where u.deletedAt IS NULL and u.email = :email and u.status=2")
    User findAccountAdmin(@Param("email") String email);

    @Query("SELECT u from User u where u.deletedAt IS NULL and u.email = :email and u.status=1")
    User findAccountMember(@Param("email") String email);

    @Query("SELECT u from User u where u.deletedAt IS NULL and u.email = :email")
    List<User> checkEmailUnique(@Param("email") String email);

    @Query("SELECT u from User u where u.deletedAt IS NULL and u.phone = :phone")
    List<User> checkPhoneUnique(@Param("phone") String phone);

    @Query("SELECT u from User u where u.deletedAt IS NULL and u.token = :token")
    User checkToken(@Param("token") String token);
}
