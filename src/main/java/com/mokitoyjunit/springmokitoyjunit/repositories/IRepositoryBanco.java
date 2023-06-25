package com.mokitoyjunit.springmokitoyjunit.repositories;

import com.mokitoyjunit.springmokitoyjunit.models.DtoBanco;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IRepositoryBanco extends JpaRepository<DtoBanco, Long> {



}
