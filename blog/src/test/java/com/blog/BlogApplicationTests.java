package com.blog;




import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.blog.dto.Ad.AdDto;
import com.blog.mapper.ArticleTypeMapper;
import com.blog.pojo.*;
import com.blog.service.*;
import com.blog.vo.ArticleTypeTreeVo;
import com.blog.vo.ArticleTypeVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;


@SpringBootTest
class BlogApplicationTests {
    @Autowired
    DataSource dataSource;
    @Autowired
    UserService userService;
    @Autowired
    ArticleTypeMapper articleTypeMapper;
    @Autowired
    ArticleTypeService articleTypeService;
    @Autowired
    ArticleTagListService articleTagListService;
    @Autowired
    ArticleService articleService;
    @Autowired
    ArticleTagService articleTagService;
    @Autowired
    LinkService linkService;
    @Autowired
    AdService adService;
    @Autowired
    AdTypeService adTypeService;
    @Autowired
    AdminService adminService;

    @Test
    void test() throws SQLException {

    }

    @Test
    void te(){
        List<ArticleTypeTreeVo> articleTypeList = articleTypeService.getIndexArticleTypeList(null);
        if (CollUtil.isNotEmpty(articleTypeList)) {
            for (ArticleTypeTreeVo articleTypeTreeVo : articleTypeList) {
                articleTypeTreeVo.setArticleTypeTreeVoList(articleTypeService.getIndexArticleTypeList(articleTypeTreeVo.getArticleTypeId()));
            }
            for (ArticleTypeTreeVo articleTypeTreeVo : articleTypeList) {
                System.out.println(articleTypeTreeVo);
            }
        }
    }
    /**
     *
     * 模拟文章数据
     */

    @Test
    public void addArticleData() {
        List<User> users = userService.list();


        List<ArticleType> articleTypeList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            ArticleType articleType = new ArticleType();
            articleType.setArticleTypeId(i+"a文章类型id:");
            articleType.setArticleTypeName("a文章分类" + i);
            articleType.setArticleTypeSort(10);
            articleType.setArticleTypeAddTime(new Date());
            articleTypeList.add(articleType);
        }
        articleTypeService.saveBatch(articleTypeList);

        ArrayList<Article> arrayList = new ArrayList<>();
        int b=1;
        for (ArticleType articleType : articleTypeList) {
            for (int i = 0; i < 6; i++) {
                Article article = new Article();
                article.setArticleId(b+"文章id");
                article.setArticleTypeId(articleType.getArticleTypeId());
                article.setUserId(users.get(ThreadLocalRandom.current().nextInt(users.size())).getUserId());
                article.setArticleTitle("b文章标题：" + i);
                article.setArticleAddTime(DateUtil.date());
                article.setArticleContext("b文章内容：" + ThreadLocalRandom.current().nextInt(1000));
                article.setArticleGoodNumber(0);
                article.setArticleLookNumber(0);
                article.setArticleCollectionNumber(0);
                arrayList.add(article);
                b++;
            }
        }
        articleService.saveBatch(arrayList,50);


        ArrayList<ArticleTag> articleTags = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ArticleTag articleTag = new ArticleTag();
            articleTag.setArticleTagId("c文章标签id："+i);
            articleTag.setArticleTagName("c文章标签："+ i);
            articleTag.setArticleTagAddTime(DateUtil.date());
            articleTags.add(articleTag);
        }
        articleTagService.saveBatch(articleTags);


        ArrayList<ArticleTagList> articleTagLists = new ArrayList<>();
        int c=1;
        for (ArticleTag articleTag : articleTags) {
            for (int i = 0; i < 3; i++) {
                ArticleTagList articleTagList = new ArticleTagList();
                articleTagList.setArticleTagListId("d文章标签表id"+c);
                articleTagList.setArticleId(arrayList.get(ThreadLocalRandom.current().nextInt(arrayList.size())).getArticleId());
                articleTagList.setArticleTagId(articleTag.getArticleTagId());
                articleTagLists.add(articleTagList);
                c++;
            }
        }
        articleTagListService.saveBatch(articleTagLists, 50);

    }
    @Test
    public void addLinkData() {
        ArrayList<Link> links = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Link link = new Link();
            link.setLinkTitle("百度" + i);
            link.setLinkUrl("https://www.baidu.com");
            link.setLinkLogoUrl("https://www.baidu.com/img/PCtm_d9c8750bed0b3c7d089fa7d55720d6cf.png");
            link.setLinkSort(10);
            link.setLinkAddTime(DateUtil.date());
            links.add(link);

        }
        linkService.saveBatch(links);
    }
    @Test
    public void addAdData() {
        ArrayList<AdType> adTypes = new ArrayList<>();

        AdType adType1 = new AdType();
        adType1.setAdTypeTitle("首页轮播图广告");
        adType1.setAdTypeTag("homeAd");
        adType1.setAdTypeSort(0);
        adType1.setAdTypeAddTime(DateUtil.date());
        adTypes.add(adType1);

        AdType adType2 = new AdType();
        adType2.setAdTypeTitle("文章页面广告");
        adType2.setAdTypeTag("articleAd");
        adType2.setAdTypeSort(1);
        adType2.setAdTypeAddTime(DateUtil.date());
        adTypes.add(adType2);
        adTypeService.saveBatch(adTypes);


/**
 * https://imgtu.com/i/TUCl5j
 * https://imgtu.com/i/TUCuqS
 * https://imgtu.com/i/TUCMVg
 * https://imgtu.com/i/TUCQaQ
 */
        Ad ad1 = new Ad();
        ad1.setAdTypeId(adType1.getAdTypeId());
        ad1.setAdTitle("广告1");
        ad1.setAdLinkUrl("https://imgtu.com/i/TUCQaQ");
        ad1.setAdSort(10);
        ad1.setAdBeginTime(DateUtil.date());
        ad1.setAdEndTime(DateUtil.parseDateTime("2022-05-05 12:12:12"));
        ad1.setAdAddTime(DateUtil.date());


        Ad ad2 = new Ad();
        ad2.setAdTypeId(adType1.getAdTypeId());
        ad2.setAdTitle("广告2");
        ad2.setAdLinkUrl("https://imgtu.com/i/TUCl5j");
        ad2.setAdSort(10);
        ad2.setAdBeginTime(DateUtil.date());
        ad2.setAdEndTime(DateUtil.parseDateTime("2022-05-05 12:12:12"));
        ad2.setAdAddTime(DateUtil.date());

        Ad ad3 = new Ad();
        ad3.setAdTypeId(adType2.getAdTypeId());
        ad3.setAdTitle("广告3");

        ad3.setAdLinkUrl("https://imgtu.com/i/TUCuqS");
        ad3.setAdSort(10);
        ad3.setAdBeginTime(DateUtil.date());
        ad3.setAdEndTime(DateUtil.parseDateTime("2022-05-05 12:12:12"));
        ad3.setAdAddTime(DateUtil.date());

        Ad ad4 = new Ad();
        ad4.setAdTypeId(adType2.getAdTypeId());
        ad4.setAdTitle("广告4");
        ad4.setAdLinkUrl("https://imgtu.com/i/TUCMVg");
        ad4.setAdSort(10);
        ad4.setAdBeginTime(DateUtil.date());
        ad4.setAdEndTime(DateUtil.parseDateTime("2022-05-05 12:12:12"));
        ad4.setAdAddTime(DateUtil.date());

        ArrayList<Ad> ads = new ArrayList<>();
        ads.add(ad1);
        ads.add(ad2);
        ads.add(ad3);
        ads.add(ad4);

        adService.saveBatch(ads);
    }

    @Test
    public void aVoid(){

        Admin admin = new Admin();
        admin.setAdminName("admin");
        admin.setAdminPassword(SecureUtil.md5(admin.getAdminName()+"admin"));
        String pas="admin";
        String adminPassword = admin.getAdminPassword();
        boolean equals = SecureUtil.md5(admin.getAdminName() + admin.getAdminPassword()).equals(admin.getAdminName()+pas);
        System.out.println(equals);
        System.out.println(adminPassword);
        System.out.println(admin.getAdminName()+pas);
//        adminService.save(admin);
    }
}
