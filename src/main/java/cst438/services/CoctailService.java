package cst438.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cst438.domain.Model.Coctail;

@Service
public class CoctailService {

   @Autowired
   public CoctailAPIService coctailAPIServ;

   public CoctailService() {

   }

   public CoctailService(CoctailAPIService coctailServ) {
      this.coctailAPIServ = coctailServ;
   }

   public Coctail getARandomCoctail() {
      return coctailAPIServ.getARandomCoctail();
   }
   
}
