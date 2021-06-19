package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Seller;

public class SellerService {
	
	private SellerDao dao = DaoFactory.createSellerDao();
	
	public List<Seller> findAll() {//acessando o banco de dados de seller
		return dao.findAll();
	}
	
	public void saveOrUpdate(Seller obj) {//medodo atualiza ou cadastra novo obj 
		if(obj.getId() == null) {
			dao.insert(obj);
		}
		else {
			dao.update(obj);
		}
	}
	
	public void remove(Seller obj) {//remove um seller  do banco de dados
		dao.deleteById(obj.getId());
	}
}
