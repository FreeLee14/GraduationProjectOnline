package cn.com.tjise.onlineedu.controller;

import cn.com.tjise.onlineedu.entity.dto.R;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 文件上传控制器
 *
 * @author admin
 */
@RestController
@CrossOrigin
@Slf4j
@RequestMapping("/onlineedu/upload")
public class FileUploadController
{
    @PostMapping("userAvatar")
    public R uploadUserAvatar(
        @ApiParam(name = "avatar", value = "用户头像流", required = true)
        @RequestParam("avatar") MultipartFile uploadFile
    ) throws FileNotFoundException
    {
        //获得resources路径
        String path = System.getProperty("user.dir");
        path += "\\src\\main\\resources";
        //空文件夹在编译时不会打包进入target中
        File uploadDir = new File(path + "\\static\\user\\avatar");
        if (!uploadDir.exists())
        {
            log.info("上传头像路径不存在，正在创建...");
            uploadDir.mkdir();
        }
        if (uploadFile != null)
        {
            //获得上传文件的文件名
            String oldName = uploadFile.getOriginalFilename();
            log.info("上传的文件名" + oldName);
            //我的文件保存在static目录下的user/avatar
            assert oldName != null;
            File avatar = new File(path + "/static/user/avatar/", oldName);
            if (avatar.exists())
            {
                return R.ok().data("avatarUrl", "/user/avatar/" + oldName);
            }
            try
            {
                //保存图片
                uploadFile.transferTo(avatar);
                //返回成功结果，附带文件的相对路径
                return R.ok().data("avatarUrl", "/user/avatar/" + oldName);
            }
            catch (IOException e)
            {
                e.printStackTrace();
                return R.error().message("上传失败！！");
            }
        }
        else
        {
            return R.error().message("上传的文件为空！！");
        }
    }
}
