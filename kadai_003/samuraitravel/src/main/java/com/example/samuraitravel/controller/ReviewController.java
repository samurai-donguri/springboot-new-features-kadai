package com.example.samuraitravel.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.Review;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.form.ReviewEditForm;
import com.example.samuraitravel.form.ReviewRegisterForm;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.repository.ReviewRepository;
import com.example.samuraitravel.security.UserDetailsImpl;
import com.example.samuraitravel.service.ReviewService;

@Controller
@RequestMapping("/reviews")
public class ReviewController {
	private final HouseRepository houseRepository;        
	private final ReviewRepository reviewRepository;        
	private final ReviewService reviewService;        
    
    public ReviewController(HouseRepository houseRepository, ReviewRepository reviewRepository, ReviewService reviewService) {
        this.houseRepository = houseRepository;            
        this.reviewRepository = reviewRepository;            
        this.reviewService = reviewService;            
    }

    @GetMapping("/{id}")
    public String index(@PathVariable(name = "id") Integer id, Model model, @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable, @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
    	if (isAuthenticated()) {
            User user = userDetailsImpl.getUser();
            model.addAttribute("user", user);
    	}

    	House house = houseRepository.getReferenceById(id);
        model.addAttribute("house", house);

       	Page<Review> reviewPage = reviewRepository.findByHouseOrderByCreatedAtDesc(house, pageable);
        
        model.addAttribute("reviewPage", reviewPage);         

        return "reviews/index";
    }

    @GetMapping("/{id}/register")
    public String register(@PathVariable(name = "id") Integer id, Model model, @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        User user = userDetailsImpl.getUser();
       	House house = houseRepository.getReferenceById(id);
        
        model.addAttribute("user", user);
        model.addAttribute("house", house);

    	model.addAttribute("reviewRegisterForm", new ReviewRegisterForm());
        return "reviews/register";
    }

    @PostMapping("/{id}/register")
    public String register(@PathVariable(name = "id") Integer id, Model model, @ModelAttribute @Validated ReviewRegisterForm reviewRegisterForm, BindingResult bindingResult, RedirectAttributes redirectAttributes, @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        User user = userDetailsImpl.getUser();
       	House house = houseRepository.getReferenceById(id);
        
        model.addAttribute("user", user);
        model.addAttribute("house", house);

    	if (bindingResult.hasErrors()) {
            return "reviews/register";
        }
        
        reviewService.create(reviewRegisterForm);
        redirectAttributes.addFlashAttribute("successMessage", "レビューを投稿しました。");

        return "redirect:/houses/" + id;
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable(name = "id") Integer id, Model model) {
    	Review review = reviewRepository.getReferenceById(id);
        ReviewEditForm reviewEditForm = new ReviewEditForm(review.getId(), review.getHouse().getId(), review.getUser().getId(), review.getRate(), review.getComment());
        
        model.addAttribute("review", review);
        model.addAttribute("reviewEditForm", reviewEditForm);
        return "reviews/edit";
    }

    @PostMapping("/{id}/edit")
    public String edit(@PathVariable(name = "id") Integer id, Model model, @ModelAttribute @Validated ReviewEditForm reviewEditForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
    	Review review = reviewRepository.getReferenceById(id);
        
        model.addAttribute("review", review);

    	if (bindingResult.hasErrors()) {
            return "reviews/edit";
        }
        
        reviewService.update(reviewEditForm);
        redirectAttributes.addFlashAttribute("successMessage", "レビューを更新しました。");

        return "redirect:/houses/" + review.getHouse().getId();
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {        
    	Review review = reviewRepository.getReferenceById(id);

    	reviewRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "レビューを削除しました。");
        
        return "redirect:/houses/" + review.getHouse().getId();
    }

    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }
}
