import com.chrissy.model.enums.article.ArticleTypeEnum;
import org.junit.Test;

/**
 * @author chrissy
 * @description
 * @date 2024/8/9 13:46
 */
public class TestArticleTypeEnum {
    @Test
    public void articleTypeEnumTest(){
        System.out.println(ArticleTypeEnum.BLOG);
        System.out.println(ArticleTypeEnum.BLOG.getCode());
        System.out.println(ArticleTypeEnum.BLOG.getDesc());
//        System.out.println((ArticleTypeEnum) 1);
    }
}
