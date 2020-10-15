package com.abridged.forestrymanagementsystem.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.abridged.forestrymanagementsystem.dto.Contract;
import com.abridged.forestrymanagementsystem.exception.AlreadyPresentException;
import com.abridged.forestrymanagementsystem.exception.EmptyRecordException;
import com.abridged.forestrymanagementsystem.exception.NotFoundException;

@Repository
public class ContractDaoImpl implements ContractDao {

	@PersistenceUnit
	private EntityManagerFactory emf;

	/**
	 * This method is use to get contract record.
	 * 
	 * @param contractNumber is the parameter to getContract method.
	 * @return record value base on condition, returns record if contractNumber is
	 *         present else throw new NotFoundException().
	 */
	@Override
	public Contract getContract(String contractNumber) {
		EntityManager manager = emf.createEntityManager();
		Contract record = manager.find(Contract.class, contractNumber);
		manager.close();
		if (record != null) {
			return record;
		} else
			throw new NotFoundException();
	}

	/**
	 * This method is use to add contract record.
	 * 
	 * @param contract is the parameter to addContract method.
	 * @return true value if contract is added else throw new
	 *         AlreadyPresentException().
	 */
	@Override
	public boolean addContract(Contract contract) {
		EntityManager manager = emf.createEntityManager();
		EntityTransaction transaction = manager.getTransaction();
		transaction.begin();
		try {
			manager.persist(contract);
			transaction.commit();
			return true;
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
			throw new AlreadyPresentException();
		}
	}

	/**
	 * This method is use to update contract record.
	 * 
	 * @param contract is the parameter to updateContract method.
	 * @return true if contract is updated else throw new NotFoundException().
	 */
	@Override
	public boolean updateContract(Contract contract) {
		EntityManager manager = emf.createEntityManager();
		EntityTransaction transaction = manager.getTransaction();
		transaction.begin();

		String JPQL = "update Contract c set c.customerId=:customerId, c.productId=:productId, c.deliveryPlace=:deliveryPlace, c.deliveryDate=:deliveryDate, c.quantity=:quantity, c.schedulerId=:schedulerId where c.contractNumber=:contractNumber";
		Query query = manager.createQuery(JPQL);
		query.setParameter("contractNumber", contract.getContractNumber());
		query.setParameter("customerId", contract.getCustomerId());
		query.setParameter("productId", contract.getProductId());
		query.setParameter("deliveryPlace", contract.getDeliveryPlace());
		query.setParameter("deliveryDate", contract.getDeliveryDate());
		query.setParameter("quantity", contract.getQuantity());
		query.setParameter("schedulerId", contract.getSchedulerId());

		int i = query.executeUpdate();
		transaction.commit();
		if (i != 0) {
			return true;
		}
		throw new NotFoundException();
	}

	/**
	 * This method is use to delete contract record.
	 * 
	 * @param contractNumber is the parameter to deleteContract method.
	 * @return true if contract is deleted else throw new NotFoundException().
	 */
	@Override
	public boolean deleteContract(String contractNumber) {
		String JPQL = "delete from Contract c where c.contractNumber=:contractNumber";

		EntityManager manager = emf.createEntityManager();
		EntityTransaction transaction = manager.getTransaction();
		transaction.begin();
		Query query = manager.createQuery(JPQL);
		query.setParameter("contractNumber", contractNumber);
		int i = query.executeUpdate();
		transaction.commit();
		if (i != 0) {
			return true;
		}
		throw new NotFoundException();
	}

	/**
	 * This method is use to get all contracts record.
	 * 
	 * @return contractList if contractList is not empty else throw new
	 *         EmptyRecordException().
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Contract> getAllContracts() {
		String JPQL = "select c from Contract c";
		EntityManager manager = emf.createEntityManager();
		Query query = manager.createQuery(JPQL);
		List<Contract> contractList = query.getResultList();
		manager.close();
		if (contractList.isEmpty()) {
			throw new EmptyRecordException();
		} else {
			return contractList;
		}
	}

}
