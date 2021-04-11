package cn.com.tjise.onlineedu.controller;

import cn.com.tjise.onlineedu.entity.dto.R;
import cn.com.tjise.onlineedu.entity.po.Class;
import cn.com.tjise.onlineedu.entity.vo.classinfo.ClassFileVO;
import cn.com.tjise.onlineedu.service.ClassService;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private ClassService classService;
    
    @PostMapping("userAvatar")
    public R uploadUserAvatar(
        @ApiParam(name = "avatar", value = "用户头像流", required = true)
        @RequestParam("avatar") MultipartFile uploadFile
    ) throws FileNotFoundException
    {
        String avatarPath = FILE_ROOT_PATH + File.separator + "user" + File.separator + "avatar";
        //空文件夹在编译时不会打包进入target中
        File uploadDir = new File(avatarPath);
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
            File avatar = new File(avatarPath + File.separator, oldName);
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
    
    @PostMapping("uploadFiles")
    public R uploadFiles(ClassFileVO classFileVO) throws IOException
    {
        boolean flag = false;
        
        if (classFileVO.getFile() == null)
        {
            return R.error().message("上传失败");
        }
        // 获取文件名称
        String filename = classFileVO.getName();
        
        // 将文件持久化至本地
        String path = saveFileByNio(classFileVO.getFile(), filename);
        // 如果获取到的结果为null说明文件没有持久化到本地
        if (path != null)
        {
            String[] split = filename.split(FILE_SPLIT);
            String classIdName = split[0];
            String fileName = split[1];
            String fileRes = classIdName + File.separator + fileName;
            UpdateWrapper<Class> updateWrapper = new UpdateWrapper<>();
            updateWrapper.set("file_name", fileRes + ";");
            updateWrapper.eq("class_id", classIdName);
            boolean update = classService.update(updateWrapper);
            if (update)
            {
                flag = true;
            }
            else
            {
                flag = false;
            }
        }
        else
        {
            flag = false;
        }
        
        if (flag)
        {
            
            return R.ok().message("上传成功");
        }
        else
        {
            return R.error().message("上传失败");
        }
    }
    
    /**
     * 将文件保存至服务端
     *
     * @param uploadFile
     * @param fileName   这里的fileName的格式带有上级文件夹目录内容使用$进行分隔
     * @return
     */
    private static String saveFileByNio(MultipartFile uploadFile, String fileName) throws IOException
    {
        // 如果不包含$符号则直接返回null
        if (!fileName.contains("$"))
        {
            return null;
        }
        else
        {
            String[] split = fileName.split(FILE_SPLIT);
            // 这个parentName代表课程名称
            String parentName = split[0];
            // 真实的文件名字
            String fileNameStr = split[1];
            // 这个路径最后是在: src\main\resources\static\classInfo
            String path = FILE_ROOT_PATH + File.separator + "classInfo" + File.separator + parentName + File.separator + fileNameStr;
            File file = new File(path);
            // 判断上级路径是否存在，同时判断是否是文件夹
            if (!file.getParentFile().exists() && !file.getParentFile().isDirectory())
            {
                // 创建当前文件级别路径的上一级目录
                file.getParentFile().mkdirs();
            }
            // 将接收到的文件流持久化到服务器的对应文件中
            uploadFile.transferTo(file);
            
            return path;
        }
    }
    
    /**
     * 文件上级目录和文件名称分隔符
     */
    private static final String FILE_SPLIT = "\\$";
    /**
     * 所有上传文件存储与服务端的根路径
     */
    public static final String FILE_ROOT_PATH = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "static";
}
