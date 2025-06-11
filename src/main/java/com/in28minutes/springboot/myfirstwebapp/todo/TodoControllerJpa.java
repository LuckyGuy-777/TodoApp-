package com.in28minutes.springboot.myfirstwebapp.todo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import jakarta.validation.Valid;

@Controller
@SessionAttributes("name")
public class TodoControllerJpa {
	
	
	public TodoControllerJpa(TodoRepository todoRepository) {
		super();
		this.todoRepository = todoRepository;
	}
	
	private TodoRepository todoRepository;



	@RequestMapping("list-todos")
	public String listAllTodos(ModelMap model) {
		String username = getLoggedInUsername(model);
		
		List<Todo> todos = todoRepository.findByUsername(username);
		model.addAttribute("todos", todos);
		return "listTodos";
	}
	
	@RequestMapping(value = "add-todo", method = RequestMethod.GET)
	public String showNewTodoPage(ModelMap model) {
		String username = getLoggedInUsername(model);
		Todo todo = new Todo(0, username, "", LocalDate.now().plusYears(1), false);
		model.put("todo", todo);
		return "todo";
	}


	// 새로운 Todo를 추가할때 호출되는 메서드.
	@RequestMapping(value = "add-todo", method = RequestMethod.POST)
	public String addNewTodo(ModelMap model, @Valid Todo todo, BindingResult result) {
		
		if(result.hasErrors()) {
			return "todo";
		}
		
		String username = getLoggedInUsername(model);
		todo.setUsername(username);
		todoRepository.save(todo);
//		todoService.addTodo(username, todo.getDescription(),
//				todo.getTargetDate(), todo.isDone());
		return "redirect:list-todos";
	}
	
	@RequestMapping("delete-todo") // listTodos.jsp의 쿼리파라미터로 인해, id값이 전해짐.
	public String deleteTodo(@RequestParam int id ) {
		// Delete todo
		todoRepository.deleteById(id);
		return "redirect:list-todos";
	}
	
	
	// 현재 todo값이 표시되어있는상태의, 업데이트페이지 표시.
	@RequestMapping(value = "update-todo", method = RequestMethod.GET) // Todo 업데이트
	public String showUpdateTodoPage(@RequestParam int id, ModelMap model ) {
		Todo todo = todoRepository.findById(id).get(); // 레포에서, id값으로 세부정보를 받음
		model.addAttribute("todo", todo);
		return "todo";
	}
	
	// 업데이트 페이지에서, submit을 클릭하면 호출되는 메서드
	@RequestMapping(value = "update-todo", method = RequestMethod.POST)
	public String updateTodo(ModelMap model, @Valid Todo todo, BindingResult result) {
		
		if(result.hasErrors()) {
			return "todo";
		}
		
		String username = getLoggedInUsername(model);
		todo.setUsername(username);
		todoRepository.save(todo);  // todo 입력방식
		return "redirect:list-todos";
	}
	
	private String getLoggedInUsername(ModelMap model) {
		Authentication authentication = 
				SecurityContextHolder.getContext().getAuthentication(); // spring security 로 로그인정보 받기
		return authentication.getName();
	}
	
}
