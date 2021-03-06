package life.majiang.community.service;

import life.majiang.community.dto.PaginationDTO;
import life.majiang.community.dto.QuestionDTO;
import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.Question;
import life.majiang.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 中间层 组装请求
 */
@Service
public class QuestionService {

    // 注入依赖
    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    public PaginationDTO list(Integer page, Integer size) {

        PaginationDTO paginationDTO = new PaginationDTO();

        Integer totalPage;

        Integer totalCount = questionMapper.count(); // 问题总数

        // 计算总页数  totalCount / size
        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }

        // 页码异常处理
        if (page < 1) {//页码小于1
            page = 1;
        }
        if (page > totalPage) {//页码大于page
            page = totalPage;
        }

        paginationDTO.setPagination(totalPage, page); // 传入 问题总数 当前页面
        //  size * (page -1)  实际页码
        Integer offset = size * (page - 1);

        List<Question> questions = questionMapper.list(offset, size); // 查询所有question  加入分页操作 每一页的列表数
        List<QuestionDTO> questionDTOList = new ArrayList<>(); // new list


        for (Question question : questions) { // 循环Question对象
            User user = userMapper.findByID(question.getCreator()); // 用getCreator获取当前User 返回User对象
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO); // 该工具类的目的是 question的属性快速复制到 questionDTO
            questionDTO.setUser(user); // 返回的DTO对象 需新建list
            questionDTOList.add(questionDTO);

        }

//        return questionDTOList; // 返回的 DTOlist
        paginationDTO.setQuestions(questionDTOList);

        return paginationDTO;
    }

    public PaginationDTO list(Integer userId, Integer page, Integer size) {

        PaginationDTO paginationDTO = new PaginationDTO();

        Integer totalPage;

        Integer totalCount = questionMapper.countByUserId(userId); // 问题总数


        // 计算总页数  totalCount / size
        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }

        // 页码异常处理
        if (page < 1) {//页码小于1
            page = 1;
        }
        if (page > totalPage) {//页码大于page
            page = totalPage;
        }

        paginationDTO.setPagination(totalPage, page); // 传入 问题总数 当前页面
        //  size * (page -1)  实际页码
        Integer offset = size * (page - 1);

        List<Question> questions = questionMapper.listByUserId(userId, offset, size); // 查询所有question  加入分页操作 每一页的列表数
        List<QuestionDTO> questionDTOList = new ArrayList<>(); // new list

        for (Question question : questions) { // 循环Question对象
            User user = userMapper.findByID(question.getCreator()); // 用getCreator获取当前User 返回User对象
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO); // 该工具类的目的是 question的属性快速复制到 questionDTO
            questionDTO.setUser(user); // 返回的DTO对象 需新建list
            questionDTOList.add(questionDTO);

        }

//        return questionDTOList; // 返回的 DTOlist
        paginationDTO.setQuestions(questionDTOList);

        return paginationDTO;
    }

    // 通过 questionserver 调用QuestionDTO 获取id
    public QuestionDTO getById(Integer id) {
        Question question = questionMapper.getById(id);
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question, questionDTO); // 该工具类的目的是 question的属性快速复制到 questionDTO
        User user = userMapper.findByID(question.getCreator()); // 用getCreator获取当前User 返回User对象
        questionDTO.setUser(user); // 添加返回 user属性
        return questionDTO;
    }

    public void createOrUpdate(Question question) {
        if (question.getId() == null) {
            // 插入操作 未找到
            question.setGmtCreate(System.currentTimeMillis()); // set 创建时间
            question.setGmtModified(System.currentTimeMillis()); // set 更新时间
            questionMapper.create(question);
        } else {
            // 更新操作 找到
            question.setGmtModified(System.currentTimeMillis()); // set 更新时间
            questionMapper.update(question);

        }
    }
}
