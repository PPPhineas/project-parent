package com.hgt.project.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hgt.project.dao.entity.ProjectData;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 项目文件 Mapper 接口
 * </p>
 *
 * @author xavier
 * @since 2019-12-19
 */
public interface ProjectDataMapper extends BaseMapper<ProjectData> {

    /**
     * 获取本地路径
     * @param id 项目资料id
     * @return 项目资料本地路径
     */
    @Select("SELECT \n" +
            "    group_concat(T2.address separator '/') as path\n" +
            "FROM\n" +
            "    (SELECT \n" +
            "        @r AS _id,\n" +
            "            (SELECT \n" +
            "                    @r:=parent_id\n" +
            "                FROM\n" +
            "                    project.project_data\n" +
            "                WHERE\n" +
            "                    id = _id) AS parent_id,\n" +
            "" +
            "" +
            "            @l:=@l + 1 AS lvl\n" +
            "    FROM\n" +
            "        (SELECT @r:=#{id}, @l:=0) vars, project.project_data h\n" +
            "    WHERE\n" +
            "        @r <> 0) T1\n" +
            "        JOIN\n" +
            "    project.project_data T2 ON T1._id = T2.id\n" +
            "ORDER BY T1.lvl DESC")
    String getLocalPath(Long id);
}
