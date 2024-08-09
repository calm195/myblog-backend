import com.chrissy.model.enums.user.LoginTypeEnum;
import org.junit.Test;

/**
 * @author chrissy
 * @description 测试
 * @date 2024/8/7 21:46
 */

public class TestLoginTypeEnum {
    @Test
    public void enumTypeTest(){
        for (LoginTypeEnum one : LoginTypeEnum.values()) {
            System.out.println(one.ordinal());
        }
    }
}
