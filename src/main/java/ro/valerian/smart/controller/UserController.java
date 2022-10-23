package ro.valerian.smart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ro.valerian.smart.dao.UserRepository;
import ro.valerian.smart.entities.Contact;
import ro.valerian.smart.entities.User;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    //method
    @ModelAttribute
    public void addCommonData(Model model, Principal principal) {

        String userName = principal.getName();
        System.out.println("USERNAME " + userName);

        User user = userRepository.getUserByUsername(userName);
        System.out.println("USER " + user);

        model.addAttribute("user", user);

    }

    private User user;

    //dashboard home
    @RequestMapping("/index")
    public String dashboard(Model model, Principal principal) {

        return "normal/user_dashboard";
    }

    //open add from controller
    @GetMapping("/add-contact")
    public String openAddContactForm(Model model) {

        model.addAttribute("title", "Add Contact");
        model.addAttribute("contact", new Contact());

        return "normal/add_contact_form";
    }

    //processing add contact form
    @PostMapping("/process-contact")
    public String processContact(
            @ModelAttribute Contact contact,
            @RequestParam("profileImage") MultipartFile file,
            Principal principal) {
        try {

            String name = principal.getName();
            User user = this.userRepository.getUserByUsername(name);

            //processing adn uploading file...
            if (file.isEmpty()) {
                //if the file is empty then try our image
                System.out.println("File is empty");

            } else {
                //file the file to folder and update the name to contact
                contact.setImage(file.getOriginalFilename());

                File saveFile = new ClassPathResource("static/img").getFile();

                Path path=Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
                Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Image is uploaded");
            }

            user.getContacts().add(contact);

            contact.setUser(user);


            this.userRepository.save(user);
            System.out.println("DATA " + contact);

            System.out.println("Added to data base");
        } catch (Exception e) {
            System.out.println("ERROR " + e.getMessage());
            e.printStackTrace();
        }
        return "normal/add_contact_form";
    }
}
