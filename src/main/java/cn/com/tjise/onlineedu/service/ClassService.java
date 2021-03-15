package cn.com.tjise.onlineedu.service;

import cn.com.tjise.onlineedu.entity.po.Class;
import cn.com.tjise.onlineedu.entity.po.UStudent;
import cn.com.tjise.onlineedu.entity.vo.classinfo.ClassQueryVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author admin
 * @since 2021-03-11
 */
public interface ClassService extends IService<Class>
{
    /**
     * 根据课程编号删除课程
     * @param id
     * @return
     */
    boolean deleteById(String id);
    
    /**
     * 根据课程编号更新课程信息
     * @param classInfo 传入更新课程信息实体
     * @return
     */
    boolean updateByClassId(Class classInfo);
    
    /**
     * 根据条件分页查询课程
     * @param page
     * @param classQueryVO
     */
    void pageSearchByCondition(Page<Class> page, ClassQueryVO classQueryVO);
    
    /**
     * 根据课程id查询当前课程中的所有学生信息
     * @param classId
     * @return
     */
    List<UStudent> queryStuByClassId(String classId);
    
    /**
     * 根据课程id查询课程剩余名额
     * @param classId
     * @return
     */
    int queryQuotaOfPeople(String classId);
    
    /**
     * 根据classid查询独一份的课程信息
     * @param classId
     * @return
     */
    default Class queryByClassId(String classId)
    {
        QueryWrapper<Class> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("class_id", classId);
        return getOne(queryWrapper);
    }
}
