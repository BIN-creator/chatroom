package com.itheima.mm.service.impl;
import com.itheima.mm.base.BaseService;
import com.itheima.mm.common.QuestionConst;
import com.itheima.mm.dao.*;
import com.itheima.mm.entity.PageResult;
import com.itheima.mm.entity.QueryPageBean;
import com.itheima.mm.exception.MmException;
import com.itheima.mm.pojo.*;
import com.itheima.mm.service.QuestionService;
import com.itheima.mm.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author ：seanyang
 * @date ：Created in 2019/8/12
 * @description ：题目业务实现类
 * @version: 1.0
 */
@Service
@Slf4j
public class QuestionServiceImpl extends BaseService implements QuestionService {
	@Autowired
	private QuestionDao questionDao;

	@Autowired
	private ReviewLogDao reiviewLogDao;

	@Autowired
	private CompanyDao companyDao;

	@Autowired
	private IndustryDao industryDao;

	@Autowired
	private TagDao tagDao;

	@Autowired
	private QuestionItemDao questionItemDao;
	@Override
	public PageResult findByPage(QueryPageBean queryPageBean)  {
		log.info("QuestionService findByPage:{}",queryPageBean);
		if(queryPageBean.getQueryParams() == null){
			queryPageBean.setQueryParams(new HashMap());
		}
		queryPageBean.getQueryParams().put("isClassic",0);
		// 获取数据集
		List<Question> questionList = questionDao.selectIsClassicByPage(queryPageBean);
		// 获取数据记录格式
		Long totalCount = questionDao.selectCountIsClassicByPage(queryPageBean);
		return new PageResult(totalCount,questionList);
	}

	/**
	 * 更新题目信息
	 * 更新选项信息（更新旧选项，添加信息选项）
	 * 更新标签信息 （添加新标签，删除旧关系，插入新关系）
	 */

	@Override
	public PageResult findClassicByPage(QueryPageBean queryPageBean) {
		log.info("QuestionService findClassicByPage:{}",queryPageBean);
		if(queryPageBean.getQueryParams() == null){
			queryPageBean.setQueryParams(new HashMap());
		}
		// 查询精选题目
		queryPageBean.getQueryParams().put("isClassic",1);
		List<Question> questionList = questionDao.selectIsClassicByPage(queryPageBean);
		// 获取每条记录的审核状态
		for (Question question:questionList){
			ReviewLog reviewLog = reiviewLogDao.selectLastByQuestionId(question.getId());
			if (reviewLog == null){
				reviewLog = new ReviewLog();
				reviewLog.setStatus(0);
				reviewLog.setComments("");
				reviewLog.setReviewer("");
				reviewLog.setCreateDate("");
			}
			question.setReviewLog(reviewLog);
		}
		// 获取总记录数
		Long totalCount = questionDao.selectCountIsClassicByPage(queryPageBean);
		return new PageResult(totalCount,questionList);
	}

	@Transactional
	@Override
	public void updateIsClassic(Integer questionId, Integer isClassic) {
		 questionDao.updateIsClassic(questionId,isClassic);

	}

	@Override
	public Question findQuestionById(Integer id) {
		log.info("QuestionService findQuestionById:{}",id);
		// 获取题目基本信息
		// 级联获取题目选项信息
		Question question = questionDao.selectById(id);
		log.debug("question:{}",question);
		// 获取所属标签名称列表
		initQuestionTag(tagDao,question);
		// 根据公司ID，获取所属公司信息
		initQuestionCompany(companyDao,question);
		return question;
	}

	// 获取题目公司及行业信息
	private void initQuestionCompany(CompanyDao companyDao, Question question){
		log.info("QuestionService initQuestionCompany:{}",question.getCompanyId());
		Company company = companyDao.selectByIdForQuestion(question.getCompanyId());
		// 根据公司ID，获取所属行业信息
		List<Industry> industryList = industryDao.selectIndustryListByCompany(question.getCompanyId());
		List<String> industryNameList = new ArrayList<>();
		for (Industry one:industryList){
			industryNameList.add(one.getName());
		}
		company.setIndustryNameList(industryNameList);
		company.setIndustryList(industryList);
		// 设置所属公司(包含所属城市行业)
		question.setCompany(company);
	}

	// 获取题目标签信息
	private void initQuestionTag(TagDao tagDao, Question question){
		log.info("QuestionService initQuestionTag:{}",question.getId());
		List<Tag> tagList = tagDao.selectTagListByQuestionId(question.getId());
		List<String> tagNameList = new ArrayList<>();
		for (Tag one:tagList){
			tagNameList.add(one.getName());
		}
		// 设置标签名称列表
		question.setTagNameList(tagNameList);
		question.setTagList(tagList);
	}


	// 添加题目信息

	// 更新题目选项信息

	// 更新标签信息




	@Transactional
	@Override
	public void updateStatus(Integer questionId, Integer status) {
		log.info("QuestionService updateStatus:questionId-{},status-{}",questionId,status);
		questionDao.updateStatus(questionId,status);
	}

	/**
	 * 大体思路,新增试题,有两种选择,一种是新增信息,另外是更新信息
	 * @param question
	 */
	@Override
	public void addOrUpdate(Question question) {
		log.info("QuestionService question:{}",question);
		// 1 添加题目信息
		// 2.更新题目选项信息
		// 3.更新标签信息
		//表示逻辑的与或非,这里是用的或逻辑关系||,全都是假,最后才为假
		Integer result =0;
		question.setCompanyId(question.getCompany().getId());
		if (question.getId()==null ||question.getId()==0){
			//为什么迷惑,因为不理解前端发过来什么,前端给的信息,多表查询的时候,涉及到多表信息的混乱
			question.setCreateDate(DateUtils.parseDate2String(new Date()));
			question.setStatus(QuestionConst.Status.PRE_PUBLISH.ordinal());
			question.setReviewStatus(QuestionConst.ReviewStatus.PRE_REVIEW.ordinal());
			question.setIsClassic(0);
			result =questionDao.add(question);
		}else {
			result=questionDao.update(question);
		}
	}



	/**
	 * addorupdate是两个意思,新增和更新,实际上新增试题这个操作,
	 * 加入的是题目信息,同时更新默认的题目选项信息
	 * 对标签信息进行更新
	 *
	 *
	 * @param sqlSession
	 * @param question
	 */
//	public void addOrUpdateQuestion(Question question){
//		//...老师给的文档是在dao层实现的值的传递,service只是用来处理业务,判断是否是null,
//		//用来检查是否是新增或者还是更新
//		//如果是新增就直接走的是dao层,用xml文件处理这个关系,并不是setget这种
//		//是直接插入的
//		log.debug("QuestionSevice addOrUpdateQuestion:{}"+question);
//		question.setCatalogId(question.getCatalogId());//目录名字,根据id查找?
//		question.setCourseId(question.getCourseId());//学科名字
//		question.setCompanyId(question.getCompanyId());//公司名字
//		question.setTitle(question.getTitle());//标题
//		question.setType(question.getType());//题目类型
//		question.setAnalysis(question.getAnalysis());//答案解析
//		question.setAnalysisVideo(question.getAnalysisVideo());//视频解析地址
//		question.setRemark(question.getRemark());//备注
//		question.setSubject(question.getSubject());//题干
//		//这样写数据压根不会传到数据库啊,本来就是数据到这样,再传给dao层,dao层再通过映射文件
//		//操控数据库
//
//	}
	//文档中的dao参数,还有sqlsession就是坑
//	private Integer addOrUpdateQuestion(QuestionDao questionDao, Question question){
//		log.debug("QuestionService addOrUpdateQuestion:{}",question);
//		Integer result = 0;
	//这样看来很多信息,无法通过dao层直接获取,所以有的信息必须通过set赋值
//		question.setCompanyId(question.getCompany().getId());
//		if(question.getId()==null  || question.getId()==0 ){
//			// 初始化题目相关信息
//			question.setCreateDate(DateUtils.parseDate2String(new Date()));
			//通过时间类工具类,配置创建者时间,这不是增加界面吗,为什么还有创建时间
//			// 通过枚举下标，获取的值对应数据需要的值
//			// 初始化题目状态及审核状态
//			question.setStatus(QuestionConst.Status.PRE_PUBLISH.ordinal());
	//ordinal表示获取枚举中的对应的序列值,这么看为什么不直接设置,因为你不知道直接设置的结果
	//所谓的枚举压根就是一个集合,只不过将其中的所有元素都放在里面,没有的数字会报错
//			question.setReviewStatus(QuestionConst.ReviewStatus.PRE_REVIEW.ordinal());
//			question.setIsClassic(0);
//			result = questionDao.add(question);
//		}else{
//			result = questionDao.update(question);
//		}
//		return result;
//	}
//
	// 更新题目选项信息
	private void updateQuestionItemList(QuestionItemDao questionItemDao, Question question){
		log.info("QuestionService updateQuestionItemList:{}",question.getQuestionItemList());
		for (QuestionItem questionItem:question.getQuestionItemList()){
			// 设置应对的题目ID
			questionItem.setQuestionId(question.getId());
			if(questionItem.getContent() == null || questionItem.getContent().length() ==0){
				if(questionItem.getImgUrl() == null || questionItem.getImgUrl().length() == 0)
					continue;
			}
			if(questionItem.getId() == null || questionItem.getId() ==0){
				// 新增
				questionItemDao.addQuestionItem(questionItem);
			}else{
				// 更新
				questionItemDao.updateQuestionItem(questionItem);
			}
		}
	}
//
	// 更新标签信息
	private void updateTagList(TagDao tagDao , Question question){
		log.info("QuestionService updateTagList:{}",question.getTagList());
		// 删除之前的题目标签关系
		tagDao.deleteTagByQuestionId(question.getId());
		//这里发现两个弱点
		for (Tag tag:question.getTagList()){
			Map<String,Object> mapQuestionTag = new HashMap<>();
			mapQuestionTag.put("questionId",question.getId());
			Integer tagId = 0;
			// 新增标签
			if(tag.getId() == 0){
				tag.setStatus(QuestionConst.TagStatus.ENABLE.ordinal());
				tag.setUserId(question.getUserId());
				tag.setCreateDate(DateUtils.parseDate2String(new Date()));
				tag.setCourseId(question.getCourseId());
				tagDao.addTag(tag);
			}
			mapQuestionTag.put("tagId",tag.getId());
			tagDao.addTagByQuestionId(mapQuestionTag);
		}
	}
}
