package com.abridged.forestrymanagementsystem.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;
import com.abridged.forestrymanagementsystem.dto.Orders;
import com.abridged.forestrymanagementsystem.exception.AlreadyPresentException;
import com.abridged.forestrymanagementsystem.exception.EmptyRecordException;
import com.abridged.forestrymanagementsystem.exception.NotFoundException;

@Repository
public class OrderDaoImpl implements OrderDao {

	@PersistenceUnit
	private EntityManagerFactory emf;

	/**
	 * This method is use to get order record.
	 * 
	 * @param orderNumber is the parameter to getOrder method.
	 * @return record value base on condition, returns record if orderNumber is
	 *         present else throw new NotFoundException().
	 */
	@Override
	public Orders getOrder(String orderNumber) {
		EntityManager manager = emf.createEntityManager();
		Orders record = manager.find(Orders.class, orderNumber);
		manager.close();
		if (record != null) {
			return record;
		} else
			throw new NotFoundException();
	}

	/**
	 * This method is use to add order record.
	 * 
	 * @param order is the parameter to addOrder method.
	 * @return true value if order is added else throw new
	 *         AlreadyPresentException().
	 */
	@Override
	public boolean addOrder(Orders order) {
		EntityManager manager = emf.createEntityManager();
		EntityTransaction transaction = manager.getTransaction();
		transaction.begin();
		try {
			manager.persist(order);
			transaction.commit();
			return true;
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
			throw new AlreadyPresentException();
		}
	}

	/**
	 * This method is use to update order record.
	 * 
	 * @param order is the parameter to updateOrder method.
	 * @return true if order is updated else throw new NotFoundException().
	 */
	@Override
	public boolean updateOrder(Orders order) {
		EntityManager manager = emf.createEntityManager();
		EntityTransaction transaction = manager.getTransaction();
		transaction.begin();

		String JPQL = "update Orders o set o.customerId=:customerId, o.productId=:productId, o.deliveryPlace=:deliveryPlace, o.deliveryDate=:deliveryDate, o.quantity=:quantity, o.schedulerId=:schedulerId, o.contractNumber=:contractNumber where o.orderNumber=:orderNumber";
		Query query = manager.createQuery(JPQL);
		query.setParameter("orderNumber", order.getOrderNumber());
		query.setParameter("customerId", order.getCustomerId());
		query.setParameter("productId", order.getProductId());
		query.setParameter("deliveryPlace", order.getDeliveryPlace());
		query.setParameter("deliveryDate", order.getDeliveryDate());
		query.setParameter("quantity", order.getQuantity());
		query.setParameter("schedulerId", order.getSchedulerId());
		query.setParameter("contractNumber", order.getContractNumber());


		int i = query.executeUpdate();
		transaction.commit();
		if (i != 0) {
			return true;
		}
		throw new NotFoundException();
	}

	/**
	 * This method is use to delete order record.
	 * 
	 * @param orderNumber is the parameter to deleteOrder method.
	 * @return true if order is deleted else throw new NotFoundException().
	 */
	@Override
	public boolean deleteOrder(String orderNumber) {
		String JPQL = "delete from Orders o where o.orderNumber=:orderNumber";

		EntityManager manager = emf.createEntityManager();
		EntityTransaction transaction = manager.getTransaction();
		transaction.begin();
		Query query = manager.createQuery(JPQL);
		query.setParameter("orderNumber", orderNumber);
		int i = query.executeUpdate();
		transaction.commit();
		if (i != 0) {
			return true;
		}
		throw new NotFoundException();
	}

	/**
	 * This method is use to get all orders record.
	 * 
	 * @return orderList if orderList is not empty else throw new
	 *         EmptyRecordException().
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Orders> getAllOrders() {
		String JPQL = "select o from Orders o";
		EntityManager manager = emf.createEntityManager();
		Query query = manager.createQuery(JPQL);
		List<Orders> orderList = query.getResultList();
		manager.close();
		if (orderList.isEmpty()) {
			throw new EmptyRecordException();
		} else {
			return orderList;
		}
	}

}
