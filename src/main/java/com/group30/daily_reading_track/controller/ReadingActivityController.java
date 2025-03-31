package com.group30.daily_reading_track.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.group30.daily_reading_track.model.ReadingActivity;
import com.group30.daily_reading_track.model.User;
import com.group30.daily_reading_track.service.ReadingActivityService;
import com.group30.daily_reading_track.service.UserService;


@Controller
@RequestMapping("/readingActivities")
public class ReadingActivityController {
    
    @Autowired
    private ReadingActivityService readingActivityService;

    @Autowired
    private UserService userService;

    /**
     * 显示指定用户的阅读日志管理页面
     * URL 示例：/readingActivities/manage?username=user123
     */
    @GetMapping("/manage")
    public  String createReadingActivity(@RequestParam String username, Model model){
        User user = userService.findByUsername(username);
        if (user == null){
            model.addAttribute("error", "User not found!");
            return "error";
        }
       List<ReadingActivity> readingActivities = readingActivityService.getActivityByUser(user);
       model.addAttribute("readingActivities", readingActivities);
       model.addAttribute("username", username);
       return "readingActivities";
    }

    /**
     * 处理创建阅读日志的表单提交
     * URL 示例：/readingActivities/create?username=user123
     */
    @PostMapping("/create")
    public String createReadingActivity(@RequestParam String username,
                                        @ModelAttribute ReadingActivity readingActivity,
                                        Model model){
        User user = userService.findByUsername(username);
        if (user == null) {
            model.addAttribute("error", "User not found");
            return "error";
        }
        readingActivity.setUser(user);
        readingActivityService.createActivity(readingActivity);
        return "redirect:/readingActivities/manage?username=" + username;               
    }

    @GetMapping("/update/{id}")
    public String showUpdatePage(@PathVariable("id") Long id, @RequestParam String username, Model model) {
        ReadingActivity activity = readingActivityService.getReadingActivityById(id);
        if (activity == null) {
            model.addAttribute("error", "Reading Activity not found");
            return "error";
        }
        model.addAttribute("activity", activity);
        model.addAttribute("username", username);
        return "updateReadingActivity"; // 对应更新日志的模板 updateReadingActivity.html
    }

    /**
     * 处理更新阅读日志的表单提交
     * URL 示例：/readingActivities/update/1
     */
    @PostMapping("/update/{id}")
    public String updateReadingActivity(@PathVariable("id") Long id,
    @RequestParam String username,
    @ModelAttribute("activity") ReadingActivity updatedActivity,
    BindingResult result){
        if (result.hasErrors()){
            return "updatedReadingActivity";
        }
        ReadingActivity existing = readingActivityService.getReadingActivityById(id);
        // 仅更新允许修改的字段
        existing.setTitle(updatedActivity.getTitle());
        existing.setAuthor(updatedActivity.getAuthor());
        existing.setContent(updatedActivity.getContent());
        existing.setReadingDate(updatedActivity.getReadingDate());
        existing.setTimeSpent(updatedActivity.getTimeSpent());
        readingActivityService.updateActivity(existing);
        // 更新后重定向到管理页面，username从现有日志中获取
        return "redirect:/readingActivities/manage?username=" + username;
    }

    /**
     * 处理删除阅读日志的请求（通过表单提交或链接调用）
     * URL 示例：/readingActivities/delete/1?username=user123
     */
    @PostMapping("/delete/{id}")
    public String deleteReadingLog(@PathVariable("id") Long id,
                                   @RequestParam String username, 
                                   Model model) {
        readingActivityService.deleteActivity(id);
        return "redirect:/readingActivities/manage?username=" + username;
    }

    /**
     * 处理搜索阅读日志的请求（根据标题模糊搜索）
     * URL 示例：/readingActivities/search?username=user123&title=Spring
     */
    @GetMapping("/search")
    public String searchReadingActivity(@RequestParam String username, 
                                        @RequestParam String title, 
                                        Model model) {
        User user = userService.findByUsername(username);
        if (user == null) {
            model.addAttribute("error", "User not found");
            return "error";
        }
        List<ReadingActivity> readingActivities = readingActivityService.searchActivity(user, title);
        model.addAttribute("readingActivities", readingActivities);
        model.addAttribute("username", username);
        return "readingActivities";
    }

                                        

//     // 查询指定用户阅读日志
//     @GetMapping
//     public ResponseEntity<?> getReadingActivity(@RequestParam String username){
//         User user = userService.findByUsername(username);
//         if (user == null){
//             return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
//         }
//         List<ReadingActivity> readingActivities = readingActivityService.getActivityByUser(user);
//         return ResponseEntity.ok(readingActivities); // 对应 resources/templates/readingActivities.html
//     }


//     // 通过id更新日志
//     @PostMapping("/{Id}")
//     public ResponseEntity<?> updateReadingActivity(@PathVariable Long Id, @RequestBody ReadingActivity readingActivity){
//         ReadingActivity existing = readingActivityService.getReadingActivityById(Id);
//         if (existing == null){
//             return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reading Activity not found!");
//         }
//         existing.setAuthor(readingActivity.getAuthor());
//         existing.setContent(readingActivity.getContent());
//         existing.setTitle(readingActivity.getTitle());
//         existing.setReadingDate(readingActivity.getReadingDate());
//         ReadingActivity update = readingActivityService.updateActivity(existing);
//         return ResponseEntity.ok(update);
//     }

//     // 删除阅读日志
//     @DeleteMapping("/{id}")
//     public ResponseEntity<?> deleteReadingLog(@PathVariable("id") Long id) {
//         readingActivityService.deleteActivity(id);
//         return ResponseEntity.ok("Deleted");
// }

//     // 搜索阅读日志
//     @GetMapping("/search")
//     public ResponseEntity<?> searchReadingActivity(@RequestParam String username, @RequestParam String title){
//         User user = userService.findByUsername(username);
//         if (user == null){
//             return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found user!");
//         }
//         List<ReadingActivity> readingActivities = readingActivityService.searchActivity(user, title);
//         return ResponseEntity.ok(readingActivities);
//     }
}
