package com.example.jwt_demo.cloud_webapp.controllers.admin.master;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.jwt_demo.cloud_webapp.forms.admin.master.UserForm;
import com.example.jwt_demo.common.spring.models.entities.User;
import com.example.jwt_demo.common.spring.services.UserService;

import lombok.extern.slf4j.Slf4j;;

@Slf4j
@Controller
@RequestMapping("/admin/master/users")
public class UserController {

	@Autowired
	UserService userService;

	@ModelAttribute
	UserForm setUpForm() {
		return new UserForm();
	}

	/**
	 * 一覧表示
	 */
	@GetMapping
	public String index(Model model) {
		log.info("/admin/master/users/index.html");
		List<User> users = userService.findAll();
		model.addAttribute("users", users);
		return "/admin/master/users/index";
	}

	/**
	 * 登録画面遷移
	 */
	@GetMapping("register")
	public String register(UserForm form, Model model) {
		log.info("admin/master/users/register.html");
		User user = new User();
		BeanUtils.copyProperties(form, user);
		return "/admin/master/users/register";
	}

	/**
	 * 登録画面 登録ボタン押下
	 */
	@PostMapping("register")
	public String register(@Validated UserForm form, BindingResult result, Model model) {
		log.info("register start");
		if (result.hasErrors()) {
			log.info("validation error");
			return register(form, model);
		}
		User user = new User();
		BeanUtils.copyProperties(form, user);
		Optional<User> userResult = userService.create(user.getName(), user.getPassword(), user.getEmailAddress(),
				user.isAdminFlag());
		log.info(userResult.toString());
		if (!userResult.isPresent()) {
			log.info("cannot create user");
			result.reject("", "cannot create user");
			return register(form, model);
		}
		log.info("register end");
		return "redirect:/admin/master/users";
	}

	/**
	 * 編集画面遷移
	 */
	@GetMapping("edit")
	public String editForm(@RequestParam Long userId, UserForm form) {
		log.info("/admin/master/users/edit.html");
		Optional<User> userResult = userService.getInculudeDeleted(userId);
		User user = userResult.get();
		user.setPassword(null);
		BeanUtils.copyProperties(user, form);
		return "/admin/master/users/edit";
	}

	/**
	 * 編集画面 更新ボタン押下
	 */
	@PostMapping("edit")
	public String edit(@RequestParam Long userId, @Validated UserForm form, BindingResult result) {
		if (result.hasErrors()) {
			return editForm(userId, form);
		}
		Optional<User> searchResult = userService.getInculudeDeleted(userId);
		User user = searchResult.get();
		BeanUtils.copyProperties(form, user);
		user.setUserId(userId);
		Optional<User> userResult = userService.update(user);
		if (!userResult.isPresent()) {
			result.reject("", "cannot edit user");
			return editForm(userId, form);
		}
		return "redirect:/admin/master/users";
	}

	/**
	 * 削除ボタン押下
	 */
	@PostMapping(path = "delete")
	public String delete(@RequestParam Long userId) {
		userService.delete(userId);
		return "redirect:/admin/master/users";
	}


	/**
	 * 復活ボタン(編集画面)
	 */
	@PostMapping(path = "restore")
	public String restore(@RequestParam Long userId, @Validated UserForm form, BindingResult result) {
		log.info("restore from editForm");
		Optional<User> userResult = userService.resotre(userId);
		if (!userResult.isPresent()) {
			result.reject("", "cannot restore user");
			return editForm(userId, form);
		}
		return "redirect:/admin/master/users";
	}


	/**
	 * 戻るボタン用
	 */
	@PostMapping(path = { "register", "edit" }, params = "return")
	public String returnList() {
		return "redirect:/admin/master/users";
	}
}
