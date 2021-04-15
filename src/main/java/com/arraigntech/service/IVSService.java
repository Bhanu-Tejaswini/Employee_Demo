package com.example.demo.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.demo.Exception.DataExistsException;
import com.example.demo.Exception.DataNotFoundException;

public interface IVSService<T, ID> {

	public Page<T> getPaginated(Integer page, Integer limit);

	public List<T> getAll() throws DataNotFoundException;

	public T create(T entity);

	public T update(T entity) throws DataExistsException;

	public boolean delete(ID id) throws DataNotFoundException;

	public T getById(String id) throws DataNotFoundException;
}
