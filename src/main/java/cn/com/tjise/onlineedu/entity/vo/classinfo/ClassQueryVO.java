package cn.com.tjise.onlineedu.entity.vo.classinfo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author admin
 * 课程分页查询条件实体类
 */
@Data
@ToString
@ApiModel(value = "class查询实体", description = "按照 价格、课程状态、课程创建时间 分页查询的实体")
public class ClassQueryVO implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty(value = "价格上限")
    private Double priceHighLimit;
    @ApiModelProperty(value = "价格下限")
    private Double priceLowLimit;
    @ApiModelProperty(value = "课程状态(1：未开课；2：已开课；3：已结课)")
    private Integer status;
    @ApiModelProperty(value = "开始时间")
    private String beginTime;
    @ApiModelProperty(value = "结束时间")
    private String endTime;
}
