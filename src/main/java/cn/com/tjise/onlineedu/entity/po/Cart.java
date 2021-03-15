package cn.com.tjise.onlineedu.entity.po;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @program: onlineedu
 * @description: 购物车实体
 * @author: admin
 * @create: 2021-03-15 19:47
 **/

@Data
@ToString
public class Cart implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    private Integer id;
    
    private String classId;
    
    private String studentId;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
    
    private Integer status;
    
}
