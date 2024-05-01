package com.example.samuraitravel.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.samuraitravel.entity.Favorite;
import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.repository.FavoriteRepository;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.security.UserDetailsImpl;
import com.example.samuraitravel.service.FavoriteService;

@Controller
@RequestMapping("/favorites")
public class FavoriteController {
	private final HouseRepository houseRepository;        
	private final FavoriteRepository favoriteRepository;        
	private final FavoriteService favoriteService;        

    public FavoriteController(HouseRepository houseRepository, FavoriteRepository favoriteRepository, FavoriteService favoriteService) {
        this.houseRepository = houseRepository;            
        this.favoriteRepository = favoriteRepository;            
        this.favoriteService = favoriteService;            
    }     

    @GetMapping
    public String index(@PageableDefault(page = 0, size = 10, sort = "house", direction = Direction.ASC) Pageable pageable, Model model, @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        User user = userDetailsImpl.getUser();

        Page<Favorite> favoritePage = favoriteRepository.findByUserOrderByHouseAsc(user, pageable);
        model.addAttribute("favoritePage", favoritePage);

        return "favorites/index";
    }

    @GetMapping("/{id}/add")
    public String add(@PathVariable(name = "id") Integer id, Model model, @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        User user = userDetailsImpl.getUser();
       	House house = houseRepository.getReferenceById(id);
        
        model.addAttribute("user", user);
        model.addAttribute("house", house);

        favoriteService.create(user, house);

        return "redirect:/houses/" + id;
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable(name = "id") Integer id, @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        User user = userDetailsImpl.getUser();
       	House house = houseRepository.getReferenceById(id);

    	favoriteRepository.deleteById(favoriteRepository.findByUserAndHouse(user, house).get(0).getId());

    	return "redirect:/houses/" + id;
    }
}
