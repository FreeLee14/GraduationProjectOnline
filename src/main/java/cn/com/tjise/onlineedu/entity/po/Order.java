package cn.com.tjise.onlineedu.entity.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author admin
 * @since 2021-03-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Order对象", description="")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "订单号")
    private String orderId;

    @ApiModelProperty(value = "学生id")
    private String studentId;

    @ApiModelProperty(value = "课程id")
    private String classId;

    @ApiModelProperty(value = "订单创建时间")
    @TableField("createTime")
    private Date createtime;

    @ApiModelProperty(value = "订单更新时间")
    @TableField("updateTime")
    private Date updatetime;

    @ApiModelProperty(value = "订单状态(0 未支付; 1 已支付; 2 废弃订单)")
    private Integer status;
    
    @ApiModelProperty(value = "逻辑删除 1（true）1已删除，0（false）未删除")
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer deleted;

}
