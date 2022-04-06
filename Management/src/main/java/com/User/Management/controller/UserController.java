package com.User.Management.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import com.User.Management.helper.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.User.Management.dao.ContactRepository;
import com.User.Management.dao.UserRepository;
import com.User.Management.entities.Contact;
import com.User.Management.entities.User;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ContactRepository contactRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	// method for heading common data for response

	@ModelAttribute
	public void addCommonData(Model model, Principal principal) {
		String username = principal.getName();
		System.out.println("USERNAME   " + username);
		User user = this.userRepository.getUserByUserName(username);
		System.out.println("User " + user);
		model.addAttribute("user", user);
	}

	// dash board home
	@RequestMapping("/index")
	public String dashboard(Model model, Principal principal) {
		model.addAttribute("title", "User Dashboard");
		return "normal/user_dashboard";

	}

	// open add form add form handler
	@GetMapping("/add_contact")
	public String openAddContactHandler(Model model) {
		model.addAttribute("title", "Add Contact");
		model.addAttribute("contact", new Contact());

		return "normal/add_Contact_form";

	}

	// processing add contact form
	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute Contact contact, @RequestParam("ProfileImage") MultipartFile file,
			Principal principal, HttpSession session) {
		try {
			String name = principal.getName();
			User user = this.userRepository.getUserByUserName(name);

			// processing and uploading images file
			if (file.isEmpty()) {
				// if the file is empty
				System.out.println("File is empty");
				contact.setImageUrl("NoImage.jpg");
			} else {
				// file to folder update
				contact.setImageUrl(file.getOriginalFilename());
				File savefile = new ClassPathResource("static/img").getFile();
				Path path = Paths.get(savefile.getAbsolutePath() + File.separator + file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				System.out.println("image is uploaded");
			}

			contact.setUser(user);

			user.getContacts().add(contact);
			this.userRepository.save(user);
			System.out.println("Contact added in database");

			// message success ........
			session.setAttribute("message", new Message("!! Your Contact has been Added !! ", "success"));

		} catch (Exception ex) {
			System.out.println("Exception Occered !!" + ex.getMessage());
			ex.printStackTrace();
			// message error.......
			session.setAttribute("message", new Message("!! Something Went Wrong Please Try again !! ", "danger"));

		}

		// System.out.println("Data "+contact);
		return "normal/add_Contact_form";
	}

	// show context handler
	// pagination per page 3 contact //couurent page 0 [pages]
	@GetMapping("show_contacts/{page}")
	public String showContacts(@PathVariable("page") Integer page, Model model, Principal principal) {
		model.addAttribute("title", "Show User Contact");
		/*
		 * //normal technique 
		 * String username=principal.getName(); 
		 * User user=this.userRepository.getUserByUserName(username); 
		 * List<Contact> contacts=user.getContacts();
		 */

		String username = principal.getName();
		User user = this.userRepository.getUserByUserName(username);

		// current-page --page
		// contact-perpage --5
		Pageable pageable = PageRequest.of(page, 5);
		Page<Contact> contacts = this.contactRepository.findContactByUser(user.getId(), pageable);

		model.addAttribute("contacts", contacts);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", contacts.getTotalPages());
		// System.out.println(contacts);

		return "normal/show_contacts";
	}

	// showing specific contact details

	@RequestMapping("/{cid}/contact")
	public String showContactDetails(@PathVariable("cid") Integer cid, Model model, Principal principal) {
		System.out.println("Contact ID " + cid);
		Optional<Contact> contactOptional = this.contactRepository.findById(cid);
		Contact contact = contactOptional.get();

		// security check
		String username = principal.getName();
		User user = this.userRepository.getUserByUserName(username);

		if (user.getId() == contact.getUser().getId()) {

			model.addAttribute("contact", contact);
			model.addAttribute("title", contact.getF_name() + " information");
		} 
		else {
			model.addAttribute("message", new Message("You are not an authorized user for this contact", "danger"));
		}

		return "normal/contact_details";
	}


	// delete contact handler
	@GetMapping("/delete/{cid}")
	public String deleteContact(@PathVariable("cid") Integer cid, Principal principal, Model model,HttpSession session ) {
		
		/** first take current login user */
		String username=principal.getName();
		User user=this.userRepository.getUserByUserName(username);
	
		/** take contact details by using its ID */
		Contact contact=this.contactRepository.findById(cid).get();
		
		/** compare both userID for avoiding url miss-leading purpose  */
		if(user.getId() == contact.getUser().getId()) {
			try {
			//contact.setUser(null);
			/**Remove contact from given user List*/
			//this.contactRepository.delete(contact);
			
				user.getContacts().remove(contact);
				this.userRepository.save(user);
				
				System.out.println("Deleted");
			session.setAttribute("message", new Message("Contact Deleted Successfully..","success"));	
			
			 //remove image from folder (target folder)
			File deleteFile = new ClassPathResource("static/img").getFile();
			File file1 =new File(deleteFile,contact.getImageUrl());
			file1.delete();
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
			}
		else {
			model.addAttribute("message", new Message("You are not an authorized user for this contact", "danger"));
		}
			
		return "redirect:/user/show_contacts/0";

	}
	
	//update form handler
	@PostMapping("/update_contact/{cid}")
	public String updateForm(@PathVariable("cid") Integer cid,Model model)
	{
		model.addAttribute("title","Update Contact");
		Contact contact=this.contactRepository.findById(cid).get();
		model.addAttribute("contact",contact);
		
		return "normal/update_form";
	}
	
	//update contact handler
	@RequestMapping(value = "/process-update",method = RequestMethod.POST)
	public String updateHandler(@ModelAttribute Contact contact, @RequestParam("profileImage") MultipartFile file,Model model,HttpSession session,Principal principal)
	{
		
		try
		{
			//old contact details
			
			Contact oldcontactdetails= this.contactRepository.findById(contact.getCid()).get();
			
			if(!file.isEmpty())
			{
				//file work delete old photo
				
				File deleteFile = new ClassPathResource("static/img").getFile();
				File file1 =new File(deleteFile,oldcontactdetails.getImageUrl());
				file1.delete();
				
				
				
				//upload new photo
				File savefile = new ClassPathResource("static/img").getFile();
				Path path = Paths.get(savefile.getAbsolutePath() + File.separator + file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				contact.setImageUrl(file.getOriginalFilename());
					
			}
			else
			{
				contact.setImageUrl(oldcontactdetails.getImageUrl());
			}
			
			User user=this.userRepository.getUserByUserName(principal.getName());
			contact.setUser(user);
			this.contactRepository.save(contact);
			session.setAttribute("message",new Message("Your Contact is Updated..","success"));
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		System.out.println("Contact Name "+contact.getF_name());
		System.out.println("Contact Id "+contact.getCid());
		return "redirect:/user/"+contact.getCid()+"/contact";
	}
	
	//Your Profile handler
	@GetMapping("/profile")
	public String yourProfile(Model model)
	{
	
		model.addAttribute("title","Profile Page");
		
		return "normal/profile";
	}
	
	
	//open setting handler
	@GetMapping("/settings")
	public String openSettings()
	{
		
		return "normal/settings";
	}
	
	
	//change password
	
	@PostMapping("/change_password")
	public String changePassword(@RequestParam("oldpassword") String oldpassword, @RequestParam("newpassword") String newpassword,Principal principal,HttpSession session )
	{
		System.out.println("Old Password "+oldpassword);
		
		System.out.println("New Password "+newpassword);

		String userName=principal.getName();
		User currentUser=this.userRepository.getUserByUserName(userName);
		System.out.println("Encoded Password is "+currentUser.getPassword());
		
		if(this.bCryptPasswordEncoder.matches(oldpassword, currentUser.getPassword()))
		{
			currentUser.setPassword(this.bCryptPasswordEncoder.encode(newpassword));
			this.userRepository.save(currentUser);
			session.setAttribute("message",new Message("Your Password is successfully Changed..","success"));
		}
		else
		{
			System.out.println("Password Error!!!");
			session.setAttribute("message",new Message("Your Password is NOT Correct..","danger"));
			return "redirect:/user/settings";
		}
		 
		return "redirect:/user/index";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
