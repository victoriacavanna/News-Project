package com.news.project.controllers;

import com.news.project.entities.News;
import com.news.project.exceptions.MyException;
import com.news.project.services.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/news")
public class NewsController {
    @Autowired
    private NewsService newsService;

    @GetMapping("/inicio")
    public String indexNews(ModelMap model){
        listPostsTable(model);
        listPosts(model);
        return "index.html";
    }
    @GetMapping("/administracion")
    public String uploadNews(ModelMap model){
        listPostsTable(model);
        model.addAttribute("modalNews", new News());
        return "panelAdmin.html";
    }
    @GetMapping("/galeria")
    public String newsGallery(ModelMap model){
        listPostsTable(model);
        return "gallery.html";
    }
    @PostMapping("/upload")
    public String submitNews(@RequestParam String title, String body, String imgUrl, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate publishedDate, ModelMap model){
        try{
            newsService.createNews(title, body, imgUrl, publishedDate);
            model.put("success", "La noticia se agregó correctamente");
            return "redirect:./administracion";
        } catch (MyException ex){
            model.put("error", ex.getMessage());
            return "redirect:./administracion";
        }
    }

    @GetMapping("/list")
    public String listPosts(ModelMap model){
        List<News> newsList = newsService.listNews();
        model.addAttribute("newsList", newsList);
        return "index.html";
    }

    public void listPostsTable(ModelMap model){
        List<News> newsList = newsService.listNews();
        model.addAttribute("newsList", newsList);
    }


    @GetMapping("/edit/{id}")
    public String modifyNews(@PathVariable String id, ModelMap model){
        model.put("news", newsService.getOne(id));
        return "modifyNews.html";
    }

    @PostMapping ("/modify/{id}")
    public String modifyNews(@PathVariable String id, String title, String body, String imgUrl,@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate publishedDate, ModelMap model ){
        try{
            newsService.modifyNews(title, body, imgUrl, publishedDate, id);
            return "redirect:../administracion";
        } catch (MyException ex){
            model.put("error", ex.getMessage());
            return "modifyNews.html";
        }
    }


    @PostMapping("/delete/{id}")
    public String deleteNews(@PathVariable String id, ModelMap model) {
        newsService.deleteNews(id);
        return "redirect:../administracion";
    }
}