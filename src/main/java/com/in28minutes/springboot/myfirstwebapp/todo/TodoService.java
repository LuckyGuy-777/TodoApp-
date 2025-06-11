package com.in28minutes.springboot.myfirstwebapp.todo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.springframework.stereotype.Service;

import jakarta.validation.Valid;

@Service
public class TodoService {
	
	// 정적 리스트 로직.
	private static List<Todo> todos = new ArrayList<>();
	private static int todosCount = 0;
	
	static {
		todos.add(new Todo(++todosCount, "in28minutes","Get Learn Certified 1",
				LocalDate.now().plusYears(1),false));
		
		todos.add(new Todo(++todosCount, "in28minutes","Learn DevOps 1",
				LocalDate.now().plusYears(2),false));
		
		todos.add(new Todo(++todosCount, "in28minutes","Learn Full Stack Development 1",
				LocalDate.now().plusYears(3),false));
		
	}
	
	public List<Todo> findByUsername(String username){
		Predicate<? super Todo> predicate =
				todo -> todo.getUsername().equalsIgnoreCase(username);
		return todos.stream().filter(predicate).toList();
	}

//	ＴＯＤＯ 추가 로직 
	public void addTodo(String username, String description,
			LocalDate targetDate, boolean done) {
		Todo todo = new Todo(++todosCount,username,description,targetDate,done);
		todos.add(todo);
		 // 새로운 Todo 추가시, ++todosCount
	}
	
	public void deleteById(int id) {
		// todo.getId() == id
		// 람다 함수 사용하려면? Bean이름 -> 조건. ex:  todo -> todo.getId() == id
		Predicate<? super Todo> predicate = todo -> todo.getId() == id;
		todos.removeIf(predicate);
	}

	public Todo findById(int id) {
		Predicate<? super Todo> predicate =todo -> todo.getId() == id;
		Todo todo = todos.stream().filter(predicate).findFirst().get();
		return todo;
	}

	public void updateTodo(@Valid Todo todo) {
		deleteById(todo.getId());
		todos.add(todo);
	}
	
}
