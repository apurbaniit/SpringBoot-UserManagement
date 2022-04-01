package com.User.Management.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.User.Management.dao.NormalRepository;
import com.User.Management.dao.UserRepository;
import com.User.Management.entities.Normal;
import com.User.Management.entities.User;
import com.User.Management.helper.Message;

@Controller
public class HomeController {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private NormalRepository normalRepository;
	
	
	@RequestMapping("/")
	public String home(Model model)
	{
		model.addAttribute("title","Home Contact Management");
		return "home";
	}
	
	@RequestMapping("/aboutus")
	public String about(Model model)
	{
		model.addAttribute("title","About Us");
		return "aboutus";
	}
	
	@RequestMapping("/contactus")
	public String contact(Model model)
	{
		model.addAttribute("title","Contact Us");
		return "contactus";
	}
	
	@RequestMapping("/register")
	public String Register(Model model)
	{
		model.addAttribute("title","Register Contact Management");
		model.addAttribute("user",new User());
		return "register";
	}
	
	//handler for custom login
	@GetMapping("/signin")
	public String customLogin(Model model)
	{
		model.addAttribute("title","Login Page");
		return "login";
	}
	
	
	//handle the register user
	@RequestMapping(value="/do_register",method = RequestMethod.POST)	
	public String registerUser(@Valid @ModelAttribute("user") User user,BindingResult results,@RequestParam(value="aggrement", defaultValue = "false") boolean aggrement,
			Model model, HttpSession session)
	{
		try {
			
			if(!aggrement)
			{
				System.out.println("You have not agreed the Terms and Conditions");
				throw new Exception("You have not agreed the Terms and Conditions");
			}
			
			if(results.hasErrors())
			{
				System.out.println("Error !!"+results.toString());
				model.addAttribute("user",user); 
				return "register";
			}
			
			
			
			System.out.println("agreement : "+aggrement);
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			user.setImagesUrl("profile.jpg");
			user.setEnabled(true);
			user.setRole("ROLE_USER");

			System.out.println("Before Register User : "+user);
			//User result = userService.userRegister(user);
			User result = userRepository.save(user);
			System.out.println("After Registered User : "+result);
		
			// after successfully registered,  form data must return empty  
			User emptyUser = new User();
			model.addAttribute("user", emptyUser);
			session.setAttribute("message", new Message("Registration successfully", "alert-success"));

		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			model.addAttribute("user",user);
			session.setAttribute("message", new Message("something went wrong!!"+ex.getMessage(),"alert-danger"));
			return "register";
		}
		return "register";
		
	}
	
	
	@PostMapping("/do_contact")
	public String contactUs(Model model,@ModelAttribute("normal") Normal normal,HttpSession session)
	{
		try {
		model.addAttribute("title","Contact US");
		normal.setF_name(normal.getF_name());
		normal.setL_name(normal.getL_name());
		normal.setEmail(normal.getEmail());
		normal.setPhone(normal.getPhone());
		normal.setCountry(normal.getCountry());
		
		Normal data=normalRepository.save(normal);
		
		 System.out.println("Data Send to our team we will get back to you");
		session.setAttribute("message",new Message("Your Contact US is Updated..","success"));	 
		
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			 System.out.println("Something wrong");
			session.setAttribute("message",new Message("Your Contact US  is NOT Updated..","danger"));
			return "home";
		}
		return "contactus";
	}
}
