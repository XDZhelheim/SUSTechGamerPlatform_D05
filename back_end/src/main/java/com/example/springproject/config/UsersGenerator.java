package com.example.springproject.config;

import com.example.springproject.service.GameService;
import com.example.springproject.service.GameUserService;
import com.example.springproject.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController("/OOAD")
public class UsersGenerator {
    public static UsersGenerator Services;

    public UsersGenerator() {
        Services = this;
    }


    @Autowired
    public UsersService usersService;
    @Autowired
    public GameService gamesService;
    @Autowired
    public GameUserService gameUserService;

    @RequestMapping("/users")
    public void hello(){
    }
//    public boolean checkLogin(Users users) {
//        return usersService.checkLogin(users);
//    }
//
//    public void save(Users users){
//        usersService.save(users);
//    }
//
//    public Users get_user(String user_name){
//        return usersService.findByUsername(user_name);
//    }
//
//    public void game_save(Game game){
//        gamesService.save(game);
//    }
//
//    public void game_del(Game game){
//        gamesService.delete(game);
//    }
//
//    public List<Game> game_get(){
//        return gamesService.getAllGame();
//    }
//
//    public Game get_game(String name){
//        return gamesService.getGame(name);
//    }
//
//    public void save_game(GameUser gameUser){
//        gameUserService.save(gameUser);
//    }
//
//    public GameUser get_gu(Users u,Game g){
//        return gameUserService.getRecord(u,g);
//    }








}
