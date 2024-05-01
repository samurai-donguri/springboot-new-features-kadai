package com.example.samuraitravel.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.samuraitravel.entity.Favorite;
import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.repository.FavoriteRepository;

@Service
public class FavoriteService {
	private final FavoriteRepository favoriteRepository;  
    
    public FavoriteService(FavoriteRepository favoriteRepository) {
        this.favoriteRepository = favoriteRepository;  
    }    

    @Transactional
    public Favorite create(User user, House house) {
    	Favorite favorite = new Favorite();

        favorite.setUser(user);
        favorite.setHouse(house);
        
        return favoriteRepository.save(favorite);
    }
}
