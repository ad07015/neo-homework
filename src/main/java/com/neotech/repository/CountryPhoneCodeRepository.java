package com.neotech.repository;

import com.neotech.entity.CountryPhoneCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CountryPhoneCodeRepository extends JpaRepository<CountryPhoneCode, Integer> {

    List<CountryPhoneCode> findByCodeStartingWith(String firstDigit);
}
