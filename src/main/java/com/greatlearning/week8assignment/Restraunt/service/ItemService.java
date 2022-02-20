package com.greatlearning.week8assignment.Restraunt.service;

import java.util.List;

import com.greatlearning.week8assignment.Restraunt.model.Item;

public interface ItemService {
	List<Item> getAllItems();

	Item findItemById(long id);

	Item save(Item item);

	void delete(long id);
}
