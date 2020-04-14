package cst438.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cst438.domain.Model.Coctail;

@Service
public class CoctailService {

   @Autowired
   public CoctailAPIService coctailServ;

   public CoctailService() {

   }

   public CoctailService(CoctailAPIService coctailServ) {
      this.coctailServ = coctailServ;
   }

   public Coctail getARandomCoctail() {
      return coctailServ.getARandomCoctail();
   }
   
}
