package com.amarDokan.amarDokan.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

import com.amarDokan.amarDokan.dto.request.UserRequestDto;
import com.amarDokan.amarDokan.models.Category;
import com.amarDokan.amarDokan.models.Product;
import com.amarDokan.amarDokan.models.User;
import com.amarDokan.amarDokan.service.CartService;
import com.amarDokan.amarDokan.service.CategoryService;
import com.amarDokan.amarDokan.service.ProductService;
import com.amarDokan.amarDokan.service.UserService;
import com.amarDokan.amarDokan.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import io.micrometer.common.util.StringUtils;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommonUtil commonUtil;

    //Comment out for testing purpose
//    @Autowired
//    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private CartService cartService;

    @Value("${image.upload.path:uploads/img}")
    private String uploadPath;

    @ModelAttribute
    public void getUserDetails(Principal p, Model m) {
        if (p != null) {
            String email = p.getName();
            User userDtls = userService.getUserByEmail(email);
            if (userDtls != null) {
                m.addAttribute("user", userDtls);

                Integer countCart = cartService.getCountCart(userDtls.getId());
                m.addAttribute("countCart", countCart);
            }
        }

        List<Category> allActiveCategory = categoryService.getAllActiveCategory();
        m.addAttribute("categorys", allActiveCategory);
    }

    @GetMapping("/")
    // @ResponseBody //Remove this when sending html file
    public String index(Model m) {

        List<Category> allActiveCategory = categoryService.getAllActiveCategory().stream()
                .sorted((c1, c2) -> c2.getId().compareTo(c1.getId())).limit(6).toList();
        List<Product> allActiveProducts = productService.getAllActiveProducts("").stream()
                .sorted((p1, p2) -> p2.getId().compareTo(p1.getId())).limit(8).toList();
        m.addAttribute("category", allActiveCategory);
        m.addAttribute("products", allActiveProducts);
        return "index";
        // return "Hello World ! This is Home";
    }

    //  @ResponseBody
    @GetMapping({"/signin", "/login"})
    public String login() {
        return "login";
        //  return "Login page is here";
    }

    //  @ResponseBody
    @GetMapping("/register")
    public String register() {
        return "register";
        //  return "registration page is here";
    }

    @GetMapping("/products")
    public String products(Model m, @RequestParam(value = "category", defaultValue = "") String category,
            @RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "12") Integer pageSize,
            @RequestParam(defaultValue = "") String ch) {

        List<Category> categories = categoryService.getAllActiveCategory();
        m.addAttribute("paramValue", category);
        m.addAttribute("categories", categories);

//		List<Product> products = productService.getAllActiveProducts(category);
//		m.addAttribute("products", products);
        Page<Product> page = null;
        if (StringUtils.isEmpty(ch)) {
            page = productService.getAllActiveProductPagination(pageNo, pageSize, category); 
        }else {
            page = productService.searchActiveProductPagination(pageNo, pageSize, category, ch);
        }

        List<Product> products = page.getContent();
        m.addAttribute("products", products);
        m.addAttribute("productsSize", products.size());

        m.addAttribute("pageNo", page.getNumber());
        m.addAttribute("pageSize", pageSize);
        m.addAttribute("totalElements", page.getTotalElements());
        m.addAttribute("totalPages", page.getTotalPages());
        m.addAttribute("isFirst", page.isFirst());
        m.addAttribute("isLast", page.isLast());

        return "product";
    }

    @GetMapping("/product/{id}")
    public String product(@PathVariable Long id, Model m) {
        Product productById = productService.getProductById(id);
        m.addAttribute("product", productById);
        return "view_product";
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute UserRequestDto userRequestDto, @RequestParam("img") MultipartFile file, HttpSession session)
            throws IOException {

        Boolean existsEmail = userService.existsEmail(userRequestDto.getEmail());

        if (existsEmail) {
            session.setAttribute("errorMsg", "Email already exist"); 
        }
        else {
            String imageName = file.isEmpty() ? "default.jpg" : file.getOriginalFilename();
            User savedUser = userService.saveUserFromDto(userRequestDto, imageName);

            if (!ObjectUtils.isEmpty(savedUser)) {
                if (!file.isEmpty()) {
                    File saveFile = new File(uploadPath);
                    if (!saveFile.exists()) 
                        saveFile.mkdirs();

                    Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "profile_img" + File.separator
                            + file.getOriginalFilename());

                    File imgFolder = path.getParent().toFile();
                    if (!imgFolder.exists()) {
                        imgFolder.mkdirs();
                    }

                    Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                }
                session.setAttribute("succMsg", "Register successfully");
            } 
            else 
                session.setAttribute("errorMsg", "something wrong on server");
        }

        return "redirect:/register";
    }

    //	Forgot Password Code
    @GetMapping("/forgot-password")
    public String showForgotPassword() {
        return "forgot_password.html";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam String email, HttpSession session, HttpServletRequest request)
            throws UnsupportedEncodingException, MessagingException {

        User userByEmail = userService.getUserByEmail(email);

        if (ObjectUtils.isEmpty(userByEmail)) {
            session.setAttribute("errorMsg", "Invalid email"); 
        }else {

            String resetToken = UUID.randomUUID().toString();
            userService.updateUserResetToken(email, resetToken);

            // Generate URL :
            // http://localhost:8080/reset-password?token=sfgdbgfswegfbdgfewgvsrg
            String url = CommonUtil.generateUrl(request) + "/reset-password?token=" + resetToken;

            Boolean sendMail = commonUtil.sendMail(url, email);

            if (sendMail) {
                session.setAttribute("succMsg", "Please check your email..Password Reset link sent"); 
            }else {
                session.setAttribute("errorMsg", "Somethong wrong on server ! Email not send");
            }

        }

        return "redirect:/forgot-password";
    }

    @GetMapping("/reset-password")
    public String showResetPassword(@RequestParam String token, HttpSession session, Model m) {

        User userByToken = userService.getUserByToken(token);

        if (userByToken == null) {
            m.addAttribute("msg", "Your link is invalid or expired !!");
            return "message";
        }
        m.addAttribute("token", token);
        return "reset_password";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String token, @RequestParam String password, HttpSession session,
            Model m) {

        User userByToken = userService.getUserByToken(token);
        if (userByToken == null) {
            m.addAttribute("errorMsg", "Your link is invalid or expired !!");
            return "message";
        } else {
//            userByToken.setPassword(passwordEncoder.encode(password));
//            userByToken.setResetToken(null);
//            userService.updateUser(userByToken);
            // session.setAttribute("succMsg", "Password change successfully");
            m.addAttribute("msg", "Password change successfully");

            return "message";
        }

    }

    @GetMapping("/search")
    public String searchProduct(@RequestParam String ch, Model m) {
        List<Product> searchProducts = productService.searchProduct(ch);
        m.addAttribute("products", searchProducts);
        List<Category> categories = categoryService.getAllActiveCategory();
        m.addAttribute("categories", categories);
        return "product";

    }

}
