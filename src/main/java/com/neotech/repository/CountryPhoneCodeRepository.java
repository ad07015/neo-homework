package com.neotech.repository;

import com.neotech.model.CountryPhoneCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryPhoneCodeRepository extends JpaRepository<CountryPhoneCode, Integer> {
}
