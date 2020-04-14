package cst438.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cst438.domain.States;
import cst438.domain.StatesRepository;


@Service
public class StatesService {
   @Autowired
   private StatesRepository statesRepository;
   
   public StatesService() {
      
   }
   
   public States fetchByState(String code) {
      return statesRepository.findByStateCode(code);
   }
   
   public List<States> fetchAll() {
      return statesRepository.findAll();
   }
}