package cst438.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cst438.domain.Model.States;
import cst438.domain.Repository.StatesRepository;


@Service
public class StatesService {
   @Autowired
   private StatesRepository statesRepository;
   
   public StatesService() {
      
   }
   
   public StatesService(StatesRepository statesRepository) {
      super();
      this.statesRepository = statesRepository;
   }
   
   public States fetchByState(String code) {
      return statesRepository.findByStateCode(code);
   }
   
   public List<States> fetchAll() {
      return statesRepository.findAll();
   }
}
