package io.mountblue.BlogApplication.controller;

import io.mountblue.BlogApplication.dao.ServiceImplementation;
import io.mountblue.BlogApplication.entity.Post;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class SortController {
    private ServiceImplementation serviceImplementation;

    public SortController(ServiceImplementation serviceImplementation){
        this.serviceImplementation = serviceImplementation;
    }
    @GetMapping("/")
    public String index(@RequestParam(value = "sort", defaultValue = "newest") String sort, Model model) {

        List<Post> posts;
        if (sort.equals("newest")) {
            posts = serviceImplementation.getAllPostsSortedByDate();
        } else {
            posts = serviceImplementation.getAllPostsSortedByOldestDate();
        }
        model.addAttribute("posts", posts);
//        List<Post> posts = serviceImplementation.getAllPostsSortedByDate();
//        model.addAttribute("posts", posts);
//        return "landingPage";

//        List<Post> posts = serviceImplementation.showAllPosts();
//        model.addAttribute("posts", posts);
        return "landingPage";
    }
}
