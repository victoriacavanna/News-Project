package com.news.project.services;

import com.news.project.entities.News;
import com.news.project.exceptions.MyException;
import com.news.project.repositories.NewsRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class NewsService {
    @Autowired
    NewsRepository newsRepository;

    @Transactional
    public void createNews(String title, String body, String imgUrl, LocalDate publishedDate) throws MyException{
        validate(title, body, publishedDate);
        News news = new News();
        news.setTitle(title);
        news.setBody(body);
        news.setPublishedDate(publishedDate);
        news.setImgUrl(imgUrl);
        newsRepository.save(news);
    }

    public List<News> listNews(){
        List<News> newsList = new ArrayList<>();
        newsList = newsRepository.findAll();
        return newsList;
    }
    @Transactional
    public void modifyNews(String title, String body,String imgUrl, LocalDate publishedDate, String id) throws MyException {
        validate(title, body, publishedDate);
        if (id == null || id.isEmpty()){
            throw new MyException("Id no puede ser nulo o vacío");
        }

        Optional<News> response = newsRepository.findById(id);
        if (response.isPresent()){
            News news = response.get();
            news.setTitle(title);
            news.setBody(body);
            news.setPublishedDate(publishedDate);
            news.setImgUrl(imgUrl);
            newsRepository.save(news);
        }
    }

    @Transactional
    public void deleteNews(String id){
        newsRepository.deleteById(id);
    }
    public News getOne(String id){
        return newsRepository.getOne(id);
    }
    public void validate(String title, String body, LocalDate publishingDate) throws MyException {
        if(title == null || title.isEmpty() || body == null || publishingDate == null){
            throw new MyException("Los valores no pueden ser nulos");
        }
    }
}