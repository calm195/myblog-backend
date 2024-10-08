import org.junit.Test;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author chrissy
 * @description
 * @date 2024/7/24 22:27
 */
public class TestSpEL {
    public class Inventor {

        public String name;
        private String nationality;
        private String[] inventions;
        private Date birthdate;
        private PlaceOfBirth placeOfBirth;

        public Inventor(String nikolaTesla, Date time, String serbian) {
            this.name = nikolaTesla;
            birthdate = time;
            nationality = serbian;
        }
        // 省略其它方法
    }
    public class PlaceOfBirth {

        private String city;
        private String country;
        // 省略其它方法
    }


    @Test
    public void test_String_method() {
        // 1 定义解析器
        SpelExpressionParser parser = new SpelExpressionParser();
        // 2 使用解析器解析表达式
        Expression exp = parser.parseExpression("'Hello World'.concat('!')");
        // 3 获取解析结果
        String value = (String) exp.getValue();
        System.out.println(value);
        exp = parser.parseExpression("'Hello World'.bytes");
        byte[] bytes = (byte[]) exp.getValue();
        exp = parser.parseExpression("'Hello World'.bytes.length");
        int length = (Integer) exp.getValue();
        System.out.println("length: " + length);

        //  调用
        exp = parser.parseExpression("new String('hello world').toUpperCase()");
        System.out.println("大写: " + exp.getValue());

    }

    @Test
    public void test_over_root() {
        // 创建  Inventor 对象
        GregorianCalendar c = new GregorianCalendar();
        c.set(1856, 7, 9);
        Inventor tesla = new Inventor("Nikola Tesla", c.getTime(), "Serbian");
        // 1 定义解析器
        ExpressionParser parser = new SpelExpressionParser();
        // 指定表达式
        Expression exp = parser.parseExpression("name");
        // 在 tesla对象上解析
        String name = (String) exp.getValue(tesla);
        System.out.println(name); // Nikola Tesla

        exp = parser.parseExpression("name == 'Nikola Tesla'");
        // 在 tesla对象上解析并指定返回结果
        boolean result = exp.getValue(tesla, Boolean.class);
        System.out.println(result); // true
    }

    @Test
    public void test_time(){
        long now = System.currentTimeMillis();
        System.out.println(now);
    }

}
